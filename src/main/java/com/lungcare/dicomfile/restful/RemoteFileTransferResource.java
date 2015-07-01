package com.lungcare.dicomfile.restful;



import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.ws.WebServiceContext;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import antlr.StringUtils;

import com.lungcare.dicomfile.entity.ReceiveEntity;
import com.lungcare.dicomfile.service.IRemoteFileService;
import com.lungcare.dicomfile.util.ZipUtils;
import com.sun.jersey.multipart.FormDataMultiPart;

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
	@Resource
	private WebServiceContext wsCtxt;

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("text/plain")
	@Path("/multipleFiles/{id}")
	public String uploadMultiFiles(FormDataMultiPart formParams,
			@Context HttpServletRequest request, @PathParam("id") String cid) {
		System.out.println("uploadMultiFiles");

		ReceiveEntity receiveEntity = new ReceiveEntity();
		MultivaluedMap<String, String> mapHeaders = formParams.getHeaders();
		receiveEntity.setId(cid);

		String remoteHostString = "";
		if (request.getHeader("x-forwarded-for") == null) {
			remoteHostString = request.getRemoteAddr();
		} else {
			remoteHostString = request.getHeader("x-forwarded-for");
		}
		System.out.println("ip : " + remoteHostString);

		receiveEntity.setIp(remoteHostString);

		int port = request.getRemotePort();

		receiveEntity.setPort(port);

		receiveEntity.setTotalFiles(formParams.getFields().values().size());
		receiveEntity.setFailed(0);
		receiveEntity.setReceived(0);
		receiveEntity.setSpeed(0);
		receivedMap.put(receiveEntity.getId(), receiveEntity);
		remoteFileService.uploadFile(formParams, receiveEntity);
		return SUCCESS_RESPONSE;
	}
	
	@GET
	@Path("/downloadZip")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<String> downloadZip() throws JSONException{
		//String pathString = RemoteFileTransferResource.class.getClass().getResource("/").getPath().replace("target/classes/", "src/main/webapp/testFile/");
		String pathString=new File("").getAbsolutePath();
		//String pathString ="G:/wjlProgramFiles/local-git-repository/src/main/webapp/testFile/";
		pathString += "/src/main/webapp/testFile/";
		//ZipUtils.createZip(pathString+cid, pathString+cid+".zip");
		List<String> zipList = getAllZip(pathString);
		return zipList;
	}
	
	public List<String> getAllZip(String path) {
		File file = new File(path);
		String test[];
		List<String> zipList = new ArrayList<String>(); 
		test = file.list();
		for (int i = 0; i < test.length; i++) {
			File testFile = new File(path+test[i]);
			if(!testFile.isDirectory()){
				zipList.add(test[i]);
			}
		}
		return zipList;
	}
	
	@GET
	@Path("/download")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public byte[] download(@Context HttpServletRequest req)throws Exception {
		return remoteFileService.downloadFile(req);
	}

	@GET
	@Path("add")
	@Produces("text/html")
	@Consumes("application/xml")
	public String AddCustomers() {
		return "Customer added with Id ";
	}

	@GET
	@Path("get/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ReceiveEntity GetRecieiveEntity(@PathParam("id") String cid) {
		return remoteFileService.getReceiveEntity(cid);
	}

	@GET
	@Path("/getAllReceiveEntity")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<ReceiveEntity> GetAllReceiveEntity(){
		return  remoteFileService.GetAllReceiveEntity();
	}
}
