package xyz.xenondevs.commons.provider.mutable

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import xyz.xenondevs.commons.provider.MutableProvider
import xyz.xenondevs.commons.provider.mutableProvider
import xyz.xenondevs.commons.tuple.Tuple10
import xyz.xenondevs.commons.tuple.Tuple2
import xyz.xenondevs.commons.tuple.Tuple3
import xyz.xenondevs.commons.tuple.Tuple4
import xyz.xenondevs.commons.tuple.Tuple5
import xyz.xenondevs.commons.tuple.Tuple6
import xyz.xenondevs.commons.tuple.Tuple7
import xyz.xenondevs.commons.tuple.Tuple8
import xyz.xenondevs.commons.tuple.Tuple9
import kotlin.test.assertEquals

class DecomposingProviderTest {
    
    @Test
    fun `decompose list - child update does not invalidate siblings`() {
        val parent = mutableProvider(listOf(1, 2, 3))
        val children = parent.strongDecompose(3, { it }, { it })
        
        var observeCount1 = 0
        var observeCount2 = 0
        children[1].observe { observeCount1++ }
        children[2].observe { observeCount2++ }
        
        // update child 0 — siblings should not be notified
        children[0].set(99)
        
        assertEquals(0, observeCount1)
        assertEquals(0, observeCount2)
        assertEquals(99, children[0].get())
        assertEquals(2, children[1].get())
        assertEquals(3, children[2].get())
        assertEquals(listOf(99, 2, 3), parent.get())
    }
    
    @ParameterizedTest
    @ValueSource(ints = [2, 3, 4, 5, 6, 7, 8, 9, 10])
    fun `decompose tuple - child update does not invalidate siblings`(n: Int) {
        val parent = mutableProvider(List(n) { it + 1 })
        val decomposed = decomposeTupleProvider(parent, n)
        
        // observe every sibling except the first
        val observeCounts = IntArray(n)
        for (i in 1..<n) {
            decomposed[i].observe { observeCounts[i]++ }
        }
        
        // update the first child — no sibling should be notified
        decomposed[0].set(99)
        
        for (i in 1..<n) {
            assertEquals(0, observeCounts[i], "sibling $i was invalidated")
        }
        assertEquals(99, decomposed[0].get())
        for (i in 1..<n) {
            assertEquals(i + 1, decomposed[i].get())
        }
    }
    
    @Test
    fun `decompose list - parent update invalidates all children`() {
        val parent = mutableProvider(listOf(1, 2, 3))
        val children = parent.strongDecompose(3, { it }, { it })
        
        val observeCounts = IntArray(3)
        children.forEachIndexed { i, p -> p.observe { observeCounts[i]++ } }
        
        parent.set(listOf(10, 20, 30))
        
        observeCounts.forEachIndexed { i, count ->
            assertEquals(1, count, "child $i was not notified")
        }
        assertEquals(10, children[0].get())
        assertEquals(20, children[1].get())
        assertEquals(30, children[2].get())
    }
    
    @ParameterizedTest
    @ValueSource(ints = [2, 3, 4, 5, 6, 7, 8, 9, 10])
    fun `decompose tuple - parent update invalidates all children`(n: Int) {
        val parent = mutableProvider(List(n) { 1 })
        val decomposed = decomposeTupleProvider(parent, n)
        
        val observeCounts = IntArray(n)
        decomposed.forEachIndexed { i, p -> p.observe { observeCounts[i]++ } }
        
        parent.set(List(n) { (it + 1) * 10 })
        
        for (i in 0..<n) {
            assertEquals(1, observeCounts[i], "child $i was not notified")
            assertEquals((i + 1) * 10, decomposed[i].get())
        }
    }
    
    @Test
    fun `decompose list provider`() {
        repeat(10) { num ->
            val size = num + 2
            val initial = (1..size).toList()
            val parent = mutableProvider(initial)
            val children = parent.decompose(size, { it }, { it })
            
            // check initial values
            children.forEachIndexed { i, p -> assertEquals(i + 1, p.get()) }
            
            // child to parent propagation
            children[0].set(100)
            assertEquals(100, parent.get()[0])
            
            // parent to child propagation
            val newValues = (1..size).map { it * 10 }
            parent.set(newValues)
            children.forEachIndexed { i, p -> assertEquals((i + 1) * 10, p.get()) }
        }
    }
    
    @ParameterizedTest
    @ValueSource(ints = [2, 3, 4, 5, 6, 7, 8, 9, 10])
    fun `decompose tuple provider - initial values`(n: Int) {
        val parent = mutableProvider(List(n) { 1 })
        
        val decomposed: List<MutableProvider<Int>> = decomposeTupleProvider(parent, n)
        
        decomposed.forEach { assertEquals(1, it.get()) }
    }
    
    @ParameterizedTest
    @ValueSource(ints = [2, 3, 4, 5, 6, 7, 8, 9, 10])
    fun `decompose tuple provider - child to parent propagation`(n: Int) {
        val parent = mutableProvider(List(n) { 1 })
        
        val decomposed: List<MutableProvider<Int>> = decomposeTupleProvider(parent, n)
        
        // set each child to 2
        decomposed.forEach { it.set(2) }
        
        assertEquals(List(n) { 2 }, parent.get())
    }
    
    @ParameterizedTest
    @ValueSource(ints = [2, 3, 4, 5, 6, 7, 8, 9, 10])
    fun `decompose tuple provider - parent to child propagation`(n: Int) {
        val parent = mutableProvider(List(n) { 1 })
        
        val decomposed: List<MutableProvider<Int>> = decomposeTupleProvider(parent, n)
        
        // update parent
        parent.set(List(n) { 2 })
        
        decomposed.forEach { assertEquals(2, it.get()) }
    }
    
    @ParameterizedTest
    @ValueSource(ints = [2, 3, 4, 5, 6, 7, 8, 9, 10])
    fun `decompose tuple provider - single child update`(n: Int) {
        val parent = mutableProvider(List(n) { it + 1 })
        
        val decomposed: List<MutableProvider<Int>> = decomposeTupleProvider(parent, n)
        
        // verify initial
        decomposed.forEachIndexed { i, p -> assertEquals(i + 1, p.get()) }
        
        // update only the first child
        decomposed[0].set(99)
        
        val expected = (1..n).toMutableList()
        expected[0] = 99
        assertEquals(expected, parent.get())
        
        // verify other children reflect parent state
        decomposed.forEachIndexed { i, p -> assertEquals(expected[i], p.get()) }
    }
    
    @Suppress("UNCHECKED_CAST")
    private fun decomposeTupleProvider(parent: MutableProvider<List<Int>>, n: Int): List<MutableProvider<Int>> {
        return when (n) {
            2 -> {
                val (a, b) = parent.decompose(
                    { Tuple2(it[0], it[1]) },
                    { a, b -> listOf(a, b) }
                )
                listOf(a, b)
            }
            
            3 -> {
                val (a, b, c) = parent.decompose(
                    { Tuple3(it[0], it[1], it[2]) },
                    { a, b, c -> listOf(a, b, c) }
                )
                listOf(a, b, c)
            }
            
            4 -> {
                val (a, b, c, d) = parent.decompose(
                    { Tuple4(it[0], it[1], it[2], it[3]) },
                    { a, b, c, d -> listOf(a, b, c, d) }
                )
                listOf(a, b, c, d)
            }
            
            5 -> {
                val (a, b, c, d, e) = parent.decompose(
                    { Tuple5(it[0], it[1], it[2], it[3], it[4]) },
                    { a, b, c, d, e -> listOf(a, b, c, d, e) }
                )
                listOf(a, b, c, d, e)
            }
            
            6 -> {
                val (a, b, c, d, e, f) = parent.decompose(
                    { Tuple6(it[0], it[1], it[2], it[3], it[4], it[5]) },
                    { a, b, c, d, e, f -> listOf(a, b, c, d, e, f) }
                )
                listOf(a, b, c, d, e, f)
            }
            
            7 -> {
                val (a, b, c, d, e, f, g) = parent.decompose(
                    { Tuple7(it[0], it[1], it[2], it[3], it[4], it[5], it[6]) },
                    { a, b, c, d, e, f, g -> listOf(a, b, c, d, e, f, g) }
                )
                listOf(a, b, c, d, e, f, g)
            }
            
            8 -> {
                val (a, b, c, d, e, f, g, h) = parent.decompose(
                    { Tuple8(it[0], it[1], it[2], it[3], it[4], it[5], it[6], it[7]) },
                    { a, b, c, d, e, f, g, h -> listOf(a, b, c, d, e, f, g, h) }
                )
                listOf(a, b, c, d, e, f, g, h)
            }
            
            9 -> {
                val (a, b, c, d, e, f, g, h, i) = parent.decompose(
                    { Tuple9(it[0], it[1], it[2], it[3], it[4], it[5], it[6], it[7], it[8]) },
                    { a, b, c, d, e, f, g, h, i -> listOf(a, b, c, d, e, f, g, h, i) }
                )
                listOf(a, b, c, d, e, f, g, h, i)
            }
            
            10 -> {
                val (a, b, c, d, e, f, g, h, i, j) = parent.decompose(
                    { Tuple10(it[0], it[1], it[2], it[3], it[4], it[5], it[6], it[7], it[8], it[9]) },
                    { a, b, c, d, e, f, g, h, i, j -> listOf(a, b, c, d, e, f, g, h, i, j) }
                )
                listOf(a, b, c, d, e, f, g, h, i, j)
            }
            
            else -> throw UnsupportedOperationException()
        }
    }
    
}