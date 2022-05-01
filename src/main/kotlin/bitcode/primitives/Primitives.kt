package bitcode.primitives

enum class AbbrevMemberIds(val id: Int) {
    Literal(0),
    Fixed(1),
    VBR(2),
    Array(3),
    Char6(4),
    Blob(5)
}


interface AbbrevMember {
    fun getKind(): AbbrevMemberIds
}


class Literal() : AbbrevMember {
    override fun getKind() = AbbrevMemberIds.Literal
}

class Fixed() : AbbrevMember {
    override fun getKind(): AbbrevMemberIds = AbbrevMemberIds.Fixed
}

class VBR(val value: Long, val size: Int) : AbbrevMember {
    override fun getKind(): AbbrevMemberIds = AbbrevMemberIds.VBR
}

class Array() : AbbrevMember {
    override fun getKind(): AbbrevMemberIds = AbbrevMemberIds.Array
}

class Char() : AbbrevMember {
    override fun getKind(): AbbrevMemberIds = AbbrevMemberIds.Char6
}

class Blob() : AbbrevMember {
    override fun getKind(): AbbrevMemberIds = AbbrevMemberIds.Blob
}


