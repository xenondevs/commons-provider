package xyz.xenondevs.commons.provider.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SingleElementWeakSetTest {
    
    @Test
    fun getSize() {
        val set = SingleElementWeakSet("A")
        assertEquals(1, set.size)
    }
    
    @Test
    fun isEmpty() {
        val set = SingleElementWeakSet("A")
        assertFalse(set.isEmpty())
    }
    
    @Test
    fun contains() {
        val set = SingleElementWeakSet("A")
        assertTrue(set.contains("A"))
        assertFalse(set.contains("B"))
    }
    
    @Test
    fun iterator() {
        val set = SingleElementWeakSet("A")
        
        val iterator = set.iterator()
        assertTrue(iterator.hasNext())
        assertEquals("A", iterator.next())
        assertFalse(iterator.hasNext())
        assertThrows<NoSuchElementException> { iterator.next() }
    }
    
    @Test
    fun containsAll() {
        val set = SingleElementWeakSet("A")
        assertTrue(set.containsAll(listOf("A")))
        assertTrue(set.containsAll(emptyList()))
        assertFalse(set.containsAll(listOf("B")))
        assertFalse(set.containsAll(listOf("A", "B")))
    }
    
}