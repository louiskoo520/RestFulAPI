package com.lungcare.dicomfile.dao.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import com.lungcare.dicomfile.dao.IRemoteFileTransferDAO;
import com.lungcare.dicomfile.entity.ReceiveEntity;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

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

		// Usually each value in fieldsByName will be a list of length 1.
		// Assuming each field in the form is a file, just loop through them.
		this.sessionFactory.getCurrentSession().save(receiveEntity);
		for (List<FormDataBodyPart> fields : fieldsByName.values()) {
			for (FormDataBodyPart field : fields) {
				InputStream is = field.getEntityAs(InputStream.class);
				String fileName = field.getName();
				FormDataContentDisposition fdcd = field
						.getFormDataContentDisposition();
				saveFile(is, fdcd, fileName);
				// TODO: SAVE FILE HERE

				// if you want media type for validation, it's
				// field.getMediaType()
			}
		}

	}

	public void downloadFile() {
		// TODO Auto-generated method stub
		logger.info("downloadFile");
	}

	private void saveFile(InputStream fis, FormDataContentDisposition fdcd,
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

	}

}
