package tech.jaboc.sorting

class RadixSort(val radix : Int) : Algorithm {
    override fun execute(dataPoints : MutableList<Int>) : Sequence<AlgorithmOperation> {
        return sequence {
            val maxValue : Int = dataPoints.maxOf{it}

            var i : Int = 1
            while(maxValue / i > 0) {
                yieldAll(countSort(dataPoints, i))
                i *= radix
            }
        }
    }

    private fun countSort(dataPoints : MutableList<Int>, exp : Int) : Sequence<AlgorithmOperation> {
        return sequence {
            try {
            val output = MutableList<Int>(dataPoints.size, {0})
            
            val count = MutableList<Int>(radix, {0})

            for((oldIndex, value) in dataPoints.withIndex()) {
                val index = (value / exp) % radix
                yield(GetPointValue(oldIndex))
                count[index]++
            }

            for(i in 1 until radix) {
                count[i] += count[i - 1]
            }

            for(i in (dataPoints.size - 1) downTo 0) {
                yield(GetPointValue(i))
                output[count[(dataPoints[i] / exp) % radix] - 1] = dataPoints[i]
                count[(dataPoints[i] / exp) % radix]--
            }

            for((index, value) in output.withIndex()) {
                yield(SetPointValue(index, value))
                dataPoints[index] = value
            }
            } catch(e: Exception) {
                e.printStackTrace()
                System.exit(1)
            }
        }
    }

    override fun getName() : String { return "Radix Sort (LSD, base-$radix)" }
}