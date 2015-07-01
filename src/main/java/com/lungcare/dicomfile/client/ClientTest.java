package com.lungcare.dicomfile.client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.UUID;

public class ClientTest {
	public static void main(String... strings) {
		System.out.println("start");
		downLoad("G:/java_tmp/" + UUID.randomUUID().toString() + ".zip",
				"http://localhost:8787/transfer/rest/remotefile/download");
	}
	public static boolean downLoad(String newfile, String url) {
		BufferedInputStream in = null;
		OutputStream out = null;
		try {
			String mkdirs = newfile.substring(0, newfile.lastIndexOf("/") + 1);
			File file = new File(mkdirs);
			/** 创建文件夹 */
			if (!file.exists()) {
				file.mkdirs();
			}
			file = new File(newfile);
			if (!file.exists()) {
				file.createNewFile();
			}
			URL get = new URL(url);
			in = new BufferedInputStream(get.openStream());
			byte[] bytes = new byte[100];
			out = new FileOutputStream(file);
			int len;
			while ((len = in.read(bytes)) > 0) {
				out.write(bytes, 0, len);
			}
			out.flush();
			out.close();
			in.close();
			return true;
		} catch (Exception e) {
			try {
				in.close();
				out.close();
			} catch (Exception en) {

			}
			return false;
		}
	}
}
