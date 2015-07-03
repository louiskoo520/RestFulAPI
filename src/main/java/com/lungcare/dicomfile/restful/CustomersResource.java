package com.lungcare.dicomfile.restful;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lungcare.dicomfile.entity.Customer;
import com.lungcare.dicomfile.service.ILocalFileService;

@Path("customers")
@Component
public class CustomersResource {

	public CustomersResource() {
		Customer customer = new Customer();
		customer.setName("Harold Abernathy");
		customer.setAddress("Sheffield, UK");
		AddCustomers(customer);

		Customer customer1 = new Customer();
		customer1.setName("Harold Abernathy1");
		customer1.setAddress("Sheffield, UK1");
		AddCustomers(customer1);

		// localFileService = new LocalFileServiceImp();
	}

	private TreeMap<Integer, Customer> customerMap = new TreeMap<Integer, Customer>();


	@Autowired
	private ILocalFileService localFileService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Customer getCustomers(@PathParam("id") int cId) {
		System.out.println("getCustomers by id : " + cId);

		return customerMap.get(cId);
		// throw new UnsupportedOperationException("Not yet implemented.");
	}

	@POST
	@Path("add")
	@Produces("text/html")
	@Consumes("application/xml")
	public String AddCustomers(Customer customer) {
		int id = customerMap.size();
		customer.setId(id);
		customerMap.put(id, customer);

		return "Customer " + customer.getName() + " added with Id " + id;
		// throw new UnsupportedOperationException("Not yet implemented.");
	}

	@GET
	@Path("getall")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<Customer> GetCustomers() {
		List<Customer> list = new ArrayList<Customer>();
		list.addAll(customerMap.values());
		return list;
		// throw new UnsupportedOperationException("Not yet implemented.");
	}

	@GET
	@Path("test")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String test() {
		localFileService.upload();
		localFileService.download();
		return "success lx ";
		// throw new UnsupportedOperationException("Not yet implemented.");
	}

}