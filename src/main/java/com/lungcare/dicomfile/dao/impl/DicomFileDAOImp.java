package com.lungcare.dicomfile.dao.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.log4j.Logger;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReadParam;
import org.dcm4che3.io.DicomInputStream;

import com.lungcare.dicomfile.dao.IDicomFileDAO;

public class DicomFileDAOImp implements IDicomFileDAO {
	
	
	public static Logger logger = Logger.getLogger(DicomFileDAOImp.class);
	
	public void dcmToJPEG(String getPath,String savePath){
		logger.info("dcm To jpg");
		File folderFile = new File(getPath);
		String[] filesName = folderFile.list();
		File dcmFolder = new File(savePath);
		if(!dcmFolder.exists()){
			dcmFolder.mkdirs();
		}
		int index = 1;
		for(int i=0;i<filesName.length;i++){
			File tempFile = new File(getPath+"\\"+filesName[i]);
			try {
				BufferedImage myJpegImage = null;
				Iterator<ImageReader> iter = ImageIO.getImageReadersByFormatName("DICOM");
				ImageReader reader = (ImageReader) iter.next();
				DicomImageReadParam param = null;
				try {
					param = (DicomImageReadParam) reader.getDefaultReadParam();
				} catch (Exception e) {
					e.printStackTrace();
				}
				ImageInputStream iis = ImageIO.createImageInputStream(tempFile);
				reader.setInput(iis, false);
				myJpegImage = reader.read(0, param);
				iis.close();
				if (myJpegImage == null) {
					System. out.println("\nError: couldn't read dicom image!");
					return;
				}
				
//				int tempInt = Integer.parseInt(filesName[i]);
				DecimalFormat df = new DecimalFormat("000");
				String dstName = savePath+"\\"+df.format(index)+".jpg";//例如把名字0改为001		
				String formatName = dstName.substring(dstName.lastIndexOf(".") + 1);  
				
				ImageIO.write(myJpegImage, formatName , new File(dstName)); 
			} catch (IOException e) {
				System. out.println("\nError: couldn't read dicom image!"
						+ e.getMessage());
				return;
			}
			index++ ;
		}
	}
	
	
	
	public String getDCMFolder(String path){
		Map<String, ArrayList<String>> allFileList = new HashMap<String, ArrayList<String>>();
		addFileListToMap(path, allFileList);
		Set<String> keysSet = allFileList.keySet();
		Object[] arrayList = keysSet.toArray();
		
		//获取所有可用的CT文件夹路径
//		ArrayList<String> dcmPaths = new ArrayList<String>();
//		for (int i = 0; i < keysSet.size(); i++) {
//			ArrayList<String> valuesArrayList =  allFileList.get(arrayList[i]);
//			if(valuesArrayList.size()>0){
//				String tempDCMPath = valuesArrayList.get(0);//获取文件夹下第一个文件进行验证是否是可用CT文件
//				if(checkModility(tempDCMPath)){
//					dcmPaths.add(tempDCMPath.substring(0,tempDCMPath.lastIndexOf(File.separator)));
//				}
//			}
//
//		}
//		return dcmPaths;
		
		int maxSize = 0;
		String ctpathString="";
		for (int i = 0; i < keysSet.size(); i++) {
			ArrayList<String> valuesArrayList =  allFileList.get(arrayList[i]);
			if(valuesArrayList.size()>0){
				String tempDCMPath = valuesArrayList.get(0);
				if(checkModility(tempDCMPath)&&valuesArrayList.size()>maxSize){
					ctpathString = (String)arrayList[i];
					maxSize = valuesArrayList.size();
				}
			}
		}
		return ctpathString;

	}
	
	public void addFileListToMap(String path,Map<String, ArrayList<String>> allFileList){
		File file = new File(path);
		System.out.println("addFileListToMap : "+ path);
		
		if(!file.exists()){
			return;
		}
		
		String[] files = file.list();
		ArrayList<String> tempFilesList = new ArrayList<String>();
		
		for(int i=0;i<files.length;i++){
			if(new File(path+File.separator+files[i]).isDirectory()){
				addFileListToMap(path+File.separator+files[i],allFileList);
			}
		    else {
		    	if(isDCM(new File(path+File.separator+files[i]))){
		    		tempFilesList.add(path+File.separator+files[i]);
		    	}
			}	
		}
		allFileList.put(file.getPath(), tempFilesList);
	}
	
	//判断是否是dcm文件
	public boolean isDCM(File file){
		FileInputStream fis = null;
		if(!file.isDirectory()){
			try {
				fis = new FileInputStream(file);
				byte[] data = new byte[132];
				fis.read(data, 0, data.length);
				int b0 = data[0] & 255;
				int b1 = data[1] & 255;
				//int b2 = data[2] & 255;
				int b3 = data[3] & 255;
				
				if (data[128] == 68 && data[129] == 73 && data[130] == 67 && data[131] == 77)
				{
					return true;
				}
				else if ((b0 == 8 || b0 == 2) && b1 == 0 && b3 == 0)
				{
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(fis!=null){
					try {
						fis.close();
					} catch (IOException e) {
					}
				}
			}
		}
        return false;
	}
	
	/**
	 * 检查dcm文件中modility属性是否是"CT"来判断文件是否可用
	 * @param firstFilePath
	 * @return
	 */
    public boolean checkModility(String firstFilePath){
    	File file = new File(firstFilePath);
    	if(isDCM(file)){
    		try {
    			DicomInputStream dis = new DicomInputStream(file);
    			Attributes meta = dis.readDataset(-1, -1);
    			String modality = meta.getString(Tag.Modality);
    			if(modality.toUpperCase().equals("CT")){
    				return true;
    			}
    			dis.close();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	return false;
    }
	
	
	
	
}
