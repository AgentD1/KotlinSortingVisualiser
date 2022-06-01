package tech.jaboc.sorting

class BubbleSort : Algorithm {
    override fun execute(dataPoints : MutableList<Int>) : Sequence<AlgorithmOperation> {
        return sequence {
            var iterations = 0
            while(true) {
                var dirty = false
                for(i in 0..(dataPoints.size - 2 - iterations)) {
                    val firstElement = dataPoints[i]
                    val secondElement = dataPoints[i + 1]
                    val compared = firstElement.compareTo(secondElement)
                    yield(CompareTwoPoints(i, i + 1))
                    if(compared == 0) continue
                    if(compared > 0) {
                        dataPoints[i] = secondElement
                        dataPoints[i + 1] = firstElement
                        yield(SwapTwoPoints(i, i + 1))
                        dirty = true
                    }
                }
                iterations++
                if(!dirty) break
            }
        }
    }
    
    override fun getName() : String { return "Bubble Sort" }
}