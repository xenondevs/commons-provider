@file:JvmName("Providers")
@file:JvmMultifileClass

package xyz.xenondevs.commons.provider

import java.lang.ref.WeakReference

/**
 * Creates and returns a new [Provider] that maps to the value associated with [key] or `null` if the key is not present.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("mapGet")
operator fun <K, V> Provider<Map<K, V>>.get(key: K): Provider<V?> =
    map { it[key] }

/**
 * Creates and returns a new [Provider] that maps to the value associated with [key] or `null` if the key is not present.
 */
@JvmName("strongMapGet")
fun <K, V> Provider<Map<K, V>>.strongGet(key: K): Provider<V?> =
    strongMap { it[key] }

/**
 * Creates and returns a new [Provider] that maps to the value associated with [key] or `null` if the key is not present.
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("mapGet")
operator fun <K, V> Provider<Map<K, V>>.get(key: Provider<K>): Provider<V?> =
    combinedProvider(this, key) { map, k -> map[k] }

/**
 * Creates and returns a new [Provider] that maps to the value associated with [key] or `null` if the key is not present.
 */
@JvmName("strongMapGet")
fun <K, V> Provider<Map<K, V>>.strongGet(key: Provider<K>): Provider<V?> =
    strongCombinedProvider(this, key) { map, k -> map[k] }

/**
 * Creates and returns a new [Provider] that maps to the value associated with [key] or throws [NoSuchElementException].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("mapGetOrThrow")
fun <K, V> Provider<Map<K, V>>.getOrThrow(key: K): Provider<V> =
    map { it.getValue(key) }

/**
 * Creates and returns a new [Provider] that maps to the value associated with [key] or throws [NoSuchElementException].
 */
@JvmName("strongMapGetOrThrow")
fun <K, V> Provider<Map<K, V>>.strongGetOrThrow(key: K): Provider<V> =
    strongMap { it.getValue(key) }

/**
 * Creates and returns a new [Provider] that maps to the value associated with [key] or throws [NoSuchElementException].
 *
 * The returned provider will only be stored in a [WeakReference] in the parent provider ([this][MutableProvider]).
 */
@JvmName("mapGetOrThrow")
fun <K, V> Provider<Map<K, V>>.getOrThrow(key: Provider<K>): Provider<V> =
    combinedProvider(this, key) { map, k -> map.getValue(k) }

/**
 * Creates and returns a new [Provider] that maps to the value associated with [key] or throws [NoSuchElementException].
 */
@JvmName("strongMapGetOrThrow")
fun <K, V> Provider<Map<K, V>>.strongGetOrThrow(key: Provider<K>): Provider<V> =
    strongCombinedProvider(this, key) { map, k -> map.getValue(k) }
