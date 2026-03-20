package xyz.xenondevs.commons.provider.immutable

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import xyz.xenondevs.commons.provider.get
import xyz.xenondevs.commons.provider.getCoerced
import xyz.xenondevs.commons.provider.getMod
import xyz.xenondevs.commons.provider.getOrNull
import xyz.xenondevs.commons.provider.mutableProvider
import xyz.xenondevs.commons.provider.provider
import kotlin.test.assertEquals
import kotlin.test.assertNull

class IndexProviderTest {

    @Test
    fun `list get with valid index`() {
        val provider = provider(listOf("a", "b", "c"))
        val element = provider[1]
        assertEquals("b", element.get())
    }

    @Test
    fun `list get with invalid index throws IndexOutOfBoundsException`() {
        val provider = provider(listOf("a", "b", "c"))
        val element = provider[5]
        assertThrows<IndexOutOfBoundsException> { element.get() }
    }

    @Test
    fun `list get with provider index`() {
        val list = mutableProvider(listOf("a", "b", "c"))
        val index = mutableProvider(0)
        val element = list[index]

        assertEquals("a", element.get())

        index.set(2)
        assertEquals("c", element.get())

        list.set(listOf("x", "y", "z"))
        assertEquals("z", element.get())
    }

    @Test
    fun `list getOrNull with valid index`() {
        val provider = provider(listOf("a", "b", "c"))
        val element = provider.getOrNull(1)
        assertEquals("b", element.get())
    }

    @Test
    fun `list getOrNull with invalid index returns null`() {
        val provider = provider(listOf("a", "b", "c"))
        val element = provider.getOrNull(5)
        assertNull(element.get())
    }

    @Test
    fun `list getOrNull with provider index`() {
        val list = mutableProvider(listOf("a", "b", "c"))
        val index = mutableProvider(1)
        val element = list.getOrNull(index)

        assertEquals("b", element.get())

        index.set(10)
        assertNull(element.get())
    }

    @Test
    fun `list getCoerced clamps to valid range`() {
        val provider = provider(listOf("a", "b", "c"))

        assertEquals("c", provider.getCoerced(100).get())
        assertEquals("a", provider.getCoerced(-1).get())
        assertEquals("b", provider.getCoerced(1).get())
    }

    @Test
    fun `list getCoerced with provider index`() {
        val list = mutableProvider(listOf("a", "b", "c"))
        val index = mutableProvider(100)
        val element = list.getCoerced(index)

        assertEquals("c", element.get())

        index.set(-1)
        assertEquals("a", element.get())
    }

    @Test
    fun `list getMod wraps around`() {
        val provider = provider(listOf("a", "b", "c"))

        assertEquals("a", provider.getMod(0).get())
        assertEquals("a", provider.getMod(3).get())
        assertEquals("b", provider.getMod(4).get())
    }

    @Test
    fun `list getMod with provider index`() {
        val list = mutableProvider(listOf("a", "b", "c"))
        val index = mutableProvider(4)
        val element = list.getMod(index)

        assertEquals("b", element.get())

        index.set(3)
        assertEquals("a", element.get())
    }

    @Test
    fun `array get with valid index`() {
        val provider = provider(arrayOf("a", "b", "c"))
        val element = provider[1]
        assertEquals("b", element.get())
    }

    @Test
    fun `array get with invalid index throws IndexOutOfBoundsException`() {
        val provider = provider(arrayOf("a", "b", "c"))
        val element = provider[5]
        assertThrows<IndexOutOfBoundsException> { element.get() }
    }

    @Test
    fun `array get with provider index`() {
        val array = mutableProvider(arrayOf("a", "b", "c"))
        val index = mutableProvider(0)
        val element = array[index]

        assertEquals("a", element.get())

        index.set(2)
        assertEquals("c", element.get())
    }

    @Test
    fun `array getOrNull with valid index`() {
        val provider = provider(arrayOf("a", "b", "c"))
        val element = provider.getOrNull(1)
        assertEquals("b", element.get())
    }

    @Test
    fun `array getOrNull with invalid index returns null`() {
        val provider = provider(arrayOf("a", "b", "c"))
        val element = provider.getOrNull(5)
        assertNull(element.get())
    }

    @Test
    fun `array getOrNull with provider index`() {
        val array = mutableProvider(arrayOf("a", "b", "c"))
        val index = mutableProvider(1)
        val element = array.getOrNull(index)

        assertEquals("b", element.get())

        index.set(10)
        assertNull(element.get())
    }

    @Test
    fun `array getCoerced clamps to valid range`() {
        val provider = provider(arrayOf("a", "b", "c"))

        assertEquals("c", provider.getCoerced(100).get())
        assertEquals("a", provider.getCoerced(-1).get())
        assertEquals("b", provider.getCoerced(1).get())
    }

    @Test
    fun `array getCoerced with provider index`() {
        val array = mutableProvider(arrayOf("a", "b", "c"))
        val index = mutableProvider(100)
        val element = array.getCoerced(index)

        assertEquals("c", element.get())

        index.set(-1)
        assertEquals("a", element.get())
    }

    @Test
    fun `array getMod wraps around`() {
        val provider = provider(arrayOf("a", "b", "c"))

        assertEquals("a", provider.getMod(0).get())
        assertEquals("a", provider.getMod(3).get())
        assertEquals("b", provider.getMod(4).get())
    }

    @Test
    fun `array getMod with provider index`() {
        val array = mutableProvider(arrayOf("a", "b", "c"))
        val index = mutableProvider(4)
        val element = array.getMod(index)

        assertEquals("b", element.get())

        index.set(3)
        assertEquals("a", element.get())
    }

    @Test
    fun `index provider updates when parent changes`() {
        val list = mutableProvider(listOf("a", "b", "c"))
        val element = list[0]

        assertEquals("a", element.get())

        list.set(listOf("x", "y", "z"))
        assertEquals("x", element.get())
    }

}
