package com.lungcare.dicomfile.dao.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.lungcare.dicomfile.dao.IFileDAO;
import com.sun.jersey.core.header.FormDataContentDisposition;

public class FileDAOImp implements IFileDAO {
	
	/**
	 * 删除文件或文件夹以及所有子文件
	 */
    public boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    
	/** 
	* 复制单个文件 
	* @param oldPath String 原文件路径 如：c:/fqf.txt 
	* @param newPath String 复制后路径 如：f:/fqf.txt 
	* @return boolean 
	*/ 
	public void copyFile(String oldPath, String newPath) { 
		try {
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.flush();
				fs.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
		} 
	}
	
    /** 
     * 解压缩 
     * @param sZipPathFile 要解压的文件 
     * @param sDestPath 解压到某文件夹 
     * @return 
     */  
    public void Extract(String sZipPathFile, String sDestPath) {  
        try {  
            // 先指定压缩档的位置和档名，建立FileInputStream对象  
            FileInputStream fins = new FileInputStream(sZipPathFile);  
            // 将fins传入ZipInputStream中  
            ZipInputStream zins = new ZipInputStream(fins);  
            ZipEntry ze = null;  
            byte[] ch = new byte[256];  
            while ((ze = zins.getNextEntry()) != null) {  
            	File zfile = new File(sDestPath +File.separator+ ze.getName());  
                File fpath = new File(zfile.getParentFile().getPath());  
                if (ze.isDirectory()) {  
                    if (!zfile.exists())  
                        zfile.mkdirs();  
                    zins.closeEntry();  
                } else {  
                    if (!fpath.exists())  
                        fpath.mkdirs();  
                    FileOutputStream fouts = new FileOutputStream(zfile);  
                    int i;  
                    while ((i = zins.read(ch)) != -1)  
                        fouts.write(ch, 0, i);  
                    zins.closeEntry();  
                    fouts.close();  
                }  
            }  
            fins.close();  
            zins.close();  
        } catch (Exception e) {  
            System.err.println("Extract error:" + e.getMessage());  
        }  
    }
  
    
	/**
	 * 利用pc中的好压软件进行压缩包的解压
	 * @param saveFolder_path 压缩文件保存的路径
	 * @param zipName 压缩文件的名字
	 * @param zipFolderName 解压后的文件夹名字
	 */
	public void ExtractByHaoZip(String saveFolder_path,String zipName, String zipFolderName){
		try {
			String filenameString = "C:\\Program Files\\2345Soft\\HaoZip";
			String commandString = "cmd /c HaoZipC x " + saveFolder_path+zipName + " -o" + saveFolder_path+zipFolderName;
			System.out.println(commandString);
			Process process = Runtime.getRuntime().exec(commandString, null, new File(filenameString));
			InputStream inputStream = process.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

			// 取得输出流
			//String line = "";
			while (reader.readLine() != null) {
				//System.out.println(line);
			}

			reader.close();
			process.destroy();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    /**
     * 删除文件，文件夹以及子文件
     * @param file
     */
	public void deleteFile(File file){
		if(file.isFile()){
			file.delete();
		}else{
			String[] childFilePaths = file.list();
			for(int i=0;i<childFilePaths.length;i++){
				File childFile = new File(file.getAbsolutePath()+"\\"+childFilePaths[i]);
				deleteFile(childFile);
			}
			file.delete();
		}
	}
	
	public boolean saveFile(String saveFolder_path,InputStream fis, FormDataContentDisposition fdcd,
			int index, String folder) {
		Date date1 = new Date();
		OutputStream outpuStream = null;
		File file = new File(saveFolder_path + folder + "\\");
		if (!file.exists() && !file.isDirectory()) {
			file.mkdir();
		}
		DecimalFormat dFormat = new DecimalFormat("000");
		String fileName = dFormat.format(index);
		fileName = String.valueOf(index);
		System.out.println("changeover:"+fileName);
		String filePath = saveFolder_path + folder + "\\" + fileName;
		try {
			int read = 0;
			byte[] bytes = new byte[1024];
			outpuStream = new FileOutputStream(new File(filePath));
			while ((read = fis.read(bytes)) != -1) {
				outpuStream.write(bytes, 0, read);
			}
			outpuStream.flush();
			outpuStream.close();
			fis.close();
		} catch (IOException iox) {
			iox.printStackTrace();
			return false;
		} finally {
			if (outpuStream != null) {
				try {
					outpuStream.close();
				} catch (Exception ex) {

				}
			}
		}

		Date date2 = new Date();
		System.out.println(date2.getTime() - date1.getTime());
		return true;
	}
    
}
