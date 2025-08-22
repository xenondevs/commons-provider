package xyz.xenondevs.commons.provider.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SingleKeyWeakHashMapTest {
    
    @Test
    fun getSize() {
        val map = SingleKeyWeakHashMap("key", "value")
        assertEquals(1, map.size)
    }
    
    @Test
    fun getKeys() {
        val map = SingleKeyWeakHashMap("key", "value")
        assertEquals(setOf("key"), map.keys)
    }
    
    @Test
    fun getValues() {
        val map = SingleKeyWeakHashMap("key", "value")
        assertEquals(listOf("value"), map.values)
    }
    
    @Test
    fun getEntries() {
        val map = SingleKeyWeakHashMap("key", "value")
        val entries = map.entries
        assertEquals(1, entries.size)
        assertEquals("key", entries.first().key)
        assertEquals("value", entries.first().value)
    }
    
    @Test
    fun isEmpty() {
        val map = SingleKeyWeakHashMap("key", "value")
        assertFalse(map.isEmpty())
    }
    
    @Test
    fun containsKey() {
        val map = SingleKeyWeakHashMap("key", "value")
        assertTrue(map.containsKey("key"))
        assertFalse(map.containsKey("otherKey"))
    }
    
    @Test
    fun containsValue() {
        val map = SingleKeyWeakHashMap("key", "value")
        assertTrue(map.containsValue("value"))
        assertFalse(map.containsValue("otherValue"))
    }
    
    @Test
    fun get() {
        val map = SingleKeyWeakHashMap("key", "value")
        assertEquals("value", map["key"])
        assertNull(map["otherKey"])
    }
    
}