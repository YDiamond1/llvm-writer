import bitcode.abbrevs.AbbrevOp
import bitcode.abbrevs.StandardWidth
import bitcode.primitives.*
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

class DomainTest {
    @Test
    fun testAbbrev() {
        val record = SampleRecord(listOf(Fixed(3.toBits()), VBR(127.toBits(), 4)))

        val abbrev = record.getAbbrev()

        val arrayAbbrev = Array(abbrev.members.size) { abbrev.members[it] }
        assertContentEquals(
            arrayAbbrev,
            arrayOf(
                AbbrevOp(AbbrevMemberIds.Fixed, VBR(2.toBits(), StandardWidth.AbbrevSizeWidth.width)),
                AbbrevOp(AbbrevMemberIds.VBR, VBR(4.toBits(), StandardWidth.AbbrevSizeWidth.width))
            )
        )
    }

    @Test
    fun testAbbrevWithArray() {
        val record = SampleRecord(listOf(Fixed(3.toBits())), Array(mutableListOf(Fixed(1.toBits()))))

        val abbrev = record.getAbbrev()

        val arrayAbbrev = Array(abbrev.members.size) { abbrev.members[it] }
        assertContentEquals(
            arrayAbbrev,
            arrayOf(
                AbbrevOp(AbbrevMemberIds.Fixed, VBR(2.toBits(), StandardWidth.AbbrevSizeWidth.width)),
                AbbrevOp(AbbrevMemberIds.Array, null),
                AbbrevOp(AbbrevMemberIds.Fixed, VBR(1.toBits(), StandardWidth.AbbrevSizeWidth.width))
            )
        )
    }

    @Test
    fun testAbbrevWithBlob() {
        val record = SampleRecord(listOf(Fixed(3.toBits())), Blob(mutableListOf(123, 0)))

        val abbrev = record.getAbbrev()
        val arrayAbbrev = Array(abbrev.members.size) { abbrev.members[it] }
        assertContentEquals(
            arrayAbbrev,
            arrayOf(
                AbbrevOp(AbbrevMemberIds.Fixed, VBR(2.toBits(), StandardWidth.AbbrevSizeWidth.width)),
                AbbrevOp(AbbrevMemberIds.Blob, null)
            )
        )
    }
}
