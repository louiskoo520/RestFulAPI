package com.lungcare.dicomfile.restful;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lungcare.dicomfile.entity.User;
import com.lungcare.dicomfile.service.IUserService;

@Path("user")
@Component
public class UsersResource {
	
	@Autowired
	private IUserService userEntityService;
	
	
	@POST
	//@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("/addUser")
	public void addUser(
			@FormParam("user_account") String user_account,@FormParam("user_name") String user_name,
			@FormParam("user_password") String user_password,@FormParam("user_age") int user_age,
			@FormParam("gender") int user_gender,@FormParam("user_role") int user_role,
			@FormParam("user_tel") String user_tel,@FormParam("user_address") String user_address){
		System.out.println("user_role:"+user_role);
		userEntityService.addUser(user_account,user_name,user_password,user_age,user_gender,user_role,user_tel,user_address);
	}
	
	
	
	@GET
	@Path("/getAllUsers")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<User> getAllUsers(){
		return userEntityService.getAllUsers();
	}
	
	
	
	
	
	
	
	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void test(){
		userEntityService.test();
	}
	
	
	
}
