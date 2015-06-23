package com.client;

import java.io.File;

public class ClientTest {
	public static void main(String[] args) {
		/*
		 * Client client = new Client(); client.setFollowRedirects(true);
		 * 
		 * WebResource result = client.resource(
		 * "http://localhost:8080/WebRestService1/services/customers/0");
		 * System.out.println(result.get(String.class));
		 */

		File file = new File("C:\\WEB-INF\\logs\\ssh.log");
		System.out.println(file.exists());
	}

}
