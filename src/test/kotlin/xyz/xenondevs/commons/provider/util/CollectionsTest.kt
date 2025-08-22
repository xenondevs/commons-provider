package xyz.xenondevs.commons.provider.util

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertInstanceOf
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame

class CollectionsTest {
    
    @Test
    fun `test Set#with`() {
        val original = setOf("a", "b")
        val new = original.with("c", ::setOf, ::HashSet)
        
        assertEquals(setOf("a", "b", "c"), new)
        assertNotSame(original, new)
        assertInstanceOf<HashSet<*>>(new)
    }
    
    @Test
    fun `test Set#with, results in singleton set`() {
        val original = emptySet<String>()
        val new = original.with("a", ::setOf, ::HashSet)
        
        assertEquals(setOf("a"), new)
        assertNotSame(original, new)
        assertNotInstanceOf<HashSet<*>>(new) // assumes not HashSet means Collections.SingletonSet
    }
    
    @Test
    fun `test Set#with, element is already contained in set`() {
        val original = setOf("a", "b")
        val new = original.with("b", ::setOf, ::HashSet)
        
        assertSame(original, new)
    }
    
    @Test
    fun `test Set#without`() {
        val original = setOf("a", "b", "c")
        val new = original.without("c", ::setOf, ::HashSet)
        
        assertEquals(setOf("a", "b"), new)
        assertNotSame(original, new)
        assertInstanceOf<HashSet<*>>(new)
    }
    
    @Test
    fun `test Set#without, results in singleton set`() {
        val original = setOf("a", "b")
        val new = original.without("b", ::setOf, ::HashSet)
        
        assertEquals(setOf("a"), new)
        assertNotSame(original, new)
        assertNotInstanceOf<HashSet<*>>(new) // assumes not HashSet means Collections.SingletonSet
    }
    
    @Test
    fun `test Set#without, results in empty set`() {
        val original = setOf("a")
        val new = original.without("a", ::setOf, ::HashSet)
        
        assertEquals(emptySet(), new)
        assertNotSame(original, new)
        assertSame(emptySet(), new)
    }
    
    @Test
    fun `test Set#without, element is not contained in set`() {
        val original = setOf("a", "b")
        val new = original.without("c", ::setOf, ::HashSet)
        
        assertEquals(setOf("a", "b"), new)
        assertSame(original, new)
    }
    
    @Test
    fun `test MapK,SetV#with`() {
        val original = mapOf(
            "a" to setOf("aa", "ab"),
            "b" to setOf("ba", "bb")
        )
        val new1 = original.with("a", "ac", ::singletonMapOf, ::HashMap, ::setOf, ::HashSet)
        
        assertEquals(setOf("aa", "ab", "ac"), new1["a"])
        assertEquals(setOf("ba", "bb"), new1["b"])
        assertNotSame(new1, original)
        assertInstanceOf<HashMap<*, *>>(new1)
        assertInstanceOf<HashSet<*>>(new1["a"])
        
        val new2 = original.with("c", "ca", ::singletonMapOf, ::HashMap, ::setOf, ::HashSet)
            .with("c", "cb", ::singletonMapOf, ::HashMap, ::setOf, ::HashSet)
        assertNotSame(original, new2)
        assertEquals(setOf("aa", "ab"), new2["a"])
        assertEquals(setOf("ba", "bb"), new2["b"])
        assertEquals(setOf("ca", "cb"), new2["c"])
        assertInstanceOf<HashMap<*, *>>(new2)
        assertInstanceOf<HashSet<*>>(new2["c"])
    }
    
    @Test
    fun `test MapK,SetV#with, results in singleton map and set`() {
        val original = emptyMap<String, Set<String>>()
        val new = original.with("a", "a", ::singletonMapOf, ::HashMap, ::setOf, ::HashSet)
        
        assertEquals(setOf("a"), new["a"])
        assertNotInstanceOf<HashMap<*, *>>(new) // assumes not HashMap means singleton map
        assertNotInstanceOf<HashSet<*>>(new["c"]) // assumes not HashSet means singleton set
    }
    
    @Test
    fun `test MapK,SetV#with, value is already contained in set`() {
        val original = mapOf(
            "a" to setOf("aa", "ab"),
            "b" to setOf("ba", "bb")
        )
        
        val new = original.with("a", "aa", ::singletonMapOf, ::HashMap, ::setOf, ::HashSet)
        assertSame(original, new)
    }
    
    @Test
    fun `test MapK,SetV#without`() {
        val original = mapOf(
            "a" to setOf("aa", "ab", "ac"),
            "b" to setOf("ba", "bb", "bc"),
            "c" to setOf("ca", "cb", "cc")
        )
        
        val new1 = original.without("a", "ac", ::singletonMapOf, ::HashMap, ::setOf, ::HashSet)
        assertEquals(setOf("aa", "ab"), new1["a"])
        assertEquals(setOf("ba", "bb", "bc"), new1["b"])
        assertNotSame(new1, original)
        assertInstanceOf<HashMap<*, *>>(new1)
        assertInstanceOf<HashSet<*>>(new1["a"])
        
        val new2 = new1.without("a", "ab", ::singletonMapOf, ::HashMap, ::setOf, ::HashSet)
            .without("a", "aa", ::singletonMapOf, ::HashMap, ::setOf, ::HashSet)
        
        assertEquals(null, new2["a"])
        assertEquals(setOf("ba", "bb", "bc"), new2["b"])
        assertNotSame(new2, original)
        assertInstanceOf<HashMap<*, *>>(new2)
    }
    
    @Test
    fun `test MapK,SetV#without, key is not in map`() {
        val original = mapOf(
            "a" to setOf("aa", "ab"),
            "b" to setOf("ba", "bb")
        )
        
        val new = original.without("c", "ca", ::singletonMapOf, ::HashMap, ::setOf, ::HashSet)
        assertSame(original, new)
    }
    
    @Test
    fun `test MapK,SetV#without, value is not in set`() {
        val original = mapOf(
            "a" to setOf("aa", "ab"),
            "b" to setOf("ba", "bb")
        )
        
        val new = original.without("a", "ac", ::singletonMapOf, ::HashMap, ::setOf, ::HashSet)
        assertSame(original, new)
    }
    
    @Test
    fun `test MapK,SetV#without, results in singleton map and set`() {
        val original = mapOf(
            "a" to setOf("aa", "ab"),
            "b" to setOf("ba")
        )
        
        val new1 = original.without("a", "ab", ::singletonMapOf, ::HashMap, ::setOf, ::HashSet)
        assertEquals(setOf("aa"), new1["a"])
        assertEquals(setOf("ba"), new1["b"])
        assertNotSame(new1, original)
        assertInstanceOf<HashMap<*, *>>(new1)
        assertNotInstanceOf<HashSet<*>>(new1["a"]) // assumes not HashSet means singleton set
        
        val new2 = new1.without("a", "aa", ::singletonMapOf, ::HashMap, ::setOf, ::HashSet)
        assertEquals(null, new2["a"])
        assertEquals(setOf("ba"), new2["b"])
        assertNotSame(new2, original)
        assertNotInstanceOf<HashMap<*, *>>(new2) // assumes not HashMap means singleton map
        assertNotInstanceOf<HashSet<*>>(new2["b"]) // assumes not HashSet means singleton set
    }
    
    private fun <K : Any, V : Any> singletonMapOf(key: K, value: V): Map<K, V> {
        return java.util.Map.of(key, value)
    }
    
    private inline fun <reified T> assertNotInstanceOf(value: Any?) {
        if (value is T)
            throw AssertionError("Expected $value to not be an instance of ${T::class.simpleName}, but it was.")
    }
    
}