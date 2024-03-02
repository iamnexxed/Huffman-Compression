import java.util.Map;

public class HuffmanTree<T> {

    public HuffmanNode<T> buildTree(CustomHashMap<T, Integer> charFreqMap) {
        CustomPriorityQueue<HuffmanNode<T>> priorityQueue = new CustomPriorityQueue<>();

        for (T key : charFreqMap.keySet()) {
            if (charFreqMap.get(key) > 0) {
                priorityQueue.add(new HuffmanNode<T>(key, charFreqMap.get(key)));
            }
        }

        while (!priorityQueue.isEmpty() && priorityQueue.size() > 1) {
            HuffmanNode<T> left = priorityQueue.poll();
            HuffmanNode<T> right = priorityQueue.poll();

            HuffmanNode<T> parent = new HuffmanNode<T>(left.getFrequency() + right.getFrequency(), left, right);
            priorityQueue.add(parent);
        }

        return priorityQueue.poll();
    }
}
