package com.lungcare.dicomfile.dao.impl;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReadParam;
import org.dcm4che3.io.DicomInputStream;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.lungcare.dicomfile.dao.IAlgorithmDAO;
import com.lungcare.dicomfile.dao.IRemoteFileTransferDAO;
import com.lungcare.dicomfile.entity.BmpPathEntity;
import com.lungcare.dicomfile.entity.ReceiveEntity;
import com.lungcare.dicomfile.entity.SendEntity;
import com.lungcare.dicomfile.util.ZipUtils;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

@Transactional
public class RemoteFileTransferDAOImp implements IRemoteFileTransferDAO {

	private static Logger logger = Logger.getLogger(RemoteFileTransferDAOImp.class);
	private static  String SAVEFOLDER_PATH = new File("").getAbsolutePath() +"/src/main/webapp/testFile/";
	private static  String BMPFOLDER_PATH = new File("").getAbsolutePath() +"/src/main/webapp/allBmps/";
	//private static final String SEND_IP = "192.168.1.13";
	//private static final int SEND_PORT = 8787;
	//private static final int PAGESIZE = 4;
	private SessionFactory sessionFactory;
	
	@Autowired
	private IAlgorithmDAO algorithmDAOImp;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void uploadFile(FormDataMultiPart formParams,HttpServletRequest request,String cid) {
		logger.info("uploadFile");

		final ReceiveEntity receiveEntity = new ReceiveEntity();
		receiveEntity.setId(cid);//设置id
		
		String remoteHostString = "";
		if (request.getHeader("x-forwarded-for") == null) {
			remoteHostString = request.getRemoteAddr();//获取上传者的IP地址
		} else {
			remoteHostString = request.getHeader("x-forwarded-for");
		}
		System.out.println("ip : " + remoteHostString);
		receiveEntity.setIp(remoteHostString);//设置ip
		
		SAVEFOLDER_PATH = new File(request.getServletContext().getRealPath("/WEB-INF")).getParent()+"\\testFile\\";
		BMPFOLDER_PATH = new File(request.getServletContext().getRealPath("/WEB-INF")).getParent()+"\\allBmps\\";
		
		int port = request.getRemotePort();
		receiveEntity.setPort(port);//设置port
		
		receiveEntity.setCompleteTime(0);
		
		receiveEntity.setComplete(false);//设置complete
		receiveEntity.setJpgAxial(false);//设置jpgAxial
		receiveEntity.setBmpAxial(false);//设置bmpAxial
		receiveEntity.setBmpCoronal(false);//设置bmpCoronal
		receiveEntity.setBmpSagittal(false);//设置bmpSagittal
		receiveEntity.setLeakingFileName(null);//设置leakingFileName
		
//		receiveEntity.setTotalFiles(formParams.getFields().values().size());//设置totalfiles
		
		receiveEntity.setFailed(0);//设置failed
		
		receiveEntity.setReceived(0);//设置received
		
		receiveEntity.setSpeed(0);//设置speed
		
		Map<String, List<FormDataBodyPart>> fieldsByName = formParams.getFields();
		
		int totalNum = 0;
		for (List<FormDataBodyPart> fields : fieldsByName.values()) {
			totalNum = fields.size();
		}
		
		receiveEntity.setTotalFiles(totalNum);//设置totalfiles
		receiveEntity.setDate(new Date());//设置时间
		receiveEntity.setSavedFolder(SAVEFOLDER_PATH + cid + "\\");//设置接受路径
		addReceiveEntity(receiveEntity);
		File saveFolder = new File(SAVEFOLDER_PATH);
		if(!saveFolder.exists()){
			saveFolder.mkdirs();
		}
		
		int index = 1;
		int failedNum = 0;
		for (List<FormDataBodyPart> fields : fieldsByName.values()) {
			for (FormDataBodyPart field : fields) {
				InputStream is = field.getEntityAs(InputStream.class);
				//String fileName = field.getName();
				FormDataContentDisposition fdcd = field
						.getFormDataContentDisposition();
				if (!saveFile(is, fdcd, index, cid)) {
					failedNum+=1;
					updateFailedReceiveEntity(receiveEntity, failedNum);//更新failed
				}
				updateReceiveEntity(receiveEntity, index);//更新received
				
					try {
						if(is!=null){
							is.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				
				++index;
			}
		}
		
		//添加病人信息
		String firstDCMPath = SAVEFOLDER_PATH+cid+"\\1";
		try {
			updateReceiveEntity(receiveEntity, firstDCMPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//将dcm文件转化成jpeg
		final String getPath = SAVEFOLDER_PATH + cid;
		final String savePath = BMPFOLDER_PATH + cid + "\\dcm\\axial";
	
		final String ctResourcePath =  SAVEFOLDER_PATH+cid;
		final String ctZipPath = SAVEFOLDER_PATH+ receiveEntity.getPatientName()+"_"+cid+".zip";
		new Thread(
				new Runnable() {
					public void run() {
						dcmToJPEG(getPath, savePath);
						updateReceiveEntityJPGAxial(receiveEntity);
						System.out.println("dcm to jpg over!");
						ZipUtils.createZip(ctResourcePath, ctZipPath);
					}
				}
		).start();

		
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		//sendToOther(cid, SEND_IP, SEND_PORT);
		
		//分割dcm文件变成三位图并压缩
		handlerSegAndZip(receiveEntity, cid);
	}
		
	@Override
	public void uploadSingleFile(FormDataMultiPart formParams,HttpServletRequest request, final String cid) {
		logger.info("uploadFile");

		final ReceiveEntity receiveEntity = new ReceiveEntity();
		receiveEntity.setId(cid);//设置id
		
		String remoteHostString = "";
		if (request.getHeader("x-forwarded-for") == null) {
			remoteHostString = request.getRemoteAddr();//获取上传者的IP地址
		} else {
			remoteHostString = request.getHeader("x-forwarded-for");
		}
		System.out.println("ip : " + remoteHostString);
		receiveEntity.setIp(remoteHostString);//设置ip
		
		SAVEFOLDER_PATH = new File(request.getServletContext().getRealPath("/WEB-INF")).getParent()+"\\testFile\\";
		BMPFOLDER_PATH = new File(request.getServletContext().getRealPath("/WEB-INF")).getParent()+"\\allBmps\\";
		
		int port = request.getRemotePort();
		receiveEntity.setPort(port);//设置port
		receiveEntity.setCompleteTime(0);//设置completeTime
		receiveEntity.setComplete(false);//设置complete
		receiveEntity.setJpgAxial(false);//设置jpgAxial
		receiveEntity.setBmpAxial(false);//设置bmpAxial
		receiveEntity.setBmpCoronal(false);//设置bmpCoronal
		receiveEntity.setBmpSagittal(false);//设置bmpSagittal
		receiveEntity.setFailed(0);//设置failed
		receiveEntity.setReceived(0);//设置received
		receiveEntity.setSpeed(0);//设置speed
		receiveEntity.setLeakingFileName(null);//设置leakingFileName
		
		receiveEntity.setDate(new Date());//设置时间
		receiveEntity.setSavedFolder(SAVEFOLDER_PATH + cid + "\\");//设置接受路径
		int totalNum = 0;
		
		Map<String, List<FormDataBodyPart>> fieldsByName = formParams.getFields();
		for (List<FormDataBodyPart> fields : fieldsByName.values()) {
			String zipName = fields.get(0).getContentDisposition().getFileName();
			zipName = "download.zip";
			for (FormDataBodyPart field : fields) {
				InputStream is = field.getEntityAs(InputStream.class);
				OutputStream outpuStream = null;
				//创建保存路径
				File file = new File(SAVEFOLDER_PATH + cid + "\\");
				if (!file.exists() && !file.isDirectory()) {
					file.mkdirs();
				}
				//保存文件路径
				String filePath = SAVEFOLDER_PATH + zipName;
				try {
					int read = 0;
					byte[] bytes = new byte[1024];
					outpuStream = new FileOutputStream(new File(filePath));
					while ((read = is.read(bytes)) != -1) {
						outpuStream.write(bytes, 0, read);
					}
					outpuStream.flush();
					outpuStream.close();
					is.close();
				} catch (IOException iox) {
					iox.printStackTrace();
				} finally {
					if (outpuStream != null) {
						try {
							outpuStream.close();
						} catch (Exception ex) {
				}}}
			}
			
//			String zipFolderName = zipName.substring(0, zipName.lastIndexOf("."));
			String zipFolderName = "zip_"+cid;
//			Extract(SAVEFOLDER_PATH+zipName, SAVEFOLDER_PATH+zipFolderName);
			
			try {
				String filenameString = "C:\\Program Files\\2345Soft\\HaoZip";
				String commandString = "cmd /c HaoZipC x " + SAVEFOLDER_PATH+zipName + " -o" + SAVEFOLDER_PATH+zipFolderName;
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
			
			if(new File(SAVEFOLDER_PATH+zipName).exists()){
				new File(SAVEFOLDER_PATH+zipName).delete();
			}
			
			String dcmFolderPath = getDCMFolder(SAVEFOLDER_PATH+zipFolderName);
//			ArrayList<String> dcmPaths = getDCMFolder(SAVEFOLDER_PATH+zipFolderName);
//			for(int i=0;i<dcmPaths.size();i++){
//				String dcmFolderPath = dcmPaths.get(i);
				System.out.println("dcmFolderPath:"+dcmFolderPath);
				File oldFolder = new File(dcmFolderPath);
				if(oldFolder.isDirectory()){
					String[] fileStrings = oldFolder.list();
					for(int j = 0;j<fileStrings.length;j++){
						if(checkModility(oldFolder+File.separator+fileStrings[j])){
							totalNum++;
							copyFile(oldFolder+File.separator+fileStrings[j], SAVEFOLDER_PATH+cid+File.separator+totalNum);
						}
					}
				}
//			}
		}
		
		deleteDir(new File(SAVEFOLDER_PATH+"zip_"+cid));
		
		
		receiveEntity.setTotalFiles(totalNum);//设置totalfiles
		addReceiveEntity(receiveEntity);
		
		updateFailedReceiveEntity(receiveEntity, 0);//更新failed
		updateReceiveEntity(receiveEntity, totalNum);//更新received
					

		
		//添加病人信息
		String firstDCMPath = SAVEFOLDER_PATH+cid+"\\1";
		try {
			updateReceiveEntity(receiveEntity, firstDCMPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//将dcm文件转化成jpeg
		final String getPath = SAVEFOLDER_PATH + cid;
		final String savePath = BMPFOLDER_PATH + cid + "\\dcm\\axial";
	
		final String ctResourcePath =  SAVEFOLDER_PATH+cid;
		final String ctZipPath = SAVEFOLDER_PATH+ receiveEntity.getPatientName()+"_"+cid+".zip";
		new Thread(
				new Runnable() {
					public void run() {
						dcmToJPEG(getPath, savePath);
						updateReceiveEntityJPGAxial(receiveEntity);
						System.out.println("dcm to jpg over!");
						ZipUtils.createZip(ctResourcePath, ctZipPath);
					}
				}
		).start();
		
		//分割dcm文件变成三位图并压缩
		handlerSegAndZip(receiveEntity, cid);
	}
	
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
		FileInputStream fis = new FileInputStream(file);
		if(!file.isDirectory()){
			try {
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
	
	public ReceiveEntity getReceiveEntity(String id) {
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			Transaction transaction = session.beginTransaction();
			Query query = session.createQuery("from ReceiveEntity r where r.id=?");
			query.setParameter(0, id);
			ReceiveEntity receiveEntity = (ReceiveEntity)query.uniqueResult();
			transaction.commit();
			return receiveEntity;
		} else {
			System.out.println("this.sessionFactory.getCurrentSession().is null");
			return null;
		}
	}

	
	private boolean saveFile(InputStream fis, FormDataContentDisposition fdcd,
			int index, String folder) {
		Date date1 = new Date();
		OutputStream outpuStream = null;
		File file = new File(SAVEFOLDER_PATH + folder + "\\");
		if (!file.exists() && !file.isDirectory()) {
			file.mkdir();
		}
		DecimalFormat dFormat = new DecimalFormat("000");
		String fileName = dFormat.format(index);
		fileName = String.valueOf(index);
		System.out.println("changeover:"+fileName);
		String filePath = SAVEFOLDER_PATH + folder + "\\" + fileName;
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

	public boolean addReceiveEntity(ReceiveEntity receiveEntity) {
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			System.out
					.println("this.sessionFactory.getCurrentSession().isOpen()");
			Transaction transaction = session.beginTransaction();
			session.save(receiveEntity);
			session.flush();
			transaction.commit();
			return true;
		} else {
			System.out
					.println("this.sessionFactory.getCurrentSession().is null");
		}
		return false;
	}

	public boolean updateReceiveEntity(ReceiveEntity receiveEntity,
			int receivedNum) {
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			System.out
					.println("this.sessionFactory.getCurrentSession().isOpen()");
			Transaction transaction = session.beginTransaction();
			Query query = session
					.createQuery("update ReceiveEntity t set t.received =? where t.id=?");
			query.setParameter(0, receivedNum);
			System.out.println("receivedNum:"+receivedNum);
			String id = receiveEntity.getId();
			query.setParameter(1, id);
			query.executeUpdate();
			transaction.commit();
			return true;
		} else {
			System.out
					.println("this.sessionFactory.getCurrentSession().is null");
		}
		return false;
	}
	
	public boolean updateReceiveEntity(ReceiveEntity receiveEntity,String firstDCMString) throws IOException{
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			File file = new File(firstDCMString);
			
			DicomInputStream dis = new DicomInputStream(file);
			
			Attributes meta = dis.readDataset(-1, -1);
			String accessionNumber = meta.getString(Tag.AccessionNumber);
			String patientName = meta.getString(Tag.PatientName);
			receiveEntity.setPatientName(patientName);
			String studyDate = meta.getString(Tag.StudyDate);
			String institutionName = meta.getString(Tag.InstitutionName);
			double pixelSpacing = Double.valueOf(meta.getString(Tag.PixelSpacing));
			double sliceThickness = Double.valueOf(meta.getString(Tag.SliceThickness));
			receiveEntity.setPixelSpacing(pixelSpacing);
			receiveEntity.setSliceThickness(sliceThickness);
			Transaction transaction = session.beginTransaction();
			Query query = session.createQuery("update ReceiveEntity t set t.accessionNumber =?, " +
					"t.patientName=?, t.studyDate=? ,t.pixelSpacing=?,t.sliceThickness=?,t.institutionName=? where t.id=?");
			query.setParameter(0, accessionNumber);
			query.setParameter(1, patientName);
			query.setParameter(2, studyDate);
			query.setParameter(3, pixelSpacing);
			query.setParameter(4, sliceThickness);
			query.setParameter(5, institutionName);
			String id = receiveEntity.getId();
			query.setParameter(6, id);
			query.executeUpdate();
			transaction.commit();
			dis.close();
			return true;
		} else {
		}
		return false;
	}
	
	public boolean updateCompleteReceiveEntity(ReceiveEntity receiveEntity) {
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			System.out.println("this.sessionFactory.getCurrentSession().isOpen()");
			Transaction transaction = session.beginTransaction();
			Query query = session.createQuery("update ReceiveEntity t set t.complete =? where t.id=?");
			query.setParameter(0, true);
			String id = receiveEntity.getId();
			query.setParameter(1, id);
			query.executeUpdate();
			transaction.commit();
			return true;
		} else {
			System.out.println("this.sessionFactory.getCurrentSession().is null");
		}
		return false;
	}
	
	public boolean updateReceiveEntityJPGAxial(ReceiveEntity receiveEntity){
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			Transaction transaction = session.beginTransaction();
			Query query = session
					.createQuery("update ReceiveEntity t set t.jpgAxial =? where t.id=?");
			query.setParameter(0, true);
			String id = receiveEntity.getId();
			query.setParameter(1, id);
			query.executeUpdate();
			transaction.commit();
			return true;
		} else {
			System.out.println("this.sessionFactory.getCurrentSession().is null");
		}
		return false;
	}

	public boolean updateReceiveEntityCompleteTime(ReceiveEntity receiveEntity,int time) {
		Session session = this.sessionFactory.getCurrentSession();
		if(session!=null){
			Transaction transaction = session.beginTransaction();
			Query query = session.createQuery("update ReceiveEntity t set t.completeTime=? where t.id=?");
			query.setParameter(0, time);
			String id = receiveEntity.getId();
			query.setParameter(1, id);
			query.executeUpdate();
			transaction.commit();
			return true;
		}else{
			System.out.println("this.sessionFactory.getCurrentSession().is null");
		}
		return false;
	}
	
	public boolean updateFailedReceiveEntity(ReceiveEntity receiveEntity,
			int failedNum) {
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			Transaction transaction = session.beginTransaction();
			Query query = session
					.createQuery("update ReceiveEntity t set t.failed = ? where t.id=?");
			query.setParameter(0, failedNum);
			String id = receiveEntity.getId();
			query.setParameter(1, id);
			query.executeUpdate();
			transaction.commit();
			return true;
		} else {
			System.out.println("this.sessionFactory.getCurrentSession().is null");
		}
		return false;
	}

	public List<ReceiveEntity> getAllReceiveEntity() {
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			Transaction transaction = session.beginTransaction();
			Query query = session
					.createQuery("select reEntity from ReceiveEntity reEntity order by date desc");
			@SuppressWarnings("unchecked")
			List<ReceiveEntity> list = query.list();
			transaction.commit();
			for (Iterator<ReceiveEntity> iterator = list.iterator(); iterator
					.hasNext();) {
				ReceiveEntity receiveEntity = (ReceiveEntity) iterator.next();
				System.out.println(receiveEntity.getIp() + "  "
						+ receiveEntity.getTotalFiles());
			}

			if (list != null && list.size() > 0) {
				return list;
			}
		} else {
			System.out.println("this.sessionFactory.getCurrentSession().is null");
			return null;
		}

		return null;
	}
	
	public List<ReceiveEntity> getCompleteReceiveEntity() {
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			Transaction transaction = session.beginTransaction();
			Query query = session
					.createQuery("select reEntity from ReceiveEntity reEntity where reEntity.complete=1 or reEntity.jpgAxial=1 order by date desc");
			@SuppressWarnings("unchecked")
			List<ReceiveEntity> list = query.list();
			transaction.commit();
			for (Iterator<ReceiveEntity> iterator = list.iterator(); iterator
					.hasNext();) {
				ReceiveEntity receiveEntity = (ReceiveEntity) iterator.next();
				System.out.println(receiveEntity.getIp() + "  "
						+ receiveEntity.getTotalFiles());
			}

			if (list != null && list.size() > 0) {
				return list;
			}
		} else {
			System.out.println("this.sessionFactory.getCurrentSession().is null");
			return null;
		}

		return null;
	}
	
	public void setSendEntity(String id,String ip,int port,int totalFiles,int sendNum,int failedNum){
		SendEntity sendEntity = new SendEntity();
		Date date = new Date();
		sendEntity.setId(id);//
		sendEntity.setDate(date);//
		sendEntity.setFailed(failedNum);//
		sendEntity.setIp(ip);//
		sendEntity.setPort(port);//
		sendEntity.setSavedFolder(SAVEFOLDER_PATH);//
		sendEntity.setSend(sendNum);//
		sendEntity.setSpeed(0);//
		sendEntity.setTotalFiles(totalFiles);//
		String firstDcmPath = SAVEFOLDER_PATH+"id\\"+"1";
		File file = new File(firstDcmPath);
		
		DicomInputStream dis;
		try {
			dis = new DicomInputStream(file);
			Attributes meta = dis.readDataset(-1, -1);
			String accessionNumber = meta.getString(Tag.AccessionNumber);
			String patientName = meta.getString(Tag.PatientName);
			String studyDate = meta.getString(Tag.StudyDate); 
			sendEntity.setAccessionNumber(accessionNumber);//
			sendEntity.setPatientName(patientName);//
			sendEntity.setStudyDate(studyDate);//
			dis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			Transaction transaction = session.beginTransaction();
			session.save(sendEntity);
			//session.flush();
			transaction.commit();
		} else {
			System.out.println("this.sessionFactory.getCurrentSession().is null");
		}	
	}
	
	public void sendToOther(String id,String ip,int port){
		Client client = Client.create();
		//UUID id = UUID.randomUUID();
		String postString = "http://"+ip+":"+port+"/transfer/rest/remotefile/multipleFiles/"+id;
		WebResource resource = client.resource(postString);
		FormDataMultiPart formDataMultiPart = new FormDataMultiPart();	
		String pathString = SAVEFOLDER_PATH+id;
		int totalFiles = 0;
		int sendNum = 0;
		int failedNum = 0;
		File file = new File(pathString);
		if(!file.isDirectory()){
			FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file",new File(pathString),
					MediaType.APPLICATION_OCTET_STREAM_TYPE);		
			formDataMultiPart.bodyPart(fileDataBodyPart);
		}else{
			File[] files = file.listFiles();
			totalFiles = files.length;
			FileDataBodyPart fileDataBodyPart;
			for(int i=0;i<files.length;i++){
				fileDataBodyPart = new FileDataBodyPart("file",
						new File(pathString+"\\"+files[i].getName()),MediaType.APPLICATION_OCTET_STREAM_TYPE);
				formDataMultiPart.bodyPart(fileDataBodyPart);
			}
			sendNum = totalFiles;
			failedNum = totalFiles - sendNum;
			setSendEntity(id, ip, port, totalFiles, sendNum, failedNum);
			String reString = resource.type(MediaType.MULTIPART_FORM_DATA).post(String.class, formDataMultiPart);
			System.out.println(reString);
		}
	}

	
	public List<SendEntity> getAllSendEntity(){
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {

			Transaction transaction = session.beginTransaction();
			Query query = session.createQuery("select seEntity from SendEntity seEntity order by date desc");
			@SuppressWarnings("unchecked")
			List<SendEntity> list = query.list();
			transaction.commit();
			for (Iterator<SendEntity> iterator = list.iterator(); iterator
					.hasNext();) {
				SendEntity sendEntity = (SendEntity) iterator.next();
				System.out.println(sendEntity.getIp() + "  "
						+ sendEntity.getTotalFiles());
			}
			if (list != null && list.size() > 0) {
				return list;
			}
		} else {
			System.out.println("this.sessionFactory.getCurrentSession().is null");
			return null;
		}
		return null;
	}
	
	
	public List<BmpPathEntity> getAllBmpPath(String path) {
		path = path.replaceAll(",","/");
		File folder = new File(BMPFOLDER_PATH+path);	
		String[] bmpStrings = folder.list();
		List<BmpPathEntity> bmpList = new ArrayList<BmpPathEntity>();
		for(int i=0;i<bmpStrings.length;i++){
			BmpPathEntity bmpPathEntity = new BmpPathEntity();
			bmpPathEntity.setBmpPath(bmpStrings[i]);
			bmpList.add(bmpPathEntity);
		}
		return bmpList;
	}
	
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
	
	public void deleteCompleteData(String id){
		ReceiveEntity receiveEntity = getReceiveEntity(id);

		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			Transaction transaction = session.beginTransaction();
			Query query = session.createQuery("delete ReceiveEntity as r where r.id=?");
			query.setParameter(0, id);
			query.executeUpdate();
			transaction.commit();
		} else {
			System.out.println("this.sessionFactory.getCurrentSession().is null");
		}
		
		if(receiveEntity == null){
			System.out.println("receiveEntity is null!!!");
			return;
		}
		
		String patientName = receiveEntity.getPatientName();
		
		File file1 = new File(BMPFOLDER_PATH+id);
		if(file1.exists()){
			deleteFile(file1);
		}
		File file2 = new File(BMPFOLDER_PATH+patientName+"_"+id+".zip");
		if(file2.exists()){
			deleteFile(file2);
		}
		File file3 = new File(SAVEFOLDER_PATH+patientName+"_"+id+".zip");
		if(file3.exists()){
			deleteFile(file3);
		}
		File file4 = new File(SAVEFOLDER_PATH+id);
		if(file4.exists()){
			deleteFile(file4);
		}
		System.out.println("delete over!!!!");
		
	}
	
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
	
	public void reHandlerSeg(String id) {
		final ReceiveEntity receiveEntity = getReceiveEntity(id);
		handlerSegAndZip(receiveEntity, id);
	}
	
	public void handlerSegAndZip(final ReceiveEntity receiveEntity,String id){
		//分割dcm文件变成三位图
		final String ctPathString = SAVEFOLDER_PATH + id + "\\";
		final String bmpSavePath = BMPFOLDER_PATH +id + "\\";
		
		final String patientName = receiveEntity.getPatientName();
		System.out.println(patientName);
		

		final String bmpResourcePath = BMPFOLDER_PATH+id;
		final String bmpZipPath = BMPFOLDER_PATH+patientName+"_"+id+".zip";
		
		final String morphyBmpResultPathString = bmpSavePath + "axial";
		final String morphyMhdResultFileName = bmpSavePath + "axial.mhd";
		//final String morphyMhdResultFileName2 = bmpSavePath + "axial.zraw";
		final String morphyWholeBmpPath = bmpSavePath + "morphy.bmp";
		
		final String Seg_CTFolderPath = bmpSavePath + "mhdANDzraw";
		
		File mhdANDzrawFolder = new File(Seg_CTFolderPath+File.separator);
		if(!mhdANDzrawFolder.exists()){
			mhdANDzrawFolder.mkdirs();
		}
		
		final String CTmhdPath = Seg_CTFolderPath + File.separator + "ct.mhd";
		final String CTmhdPath2 = Seg_CTFolderPath + File.separator + "ct.zraw";
		final String vtkExePath = "D:\\1049664802\\FileRecv\\Debug";
		new Thread(
				new Runnable() {
					public void run() {
						
						Date startDate = new Date();
						System.out.println("segStart........................................................................");
						algorithmDAOImp.dicomSegmentation(ctPathString, bmpSavePath);
						updateCompleteReceiveEntity(receiveEntity);
						Date overDate = new Date();
						int completeTime = (int) (overDate.getTime()-startDate.getTime());
						updateReceiveEntityCompleteTime(receiveEntity, completeTime);
						System.out.println("segOver..........................................................................");
						ZipUtils.createZip(bmpResourcePath,bmpZipPath);
						
						Runtime rn = Runtime.getRuntime();
						String commandString = "cmd /c VTKTest.exe "
								+ morphyBmpResultPathString + " "
								+ morphyMhdResultFileName + " "
								+ receiveEntity.getPixelSpacing() + " "
								+ receiveEntity.getSliceThickness() + " "
								+ morphyWholeBmpPath + " "
								+ ctPathString + " "
								+ CTmhdPath;
						System.out.println("-------------" + commandString);
						try {
							Process p = rn.exec(commandString, null, new File(vtkExePath));
							System.out.println(p);
							while(new File(CTmhdPath2).exists() == false){
								//System.out.println("waiting for generate mhd file.........");
							}
							
							ZipUtils.createZip(Seg_CTFolderPath, bmpSavePath+File.separator+patientName+"_CT_mhdANDzraw.zip");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					}
				
		).start();
		
	}
	
	public String downloadDCM(String id) {
		ReceiveEntity receiveEntity = getReceiveEntity(id);
		String patientname = receiveEntity.getPatientName();
		//String pathString = SAVEFOLDER_PATH+patientname+"_"+id+".zip";
		String pathString = SAVEFOLDER_PATH+patientname+"_"+id+".zip";
		return pathString;
	}
	
	public String downloadSeg(String id) {
		ReceiveEntity receiveEntity = getReceiveEntity(id);
		String patientname = receiveEntity.getPatientName();
		//String pathString = SAVEFOLDER_PATH+patientname+"_"+id+".zip";
		String pathString = BMPFOLDER_PATH+patientname+"_"+id+".zip";
		return pathString;
	}
	
	public String downloadCTmhd(String id) {
		ReceiveEntity receiveEntity = getReceiveEntity(id);
		String patientname = receiveEntity.getPatientName();
		//String pathString = SAVEFOLDER_PATH+patientname+"_"+id+".zip";
		String pathString = BMPFOLDER_PATH+id+File.separator+patientname+"_CT_mhdANDzraw.zip";
		return pathString;
	}
	
	public String downloadLeakingData(String id) {
		ReceiveEntity receiveEntity = getReceiveEntity(id);
		String leakingFileName = receiveEntity.getLeakingFileName();
		//String pathString = SAVEFOLDER_PATH+patientname+"_"+id+".zip";
		String pathString = BMPFOLDER_PATH+id+File.separator+leakingFileName;
		return pathString;
	}
	
	public void uploadLeakingData(FormDataMultiPart formParams,HttpServletRequest request, String cid) {
		logger.info("uploadLeakingData");

		
		String remoteHostString = "";
		if (request.getHeader("x-forwarded-for") == null) {
			remoteHostString = request.getRemoteAddr();//获取上传者的IP地址
		} else {
			remoteHostString = request.getHeader("x-forwarded-for");
		}
		System.out.println("ip : " + remoteHostString);
		
		SAVEFOLDER_PATH = new File(request.getServletContext().getRealPath("/WEB-INF")).getParent()+"\\testFile\\";
		BMPFOLDER_PATH = new File(request.getServletContext().getRealPath("/WEB-INF")).getParent()+"\\allBmps\\";
		
		Map<String, List<FormDataBodyPart>> fieldsByName = formParams.getFields();
		for (List<FormDataBodyPart> fields : fieldsByName.values()) {
			String leakingDataName = fields.get(0).getContentDisposition().getFileName();
			updateReceiveEntityLeakingFileName(leakingDataName, cid);
			for (FormDataBodyPart field : fields) {
				InputStream is = field.getEntityAs(InputStream.class);
				OutputStream outpuStream = null;
				//创建保存路径
				File file = new File(BMPFOLDER_PATH + cid + "\\");
				if (!file.exists() && !file.isDirectory()) {
					file.mkdirs();
				}
				//保存文件路径
				String filePath = BMPFOLDER_PATH + cid + File.separator +leakingDataName;
				try {
					int read = 0;
					byte[] bytes = new byte[1024];
					outpuStream = new FileOutputStream(new File(filePath));
					while ((read = is.read(bytes)) != -1) {
						outpuStream.write(bytes, 0, read);
					}
					outpuStream.flush();
					outpuStream.close();
					is.close();
				} catch (IOException iox) {
					iox.printStackTrace();
				} finally {
					if (outpuStream != null) {
						try {
							outpuStream.close();
						} catch (Exception ex) {
				}}}
			}
			
		}
	}

	public boolean updateReceiveEntityLeakingFileName(String name,String id){
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			Transaction transaction = session.beginTransaction();
			Query query = session.createQuery("update ReceiveEntity t set t.leakingFileName = ? where t.id=?");
			query.setParameter(0, name);
			query.setParameter(1, id);
			query.executeUpdate();
			transaction.commit();
			return true;
		} else {
			System.out.println("this.sessionFactory.getCurrentSession().is null");
		}
		return false;
	}
	
	public void test() {	
		SendEntity sendEntity = new SendEntity();
		Date date = new Date();
		sendEntity.setId("123457");
		sendEntity.setDate(date);
		sendEntity.setFailed(5);
		sendEntity.setIp("127.0.0.1");
		sendEntity.setMessage("Hello test!");
		sendEntity.setPort(5463);
		sendEntity.setSavedFolder("D:\\");
		sendEntity.setSend(500);
		sendEntity.setSpeed(100);
		sendEntity.setTotalFiles(505);
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			Transaction transaction = session.beginTransaction();
			session.save(sendEntity);
			//session.flush();
			transaction.commit();
		} else {
			System.out.println("this.sessionFactory.getCurrentSession().is null");
		}

	}

}
