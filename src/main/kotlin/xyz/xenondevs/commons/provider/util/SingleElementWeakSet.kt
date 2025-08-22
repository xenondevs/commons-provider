package xyz.xenondevs.commons.provider.util

import java.lang.ref.WeakReference

internal class SingleElementWeakSet<T : Any>(element: T) : Set<T> {
    
    private val ref = WeakReference(element)
    
    override val size
        get() = if (ref.get() != null) 1 else 0
    
    override fun isEmpty(): Boolean =
        ref.get() == null
    
    override fun contains(element: T): Boolean =
        this.ref.get() == element
    
    override fun iterator(): Iterator<T> = object : Iterator<T> {
        private var hasNext = ref.get() != null
        
        override fun hasNext(): Boolean = hasNext
        
        override fun next(): T {
            val element = ref.get()
            if (!hasNext || element == null)
                throw NoSuchElementException()
            hasNext = false
            return element
        }
    }
    
    override fun containsAll(elements: Collection<T>): Boolean {
        val element = ref.get()
        if (element == null)
            return elements.isEmpty()
        
        return elements.isEmpty() || (elements.size == 1 && elements.first() == element)
    }
    
}