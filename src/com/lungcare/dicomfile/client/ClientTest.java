package com.lungcare.dicomfile.client;

import java.io.File;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

public class ClientTest {
	public static void main(String[] args) {
		/*
		 * Client client = new Client(); client.setFollowRedirects(true);
		 * 
		 * WebResource result = client.resource(
		 * "http://localhost:8080/WebRestService1/services/customers/0");
		 * System.out.println(result.get(String.class));
		 */

		Client client = Client.create();
		WebResource resource = client
				.resource("http://localhost:8080/WebRestService1/services/customers/upload1");
		// System.out.println(resource.get(String.class));

		FormDataMultiPart formDataMultiPart = new FormDataMultiPart();

		/*
		 * String conString = "This is the content"; FormDataBodyPart bodyPart =
		 * new FormDataBodyPart("file", new
		 * ByteArrayInputStream(conString.getBytes()),
		 * MediaType.APPLICATION_OCTET_STREAM_TYPE);
		 * formDataMultiPart.bodyPart(bodyPart);
		 */

		FileDataBodyPart fileDataBodyPart1 = new FileDataBodyPart("file",
				new File("c:\\dll.log"),
				MediaType.APPLICATION_OCTET_STREAM_TYPE);
		/*
		 * FileDataBodyPart fileDataBodyPart2 = new FileDataBodyPart("file", new
		 * File("H:\\Data\\XK0001\\seg2\\123.bmp"),
		 * MediaType.APPLICATION_OCTET_STREAM_TYPE);
		 */

		formDataMultiPart.bodyPart(fileDataBodyPart1);
		// formDataMultiPart.bodyPart(fileDataBodyPart2);
		String reString = resource.type(MediaType.MULTIPART_FORM_DATA)
		// .accept(MediaType.APPLICATION_XML)
				.post(String.class, formDataMultiPart);
		System.out.println(reString);

		// MediaType of the body part will be derived from the file.
	}

}
