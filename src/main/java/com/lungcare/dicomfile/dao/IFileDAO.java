package com.lungcare.dicomfile.dao;

import java.io.File;
import java.io.InputStream;

import com.sun.jersey.core.header.FormDataContentDisposition;

public interface IFileDAO {
	public boolean saveFile(String saveFolder_path,InputStream fis, FormDataContentDisposition fdcd,
			int index, String folder);
	
	public boolean deleteDir(File dir);
	
	public void copyFile(String oldPath, String newPath);
	
	public void Extract(String sZipPathFile, String sDestPath);
	
	public void ExtractByHaoZip(String saveFolder_path,String zipName, String zipFolderName);
	
	public void deleteFile(File file);
	
	
}
