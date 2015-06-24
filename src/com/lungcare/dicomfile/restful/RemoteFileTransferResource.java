package com.lungcare.dicomfile.restful;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lungcare.dicomfile.entity.ReceiveEntity;
import com.lungcare.dicomfile.service.IRemoteFileService;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;

@Path("remotefile")
@Component
public class RemoteFileTransferResource {
	public RemoteFileTransferResource() {

	}

	private static final String SUCCESS_RESPONSE = "Successful";
	private static final String FAILED_RESPONSE = "Failed";

	@Autowired
	private IRemoteFileService remoteFileService;
	private Map<String, ReceiveEntity> receivedMap = new HashMap<String, ReceiveEntity>();

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("text/plain")
	@Path("/multipleFiles")
	public String uploadMultiFiles(FormDataMultiPart formParams,
			HttpServletRequest request) {

		ReceiveEntity receiveEntity = new ReceiveEntity();
		receiveEntity.setId(Integer.toString(request.hashCode()));

		String remoteHostString = request.getRemoteHost();
		receiveEntity.setIp(remoteHostString);
		int port = request.getRemotePort();
		receiveEntity.setPort(port);
		receiveEntity.setTotalFiles(formParams.getFields().size());
		receivedMap.put(receiveEntity.getId(), receiveEntity);
		remoteFileService.uploadFile(formParams, receiveEntity);
		return SUCCESS_RESPONSE;
	}

	private static final String FOLDER_PATH = "C:\\my_files\\";

	@POST
	@Path("/uploadsingle")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public String uploadFile(@FormDataParam("file") InputStream fis,
			@FormDataParam("file") FormDataContentDisposition fdcd) {

		saveFile(fis, fdcd, fdcd.getFileName());
		return "File Upload Successfully !!";
	}

	private Logger logger = Logger.getLogger(CustomersResource.class);

	@POST
	@Path("/upload1single")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public String upload(@FormDataParam("file") InputStream fis,
			@FormDataParam("file") FormDataContentDisposition fdcd) {

		logger.info("CustomersResource upload.");
		// localFileService.uploadFile();
		saveFile(fis, fdcd, fdcd.getFileName());

		// TODO: handle exception

		return "File Upload Successfully !!";
	}

	private void saveFile(InputStream fis, FormDataContentDisposition fdcd,
			String name) {
		SimpleDateFormat myFormatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date1 = new Date();
		OutputStream outpuStream = null;
		String fileName = fdcd.getFileName();
		System.out.println("File Name: " + fdcd.getFileName());
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
