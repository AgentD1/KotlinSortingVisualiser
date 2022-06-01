package tech.jaboc.sorting

import java.awt.EventQueue
import javax.swing.*
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.KeyListener
import java.awt.event.KeyEvent
import javax.sound.midi.Instrument
import javax.sound.midi.MidiSystem
import javax.sound.midi.Synthesizer

class MyFrame(title: String) : JFrame() {
    var text = JTextArea("EpicBruhMoment")

    init {
        SoundManager.Companion

        setTitle(title)
        layout = FlowLayout()
        text.preferredSize = Dimension(300, 600)
        text.isFocusable = false
        text.isEditable = false
    }

    val board = Board(text)

    init {
        add(board)
        add(text)
        defaultCloseOperation = EXIT_ON_CLOSE
        pack()
        isResizable = false
        setLocationRelativeTo(null)

        addKeyListener(object : KeyListener {
            override fun keyPressed(e : KeyEvent) {
                when(e.keyCode) {
                    KeyEvent.VK_SPACE -> {
                        if(board.paused) board.play()
                        else board.pause()
                    }
                    KeyEvent.VK_Q -> {
                        board.algorithmIndex = Math.floorMod(board.algorithmIndex - 1, board.algorithms.size)
                        board.setNewAlgorithm()
                    }
                    KeyEvent.VK_E -> {
                        board.algorithmIndex = Math.floorMod((board.algorithmIndex + 1), board.algorithms.size)
                        board.setNewAlgorithm()
                    }
                    KeyEvent.VK_A -> {
                        board.dataSizesIndex = Math.floorMod((board.dataSizesIndex + 1), board.dataSizes.size)
                        board.generateData()
                        board.setNewAlgorithm()
                    }
                    KeyEvent.VK_D -> {
                        board.dataSizesIndex = Math.floorMod((board.dataSizesIndex - 1), board.dataSizes.size)
                        board.generateData()
                        board.setNewAlgorithm()
                    }
                    KeyEvent.VK_P -> {
                        board.sequentialData = !board.sequentialData
                        board.generateData()
                        board.setNewAlgorithm()
                    }
                    KeyEvent.VK_W -> {
                        SoundManager.volume = Math.floorMod(SoundManager.volume + 16, 128 + 16)
                    }
                    KeyEvent.VK_S -> {
                        SoundManager.volume = Math.floorMod(SoundManager.volume - 16, 128 + 16)
                    }
                    KeyEvent.VK_M -> {
                        SoundManager.switchInstrument(this@MyFrame)
                    }
                    KeyEvent.VK_R -> {
                        board.generateData()
                        board.setNewAlgorithm()
                    }
                }
            }

            override fun keyTyped(e : KeyEvent) {}
            override fun keyReleased(e : KeyEvent) {}
        })
    }
}

private fun createAndShowUI() {
    val frame = MyFrame("Sorting Algorithms")
    frame.isVisible = true
}

fun main() {
    println("Hello World!")
    EventQueue.invokeLater(::createAndShowUI)
}
