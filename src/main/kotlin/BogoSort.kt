package tech.jaboc.sorting

class BogoSort : Algorithm {
    override fun execute(dataPoints : MutableList<Int>) : Sequence<AlgorithmOperation> {
        return sequence {
            outerLoop@ while(true) {
                dataPoints.shuffle()
                for((index, value) in dataPoints.withIndex()) {
                    yield(SetPointValue(index, value))
                }
                for(index in dataPoints.indices) {
                    if(index == dataPoints.size - 1) break
                    val compared = dataPoints[index].compareTo(dataPoints[index + 1])
                    yield(CompareTwoPoints(index, index + 1))
                    if(compared > 0) continue@outerLoop
                }
                break
            }
        }
    }

    override fun getName() : String { return "Bogo Sort" }
}