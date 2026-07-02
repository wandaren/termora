package app.termora.terminal

import kotlin.math.max

@Suppress("MemberVisibilityCanBePrivate")
class TerminalReader {
    private var buffer = CharArray(1024)
    private var head = 0
    private var size = 0


    fun addLast(char: Char) {
        ensureCapacity(size + 1)
        buffer[index(size)] = char
        size++
    }

    fun addFirst(chars: List<Char>) {
        for (i in chars.size - 1 downTo 0) {
            addFirst(chars[i])
        }
    }


    fun addLast(chars: List<Char>) {
        ensureCapacity(size + chars.size)
        for (i in chars.indices) {
            buffer[index(size + i)] = chars[i]
        }
        size += chars.size
    }

    fun addFirst(ch: Char) {
        ensureCapacity(size + 1)
        head = if (head == 0) buffer.size - 1 else head - 1
        buffer[head] = ch
        size++
    }

    fun addLast(text: String) {
        ensureCapacity(size + text.length)
        for (i in text.indices) {
            buffer[index(size + i)] = text[i]
        }
        size += text.length
    }

    fun read(): Char {
        if (isEmpty()) {
            throw NoSuchElementException()
        }
        val ch = buffer[head]
        head = (head + 1) % buffer.size
        size--
        if (size == 0) {
            head = 0
        }
        return ch
    }

    fun peek(): Char? {
        return if (isEmpty()) null else buffer[head]
    }

    fun isEmpty(): Boolean {
        return size == 0
    }

    fun isNotEmpty(): Boolean {
        return size > 0
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (i in 0 until size) {
            val c = buffer[index(i)]
            when (c) {
                ControlCharacters.TAB -> sb.append("TAB")
                ControlCharacters.ESC -> sb.append("ESC")
                ControlCharacters.BEL -> sb.append("BEL")
                ControlCharacters.CR -> sb.append("CR")
                else -> sb.append(c)
            }
        }
        return sb.toString()
    }

    fun clear() {
        head = 0
        size = 0
    }

    private fun index(offset: Int): Int {
        return (head + offset) % buffer.size
    }

    private fun ensureCapacity(capacity: Int) {
        if (capacity <= buffer.size) return

        val newBuffer = CharArray(max(buffer.size shl 1, capacity))
        for (i in 0 until size) {
            newBuffer[i] = buffer[index(i)]
        }
        buffer = newBuffer
        head = 0
    }


}
