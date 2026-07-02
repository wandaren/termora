package app.termora.terminal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TerminalReaderTest {
    @Test
    fun preservesReadOrderWithPushback() {
        val reader = TerminalReader()
        reader.addLast("abc")

        assertEquals('a', reader.read())

        reader.addFirst(listOf('x', 'y'))

        assertEquals("xybc", readAll(reader))
        assertNull(reader.peek())
    }

    @Test
    fun growsAfterWraparound() {
        val reader = TerminalReader()
        reader.addLast("a".repeat(1100))

        repeat(1000) {
            assertEquals('a', reader.read())
        }

        reader.addLast("b".repeat(1100))
        reader.addFirst('z')

        assertEquals('z', reader.read())
        assertEquals("a".repeat(100) + "b".repeat(1100), readAll(reader))
        assertTrue(reader.isEmpty())
    }

    @Test
    fun clearDropsBufferedCharacters() {
        val reader = TerminalReader()
        reader.addLast("abc")

        reader.clear()

        assertTrue(reader.isEmpty())
        assertNull(reader.peek())
    }

    @Test
    fun emptyReadFails() {
        assertFailsWith<NoSuchElementException> {
            TerminalReader().read()
        }
    }

    private fun readAll(reader: TerminalReader): String {
        val sb = StringBuilder()
        while (reader.isNotEmpty()) {
            sb.append(reader.read())
        }
        return sb.toString()
    }
}
