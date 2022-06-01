package tech.jaboc.sorting

class InsertionSort : Algorithm {
    override fun execute(dataPoints : MutableList<Int>) : Sequence<AlgorithmOperation> {
        return sequence {
            for(i in 1 until dataPoints.size) {
                val value = dataPoints[i]
                var j = i - 1

                while(j >= 0) {
                    yield(CompareTwoPoints(i, j))
                    if(dataPoints[j] <= value) {
                        break
                    }
                    yield(SwapTwoPoints(j, j + 1))
                    dataPoints[j + 1] = dataPoints[j]
                    j--
                }
                dataPoints[j + 1] = value
            }
        }
    }

    override fun getName() : String { return "Insertion Sort" }
}