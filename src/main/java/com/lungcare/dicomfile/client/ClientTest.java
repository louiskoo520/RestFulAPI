package com.lungcare.dicomfile.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class ClientTest {
	public static void main(String[] args) {
		//Client client = (Client) ClientBuilder.newClient();
		Client client = Client.create();
		WebResource resource = client
				.resource("http://localhost:8787/transfer/rest/user/addUser");
		System.out.println(resource.get(String.class));
		
	}
}
