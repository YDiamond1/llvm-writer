package bitcode;

import java.util.*


class BitsStreamCreator {

    var curbit = 0
    val bytes = mutableListOf<Byte>()


    val stack = mutableListOf<BlockEntry>()
    var curValue : Bits = Bits(mutableListOf())


    data class BlockEntry(
        val prevCodeSize: UInt,
        val startSizeWord: Int
    )

    fun writeWord(bits: Bits) {
        // todo : add all endians
        bytes.addAll(bits.value.slice(0..3))
    }

    fun writeByte(bits: Bits) {
        bytes.plus(bits.value[0])
    }

    fun write(bits: Bits) {
        curValue = curValue or (bits.value shl curbit)
        if (curbit + bits.length < 32) {
            curbit += bits.length
            return
        }

        // Add the current word.
        writeWord(curValue)

        curValue = if (curbit > 0) bits.value else 0u
        curbit = (curbit + bits.length) and 31
    }
}
