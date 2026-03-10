@file:JvmName("Providers")
@file:JvmMultifileClass

package xyz.xenondevs.commons.provider

import xyz.xenondevs.commons.provider.impl.MultiUnidirectionalTransformingProvider
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

/**
 * Creates and returns a new [Provider] that combines all values of [providers].
 */
fun <T> strongCombinedProvider(providers: List<Provider<T>>): Provider<List<T>> =
    MultiUnidirectionalTransformingProvider.of(providers, false) { it }

/**
 * Creates and returns a new [Provider] that combines the values of [a] and [b].
 */
fun <A, B> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>
): Provider<Tuple2<A, B>> = strongCombinedProvider(a, b, ::Tuple2)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b] and [c].
 */
fun <A, B, C> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>
): Provider<Tuple3<A, B, C>> = strongCombinedProvider(a, b, c, ::Tuple3)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c] and [d].
 */
fun <A, B, C, D> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>
): Provider<Tuple4<A, B, C, D>> = strongCombinedProvider(a, b, c, d, ::Tuple4)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d] and [e].
 */
fun <A, B, C, D, E> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>
): Provider<Tuple5<A, B, C, D, E>> = strongCombinedProvider(a, b, c, d, e, ::Tuple5)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e] and [f].
 */
fun <A, B, C, D, E, F> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>
): Provider<Tuple6<A, B, C, D, E, F>> = strongCombinedProvider(a, b, c, d, e, f, ::Tuple6)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f] and [g].
 */
fun <A, B, C, D, E, F, G> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>
): Provider<Tuple7<A, B, C, D, E, F, G>> = strongCombinedProvider(a, b, c, d, e, f, g, ::Tuple7)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g] and [h].
 */
fun <A, B, C, D, E, F, G, H> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>
): Provider<Tuple8<A, B, C, D, E, F, G, H>> = strongCombinedProvider(a, b, c, d, e, f, g, h, ::Tuple8)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g], [h] and [i].
 */
fun <A, B, C, D, E, F, G, H, I> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>,
    i: Provider<I>
): Provider<Tuple9<A, B, C, D, E, F, G, H, I>> = strongCombinedProvider(a, b, c, d, e, f, g, h, i, ::Tuple9)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g], [h], [i] and [j].
 */
fun <A, B, C, D, E, F, G, H, I, J> strongCombinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>,
    i: Provider<I>,
    j: Provider<J>
): Provider<Tuple10<A, B, C, D, E, F, G, H, I, J>> = strongCombinedProvider(a, b, c, d, e, f, g, h, i, j, ::Tuple10)

/**
 * Creates and returns a new [Provider] that combines all values of [providers].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <T> combinedProvider(providers: List<Provider<T>>): Provider<List<T>> =
    MultiUnidirectionalTransformingProvider.of(providers, true) { it }

/**
 * Creates and returns a new [Provider] that combines the values of [a] and [b].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B> combinedProvider(
    a: Provider<A>,
    b: Provider<B>
): Provider<Tuple2<A, B>> = combinedProvider(a, b, ::Tuple2)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b] and [c].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>
): Provider<Tuple3<A, B, C>> = combinedProvider(a, b, c, ::Tuple3)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c] and [d].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>
): Provider<Tuple4<A, B, C, D>> = combinedProvider(a, b, c, d, ::Tuple4)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d] and [e].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>
): Provider<Tuple5<A, B, C, D, E>> = combinedProvider(a, b, c, d, e, ::Tuple5)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e] and [f].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, F> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>
): Provider<Tuple6<A, B, C, D, E, F>> = combinedProvider(a, b, c, d, e, f, ::Tuple6)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f] and [g].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, F, G> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>
): Provider<Tuple7<A, B, C, D, E, F, G>> = combinedProvider(a, b, c, d, e, f, g, ::Tuple7)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g] and [h].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, F, G, H> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>
): Provider<Tuple8<A, B, C, D, E, F, G, H>> = combinedProvider(a, b, c, d, e, f, g, h, ::Tuple8)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g], [h] and [i].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, F, G, H, I> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>,
    i: Provider<I>
): Provider<Tuple9<A, B, C, D, E, F, G, H, I>> = combinedProvider(a, b, c, d, e, f, g, h, i, ::Tuple9)

/**
 * Creates and returns a new [Provider] that combines the values of [a], [b], [c], [d], [e], [f], [g], [h], [i] and [j].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent providers.
 */
fun <A, B, C, D, E, F, G, H, I, J> combinedProvider(
    a: Provider<A>,
    b: Provider<B>,
    c: Provider<C>,
    d: Provider<D>,
    e: Provider<E>,
    f: Provider<F>,
    g: Provider<G>,
    h: Provider<H>,
    i: Provider<I>,
    j: Provider<J>
): Provider<Tuple10<A, B, C, D, E, F, G, H, I, J>> = combinedProvider(a, b, c, d, e, f, g, h, i, j, ::Tuple10)
