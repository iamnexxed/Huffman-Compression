// This class holds file statistics related to compression and decompression processes.
class FileInfo {

    public String fileName; // The name of the file.
    public long byteCount; // Original file size in bytes.
    public long compressionByteCount; // File size after compression in bytes.
    public long compressionTime; // Time taken for compression in milliseconds.
    public long decompressionTime; // Time taken for decompression in milliseconds.
    public float compressionRatio; // Compression efficiency ratio.


   // Initializes FileInfo with detailed metrics.
   // Calculates compression ratio as 1 - (compressed size / original size),
   // ensuring it's not less than 0.

    public FileInfo(
    		String fileName, 
    		long byteCount, 
    		long compressionByteCount,
    		long compressionTime,
    		long decompressionTime) {
        this.fileName = fileName;
        this.byteCount = byteCount;
        this.compressionByteCount = compressionByteCount;
        this.compressionTime = compressionTime;
        this.decompressionTime = decompressionTime;
        this.compressionRatio = 1 - (float)compressionByteCount / (float) byteCount;
        if(this.compressionRatio < 0) {
        	this.compressionRatio = 0;
        }
        
    }
}