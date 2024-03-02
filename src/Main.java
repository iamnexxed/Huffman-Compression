import java.io.File;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		// Original file path
		String originalFilePath = "data/test.txt";

		// Check original file size
		File originalFile = new File(originalFilePath);
		long originalFileSize = originalFile.length();
		System.out.println("Original file size: " + originalFileSize + " bytes");

		// Compress the file
		String encodedFilePath = HuffmanSerializer.Encode(originalFilePath);
		System.out.println("Compression completed. Encoded file path: " + encodedFilePath);

		// Check encoded file size
		File encodedFile = new File(encodedFilePath);
		long encodedFileSize = encodedFile.length();
		System.out.println("Encoded file size: " + encodedFileSize + " bytes");

		// Decompress the file
		String decompressedFilePath = HuffmanSerializer.Decode(encodedFilePath);
		System.out.println("Decompression completed.");

		File decompressedFile = new File(decompressedFilePath);
		long decompressedFileSize = decompressedFile.length();
		System.out.println("Decompressed file size: " + decompressedFileSize + " bytes");

		// Execution done
		System.out.println("Execution completed successfully!");
	}
}
