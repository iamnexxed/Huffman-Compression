import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException {
		
		String sourcePath = "data/";
		
		// Specify the folder source
		File dir = new File(sourcePath);
		// Get the file names
		String[] fileNames = dir.list();
		//System.out.println(fileNames.length);

		// Create an empty list for file info
		List<FileInfo> fileInfoList = new ArrayList<>();
		System.out.println("Compressing files...");
		// Loop through each file in the array of file names
		for(String fileName : fileNames) {
			// Create a file name relative to the source of the program
			String filePath = sourcePath + fileName;
			File file = new File(filePath);
			long fileSize = file.length();
			

			long startStackTime = System.currentTimeMillis(); // Start the timer
			String encodedFilePath = HuffmanSerializer.Encode(filePath);
			long compressionTime = System.currentTimeMillis() - startStackTime; // calculate the time
			
			File encodedFile = new File(encodedFilePath);
			long encodedFileSize = encodedFile.length();
			System.out.println("Encoded file path: " + encodedFilePath);
		
			// Decompress the file
			
			startStackTime = System.currentTimeMillis(); // Start the timer
			String decompressedFilePath = HuffmanSerializer.Decode(encodedFilePath);
			long decompressionTime = System.currentTimeMillis() - startStackTime; // calculate the time
			
			File decompressedFile = new File(decompressedFilePath);
			long decompressedFileSize = decompressedFile.length();
			
			//System.out.println("Decompression completed.");

			
			// Add the file information to the list
			fileInfoList.add(new FileInfo(
					fileName, 
					fileSize, 
					encodedFileSize, 
					compressionTime, 
					decompressionTime));
			
			if(fileSize != decompressedFileSize) {
				System.out.println("Warning: Decompress file size is different for " + filePath);
			}
		}
		
		
		// Sort by byte size
		fileInfoList.sort(Comparator.comparingLong(f -> f.byteCount));

		// Header for the table
		System.out.printf(
				"%n%-30s %-20s %-20s %-20s %-20s %-20s%n",
				"File Name", 
				"bytes", 
				"Encoded bytes", 
				"Compression Time",
				"Decompression Time",
				"Compression Ratio");

		// Output the sorted result in table form
		for (FileInfo fileInfo : fileInfoList) {
			System.out.printf(
					"%-30s %-20d %-20d %-20s %-20s %-20s%n", 
					fileInfo.fileName, 
					fileInfo.byteCount, 
					fileInfo.compressionByteCount, 
					fileInfo.compressionTime + "ms",
					fileInfo.decompressionTime + "ms",
					fileInfo.compressionRatio);
		}


		// Execution done
		System.out.println("\nExecution completed successfully!");
	}
}
