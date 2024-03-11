import java.util.HashMap;

// Class to construct Huffman tree for data compression using Huffman algorithm.
public class HuffmanTree<T> {

    // Builds Huffman tree from a map of character frequencies and returns the root node.
    public HuffmanNode<T> buildTree(HashMap<T, Integer> charFreqMap) {
        CustomPriorityQueue<HuffmanNode<T>> priorityQueue = new CustomPriorityQueue<>();
    // Add each character and its frequency as a node to the priority queue.
        for (T key : charFreqMap.keySet()) {
            if (charFreqMap.get(key) > 0) {
                priorityQueue.add(new HuffmanNode<T>(key, charFreqMap.get(key)));
            }
        }
        // Merge two lowest-frequency nodes until there's only one node left in the queue.
        while (!priorityQueue.isEmpty() && priorityQueue.size() > 1) {
            HuffmanNode<T> left = priorityQueue.poll();
            HuffmanNode<T> right = priorityQueue.poll();
            // Create a new parent node with frequency equal to the sum of its children's frequencies.
            HuffmanNode<T> parent = new HuffmanNode<T>(left.getFrequency() + right.getFrequency(), left, right);
            priorityQueue.add(parent);
        }
        // Return the root of the Huffman tree.
        return priorityQueue.poll();
    }
}
