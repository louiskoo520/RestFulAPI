package com.lungcare.dicomfile.dao.impl;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.lungcare.dicomfile.dao.IFileDAO;
import com.lungcare.dicomfile.dao.IReceiveEntityDAO;
import com.lungcare.dicomfile.entity.ReceiveEntity;

@Transactional
public class ReceiveEntityDAOImp implements IReceiveEntityDAO {
	
	@Autowired
	private IFileDAO fileDAOImp;
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public boolean addReceiveEntity(ReceiveEntity receiveEntity) {
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			System.out.println("this.sessionFactory.getCurrentSession().isOpen()");
			Transaction transaction = session.beginTransaction();
			session.save(receiveEntity);
			session.flush();
			transaction.commit();
			return true;
		} else {
			System.out.println("this.sessionFactory.getCurrentSession().is null");
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
	
	
	
	public boolean updateReceiveEntity(ReceiveEntity receiveEntity,int receivedNum) {

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
	
	public boolean updateReceiveEntity(ReceiveEntity receiveEntity,String firstDCMString){
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			File file = new File(firstDCMString);
			DicomInputStream dis;
			Attributes meta;
			try {
				dis = new DicomInputStream(file);
				meta = dis.readDataset(-1, -1);
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
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean updateReceiveEntityCompleteState(ReceiveEntity receiveEntity) {
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
	
	public boolean updateFailedReceiveEntity(ReceiveEntity receiveEntity,int failedNum) {
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
	
	public void deleteCompleteData(String id,String saveFolder_path,String bmpFolder_path){
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
		
		File file1 = new File(bmpFolder_path+id);
		if(file1.exists()){
			fileDAOImp.deleteFile(file1);
		}
		File file2 = new File(bmpFolder_path+patientName+"_"+id+".zip");
		if(file2.exists()){
			fileDAOImp.deleteFile(file2);
		}
		File file3 = new File(saveFolder_path+patientName+"_"+id+".zip");
		if(file3.exists()){
			fileDAOImp.deleteFile(file3);
		}
		File file4 = new File(saveFolder_path+id);
		if(file4.exists()){
			fileDAOImp.deleteFile(file4);
		}
		System.out.println("delete over!!!!");
		
	}
	
	
	
	
	
	
}
