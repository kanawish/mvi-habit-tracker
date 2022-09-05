package utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.reflect.KProperty1

fun <T, V> Flow<T>.mapDistinct(mapper: suspend (T) -> V): Flow<V> = map(mapper).distinctUntilChanged()

fun <T, V> Flow<T>.mapDistinct(prop: KProperty1<T, V>): Flow<V> = map { prop.get(it) }.distinctUntilChanged()
