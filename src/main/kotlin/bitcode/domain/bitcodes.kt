package bitcode.abbrevs

enum class KnownAbbrevsIds(val id: Int) {
    EndBlock(0),
    EnterSubblock(1),
    DefineAbbrev(2),
    UnabbrevRecord(3),
    FirstDefineAbbrev(4)
}

enum class StandardWidth(val width: Int) {
    BlockIdWidth(8),
    CodeLenWidth(4),
    BlockSizeWidth(32),
    AbbrevSizeWidth(5),
    ArraySizeWidth(6),
}
