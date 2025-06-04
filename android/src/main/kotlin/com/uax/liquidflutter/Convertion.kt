package com.uax.liquidflutter

import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

/**
 * StandardMessageCodec でエンコード可能になるように変換します。
 * エンコードできないオブジェクトは、リフレクションを利用して HashMap に変換されます。
 *
 * オブジェクトのプロパティはすべて public でなくてはなりません。
 */
fun Any?.asEncodable(): Any? {
    if (this == null || this == Unit) return null
    return asEncodable(javaClass.kotlin)
}

private fun <T : Any> T?.asEncodable(kClass: KClass<T>): Any? {
    if (this == null || isTerminalEncodableType(this)) return this

    return when (this) {
        // エンコードできないプリミティブ型はサイズの大きい型へ変換
        // 整数は正にする
        is Byte -> toInt() and 0xFF
        is Short -> toInt() and 0xFFFF
        is Float -> toDouble()
        is ShortArray -> map { it.toInt() and 0xFFFF }.toIntArray()
        is FloatArray -> map { it.toDouble() }.toDoubleArray()
        // 複合型は再帰的に変換
        is Iterable<*> -> map { it.asEncodable() }
        is Map<*, *> -> mapValues { (_, value) -> value.asEncodable() }
        else -> HashMap(
            kClass.memberProperties.associate { prop ->
                prop.name to prop.get(this)?.let { it.asEncodable(it.javaClass.kotlin) }
            }
        )
    }

}

/**
 * StandardMessageCodec でエンコード可能な非複合型なら true
 */
private fun isTerminalEncodableType(value: Any) = value is Boolean || value is Int || value is Long || value is Double ||
        value is String || value is ByteArray || value is IntArray || value is LongArray || value is DoubleArray
