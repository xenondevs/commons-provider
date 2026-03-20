@file:OptIn(UnstableProviderApi::class)
@file:Suppress("DuplicatedCode")

package xyz.xenondevs.commons.provider.impl

import xyz.xenondevs.commons.provider.DeferredValue
import xyz.xenondevs.commons.provider.Provider
import xyz.xenondevs.commons.provider.UnstableProviderApi

private fun registerChild(parent: Provider<*>, child: Provider<*>, weak: Boolean) {
    if (weak) parent.addWeakChild(child) else parent.addStrongChild(child)
}

internal class MultiUnidirectionalTransformingProvider<P, T> private constructor(
    private val _parents: List<Provider<P>>,
    private val transform: (List<P>) -> T
) : UnidirectionalProvider<T>() {
    
    override val parents: Set<Provider<*>>
        get() = _parents.toSet()
    
    @Volatile
    override var value: DeferredValue<T> = DeferredValue.MappedMulti(_parents.map(Provider<P>::value), transform)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.MappedMulti(_parents.map(Provider<P>::value), transform))
    }
    
    companion object {
        
        fun <P, T> of(parents: List<Provider<P>>, weak: Boolean, transform: (List<P>) -> T): Provider<T> {
            return when (parents.size) {
                0 -> StableProvider(DeferredValue.Lazy { transform(emptyList()) })
                
                1 -> if (weak) parents[0].map { transform(listOf(it)) } else parents[0].strongMap { transform(listOf(it)) }
                
                2 -> MultiUnidirectionalTransformingProvider2.of(
                    parents[0], parents[1], weak
                ) { a, b ->
                    transform(listOf(a, b))
                }
                
                3 -> MultiUnidirectionalTransformingProvider3.of(
                    parents[0], parents[1], parents[2], weak
                ) { a, b, c ->
                    transform(listOf(a, b, c))
                }
                
                4 -> MultiUnidirectionalTransformingProvider4.of(
                    parents[0], parents[1], parents[2], parents[3], weak
                ) { a, b, c, d ->
                    transform(listOf(a, b, c, d))
                }
                
                5 -> MultiUnidirectionalTransformingProvider5.of(
                    parents[0], parents[1], parents[2], parents[3], parents[4], weak
                ) { a, b, c, d, e ->
                    transform(listOf(a, b, c, d, e))
                }
                
                6 -> MultiUnidirectionalTransformingProvider6.of(
                    parents[0], parents[1], parents[2], parents[3], parents[4], parents[5], weak) { a, b, c, d, e, f ->
                    transform(listOf(a, b, c, d, e, f))
                }
                
                7 -> MultiUnidirectionalTransformingProvider7.of(
                    parents[0], parents[1], parents[2], parents[3], parents[4], parents[5], parents[6], weak
                ) { a, b, c, d, e, f, g ->
                    transform(listOf(a, b, c, d, e, f, g))
                }
                
                8 -> MultiUnidirectionalTransformingProvider8.of(
                    parents[0], parents[1], parents[2], parents[3], parents[4], parents[5], parents[6], parents[7], weak
                ) { a, b, c, d, e, f, g, h ->
                    transform(listOf(a, b, c, d, e, f, g, h))
                }
                
                9 -> MultiUnidirectionalTransformingProvider9.of(
                    parents[0], parents[1], parents[2], parents[3], parents[4], parents[5], parents[6], parents[7], parents[8], weak
                ) { a, b, c, d, e, f, g, h, i ->
                    transform(listOf(a, b, c, d, e, f, g, h, i))
                }
                
                10 -> MultiUnidirectionalTransformingProvider10.of(
                    parents[0], parents[1], parents[2], parents[3], parents[4], parents[5], parents[6], parents[7], parents[8], parents[9], weak
                ) { a, b, c, d, e, f, g, h, i, j ->
                    transform(listOf(a, b, c, d, e, f, g, h, i, j))
                }
                
                else -> {
                    if (parents.all { it.delegate.isStable })
                        return StableProvider(DeferredValue.MappedMulti(parents.map { it.delegate.value }, transform))
                    
                    val parents = parents.map { it.delegate }
                    val provider = MultiUnidirectionalTransformingProvider(parents, transform)
                    if (weak) {
                        for (parent in parents) {
                            parent.addWeakChild(provider)
                        }
                    } else {
                        for (parent in parents) {
                            parent.addStrongChild(provider)
                        }
                    }
                    
                    provider.handleParentUpdated(parents[0]) // propagate potentially lost update during provider creation and child assignment
                    provider
                }
            }
        }
    }
    
}

internal class MultiUnidirectionalTransformingProvider2<A, B, T> private constructor(
    private val a: Provider<A>,
    private val b: Provider<B>,
    private val transform: (A, B) -> T
) : UnidirectionalProvider<T>() {
    
    override val parents: Set<Provider<*>>
        get() = setOf(a, b)
    
    @Volatile
    override var value: DeferredValue<T> = DeferredValue.Mapped2(a.value, b.value, transform)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.Mapped2(a.value, b.value, transform))
    }
    
    companion object {
        
        fun <A, B, T> of(a: Provider<A>, b: Provider<B>, weak: Boolean, transform: (A, B) -> T): Provider<T> {
            val parentA = a.delegate
            val parentB = b.delegate
            
            if (parentA.isStable && parentB.isStable)
                return StableProvider(DeferredValue.Mapped2(parentA.value, parentB.value, transform))
            
            val provider = MultiUnidirectionalTransformingProvider2(parentA, parentB, transform)
            registerChild(parentA, provider, weak)
            registerChild(parentB, provider, weak)
            provider.handleParentUpdated(parentA) // propagate potentially lost update during provider creation and child assignment
            return provider
        }
        
    }
    
}

internal class MultiUnidirectionalTransformingProvider3<A, B, C, T> private constructor(
    private val a: Provider<A>,
    private val b: Provider<B>,
    private val c: Provider<C>,
    private val transform: (A, B, C) -> T
) : UnidirectionalProvider<T>() {
    
    override val parents: Set<Provider<*>>
        get() = setOf(a, b, c)
    
    @Volatile
    override var value: DeferredValue<T> = DeferredValue.Mapped3(a.value, b.value, c.value, transform)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.Mapped3(a.value, b.value, c.value, transform))
    }
    
    companion object {
        
        fun <A, B, C, T> of(a: Provider<A>, b: Provider<B>, c: Provider<C>, weak: Boolean, transform: (A, B, C) -> T): Provider<T> {
            val parentA = a.delegate
            val parentB = b.delegate
            val parentC = c.delegate
            
            if (parentA.isStable && parentB.isStable && parentC.isStable)
                return StableProvider(DeferredValue.Mapped3(parentA.value, parentB.value, parentC.value, transform))
            
            val provider = MultiUnidirectionalTransformingProvider3(parentA, parentB, parentC, transform)
            registerChild(parentA, provider, weak)
            registerChild(parentB, provider, weak)
            registerChild(parentC, provider, weak)
            provider.handleParentUpdated(parentA) // propagate potentially lost update during provider creation and child assignment
            return provider
        }
        
    }
    
}

internal class MultiUnidirectionalTransformingProvider4<A, B, C, D, T> private constructor(
    private val a: Provider<A>,
    private val b: Provider<B>,
    private val c: Provider<C>,
    private val d: Provider<D>,
    private val transform: (A, B, C, D) -> T
) : UnidirectionalProvider<T>() {
    
    override val parents: Set<Provider<*>>
        get() = setOf(a, b, c, d)
    
    @Volatile
    override var value: DeferredValue<T> = DeferredValue.Mapped4(a.value, b.value, c.value, d.value, transform)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.Mapped4(a.value, b.value, c.value, d.value, transform))
    }
    
    companion object {
        
        fun <A, B, C, D, T> of(a: Provider<A>, b: Provider<B>, c: Provider<C>, d: Provider<D>, weak: Boolean, transform: (A, B, C, D) -> T): Provider<T> {
            val parentA = a.delegate
            val parentB = b.delegate
            val parentC = c.delegate
            val parentD = d.delegate
            
            if (parentA.isStable && parentB.isStable && parentC.isStable && parentD.isStable)
                return StableProvider(DeferredValue.Mapped4(parentA.value, parentB.value, parentC.value, parentD.value, transform))
            
            val provider = MultiUnidirectionalTransformingProvider4(parentA, parentB, parentC, parentD, transform)
            registerChild(parentA, provider, weak)
            registerChild(parentB, provider, weak)
            registerChild(parentC, provider, weak)
            registerChild(parentD, provider, weak)
            provider.handleParentUpdated(parentA) // propagate potentially lost update during provider creation and child assignment
            return provider
        }
        
    }
    
}

internal class MultiUnidirectionalTransformingProvider5<A, B, C, D, E, T> private constructor(
    private val a: Provider<A>,
    private val b: Provider<B>,
    private val c: Provider<C>,
    private val d: Provider<D>,
    private val e: Provider<E>,
    private val transform: (A, B, C, D, E) -> T
) : UnidirectionalProvider<T>() {
    
    override val parents: Set<Provider<*>>
        get() = setOf(a, b, c, d, e)
    
    @Volatile
    override var value: DeferredValue<T> = DeferredValue.Mapped5(a.value, b.value, c.value, d.value, e.value, transform)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.Mapped5(a.value, b.value, c.value, d.value, e.value, transform))
    }
    
    companion object {
        
        fun <A, B, C, D, E, T> of(a: Provider<A>, b: Provider<B>, c: Provider<C>, d: Provider<D>, e: Provider<E>, weak: Boolean, transform: (A, B, C, D, E) -> T): Provider<T> {
            val parentA = a.delegate
            val parentB = b.delegate
            val parentC = c.delegate
            val parentD = d.delegate
            val parentE = e.delegate
            
            if (parentA.isStable && parentB.isStable && parentC.isStable && parentD.isStable && parentE.isStable)
                return StableProvider(DeferredValue.Mapped5(parentA.value, parentB.value, parentC.value, parentD.value, parentE.value, transform))
            
            val provider = MultiUnidirectionalTransformingProvider5(parentA, parentB, parentC, parentD, parentE, transform)
            registerChild(parentA, provider, weak)
            registerChild(parentB, provider, weak)
            registerChild(parentC, provider, weak)
            registerChild(parentD, provider, weak)
            registerChild(parentE, provider, weak)
            provider.handleParentUpdated(parentA) // propagate potentially lost update during provider creation and child assignment
            return provider
        }
        
    }
    
}

internal class MultiUnidirectionalTransformingProvider6<A, B, C, D, E, F, T> private constructor(
    private val a: Provider<A>,
    private val b: Provider<B>,
    private val c: Provider<C>,
    private val d: Provider<D>,
    private val e: Provider<E>,
    private val f: Provider<F>,
    private val transform: (A, B, C, D, E, F) -> T
) : UnidirectionalProvider<T>() {
    
    override val parents: Set<Provider<*>>
        get() = setOf(a, b, c, d, e, f)
    
    @Volatile
    override var value: DeferredValue<T> = DeferredValue.Mapped6(a.value, b.value, c.value, d.value, e.value, f.value, transform)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.Mapped6(a.value, b.value, c.value, d.value, e.value, f.value, transform))
    }
    
    companion object {
        
        fun <A, B, C, D, E, F, T> of(a: Provider<A>, b: Provider<B>, c: Provider<C>, d: Provider<D>, e: Provider<E>, f: Provider<F>, weak: Boolean, transform: (A, B, C, D, E, F) -> T): Provider<T> {
            val parentA = a.delegate
            val parentB = b.delegate
            val parentC = c.delegate
            val parentD = d.delegate
            val parentE = e.delegate
            val parentF = f.delegate
            
            if (parentA.isStable && parentB.isStable && parentC.isStable && parentD.isStable && parentE.isStable && parentF.isStable)
                return StableProvider(DeferredValue.Mapped6(parentA.value, parentB.value, parentC.value, parentD.value, parentE.value, parentF.value, transform))
            
            val provider = MultiUnidirectionalTransformingProvider6(parentA, parentB, parentC, parentD, parentE, parentF, transform)
            registerChild(parentA, provider, weak)
            registerChild(parentB, provider, weak)
            registerChild(parentC, provider, weak)
            registerChild(parentD, provider, weak)
            registerChild(parentE, provider, weak)
            registerChild(parentF, provider, weak)
            provider.handleParentUpdated(parentA) // propagate potentially lost update during provider creation and child assignment
            return provider
        }
        
    }
    
}

internal class MultiUnidirectionalTransformingProvider7<A, B, C, D, E, F, G, T> private constructor(
    private val a: Provider<A>,
    private val b: Provider<B>,
    private val c: Provider<C>,
    private val d: Provider<D>,
    private val e: Provider<E>,
    private val f: Provider<F>,
    private val g: Provider<G>,
    private val transform: (A, B, C, D, E, F, G) -> T
) : UnidirectionalProvider<T>() {
    
    override val parents: Set<Provider<*>>
        get() = setOf(a, b, c, d, e, f, g)
    
    @Volatile
    override var value: DeferredValue<T> = DeferredValue.Mapped7(a.value, b.value, c.value, d.value, e.value, f.value, g.value, transform)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.Mapped7(a.value, b.value, c.value, d.value, e.value, f.value, g.value, transform))
    }
    
    companion object {
        
        fun <A, B, C, D, E, F, G, T> of(a: Provider<A>, b: Provider<B>, c: Provider<C>, d: Provider<D>, e: Provider<E>, f: Provider<F>, g: Provider<G>, weak: Boolean, transform: (A, B, C, D, E, F, G) -> T): Provider<T> {
            val parentA = a.delegate
            val parentB = b.delegate
            val parentC = c.delegate
            val parentD = d.delegate
            val parentE = e.delegate
            val parentF = f.delegate
            val parentG = g.delegate
            
            if (parentA.isStable && parentB.isStable && parentC.isStable && parentD.isStable && parentE.isStable && parentF.isStable && parentG.isStable)
                return StableProvider(DeferredValue.Mapped7(parentA.value, parentB.value, parentC.value, parentD.value, parentE.value, parentF.value, parentG.value, transform))
            
            val provider = MultiUnidirectionalTransformingProvider7(parentA, parentB, parentC, parentD, parentE, parentF, parentG, transform)
            registerChild(parentA, provider, weak)
            registerChild(parentB, provider, weak)
            registerChild(parentC, provider, weak)
            registerChild(parentD, provider, weak)
            registerChild(parentE, provider, weak)
            registerChild(parentF, provider, weak)
            registerChild(parentG, provider, weak)
            provider.handleParentUpdated(parentA) // propagate potentially lost update during provider creation and child assignment
            return provider
        }
        
    }
    
}

internal class MultiUnidirectionalTransformingProvider8<A, B, C, D, E, F, G, H, T> private constructor(
    private val a: Provider<A>,
    private val b: Provider<B>,
    private val c: Provider<C>,
    private val d: Provider<D>,
    private val e: Provider<E>,
    private val f: Provider<F>,
    private val g: Provider<G>,
    private val h: Provider<H>,
    private val transform: (A, B, C, D, E, F, G, H) -> T
) : UnidirectionalProvider<T>() {
    
    override val parents: Set<Provider<*>>
        get() = setOf(a, b, c, d, e, f, g, h)
    
    @Volatile
    override var value: DeferredValue<T> = DeferredValue.Mapped8(a.value, b.value, c.value, d.value, e.value, f.value, g.value, h.value, transform)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.Mapped8(a.value, b.value, c.value, d.value, e.value, f.value, g.value, h.value, transform))
    }
    
    companion object {
        
        fun <A, B, C, D, E, F, G, H, T> of(a: Provider<A>, b: Provider<B>, c: Provider<C>, d: Provider<D>, e: Provider<E>, f: Provider<F>, g: Provider<G>, h: Provider<H>, weak: Boolean, transform: (A, B, C, D, E, F, G, H) -> T): Provider<T> {
            val parentA = a.delegate
            val parentB = b.delegate
            val parentC = c.delegate
            val parentD = d.delegate
            val parentE = e.delegate
            val parentF = f.delegate
            val parentG = g.delegate
            val parentH = h.delegate
            
            if (parentA.isStable && parentB.isStable && parentC.isStable && parentD.isStable && parentE.isStable && parentF.isStable && parentG.isStable && parentH.isStable)
                return StableProvider(DeferredValue.Mapped8(parentA.value, parentB.value, parentC.value, parentD.value, parentE.value, parentF.value, parentG.value, parentH.value, transform))
            
            val provider = MultiUnidirectionalTransformingProvider8(parentA, parentB, parentC, parentD, parentE, parentF, parentG, parentH, transform)
            registerChild(parentA, provider, weak)
            registerChild(parentB, provider, weak)
            registerChild(parentC, provider, weak)
            registerChild(parentD, provider, weak)
            registerChild(parentE, provider, weak)
            registerChild(parentF, provider, weak)
            registerChild(parentG, provider, weak)
            registerChild(parentH, provider, weak)
            provider.handleParentUpdated(parentA) // propagate potentially lost update during provider creation and child assignment
            return provider
        }
        
    }
    
}

internal class MultiUnidirectionalTransformingProvider9<A, B, C, D, E, F, G, H, I, T> private constructor(
    private val a: Provider<A>,
    private val b: Provider<B>,
    private val c: Provider<C>,
    private val d: Provider<D>,
    private val e: Provider<E>,
    private val f: Provider<F>,
    private val g: Provider<G>,
    private val h: Provider<H>,
    private val i: Provider<I>,
    private val transform: (A, B, C, D, E, F, G, H, I) -> T
) : UnidirectionalProvider<T>() {
    
    override val parents: Set<Provider<*>>
        get() = setOf(a, b, c, d, e, f, g, h, i)
    
    @Volatile
    override var value: DeferredValue<T> = DeferredValue.Mapped9(a.value, b.value, c.value, d.value, e.value, f.value, g.value, h.value, i.value, transform)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.Mapped9(a.value, b.value, c.value, d.value, e.value, f.value, g.value, h.value, i.value, transform))
    }
    
    companion object {
        
        fun <A, B, C, D, E, F, G, H, I, T> of(a: Provider<A>, b: Provider<B>, c: Provider<C>, d: Provider<D>, e: Provider<E>, f: Provider<F>, g: Provider<G>, h: Provider<H>, i: Provider<I>, weak: Boolean, transform: (A, B, C, D, E, F, G, H, I) -> T): Provider<T> {
            val parentA = a.delegate
            val parentB = b.delegate
            val parentC = c.delegate
            val parentD = d.delegate
            val parentE = e.delegate
            val parentF = f.delegate
            val parentG = g.delegate
            val parentH = h.delegate
            val parentI = i.delegate
            
            if (parentA.isStable && parentB.isStable && parentC.isStable && parentD.isStable && parentE.isStable && parentF.isStable && parentG.isStable && parentH.isStable && parentI.isStable)
                return StableProvider(DeferredValue.Mapped9(parentA.value, parentB.value, parentC.value, parentD.value, parentE.value, parentF.value, parentG.value, parentH.value, parentI.value, transform))
            
            val provider = MultiUnidirectionalTransformingProvider9(parentA, parentB, parentC, parentD, parentE, parentF, parentG, parentH, parentI, transform)
            registerChild(parentA, provider, weak)
            registerChild(parentB, provider, weak)
            registerChild(parentC, provider, weak)
            registerChild(parentD, provider, weak)
            registerChild(parentE, provider, weak)
            registerChild(parentF, provider, weak)
            registerChild(parentG, provider, weak)
            registerChild(parentH, provider, weak)
            registerChild(parentI, provider, weak)
            provider.handleParentUpdated(parentA) // propagate potentially lost update during provider creation and child assignment
            return provider
        }
        
    }
    
}

internal class MultiUnidirectionalTransformingProvider10<A, B, C, D, E, F, G, H, I, J, T> private constructor(
    private val a: Provider<A>,
    private val b: Provider<B>,
    private val c: Provider<C>,
    private val d: Provider<D>,
    private val e: Provider<E>,
    private val f: Provider<F>,
    private val g: Provider<G>,
    private val h: Provider<H>,
    private val i: Provider<I>,
    private val j: Provider<J>,
    private val transform: (A, B, C, D, E, F, G, H, I, J) -> T
) : UnidirectionalProvider<T>() {
    
    override val parents: Set<Provider<*>>
        get() = setOf(a, b, c, d, e, f, g, h, i, j)
    
    @Volatile
    override var value: DeferredValue<T> = DeferredValue.Mapped10(a.value, b.value, c.value, d.value, e.value, f.value, g.value, h.value, i.value, j.value, transform)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.Mapped10(a.value, b.value, c.value, d.value, e.value, f.value, g.value, h.value, i.value, j.value, transform))
    }
    
    companion object {
        
        fun <A, B, C, D, E, F, G, H, I, J, T> of(a: Provider<A>, b: Provider<B>, c: Provider<C>, d: Provider<D>, e: Provider<E>, f: Provider<F>, g: Provider<G>, h: Provider<H>, i: Provider<I>, j: Provider<J>, weak: Boolean, transform: (A, B, C, D, E, F, G, H, I, J) -> T): Provider<T> {
            val parentA = a.delegate
            val parentB = b.delegate
            val parentC = c.delegate
            val parentD = d.delegate
            val parentE = e.delegate
            val parentF = f.delegate
            val parentG = g.delegate
            val parentH = h.delegate
            val parentI = i.delegate
            val parentJ = j.delegate
            
            if (parentA.isStable && parentB.isStable && parentC.isStable && parentD.isStable && parentE.isStable && parentF.isStable && parentG.isStable && parentH.isStable && parentI.isStable && parentJ.isStable)
                return StableProvider(DeferredValue.Mapped10(parentA.value, parentB.value, parentC.value, parentD.value, parentE.value, parentF.value, parentG.value, parentH.value, parentI.value, parentJ.value, transform))
            
            val provider = MultiUnidirectionalTransformingProvider10(parentA, parentB, parentC, parentD, parentE, parentF, parentG, parentH, parentI, parentJ, transform)
            registerChild(parentA, provider, weak)
            registerChild(parentB, provider, weak)
            registerChild(parentC, provider, weak)
            registerChild(parentD, provider, weak)
            registerChild(parentE, provider, weak)
            registerChild(parentF, provider, weak)
            registerChild(parentG, provider, weak)
            registerChild(parentH, provider, weak)
            registerChild(parentI, provider, weak)
            registerChild(parentJ, provider, weak)
            provider.handleParentUpdated(parentA) // propagate potentially lost update during provider creation and child assignment
            return provider
        }
        
    }
    
}

