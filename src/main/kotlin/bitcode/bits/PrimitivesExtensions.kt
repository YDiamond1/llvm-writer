package bitcode.primitives

import bitcode.bits.Bits


fun Int.toBits(): Bits = getBits(this.toUInt())

fun UInt.toBits(): Bits = getBits(this)

fun Short.toBits() = getBits(this.toUInt())

fun UShort.toBits() = getBits(this.toUInt())

fun Byte.toBits() = getBits(this.toUInt())

fun UByte.toBits() = getBits(this.toUInt())



fun getSignificantBits(value: UInt): Int {
    var local = value
    var invert = 0
    while (local < 255u) {
        local = local shl 1
        invert++
    }
    return 8 - invert
}

fun getArrayBytes(value: UInt): MutableList<Byte> {
    var local = value
    val list = mutableListOf<Byte>()
    while (local >= 255u) {
        list.add((local).toByte())
        local = local shr 8
    }

    return list
}

fun getBits(value: UInt): Bits {
    val list = getArrayBytes(value)

    val size = list.size * 8 + getSignificantBits(value)
    list.add(value.toByte())

    return Bits(list, size)
}

