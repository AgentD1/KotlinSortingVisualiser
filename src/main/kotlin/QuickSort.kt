package tech.jaboc.sorting

class QuickSort : Algorithm {
    override fun execute(dataPoints : MutableList<Int>) : Sequence<AlgorithmOperation> {
        return sequence {
            yieldAll(quicksort(dataPoints, 0, dataPoints.size - 1))
        }
    }

    private fun quicksort(dataPoints : MutableList<Int>, startIndex : Int, endIndex : Int) : Sequence<AlgorithmOperation> {
        return sequence {
            if(startIndex < endIndex) {
                val (seq, part) = partition(dataPoints, startIndex, endIndex)
                yieldAll(seq)
                yieldAll(quicksort(dataPoints, startIndex, part - 1))
                yieldAll(quicksort(dataPoints, part + 1, endIndex))
            }
        }
        
    }

    private fun partition(dataPoints : MutableList<Int>, startIndex : Int, endIndex : Int) : Pair<Sequence<AlgorithmOperation>, Int> { // sequence, partition index
        val returnSequence : MutableList<AlgorithmOperation> = mutableListOf()
        val pivot = dataPoints[endIndex]
        returnSequence.add(GetPointValue(endIndex))
        var i = startIndex - 1


        for(j in startIndex until endIndex) {
            returnSequence.add(CompareTwoPoints(i + 1, j))
            if(dataPoints[j] < pivot) {
                i++
                returnSequence.add(SwapTwoPoints(i, j))
                val temp = dataPoints[j]
                dataPoints[j] = dataPoints[i]
                dataPoints[i] = temp
            }
        }

        returnSequence.add(SwapTwoPoints(i + 1, endIndex))
        val temp = dataPoints[i + 1]
        dataPoints[endIndex] = dataPoints[i + 1]
        dataPoints[i + 1] = temp

        return Pair(returnSequence.asSequence(), i + 1)
    }

    override fun getName() : String { return "Quicksort (Partition as last index)" }
}