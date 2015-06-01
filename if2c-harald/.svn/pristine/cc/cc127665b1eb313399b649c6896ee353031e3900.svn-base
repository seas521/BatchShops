import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.io.FileUtils;


public class FileRame {

	public static void main(String[] args) throws Exception{
		String filePath = "C:\\Users\\145027.IZP\\Desktop\\批量开店\\1";

		File filedir = new File(filePath);
		int i =0;
		for(File temp : filedir.listFiles()){
			
			File base = new File(filePath+File.separator+i++);
			for(File ff: base.listFiles()){
				
			}
			base.mkdirs();
			filedir.renameTo(base);
		}
	}

}
