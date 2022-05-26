package bitcode.primitives

import bitcode.abbrevs.Abbrev
import bitcode.abbrevs.AbbrevOp

interface Record : Entity {
    var abbrevId: Int
    val members: List<Primitive>?
    val array: Array?
    val blob: Blob?
    fun getAbbrev(): Abbrev
}


class SampleRecord private constructor(
    override val members: List<Primitive>?,
    override val array: Array?,
    override val blob: Blob?
) : Record {

    constructor(members: List<Primitive>?, array: Array?) : this(members, array, null)
    constructor(members: List<Primitive>?, blob: Blob?) : this(members, null, blob)
    constructor(members: List<Primitive>?) : this(members, null, null)
    constructor(array: Array?): this(null, array, null)
    constructor(blob: Blob?): this(null, null, blob)

    override var abbrevId: Int = 0

    override fun getAbbrev(): Abbrev {
        val list = mutableListOf<AbbrevOp>()
        members?.forEach { list.add(AbbrevOp(it.getKind(), it.getExtra())) }

        if (array != null) {
            list.add(AbbrevOp(AbbrevMemberIds.Array, null))
            list.add(AbbrevOp(array.getAbbrevType(), array.getAbbrevTypeExtra()))
        } else if (blob != null) {
            list.add(AbbrevOp(AbbrevMemberIds.Blob, null))
        }
        return Abbrev(list)
    }


}
