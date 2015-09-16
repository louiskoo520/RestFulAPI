package com.lungcare.dicomfile.restful;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lungcare.dicomfile.entity.Login;
import com.lungcare.dicomfile.entity.User;
import com.lungcare.dicomfile.service.IUserService;

@Path("user")
@Component
public class UsersResource {

	@Autowired
	private IUserService userEntityService;

	@POST
	// @Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("text/plain")
	@Path("/login")
	public String login(@Context HttpServletRequest request,
			@FormParam("user_account") String user_account,
			@FormParam("user_password") String user_password) {
		return userEntityService.login(request, user_account, user_password);
	}

	@POST
	// @Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("text/plain")
	@Path("/register")
	public String register(@FormParam("username") String user_account,
			@FormParam("password") String user_password) {
		return userEntityService.register(user_account, user_password);
	}

	@POST
	// @Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("text/plain")
	@Path("/checkusername")
	public String checkUserName(@FormParam("username") String user_account) {
		return userEntityService.checkUserName(user_account);
	}

	@POST
	// @Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/addUser")
	public void addUser(@FormParam("user_account") String user_account,
			@FormParam("user_realname") String user_realname,
			@FormParam("user_password") String user_password,
			@FormParam("user_age") int user_age,
			@FormParam("gender") int user_gender,
			@FormParam("roleId") int user_role,
			@FormParam("user_tel") String user_tel,
			@FormParam("user_address") String user_address,
			@FormParam("qq") String qq, @Context HttpServletResponse response) {
		System.out.println("user_role:" + user_role);
		System.out.println("qq : " + qq);
		userEntityService.addUser(user_account, user_realname, user_password,
				user_age, user_gender, user_role, user_tel, user_address);
		try {
			response.sendRedirect("../../jpmp/userlist.html");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

	@GET
	@Path("/getAllUsers")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<User> getAllUsers() {
		return userEntityService.getAllUsers();
	}

	@GET
	@Path("/getAllLoginInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<Login> getAllLoginInfo() {
		System.out.println("getAllLoginInfo : ");
		return userEntityService.getAllLoginInfo();
	}

	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void test() {
		System.out.println("test");
		userEntityService.test();
	}

}
