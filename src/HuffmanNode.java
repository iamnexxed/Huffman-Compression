public class HuffmanNode<T> implements Comparable<HuffmanNode<T>> {
    private int frequency;
    private T character;
    
    public HuffmanNode<T> left;
    public HuffmanNode<T> right;

    public HuffmanNode(T i, int frequency) {
        this.character = i;
        this.frequency = frequency;
    }

    public HuffmanNode(int frequency, HuffmanNode<T> left, HuffmanNode<T> right) {
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return toStringHelper("");
    }

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

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public T getCharacter() {
        return character;
    }

    public void setCharacter(T character) {
        this.character = character;
    }


    @Override
    public int compareTo(HuffmanNode<T> node) {
        return this.frequency - node.frequency;
    }



}

