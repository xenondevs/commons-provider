@file:OptIn(UnstableProviderApi::class)

package xyz.xenondevs.commons.provider.impl

import xyz.xenondevs.commons.provider.MutableProvider
import xyz.xenondevs.commons.provider.Provider
import xyz.xenondevs.commons.provider.UnstableProviderApi
import xyz.xenondevs.commons.provider.util.SingleElementWeakSet
import xyz.xenondevs.commons.provider.util.SingleKeyWeakHashMap
import xyz.xenondevs.commons.provider.util.weakHashSet
import xyz.xenondevs.commons.provider.util.with
import xyz.xenondevs.commons.provider.util.without
import xyz.xenondevs.commons.tuple.Tuple10
import xyz.xenondevs.commons.tuple.Tuple2
import xyz.xenondevs.commons.tuple.Tuple3
import xyz.xenondevs.commons.tuple.Tuple4
import xyz.xenondevs.commons.tuple.Tuple5
import xyz.xenondevs.commons.tuple.Tuple6
import xyz.xenondevs.commons.tuple.Tuple7
import xyz.xenondevs.commons.tuple.Tuple8
import xyz.xenondevs.commons.tuple.Tuple9
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Suppress("UNCHECKED_CAST")
internal class UpdateHandlerCollection<T> private constructor(
    val strongSubscribers: Set<(T) -> Unit>,
    val weakSubscribers: Map<Any, Set<(Any, T) -> Unit>>,
    val strongObservers: Set<() -> Unit>,
    val weakObservers: Map<Any, Set<(Any) -> Unit>>,
    val strongChildren: Set<Provider<*>>,
    val weakChildren: Set<Provider<*>>
) {
    
    val children: Set<Provider<*>>
        get() = buildSet { addAll(strongChildren); addAll(weakChildren) }
    
    //<editor-fold desc="withers">
    fun withStrongSubscriber(subscriber: (T) -> Unit) =
        copy(strongSubscribers = strongSubscribers.with(subscriber, ::setOf, HashSet<*>::newHashSet))
    
    fun withoutStrongSubscriber(subscriber: (T) -> Unit) =
        copy(strongSubscribers = strongSubscribers.without(subscriber, ::setOf, HashSet<*>::newHashSet))
    
    
    fun withStrongObserver(observer: () -> Unit) =
        copy(strongObservers = strongObservers.with(observer, ::setOf, HashSet<*>::newHashSet))
    
    fun withoutStrongObserver(observer: () -> Unit) =
        copy(strongObservers = strongObservers.without(observer, ::setOf, HashSet<*>::newHashSet))
    
    
    fun withStrongChild(child: Provider<*>) =
        copy(strongChildren = strongChildren.with(child, ::setOf, HashSet<*>::newHashSet))
    
    fun withoutStrongChild(child: Provider<*>) =
        copy(strongChildren = strongChildren.without(child, ::setOf, HashSet<*>::newHashSet))
    
    
    fun withWeakChild(child: Provider<*>) =
        copy(weakChildren = weakChildren.with(child, ::SingleElementWeakSet, ::weakHashSet))
    
    fun withoutWeakChild(child: Provider<*>) =
        copy(weakChildren = weakChildren.without(child, ::SingleElementWeakSet, ::weakHashSet))
    
    
    fun <O : Any> withWeakSubscriber(owner: O, subscriber: (O, T) -> Unit) =
        copy(weakSubscribers = weakSubscribers.with(owner, subscriber as (Any, T) -> Unit, ::SingleKeyWeakHashMap, WeakHashMap<*, *>::newWeakHashMap, ::setOf, HashSet<*>::newHashSet))
    
    fun <O : Any> withoutWeakSubscriber(owner: O, subscriber: (O, T) -> Unit) =
        copy(weakSubscribers = weakSubscribers.without(owner, subscriber as (Any, T) -> Unit, ::SingleKeyWeakHashMap, WeakHashMap<*, *>::newWeakHashMap, ::setOf, HashSet<*>::newHashSet))
    
    fun withoutWeakSubscriber(owner: Any) =
        copy(weakSubscribers = WeakHashMap(weakSubscribers).apply { remove(owner) })
    
    
    fun <O : Any> withWeakObserver(owner: O, observer: (O) -> Unit) =
        copy(weakObservers = weakObservers.with(owner, observer as (Any) -> Unit, ::SingleKeyWeakHashMap, WeakHashMap<*, *>::newWeakHashMap, ::setOf, HashSet<*>::newHashSet))
    
    fun <O : Any> withoutWeakObserver(owner: O, observer: (O) -> Unit) =
        copy(weakObservers = weakObservers.without(owner, observer as (Any) -> Unit, ::SingleKeyWeakHashMap, WeakHashMap<*, *>::newWeakHashMap, ::setOf, HashSet<*>::newHashSet))
    
    fun withoutWeakObserver(owner: Any) =
        copy(weakObservers = WeakHashMap(weakObservers).apply { remove(owner) })
    //</editor-fold>
    
    private fun copy(
        strongSubscribers: Set<(T) -> Unit> = this.strongSubscribers,
        weakSubscribers: Map<Any, Set<(Any, T) -> Unit>> = this.weakSubscribers,
        strongObservers: Set<() -> Unit> = this.strongObservers,
        weakObservers: Map<Any, Set<(Any) -> Unit>> = this.weakObservers,
        strongChildren: Set<Provider<*>> = this.strongChildren,
        weakChildren: Set<Provider<*>> = this.weakChildren
    ) = UpdateHandlerCollection(strongSubscribers, weakSubscribers, strongObservers, weakObservers, strongChildren, weakChildren)
    
    companion object {
        
        private val EMPTY = UpdateHandlerCollection<Any>(emptySet(), emptyMap(), emptySet(), emptyMap(), emptySet(), emptySet())
        
        fun <T> empty() = EMPTY as UpdateHandlerCollection<T>
        
    }
    
}

/**
 * Base class for all default implementations of [xyz.xenondevs.commons.provider.Provider] that can have children.
 */
internal abstract class AbstractProvider<T> : Provider<T> {
    
    @Volatile
    protected var updateHandlers = UpdateHandlerCollection.empty<T>()
        private set
    
    val addtionalReferences: MutableSet<Any> = ConcurrentHashMap.newKeySet()
    
    override val children: Set<Provider<*>>
        get() = updateHandlers.children
    
    override val isStable: Boolean
        get() = false
    
    //<editor-fold desc="update handler modifications">
    override fun subscribe(action: (T) -> Unit) =
        synchronized(this) { updateHandlers = updateHandlers.withStrongSubscriber(action) }
    
    override fun observe(action: () -> Unit) =
        synchronized(this) { updateHandlers = updateHandlers.withStrongObserver(action) }
    
    override fun <R : Any> subscribeWeak(owner: R, action: (R, T) -> Unit) =
        synchronized(this) { updateHandlers = updateHandlers.withWeakSubscriber(owner, action) }
    
    override fun <R : Any> observeWeak(owner: R, action: (R) -> Unit) =
        synchronized(this) { updateHandlers = updateHandlers.withWeakObserver(owner, action) }
    
    override fun unsubscribe(action: (T) -> Unit) =
        synchronized(this) { updateHandlers = updateHandlers.withoutStrongSubscriber(action) }
    
    override fun unobserve(action: () -> Unit) =
        synchronized(this) { updateHandlers = updateHandlers.withoutStrongObserver(action) }
    
    override fun <R : Any> unsubscribeWeak(owner: R, action: (R, T) -> Unit) =
        synchronized(this) { updateHandlers = updateHandlers.withoutWeakSubscriber(owner, action) }
    
    override fun <R : Any> unobserveWeak(owner: R, action: (R) -> Unit) =
        synchronized(this) { updateHandlers = updateHandlers.withoutWeakObserver(owner, action) }
    
    override fun <R : Any> unsubscribeWeak(owner: R) =
        synchronized(this) { updateHandlers = updateHandlers.withoutWeakSubscriber(owner) }
    
    override fun <R : Any> unobserveWeak(owner: R) =
        synchronized(this) { updateHandlers = updateHandlers.withoutWeakObserver(owner) }
    
    override fun addStrongChild(child: Provider<*>) =
        synchronized(this) { updateHandlers = updateHandlers.withStrongChild(child) }
    
    override fun removeStrongChild(child: Provider<*>) =
        synchronized(this) { updateHandlers = updateHandlers.withoutStrongChild(child) }
    
    override fun addWeakChild(child: Provider<*>) =
        synchronized(this) { updateHandlers = updateHandlers.withWeakChild(child) }
    
    override fun removeWeakChild(child: Provider<*>) =
        synchronized(this) { updateHandlers = updateHandlers.withoutWeakChild(child) }
    //</editor-fold>
    
    /**
     * Notifies all observers, subscribers and children except [ignore] that their parent, which
     * is this provider, has been updated.
     */
    fun UpdateHandlerCollection<T>.notify(ignore: Set<Provider<*>> = emptySet()) {
        // children
        strongChildren.forEach { if (it !in ignore) it.handleParentUpdated(this@AbstractProvider) }
        weakChildren.forEach { if (it !in ignore) it.handleParentUpdated(this@AbstractProvider) }
        
        // observers
        strongObservers.forEach { it() }
        weakObservers.forEach { (owner, actions) -> actions.forEach { it(owner) } }
        
        // subscribers
        if (strongSubscribers.isNotEmpty() || weakSubscribers.isNotEmpty()) {
            runCatching { value.value }.onSuccess { value ->
                strongSubscribers.forEach { it(value) }
                weakSubscribers.forEach { (owner, actions) -> actions.forEach { it(owner, value) } }
            }
        }
    }
    
    //<editor-fold desc="map / flatMap">
    override fun <R> map(transform: (T) -> R): Provider<R> {
        val provider = UnidirectionalTransformingProvider(this, transform)
        addWeakChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment 
        return provider
    }
    
    override fun <R> strongMap(transform: (T) -> R): Provider<R> {
        val provider = UnidirectionalTransformingProvider(this, transform)
        addStrongChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
    override fun <R> immediateFlatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R> {
        val provider = BidirectionalImmediateFlatMappedProvider(this, transform, true)
        addWeakChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
    override fun <R> strongImmediateFlatMapMutable(transform: (T) -> MutableProvider<R>): MutableProvider<R> {
        val provider = BidirectionalImmediateFlatMappedProvider(this, transform, false)
        addStrongChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
    override fun <R> immediateFlatMap(transform: (T) -> Provider<R>): Provider<R> {
        val provider = UnidirectionalImmediateFlatMappedProvider(this, transform, true)
        addWeakChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
    override fun <R> strongImmediateFlatMap(transform: (T) -> Provider<R>): Provider<R> {
        val provider = UnidirectionalImmediateFlatMappedProvider(this, transform, false)
        addStrongChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
    override fun <R> flatMap(transform: (T) -> Provider<R>): Provider<R> {
        val provider = UnidirectionalLazyFlatMappedProvider(this, transform, true)
        addWeakChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
    override fun <R> strongFlatMap(transform: (T) -> Provider<R>): Provider<R> {
        val provider = UnidirectionalLazyFlatMappedProvider(this, transform, false)
        addStrongChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    //</editor-fold>
}

internal interface MutableProviderDefaults<T> : MutableProvider<T> {
    
    override fun <R> strongMap(transform: (T) -> R, untransform: (R) -> T): MutableProvider<R> {
        val provider = BidirectionalTransformingProvider(this, transform, untransform)
        addStrongChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
    override fun <R> map(transform: (T) -> R, untransform: (R) -> T): MutableProvider<R> {
        val provider = BidirectionalTransformingProvider(this, transform, untransform)
        addWeakChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
    override fun <R> strongMapObserved(createObservable: (T, () -> Unit) -> R): Provider<R> {
        val provider = ObservedValueUndirectionalTransformingProvider(this, createObservable)
        addStrongChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
    override fun <R> mapObserved(createObservable: (T, () -> Unit) -> R): Provider<R> {
        val provider = ObservedValueUndirectionalTransformingProvider(this, createObservable)
        addWeakChild(provider)
        provider.handleParentUpdated(this) // propagate potentially lost update during provider creation and child assignment
        return provider
    }
    
    //<editor-fold desc="decompose">
    override fun <R> strongDecompose(size: Int, decompose: (T) -> List<R>, recompose: (List<R>) -> T): List<MutableProvider<R>> =
        DecomposingProviderN.of(this, size, false, decompose, recompose)
    
    override fun <A, B> strongDecompose(decompose: (T) -> Tuple2<A, B>, recompose: (A, B) -> T): Tuple2<MutableProvider<A>, MutableProvider<B>> =
        DecomposingProvider2.of(this, false, decompose, recompose)
    
    override fun <A, B, C> strongDecompose(decompose: (T) -> Tuple3<A, B, C>, recompose: (A, B, C) -> T): Tuple3<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>> =
        DecomposingProvider3.of(this, false, decompose, recompose)
    
    override fun <A, B, C, D> strongDecompose(decompose: (T) -> Tuple4<A, B, C, D>, recompose: (A, B, C, D) -> T): Tuple4<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>> =
        DecomposingProvider4.of(this, false, decompose, recompose)
    
    override fun <A, B, C, D, E> strongDecompose(decompose: (T) -> Tuple5<A, B, C, D, E>, recompose: (A, B, C, D, E) -> T): Tuple5<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>> =
        DecomposingProvider5.of(this, false, decompose, recompose)
    
    override fun <A, B, C, D, E, F> strongDecompose(decompose: (T) -> Tuple6<A, B, C, D, E, F>, recompose: (A, B, C, D, E, F) -> T): Tuple6<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>> =
        DecomposingProvider6.of(this, false, decompose, recompose)
    
    override fun <A, B, C, D, E, F, G> strongDecompose(decompose: (T) -> Tuple7<A, B, C, D, E, F, G>, recompose: (A, B, C, D, E, F, G) -> T): Tuple7<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>, MutableProvider<G>> =
        DecomposingProvider7.of(this, false, decompose, recompose)
    
    override fun <A, B, C, D, E, F, G, H> strongDecompose(decompose: (T) -> Tuple8<A, B, C, D, E, F, G, H>, recompose: (A, B, C, D, E, F, G, H) -> T): Tuple8<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>, MutableProvider<G>, MutableProvider<H>> =
        DecomposingProvider8.of(this, false, decompose, recompose)
    
    override fun <A, B, C, D, E, F, G, H, I> strongDecompose(decompose: (T) -> Tuple9<A, B, C, D, E, F, G, H, I>, recompose: (A, B, C, D, E, F, G, H, I) -> T): Tuple9<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>, MutableProvider<G>, MutableProvider<H>, MutableProvider<I>> =
        DecomposingProvider9.of(this, false, decompose, recompose)
    
    override fun <A, B, C, D, E, F, G, H, I, J> strongDecompose(decompose: (T) -> Tuple10<A, B, C, D, E, F, G, H, I, J>, recompose: (A, B, C, D, E, F, G, H, I, J) -> T): Tuple10<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>, MutableProvider<G>, MutableProvider<H>, MutableProvider<I>, MutableProvider<J>> =
        DecomposingProvider10.of(this, false, decompose, recompose)
    
    override fun <R> decompose(size: Int, decompose: (T) -> List<R>, recompose: (List<R>) -> T): List<MutableProvider<R>> =
        DecomposingProviderN.of(this, size, true, decompose, recompose)
    
    override fun <A, B> decompose(decompose: (T) -> Tuple2<A, B>, recompose: (A, B) -> T): Tuple2<MutableProvider<A>, MutableProvider<B>> =
        DecomposingProvider2.of(this, true, decompose, recompose)
    
    override fun <A, B, C> decompose(decompose: (T) -> Tuple3<A, B, C>, recompose: (A, B, C) -> T): Tuple3<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>> =
        DecomposingProvider3.of(this, true, decompose, recompose)
    
    override fun <A, B, C, D> decompose(decompose: (T) -> Tuple4<A, B, C, D>, recompose: (A, B, C, D) -> T): Tuple4<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>> =
        DecomposingProvider4.of(this, true, decompose, recompose)
    
    override fun <A, B, C, D, E> decompose(decompose: (T) -> Tuple5<A, B, C, D, E>, recompose: (A, B, C, D, E) -> T): Tuple5<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>> =
        DecomposingProvider5.of(this, true, decompose, recompose)
    
    override fun <A, B, C, D, E, F> decompose(decompose: (T) -> Tuple6<A, B, C, D, E, F>, recompose: (A, B, C, D, E, F) -> T): Tuple6<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>> =
        DecomposingProvider6.of(this, true, decompose, recompose)
    
    override fun <A, B, C, D, E, F, G> decompose(decompose: (T) -> Tuple7<A, B, C, D, E, F, G>, recompose: (A, B, C, D, E, F, G) -> T): Tuple7<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>, MutableProvider<G>> =
        DecomposingProvider7.of(this, true, decompose, recompose)
    
    override fun <A, B, C, D, E, F, G, H> decompose(decompose: (T) -> Tuple8<A, B, C, D, E, F, G, H>, recompose: (A, B, C, D, E, F, G, H) -> T): Tuple8<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>, MutableProvider<G>, MutableProvider<H>> =
        DecomposingProvider8.of(this, true, decompose, recompose)
    
    override fun <A, B, C, D, E, F, G, H, I> decompose(decompose: (T) -> Tuple9<A, B, C, D, E, F, G, H, I>, recompose: (A, B, C, D, E, F, G, H, I) -> T): Tuple9<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>, MutableProvider<G>, MutableProvider<H>, MutableProvider<I>> =
        DecomposingProvider9.of(this, true, decompose, recompose)
    
    override fun <A, B, C, D, E, F, G, H, I, J> decompose(decompose: (T) -> Tuple10<A, B, C, D, E, F, G, H, I, J>, recompose: (A, B, C, D, E, F, G, H, I, J) -> T): Tuple10<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>, MutableProvider<G>, MutableProvider<H>, MutableProvider<I>, MutableProvider<J>> =
        DecomposingProvider10.of(this, true, decompose, recompose)
    //</editor-fold>
    
    override fun consume(source: Provider<T>) {
        source.observeWeak(this) { thisRef -> thisRef.update(source.value, setOf(source)) }
        (this as AbstractProvider<*>).addtionalReferences += source
    }
    
}