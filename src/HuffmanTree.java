import java.util.PriorityQueue;

public class HuffmanTree {
    public HuffmanNode buildTree(int[] charFrequencies) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();

        for (char i = 0; i < charFrequencies.length; i++) {
            if (charFrequencies[i] > 0) {
                priorityQueue.add(new HuffmanNode(i, charFrequencies[i]));
            }
        }

        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();
            HuffmanNode parent = new HuffmanNode(left.frequency + right.frequency, left, right);
            priorityQueue.add(parent);
        }

        return priorityQueue.poll();
    }
}
