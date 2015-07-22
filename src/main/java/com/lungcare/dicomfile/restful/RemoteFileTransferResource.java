package com.lungcare.dicomfile.restful;



import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lungcare.dicomfile.entity.BmpPathEntity;
import com.lungcare.dicomfile.entity.ReceiveEntity;
import com.lungcare.dicomfile.entity.SendEntity;
import com.lungcare.dicomfile.service.IRemoteFileService;
import com.sun.jersey.multipart.FormDataMultiPart;

@Path("remotefile")
@Component
public class RemoteFileTransferResource {
	public RemoteFileTransferResource() {

	}

	//private static final String SUCCESS_RESPONSE = "Successful";

	@Autowired
	private IRemoteFileService remoteFileService;
	//private Map<String, ReceiveEntity> receivedMap = new HashMap<String, ReceiveEntity>();
	
//	@Resource
//	private WebServiceContext wsCtxt;

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("text/plain")
	@Path("/multipleFiles/{id}")
	public void uploadMultiFiles(FormDataMultiPart formParams,
			@Context HttpServletRequest request, @PathParam("id") String cid) {
		remoteFileService.uploadFile(formParams, request , cid);	
	}

	@GET
	@Path("add")
	@Produces("text/html")
	@Consumes("application/xml")
	public String AddCustomers() {
		return "Customer added with Id ";
	}

	@GET
	@Path("getReceiveEntity/{id}")
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
		return  remoteFileService.getAllReceiveEntity();
	}
	
	@GET
	@Path("/getCompleteReceiveEntity")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<ReceiveEntity> GetCompleteReceiveEntity(){
		return  remoteFileService.getCompleteReceiveEntity();
	}
	
	@POST
	@Path("deleteCompleteData/{id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("text/plain")
	public void deleteCompleteData(@PathParam("id") String cid) {
		remoteFileService.deleteCompleteData(cid);
	}
	
	@GET
	@Path("/getAllSendEntity")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<SendEntity> getAllSendEntity(){
		return  remoteFileService.getAllSendEntity();
	}
	
	@GET
	@Path("/getAllBmpPath/{path}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<BmpPathEntity> getAllBmpPath(@PathParam("path") String path){
		return  remoteFileService.getAllBmpPath(path);
	}
	
	@GET
	@Path("/test")
	@Produces("text/html")
	@Consumes("application/xml")
	public String doTest() {
		remoteFileService.test();
		return "test success!!!";
	}
}
