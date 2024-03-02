
import java.io.IOException;


public class Main {
	
	
	public static void main(String[] args) throws IOException {
		//HuffmanSerializer.Encode("data/ss.png");
		String path = HuffmanSerializer.Encode("data/test.txt");
		//String path = HuffmanSerializer.Encode("data/LAND2.BMP");
		//String path = HuffmanSerializer.Encode("data/landscape.ARW");

		HuffmanSerializer.Decode(path);
		//HuffmanSerializer.Encode("data/Assessment.pdf");
		//HuffmanSerializer.decompressFile(new File(""));
		// TODO Auto-generated method stub
		System.out.println("Executd!");
	}
}
