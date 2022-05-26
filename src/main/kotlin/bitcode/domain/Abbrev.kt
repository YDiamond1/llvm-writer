package bitcode.abbrevs

import bitcode.primitives.AbbrevMemberIds
import bitcode.primitives.VBR

data class Abbrev(val members: List<AbbrevOp>)

data class AbbrevOp(val id: AbbrevMemberIds, val extraData: VBR?)
