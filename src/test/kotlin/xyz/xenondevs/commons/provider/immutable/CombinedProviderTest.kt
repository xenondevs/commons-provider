package xyz.xenondevs.commons.provider.immutable

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import xyz.xenondevs.commons.provider.combinedProvider
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

class CombinedProviderTest {
    
    @Test
    fun `combined list mapping provider`() {
        repeat(100) { num ->
            val parents = (0..num).map { mutableProvider(it) }
            val combined = combinedProvider(parents) { it.sum() }
            
            assertEquals((0..num).sum(), combined.get())
            
            parents.forEach { it.set(it.get() * 2) }
            
            assertEquals((0..num).sum() * 2, combined.get())
        }
    }
    
    @Test
    fun `combined list provider`() {
        repeat(100) { num ->
            val parents = (0..num).map { mutableProvider(it) }
            val combined = combinedProvider(parents)
            
            assertEquals(parents.map { it.get() }, combined.get())
            
            parents.forEach { it.set(it.get() * 2) }
            
            assertEquals(parents.map { it.get() }, combined.get())
        }
    }
    
    @ParameterizedTest
    @ValueSource(ints = [2, 3, 4, 5, 6, 7, 8, 9, 10])
    fun `combined mapping provider`(n: Int) {
        val providers = List(n) { mutableProvider(1) }
        
        val combined = when (n) {
            2 -> combinedProvider(providers[0], providers[1]) { a, b -> a + b }
            3 -> combinedProvider(providers[0], providers[1], providers[2]) { a, b, c -> a + b + c }
            4 -> combinedProvider(providers[0], providers[1], providers[2], providers[3]) { a, b, c, d -> a + b + c + d }
            5 -> combinedProvider(providers[0], providers[1], providers[2], providers[3], providers[4]) { a, b, c, d, e -> a + b + c + d + e }
            6 -> combinedProvider(providers[0], providers[1], providers[2], providers[3], providers[4], providers[5]) { a, b, c, d, e, f -> a + b + c + d + e + f }
            7 -> combinedProvider(providers[0], providers[1], providers[2], providers[3], providers[4], providers[5], providers[6]) { a, b, c, d, e, f, g -> a + b + c + d + e + f + g }
            8 -> combinedProvider(providers[0], providers[1], providers[2], providers[3], providers[4], providers[5], providers[6], providers[7]) { a, b, c, d, e, f, g, h -> a + b + c + d + e + f + g + h }
            9 -> combinedProvider(providers[0], providers[1], providers[2], providers[3], providers[4], providers[5], providers[6], providers[7], providers[8]) { a, b, c, d, e, f, g, h, i -> a + b + c + d + e + f + g + h + i }
            10 -> combinedProvider(providers[0], providers[1], providers[2], providers[3], providers[4], providers[5], providers[6], providers[7], providers[8], providers[9]) { a, b, c, d, e, f, g, h, i, j -> a + b + c + d + e + f + g + h + i + j }
            else -> throw UnsupportedOperationException()
        }
        
        assertEquals(n, combined.get())
        
        providers.forEach { it.set(2) }
        
        assertEquals(2 * n, combined.get())
    }
    
    @ParameterizedTest
    @ValueSource(ints = [2, 3, 4, 5, 6, 7, 8, 9, 10])
    fun `combined tuple provider`(n: Int) {
        val providers = List(n) { mutableProvider(1) }
        
        val combined = when (n) {
            2 -> combinedProvider(providers[0], providers[1])
            3 -> combinedProvider(providers[0], providers[1], providers[2])
            4 -> combinedProvider(providers[0], providers[1], providers[2], providers[3])
            5 -> combinedProvider(providers[0], providers[1], providers[2], providers[3], providers[4])
            6 -> combinedProvider(providers[0], providers[1], providers[2], providers[3], providers[4], providers[5])
            7 -> combinedProvider(providers[0], providers[1], providers[2], providers[3], providers[4], providers[5], providers[6])
            8 -> combinedProvider(providers[0], providers[1], providers[2], providers[3], providers[4], providers[5], providers[6], providers[7])
            9 -> combinedProvider(providers[0], providers[1], providers[2], providers[3], providers[4], providers[5], providers[6], providers[7], providers[8])
            10 -> combinedProvider(providers[0], providers[1], providers[2], providers[3], providers[4], providers[5], providers[6], providers[7], providers[8], providers[9])
            else -> throw UnsupportedOperationException()
        }
        
        val expectedInitial = when (n) {
            2 -> Tuple2(1, 1)
            3 -> Tuple3(1, 1, 1)
            4 -> Tuple4(1, 1, 1, 1)
            5 -> Tuple5(1, 1, 1, 1, 1)
            6 -> Tuple6(1, 1, 1, 1, 1, 1)
            7 -> Tuple7(1, 1, 1, 1, 1, 1, 1)
            8 -> Tuple8(1, 1, 1, 1, 1, 1, 1, 1)
            9 -> Tuple9(1, 1, 1, 1, 1, 1, 1, 1, 1)
            10 -> Tuple10(1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
            else -> throw UnsupportedOperationException()
        }
        assertEquals(expectedInitial, combined.get())
        
        providers.forEach { it.set(2) }
        
        val expectedUpdated = when (n) {
            2 -> Tuple2(2, 2)
            3 -> Tuple3(2, 2, 2)
            4 -> Tuple4(2, 2, 2, 2)
            5 -> Tuple5(2, 2, 2, 2, 2)
            6 -> Tuple6(2, 2, 2, 2, 2, 2)
            7 -> Tuple7(2, 2, 2, 2, 2, 2, 2)
            8 -> Tuple8(2, 2, 2, 2, 2, 2, 2, 2)
            9 -> Tuple9(2, 2, 2, 2, 2, 2, 2, 2, 2)
            10 -> Tuple10(2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
            else -> throw UnsupportedOperationException()
        }
        assertEquals(expectedUpdated, combined.get())
    }
    
}