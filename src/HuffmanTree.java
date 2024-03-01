

import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanTree<T> {

	public HuffmanNode<T> buildTree(Map<T, Integer> charFreqMap) {
        PriorityQueue<HuffmanNode<T>> priorityQueue = new PriorityQueue<>();

        for (T key : charFreqMap.keySet()) {
            if (charFreqMap.get(key) > 0) {
                priorityQueue.add(new HuffmanNode<T>(key, charFreqMap.get(key)));
            }
        }

        while (priorityQueue.size() > 1) {
        	
            HuffmanNode<T> left = priorityQueue.poll();
            HuffmanNode<T> right = priorityQueue.poll();
           
            HuffmanNode<T>  parent = new HuffmanNode<T>(left.getFrequency() + right.getFrequency(), left, right);
            priorityQueue.add(parent);
        }

        return priorityQueue.poll();
    }
    
    

    
}
