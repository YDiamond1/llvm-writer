package bitcode

import java.util.*

class Bits(val value: MutableList<Byte>, val length: Int) {

    fun addBits(bits: Bits, length: Int) {
        var i = getByteLocation()
        val iBit = getBitInByte()
        value[i++] = concatBytes(value[i], bits.value[0], iBit)

        copyBytes().
    }


    fun copyBytes() = mutableListOf<Byte>().addAll(value)

    fun getByteLocation() = length / 8
    fun getBitInByte() = length % 8
}


fun concatBytes(dest: Byte, source: Byte, length: Int): Byte {
    val intDest = dest.toInt()
    val intSource = source.toInt()
    return intSource.shr(length).or(intDest).toByte()
}


fun MutableList<Byte>.shr(offset: Int) {
    var begin = offset / 8
    var iBit = offset % 8
    for (i in this.indices){
        this[i] =
    }
}
