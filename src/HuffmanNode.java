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

