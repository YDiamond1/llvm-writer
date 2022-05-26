import bitcode.bitstream.BitsCollector
import bitcode.bitstream.BitstreamVisitor
import bitcode.primitives.*
import org.junit.jupiter.api.Test
import writers.BitsWriter
import java.io.File
import kotlin.test.assertContentEquals

class BitstreamTest {
    @Test
    fun testRecord() {
        val record = SampleRecord(listOf(Fixed(3.toBits()), VBR(127.toBits(), 4)))
        record.abbrevId = 5

        val collector = BitsCollector()
        collector.writeRecord(record, 3)

        val bytes = collector.bytes
        val value = Array(bytes.size) { bytes[it] }
        assertContentEquals(value, arrayOf(-125, 15))
    }

    @Test
    fun testRecordWithArray() {
        val record = SampleRecord(Array(mutableListOf(Fixed(4.toBits()))))
        record.abbrevId = 5

        val collector = BitsCollector()
        collector.writeRecord(record, 3)

        val bytes = collector.bytes
        val value = Array(bytes.size) { bytes[it] }
        assertContentEquals(value, arrayOf(13, 8))
    }

    @Test
    fun testRecordWithBlob() {
        val record = SampleRecord(Blob(mutableListOf(64)))
        record.abbrevId = 5

        val collector = BitsCollector()
        collector.writeRecord(record, 3)

        val bytes = collector.bytes
        val value = Array(bytes.size) { bytes[it] }
        assertContentEquals(value, arrayOf(13, 0, 64, 0))
    }


    @Test
    fun testBitsAbbrev() {
        val record = SampleRecord(listOf(Fixed(3.toBits()), VBR(127.toBits(), 4)))

        val abbrev = record.getAbbrev()

        val collector = BitsCollector()
        collector.writeAbbrev(abbrev, 3);

        val bytes = collector.bytes
        val value = Array(bytes.size) { bytes[it] }
        assertContentEquals(value, arrayOf(18, 50, -4, 0))
    }

    @Test
    fun testBitsAbbrevWithArray() {
        val record = SampleRecord(listOf(Fixed(3.toBits())), Array(mutableListOf(Fixed(1.toBits()))))

        val abbrev = record.getAbbrev()

        val collector = BitsCollector()
        collector.writeAbbrev(abbrev, 3);

        val bytes = collector.bytes
        val value = Array(bytes.size) { bytes[it] }
        assertContentEquals(value, arrayOf(18, 50, 76, 2))
    }

    @Test
    fun testBitsAbbrevWithBlob() {
        val record = SampleRecord(listOf(Fixed(3.toBits())), Blob(mutableListOf(123, 0)))

        val abbrev = record.getAbbrev()

        val collector = BitsCollector()
        collector.writeAbbrev(abbrev, 3);

        val bytes = collector.bytes
        val value = Array(bytes.size) { bytes[it] }
        assertContentEquals(value, arrayOf(18, 50, 20))
    }

    @Test
    fun testBlockWithInnerBlock() {
        val block = Block(
            8,
            mutableListOf(
                Block(
                    9,
                    mutableListOf(
                        SampleRecord(listOf(Fixed(3.toBits()), VBR(127.toBits(), 4)))
                    ), 3
                )
            ), 3
        )

        val visitor = BitstreamVisitor()
        visitor.enterBlock(block);

        val bytes = visitor.collector.bytes
        val value = Array(bytes.size) { bytes[it] }
        assertContentEquals(
            value,
            arrayOf(
                /*EnterBlock*/ 33, 12, 0, 0, /*sizeBlock*/ 4, 0, 0, 0,
                /*EnterBlock*/ 73, 24, 0, 0, /*sizeBlock*/ 2, 0, 0, 0,
                /*DefineAbbrev, Record, End Inner Block*/ 18, 50, 8, -66, -128, 3, 0, 0,
                /*EndBlock*/ 0, 0, 0, 0
            )
        )
    }

    @Test
    fun testWriter() {

        val block = Block(
            8,
            mutableListOf(
                Block(
                    9,
                    mutableListOf(
                        SampleRecord(listOf(Fixed(3.toBits()), VBR(127.toBits(), 4)))
                    ), 3
                )
            ), 3
        )

        val visitor = BitstreamVisitor()
        visitor.enterBlock(block);

        val bitsWriter = BitsWriter()
        bitsWriter.write("test", visitor.collector.bytes)

        val file = File("test.bc")

        assert(file.exists())
        assertContentEquals(file.inputStream().readNBytes(4).toList().toByteArray(), byteArrayOf(0xB, 0xC, 0x0C, 0xED.toByte()))
        file.delete()
    }
}
