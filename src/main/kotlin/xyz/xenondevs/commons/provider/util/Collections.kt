
package xyz.xenondevs.commons.provider.util

import java.util.*

/**
 * Creates a new empty weak hash set with the specified [initialCapacity].
 */
internal fun <E> weakHashSet(initialCapacity: Int): MutableSet<E> =
    Collections.newSetFromMap(WeakHashMap(initialCapacity))

/**
 * Creates a copy of [this][Set] using [createSet] with an additional [element] added to it.
 * Returns itself if the [element] is already contained in the collection.
 */
internal fun <E> Set<E>.with(
    element: E,
    createSingletonSet: (E) -> Set<E>,
    createSet: (Int) -> MutableSet<E>
): Set<E> {
    if (element in this)
        return this
    
    if (isEmpty())
        return createSingletonSet(element)
    
    val newSet = createSet(size + 1)
    newSet += this
    newSet += element
    return newSet
}

/**
 * Creates a copy of [this][Set] using [createSet] with the [element] removed from it.
 * Returns itself if the [element] is not contained in the collection.
 */
internal fun <E> Set<E>.without(
    element: E,
    createSingletonSet: (E) -> Set<E>,
    createSet: (Int) -> MutableSet<E>
): Set<E> {
    if (element !in this)
        return this
    
    return when (size) {
        1 -> emptySet()
        2 -> createSingletonSet(first { it != element })
        else -> {
            val newSet = createSet(size - 1)
            for (e in this) {
                if (e != element)
                    newSet += e
            }
            newSet
        }
    }
}

/**
 * Creates a copy of [this][Map] using [createMap] with an additional [value] added to the set at the given [key].
 * Returns itself if the [value] is already present in the set at the [key].
 */
internal fun <K, V> Map<K, Set<V>>.with(
    key: K,
    value: V,
    createSingletonMap: (K, Set<V>) -> Map<K, Set<V>>,
    createMap: (Int) -> MutableMap<K, Set<V>>,
    createSingletonSet: (V) -> Set<V>,
    createSet: (Int) -> MutableSet<V>
): Map<K, Set<V>> {
    val currentSet = this[key]
    if (currentSet != null && value in currentSet)
        return this // no change required
    
    val newSet = currentSet?.with(value, createSingletonSet, createSet)
        ?: createSingletonSet(value)
    
    // map becomes singleton map || map stays singleton map
    if (isEmpty() || (currentSet != null && size == 1))
        return createSingletonMap(key, newSet)
    
    val newMap = createMap(size + if (currentSet != null) 0 else 1)
    newMap += this
    newMap[key] = newSet
    
    return newMap
}

/**
 * Creates a copy of [this][Map] using [createMap] with the [value] removed from the set at the given [key].
 * If removing the [value] results in an empty list, the [key] is removed from the map.
 * Returns itself if [key] or [value] are not present in the map or set, respectively.
 */
internal fun <K, V> Map<K, Set<V>>.without(
    key: K,
    value: V,
    createSingletonMap: (K, Set<V>) -> Map<K, Set<V>>,
    createMap: (Int) -> MutableMap<K, Set<V>>,
    createSingletonSet: (V) -> Set<V>,
    createSet: (Int) -> MutableSet<V>
): Map<K, Set<V>> {
    val currentSet = this[key]
    if (currentSet.isNullOrEmpty())
        return this
    if (value !in currentSet)
        return this
    
    // key is in map, value is in set under key
    
    val setSize = currentSet.size // >= 1
    val mapSize = size // >= 1
    
    return when {
        // removing the key will result in an empty map
        setSize == 1 && mapSize == 1 -> emptyMap()
        
        // removing the key will result in a singleton map
        setSize == 1 && mapSize == 2 -> {
            val (otherKey, otherValue) = entries.first { it.key != key }
            return createSingletonMap(otherKey, otherValue)
        }
        
        // removing the key will result in a normal map
        setSize == 1 -> {
            // create copy of map with key removed
            val newMap = createMap(size - 1)
            for ((k, v) in this) {
                if (k != key)
                    newMap[k] = v
            }
            
            newMap
        }
        
        // key will stay, and map was already a singleton map
        mapSize == 1 -> createSingletonMap(key, currentSet.without(value, createSingletonSet, createSet))
        
        // key will stay, normal map
        else -> {
            val newMap = createMap(size)
            newMap += this
            newMap[key] = currentSet.without(value, createSingletonSet, createSet)
            newMap
        }
    }
}