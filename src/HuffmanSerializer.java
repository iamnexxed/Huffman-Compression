
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;



public class HuffmanSerializer {
	private static final String encodedNameExtension = ".huffman";
	private static final String decodedPath = "extracted_data/";
	
	private static final HuffmanTree<Byte> huffmanTree = new HuffmanTree<>();

	private static final HashMap<Byte, String> charToCodeMap = new HashMap<>();
	private static final HashMap<String, Byte> codeToCharMap = new HashMap<>();

	public static String Encode(String fileName) throws IOException {
		File fileOP = new File(fileName);
		byte[] bytes = HuffmanSerializer.generateBytes(fileOP);
		
		// Step 1: Build frequency table
		HashMap<Byte, Integer> frequencies = HuffmanSerializer.getByteFrequencies(bytes);
		
		// Step 2: Build Huffman tree
		HuffmanNode<Byte> root = HuffmanSerializer.huffmanTree.buildTree(frequencies);
		int totalFreq = root.getFrequency();
		
		// Step 3: Generate codes
		HuffmanSerializer.generateCodes(root, "");
		
		// Step 4: Compress data using the generated codes
		return HuffmanSerializer.compressByteStream(bytes, fileName, totalFreq);
		
	}
	
	public static String Decode(String fileName) throws IOException {
			File file = new File(fileName);
			return HuffmanSerializer.decompressFile(file);	
	
	}


	private static HashMap<Byte, Integer> getByteFrequencies(byte[] bytes) {
		HashMap<Byte, Integer> countMap = new HashMap<>();
		for (byte b : bytes) {
			//long value = b & 0xFF; // Corrected for sign extension
			Integer count = countMap.get(b);
			if (count == null) {
				countMap.put(b, 1);
			} else {
				countMap.put(b, count + 1);
			}
		}
		return countMap;
	}

	
	private static void generateCodes(HuffmanNode<Byte> node, String code) {
		if (node != null) {
			// Check for the leaf node
			if (node.left == null && node.right == null) {
				//System.out.println(node.getCharacter());
				HuffmanSerializer.charToCodeMap.put(node.getCharacter(), code);
				HuffmanSerializer.codeToCharMap.put(code, node.getCharacter());
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
	
	
	private static String compressByteStream(byte[] byteStream, String path, int totalFreq) throws IOException {
		StringBuilder prefixSB = new StringBuilder();
		StringBuilder codeSB = new StringBuilder();
		
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
		return absPath;
	}
	
	private static String decompressFile(File inFile) throws IOException {
		HashMap<String, Byte> codeToCharMap = new HashMap<String, Byte>();

		BufferedReader br = new BufferedReader(new FileReader(inFile));
		
		// Get the first line
		String encodedString = br.readLine();
		// Find the encoding map from the line
		String[] encodings = encodedString.split(" ");
		for (int i = 0; i < encodings.length; i++) {
			String encoding = encodings[i];
			String codeChar[] = encoding.split("=");
			codeToCharMap.put(codeChar[0],Byte.parseByte(codeChar[1]));
		}
		
		int charFrequency = Integer.parseInt(br.readLine());
		br.close();
		
		BufferedInputStream bIS = new BufferedInputStream(new FileInputStream(inFile));
		
		while(bIS.read()!='\n');
		while(bIS.read()!='\n');

		StringBuilder characterSB = new StringBuilder(); 
		
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
		
		bIS.close();
		outputWriter.close();
		return outFileName;
	}
	
}
