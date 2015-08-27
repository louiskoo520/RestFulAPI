package com.lungcare.dicomfile.dao.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.lungcare.dicomfile.dao.IAlgorithmDAO;
import com.lungcare.dicomfile.dao.IDicomFileDAO;
import com.lungcare.dicomfile.dao.IFileDAO;
import com.lungcare.dicomfile.dao.IReceiveEntityDAO;
import com.lungcare.dicomfile.dao.IRemoteFileTransferDAO;
import com.lungcare.dicomfile.entity.BmpPathEntity;
import com.lungcare.dicomfile.entity.ReceiveEntity;
import com.lungcare.dicomfile.util.ZipUtils;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

@Transactional
public class RemoteFileTransferDAOImp implements IRemoteFileTransferDAO {

	private static Logger logger = Logger.getLogger(RemoteFileTransferDAOImp.class);
	private static  String SAVEFOLDER_PATH = new File("").getAbsolutePath() +"/src/main/webapp/testFile/";
	private static  String BMPFOLDER_PATH = new File("").getAbsolutePath() +"/src/main/webapp/allBmps/";
//	private SessionFactory sessionFactory;
	
	@Autowired
	private IAlgorithmDAO algorithmDAOImp;
	@Autowired
	private IReceiveEntityDAO receiveEntityDAOImp;
	@Autowired
	private IFileDAO fileDAOImp;
	@Autowired
	private IDicomFileDAO dicomFileDAOImp;
	
//	public void setSessionFactory(SessionFactory sessionFactory) {
//		this.sessionFactory = sessionFactory;
//	}

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
		
		receiveEntityDAOImp.addReceiveEntity(receiveEntity);
		
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
				if (!fileDAOImp.saveFile(SAVEFOLDER_PATH,is, fdcd, index, cid)) {
					failedNum+=1;
					receiveEntityDAOImp.updateFailedReceiveEntity(receiveEntity, failedNum);//更新failed
				}
				receiveEntityDAOImp.updateReceiveEntity(receiveEntity, index);//更新received
				
					try {
						if(is!=null){
							is.close();
						}
					} catch (IOException e) {
						logger.error("Could not close stream", e);
					}
				
				++index;
			}
		}
		
		//添加病人信息
		String firstDCMPath = SAVEFOLDER_PATH+cid+"\\1";
		receiveEntityDAOImp.updateReceiveEntity(receiveEntity, firstDCMPath);
		
		//将dcm文件转化成jpeg
		final String getPath = SAVEFOLDER_PATH + cid;
		final String savePath = BMPFOLDER_PATH + cid + "\\dcm\\axial";
	
		final String ctResourcePath =  SAVEFOLDER_PATH+cid;
		final String ctZipPath = SAVEFOLDER_PATH+ receiveEntity.getPatientName()+"_"+cid+".zip";
		new Thread(
				new Runnable() {
					public void run() {
						dicomFileDAOImp.dcmToJPEG(getPath, savePath);
						receiveEntityDAOImp.updateReceiveEntityJPGAxial(receiveEntity);
						System.out.println("dcm to jpg over!");
						ZipUtils.createZip(ctResourcePath, ctZipPath);
					}
				}
		).start();

		
		//分割dcm文件变成三位图并压缩
		handlerSegAndZip(receiveEntity, cid);
		
		
	}
	
	
	public void uploadZipFile(FormDataMultiPart formParams,HttpServletRequest request, final String cid) {
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
					logger.error("Could not save zip file", iox);
				} finally {
					if (outpuStream != null) {
						try {
							outpuStream.close();
						} catch (Exception ex) {
							logger.error("Could not close outputStream", ex);
				}}}
			}
			
			String zipFolderName = "zip_"+cid;
			
			fileDAOImp.ExtractByHaoZip(SAVEFOLDER_PATH, zipName, zipFolderName);
			
			//删除上传的压缩文件
			if(new File(SAVEFOLDER_PATH+zipName).exists()){
				new File(SAVEFOLDER_PATH+zipName).delete();
			}
			
			String dcmFolderPath = dicomFileDAOImp.getDCMFolder(SAVEFOLDER_PATH+zipFolderName);
//			ArrayList<String> dcmPaths = getDCMFolder(SAVEFOLDER_PATH+zipFolderName);
//			for(int i=0;i<dcmPaths.size();i++){
//				String dcmFolderPath = dcmPaths.get(i);
				System.out.println("dcmFolderPath:"+dcmFolderPath);
				File oldFolder = new File(dcmFolderPath);
				if(oldFolder.isDirectory()){
					String[] fileStrings = oldFolder.list();
					for(int j = 0;j<fileStrings.length;j++){
						if(dicomFileDAOImp.checkModility(oldFolder+File.separator+fileStrings[j])){
							totalNum++;
							fileDAOImp.copyFile(oldFolder+File.separator+fileStrings[j], SAVEFOLDER_PATH+cid+File.separator+totalNum);
						}
					}
				}
//			}
		}
		
		
		
		fileDAOImp.deleteDir(new File(SAVEFOLDER_PATH+"zip_"+cid));
		
		receiveEntity.setTotalFiles(totalNum);//设置totalfiles
		
		receiveEntityDAOImp.addReceiveEntity(receiveEntity);
		
		receiveEntityDAOImp.updateFailedReceiveEntity(receiveEntity, 0);//更新failed
		receiveEntityDAOImp.updateReceiveEntity(receiveEntity, totalNum);//更新received
					

		
		//添加病人信息
		String firstDCMPath = SAVEFOLDER_PATH+cid+"\\1";
		receiveEntityDAOImp.updateReceiveEntity(receiveEntity, firstDCMPath);
		
		//将dcm文件转化成jpeg
		final String getPath = SAVEFOLDER_PATH + cid;
		final String savePath = BMPFOLDER_PATH + cid + "\\dcm\\axial";
		final String ctResourcePath =  SAVEFOLDER_PATH+cid;
		final String ctZipPath = SAVEFOLDER_PATH+ receiveEntity.getPatientName()+"_"+cid+".zip";
		new Thread(
				new Runnable() {
					public void run() {
						
						dicomFileDAOImp.dcmToJPEG(getPath, savePath);
						receiveEntityDAOImp.updateReceiveEntityJPGAxial(receiveEntity);
						System.out.println("dcm to jpg over!");
						ZipUtils.createZip(ctResourcePath, ctZipPath);
						
					}
				}
				
		).start();
		
		//分割dcm文件变成三位图并压缩
		handlerSegAndZip(receiveEntity, cid);
		
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
	
	
	
	public void deleteCompleteData(String id){
		receiveEntityDAOImp.deleteCompleteData(id, SAVEFOLDER_PATH,BMPFOLDER_PATH);
		
	}
	
	
	public void reHandlerSeg(String id) {
		final ReceiveEntity receiveEntity = receiveEntityDAOImp.getReceiveEntity(id);
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
						receiveEntityDAOImp.updateReceiveEntityCompleteState(receiveEntity);
						Date overDate = new Date();
						int completeTime = (int) (overDate.getTime()-startDate.getTime());
						receiveEntityDAOImp.updateReceiveEntityCompleteTime(receiveEntity, completeTime);
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
							logger.error("some wrong happend to vtkTest.exe", e);
						}
					}

					}
				
		).start();
		
	}
	
	public String downloadDCM(String id) {
		ReceiveEntity receiveEntity = receiveEntityDAOImp.getReceiveEntity(id);
		String patientname = receiveEntity.getPatientName();
		String pathString = SAVEFOLDER_PATH+patientname+"_"+id+".zip";
		return pathString;
	}
	
	public String downloadSeg(String id) {
		ReceiveEntity receiveEntity = receiveEntityDAOImp.getReceiveEntity(id);
		String patientname = receiveEntity.getPatientName();
		String pathString = BMPFOLDER_PATH+patientname+"_"+id+".zip";
		return pathString;
	}
	
	public String downloadCTmhd(String id) {
		ReceiveEntity receiveEntity = receiveEntityDAOImp.getReceiveEntity(id);
		String patientname = receiveEntity.getPatientName();
		String pathString = BMPFOLDER_PATH+id+File.separator+patientname+"_CT_mhdANDzraw.zip";
		return pathString;
	}
	
	public String downloadLeakingData(String id) {
		ReceiveEntity receiveEntity = receiveEntityDAOImp.getReceiveEntity(id);
		String leakingFileName = receiveEntity.getLeakingFileName();
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
			receiveEntityDAOImp.updateReceiveEntityLeakingFileName(leakingDataName, cid);
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
					logger.error("Could not save leakingData", iox);
				} finally {
					if (outpuStream != null) {
						try {
							outpuStream.close();
						} catch (Exception ex) {
							logger.error("Could not close stream", ex);
				}}}
			}
			
		}
	}

	
	public void test() {	
//		SendEntity sendEntity = new SendEntity();
//		Date date = new Date();
//		sendEntity.setId("123457");
//		sendEntity.setDate(date);
//		sendEntity.setFailed(5);
//		sendEntity.setIp("127.0.0.1");
//		sendEntity.setMessage("Hello test!");
//		sendEntity.setPort(5463);
//		sendEntity.setSavedFolder("D:\\");
//		sendEntity.setSend(500);
//		sendEntity.setSpeed(100);
//		sendEntity.setTotalFiles(505);
//		Session session = this.sessionFactory.getCurrentSession();
//		if (session != null) {
//			Transaction transaction = session.beginTransaction();
//			session.save(sendEntity);
//			//session.flush();
//			transaction.commit();
//		} else {
//			System.out.println("this.sessionFactory.getCurrentSession().is null");
//		}

	}

}
