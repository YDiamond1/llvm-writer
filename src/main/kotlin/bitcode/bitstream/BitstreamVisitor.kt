package bitcode.bitstream

import bitcode.abbrevs.Abbrev
import bitcode.abbrevs.KnownAbbrevsIds
import bitcode.primitives.*

object RootBlockEntry : BlockEntry(2, 0, mutableMapOf())

open class BlockEntry(
    val codeWidth: Int,
    val startSizeWord: Int,
    val abbrevs: MutableMap<Abbrev, Int>
)

class BitstreamVisitor {

    val stack = mutableListOf<BlockEntry>()
    val collector: BitsCollector = BitsCollector()

    fun getStartSize() = stack.last().startSizeWord


    fun enterBlock(block: Block) {
        stack.push(RootBlockEntry)
        enterSubBlock(block)
        exitBlock()
        stack.pop()
    }

    fun enterSubBlock(block: Block) {
        val indexSize = collector.enterSubblock(block, getWidth())
        stack.push(BlockEntry(block.width, indexSize, mutableMapOf()))

        writeAbbrevs(block.entities)

        writeEntities(block.entities)

    }

    private fun writeAbbrevs(entities: List<Entity>) {
        var idDefine = KnownAbbrevsIds.FirstDefineAbbrev.id

        entities.forEach {
            when (it) {
                is Record -> {
                    val abbrevs = stack.last().abbrevs
                    val key = it.getAbbrev()
                    if (abbrevs.containsKey(key)) {
                        abbrevs.put(key, idDefine)
                        it.abbrevId = idDefine
                        idDefine++
                        collector.writeAbbrev(key, getWidth())
                    } else {
                        it.abbrevId = abbrevs[key]!!
                    }
                }
            }
        }
    }

    fun writeEntities(list: List<Entity>) {
        list.forEach { writeEntity(it) }
    }

    fun writeEntity(entity: Entity) {
        when (entity) {
            is Record -> collector.writeRecord(entity, getWidth())
            is Block -> enterBlock(entity)
        }
    }



    fun exitBlock() {
        collector.exitBlock(getWidth())
        val sizeOfBlock = collector.getIndexSize() - getStartSize() - 1
        val block: BlockEntry = stack.pop()
        collector.backpathWord(block.startSizeWord, sizeOfBlock.toBits())
    }

    fun getWidth() = stack.last().codeWidth
}

fun MutableList<BlockEntry>.push(blockEntry: BlockEntry) {
    this.add(blockEntry)
}

fun MutableList<BlockEntry>.pop() = this.removeLast()
