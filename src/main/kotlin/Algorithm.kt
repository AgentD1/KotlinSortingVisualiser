package tech.jaboc.sorting

interface Algorithm {
    fun execute(dataPoints : MutableList<Int>) : Sequence<AlgorithmOperation>
    fun getName() : String
}

interface AlgorithmOperation

class CompareTwoPoints(val index1: Int, val index2: Int) : AlgorithmOperation
class SwapTwoPoints(val index1: Int, val index2: Int) : AlgorithmOperation
class RemoveAndInsertPoints(val index1: Int, val index2: Int) : AlgorithmOperation
class SetPointValue(val index: Int, val value: Int) : AlgorithmOperation
class GetPointValue(val index: Int) : AlgorithmOperation