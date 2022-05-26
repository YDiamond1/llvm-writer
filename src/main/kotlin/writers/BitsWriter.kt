package writers

import java.io.File

class BitsWriter {

    fun write(name: String, bytes: List<Byte>){
        val outputStream = File("$name.bc").outputStream()
        outputStream.write(byteArrayOf(0xB, 0xC, 0x0C, 0xED.toByte()))
        outputStream.write(bytes.toByteArray())
    }
}
