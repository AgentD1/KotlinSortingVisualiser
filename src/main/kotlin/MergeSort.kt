package tech.jaboc.sorting

class MergeSort : Algorithm {
    override fun execute(dataPoints : MutableList<Int>) : Sequence<AlgorithmOperation> {
        return sequence {
            yieldAll(mergeSort(dataPoints, 0, dataPoints.size - 1))
        }
    }

    fun mergeSort(dataPoints : MutableList<Int>, startIndex : Int, endIndex : Int) : Sequence<AlgorithmOperation> {
        return sequence {
            if (endIndex > startIndex) {
                val middleIndex = startIndex + (endIndex - startIndex) / 2
                yieldAll(mergeSort(dataPoints, startIndex, middleIndex))
                yieldAll(mergeSort(dataPoints, middleIndex + 1, endIndex))
                
                val leftSize = middleIndex - startIndex + 1
                val rightSize = endIndex - middleIndex

                val firstArray = Array<Int>(leftSize, {0})
                val secondArray = Array<Int>(rightSize, {0})

                for(i in 0 until leftSize) {
                    yield(GetPointValue(startIndex + i))
                    firstArray[i] = dataPoints[startIndex + i]
                }
                for(i in 0 until rightSize) {
                    yield(GetPointValue(middleIndex + i + 1))
                    secondArray[i] = dataPoints[middleIndex + i + 1]
                } 

                var i = 0
                var j = 0

                var k = startIndex
                while(i < leftSize && j < rightSize) {
                    yield(CompareTwoPoints(startIndex + i, middleIndex + j))
                    if(firstArray[i] <= secondArray[j]) {
                        yield(SetPointValue(k, firstArray[i]))
                        dataPoints[k] = firstArray[i]
                        i++
                    } else {
                        yield(SetPointValue(k, secondArray[j]))
                        dataPoints[k] = secondArray[j]
                        j++
                    }
                    k++
                }

                while(i < leftSize) {
                    yield(SetPointValue(k, firstArray[i]))
                    dataPoints[k] = firstArray[i]
                    i++
                    k++
                }

                while(j < rightSize) {
                    yield(SetPointValue(k, secondArray[j]))
                    dataPoints[k] = secondArray[j]
                    j++
                    k++
                }
            }
        }
    }

    override fun getName() : String { return "Merge Sort" }
}