package com.lungcare.dicomfile.dao;

public interface IDicomFileDAO {
	
	public void dcmToJPEG(String getPath,String savePath);
	
	public String getDCMFolder(String path);
	
	public boolean checkModility(String firstFilePath);
}
