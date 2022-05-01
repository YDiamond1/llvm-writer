package bitcode

import bitcode.primitives.AbbrevMember
import java.util.BitSet

interface Record {
    fun getDefineAbbrev(): BitSet
    fun getBits(): BitSet
    val members: List<AbbrevMember>
}


class SampleRecord(override val members: List<AbbrevMember>) : Record {


    override fun getDefineAbbrev(): BitSet =
        encodeDefineAbbrev(this)


    override fun getBits(): BitSet {
        TODO("Not yet implemented")
    }


}
