@file:JvmName("Providers")
@file:JvmMultifileClass

package xyz.xenondevs.commons.provider

import xyz.xenondevs.commons.provider.impl.MultiUnidirectionalTransformingProvider
import xyz.xenondevs.commons.provider.impl.MultiUnidirectionalTransformingProvider10
import xyz.xenondevs.commons.provider.impl.MultiUnidirectionalTransformingProvider2
import xyz.xenondevs.commons.provider.impl.MultiUnidirectionalTransformingProvider3
import xyz.xenondevs.commons.provider.impl.MultiUnidirectionalTransformingProvider4
import xyz.xenondevs.commons.provider.impl.MultiUnidirectionalTransformingProvider5
import xyz.xenondevs.commons.provider.impl.MultiUnidirectionalTransformingProvider6
import xyz.xenondevs.commons.provider.impl.MultiUnidirectionalTransformingProvider7
import xyz.xenondevs.commons.provider.impl.MultiUnidirectionalTransformingProvider8
import xyz.xenondevs.commons.provider.impl.MultiUnidirectionalTransformingProvider9
import java.lang.ref.WeakReference

/**
 * Creates and returns a new [Provider] that combines all values of [providers]
 * and immediately maps them to [R] using [mapValue].
 * 
 * [mapValue] should be a pure function.
 */
fun <T, R> strongCombinedProvider(providers: List<Provider<T>>, mapValue: (List<T>) -> R): Provider<R> =
    MultiUnidirectionalTransformingProvider.of(providers, false, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a] and [b]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 */
fun <A, B, R> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    mapValue: (A, B) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider2.of(a, b, false, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b] and [c]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 */
fun <A, B, C, R> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    mapValue: (A, B, C) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider3.of(a, b, c, false, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c] and [d]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 */
fun <A, B, C, D, R> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    mapValue: (A, B, C, D) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider4.of(a, b, c, d, false, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d] and [e]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 */
fun <A, B, C, D, E, R> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    mapValue: (A, B, C, D, E) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider5.of(a, b, c, d, e, false, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e] and [f]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 */
fun <A, B, C, D, E, F, R> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    mapValue: (A, B, C, D, E, F) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider6.of(a, b, c, d, e, f, false, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f] and [g]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 */
fun <A, B, C, D, E, F, G, R> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    mapValue: (A, B, C, D, E, F, G) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider7.of(a, b, c, d, e, f, g, false, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g] and [h]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 */
fun <A, B, C, D, E, F, G, H, R> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>,
    mapValue: (A, B, C, D, E, F, G, H) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider8.of(a, b, c, d, e, f, g, h, false, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g], [h] and [i]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 */
fun <A, B, C, D, E, F, G, H, I, R> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>,
    i: Provider<I>,
    mapValue: (A, B, C, D, E, F, G, H, I) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider9.of(a, b, c, d, e, f, g, h, i, false, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g], [h], [i] and [j]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 */
fun <A, B, C, D, E, F, G, H, I, J, R> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>,
    i: Provider<I>,
    j: Provider<J>,
    mapValue: (A, B, C, D, E, F, G, H, I, J) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider10.of(a, b, c, d, e, f, g, h, i, j, false, mapValue)

/**
 * Creates and returns a new [Provider] that combines all values of [providers]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <T, R> combinedProvider(providers: List<Provider<T>>, mapValue: (List<T>) -> R): Provider<R> =
    MultiUnidirectionalTransformingProvider.of(providers, true, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a] and [b]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    mapValue: (A, B) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider2.of(a, b, true, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b] and [c]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    mapValue: (A, B, C) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider3.of(a, b, c, true, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c] and [d]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    mapValue: (A, B, C, D) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider4.of(a, b, c, d, true, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d] and [e]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    mapValue: (A, B, C, D, E) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider5.of(a, b, c, d, e, true, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e] and [f]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, F, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    mapValue: (A, B, C, D, E, F) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider6.of(a, b, c, d, e, f, true, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f] and [g]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, F, G, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    mapValue: (A, B, C, D, E, F, G) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider7.of(a, b, c, d, e, f, g, true, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g] and [h]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, F, G, H, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>,
    mapValue: (A, B, C, D, E, F, G, H) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider8.of(a, b, c, d, e, f, g, h, true, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g], [h] and [i]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, F, G, H, I, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>,
    i: Provider<I>,
    mapValue: (A, B, C, D, E, F, G, H, I) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider9.of(a, b, c, d, e, f, g, h, i, true, mapValue)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g], [h], [i] and [j]
 * and immediately maps them to [R] using [mapValue].
 *
 * [mapValue] should be a pure function.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, F, G, H, I, J, R> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>,
    i: Provider<I>,
    j: Provider<J>,
    mapValue: (A, B, C, D, E, F, G, H, I, J) -> R
): Provider<R> = MultiUnidirectionalTransformingProvider10.of(a, b, c, d, e, f, g, h, i, j, true, mapValue)
