package com.lungcare.dicomfile.restful;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.lungcare.dicomfile.entity.Customer;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.spi.resource.Singleton;

@Produces("application/xml")
@Path("customers")
@Singleton
public class CustomersResource {

	private TreeMap<Integer, Customer> customerMap = new TreeMap<Integer, Customer>();

	private static final String FILE_UPLOAD_PATH = "/Users/arun_kumar/Pictures";
	private static final String CANDIDATE_NAME = "candidateName";
	private static final String SUCCESS_RESPONSE = "Successful";
	private static final String FAILED_RESPONSE = "Failed";

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("text/plain")
	@Path("/multipleFiles")
	public String registerWebService(FormDataMultiPart formParams) {

		Map<String, List<FormDataBodyPart>> fieldsByName = formParams
				.getFields();

		// Usually each value in fieldsByName will be a list of length 1.
		// Assuming each field in the form is a file, just loop through them.

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

		return SUCCESS_RESPONSE;
	}

	private static final String FOLDER_PATH = "C:\\my_files\\";

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public String uploadFile(@FormDataParam("file") InputStream fis,
			@FormDataParam("file") FormDataContentDisposition fdcd) {

		saveFile(fis, fdcd, fdcd.getFileName());
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

	public CustomersResource() {
		Customer customer = new Customer();
		customer.setName("Harold Abernathy");
		customer.setAddress("Sheffield, UK");
		AddCustomers(customer);

		Customer customer1 = new Customer();
		customer1.setName("Harold Abernathy1");
		customer1.setAddress("Sheffield, UK1");
		AddCustomers(customer1);
	}

	@GET
	@Path("{id}")
	public Customer getCustomers(@PathParam("id") int cId) {
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
	public List<Customer> GetCustomers() {
		List<Customer> list = new ArrayList<Customer>();
		list.addAll(customerMap.values());
		return list;
		// throw new UnsupportedOperationException("Not yet implemented.");
	}
}