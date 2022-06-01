package tech.jaboc.sorting

import javax.swing.*
import java.awt.*
import java.awt.geom.*

class Board(val label: JTextArea) : JPanel() {
    var algorithmIndex = 3

    val algorithms = listOf(BubbleSort(), InsertionSort(), MergeSort(), QuickSort(), RadixSort(2), RadixSort(10), BogoSort())

    val dataList = mutableListOf<Int>()
    var dataSizesIndex: Int = 4
    var dataSizes = listOf(5, 10, 20, 50, 99, 200, 500, 999)

    var sequentialData = true

    var directAccesses = 0
    var directSets = 0
    var swaps = 0
    var comparisons = 0

    var maxValue = 0

    init {
        val timer = Timer(1000/60) { repaint() }
        timer.start()
        
        generateData()
    }

    var paused = false
    var sorted = false

    var sortIterator: Iterator<AlgorithmOperation>? = null
        private set

    var colourList : MutableList<Color> = mutableListOf()

    override fun paintComponent(gr: Graphics) {
        super.paintComponent(gr)
        val g = gr as Graphics2D

        label.text = """
                Jacob's Sorting Simulator
                Current Algorithm:
                ${algorithms[algorithmIndex].getName()}
                (Change with Q and E)
                Currently ${if(paused) "Paused" else "Playing"}
                (Pause and play with space)
                Currently using ${dataSizes[dataSizesIndex]} data points
                (Change with A and D)
                Currently using ${if (sequentialData) "Sequential" else "Random"} data
                (Toggle with P)
                The list is ${if(sorted) "" else "not "}sorted (Press R to reset it)
                Stats:
                $swaps Swaps
                $comparisons Comparisons
                $directAccesses Direct Accesses
                $directSets Direct Sets
                The volume is currently ${SoundManager.volume}/128
                Use W to raise it and S to lower it
                The current instrument is ${SoundManager.instrument.name}
                Use M to change it
            """.trimIndent()

        val renderingHints = RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        g.setRenderingHints(renderingHints)

        if(dataList.size == 0) return

        if(!paused || colourList.size != dataList.size)
            colourList = dataList.map { Color.gray }.toMutableList()


        val notes = mutableListOf<Int>()

        if(!paused && !sorted) {
            if(sortIterator!!.hasNext()) {
                when (val algorithmOperation = sortIterator!!.next()) {
                    is CompareTwoPoints -> {
                        colourList[algorithmOperation.index1] = Color.red
                        colourList[algorithmOperation.index2] = Color.red
                        notes.add(dataList[algorithmOperation.index2])
                        notes.add(dataList[algorithmOperation.index1])   
                        comparisons++
                    }
                    is SwapTwoPoints -> {
                        colourList[algorithmOperation.index1] = Color.green
                        colourList[algorithmOperation.index2] = Color.green
                        val temp = dataList[algorithmOperation.index2]
                        dataList[algorithmOperation.index2] = dataList[algorithmOperation.index1]
                        dataList[algorithmOperation.index1] = temp
                        notes.add(dataList[algorithmOperation.index2])
                        notes.add(dataList[algorithmOperation.index1])
                        swaps++
                    }
                    is RemoveAndInsertPoints -> {
                        colourList[algorithmOperation.index1] = Color.green
                        colourList[algorithmOperation.index2] = Color.green
                        notes.add(dataList[algorithmOperation.index2])
                        notes.add(dataList[algorithmOperation.index1])
                        val temp = dataList[algorithmOperation.index1]
                        dataList.removeAt(algorithmOperation.index1)
                        dataList.add(algorithmOperation.index2, temp)
                    }
                    is SetPointValue -> {
                        colourList[algorithmOperation.index] = Color.green
                        notes.add(dataList[algorithmOperation.index])
                        dataList[algorithmOperation.index] = algorithmOperation.value
                        directSets++
                    }
                    is GetPointValue -> {
                        colourList[algorithmOperation.index] = Color.red
                        notes.add(dataList[algorithmOperation.index])
                        directAccesses++
                    }
                }
            } else {
                sorted = true
                paused = true
            }
        }

        SoundManager.stopNotes()
        SoundManager.playNotes(notes.map { SoundManager.processNote(it, 0, maxValue) })

        val size = size
        val w = size.getWidth()
        val h = size.getHeight()

        val barWidth = w / dataList.size

        g.stroke = BasicStroke(2f)

        for(i in 0 until dataList.size) {
            if(barWidth > 2) {
                g.color = Color.black
            } else {
                g.color = colourList[i]
            }
            val height = (h - 1) * (dataList[i].toDouble() / maxValue.toDouble())
            g.drawRect((i * barWidth).toInt(), h.toInt() - height.toInt() - 1, barWidth.toInt(), height.toInt() - 1)
            g.color = colourList[i]
            g.fillRect((i * barWidth).toInt(), h.toInt() - height.toInt() - 1, barWidth.toInt(), height.toInt() - 1)
        }

        

        Toolkit.getDefaultToolkit().sync()
    }

    fun generateData() {
        if(sequentialData) {
            generateSequentialData(dataSizes[dataSizesIndex])
        } else {
            generateRandomData(dataSizes[dataSizesIndex])
        }

        maxValue = dataList.maxOf { it }

        paused = true
        sorted = true
    }

    fun generateSequentialData(number: Int) {
        dataList.clear()
        dataList.addAll((1..number).toList())
    }

    fun generateRandomData(number: Int) {
        dataList.clear()
        for(i in 1..number) {
            dataList.add((Math.random() * (number * 4)).toInt())
        }
        dataList.sort()
    }

    fun shuffleData() {
        dataList.shuffle()

        sorted = false
    }

    fun play() {
        if(sorted) {
            generateData()
            shuffleData()
            setNewAlgorithm()
        }

        paused = false
    }

    fun pause() {
        paused = true
    }

    fun setNewAlgorithm() {
        directAccesses = 0
        directSets = 0
        comparisons = 0
        swaps = 0
        sortIterator = algorithms[algorithmIndex].execute(dataList.toMutableList()).iterator()
    }

    override fun getPreferredSize() : Dimension {
        return Dimension(600, 600)
    }
}