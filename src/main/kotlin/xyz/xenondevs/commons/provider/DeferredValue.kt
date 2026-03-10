package xyz.xenondevs.commons.provider

import xyz.xenondevs.commons.provider.DeferredValue.Companion.nextSeqNo
import xyz.xenondevs.commons.provider.DeferredValue.Companion.seqNo
import java.util.concurrent.atomic.AtomicLong

/**
 * A container for a [value] that is possibly computed lazily.
 */
interface DeferredValue<out T> : Comparable<DeferredValue<*>> {
    
    /**
     * The sequence number of this [DeferredValue]. The sequence number defines the "age" of the value,
     * where a lower sequence number indicates an older value. Sequence numbers are unique for each [DeferredValue] chain,
     * but are the same for all [DeferredValues][DeferredValue] that are derived from the same source.
     * Use [nextSeqNo] to generate a new unique sequence number.
     */
    val seqNo: Long
    
    /**
     * The possibly lazily initialized value of this [DeferredValue].
     * Retrieving this value will initialize it if it hasn't been initialized yet.
     * Once initialized, the value will not change.
     */
    val value: T
    
    /**
     * Compares this [DeferredValue] with another [DeferredValue] based on their [seqNo].
     * Equivalent to `seqNo.compareTo(other.seqNo)`.
     */
    override fun compareTo(other: DeferredValue<*>): Int = seqNo.compareTo(other.seqNo)
    
    companion object {
        
        private val seqNo = AtomicLong(0L)
        
        /**
         * Generates the next unique sequence number.
         */
        fun nextSeqNo(): Long = seqNo.incrementAndGet()
        
    }
    
    /**
     * A [DeferredValue] that is directly initialized with a value.
     * Generates a new sequence number when created.
     */
    class Direct<T>(override val value: T) : DeferredValue<T> {
        
        override val seqNo: Long = nextSeqNo()
        
    }
    
    // may be able to take advantage of Stable Values (https://openjdk.org/jeps/502) in the future
    /**
     * A [DeferredValue] that is backed by [lazy].
     * Generates a new sequence number when created.
     */
    class Lazy<T>(lazy: kotlin.Lazy<T>) : DeferredValue<T> {
        
        constructor(initializer: () -> T) : this(lazy(initializer))
        
        override val seqNo: Long = nextSeqNo()
        override val value: T by lazy
        
    }
    
    /**
     * A [DeferredValue] that is the result of applying [transform] to the value of [parent].
     * Inherits the sequence number from [parent].
     */
    class Mapped<P, T>(parent: DeferredValue<P>, transform: (P) -> T) : DeferredValue<T> {
        
        override val seqNo: Long = parent.seqNo
        override val value: T by lazy { transform(parent.value) }
        
    }
    
    //<editor-fold desc="mapped with fixed arity">
    /**
     * A [DeferredValue] that is the result of applying [transform] to the values of [parentA] and [parentB].
     * Inherits the highest sequence number of its parents.
     */
    class Mapped2<A, B, T>(
        private val parentA: DeferredValue<A>,
        private val parentB: DeferredValue<B>,
        private val transform: (A, B) -> T
    ) : DeferredValue<T> {
        
        override val seqNo: Long = maxOf(parentA.seqNo, parentB.seqNo)
        override val value: T by lazy { transform(parentA.value, parentB.value) }
        
    }
    
    /**
     * A [DeferredValue] that is the result of applying [transform] to the values of [parentA], [parentB], and [parentC].
     * Inherits the highest sequence number of its parents.
     */
    class Mapped3<A, B, C, T>(
        private val parentA: DeferredValue<A>,
        private val parentB: DeferredValue<B>,
        private val parentC: DeferredValue<C>,
        private val transform: (A, B, C) -> T
    ) : DeferredValue<T> {
        
        override val seqNo: Long = maxOf(parentA.seqNo, parentB.seqNo, parentC.seqNo)
        override val value: T by lazy { transform(parentA.value, parentB.value, parentC.value) }
        
    }
    
    /**
     * A [DeferredValue] that is the result of applying [transform] to the values of [parentA], [parentB], [parentC], and [parentD].
     * Inherits the highest sequence number of its parents.
     */
    class Mapped4<A, B, C, D, T>(
        private val parentA: DeferredValue<A>,
        private val parentB: DeferredValue<B>,
        private val parentC: DeferredValue<C>,
        private val parentD: DeferredValue<D>,
        private val transform: (A, B, C, D) -> T
    ) : DeferredValue<T> {
        
        override val seqNo: Long = maxOf(parentA.seqNo, parentB.seqNo, parentC.seqNo, parentD.seqNo)
        override val value: T by lazy { transform(parentA.value, parentB.value, parentC.value, parentD.value) }
        
    }
    
    /**
     * A [DeferredValue] that is the result of applying [transform] to the values of [parentA], [parentB], [parentC], [parentD], and [parentE].
     * Inherits the highest sequence number of its parents.
     */
    class Mapped5<A, B, C, D, E, T>(
        private val parentA: DeferredValue<A>,
        private val parentB: DeferredValue<B>,
        private val parentC: DeferredValue<C>,
        private val parentD: DeferredValue<D>,
        private val parentE: DeferredValue<E>,
        private val transform: (A, B, C, D, E) -> T
    ) : DeferredValue<T> {
        
        override val seqNo: Long = maxOf(parentA.seqNo, parentB.seqNo, parentC.seqNo, parentD.seqNo, parentE.seqNo)
        override val value: T by lazy { transform(parentA.value, parentB.value, parentC.value, parentD.value, parentE.value) }
        
    }
    
    /**
     * A [DeferredValue] that is the result of applying [transform] to the values of [parentA], [parentB], [parentC], [parentD], [parentE], and [parentF].
     * Inherits the highest sequence number of its parents.
     */
    class Mapped6<A, B, C, D, E, F, T>(
        private val parentA: DeferredValue<A>,
        private val parentB: DeferredValue<B>,
        private val parentC: DeferredValue<C>,
        private val parentD: DeferredValue<D>,
        private val parentE: DeferredValue<E>,
        private val parentF: DeferredValue<F>,
        private val transform: (A, B, C, D, E, F) -> T
    ) : DeferredValue<T> {
        
        override val seqNo: Long = maxOf(parentA.seqNo, parentB.seqNo, parentC.seqNo, parentD.seqNo, parentE.seqNo, parentF.seqNo)
        override val value: T by lazy { transform(parentA.value, parentB.value, parentC.value, parentD.value, parentE.value, parentF.value) }
        
    }
    
    /**
     * A [DeferredValue] that is the result of applying [transform] to the values of [parentA], [parentB], [parentC], [parentD], [parentE], [parentF], and [parentG].
     * Inherits the highest sequence number of its parents.
     */
    class Mapped7<A, B, C, D, E, F, G, T>(
        private val parentA: DeferredValue<A>,
        private val parentB: DeferredValue<B>,
        private val parentC: DeferredValue<C>,
        private val parentD: DeferredValue<D>,
        private val parentE: DeferredValue<E>,
        private val parentF: DeferredValue<F>,
        private val parentG: DeferredValue<G>,
        private val transform: (A, B, C, D, E, F, G) -> T
    ) : DeferredValue<T> {
        
        override val seqNo: Long = maxOf(parentA.seqNo, parentB.seqNo, parentC.seqNo, parentD.seqNo, parentE.seqNo, parentF.seqNo, parentG.seqNo)
        override val value: T by lazy { transform(parentA.value, parentB.value, parentC.value, parentD.value, parentE.value, parentF.value, parentG.value) }
        
    }
    
    /**
     * A [DeferredValue] that is the result of applying [transform] to the values of [parentA], [parentB], [parentC], [parentD], [parentE], [parentF], [parentG], and [parentH].
     * Inherits the highest sequence number of its parents.
     */
    class Mapped8<A, B, C, D, E, F, G, H, T>(
        private val parentA: DeferredValue<A>,
        private val parentB: DeferredValue<B>,
        private val parentC: DeferredValue<C>,
        private val parentD: DeferredValue<D>,
        private val parentE: DeferredValue<E>,
        private val parentF: DeferredValue<F>,
        private val parentG: DeferredValue<G>,
        private val parentH: DeferredValue<H>,
        private val transform: (A, B, C, D, E, F, G, H) -> T
    ) : DeferredValue<T> {
        
        override val seqNo: Long = maxOf(parentA.seqNo, parentB.seqNo, parentC.seqNo, parentD.seqNo, parentE.seqNo, parentF.seqNo, parentG.seqNo, parentH.seqNo)
        override val value: T by lazy { transform(parentA.value, parentB.value, parentC.value, parentD.value, parentE.value, parentF.value, parentG.value, parentH.value) }
        
    }
    
    /**
     * A [DeferredValue] that is the result of applying [transform] to the values of [parentA], [parentB], [parentC], [parentD], [parentE], [parentF], [parentG], [parentH], and [parentI].
     * Inherits the highest sequence number of its parents.
     */
    class Mapped9<A, B, C, D, E, F, G, H, I, T>(
        private val parentA: DeferredValue<A>,
        private val parentB: DeferredValue<B>,
        private val parentC: DeferredValue<C>,
        private val parentD: DeferredValue<D>,
        private val parentE: DeferredValue<E>,
        private val parentF: DeferredValue<F>,
        private val parentG: DeferredValue<G>,
        private val parentH: DeferredValue<H>,
        private val parentI: DeferredValue<I>,
        private val transform: (A, B, C, D, E, F, G, H, I) -> T
    ) : DeferredValue<T> {
        
        override val seqNo: Long = maxOf(parentA.seqNo, parentB.seqNo, parentC.seqNo, parentD.seqNo, parentE.seqNo, parentF.seqNo, parentG.seqNo, parentH.seqNo, parentI.seqNo)
        override val value: T by lazy { transform(parentA.value, parentB.value, parentC.value, parentD.value, parentE.value, parentF.value, parentG.value, parentH.value, parentI.value) }
        
    }
    
    /**
     * A [DeferredValue] that is the result of applying [transform] to the values of [parentA], [parentB], [parentC], [parentD], [parentE], [parentF], [parentG], [parentH], [parentI], and [parentJ].
     * Inherits the highest sequence number of its parents.
     */
    class Mapped10<A, B, C, D, E, F, G, H, I, J, T>(
        private val parentA: DeferredValue<A>,
        private val parentB: DeferredValue<B>,
        private val parentC: DeferredValue<C>,
        private val parentD: DeferredValue<D>,
        private val parentE: DeferredValue<E>,
        private val parentF: DeferredValue<F>,
        private val parentG: DeferredValue<G>,
        private val parentH: DeferredValue<H>,
        private val parentI: DeferredValue<I>,
        private val parentJ: DeferredValue<J>,
        private val transform: (A, B, C, D, E, F, G, H, I, J) -> T
    ) : DeferredValue<T> {
        
        override val seqNo: Long = maxOf(parentA.seqNo, parentB.seqNo, parentC.seqNo, parentD.seqNo, parentE.seqNo, parentF.seqNo, parentG.seqNo, parentH.seqNo, parentI.seqNo, parentJ.seqNo)
        override val value: T by lazy { transform(parentA.value, parentB.value, parentC.value, parentD.value, parentE.value, parentF.value, parentG.value, parentH.value, parentI.value, parentJ.value) }
        
    }
    //</editor-fold>
    
    /**
     * A [DeferredValue] that is the result of applying [transform] to the values of all [parents].
     * Inherits the highest sequence number of all [parents].
     */
    class MappedMulti<P, T>(parents: List<DeferredValue<P>>, transform: (List<P>) -> T) : DeferredValue<T> {
        
        override val seqNo: Long = parents.maxOf { it.seqNo }
        override val value: T by lazy { transform(parents.map { it.value }) }
        
    }
    
    /**
     * A [DeferredValue] that delegates its value to [parent] but generates a new sequence number when created.
     */
    class ReEmitted<T>(private val parent: DeferredValue<T>) : DeferredValue<T> {
        
        override val seqNo: Long = nextSeqNo()
        override val value: T get() = parent.value
        
    }
    
}