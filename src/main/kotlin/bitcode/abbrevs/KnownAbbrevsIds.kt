package bitcode.abbrevs

enum class KnownAbbrevsIds(val id: Int) {
    EndBlock(0),
    EnterSubblock(1),
    DefineAbbrev(2),
    UnabbrevRecord(3)
}
