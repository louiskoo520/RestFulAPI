package com.lungcare.dicomfile.dao.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.transaction.annotation.Transactional;

import com.lungcare.dicomfile.dao.ISendDAO;
import com.lungcare.dicomfile.entity.SendEntity;

@Transactional
public class SendDAOImp implements ISendDAO {
	
	private static String SAVEFOLDER_PATH = new File("").getAbsolutePath() +"/src/main/webapp/testFile/";
//	private static String BMPFOLDER_PATH = new File("").getAbsolutePath() +"/src/main/webapp/allBmps/";
	
	private Logger logger = Logger.getLogger(SendDAOImp.class);
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
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
			logger.error("Could not read some message from dcm file", e);
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
	
	
	
	
}
