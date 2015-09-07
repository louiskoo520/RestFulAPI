package com.lungcare.dicomfile.restful;



import java.io.File;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lungcare.dicomfile.entity.BmpPathEntity;
import com.lungcare.dicomfile.entity.ReceiveEntity;
import com.lungcare.dicomfile.entity.SendEntity;
import com.lungcare.dicomfile.service.IReceiveEntityService;
import com.lungcare.dicomfile.service.IRemoteFileService;
import com.lungcare.dicomfile.service.ISendEntityService;
import com.sun.jersey.multipart.FormDataMultiPart;

@Path("remotefile")
@Component
public class RemoteFileTransferResource {

	@Autowired
	private IRemoteFileService remoteFileService;
	@Autowired
	private IReceiveEntityService receiveEntityService;
	@Autowired
	private ISendEntityService sendEntityService;
	
	
	
	
	
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
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("text/plain")
	@Path("/singleFile/{id}")
	public void uploadSingleFiles(FormDataMultiPart formParams,@Context HttpServletRequest request, @PathParam("id") String cid) {
		remoteFileService.uploadSingleFile(formParams, request , cid);	
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("text/plain")
	@Path("/uploadLeakingData/{id}")
	public void uploadLeakingData(FormDataMultiPart formParams,@Context HttpServletRequest request, @PathParam("id") String cid) {
		remoteFileService.uploadLeakingData(formParams, request, cid);
	}
	
	
	
    @GET
    @Path("/downloadDCM/{id}")
    @Produces(MediaType.MULTIPART_FORM_DATA)
	public Response downloadDCM(@PathParam("id") String id){
        String pathString = remoteFileService.downloadDCM(id);
    	File download = new File(pathString);
    	if(download.exists()){
    		String fileName = pathString.substring(pathString.lastIndexOf(File.separator)+1);
    		System.out.println(download.length());
    		ResponseBuilder response = Response.ok((Object)download);
    		response.header("Content-Disposition", "attachment; filename="+fileName);
    		return response.build();
    	}
    	return null;
	}
    
    @GET
    @Path("/downloadSeg/{id}")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public Response downloadSeg(@PathParam("id") String id){
    	String pathString = remoteFileService.downloadSeg(id);
    	File download = new File(pathString);
    	if(download.exists()){
    		String fileName = pathString.substring(pathString.lastIndexOf("//")+1);
    		fileName = fileName.substring(fileName.lastIndexOf("/")+1);
    		System.out.println(download.length());
    		ResponseBuilder response = Response.ok((Object)download);
    		response.header("Content-Disposition", "attachment; filename="+fileName);
    		return response.build();
    	}
    	return null;
    }
    
    @GET
    @Path("/downloadCTmhd/{id}")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public Response downloadCTmhd(@PathParam("id") String id,@Context HttpServletRequest request){
    	String pathString = remoteFileService.downloadCTmhd(id);
    	File downloadFile = new File(pathString);
    	if(downloadFile.exists()){
    		String fileName = pathString.substring(pathString.lastIndexOf(File.separator)+1);
    		System.out.println(downloadFile.length());
    		ResponseBuilder response = Response.ok((Object)downloadFile);
    		response.header("Content-Disposition", "attachment; filename="+fileName);
    		return response.build();
    	}
    	return null;
    }
    
    @GET
    @Path("/downloadLeakingData/{id}")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public Response downloadLeakingData(@PathParam("id") String id,@Context HttpServletRequest request){
    	String pathString = remoteFileService.downloadLeakingData(id);
    	File downloadFile = new File(pathString);
    	if(downloadFile.exists()){
    		String fileName = pathString.substring(pathString.lastIndexOf(File.separator)+1);
    		System.out.println(downloadFile.length());
    		ResponseBuilder response = Response.ok((Object)downloadFile);
    		response.header("Content-Disposition", "attachment; filename="+fileName);
    		return response.build();
    	}
    	return null;
    }
    
  
	@GET
	@Path("getReceiveEntity/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ReceiveEntity GetRecieiveEntity(@PathParam("id") String id) {
		return receiveEntityService.getReceiveEntity(id);
	}
	
	
	@GET
	@Path("/getAllReceiveEntity")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<ReceiveEntity> GetAllReceiveEntity(){
		return  receiveEntityService.getAllReceiveEntity();
	}
	
	
	@GET
	@Path("/getCompleteReceiveEntity")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<ReceiveEntity> GetCompleteReceiveEntity(){
		return  receiveEntityService.getCompleteReceiveEntity();
	}
	
	@POST
	@Path("deleteCompleteData/{id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("text/plain")
	public void deleteCompleteData(@PathParam("id") String cid) {
		remoteFileService.deleteCompleteData(cid);
	}
	
	@POST
	@Path("reHandlerSeg/{id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("text/plain")
	public void reHandleSeg(@PathParam("id") String id){
		remoteFileService.reHandlerSeg(id);
	}
	
	@GET
	@Path("/getAllSendEntity")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<SendEntity> getAllSendEntity(){
		return  sendEntityService.getAllSendEntity();
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
