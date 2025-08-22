package xyz.xenondevs.commons.provider.util

import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference

internal class SingleKeyWeakHashMap<K : Any, V : Any>(key: K, value: V) : Map<K, V> {
    
    private val queue = ReferenceQueue<K>()
    private val ref = WeakReference(key, queue)
    private var value: V? = value
    
    override val size: Int
        get() {
            clean()
            return if (ref.get() != null) 1 else 0
        }
    
    override val keys: Set<K>
        get() {
            clean()
            return ref.get()?.let(::SingleElementWeakSet) ?: emptySet()
        }
    
    override val values: Collection<V>
        get() {
            clean()
            return value?.let(::listOf) ?: emptyList()
        }
    
    override val entries: Set<Map.Entry<K, V>>
        get() {
            val key = ref.get()
            val value = value
            
            if (key == null || value == null) 
                return emptySet()
            
            val entry = object : Map.Entry<K, V> {
                override val key: K = key
                override val value: V = value
            }
            
            return setOf(entry)
        }
    
    override fun isEmpty(): Boolean {
        clean()
        return ref.get() == null
    }
    
    override fun containsKey(key: K): Boolean {
        clean()
        return ref.get() == key
    }
    
    override fun containsValue(value: V): Boolean {
        clean()
        return this.value == value
    }
    
    override fun get(key: K): V? {
        clean()
        
        val thisKey = ref.get()
        val thisValue = value
        
        if (thisKey != null && thisValue != null && thisKey == key)
            return thisValue
        
        return null
    }
    
    private fun clean() {
        if (queue.poll() != null)
            value = null
    }
    
}