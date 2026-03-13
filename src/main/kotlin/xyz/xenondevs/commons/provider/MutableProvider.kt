package xyz.xenondevs.commons.provider

import xyz.xenondevs.commons.tuple.Tuple10
import xyz.xenondevs.commons.tuple.Tuple2
import xyz.xenondevs.commons.tuple.Tuple3
import xyz.xenondevs.commons.tuple.Tuple4
import xyz.xenondevs.commons.tuple.Tuple5
import xyz.xenondevs.commons.tuple.Tuple6
import xyz.xenondevs.commons.tuple.Tuple7
import xyz.xenondevs.commons.tuple.Tuple8
import xyz.xenondevs.commons.tuple.Tuple9
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

/**
 * A [MutableProvider] is a [Provider] that allows [setting][set] the value.
 * 
 * Usage examples:
 *
 * 1:
 * ```kotlin
 * val provider0 = mutableProvider("Hello") // MutableProvider with initial value "Hello"
 * val provider1 = provider0.map { it + ", World!" } // Lazily maps the value of provider0 by appending ", World!" to it
 *
 * println(provider1.get()) // "Hello, World!" (runs provider1 transform lambda)
 * provider0.set("Hi") // Sets the value of provider0 to "Hi", invalidating the cached value of provider1 without running the mapping lambda
 * println(provider1.get()) // "Hi, World!" (runs provider1 transform lambda)
 * ```
 * 
 * 2:
 * ```kotlin
 * val provider0 = mutableProvider(1) // MutableProvider with initial value 1
 * val provider1 = provider0.map({ it * 2 }, { it / 2 }) // Lazily maps the value of provider0 by multiplying it with 2 and untransforming it by dividing by 2
 *
 * println(provider1.get()) // "2" (runs provider1 transform lambda)
 * provider1.set(4) // Sets the value of provider1 to 4, invalidating the cached value of provider0 without running the mapping lambda
 * println(provider0.get()) // "2" (runs provider1 untransform lambda)
 * ```
 * 
 * @see mutableProvider
 */
@SubclassOptInRequired(UnstableProviderApi::class)
interface MutableProvider<T> : Provider<T> {
    
    @UnstableProviderApi
    override val delegate: MutableProvider<T>
        get() = this
    
    /**
     * Creates and returns a new [MutableProvider] that maps the value of [this][MutableProvider]
     * bi-directionally using the provided [transform] and [untransform] functions.
     *
     * [transform] and [untransform] should be pure functions.
     */
    fun <R> strongMap(transform: (T) -> R, untransform: (R) -> T): MutableProvider<R>
    
    /**
     * Creates and returns a new [MutableProvider] that maps the value of [this][MutableProvider]
     * bi-directionally using the provided [transform] and [untransform] functions.
     *
     * [transform] and [untransform] should be pure functions.
     *
     * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
     */
    fun <R> map(transform: (T) -> R, untransform: (R) -> T): MutableProvider<R>
    
    /**
     * Creates and returns a new [Provider] that maps the value of [this][MutableProvider]
     * unidirectionally using the provided [createObservable] function.
     * The value returned by [createObservable] should be an observable type that propagates updates
     * to the updateHandler specified in the function, which will in turn propagate changes from the provider.
     * 
     * [createObservable] should be a pure function.
     * 
     * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
     * 
     * @see [observed]
     */
    fun <R> mapObserved(createObservable: (value: T, updateHandler: () -> Unit) -> R): Provider<R>
    
    /**
     * Creates and returns a new [Provider] that maps the value of [this][MutableProvider]
     * unidirectionally using the provided [createObservable] function.
     * The value returned by [createObservable] should be an observable type that propagates updates
     * to the updateHandler specified in the function, which will in turn propagate changes from the provider.
     *
     * [createObservable] should be a pure function.
     * 
     * @see [strongObserved]
     */
    fun <R> strongMapObserved(createObservable: (value: T, updateHandler: () -> Unit) -> R): Provider<R>
    
    //<editor-fold desc="decompose">
    /**
     * Decomposes [this][MutableProvider] into a list of [size][size] [MutableProviders][MutableProvider]
     * using the provided [decompose] and [recompose] functions.
     *
     * [decompose] and [recompose] should be pure functions whose components are independent:
     * modifying one component and round-tripping through [recompose] and [decompose] must not
     * alter any other component. This allows updating a single component without invalidating its siblings.
     */
    fun <R> strongDecompose(size: Int, decompose: (T) -> List<R>, recompose: (List<R>) -> T): List<MutableProvider<R>>
    
    /**
     * Decomposes [this][MutableProvider] into two [MutableProviders][MutableProvider]
     * using the provided [decompose] and [recompose] functions.
     *
     * [decompose] and [recompose] should be pure functions whose components are independent:
     * modifying one component and round-tripping through [recompose] and [decompose] must not
     * alter any other component. This allows updating a single component without invalidating its siblings.
     */
    fun <A, B> strongDecompose(
        decompose: (T) -> Tuple2<A, B>,
        recompose: (A, B) -> T
    ): Tuple2<MutableProvider<A>, MutableProvider<B>>
    
    /**
     * Decomposes [this][MutableProvider] into three [MutableProviders][MutableProvider]
     * using the provided [decompose] and [recompose] functions.
     *
     * [decompose] and [recompose] should be pure functions whose components are independent:
     * modifying one component and round-tripping through [recompose] and [decompose] must not
     * alter any other component. This allows updating a single component without invalidating its siblings.
     */
    fun <A, B, C> strongDecompose(
        decompose: (T) -> Tuple3<A, B, C>,
        recompose: (A, B, C) -> T
    ): Tuple3<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>>
    
    /**
     * Decomposes [this][MutableProvider] into four [MutableProviders][MutableProvider]
     * using the provided [decompose] and [recompose] functions.
     *
     * [decompose] and [recompose] should be pure functions whose components are independent:
     * modifying one component and round-tripping through [recompose] and [decompose] must not
     * alter any other component. This allows updating a single component without invalidating its siblings.
     */
    fun <A, B, C, D> strongDecompose(
        decompose: (T) -> Tuple4<A, B, C, D>,
        recompose: (A, B, C, D) -> T
    ): Tuple4<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>>
    
    /**
     * Decomposes [this][MutableProvider] into five [MutableProviders][MutableProvider]
     * using the provided [decompose] and [recompose] functions.
     *
     * [decompose] and [recompose] should be pure functions whose components are independent:
     * modifying one component and round-tripping through [recompose] and [decompose] must not
     * alter any other component. This allows updating a single component without invalidating its siblings.
     */
    fun <A, B, C, D, E> strongDecompose(
        decompose: (T) -> Tuple5<A, B, C, D, E>,
        recompose: (A, B, C, D, E) -> T
    ): Tuple5<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>>
    
    /**
     * Decomposes [this][MutableProvider] into six [MutableProviders][MutableProvider]
     * using the provided [decompose] and [recompose] functions.
     *
     * [decompose] and [recompose] should be pure functions whose components are independent:
     * modifying one component and round-tripping through [recompose] and [decompose] must not
     * alter any other component. This allows updating a single component without invalidating its siblings.
     */
    fun <A, B, C, D, E, F> strongDecompose(
        decompose: (T) -> Tuple6<A, B, C, D, E, F>,
        recompose: (A, B, C, D, E, F) -> T
    ): Tuple6<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>>
    
    /**
     * Decomposes [this][MutableProvider] into seven [MutableProviders][MutableProvider]
     * using the provided [decompose] and [recompose] functions.
     *
     * [decompose] and [recompose] should be pure functions whose components are independent:
     * modifying one component and round-tripping through [recompose] and [decompose] must not
     * alter any other component. This allows updating a single component without invalidating its siblings.
     */
    fun <A, B, C, D, E, F, G> strongDecompose(
        decompose: (T) -> Tuple7<A, B, C, D, E, F, G>,
        recompose: (A, B, C, D, E, F, G) -> T
    ): Tuple7<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>, MutableProvider<G>>
    
    /**
     * Decomposes [this][MutableProvider] into eight [MutableProviders][MutableProvider]
     * using the provided [decompose] and [recompose] functions.
     *
     * [decompose] and [recompose] should be pure functions whose components are independent:
     * modifying one component and round-tripping through [recompose] and [decompose] must not
     * alter any other component. This allows updating a single component without invalidating its siblings.
     */
    fun <A, B, C, D, E, F, G, H> strongDecompose(
        decompose: (T) -> Tuple8<A, B, C, D, E, F, G, H>,
        recompose: (A, B, C, D, E, F, G, H) -> T
    ): Tuple8<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>, MutableProvider<G>, MutableProvider<H>>
    
    /**
     * Decomposes [this][MutableProvider] into nine [MutableProviders][MutableProvider]
     * using the provided [decompose] and [recompose] functions.
     *
     * [decompose] and [recompose] should be pure functions whose components are independent:
     * modifying one component and round-tripping through [recompose] and [decompose] must not
     * alter any other component. This allows updating a single component without invalidating its siblings.
     */
    fun <A, B, C, D, E, F, G, H, I> strongDecompose(
        decompose: (T) -> Tuple9<A, B, C, D, E, F, G, H, I>,
        recompose: (A, B, C, D, E, F, G, H, I) -> T
    ): Tuple9<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>, MutableProvider<G>, MutableProvider<H>, MutableProvider<I>>
    
    /**
     * Decomposes [this][MutableProvider] into ten [MutableProviders][MutableProvider]
     * using the provided [decompose] and [recompose] functions.
     *
     * [decompose] and [recompose] should be pure functions whose components are independent:
     * modifying one component and round-tripping through [recompose] and [decompose] must not
     * alter any other component. This allows updating a single component without invalidating its siblings.
     */
    fun <A, B, C, D, E, F, G, H, I, J> strongDecompose(
        decompose: (T) -> Tuple10<A, B, C, D, E, F, G, H, I, J>,
        recompose: (A, B, C, D, E, F, G, H, I, J) -> T
    ): Tuple10<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>, MutableProvider<G>, MutableProvider<H>, MutableProvider<I>, MutableProvider<J>>
    
    /**
     * Decomposes [this][MutableProvider] into a list of [size][size] [MutableProviders][MutableProvider]
     * using the provided [decompose] and [recompose] functions.
     *
     * [decompose] and [recompose] should be pure functions whose components are independent:
     * modifying one component and round-tripping through [recompose] and [decompose] must not
     * alter any other component. This allows updating a single component without invalidating its siblings.
     *
     * The returned providers will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
     */
    fun <R> decompose(size: Int, decompose: (T) -> List<R>, recompose: (List<R>) -> T): List<MutableProvider<R>>
    
    /**
     * Decomposes [this][MutableProvider] into two [MutableProviders][MutableProvider]
     * using the provided [decompose] and [recompose] functions.
     *
     * [decompose] and [recompose] should be pure functions whose components are independent:
     * modifying one component and round-tripping through [recompose] and [decompose] must not
     * alter any other component. This allows updating a single component without invalidating its siblings.
     *
     * The returned providers will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
     */
    fun <A, B> decompose(
        decompose: (T) -> Tuple2<A, B>,
        recompose: (A, B) -> T
    ): Tuple2<MutableProvider<A>, MutableProvider<B>>
    
    /**
     * Decomposes [this][MutableProvider] into three [MutableProviders][MutableProvider]
     * using the provided [decompose] and [recompose] functions.
     *
     * [decompose] and [recompose] should be pure functions whose components are independent:
     * modifying one component and round-tripping through [recompose] and [decompose] must not
     * alter any other component. This allows updating a single component without invalidating its siblings.
     *
     * The returned providers will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
     */
    fun <A, B, C> decompose(
        decompose: (T) -> Tuple3<A, B, C>,
        recompose: (A, B, C) -> T
    ): Tuple3<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>>
    
    /**
     * Decomposes [this][MutableProvider] into four [MutableProviders][MutableProvider]
     * using the provided [decompose] and [recompose] functions.
     *
     * [decompose] and [recompose] should be pure functions whose components are independent:
     * modifying one component and round-tripping through [recompose] and [decompose] must not
     * alter any other component. This allows updating a single component without invalidating its siblings.
     *
     * The returned providers will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
     */
    fun <A, B, C, D> decompose(
        decompose: (T) -> Tuple4<A, B, C, D>,
        recompose: (A, B, C, D) -> T
    ): Tuple4<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>>
    
    /**
     * Decomposes [this][MutableProvider] into five [MutableProviders][MutableProvider]
     * using the provided [decompose] and [recompose] functions.
     *
     * [decompose] and [recompose] should be pure functions whose components are independent:
     * modifying one component and round-tripping through [recompose] and [decompose] must not
     * alter any other component. This allows updating a single component without invalidating its siblings.
     *
     * The returned providers will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
     */
    fun <A, B, C, D, E> decompose(
        decompose: (T) -> Tuple5<A, B, C, D, E>,
        recompose: (A, B, C, D, E) -> T
    ): Tuple5<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>>
    
    /**
     * Decomposes [this][MutableProvider] into six [MutableProviders][MutableProvider]
     * using the provided [decompose] and [recompose] functions.
     *
     * [decompose] and [recompose] should be pure functions whose components are independent:
     * modifying one component and round-tripping through [recompose] and [decompose] must not
     * alter any other component. This allows updating a single component without invalidating its siblings.
     *
     * The returned providers will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
     */
    fun <A, B, C, D, E, F> decompose(
        decompose: (T) -> Tuple6<A, B, C, D, E, F>,
        recompose: (A, B, C, D, E, F) -> T
    ): Tuple6<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>>
    
    /**
     * Decomposes [this][MutableProvider] into seven [MutableProviders][MutableProvider]
     * using the provided [decompose] and [recompose] functions.
     *
     * [decompose] and [recompose] should be pure functions whose components are independent:
     * modifying one component and round-tripping through [recompose] and [decompose] must not
     * alter any other component. This allows updating a single component without invalidating its siblings.
     *
     * The returned providers will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
     */
    fun <A, B, C, D, E, F, G> decompose(
        decompose: (T) -> Tuple7<A, B, C, D, E, F, G>,
        recompose: (A, B, C, D, E, F, G) -> T
    ): Tuple7<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>, MutableProvider<G>>
    
    /**
     * Decomposes [this][MutableProvider] into eight [MutableProviders][MutableProvider]
     * using the provided [decompose] and [recompose] functions.
     *
     * [decompose] and [recompose] should be pure functions whose components are independent:
     * modifying one component and round-tripping through [recompose] and [decompose] must not
     * alter any other component. This allows updating a single component without invalidating its siblings.
     *
     * The returned providers will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
     */
    fun <A, B, C, D, E, F, G, H> decompose(
        decompose: (T) -> Tuple8<A, B, C, D, E, F, G, H>,
        recompose: (A, B, C, D, E, F, G, H) -> T
    ): Tuple8<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>, MutableProvider<G>, MutableProvider<H>>
    
    /**
     * Decomposes [this][MutableProvider] into nine [MutableProviders][MutableProvider]
     * using the provided [decompose] and [recompose] functions.
     *
     * [decompose] and [recompose] should be pure functions whose components are independent:
     * modifying one component and round-tripping through [recompose] and [decompose] must not
     * alter any other component. This allows updating a single component without invalidating its siblings.
     *
     * The returned providers will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
     */
    fun <A, B, C, D, E, F, G, H, I> decompose(
        decompose: (T) -> Tuple9<A, B, C, D, E, F, G, H, I>,
        recompose: (A, B, C, D, E, F, G, H, I) -> T
    ): Tuple9<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>, MutableProvider<G>, MutableProvider<H>, MutableProvider<I>>
    
    /**
     * Decomposes [this][MutableProvider] into ten [MutableProviders][MutableProvider]
     * using the provided [decompose] and [recompose] functions.
     *
     * [decompose] and [recompose] should be pure functions whose components are independent:
     * modifying one component and round-tripping through [recompose] and [decompose] must not
     * alter any other component. This allows updating a single component without invalidating its siblings.
     *
     * The returned providers will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
     */
    fun <A, B, C, D, E, F, G, H, I, J> decompose(
        decompose: (T) -> Tuple10<A, B, C, D, E, F, G, H, I, J>,
        recompose: (A, B, C, D, E, F, G, H, I, J) -> T
    ): Tuple10<MutableProvider<A>, MutableProvider<B>, MutableProvider<C>, MutableProvider<D>, MutableProvider<E>, MutableProvider<F>, MutableProvider<G>, MutableProvider<H>, MutableProvider<I>, MutableProvider<J>>
    //</editor-fold>
    
    /**
     * Listens to updates from [source], updating [this][MutableProvider]'s value to the same value as [source] whenever [source]'s value changes.
     * This provider will keep a strong reference to [source].
     */
    fun consume(source: Provider<T>)
    
    /**
     * Sets the value of [this][MutableProvider] to [value].
     */
    fun set(value: T) {
        update(DeferredValue.Direct(value))
    }
    
    /**
     * Sets the value of [this][MutableProvider] to the result of [lazyValue].
     */
    fun set(lazyValue: () -> T) {
        update(DeferredValue.Lazy(lazyValue))
    }
    
    /**
     * Sets the value of [this][MutableProvider] to [value].
     */
    operator fun <X> setValue(thisRef: X?, property: KProperty<*>, value: T) = set(value)
    
    /**
     * Attempts to update the value of this [MutableProvider] to [value].
     * Fails if the [DeferredValue.seqNo] of [value] is less than the current state of this [MutableProvider]. Equal state succeeds.
     *
     * On success, returns `true` and updates the value while not notifying [ignore].
     */
    fun update(value: DeferredValue<T>, ignore: Set<Provider<*>> = emptySet()): Boolean
    
}