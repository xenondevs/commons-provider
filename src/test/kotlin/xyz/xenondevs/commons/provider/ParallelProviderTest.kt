package xyz.xenondevs.commons.provider

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import xyz.xenondevs.commons.provider.util.ObservableList
import xyz.xenondevs.commons.tuple.Tuple10
import xyz.xenondevs.commons.tuple.Tuple2
import xyz.xenondevs.commons.tuple.Tuple3
import xyz.xenondevs.commons.tuple.Tuple4
import xyz.xenondevs.commons.tuple.Tuple5
import xyz.xenondevs.commons.tuple.Tuple6
import xyz.xenondevs.commons.tuple.Tuple7
import xyz.xenondevs.commons.tuple.Tuple8
import xyz.xenondevs.commons.tuple.Tuple9
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.test.assertEquals

class ParallelProviderTest {
    
    companion object {
        
        @JvmStatic
        fun rootMiddleLeafThreadConfigurations(): List<Arguments> = listOf(
            Arguments.of(10, 10, 10),
            Arguments.of(0, 10, 10),
            Arguments.of(10, 0, 10),
            Arguments.of(10, 10, 0),
            Arguments.of(1, 1, 1),
            Arguments.of(0, 1, 1),
            Arguments.of(1, 0, 1),
            Arguments.of(1, 1, 0),
        )
        
    }
    
    @MethodSource("rootMiddleLeafThreadConfigurations")
    @ParameterizedTest
    fun `test parallel writes to bidirectional transforming provider`(
        nRootThreads: Int,
        nMiddleThreads: Int,
        nLeafThreads: Int
    ) {
        val root = mutableProvider(0)
        
        val middle = root.map(
            { it * 2 }, { it / 2 }
        )
        
        val leaf1 = middle.map(
            { it + 1 },
            { it - 1 }
        )
        val leaf2 = middle.map(
            { it + 2 },
            { it - 2 }
        )
        
        fun updateRoot() {
            for (i in -100_000..100_000) {
                root.set(i)
            }
        }
        
        fun updateMiddle() {
            for (i in -100_000..100_000) {
                middle.set(i)
            }
        }
        
        fun updateLeaf1() {
            for (i in -100_000..100_000) {
                leaf1.set(i)
            }
        }
        
        val rootThreads = Array(nRootThreads) { Thread { updateRoot() } }
        val middleThreads = Array(nMiddleThreads) { Thread { updateMiddle() } }
        val leafThreads = Array(nLeafThreads) { Thread { updateLeaf1() } }
        
        listOf(*rootThreads, *middleThreads, *leafThreads).shuffled()
            .onEach { it.start() }
            .onEach { it.join() }
        
        
        // 3 possible scenarios: root thread won, middle thread won, leaf thread won
        // for each scenario, assert that the other providers are in the appropriate state
        
        if (root.get() == 100_000) {
            // a root thread won
            assertEquals(200_000, middle.get())
            assertEquals(200_001, leaf1.get())
            assertEquals(200_002, leaf2.get())
        } else if (middle.get() == 100_000) {
            // a middle thread won
            assertEquals(50_000, root.get())
            assertEquals(100_001, leaf1.get())
            assertEquals(100_002, leaf2.get())
        } else if (leaf1.get() == 100_000) {
            // a leaf1 thread won
            assertEquals(49_999, root.get())
            assertEquals(99_999, middle.get())
            assertEquals(100_001, leaf2.get())
        } else {
            throw AssertionError()
        }
    }
    
    @Test
    fun testCombinedMappingProviderInit() {
        val a = mutableProvider(1)
        val b = mutableProvider(1)
        val c = mutableProvider(1)
        val d = mutableProvider(1)
        val e = mutableProvider(1)
        val f = mutableProvider(1)
        val g = mutableProvider(1)
        val h = mutableProvider(1)
        val i = mutableProvider(1)
        val j = mutableProvider(1)
        
        runParallel {
            combinedProvider(
                a, b, c, d, e, f, g, h, i, j
            ) { a, b, c, d, e, f, g, h, i, j ->
                a + b + c + d + e + f + g + h + i + j
            }
        }
    }
    
    private fun runParallel(nThreads: Int = 100, run: () -> Unit) {
        var failed = false
        val threads = Array(nThreads) {
            Thread {
                try {
                    run()
                } catch (t: Throwable) {
                    failed = true
                    throw t
                }
            }
        }
        
        threads.forEach(Thread::start)
        threads.forEach(Thread::join)
        
        if (failed)
            throw AssertionError("Failed")
    }
    
    @Test
    fun `test for lost update on provider creation via map`() {
        testForProviderCreationLostUpdate(0, 1, 2) { provider -> provider.map { it + 1 } }
    }
    
    @Test
    fun `test for lost update on provider creation via strongMap`() {
        testForProviderCreationLostUpdate(0, 1, 2) { provider -> provider.strongMap { it + 1 } }
    }
    
    @Test
    fun `test for lost update on provider creation via immediateFlatMap`() {
        testForProviderCreationLostUpdate(0, 1, 2) { provider ->
            val selection = listOf(provider(1), provider(2))
            provider.immediateFlatMap { selection[it] } 
        }
    }
    
    @Test
    fun `test for lost update on provider creation via strongImmediateFlatMap`() {
        testForProviderCreationLostUpdate(0, 1, 2) { provider ->
            val selection = listOf(provider(1), provider(2))
            provider.strongImmediateFlatMap { selection[it] }
        }
    }
    
    @Test
    fun `test for lost update on provider creation via immediateFlatMapMutable`() {
        testForProviderCreationLostUpdate(0, 1, 2) { provider ->
            val selection = listOf(mutableProvider(1), mutableProvider(2))
            provider.immediateFlatMapMutable { selection[it] }
        }
    }
    
    @Test
    fun `test for lost update on provider creation via strongImmediateFlatMapMutable`() {
        testForProviderCreationLostUpdate(0, 1, 2) { provider ->
            val selection = listOf(mutableProvider(1), mutableProvider(2))
            provider.strongImmediateFlatMapMutable { selection[it] } 
        }
    }
    
    @Test
    fun `test for lost update on provider creation via flatMap`() {
        testForProviderCreationLostUpdate(0, 1, 2) { provider ->
            val selection = listOf(provider(1), provider(2))
            provider.flatMap { selection[it] } 
        }
    }
    
    @Test
    fun `test for lost update on provider creation via strongFlatMap`() {
        testForProviderCreationLostUpdate(0, 1, 2) { provider ->
            val selection = listOf(provider(1), provider(2))
            provider.strongFlatMap { selection[it] }
        }
    }
    
    @Test
    fun `test for lost update on provider creation via mapObserved`() {
        testForProviderCreationLostUpdate(
            mutableListOf(),
            mutableListOf("a"),
            mutableListOf("a")
        ) { provider -> provider.mapObserved(::ObservableList) }
    }
    
    @Test
    fun `test for lost update on provider creation via strongMapObserved`() {
        testForProviderCreationLostUpdate(
            mutableListOf(),
            mutableListOf("a"),
            mutableListOf("a")
        ) { provider -> provider.strongMapObserved(::ObservableList) }
    }
    
    @Test
    fun `test for lost update on provider creation via combinedProvider`() {
        val b = provider("b")
        testForProviderCreationLostUpdate("", "a", "ab") { combinedProvider(it, b) { a, b -> a + b } }
    }
    
    @Test
    fun `test for lost update on provider creation via strongCombinedProvider`() {
        val b = provider("b")
        testForProviderCreationLostUpdate("", "a", "ab") { strongCombinedProvider(it, b) { a, b -> a + b } }
    }
    
    @Test
    fun `test for lost update on provider creation via list combinedProvider`() {
        testForProviderCreationLostUpdate(0, 1, listOf(1)) { combinedProvider(listOf(it)) }
    }
    
    @Test
    fun `test for lost update on provider creation via list strongCombinedProvider`() {
        testForProviderCreationLostUpdate(0, 1, listOf(1)) { strongCombinedProvider(listOf(it)) }
    }
    
    @Test
    fun `test for lost update on provider creation via list mapping combinedProvider`() {
        testForProviderCreationLostUpdate(0, 1, 1) { combinedProvider(listOf(it), List<Int>::sum) }
    }
    
    @Test
    fun `test for lost update on provider creation via list mapping strongCombinedProvider`() {
        testForProviderCreationLostUpdate(0, 1, 1) { strongCombinedProvider(listOf(it), List<Int>::sum)  }
    }
    
    @ParameterizedTest
    @ValueSource(ints = [2, 3, 4, 5, 6, 7, 8, 9, 10])
    fun `test for lost update on provider creation via fixed-arity mapping combinedProvider`(n: Int) {
        val extras = List(n - 1) { provider(1) }
        testForProviderCreationLostUpdate(0, 1, n) {
            createMappingCombinedProvider(it, extras, weak = true)
        }
    }
    
    @ParameterizedTest
    @ValueSource(ints = [2, 3, 4, 5, 6, 7, 8, 9, 10])
    fun `test for lost update on provider creation via fixed-arity mapping strongCombinedProvider`(n: Int) {
        val extras = List(n - 1) { provider(1) }
        testForProviderCreationLostUpdate(0, 1, n) {
            createMappingCombinedProvider(it, extras, weak = false)
        }
    }
    
    @Test
    fun `test for lost update on provider creation via list decompose`() {
        testForProviderCreationLostUpdate(0, 1, 1) { provider ->
            provider.decompose(1, { listOf(it) }, { it[0] })[0]
        }
    }
    
    @Test
    fun `test for lost update on provider creation via list strongDecompose`() {
        testForProviderCreationLostUpdate(0, 1, 1) { provider ->
            provider.strongDecompose(1, { listOf(it) }, { it[0] })[0]
        }
    }
    
    @ParameterizedTest
    @ValueSource(ints = [2, 3, 4, 5, 6, 7, 8, 9, 10])
    fun `test for lost update on provider creation via fixed-arity decompose`(n: Int) {
        testForProviderCreationLostUpdate(0, 1, 1) {
            createDecomposedProvider(it, n, weak = true)
        }
    }
    
    @ParameterizedTest
    @ValueSource(ints = [2, 3, 4, 5, 6, 7, 8, 9, 10])
    fun `test for lost update on provider creation via fixed-arity strongDecompose`(n: Int) {
        testForProviderCreationLostUpdate(0, 1, 1) {
            createDecomposedProvider(it, n, weak = false)
        }
    }
    
    private fun createMappingCombinedProvider(a: Provider<Int>, extras: List<Provider<Int>>, weak: Boolean): Provider<Int> {
        val n = extras.size + 1
        return if (weak) {
            when (n) {
                2 -> combinedProvider(a, extras[0]) { a, b -> a + b }
                3 -> combinedProvider(a, extras[0], extras[1]) { a, b, c -> a + b + c }
                4 -> combinedProvider(a, extras[0], extras[1], extras[2]) { a, b, c, d -> a + b + c + d }
                5 -> combinedProvider(a, extras[0], extras[1], extras[2], extras[3]) { a, b, c, d, e -> a + b + c + d + e }
                6 -> combinedProvider(a, extras[0], extras[1], extras[2], extras[3], extras[4]) { a, b, c, d, e, f -> a + b + c + d + e + f }
                7 -> combinedProvider(a, extras[0], extras[1], extras[2], extras[3], extras[4], extras[5]) { a, b, c, d, e, f, g -> a + b + c + d + e + f + g }
                8 -> combinedProvider(a, extras[0], extras[1], extras[2], extras[3], extras[4], extras[5], extras[6]) { a, b, c, d, e, f, g, h -> a + b + c + d + e + f + g + h }
                9 -> combinedProvider(a, extras[0], extras[1], extras[2], extras[3], extras[4], extras[5], extras[6], extras[7]) { a, b, c, d, e, f, g, h, i -> a + b + c + d + e + f + g + h + i }
                10 -> combinedProvider(a, extras[0], extras[1], extras[2], extras[3], extras[4], extras[5], extras[6], extras[7], extras[8]) { a, b, c, d, e, f, g, h, i, j -> a + b + c + d + e + f + g + h + i + j }
                else -> throw UnsupportedOperationException()
            }
        } else {
            when (n) {
                2 -> strongCombinedProvider(a, extras[0]) { a, b -> a + b }
                3 -> strongCombinedProvider(a, extras[0], extras[1]) { a, b, c -> a + b + c }
                4 -> strongCombinedProvider(a, extras[0], extras[1], extras[2]) { a, b, c, d -> a + b + c + d }
                5 -> strongCombinedProvider(a, extras[0], extras[1], extras[2], extras[3]) { a, b, c, d, e -> a + b + c + d + e }
                6 -> strongCombinedProvider(a, extras[0], extras[1], extras[2], extras[3], extras[4]) { a, b, c, d, e, f -> a + b + c + d + e + f }
                7 -> strongCombinedProvider(a, extras[0], extras[1], extras[2], extras[3], extras[4], extras[5]) { a, b, c, d, e, f, g -> a + b + c + d + e + f + g }
                8 -> strongCombinedProvider(a, extras[0], extras[1], extras[2], extras[3], extras[4], extras[5], extras[6]) { a, b, c, d, e, f, g, h -> a + b + c + d + e + f + g + h }
                9 -> strongCombinedProvider(a, extras[0], extras[1], extras[2], extras[3], extras[4], extras[5], extras[6], extras[7]) { a, b, c, d, e, f, g, h, i -> a + b + c + d + e + f + g + h + i }
                10 -> strongCombinedProvider(a, extras[0], extras[1], extras[2], extras[3], extras[4], extras[5], extras[6], extras[7], extras[8]) { a, b, c, d, e, f, g, h, i, j -> a + b + c + d + e + f + g + h + i + j }
                else -> throw UnsupportedOperationException()
            }
        }
    }
    
    private fun createDecomposedProvider(parent: MutableProvider<Int>, n: Int, weak: Boolean): Provider<Int> = if (weak) {
        when (n) {
            2 -> parent.decompose({ Tuple2(it, it) }, { a, _ -> a }).a
            3 -> parent.decompose({ Tuple3(it, it, it) }, { a, _, _ -> a }).a
            4 -> parent.decompose({ Tuple4(it, it, it, it) }, { a, _, _, _ -> a }).a
            5 -> parent.decompose({ Tuple5(it, it, it, it, it) }, { a, _, _, _, _ -> a }).a
            6 -> parent.decompose({ Tuple6(it, it, it, it, it, it) }, { a, _, _, _, _, _ -> a }).a
            7 -> parent.decompose({ Tuple7(it, it, it, it, it, it, it) }, { a, _, _, _, _, _, _ -> a }).a
            8 -> parent.decompose({ Tuple8(it, it, it, it, it, it, it, it) }, { a, _, _, _, _, _, _, _ -> a }).a
            9 -> parent.decompose({ Tuple9(it, it, it, it, it, it, it, it, it) }, { a, _, _, _, _, _, _, _, _ -> a }).a
            10 -> parent.decompose({ Tuple10(it, it, it, it, it, it, it, it, it, it) }, { a, _, _, _, _, _, _, _, _, _ -> a }).a
            else -> throw UnsupportedOperationException()
        }
    } else {
        when (n) {
            2 -> parent.strongDecompose({ Tuple2(it, it) }, { a, _ -> a }).a
            3 -> parent.strongDecompose({ Tuple3(it, it, it) }, { a, _, _ -> a }).a
            4 -> parent.strongDecompose({ Tuple4(it, it, it, it) }, { a, _, _, _ -> a }).a
            5 -> parent.strongDecompose({ Tuple5(it, it, it, it, it) }, { a, _, _, _, _ -> a }).a
            6 -> parent.strongDecompose({ Tuple6(it, it, it, it, it, it) }, { a, _, _, _, _, _ -> a }).a
            7 -> parent.strongDecompose({ Tuple7(it, it, it, it, it, it, it) }, { a, _, _, _, _, _, _ -> a }).a
            8 -> parent.strongDecompose({ Tuple8(it, it, it, it, it, it, it, it) }, { a, _, _, _, _, _, _, _ -> a }).a
            9 -> parent.strongDecompose({ Tuple9(it, it, it, it, it, it, it, it, it) }, { a, _, _, _, _, _, _, _, _ -> a }).a
            10 -> parent.strongDecompose({ Tuple10(it, it, it, it, it, it, it, it, it, it) }, { a, _, _, _, _, _, _, _, _, _ -> a }).a
            else -> throw UnsupportedOperationException()
        }
    }
    
    private fun <T, R> testForProviderCreationLostUpdate(
        initialRootValue: T,
        newRootValue: T,
        expectedResult: R,
        createResultProvider: (MutableProvider<T>) -> Provider<R>
    ) {
        val executor = Executors.newFixedThreadPool(100)
        repeat(100_000) {
            val root: MutableProvider<T> = mutableProvider(initialRootValue)
            val result: Future<Provider<R>> = executor.submit<Provider<R>> { createResultProvider(root) }
            executor.submit { root.set(newRootValue) }.get()
            
            assertEquals(expectedResult, result.get().get(), "Iteration $it")
        }
    }
    
}