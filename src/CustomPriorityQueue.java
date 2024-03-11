import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
// A custom implementation of a Priority Queue using a min-heap.
// It's a generic class that accepts types extending Comparable,
// ensuring elements can be compared for proper placement in the heap.
public class CustomPriorityQueue<T extends Comparable<T>> {
    // The heap is represented as a dynamic array (ArrayList).
    private List<T> heap;
    // Constructor initializes the heap as an empty ArrayList.
    public CustomPriorityQueue() {

        this.heap = new ArrayList<>();
    }
    // Adds a new element to the priority queue and maintains the heap property.
    public void add(T element) {
        heap.add(element); // Add the element to the end of the heap.
        // Bubble up the added element to maintain the min-heap property.
        int currentIndex = heap.size() - 1;
        while (currentIndex > 0) {
            int parentIndex = (currentIndex - 1) / 2;
            // If the added element is smaller than its parent, swap them.
            if (heap.get(currentIndex).compareTo(heap.get(parentIndex)) < 0) {
                Collections.swap(heap, currentIndex, parentIndex);
                currentIndex = parentIndex;// Move up to the parent's index.
            } else {
                break;// The heap property is maintained.
            }
        }
    }
    // Removes and returns the smallest element from the priority queue.
    public T poll() {
        if (heap.size() == 0) return null;// Return null if the queue is empty.
        T result = heap.get(0);// The smallest element is always at the root.
        T lastElement = heap.remove(heap.size() - 1); // Remove the last element.
        if (heap.size() > 0) {
            heap.set(0, lastElement);// Move the last element to the root.
            heapify(0);// Heapify down from the root to maintain heap property.
        }
        return result;// Return the smallest element.
    }

    // Helper method to maintain the heap property from the given index downwards.
    private void heapify(int index) {
        int leftIndex = 2 * index + 1;// Index of left child.
        int rightIndex = 2 * index + 2;// Index of right child.
        int smallestIndex = index;// Assume current index is the smallest.
        // Find the smallest among current, left, and right nodes.
        if (leftIndex < heap.size() && heap.get(leftIndex).compareTo(heap.get(smallestIndex)) < 0) {
            smallestIndex = leftIndex;
        }

        if (rightIndex < heap.size() && heap.get(rightIndex).compareTo(heap.get(smallestIndex)) < 0) {
            smallestIndex = rightIndex;
        }
        // If the smallest is not the current node, swap and continue heapifying.
        if (smallestIndex != index) {
            Collections.swap(heap, index, smallestIndex);
            heapify(smallestIndex);// Heapify down from the smallest index.
        }
    }
    // Checks if the priority queue is empty.
    public boolean isEmpty() {
        return heap.size() == 0;
    }
    // Returns the number of elements in the priority queue.
    public int size() {
        return heap.size();
    }
}
