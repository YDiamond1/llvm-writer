package bitcode

import bitcode.primitives.AbbrevMember
import bitcode.primitives.VBR
import java.util.BitSet

fun encodeDefineAbbrev(members: List<AbbrevMember>) : BitSet {
    val size = members.size
    BitSet
}

fun encodeDefineAbbrev(record: Record) : BitSet {
    return encodeDefineAbbrev(record.members)
}

fun encodeVbr(vbr: VBR) : BitSet {
    val set = BitSet()

}

fun encodeVbr(value: Long, size: Int) : BitSet =
    encodeVbr(VBR(value, size))

