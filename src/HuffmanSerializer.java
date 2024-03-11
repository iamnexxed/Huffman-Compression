
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

// This class compresses and decompresses files using Huffman coding.
// It handles encoding and decoding by leveraging byte frequency.
public class HuffmanSerializer {
	// Constants for file paths and extensions used during serialization and deserialization.
	private static final String encodedNameExtension = ".huffman";
	private static final String encodedPath = "compressed_data/";
	private static final String decodedPath = "extracted_data/";
	// Static HuffmanTree instance for building and manipulating Huffman trees.
	private static final HuffmanTree<Byte> huffmanTree = new HuffmanTree<>();
	// Maps for character to Huffman code conversion and vice versa.
	private static final HashMap<Byte, String> charToCodeMap = new HashMap<Byte, String>();
	private static final HashMap<String, Byte> codeToCharMap = new HashMap<>();

	// Encodes a file into Huffman compressed format.
	public static String Encode(String fileName) throws IOException {
		// Clear previous mappings
		HuffmanSerializer.charToCodeMap.clear();
		HuffmanSerializer.codeToCharMap.clear();
		
		File fileOP = new File(fileName);
		byte[] bytes = HuffmanSerializer.generateBytes(fileOP);

		// Step 1: Read bytes from file and build frequency table.
		HashMap<Byte, Integer> frequencies = HuffmanSerializer.getByteFrequencies(bytes);

		// Step 2: Construct Huffman tree from frequencies.
		HuffmanNode<Byte> root = HuffmanSerializer.huffmanTree.buildTree(frequencies);
		int totalFreq = root.getFrequency();

		// Step 3: Generate Huffman codes for each byte.
		if(root.left == null & root.right == null)
			HuffmanSerializer.generateCodes(root, "0");
		else 
			HuffmanSerializer.generateCodes(root, "");

		// Step 4: Compress byte stream into Huffman encoding and write to file.
		return HuffmanSerializer.compressByteStream(
				bytes, 
				HuffmanSerializer.encodedPath + fileOP.getName(), 
				totalFreq);
		
	}

	// Decodes a Huffman compressed file back to its original form.
	public static String Decode(String fileName) throws IOException {
		// Read Huffman encoded file and parse encoding information.
		// Reconstruct original byte stream from Huffman codes.
			File file = new File(fileName);
			return HuffmanSerializer.decompressFile(file);	
	
	}

	// Calculates the frequency of each byte in a given array of bytes.
	private static HashMap<Byte, Integer> getByteFrequencies(byte[] bytes) {
		HashMap<Byte, Integer> countMap = new HashMap<>();
		for (byte b : bytes) {
			//long value = b & 0xFF; // Corrected for sign extension
			Integer count = countMap.get(b);
			if (count == null) {
				countMap.put(b, 1);// If this is the first occurrence, initialize count to 1.
			} else {
				countMap.put(b, count + 1);// If already present, increment the count.
			}
		}
		return countMap;
	}

	// Recursively generates Huffman codes for each byte character starting from the root node.
	private static void generateCodes(HuffmanNode<Byte> node, String code) {
		if (node != null) {
			// If it's a leaf node (has no children), map the character to its code.
			if (node.left == null && node.right == null) {
				//System.out.println(node.getCharacter());
				HuffmanSerializer.charToCodeMap.put(node.getCharacter(), code);
				HuffmanSerializer.codeToCharMap.put(code, node.getCharacter());
			}
			// Recursively generate codes for left and right children, appending '0' for left and '1' for right
			HuffmanSerializer.generateCodes(node.left, code + "0");
			HuffmanSerializer.generateCodes(node.right, code + "1");
		}
	}

	// Reads a file and converts it into an array of bytes.
	private static byte[] generateBytes(File file) {
		byte[] readBytes = new byte[(int)file.length()];
		try (FileInputStream fis = new FileInputStream(file)) {
			fis.read(readBytes); // Read file content into byte array.
		} catch(Exception e) {
			System.err.println("File not found: " + file.getName());
		}
		return readBytes;
	}

    //	Compresses a byte array using Huffman encoding and writes the encoded data to a file.
	private static String compressByteStream(
			byte[] byteStream, 
			String path, 
			int totalFreq) throws IOException {
		StringBuilder prefixSB = new StringBuilder();
		StringBuilder codeSB = new StringBuilder();
		// Convert the character-to-code map to a single string, removing commas and brackets for file writing
		String codeChars = HuffmanSerializer.codeToCharMap.toString();
		codeChars = codeChars.replace(", ", " ");
		// Remove start and end brackets
		codeChars= codeChars.substring(1, codeChars.length()-1);
		// To store first line as encoding map
		prefixSB.append(codeChars);
		prefixSB.append('\n');
		// To store first line as frequency count
		prefixSB.append(totalFreq);
		prefixSB.append('\n');
		// Third line to store encodings

		// Build the string of Huffman codes for the byteStream
		for(int i = 0; i < byteStream.length; i++) {
			byte c = (byteStream[i]);
			String data = HuffmanSerializer.charToCodeMap.get(c);
			if(data == null) 
				System.out.println("Null Character: " + c);

			codeSB.append(data);
		}
		String absPath = path + HuffmanSerializer.encodedNameExtension;
		BufferedOutputStream br = new BufferedOutputStream(
				new FileOutputStream(absPath));

		// Write the prefix and encoded data to the file
		for(int i = 0; i < prefixSB.length(); i++) {
			br.write(prefixSB.charAt(i));
		}
		// Convert every 8 bits (1 byte) of the encoded string into binary and write as a byte
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
		return absPath;// Return the path of the compressed file
	}

	// Decompresses a Huffman encoded file back into its original byte stream.
	private static String decompressFile(File inFile) throws IOException {
		// Temporary map for decoding Huffman codes back to bytes.
		HashMap<String, Byte> codeToCharMap = new HashMap<String, Byte>();


		// Read the encoding map and frequency count from the file header.
		BufferedReader br = new BufferedReader(new FileReader(inFile));

		// Extract the encoding map from the first line.
		String encodedString = br.readLine();
		// Find the encoding map from the line
		String[] encodings = encodedString.split(" ");
		for (int i = 0; i < encodings.length; i++) {
			String encoding = encodings[i];
			String codeChar[] = encoding.split("=");
			codeToCharMap.put(codeChar[0],Byte.parseByte(codeChar[1]));
		}

		// Read the total character frequency to determine when to stop decoding.
		int charFrequency = Integer.parseInt(br.readLine());
		br.close();
		// Skip to the binary encoded content.
		BufferedInputStream bIS = new BufferedInputStream(new FileInputStream(inFile));
		while(bIS.read()!='\n');
		while(bIS.read()!='\n');

		// Reconstruct the binary string representation of the encoded file.
		StringBuilder characterSB = new StringBuilder();
		// Prepare the output file.
		String outFileName = HuffmanSerializer.decodedPath;
		outFileName = outFileName + inFile.getName();
		outFileName = outFileName.substring(
				0, outFileName.length() - HuffmanSerializer.encodedNameExtension.length());

		BufferedOutputStream outputWriter = new BufferedOutputStream(new FileOutputStream(outFileName));
		int readByte = 0;
		while((readByte = bIS.read())!= -1) {
			String formStr = Integer.toBinaryString(readByte);
			String byteFormat = ("00000000" + formStr).substring(formStr.length());
			characterSB.append(byteFormat);
		}

		// Decode the binary string using the Huffman encoding map.
		int currentCharacterCount = 0;
		String currentCode = "";
		for (int i = 0; i < characterSB.length(); i++) {
			currentCode = currentCode + characterSB.charAt(i);
			if(currentCharacterCount < charFrequency && codeToCharMap.containsKey(currentCode)) {
				outputWriter.write(codeToCharMap.get(currentCode));
				currentCode = "";
				currentCharacterCount++;
			}	
		}
		// Finalize and return the output file name.
		bIS.close();
		outputWriter.close();
		return outFileName;
	}
	
}
