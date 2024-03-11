// This class represents a node in a Huffman tree, which is used in Huffman coding for data compression.
// Each node stores a character and its frequency in the source data, along with references to left and right child nodes.
public class HuffmanNode<T> implements Comparable<HuffmanNode<T>> {
    private int frequency; // The frequency of occurrence of the character.
    private T character; // The character this node represents.
    // References to the left and right children in the Huffman tree.
    public HuffmanNode<T> left;
    public HuffmanNode<T> right;

    // Constructor for leaf nodes which contain a character and its frequency.
    public HuffmanNode(T i, int frequency) {
        this.character = i;
        this.frequency = frequency;
    }

    // Constructor for internal nodes which don't contain a character but have a frequency (sum of its children's frequencies)
    // and references to its left and right child nodes.
    public HuffmanNode(int frequency, HuffmanNode<T> left, HuffmanNode<T> right) {
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    // Generates a string representation of this node and its children
    @Override
    public String toString() {
        return toStringHelper("");
    }

    // Helper method to create a string representation of this node and its descendants
    // with indentation to represent the tree structure.
    private String toStringHelper(String prefix) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        if (character != null) {
            sb.append("Char: ").append(character).append(" (Freq: ").append(frequency).append(")\n");
        } else {
            sb.append("Node (Freq: ").append(frequency).append(")\n");
        }
        if (left != null) {
            sb.append(left.toStringHelper(prefix + "    "));
        }
        if (right != null) {
            sb.append(right.toStringHelper(prefix + "    "));
        }
        return sb.toString();
    }
    // Getter for frequency.
    public int getFrequency() {
        return frequency;
    }
    // Setter for frequency.
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    // Getter for character.
    public T getCharacter() {
        return character;
    }

    // Setter for character.
    public void setCharacter(T character) {
        this.character = character;
    }

    // Compares this node with another HuffmanNode based on frequency.
    // This is used to order nodes in a priority queue during the construction of the Huffman tree.
    @Override
    public int compareTo(HuffmanNode<T> node) {
        return this.frequency - node.frequency;
    }



}

