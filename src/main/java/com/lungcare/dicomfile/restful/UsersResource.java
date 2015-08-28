package com.lungcare.dicomfile.restful;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lungcare.dicomfile.service.IUserEntityService;

@Path("user")
@Component
public class UsersResource {
	
	@Autowired
	private IUserEntityService userEntityService;
	
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("text/plain")
	@Path("/addUser")
	public void addUser(){
		userEntityService.addUser();
	}
	
	
	@GET
	@Path("/getAllUsers")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<?> getAllUsers(){
		return userEntityService.getAllUsers();
	}
	
}
