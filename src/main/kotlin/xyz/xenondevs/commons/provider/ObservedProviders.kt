@file:JvmName("Providers")
@file:JvmMultifileClass

package xyz.xenondevs.commons.provider

import xyz.xenondevs.commons.provider.util.ObservableList
import xyz.xenondevs.commons.provider.util.ObservableMap
import xyz.xenondevs.commons.provider.util.ObservableSet
import java.lang.ref.WeakReference

/**
 * Creates and returns a new [Provider] that observes the list of [this][MutableProvider]
 * and propagates changes appropriately.
 */
@JvmName("strongObservedList")
fun <E> MutableProvider<out MutableList<E>>.strongObserved(): Provider<MutableList<E>> =
    strongMapObserved(::ObservableList)

/**
 * Creates and returns a new [Provider] that observes the list of [this][MutableProvider]
 * and propagates changes appropriately using a lazy copy-on-write strategy.
 */
@JvmName("strongObservedCopyList")
fun <E> MutableProvider<List<E>>.strongObserved(): Provider<MutableList<E>> =
    strongMap({ it.toMutableList() }, { it.toList() }).strongObserved()

/**
 * Creates and returns a new [Provider] that observes the map of [this][MutableProvider]
 * and propagates changes appropriately.
 */
@JvmName("strongObservedMap")
fun <K, V> MutableProvider<out MutableMap<K, V>>.strongObserved(): Provider<MutableMap<K, V>> =
    strongMapObserved(::ObservableMap)

/**
 * Creates and returns a new [Provider] that observes the list of [this][MutableProvider]
 * and propagates changes appropriately using a lazy copy-on-write strategy.
 */
@JvmName("strongObservedCopyMap")
fun <K, V> MutableProvider<Map<K, V>>.strongObserved(): Provider<MutableMap<K, V>> =
    strongMap({ it.toMutableMap() }, { it.toMap() }).strongObserved()

/**
 * Creates and returns a new [Provider] that observes the set of [this][MutableProvider]
 * and propagates changes appropriately.
 */
@JvmName("strongObservedSet")
fun <E> MutableProvider<out MutableSet<E>>.strongObserved(): Provider<MutableSet<E>> =
    strongMapObserved(::ObservableSet)

/**
 * Creates and returns a new [Provider] that observes the list of [this][MutableProvider]
 * and propagates changes appropriately using a lazy copy-on-write strategy.
 */
@JvmName("strongObservedCopySet")
fun <E> MutableProvider<Set<E>>.strongObserved(): Provider<MutableSet<E>> =
    strongMap({ it.toMutableSet() }, { it.toSet() }).strongObserved()

/**
 * Creates and returns a new [Provider] that observes the list of [this][MutableProvider]
 * and propagates changes appropriately.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("observedList")
fun <E> MutableProvider<out MutableList<E>>.observed(): Provider<MutableList<E>> =
    mapObserved(::ObservableList)

/**
 * Creates and returns a new [Provider] that observes the list of [this][MutableProvider]
 * and propagates changes appropriately using a lazy copy-on-write strategy.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("observedCopyList")
fun <E> MutableProvider<List<E>>.observed(): Provider<MutableList<E>> =
    map({ it.toMutableList() }, { it.toList() }).observed()

/**
 * Creates and returns a new [Provider] that observes the map of [this][MutableProvider]
 * and propagates changes appropriately.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("observedMap")
fun <K, V> MutableProvider<out MutableMap<K, V>>.observed(): Provider<MutableMap<K, V>> =
    mapObserved(::ObservableMap)

/**
 * Creates and returns a new [Provider] that observes the list of [this][MutableProvider]
 * and propagates changes appropriately using a lazy copy-on-write strategy.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("observedCopyMap")
fun <K, V> MutableProvider<Map<K, V>>.observed(): Provider<MutableMap<K, V>> =
    map({ it.toMutableMap() }, { it.toMap() }).observed()

/**
 * Creates and returns a new [Provider] that observes the set of [this][MutableProvider]
 * and propagates changes appropriately.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("observedSet")
fun <E> MutableProvider<out MutableSet<E>>.observed(): Provider<MutableSet<E>> =
    mapObserved(::ObservableSet)

/**
 * Creates and returns a new [Provider] that observes the list of [this][MutableProvider]
 * and propagates changes appropriately using a lazy copy-on-write strategy.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("observedCopySet")
fun <E> MutableProvider<Set<E>>.observed(): Provider<MutableSet<E>> =
    map({ it.toMutableSet() }, { it.toSet() }).observed()