package com.lungcare.dicomfile.dao.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	//private static final String SAVEFOLDER_PATH = "G:\\wjlProgramFiles\\local-test-data\\testFile\\";
	private static  String BMPFOLDER_PATH = new File("").getAbsolutePath() +"/src/main/webapp/allBmps/";
	//private static final String BMPFOLDER_PATH = "G:\\wjlProgramFiles\\local-test-data\\allBmps\\";
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
		
		SAVEFOLDER_PATH = new File(request.getServletContext().getRealPath("/WEB-INF")).getParent()+"//testFile/";
		BMPFOLDER_PATH = new File(request.getServletContext().getRealPath("/WEB-INF")).getParent()+"//allBmps/";
		
		int port = request.getRemotePort();
		receiveEntity.setPort(port);//设置port
		
		receiveEntity.setCompleteTime(0);
		
		receiveEntity.setComplete(false);//设置complete
		receiveEntity.setJpgAxial(false);//设置jpgAxial
		receiveEntity.setBmpAxial(false);//设置bmpAxial
		receiveEntity.setBmpCoronal(false);//设置bmpCoronal
		receiveEntity.setBmpSagittal(false);//设置bmpSagittal
		
//		receiveEntity.setTotalFiles(formParams.getFields().values().size());//设置totalfiles
		
		receiveEntity.setFailed(0);//设置failed
		
		receiveEntity.setReceived(0);//设置received
		
		receiveEntity.setSpeed(0);//设置speed
		
		Map<String, List<FormDataBodyPart>> fieldsByName = formParams.getFields();
		int totalNum = 0;
		for (List<FormDataBodyPart> fields : fieldsByName.values()) {
			for(int i=0;i<fields.size();i++){
				totalNum++;
			}
		}
		receiveEntity.setTotalFiles(totalNum);//设置totalfiles
		receiveEntity.setDate(new Date());//设置时间
		receiveEntity.setSavedFolder(SAVEFOLDER_PATH + cid + "\\");//设置接受路径
		addReceiveEntity(receiveEntity);
		File saveFolder = new File(SAVEFOLDER_PATH);
		if(!saveFolder.exists()){
			saveFolder.mkdirs();
		}
		// Usually each value in fieldsByName will be a list of length 1.
		// Assuming each field in the form is a file, just loop through them.
		
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
		new Thread(
				new Runnable() {
					public void run() {
						dcmToJPEG(getPath, savePath);
						updateReceiveEntityJPGAxial(receiveEntity);
						System.out.println("dcm to jpg over!");
					}
				}
		).start();
		
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		//sendToOther(cid, SEND_IP, SEND_PORT);
		
		//分割dcm文件变成三位图
		final String ctPathString = SAVEFOLDER_PATH + cid + "\\";
		final String bmpSavePath = BMPFOLDER_PATH +cid + "\\";
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
						System.out.println(SAVEFOLDER_PATH);
					}
				}
		).start();
		
		String patientName = receiveEntity.getPatientName();
		System.out.println(patientName);
		//压缩接收文件和处理后图片
		ZipUtils.createZip(SAVEFOLDER_PATH+cid, SAVEFOLDER_PATH+patientName+"_"+cid+".zip");
		ZipUtils.createZip(BMPFOLDER_PATH+cid, BMPFOLDER_PATH+patientName+"_"+cid+".zip");
	}
		
	public byte[] downloadFile(HttpServletRequest req){
		logger.info("downloadFile");
			return null;
	}

	public ReceiveEntity getReceiveEntity(String id) {
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			Transaction transaction = session.beginTransaction();
			Query query = session.createQuery("from ReceiveEntity t where t.id=?");
			query.setString(0, id);
			@SuppressWarnings("unchecked")
			List<ReceiveEntity> list = query.list();
			transaction.commit();
			for (Iterator<ReceiveEntity> iterator = list.iterator(); iterator.hasNext();) {
				ReceiveEntity receiveEntity = (ReceiveEntity) iterator.next();
				System.out.println(receiveEntity.getIp() + "  " + receiveEntity.getTotalFiles());
			}
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		} else {
			System.out.println("this.sessionFactory.getCurrentSession().is null");
			return null;
		}
		return null;
	}

	
	private boolean saveFile(InputStream fis, FormDataContentDisposition fdcd,
			int index, String folder) {
		Date date1 = new Date();
		OutputStream outpuStream = null;
//		String fileName = fdcd.getFileName();
//		int index = fileName.indexOf("/");
//		if (index != -1) {
//			fileName = fileName.substring(index + 1, fileName.length());
//		}
//		System.out.println("File Name: " + fileName);
//		System.out.println(folder);
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
			Transaction transaction = session.beginTransaction();
			Query query = session.createQuery("update ReceiveEntity t set t.accessionNumber =?, " +
					"t.patientName=?, t.studyDate=? where t.id=?");
			query.setParameter(0, accessionNumber);
			query.setParameter(1, patientName);
			query.setParameter(2, studyDate);
			String id = receiveEntity.getId();
			query.setParameter(3, id);
			query.executeUpdate();
			transaction.commit();
			return true;
		} else {
		}
		return false;
	}
	
	public boolean updateCompleteReceiveEntity(ReceiveEntity receiveEntity) {
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			System.out
					.println("this.sessionFactory.getCurrentSession().isOpen()");
			Transaction transaction = session.beginTransaction();
			Query query = session
					.createQuery("update ReceiveEntity t set t.complete =? where t.id=?");
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
		path = path.replace(",","/");
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
		File folderFile = new File(getPath);
		String[] filesName = folderFile.list();
		File dcmFolder = new File(savePath);
		if(!dcmFolder.exists()){
			dcmFolder.mkdirs();
		}
		for(int i=0;i<filesName.length;i++){
			File tempFile = new File(getPath+"\\"+filesName[i]);
			try {
				BufferedImage myJpegImage = null;
				Iterator<ImageReader> iter = ImageIO
						. getImageReadersByFormatName("DICOM");
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
				
				int tempInt = Integer.parseInt(filesName[i]);
				DecimalFormat df = new DecimalFormat("000");
				String dstName = savePath+"\\"+df.format(tempInt)+".jpg";//例如把名字0改为001
				
//				File myJpegFile = new File(savePath+"\\"+df.format(tempInt)+".jpg");			
				
				
//				OutputStream output = new BufferedOutputStream(new FileOutputStream(myJpegFile));
//				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
//				encoder.encode(myJpegImage);
//				output.close();
				
				String formatName = dstName.substring(dstName.lastIndexOf(".") + 1);  
				//FileOutputStream out = new FileOutputStream(dstName);  
				//JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
				//encoder.encode(dstImage);  
				ImageIO.write(myJpegImage, formatName , new File(dstName)); 
			} catch (IOException e) {
				System. out.println("\nError: couldn't read dicom image!"
						+ e.getMessage());
				return;
			}
			
		}
		
	}
	
	public void deleteCompleteData(String id){
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
		
		ReceiveEntity receiveEntity = getReceiveEntity(id);
		String patientName = receiveEntity.getPatientName();
		
		File file1 = new File(BMPFOLDER_PATH+id);
		if(file1.exists()){
			deleteFile(file1);
		}
		File file2 = new File(patientName+"_"+BMPFOLDER_PATH+id+".zip");
		if(file2.exists()){
			deleteFile(file2);
		}
		File file3 = new File(patientName+"_"+SAVEFOLDER_PATH+id+".zip");
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
