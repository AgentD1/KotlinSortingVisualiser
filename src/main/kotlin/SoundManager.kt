package tech.jaboc.sorting

import java.awt.Component
import javax.sound.midi.Instrument
import javax.sound.midi.MidiSystem
import javax.swing.JOptionPane
import kotlin.math.roundToInt

class SoundManager {
    companion object {
        val synthesizer = MidiSystem.getSynthesizer()
        var instrument = synthesizer.availableInstruments[0]

        var volume = 64

        init {
            synthesizer.open()
            synthesizer.loadInstrument(instrument)
        }

        val channel = synthesizer.channels[0]

        init {
            channel.programChange(instrument.patch.bank, instrument.patch.program)
        }

        var notesPlayed: MutableList<Int> = mutableListOf()

        val output_start = 30
        val output_end = 96

        fun processNote(note: Int, min: Int, max: Int): Int {
            val slope = 1.0 * (output_end - output_start) / (max - min)
            return (output_start + slope * (note - min)).roundToInt()
        }

        fun playNotes(notes: List<Int>) {
            notesPlayed = notes.toMutableList()
            for (note in notes) {
                channel.noteOn(note, volume)
            }
        }

        fun stopNotes() {
            for (note in notesPlayed) {
                channel.noteOff(note)
            }
        }

        fun switchInstrument(parent: Component) {
            val answer = JOptionPane.showInputDialog(
                parent,
                "Pick an instrument (Not all instruments work well, if in double, use a piano)",
                "Instrument Picker",
                JOptionPane.PLAIN_MESSAGE,
                null,
                synthesizer.availableInstruments.map { it.name }.toTypedArray(),
                instrument.name
            ) as String? ?: return

            instrument = synthesizer.availableInstruments.find { it.name == answer }
            synthesizer.loadInstrument(instrument)
            channel.programChange(instrument.patch.bank, instrument.patch.program)
        }
    }
}