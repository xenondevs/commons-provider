@file:JvmName("Providers")
@file:JvmMultifileClass

package xyz.xenondevs.commons.provider

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Converts this [Provider] to a [ReadOnlyProperty] that transforms retrieved values with [getter].
 */
fun <T, R> Provider<T>.asProperty(getter: (T) -> R): ReadOnlyProperty<Any?, R> =
    ReadOnlyProperty { _, _ -> getter(get()) }

/**
 * Converts this [MutableProvider] to a [ReadWriteProperty] that transforms 
 * retrieved values with [getter] and transforms set values with [setter].
 */
fun <T, R> MutableProvider<T>.asProperty(getter: (T) -> R, setter: (R) -> T) = object : ReadWriteProperty<Any?, R> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): R = getter(get())
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: R) = set(setter(value))
}