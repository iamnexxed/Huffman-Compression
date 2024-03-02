
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;


public class HuffmanSerializer {
	private static final String encodedNameExtension = ".huffman";
	private static final HuffmanTree<Long> huffmanTree = new HuffmanTree<>();
	// Use CustomHashMap instead of HashMap
	private static final CustomHashMap<Long, String> charToCodeMap = new CustomHashMap<>();
	private static final CustomHashMap<String, Long> codeToCharMap = new CustomHashMap<>();

	public static void Encode(String fileName) throws IOException {
		File fileOP = new File(fileName);
		byte[] bytes = HuffmanSerializer.generateBytes(fileOP);
		
		// Step 1: Build frequency table
		CustomHashMap<Long, Integer> frequencies = HuffmanSerializer.getByteFrequencies(bytes);
		
		// Step 2: Build Huffman tree
		HuffmanNode<Long> root = HuffmanSerializer.huffmanTree.buildTree(frequencies);
		int totalFreq = root.getFrequency();
		
		// Step 3: Generate codes
		HuffmanSerializer.generateCodes(root, "");
		
		// Step 4: Compress data using the generated codes
		HuffmanSerializer.compressByteStream(bytes, fileName, totalFreq);
		
	}
	
	public static void Decode(String fileName) throws IOException {
			File file = new File(fileName);
			HuffmanSerializer.decompressFile(file);	
			file.delete();
	
	}


	private static CustomHashMap<Long, Integer> getByteFrequencies(byte[] bytes) {
		CustomHashMap<Long, Integer> countMap = new CustomHashMap<>();
		for (byte b : bytes) {
			long value = b & 0xFF; // Corrected for sign extension
			Integer count = countMap.get(value);
			if (count == null) {
				countMap.put(value, 1);
			} else {
				countMap.put(value, count + 1);
			}
		}
		return countMap;
	}

	
	private static void generateCodes(HuffmanNode<Long> node, String code) {
		if (node != null) {
			// Check for the leaf node
			if (node.left == null && node.right == null) {
				// System.out.println(node.character);
				HuffmanSerializer.charToCodeMap.put((long)node.getCharacter(), code);
				HuffmanSerializer.codeToCharMap.put(code, (long)node.getCharacter());
			}
			HuffmanSerializer.generateCodes(node.left, code + "0");
			HuffmanSerializer.generateCodes(node.right, code + "1");
		}
	}
    
	
	private static byte[] generateBytes(File file) {
		byte[] readBytes = new byte[(int)file.length()];
		try (FileInputStream fis = new FileInputStream(file)) {
			fis.read(readBytes);
		} catch(Exception e) {
			System.err.println("File not found: " + file.getName());
		}
		return readBytes;
	}
	
	
	private static void compressByteStream(byte[] byteStream, String path, int totalFreq) throws IOException {
		StringBuilder prefixSB = new StringBuilder();
		StringBuilder codeSB = new StringBuilder();
		
		String codeChars = HuffmanSerializer.codeToCharMap.toString();
		codeChars = codeChars.replace(", ", " ");
		// Remove start and end brackets
		codeChars= codeChars.substring(1, codeChars.length()-1);
		prefixSB.append(codeChars);
		prefixSB.append('\n');
		prefixSB.append(totalFreq);
		prefixSB.append('\n');
		
		
		for(int i = 0; i < byteStream.length; i++) {
			long c = (long) (byteStream[i]);
			codeSB.append(HuffmanSerializer.charToCodeMap.get(c));
		}
		
		BufferedOutputStream br = new BufferedOutputStream(
				new FileOutputStream(path + HuffmanSerializer.encodedNameExtension));
		
		for(int i = 0; i < prefixSB.length(); i++) {
			br.write(prefixSB.charAt(i));
		}
		
		String oneByteString = "";
		for(int i = 0; i < codeSB.length(); i++) {
			oneByteString = oneByteString + codeSB.charAt(i);
					
			if(oneByteString.length() == 8) {
				//System.out.println(oneByteString);
				int c = Integer.parseInt(oneByteString, 2);
				br.write(c);
				
				oneByteString = "";	
			}
		}
		
		// Check if one byte isn't complete at the end of the byte array
		if(oneByteString != "") {
			String formatted = (oneByteString + "00000000").substring(0, 8);
			br.write(Integer.parseInt(formatted, 2));
		}
		br.close();
		
	}
	
	private static void decompressFile(File outFile) throws IOException {
		HashMap<String, Byte> codeToCharMap = new HashMap<String,Byte>();

		BufferedReader br = new BufferedReader(new FileReader(outFile));
		 
		String encodedString = br.readLine();
		String[] encodings = encodedString.split(" ");

		
	}
	
}
