import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomPriorityQueue<T extends Comparable<T>> {
    private List<T> heap;

    public CustomPriorityQueue() {
        this.heap = new ArrayList<>();
    }

    public void add(T element) {
        heap.add(element);
        int currentIndex = heap.size() - 1;
        while (currentIndex > 0) {
            int parentIndex = (currentIndex - 1) / 2;
            if (heap.get(currentIndex).compareTo(heap.get(parentIndex)) < 0) {
                Collections.swap(heap, currentIndex, parentIndex);
                currentIndex = parentIndex;
            } else {
                break;
            }
        }
    }

    public T poll() {
        if (heap.size() == 0) return null;
        T result = heap.get(0);
        T lastElement = heap.remove(heap.size() - 1);
        if (heap.size() > 0) {
            heap.set(0, lastElement);
            heapify(0);
        }
        return result;
    }

    private void heapify(int index) {
        int leftIndex = 2 * index + 1;
        int rightIndex = 2 * index + 2;
        int smallestIndex = index;

        if (leftIndex < heap.size() && heap.get(leftIndex).compareTo(heap.get(smallestIndex)) < 0) {
            smallestIndex = leftIndex;
        }

        if (rightIndex < heap.size() && heap.get(rightIndex).compareTo(heap.get(smallestIndex)) < 0) {
            smallestIndex = rightIndex;
        }

        if (smallestIndex != index) {
            Collections.swap(heap, index, smallestIndex);
            heapify(smallestIndex);
        }
    }

    public boolean isEmpty() {
        return heap.size() == 0;
    }

    public int size() {
        return heap.size();
    }
}
