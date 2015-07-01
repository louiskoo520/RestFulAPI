package com.lungcare.dicomfile;

import java.awt.List;
import java.io.File;
import java.util.ArrayList;

import com.lungcare.dicomfile.restful.RemoteFileTransferResource;
import com.lungcare.dicomfile.util.ZipUtils;

/**
 * Hello world!
 * 
 */
public class App {
	
	public static void main(String[] args) {
		App app = new App();
		app.getAllZip();
		ArrayList<String> zipList = app.getAllZip();
		for(String string : zipList) {
			System.out.println(string);
		}
	}

	public void getClassPath() {
		String pathString = RemoteFileTransferResource.class.getClass()
				.getResource("/").getPath()
				.replace("target/classes/", "src/main/webapp/testFile/");
		ZipUtils.createZip(pathString + "557524fb-f3a5-f3c2-8ac1-61317b1eafc7",
				pathString + "557524fb-f3a5-f3c2-8ac1-61317b1eafc7" + ".zip");
		String str = pathString.replaceFirst("/", "");
		System.out.println(str);
	}

	public ArrayList<String> getAllZip() {
		String pathString = RemoteFileTransferResource.class.getClass()
				.getResource("/").getPath().replace("target/classes/", "src/main/webapp/testFile/");
		String str = pathString.replaceFirst("/", "");
		File file = new File(str);
		String test[];
		ArrayList<String> zipList = new ArrayList<String>(); 
		test = file.list();
		for (int i = 0; i < test.length; i++) {
			File testFile = new File(str+test[i]);
			if(!testFile.isDirectory()){
				zipList.add(test[i]);
			}
		}
		return zipList;
	}
}
