package xyz.xenondevs.commons.provider.immutable

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertInstanceOf
import xyz.xenondevs.commons.provider.combinedProvider
import xyz.xenondevs.commons.provider.impl.StableProvider
import xyz.xenondevs.commons.provider.mutableProvider
import xyz.xenondevs.commons.provider.provider
import kotlin.test.assertSame

class StableProviderTest {
    
    @Test
    fun `test StableProvider#strongMap returns StableProvider`() {
        assertInstanceOf<StableProvider<*>>(provider(1).strongMap { it + 1 })
    }
    
    @Test
    fun `test StableProvider#map returns StableProvider`() {
        assertInstanceOf<StableProvider<*>>(provider(1).map { it + 1 })
    }
    
    @Test
    fun `test StableProvider#strongImmediateFlatMap returns identity`() {
        val p = provider(1)
        assertSame(p, provider(0).strongImmediateFlatMap { p })
    }
    
    @Test
    fun `test StableProvider#immediateFlatMap returns identity`() {
        val p = provider(1)
        assertSame(p, provider(0).immediateFlatMap { p })
    }
    
    @Test
    fun `test StableProvider#strongImmediateFlatMapMutable returns identity`() {
        val p = mutableProvider(1)
        assertSame(p, provider(0).strongImmediateFlatMapMutable { p })
    }
    
    @Test
    fun `test StableProvider#immediateFlatMapMutable returns identity`() {
        val id = mutableProvider(1)
        assertSame(id, provider(0).immediateFlatMapMutable { id })
    }
    
    @Test
    fun `test combination of stable providers is a stable provider`() {
        val a = provider(1)
        val b = provider(2)
        val c = provider(3)
        val d = provider(4)
        val e = provider(5)
        
        assertInstanceOf<StableProvider<*>>(combinedProvider(a, b, c, d, e) { a, b, c, d, e -> a + b + c + d + e })
    }
    
}