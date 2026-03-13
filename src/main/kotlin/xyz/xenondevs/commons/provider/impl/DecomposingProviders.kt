@file:OptIn(UnstableProviderApi::class)
@file:Suppress("DuplicatedCode", "UNCHECKED_CAST")

package xyz.xenondevs.commons.provider.impl

import xyz.xenondevs.commons.provider.DeferredValue
import xyz.xenondevs.commons.provider.MutableProvider
import xyz.xenondevs.commons.provider.Provider
import xyz.xenondevs.commons.provider.UnstableProviderApi
import xyz.xenondevs.commons.tuple.Tuple10
import xyz.xenondevs.commons.tuple.Tuple2
import xyz.xenondevs.commons.tuple.Tuple3
import xyz.xenondevs.commons.tuple.Tuple4
import xyz.xenondevs.commons.tuple.Tuple5
import xyz.xenondevs.commons.tuple.Tuple6
import xyz.xenondevs.commons.tuple.Tuple7
import xyz.xenondevs.commons.tuple.Tuple8
import xyz.xenondevs.commons.tuple.Tuple9

internal class DecomposingProviderN<T, R>(
    private val parent: MutableProvider<T>,
    private val decompose: (T) -> List<R>,
    private val recompose: (List<R>) -> T,
    private val index: Int,
    private val siblings: Array<DecomposingProviderN<T, R>?>
) : BidirectionalProvider<R>(
    DeferredValue.Mapped(parent.value) { decompose(it)[index] }
) {
    
    private val allSiblings: Set<Provider<*>> by lazy(LazyThreadSafetyMode.NONE) {
        val set = HashSet<Provider<*>>(siblings.size)
        for (s in siblings) set.add(s!!)
        set
    }
    
    override val parents: Set<Provider<*>>
        get() = setOf(parent)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(
            DeferredValue.Mapped(parent.value) { decompose(it)[index] },
            setOf(parent)
        )
    }
    
    override fun update(value: DeferredValue<R>, ignore: Set<Provider<*>>): Boolean {
        if (super.update(value, ignore) && parent !in ignore) {
            parent.update(
                DeferredValue.MappedMulti(siblings.map { it!!.value }, recompose),
                allSiblings
            )
            return true
        }
        return false
    }
    
    companion object {
        
        fun <T, R> of(
            parent: MutableProvider<T>,
            size: Int,
            weak: Boolean,
            decompose: (T) -> List<R>,
            recompose: (List<R>) -> T
        ): List<MutableProvider<R>> {
            val siblings = arrayOfNulls<DecomposingProviderN<T, R>>(size)
            
            for (i in 0..<size) {
                siblings[i] = DecomposingProviderN(parent, decompose, recompose, i, siblings)
            }
            for (sibling in siblings) {
                if (weak) parent.addWeakChild(sibling!!) else parent.addStrongChild(sibling!!)
            }
            
            // propagate potentially lost update during provider creation and child assignment
            for (sibling in siblings) {
                sibling!!.handleParentUpdated(parent)
            }
            
            return siblings.map { it!! as MutableProvider<R> }
        }
        
    }
    
}

internal class DecomposingProvider2<T, A, B, R>(
    private val parent: MutableProvider<T>,
    private val extract: (T) -> R,
    private val recompose: (A, B) -> T,
    private val siblings: Array<DecomposingProvider2<T, A, B, *>?>
) : BidirectionalProvider<R>(DeferredValue.Mapped(parent.value, extract)) {
    
    private val allSiblings: Set<Provider<*>> by lazy(LazyThreadSafetyMode.NONE) {
        val set = HashSet<Provider<*>>(siblings.size)
        for (s in siblings) set.add(s!!)
        set
    }
    
    override val parents: Set<Provider<*>>
        get() = setOf(parent)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.Mapped(parent.value, extract), setOf(parent))
    }
    
    override fun update(value: DeferredValue<R>, ignore: Set<Provider<*>>): Boolean {
        if (super.update(value, ignore) && parent !in ignore) {
            parent.update(
                DeferredValue.Mapped2(
                    siblings[0]!!.value as DeferredValue<A>,
                    siblings[1]!!.value as DeferredValue<B>
                ) { a, b -> recompose(a, b) },
                allSiblings
            )
            return true
        }
        return false
    }
    
    companion object {
        
        fun <T, A, B> of(
            parent: MutableProvider<T>,
            weak: Boolean,
            decompose: (T) -> Tuple2<A, B>,
            recompose: (A, B) -> T
        ): Tuple2<MutableProvider<A>, MutableProvider<B>> {
            val siblings = arrayOfNulls<DecomposingProvider2<T, A, B, *>>(2)
            
            siblings[0] = DecomposingProvider2(parent, { decompose(it).a }, recompose, siblings)
            siblings[1] = DecomposingProvider2(parent, { decompose(it).b }, recompose, siblings)
            
            for (sibling in siblings) {
                if (weak) parent.addWeakChild(sibling!!) else parent.addStrongChild(sibling!!)
            }
            
            // propagate potentially lost update during provider creation and child assignment
            for (sibling in siblings) {
                sibling!!.handleParentUpdated(parent)
            }
            
            return Tuple2(
                siblings[0]!! as MutableProvider<A>,
                siblings[1]!! as MutableProvider<B>
            )
        }
        
    }
    
}

internal class DecomposingProvider3<T, A, B, C, R>(
    private val parent: MutableProvider<T>,
    private val extract: (T) -> R,
    private val recompose: (A, B, C) -> T,
    private val siblings: Array<DecomposingProvider3<T, A, B, C, *>?>
) : BidirectionalProvider<R>(DeferredValue.Mapped(parent.value, extract)) {
    
    private val allSiblings: Set<Provider<*>> by lazy(LazyThreadSafetyMode.NONE) {
        val set = HashSet<Provider<*>>(siblings.size)
        for (s in siblings) set.add(s!!)
        set
    }
    
    override val parents: Set<Provider<*>>
        get() = setOf(parent)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.Mapped(parent.value, extract), setOf(parent))
    }
    
    override fun update(value: DeferredValue<R>, ignore: Set<Provider<*>>): Boolean {
        if (super.update(value, ignore) && parent !in ignore) {
            parent.update(
                DeferredValue.Mapped3(
                    siblings[0]!!.value as DeferredValue<A>,
                    siblings[1]!!.value as DeferredValue<B>,
                    siblings[2]!!.value as DeferredValue<C>
                ) { a, b, c -> recompose(a, b, c) },
                allSiblings
            )
            return true
        }
        return false
    }
    
    companion object {
        
        fun <T, A, B, C> of(
            parent: MutableProvider<T>,
            weak: Boolean,
            decompose: (T) -> Tuple3<A, B, C>,
            recompose: (A, B, C) -> T
        ): Tuple3<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>> {
            val siblings = arrayOfNulls<DecomposingProvider3<T, A, B, C, *>>(3)
            
            siblings[0] = DecomposingProvider3(parent, { decompose(it).a }, recompose, siblings)
            siblings[1] = DecomposingProvider3(parent, { decompose(it).b }, recompose, siblings)
            siblings[2] = DecomposingProvider3(parent, { decompose(it).c }, recompose, siblings)
            
            for (sibling in siblings) {
                if (weak) parent.addWeakChild(sibling!!) else parent.addStrongChild(sibling!!)
            }
            
            // propagate potentially lost update during provider creation and child assignment
            for (sibling in siblings) {
                sibling!!.handleParentUpdated(parent)
            }
            
            return Tuple3(
                siblings[0]!! as MutableProvider<A>,
                siblings[1]!! as MutableProvider<B>,
                siblings[2]!! as MutableProvider<C>
            )
        }
        
    }
    
}


internal class DecomposingProvider4<T, A, B, C, D, R>(
    private val parent: MutableProvider<T>,
    private val extract: (T) -> R,
    private val recompose: (A, B, C, D) -> T,
    private val siblings: Array<DecomposingProvider4<T, A, B, C, D, *>?>
) : BidirectionalProvider<R>(DeferredValue.Mapped(parent.value, extract)) {
    
    private val allSiblings: Set<Provider<*>> by lazy(LazyThreadSafetyMode.NONE) {
        val set = HashSet<Provider<*>>(siblings.size)
        for (s in siblings) set.add(s!!)
        set
    }
    
    override val parents: Set<Provider<*>>
        get() = setOf(parent)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.Mapped(parent.value, extract), setOf(parent))
    }
    
    override fun update(value: DeferredValue<R>, ignore: Set<Provider<*>>): Boolean {
        if (super.update(value, ignore) && parent !in ignore) {
            parent.update(
                DeferredValue.Mapped4(
                    siblings[0]!!.value as DeferredValue<A>,
                    siblings[1]!!.value as DeferredValue<B>,
                    siblings[2]!!.value as DeferredValue<C>,
                    siblings[3]!!.value as DeferredValue<D>
                ) { a, b, c, d -> recompose(a, b, c, d) },
                allSiblings
            )
            return true
        }
        return false
    }
    
    companion object {
        
        fun <T, A, B, C, D> of(
            parent: MutableProvider<T>,
            weak: Boolean,
            decompose: (T) -> Tuple4<A, B, C, D>,
            recompose: (A, B, C, D) -> T
        ): Tuple4<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>> {
            val siblings = arrayOfNulls<DecomposingProvider4<T, A, B, C, D, *>>(4)
            
            siblings[0] = DecomposingProvider4(parent, { decompose(it).a }, recompose, siblings)
            siblings[1] = DecomposingProvider4(parent, { decompose(it).b }, recompose, siblings)
            siblings[2] = DecomposingProvider4(parent, { decompose(it).c }, recompose, siblings)
            siblings[3] = DecomposingProvider4(parent, { decompose(it).d }, recompose, siblings)
            
            for (sibling in siblings) {
                if (weak) parent.addWeakChild(sibling!!) else parent.addStrongChild(sibling!!)
            }
            
            // propagate potentially lost update during provider creation and child assignment
            for (sibling in siblings) {
                sibling!!.handleParentUpdated(parent)
            }
            
            return Tuple4(
                siblings[0]!! as MutableProvider<A>,
                siblings[1]!! as MutableProvider<B>,
                siblings[2]!! as MutableProvider<C>,
                siblings[3]!! as MutableProvider<D>
            )
        }
        
    }
    
}

internal class DecomposingProvider5<T, A, B, C, D, E, R>(
    private val parent: MutableProvider<T>,
    private val extract: (T) -> R,
    private val recompose: (A, B, C, D, E) -> T,
    private val siblings: Array<DecomposingProvider5<T, A, B, C, D, E, *>?>
) : BidirectionalProvider<R>(DeferredValue.Mapped(parent.value, extract)) {
    
    private val allSiblings: Set<Provider<*>> by lazy(LazyThreadSafetyMode.NONE) {
        val set = HashSet<Provider<*>>(siblings.size)
        for (s in siblings) set.add(s!!)
        set
    }
    
    override val parents: Set<Provider<*>>
        get() = setOf(parent)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.Mapped(parent.value, extract), setOf(parent))
    }
    
    override fun update(value: DeferredValue<R>, ignore: Set<Provider<*>>): Boolean {
        if (super.update(value, ignore) && parent !in ignore) {
            parent.update(
                DeferredValue.Mapped5(
                    siblings[0]!!.value as DeferredValue<A>,
                    siblings[1]!!.value as DeferredValue<B>,
                    siblings[2]!!.value as DeferredValue<C>,
                    siblings[3]!!.value as DeferredValue<D>,
                    siblings[4]!!.value as DeferredValue<E>
                ) { a, b, c, d, e -> recompose(a, b, c, d, e) },
                allSiblings
            )
            return true
        }
        return false
    }
    
    companion object {
        
        fun <T, A, B, C, D, E> of(
            parent: MutableProvider<T>,
            weak: Boolean,
            decompose: (T) -> Tuple5<A, B, C, D, E>,
            recompose: (A, B, C, D, E) -> T
        ): Tuple5<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>> {
            val siblings = arrayOfNulls<DecomposingProvider5<T, A, B, C, D, E, *>>(5)
            
            siblings[0] = DecomposingProvider5(parent, { decompose(it).a }, recompose, siblings)
            siblings[1] = DecomposingProvider5(parent, { decompose(it).b }, recompose, siblings)
            siblings[2] = DecomposingProvider5(parent, { decompose(it).c }, recompose, siblings)
            siblings[3] = DecomposingProvider5(parent, { decompose(it).d }, recompose, siblings)
            siblings[4] = DecomposingProvider5(parent, { decompose(it).e }, recompose, siblings)
            
            for (sibling in siblings) {
                if (weak) parent.addWeakChild(sibling!!) else parent.addStrongChild(sibling!!)
            }
            
            // propagate potentially lost update during provider creation and child assignment
            for (sibling in siblings) {
                sibling!!.handleParentUpdated(parent)
            }
            
            return Tuple5(
                siblings[0]!! as MutableProvider<A>,
                siblings[1]!! as MutableProvider<B>,
                siblings[2]!! as MutableProvider<C>,
                siblings[3]!! as MutableProvider<D>,
                siblings[4]!! as MutableProvider<E>
            )
        }
        
    }
    
}

internal class DecomposingProvider6<T, A, B, C, D, E, F, R>(
    private val parent: MutableProvider<T>,
    private val extract: (T) -> R,
    private val recompose: (A, B, C, D, E, F) -> T,
    private val siblings: Array<DecomposingProvider6<T, A, B, C, D, E, F, *>?>
) : BidirectionalProvider<R>(DeferredValue.Mapped(parent.value, extract)) {
    
    private val allSiblings: Set<Provider<*>> by lazy(LazyThreadSafetyMode.NONE) {
        val set = HashSet<Provider<*>>(siblings.size)
        for (s in siblings) set.add(s!!)
        set
    }
    
    override val parents: Set<Provider<*>>
        get() = setOf(parent)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.Mapped(parent.value, extract), setOf(parent))
    }
    
    override fun update(value: DeferredValue<R>, ignore: Set<Provider<*>>): Boolean {
        if (super.update(value, ignore) && parent !in ignore) {
            parent.update(
                DeferredValue.Mapped6(
                    siblings[0]!!.value as DeferredValue<A>,
                    siblings[1]!!.value as DeferredValue<B>,
                    siblings[2]!!.value as DeferredValue<C>,
                    siblings[3]!!.value as DeferredValue<D>,
                    siblings[4]!!.value as DeferredValue<E>,
                    siblings[5]!!.value as DeferredValue<F>
                ) { a, b, c, d, e, f -> recompose(a, b, c, d, e, f) },
                allSiblings
            )
            return true
        }
        return false
    }
    
    companion object {
        
        fun <T, A, B, C, D, E, F> of(
            parent: MutableProvider<T>,
            weak: Boolean,
            decompose: (T) -> Tuple6<A, B, C, D, E, F>,
            recompose: (A, B, C, D, E, F) -> T
        ): Tuple6<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>> {
            val siblings = arrayOfNulls<DecomposingProvider6<T, A, B, C, D, E, F, *>>(6)
            
            siblings[0] = DecomposingProvider6(parent, { decompose(it).a }, recompose, siblings)
            siblings[1] = DecomposingProvider6(parent, { decompose(it).b }, recompose, siblings)
            siblings[2] = DecomposingProvider6(parent, { decompose(it).c }, recompose, siblings)
            siblings[3] = DecomposingProvider6(parent, { decompose(it).d }, recompose, siblings)
            siblings[4] = DecomposingProvider6(parent, { decompose(it).e }, recompose, siblings)
            siblings[5] = DecomposingProvider6(parent, { decompose(it).f }, recompose, siblings)
            
            for (sibling in siblings) {
                if (weak) parent.addWeakChild(sibling!!) else parent.addStrongChild(sibling!!)
            }
            
            // propagate potentially lost update during provider creation and child assignment
            for (sibling in siblings) {
                sibling!!.handleParentUpdated(parent)
            }
            
            return Tuple6(
                siblings[0]!! as MutableProvider<A>,
                siblings[1]!! as MutableProvider<B>,
                siblings[2]!! as MutableProvider<C>,
                siblings[3]!! as MutableProvider<D>,
                siblings[4]!! as MutableProvider<E>,
                siblings[5]!! as MutableProvider<F>
            )
        }
        
    }
    
}

internal class DecomposingProvider7<T, A, B, C, D, E, F, G, R>(
    private val parent: MutableProvider<T>,
    private val extract: (T) -> R,
    private val recompose: (A, B, C, D, E, F, G) -> T,
    private val siblings: Array<DecomposingProvider7<T, A, B, C, D, E, F, G, *>?>
) : BidirectionalProvider<R>(DeferredValue.Mapped(parent.value, extract)) {
    
    private val allSiblings: Set<Provider<*>> by lazy(LazyThreadSafetyMode.NONE) {
        val set = HashSet<Provider<*>>(siblings.size)
        for (s in siblings) set.add(s!!)
        set
    }
    
    override val parents: Set<Provider<*>>
        get() = setOf(parent)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.Mapped(parent.value, extract), setOf(parent))
    }
    
    override fun update(value: DeferredValue<R>, ignore: Set<Provider<*>>): Boolean {
        if (super.update(value, ignore) && parent !in ignore) {
            parent.update(
                DeferredValue.Mapped7(
                    siblings[0]!!.value as DeferredValue<A>,
                    siblings[1]!!.value as DeferredValue<B>,
                    siblings[2]!!.value as DeferredValue<C>,
                    siblings[3]!!.value as DeferredValue<D>,
                    siblings[4]!!.value as DeferredValue<E>,
                    siblings[5]!!.value as DeferredValue<F>,
                    siblings[6]!!.value as DeferredValue<G>
                ) { a, b, c, d, e, f, g -> recompose(a, b, c, d, e, f, g) },
                allSiblings
            )
            return true
        }
        return false
    }
    
    companion object {
        
        fun <T, A, B, C, D, E, F, G> of(
            parent: MutableProvider<T>,
            weak: Boolean,
            decompose: (T) -> Tuple7<A, B, C, D, E, F, G>,
            recompose: (A, B, C, D, E, F, G) -> T
        ): Tuple7<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>, MutableProvider<G>> {
            val siblings = arrayOfNulls<DecomposingProvider7<T, A, B, C, D, E, F, G, *>>(7)
            
            siblings[0] = DecomposingProvider7(parent, { decompose(it).a }, recompose, siblings)
            siblings[1] = DecomposingProvider7(parent, { decompose(it).b }, recompose, siblings)
            siblings[2] = DecomposingProvider7(parent, { decompose(it).c }, recompose, siblings)
            siblings[3] = DecomposingProvider7(parent, { decompose(it).d }, recompose, siblings)
            siblings[4] = DecomposingProvider7(parent, { decompose(it).e }, recompose, siblings)
            siblings[5] = DecomposingProvider7(parent, { decompose(it).f }, recompose, siblings)
            siblings[6] = DecomposingProvider7(parent, { decompose(it).g }, recompose, siblings)
            
            for (sibling in siblings) {
                if (weak) parent.addWeakChild(sibling!!) else parent.addStrongChild(sibling!!)
            }
            
            // propagate potentially lost update during provider creation and child assignment
            for (sibling in siblings) {
                sibling!!.handleParentUpdated(parent)
            }
            
            return Tuple7(
                siblings[0]!! as MutableProvider<A>,
                siblings[1]!! as MutableProvider<B>,
                siblings[2]!! as MutableProvider<C>,
                siblings[3]!! as MutableProvider<D>,
                siblings[4]!! as MutableProvider<E>,
                siblings[5]!! as MutableProvider<F>,
                siblings[6]!! as MutableProvider<G>
            )
        }
        
    }
    
}

internal class DecomposingProvider8<T, A, B, C, D, E, F, G, H, R>(
    private val parent: MutableProvider<T>,
    private val extract: (T) -> R,
    private val recompose: (A, B, C, D, E, F, G, H) -> T,
    private val siblings: Array<DecomposingProvider8<T, A, B, C, D, E, F, G, H, *>?>
) : BidirectionalProvider<R>(DeferredValue.Mapped(parent.value, extract)) {
    
    private val allSiblings: Set<Provider<*>> by lazy(LazyThreadSafetyMode.NONE) {
        val set = HashSet<Provider<*>>(siblings.size)
        for (s in siblings) set.add(s!!)
        set
    }
    
    override val parents: Set<Provider<*>>
        get() = setOf(parent)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.Mapped(parent.value, extract), setOf(parent))
    }
    
    override fun update(value: DeferredValue<R>, ignore: Set<Provider<*>>): Boolean {
        if (super.update(value, ignore) && parent !in ignore) {
            parent.update(
                DeferredValue.Mapped8(
                    siblings[0]!!.value as DeferredValue<A>,
                    siblings[1]!!.value as DeferredValue<B>,
                    siblings[2]!!.value as DeferredValue<C>,
                    siblings[3]!!.value as DeferredValue<D>,
                    siblings[4]!!.value as DeferredValue<E>,
                    siblings[5]!!.value as DeferredValue<F>,
                    siblings[6]!!.value as DeferredValue<G>,
                    siblings[7]!!.value as DeferredValue<H>
                ) { a, b, c, d, e, f, g, h -> recompose(a, b, c, d, e, f, g, h) },
                allSiblings
            )
            return true
        }
        return false
    }
    
    companion object {
        
        fun <T, A, B, C, D, E, F, G, H> of(
            parent: MutableProvider<T>,
            weak: Boolean,
            decompose: (T) -> Tuple8<A, B, C, D, E, F, G, H>,
            recompose: (A, B, C, D, E, F, G, H) -> T
        ): Tuple8<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>, MutableProvider<G>, MutableProvider<H>> {
            val siblings = arrayOfNulls<DecomposingProvider8<T, A, B, C, D, E, F, G, H, *>>(8)
            
            siblings[0] = DecomposingProvider8(parent, { decompose(it).a }, recompose, siblings)
            siblings[1] = DecomposingProvider8(parent, { decompose(it).b }, recompose, siblings)
            siblings[2] = DecomposingProvider8(parent, { decompose(it).c }, recompose, siblings)
            siblings[3] = DecomposingProvider8(parent, { decompose(it).d }, recompose, siblings)
            siblings[4] = DecomposingProvider8(parent, { decompose(it).e }, recompose, siblings)
            siblings[5] = DecomposingProvider8(parent, { decompose(it).f }, recompose, siblings)
            siblings[6] = DecomposingProvider8(parent, { decompose(it).g }, recompose, siblings)
            siblings[7] = DecomposingProvider8(parent, { decompose(it).h }, recompose, siblings)
            
            for (sibling in siblings) {
                if (weak) parent.addWeakChild(sibling!!) else parent.addStrongChild(sibling!!)
            }
            
            // propagate potentially lost update during provider creation and child assignment
            for (sibling in siblings) {
                sibling!!.handleParentUpdated(parent)
            }
            
            return Tuple8(
                siblings[0]!! as MutableProvider<A>,
                siblings[1]!! as MutableProvider<B>,
                siblings[2]!! as MutableProvider<C>,
                siblings[3]!! as MutableProvider<D>,
                siblings[4]!! as MutableProvider<E>,
                siblings[5]!! as MutableProvider<F>,
                siblings[6]!! as MutableProvider<G>,
                siblings[7]!! as MutableProvider<H>
            )
        }
        
    }
    
}

internal class DecomposingProvider9<T, A, B, C, D, E, F, G, H, I, R>(
    private val parent: MutableProvider<T>,
    private val extract: (T) -> R,
    private val recompose: (A, B, C, D, E, F, G, H, I) -> T,
    private val siblings: Array<DecomposingProvider9<T, A, B, C, D, E, F, G, H, I, *>?>
) : BidirectionalProvider<R>(DeferredValue.Mapped(parent.value, extract)) {
    
    private val allSiblings: Set<Provider<*>> by lazy(LazyThreadSafetyMode.NONE) {
        val set = HashSet<Provider<*>>(siblings.size)
        for (s in siblings) set.add(s!!)
        set
    }
    
    override val parents: Set<Provider<*>>
        get() = setOf(parent)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.Mapped(parent.value, extract), setOf(parent))
    }
    
    override fun update(value: DeferredValue<R>, ignore: Set<Provider<*>>): Boolean {
        if (super.update(value, ignore) && parent !in ignore) {
            parent.update(
                DeferredValue.Mapped9(
                    siblings[0]!!.value as DeferredValue<A>,
                    siblings[1]!!.value as DeferredValue<B>,
                    siblings[2]!!.value as DeferredValue<C>,
                    siblings[3]!!.value as DeferredValue<D>,
                    siblings[4]!!.value as DeferredValue<E>,
                    siblings[5]!!.value as DeferredValue<F>,
                    siblings[6]!!.value as DeferredValue<G>,
                    siblings[7]!!.value as DeferredValue<H>,
                    siblings[8]!!.value as DeferredValue<I>
                ) { a, b, c, d, e, f, g, h, i -> recompose(a, b, c, d, e, f, g, h, i) },
                allSiblings
            )
            return true
        }
        return false
    }
    
    companion object {
        
        fun <T, A, B, C, D, E, F, G, H, I> of(
            parent: MutableProvider<T>,
            weak: Boolean,
            decompose: (T) -> Tuple9<A, B, C, D, E, F, G, H, I>,
            recompose: (A, B, C, D, E, F, G, H, I) -> T
        ): Tuple9<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>, MutableProvider<G>, MutableProvider<H>, MutableProvider<I>> {
            val siblings = arrayOfNulls<DecomposingProvider9<T, A, B, C, D, E, F, G, H, I, *>>(9)
            
            siblings[0] = DecomposingProvider9(parent, { decompose(it).a }, recompose, siblings)
            siblings[1] = DecomposingProvider9(parent, { decompose(it).b }, recompose, siblings)
            siblings[2] = DecomposingProvider9(parent, { decompose(it).c }, recompose, siblings)
            siblings[3] = DecomposingProvider9(parent, { decompose(it).d }, recompose, siblings)
            siblings[4] = DecomposingProvider9(parent, { decompose(it).e }, recompose, siblings)
            siblings[5] = DecomposingProvider9(parent, { decompose(it).f }, recompose, siblings)
            siblings[6] = DecomposingProvider9(parent, { decompose(it).g }, recompose, siblings)
            siblings[7] = DecomposingProvider9(parent, { decompose(it).h }, recompose, siblings)
            siblings[8] = DecomposingProvider9(parent, { decompose(it).i }, recompose, siblings)
            
            for (sibling in siblings) {
                if (weak) parent.addWeakChild(sibling!!) else parent.addStrongChild(sibling!!)
            }
            
            // propagate potentially lost update during provider creation and child assignment
            for (sibling in siblings) {
                sibling!!.handleParentUpdated(parent)
            }
            
            return Tuple9(
                siblings[0]!! as MutableProvider<A>,
                siblings[1]!! as MutableProvider<B>,
                siblings[2]!! as MutableProvider<C>,
                siblings[3]!! as MutableProvider<D>,
                siblings[4]!! as MutableProvider<E>,
                siblings[5]!! as MutableProvider<F>,
                siblings[6]!! as MutableProvider<G>,
                siblings[7]!! as MutableProvider<H>,
                siblings[8]!! as MutableProvider<I>
            )
        }
        
    }
    
}

internal class DecomposingProvider10<T, A, B, C, D, E, F, G, H, I, J, R>(
    private val parent: MutableProvider<T>,
    private val extract: (T) -> R,
    private val recompose: (A, B, C, D, E, F, G, H, I, J) -> T,
    private val siblings: Array<DecomposingProvider10<T, A, B, C, D, E, F, G, H, I, J, *>?>
) : BidirectionalProvider<R>(DeferredValue.Mapped(parent.value, extract)) {
    
    private val allSiblings: Set<Provider<*>> by lazy(LazyThreadSafetyMode.NONE) {
        val set = HashSet<Provider<*>>(siblings.size)
        for (s in siblings) set.add(s!!)
        set
    }
    
    override val parents: Set<Provider<*>>
        get() = setOf(parent)
    
    override fun handleParentUpdated(updatedParent: Provider<*>) {
        update(DeferredValue.Mapped(parent.value, extract), setOf(parent))
    }
    
    override fun update(value: DeferredValue<R>, ignore: Set<Provider<*>>): Boolean {
        if (super.update(value, ignore) && parent !in ignore) {
            parent.update(
                DeferredValue.Mapped10(
                    siblings[0]!!.value as DeferredValue<A>,
                    siblings[1]!!.value as DeferredValue<B>,
                    siblings[2]!!.value as DeferredValue<C>,
                    siblings[3]!!.value as DeferredValue<D>,
                    siblings[4]!!.value as DeferredValue<E>,
                    siblings[5]!!.value as DeferredValue<F>,
                    siblings[6]!!.value as DeferredValue<G>,
                    siblings[7]!!.value as DeferredValue<H>,
                    siblings[8]!!.value as DeferredValue<I>,
                    siblings[9]!!.value as DeferredValue<J>
                ) { a, b, c, d, e, f, g, h, i, j -> recompose(a, b, c, d, e, f, g, h, i, j) },
                allSiblings
            )
            return true
        }
        return false
    }
    
    companion object {
        
        fun <T, A, B, C, D, E, F, G, H, I, J> of(
            parent: MutableProvider<T>,
            weak: Boolean,
            decompose: (T) -> Tuple10<A, B, C, D, E, F, G, H, I, J>,
            recompose: (A, B, C, D, E, F, G, H, I, J) -> T
        ): Tuple10<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>, MutableProvider<G>, MutableProvider<H>, MutableProvider<I>, MutableProvider<J>> {
            val siblings = arrayOfNulls<DecomposingProvider10<T, A, B, C, D, E, F, G, H, I, J, *>>(10)
            
            siblings[0] = DecomposingProvider10(parent, { decompose(it).a }, recompose, siblings)
            siblings[1] = DecomposingProvider10(parent, { decompose(it).b }, recompose, siblings)
            siblings[2] = DecomposingProvider10(parent, { decompose(it).c }, recompose, siblings)
            siblings[3] = DecomposingProvider10(parent, { decompose(it).d }, recompose, siblings)
            siblings[4] = DecomposingProvider10(parent, { decompose(it).e }, recompose, siblings)
            siblings[5] = DecomposingProvider10(parent, { decompose(it).f }, recompose, siblings)
            siblings[6] = DecomposingProvider10(parent, { decompose(it).g }, recompose, siblings)
            siblings[7] = DecomposingProvider10(parent, { decompose(it).h }, recompose, siblings)
            siblings[8] = DecomposingProvider10(parent, { decompose(it).i }, recompose, siblings)
            siblings[9] = DecomposingProvider10(parent, { decompose(it).j }, recompose, siblings)
            
            for (sibling in siblings) {
                if (weak) parent.addWeakChild(sibling!!) else parent.addStrongChild(sibling!!)
            }
            
            // propagate potentially lost update during provider creation and child assignment
            for (sibling in siblings) {
                sibling!!.handleParentUpdated(parent)
            }
            
            return Tuple10(
                siblings[0]!! as MutableProvider<A>,
                siblings[1]!! as MutableProvider<B>,
                siblings[2]!! as MutableProvider<C>,
                siblings[3]!! as MutableProvider<D>,
                siblings[4]!! as MutableProvider<E>,
                siblings[5]!! as MutableProvider<F>,
                siblings[6]!! as MutableProvider<G>,
                siblings[7]!! as MutableProvider<H>,
                siblings[8]!! as MutableProvider<I>,
                siblings[9]!! as MutableProvider<J>
            )
        }
        
    }
    
}

