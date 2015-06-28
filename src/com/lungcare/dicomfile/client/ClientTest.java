package com.lungcare.dicomfile.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

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
				.resource("http://localhost:8080/WebRestService/services/remotefile/get/127.0.0.1");
		System.out.println(resource.get(String.class));
		// System.out.println("bb");

	}

}
