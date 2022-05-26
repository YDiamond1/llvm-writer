package bitcode.primitives

import bitcode.abbrevs.StandardWidth
import bitcode.bits.Bits


enum class AbbrevMemberIds(val id: Int, val encoding: Bits?) {
    Literal(0, null),
    Fixed(1, Bits(mutableListOf(1), 3)),
    VBR(2, Bits(mutableListOf(2), 3)),
    Array(3, Bits(mutableListOf(3), 3)),
    Char6(4, Bits(mutableListOf(4), 3)),
    Blob(5, Bits(mutableListOf(5), 3))
}


interface Primitive {
    fun getExtra() : VBR?
    fun getKind(): AbbrevMemberIds
    val value: Bits
}


class Literal() : Primitive {
    override fun getExtra(): VBR? = null

    override fun getKind() = AbbrevMemberIds.Literal
    override val value: Bits
        get() = TODO("Not yet implemented")
}

class Fixed(override val value: Bits) : Primitive {
    override fun getExtra(): VBR = VBR(value.length.toBits(), StandardWidth.AbbrevSizeWidth.width)

    override fun getKind(): AbbrevMemberIds = AbbrevMemberIds.Fixed
}

class VBR(override val value: Bits, val size: Int) : Primitive {
    override fun getExtra(): VBR = VBR(size.toBits(), StandardWidth.AbbrevSizeWidth.width)

    override fun getKind(): AbbrevMemberIds = AbbrevMemberIds.VBR
}

class Char6(override val value: Bits) : Primitive {
    override fun getExtra(): VBR? = null

    override fun getKind(): AbbrevMemberIds = AbbrevMemberIds.Char6
}



class Array(val list: MutableList<Primitive>) {
    fun getAbbrevType() = list[0].getKind()
    fun getAbbrevTypeExtra() = list[0].getExtra()
}

class Blob(val list: MutableList<Byte>) {
    fun getKind(): AbbrevMemberIds = AbbrevMemberIds.Blob
}



