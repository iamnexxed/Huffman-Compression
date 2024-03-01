import java.util.HashMap;
import java.util.Map;

public class Main {
	private final HuffmanTree huffmanTree = new HuffmanTree();
	private final Map<Character, String> charToCodeMap = new HashMap<>();
	private final Map<String, Character> codeToCharMap = new HashMap<>();

	public void compress(String data) {
		// Step 1: Build frequency table
		int[] frequencies = new int[256];
		for (char c : data.toCharArray()) {
			frequencies[c]++;
		}

		// Step 2: Build Huffman tree
		HuffmanNode root = huffmanTree.buildTree(frequencies);

		// Step 3: Generate codes
		generateCodes(root, "");

		// Compress data using the generated codes
	}

	private void generateCodes(HuffmanNode node, String code) {
		if (node != null) {
			if (node.left == null && node.right == null) {
				charToCodeMap.put(node.character, code);
				codeToCharMap.put(code, node.character);
			}
			generateCodes(node.left, code + "0");
			generateCodes(node.right, code + "1");
		}
	}

	public String decompress(String data) {
		return "";
	}
}
