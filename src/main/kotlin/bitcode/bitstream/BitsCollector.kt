package bitcode.bitstream;

import bitcode.abbrevs.Abbrev
import bitcode.abbrevs.KnownAbbrevsIds
import bitcode.abbrevs.StandardWidth
import bitcode.bits.*
import bitcode.primitives.*
import bitcode.primitives.Array


class BitsCollector {

    val bytes = mutableListOf<Byte>()

    var curValue: Bits = Bits(mutableListOf(), 0)

    private fun writeWord(bits: Bits) {
        bytes.addAll(bits.value.slice(0..3))
    }

    private fun writeByte(bits: Bits) {
        bytes.add(bits.value[0])
    }

    fun write(bits: Bits) {
        curValue.addBits(bits, bits.length)
        if (curValue.length < 32) {
            return
        }

        // Add the current word.
        writeWord(curValue)

        val end = curValue.length - 32
        curValue = Bits(curValue.takeBits(32, end), end)
    }

    fun flushToWord() {
        if (curValue.length != 0) {
            curValue.flushToLength(32)
            writeWord(curValue)
            curValue = Bits(mutableListOf(), 0)
        }
    }

    fun write(vbr: VBR) {

        // Emit the bits with VBR encoding, NumBits-1 bits at a time.
        val value = vbr.value
        val length = vbr.value.length
        var beginStep = 0

        while (length > beginStep + vbr.size) {
            val part = value.takeBits(beginStep, beginStep + vbr.size - 1)
            part[0] = part[0] or (1 shr vbr.size)
            write(Bits(part, vbr.size))
            beginStep += vbr.size
        }

        write(Bits(value.takeBits(beginStep, beginStep + vbr.size), vbr.size));
    }

    fun write(literal: Literal) {
        println("it's already in abbrev")
    }

    fun write(fixed: Fixed) {
        write(fixed.value)
    }

    fun write(char: Char6){
        if(char.value.length != 6)
            throw IllegalArgumentException("char cannot have ${char.value.length}")

        write(char.value)
    }

    fun writeCode(id: Int, width: Int) {
        val bits = id.toBits()
        bits.length = width
        write(bits)
    }

    fun getIndexSize() = bytes.size
    fun enterSubblock(block: Block, width: Int) : Int {
        writeCode(KnownAbbrevsIds.EnterSubblock.id, width)

        write(VBR(block.blockId.toBits(), StandardWidth.BlockIdWidth.width))
        write(VBR(block.width.toBits(), StandardWidth.CodeLenWidth.width))
        flushToWord()

        val indexSize = getIndexSize()
        write(Placeholder)

        return indexSize


    }

    fun backpathWord(indexByte: Int, bits: Bits) {
        var i = indexByte
        bits.value.forEach { bytes.add(i++, it) }
    }

    fun exitBlock(width: Int) {
        writeCode(KnownAbbrevsIds.EndBlock.id, width)
        flushToWord()
    }

    fun writeAbbrev(abbrev: Abbrev, width: Int) {
        writeCode(KnownAbbrevsIds.DefineAbbrev.id, width)
        write(VBR(abbrev.members.size.toBits(), StandardWidth.AbbrevSizeWidth.width))

        for (op in abbrev.members){
            if(op.id == AbbrevMemberIds.Literal){
                write(True)
            }else {
                write(False)
                write(op.id.encoding!!)
            }

            if(op.extraData != null) {
                write(op.extraData)
            }
        }
    }

    fun write(array: Array) {
        // write size
        write(VBR(array.list.size.toBits(), StandardWidth.ArraySizeWidth.width))

        //write array
        array.list.forEach { writeField(it) }
    }

    fun writeField(primitive: Primitive) {
        when(primitive.getKind()) {
            AbbrevMemberIds.Fixed -> write(primitive as Fixed)
            AbbrevMemberIds.VBR -> write(primitive as VBR)
            AbbrevMemberIds.Literal -> write(primitive as Literal)
            AbbrevMemberIds.Char6 -> write(primitive as Char6)
        }
    }


    fun writeRecord(record: Record, width: Int) {
        writeCode(record.abbrevId, width)

        record.members?.forEach {
            writeField(it)
        }

        if(record.array != null) {
            write(record.array!!)
        }else if(record.blob != null) {
            write(record.blob!!)
        }
    }

    fun write(blob: Blob) {
        //emit size
        write(VBR(blob.list.size.toBits(), StandardWidth.ArraySizeWidth.width))

        flushToWord()

        bytes.addAll(blob.list)

        //alignment
        while(bytes.size and 3 != 0) {
            bytes.add(0)
        }


    }
}


