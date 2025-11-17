package xyz.xenondevs.commons.provider.immutable

import org.junit.jupiter.api.Test
import xyz.xenondevs.commons.provider.flatMapCollection
import xyz.xenondevs.commons.provider.flattenIterables
import xyz.xenondevs.commons.provider.mapEach
import xyz.xenondevs.commons.provider.mapEachNotNull
import xyz.xenondevs.commons.provider.mergeMaps
import xyz.xenondevs.commons.provider.minus
import xyz.xenondevs.commons.provider.mutableProvider
import xyz.xenondevs.commons.provider.plus
import xyz.xenondevs.commons.provider.provider
import kotlin.test.assertEquals

class CollectionMappingProviderTest {
    
    @Test
    fun mapEach() {
        val provider = provider(listOf(1, 2, 3))
        val mapped = provider.mapEach { it + 1 }
        
        assertEquals(listOf(1, 2, 3), provider.get())
        assertEquals(listOf(2, 3, 4), mapped.get())
    }
    
    @Test
    fun mapEachNotNull() {
        val provider = provider(listOf(1, null, null, 2, 3, null))
        val mapped = provider.mapEachNotNull { it?.plus(1) }
        
        assertEquals(listOf(1, null, null, 2, 3, null), provider.get())
        assertEquals(listOf(2, 3, 4), mapped.get())
    }
    
    @Test
    fun flatMapCollection() {
        val provider = provider(listOf("AB", "CD"))
        val flatMapped = provider.flatMapCollection { it.toCharArray().asList() }
        
        assertEquals(listOf("AB", "CD"), provider.get())
        assertEquals(listOf('A', 'B', 'C', 'D'), flatMapped.get())
    }
    
    @Test
    fun flattenIterables() {
        val provider = provider(listOf(listOf(1, 2), listOf(3, 4)))
        val flattened = provider.flattenIterables()
        
        assertEquals(listOf(listOf(1, 2), listOf(3, 4)), provider.get())
        assertEquals(listOf(1, 2, 3, 4), flattened.get())
    }
    
    @Test
    fun mergeMaps() {
        val provider = provider(listOf(mapOf("a" to 1, "b" to 2), mapOf("b" to -2, "c" to 3)))
        val merged = provider.mergeMaps()
        
        assertEquals(listOf(mapOf("a" to 1, "b" to 2), mapOf("b" to -2, "c" to 3)), provider.get())
        assertEquals(mapOf("a" to 1, "b" to -2, "c" to 3), merged.get())
    }
    
    @Test
    fun iterablePlusElement() {
        val a = mutableProvider(listOf("a", "b"))
        val result = a + "c"
        
        assertEquals(listOf("a", "b", "c"), result.get())
       
        a.set(listOf("a"))
        assertEquals(listOf("a", "c"), result.get())
    }
    
    @Test
    fun iterablePlusElementProvider() {
        val a = mutableProvider(listOf("a", "b"))
        val b = mutableProvider("c")
        val result = a + b
        
        assertEquals(listOf("a", "b", "c"), result.get())
        
        a.set(listOf("a"))
        assertEquals(listOf("a", "c"), result.get())
        
        b.set("d")
        assertEquals(listOf("a", "d"), result.get())
    }
    
    @Test
    fun iterablePlusIterable() {
        val a = mutableProvider(listOf("a"))
        val b = listOf("b")
        val result = a + b
        
        assertEquals(listOf("a", "b"), result.get())
        
        a.set(emptyList())
        assertEquals(listOf("b"), result.get())
    }
    
    @Test
    fun iterablePlusIterableProvider() {
        val a = mutableProvider(listOf("a"))
        val b = mutableProvider(listOf("b"))
        val result = a + b
        
        assertEquals(listOf("a", "b"), result.get())
        
        a.set(emptyList())
        assertEquals(listOf("b"), result.get())
        
        b.set(emptyList())
        assertEquals(emptyList(), result.get())
    }
    
    @Test
    fun iterableMinusElement() {
        val a = mutableProvider(listOf("a", "b"))
        val result = a - "b"
        
        assertEquals(listOf("a"), result.get())
        
        a.set(listOf("a", "b", "c"))
        assertEquals(listOf("a", "c"), result.get())
    }
    
    @Test
    fun iterableMinusElementProvider() {
        val a = mutableProvider(listOf("a", "b"))
        val b = mutableProvider("b")
        val result = a - b
        
        assertEquals(listOf("a"), result.get())
        
        a.set(listOf("a", "b", "c"))
        assertEquals(listOf("a", "c"), result.get())
        
        b.set("c")
        assertEquals(listOf("a", "b"), result.get())
    }
    
    @Test
    fun iterableMinusIterable() {
        val a = mutableProvider(listOf("a", "b", "c"))
        val result = a - listOf("b", "c")
        
        assertEquals(listOf("a"), result.get())
        
        a.set(listOf("a", "b", "c", "d"))
        assertEquals(listOf("a", "d"), result.get())
    }
    
    @Test
    fun iterableMinusIterableProvider() {
        val a = mutableProvider(listOf("a", "b", "c"))
        val b = mutableProvider(listOf("b", "c"))
        val result = a - b
        
        assertEquals(listOf("a"), result.get())
        
        a.set(listOf("a", "b", "c", "d"))
        assertEquals(listOf("a", "d"), result.get())
        
        b.set(listOf("c", "d"))
        assertEquals(listOf("a", "b"), result.get())
    }
    
    @Test
    fun setPlusElement() {
        val a = mutableProvider(setOf("a", "b"))
        val result = a + "c"
        
        assertEquals(setOf("a", "b", "c"), result.get())
        
        a.set(setOf("a"))
        assertEquals(setOf("a", "c"), result.get())
    }
    
    @Test
    fun setPlusElementProvider() {
        val a = mutableProvider(setOf("a", "b"))
        val b = mutableProvider("c")
        val result = a + b
        
        assertEquals(setOf("a", "b", "c"), result.get())
        
        a.set(setOf("a"))
        assertEquals(setOf("a", "c"), result.get())
        
        b.set("d")
        assertEquals(setOf("a", "d"), result.get())
    }
    
    @Test
    fun setPlusIterable() {
        val a = mutableProvider(setOf("a"))
        val b = setOf("b")
        val result = a + b
        
        assertEquals(setOf("a", "b"), result.get())
        
        a.set(emptySet())
        assertEquals(setOf("b"), result.get())
    }
    
    @Test
    fun setPlusIterableProvider() {
        val a = mutableProvider(setOf("a"))
        val b = mutableProvider(setOf("b"))
        val result = a + b
        
        assertEquals(setOf("a", "b"), result.get())
        
        a.set(emptySet())
        assertEquals(setOf("b"), result.get())
        
        b.set(emptySet())
        assertEquals(emptySet(), result.get())
    }
    
    @Test
    fun setMinusElement() {
        val a = mutableProvider(setOf("a", "b"))
        val result = a - "b"
        
        assertEquals(setOf("a"), result.get())
        
        a.set(setOf("a", "b", "c"))
        assertEquals(setOf("a", "c"), result.get())
    }
    
    @Test
    fun setMinusElementProvider() {
        val a = mutableProvider(setOf("a", "b"))
        val b = mutableProvider("b")
        val result = a - b
        
        assertEquals(setOf("a"), result.get())
        
        a.set(setOf("a", "b", "c"))
        assertEquals(setOf("a", "c"), result.get())
        
        b.set("c")
        assertEquals(setOf("a", "b"), result.get())
    }
    
    @Test
    fun setMinusIterable() {
        val a = mutableProvider(setOf("a", "b", "c"))
        val result = a - setOf("b", "c")
        
        assertEquals(setOf("a"), result.get())
        
        a.set(setOf("a", "b", "c", "d"))
        assertEquals(setOf("a", "d"), result.get())
    }
    
    @Test
    fun setMinusIterableProvider() {
        val a = mutableProvider(setOf("a", "b", "c"))
        val b = mutableProvider(setOf("b", "c"))
        val result = a - b
        
        assertEquals(setOf("a"), result.get())
        
        a.set(setOf("a", "b", "c", "d"))
        assertEquals(setOf("a", "d"), result.get())
        
        b.set(setOf("c", "d"))
        assertEquals(setOf("a", "b"), result.get())
    }
    
}