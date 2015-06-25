package com.lungcare.dicomfile.dao.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.transaction.annotation.Transactional;

import com.lungcare.dicomfile.dao.IRemoteFileTransferDAO;
import com.lungcare.dicomfile.entity.ReceiveEntity;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

@Transactional
public class RemoteFileTransferDAOImp implements IRemoteFileTransferDAO {
	private static Logger logger = Logger
			.getLogger(RemoteFileTransferDAOImp.class);

	private static final String FOLDER_PATH = "C:\\my_files\\";

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void uploadFile(FormDataMultiPart formParams,
			ReceiveEntity receiveEntity) {
		// TODO Auto-generated method stub
		logger.info("uploadFile");

		System.out.println("RemoteFileTransferDAOImp uploadFile.........");
		Map<String, List<FormDataBodyPart>> fieldsByName = formParams
				.getFields();
		int totalNum = 0;
		for (List<FormDataBodyPart> fields : fieldsByName.values()) {
			for (FormDataBodyPart field : fields) {
				++totalNum;
			}
		}
		receiveEntity.setTotalFiles(totalNum);
		receiveEntity.setSavedFolder(FOLDER_PATH);
		addReceiveEntity(receiveEntity);
		// Usually each value in fieldsByName will be a list of length 1.
		// Assuming each field in the form is a file, just loop through them.
		int index = 1;
		int failedNum = 0;
		for (List<FormDataBodyPart> fields : fieldsByName.values()) {
			for (FormDataBodyPart field : fields) {
				InputStream is = field.getEntityAs(InputStream.class);
				String fileName = field.getName();
				FormDataContentDisposition fdcd = field
						.getFormDataContentDisposition();
				if (!saveFile(is, fdcd, fileName)) {
					updateFailedReceiveEntity(receiveEntity, failedNum);
				}
				updateReceiveEntity(receiveEntity, index);

				// TODO: SAVE FILE HERE

				// if you want media type for validation, it's
				// field.getMediaType()
				++index;
			}
		}

	}

	public void downloadFile() {
		// TODO Auto-generated method stub
		logger.info("downloadFile");
	}

	public ReceiveEntity getReceiveEntity(String id) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			System.out
					.println("this.sessionFactory.getCurrentSession().isOpen()");
			Transaction transaction = session.beginTransaction();
			Query query = session
					.createQuery("from ReceiveEntity t where t.id=?");
			query.setString(0, id);
			List<ReceiveEntity> list = query.list();
			transaction.commit();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				ReceiveEntity receiveEntity = (ReceiveEntity) iterator.next();
				System.out.println(receiveEntity.getIp() + "  "
						+ receiveEntity.getTotalFiles());
			}
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		} else {
			System.out
					.println("this.sessionFactory.getCurrentSession().is null");
			return null;
		}

		return null;
	}

	public void test() {
		// TODO Auto-generated method stub
		ReceiveEntity receiveEntity = new ReceiveEntity();
		receiveEntity.setId("dsadasda");
		receiveEntity.setIp("321312312");
		System.out.println("RemoteFileTransferDAOImp test");
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			System.out
					.println("this.sessionFactory.getCurrentSession().isOpen()");
			Transaction transaction = session.beginTransaction();
			session.save(receiveEntity);
			transaction.commit();

		} else {
			System.out
					.println("this.sessionFactory.getCurrentSession().is null");
		}

	}

	private boolean saveFile(InputStream fis, FormDataContentDisposition fdcd,
			String name) {
		SimpleDateFormat myFormatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date1 = new Date();
		OutputStream outpuStream = null;
		String fileName = fdcd.getFileName();
		int index = fileName.indexOf("/");
		if (index != -1) {
			fileName = fileName.substring(index + 1, fileName.length());
		}
		System.out.println("File Name: " + fileName);
		File file = new File(FOLDER_PATH);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdir();
		}
		String filePath = FOLDER_PATH + fileName;

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
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			System.out
					.println("this.sessionFactory.getCurrentSession().isOpen()");
			Transaction transaction = session.beginTransaction();
			session.save(receiveEntity);
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
		// TODO Auto-generated method stub
		// ReceiveEntity receive = getReceiveEntity(receiveEntity.getIp());
		// receive.setReceived(receivedNum);
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			System.out
					.println("this.sessionFactory.getCurrentSession().isOpen()");
			Transaction transaction = session.beginTransaction();
			Query query = session
					.createQuery("update ReceiveEntity t set t.received = ? where t.id=?");
			query.setParameter(0, receivedNum);
			String id = receiveEntity.getId();
			query.setParameter(1, id);
			int result = query.executeUpdate();
			// session.update(receive);
			transaction.commit();
			return true;
		} else {
			System.out
					.println("this.sessionFactory.getCurrentSession().is null");

		}

		return false;

	}

	public boolean updateFailedReceiveEntity(ReceiveEntity receiveEntity,
			int failedNum) {
		// TODO Auto-generated method stub
		// ReceiveEntity receive = getReceiveEntity(receiveEntity.getIp());
		// receive.setReceived(receivedNum);
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			System.out
					.println("this.sessionFactory.getCurrentSession().isOpen()");
			Transaction transaction = session.beginTransaction();
			Query query = session
					.createQuery("update ReceiveEntity t set t.failed = ? where t.id=?");
			query.setParameter(0, failedNum);
			String id = receiveEntity.getId();
			query.setParameter(1, id);
			int result = query.executeUpdate();
			// session.update(receive);
			transaction.commit();
			return true;
		} else {
			System.out
					.println("this.sessionFactory.getCurrentSession().is null");

		}

		return false;

	}
}
