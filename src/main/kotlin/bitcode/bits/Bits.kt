package bitcode.bits

import bitcode.abbrevs.StandardWidth
import java.util.*
import kotlin.collections.ArrayList


val masks = arrayOf(0, 1, 3, 7, 15, 31, 63, 127, 255)

/**
 * @param value is list of bytes in little endian format
 */
open class Bits(
    val value: MutableList<Byte>, var length: Int
) {

    fun addBits(bits: Bits, length: Int) {
        val i = getByteLocation()
        val iBit = getBitInByte()
        var taken = 0
        if(iBit != 0){
            value[i] = concatBytes(value[i], bits.value[0], iBit)
            taken = 8 - iBit
        }
        if(length > taken) {
            value.addAll(bits.takeBits(taken, length))
        }

        this.length += length
    }

    fun takeBits(begin: Int, end: Int): MutableList<Byte> = copyBytes().shl(begin).end(end - begin)


    fun copyBytes(): MutableList<Byte> = ArrayList(value)

    fun getByteLocation() = length / 8
    fun getBitInByte() = length % 8
    fun flushToLength(end: Int) {
        if(end > length) {
            val byteFlush = if (getBitInByte() != 0) getByteLocation() + 1 else getByteLocation()
            val endByte = if(end % 8 != 0) end/8 + 1 else end / 8
            for(i in byteFlush..endByte){
                value.add(0.toByte())
            }
        }
    }
}


fun concatBytes(dest: Byte, source: Byte, length: Int): Byte {
    val intDest = dest.toInt()
    val intSource = source.toInt()
    return ((intSource shl length) or (intDest and masks[length])).toByte()
}

/**
 * checked
 */
fun MutableList<Byte>.shl(offset: Int): MutableList<Byte> {
    val begin = offset / 8
    val iBit = offset % 8

    if (iBit > 0) {
        this[begin] = this[begin] shr iBit
        for (i in begin until this.lastIndex) {
            this[i] = concatBytes(this[i], this[i + 1], 8 - iBit)
        }
        this[this.lastIndex] = this.last() shr iBit
        return this.slice(begin..this.lastIndex).toMutableList()
    } else {
        return this.slice(begin..this.lastIndex).toMutableList()
    }

}

fun MutableList<Byte>.end(offset: Int): MutableList<Byte> {
    println("ask:$offset - all size: ${this.size * 8}")
    val end = offset / 8
    val iBit = offset % 8
    val sliced = this.slice(0..end).toMutableList()
    if (iBit == 0) {
        return sliced
    }

    sliced[end] = sliced[end] and masks[iBit]

    return sliced
}


infix fun Byte.shr(shift: Int) = (this.toInt() shr shift).toByte()
infix fun Byte.and(mask: Int) = (this.toInt() and mask).toByte()
infix fun Byte.or(byte: Int) = (this.toInt() and byte).toByte()

object True : Bits(mutableListOf(1), 1)
object False : Bits(mutableListOf(0), 1)
object Placeholder : Bits(mutableListOf(0, 0, 0, 0), StandardWidth.BlockSizeWidth.width)

