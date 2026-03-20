package xyz.xenondevs.commons.provider.immutable

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import xyz.xenondevs.commons.provider.get
import xyz.xenondevs.commons.provider.getOrThrow
import xyz.xenondevs.commons.provider.mutableProvider
import xyz.xenondevs.commons.provider.provider
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MapEntryProviderTest {

    @Test
    fun `map get with existing key`() {
        val provider = provider(mapOf("a" to 1, "b" to 2, "c" to 3))
        val value = provider["b"]
        assertEquals(2, value.get())
    }

    @Test
    fun `map get with missing key returns null`() {
        val provider = provider(mapOf("a" to 1, "b" to 2))
        val value = provider["missing"]
        assertNull(value.get())
    }

    @Test
    fun `map get with provider key`() {
        val map = mutableProvider(mapOf("a" to 1, "b" to 2, "c" to 3))
        val key = mutableProvider("a")
        val value = map[key]

        assertEquals(1, value.get())

        key.set("c")
        assertEquals(3, value.get())

        key.set("missing")
        assertNull(value.get())
    }

    @Test
    fun `map get updates when parent changes`() {
        val map = mutableProvider(mapOf("a" to 1))
        val value = map["a"]

        assertEquals(1, value.get())

        map.set(mapOf("a" to 42))
        assertEquals(42, value.get())

        map.set(mapOf("b" to 1))
        assertNull(value.get())
    }

    @Test
    fun `map getOrThrow with existing key`() {
        val provider = provider(mapOf("a" to 1, "b" to 2))
        val value = provider.getOrThrow("a")
        assertEquals(1, value.get())
    }

    @Test
    fun `map getOrThrow with missing key throws NoSuchElementException`() {
        val provider = provider(mapOf("a" to 1))
        val value = provider.getOrThrow("missing")
        assertThrows<NoSuchElementException> { value.get() }
    }

    @Test
    fun `map getOrThrow with provider key`() {
        val map = mutableProvider(mapOf("a" to 1, "b" to 2))
        val key = mutableProvider("a")
        val value = map.getOrThrow(key)

        assertEquals(1, value.get())

        key.set("b")
        assertEquals(2, value.get())
    }

    @Test
    fun `map getOrThrow with provider key throws on missing`() {
        val map = mutableProvider(mapOf("a" to 1))
        val key = mutableProvider("a")
        val value = map.getOrThrow(key)

        assertEquals(1, value.get())

        key.set("missing")
        assertThrows<NoSuchElementException> { value.get() }
    }

}
