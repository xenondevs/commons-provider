@file:JvmName("Providers")
@file:JvmMultifileClass

package xyz.xenondevs.commons.provider

import java.lang.ref.WeakReference

/**
 * Creates and returns a new [Provider] that maps each element of the [Collection] obtained from [this][Provider]
 * using the [transform] function.
 *
 * [transform] should be a pure function.
 */
inline fun <T, R> Provider<Collection<T>>.strongMapEach(crossinline transform: (T) -> R): Provider<List<R>> =
    strongMapEachTo({ size -> ArrayList(size) }, transform)

/**
 * Creates and returns a new [Provider] that maps each element of the [Collection] obtained from [this][Provider]
 * using the [transform] function.
 *
 * [transform] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
inline fun <T, R> Provider<Collection<T>>.mapEach(crossinline transform: (T) -> R): Provider<List<R>> =
    mapEachTo({ size -> ArrayList(size) }, transform)

/**
 * Creates and returns a new [Provider] that maps each element of the [Collection] obtained from [this][Provider]
 * using the [transform] function and adds the results to a collection created by [makeCollection].
 *
 * [makeCollection] and [transform] should be pure functions.
 */
inline fun <T, R, C : MutableCollection<in R>> Provider<Collection<T>>.strongMapEachTo(
    crossinline makeCollection: (size: Int) -> C,
    crossinline transform: (T) -> R
): Provider<C> = strongMap { it.mapTo(makeCollection(it.size), transform) }

/**
 * Creates and returns a new [Provider] that maps each element of the [Collection] obtained from [this][Provider]
 * using the [transform] function and adds the results to a collection created by [makeCollection].
 *
 * [makeCollection] and [transform] should be pure functions.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
inline fun <T, R, C : MutableCollection<in R>> Provider<Collection<T>>.mapEachTo(
    crossinline makeCollection: (size: Int) -> C,
    crossinline transform: (T) -> R
): Provider<C> = map { it.mapTo(makeCollection(it.size), transform) }

/**
 * Creates and returns a new [Provider] that maps each element of the [Collection] obtained from [this][Provider]
 * using the [transform] function and filters out all null results.
 *
 * [transform] should be a pure function.
 */
inline fun <T, R : Any> Provider<Collection<T>>.strongMapEachNotNull(crossinline transform: (T) -> R?): Provider<List<R>> =
    strongMapEachNotNullTo(::ArrayList, transform)

/**
 * Creates and returns a new [Provider] that maps each element of the [Collection] obtained from [this][Provider]
 * using the [transform] function and filters out all null results.
 *
 * [transform] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
inline fun <T, R : Any> Provider<Collection<T>>.mapEachNotNull(crossinline transform: (T) -> R?): Provider<List<R>> =
    mapEachNotNullTo(::ArrayList, transform)

/**
 * Creates and returns a new [Provider] that maps each element of the [Collection] obtained from [this][Provider]
 * using the [transform] function and filters out all null results.
 * The results are added to a collection created by [makeCollection].
 *
 * [makeCollection] and [transform] should be pure functions.
 */
inline fun <T, R : Any, C : MutableCollection<in R>> Provider<Collection<T>>.strongMapEachNotNullTo(
    crossinline makeCollection: (size: Int) -> C,
    crossinline transform: (T) -> R?
): Provider<C> = strongMap { it.mapNotNullTo(makeCollection(it.size), transform) }

/**
 * Creates and returns a new [Provider] that maps each element of the [Collection] obtained from [this][Provider]
 * using the [transform] function and filters out all null results.
 * The results are added to a collection created by [makeCollection].
 *
 * [makeCollection] and [transform] should be pure functions.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
inline fun <T, R : Any, C : MutableCollection<in R>> Provider<Collection<T>>.mapEachNotNullTo(
    crossinline makeCollection: (size: Int) -> C,
    crossinline transform: (T) -> R?
): Provider<C> = map { it.mapNotNullTo(makeCollection(it.size), transform) }

/**
 * Creates and returns a new [Provider] that maps each element and its index of the [Collection]
 * obtained from [this][Provider] using the [transform] function.
 *
 * [transform] should be a pure function.
 */
inline fun <T, R> Provider<Collection<T>>.strongMapEachIndexed(crossinline transform: (Int, T) -> R): Provider<List<R>> =
    strongMapEachIndexedTo({ size -> ArrayList(size) }, transform)

/**
 * Creates and returns a new [Provider] that maps each element and its index of the [Collection]
 * obtained from [this][Provider] using the [transform] function.
 *
 * [transform] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
inline fun <T, R> Provider<Collection<T>>.mapEachIndexed(crossinline transform: (Int, T) -> R): Provider<List<R>> =
    mapEachIndexedTo({ size -> ArrayList(size) }, transform)

/**
 * Creates and returns a new [Provider] that maps each element and its index of the [Collection]
 * obtained from [this][Provider] using the [transform] function and adds the results to a collection created by [makeCollection].
 *
 * [makeCollection] and [transform] should be pure functions.
 */
inline fun <T, R, C : MutableCollection<in R>> Provider<Collection<T>>.strongMapEachIndexedTo(
    crossinline makeCollection: (size: Int) -> C,
    crossinline transform: (Int, T) -> R
): Provider<C> = strongMap { it.mapIndexedTo(makeCollection(it.size), transform) }

/**
 * Creates and returns a new [Provider] that maps each element and its index of the [Collection]
 * obtained from [this][Provider] using the [transform] function and adds the results to a collection created by [makeCollection].
 *
 * [makeCollection] and [transform] should be pure functions.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
inline fun <T, R, C : MutableCollection<in R>> Provider<Collection<T>>.mapEachIndexedTo(
    crossinline makeCollection: (size: Int) -> C,
    crossinline transform: (Int, T) -> R
): Provider<C> = map { it.mapIndexedTo(makeCollection(it.size), transform) }

/**
 * Creates and returns a new [Provider] that flat-maps the elements of the [Collection] obtained from [this][Provider]
 * into a list using the [transform] function.
 *
 * [transform] should be a pure function.
 */
inline fun <T, R> Provider<Collection<T>>.strongFlatMapCollection(crossinline transform: (T) -> Iterable<R>): Provider<List<R>> =
    strongFlatMapCollectionTo({ size -> ArrayList(size) }, transform)

/**
 * Creates and returns a new [Provider] that flat-maps the elements of the [Collection] obtained from [this][Provider]
 * into a list using the [transform] function.
 *
 * [transform] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
inline fun <T, R> Provider<Collection<T>>.flatMapCollection(crossinline transform: (T) -> Iterable<R>): Provider<List<R>> =
    flatMapCollectionTo({ size -> ArrayList(size) }, transform)

/**
 * Creates and returns a new [Provider] that flat-maps the elements of the [Collection] obtained from [this][Provider]
 * into a collection created by [makeCollection] using the [transform] function.
 *
 * [makeCollection] and [transform] should be pure functions.
 */
inline fun <T, R, C : MutableCollection<in R>> Provider<Collection<T>>.strongFlatMapCollectionTo(
    crossinline makeCollection: (size: Int) -> C,
    crossinline transform: (T) -> Iterable<R>
): Provider<C> = strongMap { it.flatMapTo(makeCollection(it.size), transform) }

/**
 * Creates and returns a new [Provider] that flat-maps the elements of the [Collection] obtained from [this][Provider]
 * into a collection created by [makeCollection] using the [transform] function.
 *
 * [makeCollection] and [transform] should be pure functions.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
inline fun <T, R, C : MutableCollection<in R>> Provider<Collection<T>>.flatMapCollectionTo(
    crossinline makeCollection: (size: Int) -> C,
    crossinline transform: (T) -> Iterable<R>
): Provider<C> = map { it.flatMapTo(makeCollection(it.size), transform) }

/**
 * Creates and returns a new [Provider] that flattens the [List] of [Lists][List] obtained from [this][Provider].
 */
fun <T> Provider<Iterable<Iterable<T>>>.strongFlattenIterables(): Provider<List<T>> =
    strongMap { it.flatten() }

/**
 * Creates and returns a new [Provider] that flattens the [List] of [Lists][List] obtained from [this][Provider].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
fun <T> Provider<Iterable<Iterable<T>>>.flattenIterables(): Provider<List<T>> =
    map { it.flatten() }

/**
 * Creates and returns a new [Provider] that merges all [Maps][Map] obtained from [this][Provider] into a single [Map].
 */
fun <K, V> Provider<List<Map<K, V>>>.strongMergeMaps(): Provider<Map<K, V>> =
    strongMergeMapsTo(::HashMap)

/**
 * Creates and returns a new [Provider] that merges all [Maps][Map] obtained from [this][Provider] into a single [Map].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
fun <K, V> Provider<List<Map<K, V>>>.mergeMaps(): Provider<Map<K, V>> =
    mergeMapsTo(::HashMap)

/**
 * Creates and returns a new [Provider] that merges all [Maps][Map] obtained from [this][Provider] into a single [Map],
 * which is created by the [makeMap] function.
 *
 * [makeMap] should be a pure function.
 */
fun <K, V, M : MutableMap<in K, in V>> Provider<List<Map<K, V>>>.strongMergeMapsTo(makeMap: (size: Int) -> M): Provider<M> =
    strongMap { maps ->
        val size = maps.sumOf { it.size }
        val map = makeMap(size)
        maps.forEach(map::putAll)
        map
    }

/**
 * Creates and returns a new [Provider] that merges all [Maps][Map] obtained from [this][Provider] into a single [Map],
 * which is created by the [makeMap] function.
 *
 * [makeMap] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
fun <K, V, M : MutableMap<in K, in V>> Provider<List<Map<K, V>>>.mergeMapsTo(makeMap: (size: Int) -> M): Provider<M> =
    map { maps ->
        val size = maps.sumOf { it.size }
        val map = makeMap(size)
        maps.forEach(map::putAll)
        map
    }

/**
 * Creates and returns a new [Provider] of a [List] with all elements of the original
 * [Iterable] and the given [element].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmSynthetic
@JvmName("iterablePlusElement")
operator fun <T> Provider<Iterable<T>>.plus(element: T): Provider<List<T>> =
    map { it + element }

/**
 * Creates and returns a new [Provider] of a [List] with all elements of the original
 * [Iterable] and the value of [element].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmSynthetic
@JvmName("iterablePlusElementProvider")
operator fun <T> Provider<Iterable<T>>.plus(element: Provider<T>): Provider<List<T>> =
    combinedProvider(this, element) { a, b -> a + b }

/**
 * Creates and returns a new [Provider] of a [List] with all elements of the original
 * [Iterable] and the given [elements].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmSynthetic
@JvmName("iterablePlusIterable")
operator fun <T> Provider<Iterable<T>>.plus(elements: Iterable<T>): Provider<List<T>> =
    map { it + elements }

/**
 * Creates and returns a new [Provider] of a [List] with all elements of the original
 * [Iterable] and the values of [elements].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmSynthetic
@JvmName("iterablePlusIterableProvider")
operator fun <T> Provider<Iterable<T>>.plus(elements: Provider<Iterable<T>>): Provider<List<T>> =
    combinedProvider(this, elements) { a, b -> a + b }

/**
 * Creates and returns a new [Provider] of a [List] with all elements of the original
 * [Iterable] without the given [element].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmSynthetic
@JvmName("iterableMinusElement")
operator fun <T> Provider<Iterable<T>>.minus(element: T): Provider<List<T>> =
    map { it - element }

/**
 * Creates and returns a new [Provider] of a [List] with all elements of the original
 * [Iterable] without the value of [element].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmSynthetic
@JvmName("iterableMinusElementProvider")
operator fun <T> Provider<Iterable<T>>.minus(element: Provider<T>): Provider<List<T>> =
    combinedProvider(this, element) { a, b -> a - b }

/**
 * Creates and returns a new [Provider] of a [List] with all elements of the original
 * [Iterable] without the given [elements].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmSynthetic
@JvmName("iterableMinusIterable")
@Suppress("ConvertArgumentToSet")
operator fun <T> Provider<Iterable<T>>.minus(elements: Iterable<T>): Provider<List<T>> =
    map { it - (elements as? Set<T> ?: elements.toHashSet()) }

/**
 * Creates and returns a new [Provider] of a [List] with all elements of the original
 * [Iterable] without the values of [elements].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmSynthetic
@JvmName("iterableMinusIterableProvider")
@Suppress("ConvertArgumentToSet")
operator fun <T> Provider<Iterable<T>>.minus(elements: Provider<Iterable<T>>): Provider<List<T>> =
    combinedProvider(this, elements) { a, b -> a - (b as? Set<T> ?: b.toHashSet()) }

/**
 * Creates and returns a new [Provider] of a [Set] with all elements of the original
 * [Set] and the given [element].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmSynthetic
@JvmName("setPlusElement")
operator fun <T> Provider<Set<T>>.plus(element: T): Provider<Set<T>> =
    map { it + element }

/**
 * Creates and returns a new [Provider] of a [Set] with all elements of the original
 * [Set] and the value of [element].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmSynthetic
@JvmName("setPlusElementProvider")
operator fun <T> Provider<Set<T>>.plus(element: Provider<T>): Provider<Set<T>> =
    combinedProvider(this, element) { a, b -> a + b }

/**
 * Creates and returns a new [Provider] of a [Set] with all elements of the original
 * [Set] and the given [elements].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmSynthetic
@JvmName("setPlusIterable")
operator fun <T> Provider<Set<T>>.plus(elements: Iterable<T>): Provider<Set<T>> =
    map { it + elements }

/**
 * Creates and returns a new [Provider] of a [Set] with all elements of the original
 * [Set] and the values of [elements].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmSynthetic
@JvmName("setPlusIterableProvider")
operator fun <T> Provider<Set<T>>.plus(elements: Provider<Iterable<T>>): Provider<Set<T>> =
    combinedProvider(this, elements) { a, b -> a + b }

/**
 * Creates and returns a new [Provider] of a [Set] with all elements of the original
 * [Set] without the given [element].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmSynthetic
@JvmName("setMinusElement")
operator fun <T> Provider<Set<T>>.minus(element: T): Provider<Set<T>> =
    map { it - element }

/**
 * Creates and returns a new [Provider] of a [Set] with all elements of the original
 * [Set] without the value of [element].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmSynthetic
@JvmName("setMinusElementProvider")
operator fun <T> Provider<Set<T>>.minus(element: Provider<T>): Provider<Set<T>> =
    combinedProvider(this, element) { a, b -> a - b }

/**
 * Creates and returns a new [Provider] of a [Set] with all elements of the original
 * [Set] without the given [elements].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmSynthetic
@JvmName("setMinusIterable")
@Suppress("ConvertArgumentToSet")
operator fun <T> Provider<Set<T>>.minus(elements: Iterable<T>): Provider<Set<T>> =
    map { it - (elements as? Set<T> ?: elements.toHashSet()) }

/**
 * Creates and returns a new [Provider] of a [Set] with all elements of the original
 * [Set] without the values of [elements].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmSynthetic
@JvmName("setMinusIterableProvider")
@Suppress("ConvertArgumentToSet")
operator fun <T> Provider<Set<T>>.minus(elements: Provider<Iterable<T>>): Provider<Set<T>> =
    combinedProvider(this, elements) { a, b -> a - (b as? Set<T> ?: b.toHashSet()) }