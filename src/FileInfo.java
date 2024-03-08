// Class for storing file information
class FileInfo {
	// file name 
    public String fileName;
    // Byte count in the file
    public long byteCount;
    public long compressionByteCount;
    public long compressionTime;
    public long decompressionTime;
    public float compressionRatio;
  
    // Constructor for creating the file info
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