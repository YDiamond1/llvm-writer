import bitcode.abbrevs.AbbrevOp
import bitcode.abbrevs.StandardWidth
import bitcode.bitstream.BitsCollector
import bitcode.bitstream.BitstreamVisitor
import bitcode.primitives.*
import org.junit.jupiter.api.Test
import writers.BitsWriter
import java.io.File
import kotlin.test.assertContentEquals

class BitsTest {


    @Test
    fun testAddBits() {
        val value1 = 4.toBits()
        val value2 = 4.toBits()

        value1.addBits(value2, 3)
        val sum1 = Array(value1.value.size) { value1.value[it] }
        assertContentEquals(sum1, arrayOf(36))
        assert(value1.length == 6)


        val value3 = 63.toBits()
        val value4 = 64.toBits()

        value3.addBits(value4, 7)
        val sum2 = Array(value3.value.size) { value3.value[it] }
        assertContentEquals(sum2, arrayOf(63, 16))
        assert(value3.length == 13)
    }

    @Test
    fun testIntegerBits() {
        val test = 1024.toBits()
        val value = Array(test.value.size) { test.value[it] }
        assertContentEquals(value, arrayOf(0, 4))

        val test2 = 125.toBits()
        val value2 = Array(test2.value.size) { test2.value[it] }
        assertContentEquals(value2, arrayOf(125))

        val test3 = 100_000.toBits()
        val value3 = Array(test3.value.size) { test3.value[it] }
        assertContentEquals(value3, arrayOf(-64, -6, 1))
    }

    @Test
    fun testUIntegerBits() {
        val test = 1024u.toBits()
        val value = Array(test.value.size) { test.value[it] }
        assertContentEquals(value, arrayOf(0, 4))

        val test2 = 125u.toBits()
        val value2 = Array(test2.value.size) { test2.value[it] }
        assertContentEquals(value2, arrayOf(125))

        val test3 = 100_000u.toBits()
        val value3 = Array(test3.value.size) { test3.value[it] }
        assertContentEquals(value3, arrayOf(-64, -6, 1))
    }

    @Test
    fun testShortBits() {
        val test = 1024.toShort().toBits()
        val value = Array(test.value.size) { test.value[it] }
        assertContentEquals(value, arrayOf(0, 4))

        val test2 = 125.toShort().toBits()
        val value2 = Array(test2.value.size) { test2.value[it] }
        assertContentEquals(value2, arrayOf(125))
    }

    @Test
    fun testUShortBits() {
        val test = 1024.toUShort().toBits()
        val value = Array(test.value.size) { test.value[it] }
        assertContentEquals(value, arrayOf(0, 4))

        val test2 = 125.toUShort().toBits()
        val value2 = Array(test2.value.size) { test2.value[it] }
        assertContentEquals(value2, arrayOf(125))

        val test3 = 100_000.toUShort().toBits()
        val value3 = Array(test3.value.size) { test3.value[it] }
        assertContentEquals(value3, arrayOf(-64, -6, 1))
    }
}
