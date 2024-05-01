package shark

class HeapGraphSequenceObjectGrowthDetector(
  private val objectGrowthDetector: ObjectGrowthDetector
) {

  fun findRepeatedlyGrowingObjects(
    initialState: InitialState = InitialState(),
    heapGraphSequence: Sequence<CloseableHeapGraph>,
    ): HeapGrowthTraversal {
    var lastTraversal: HeapTraversalInput = initialState
    for (heapGraph in heapGraphSequence) {
      val output = objectGrowthDetector.findGrowingObjects(
        heapGraph = heapGraph,
        previousTraversal = lastTraversal,
      )
      if (output is HeapGrowthTraversal && !output.isGrowing) {
        return output
      }
      lastTraversal = output
    }
    check(lastTraversal is HeapGrowthTraversal) {
      "Final output should be a HeapGrowth, traversalCount ${lastTraversal.traversalCount - 1} " +
        "should be >= 2. Output: $lastTraversal"
    }
    return lastTraversal
  }
}

fun ObjectGrowthDetector.fromHeapGraphSequence(): HeapGraphSequenceObjectGrowthDetector {
  return HeapGraphSequenceObjectGrowthDetector(this)
}