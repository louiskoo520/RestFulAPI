package com.lungcare.dicomfile.client;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;


public class CsrfProtectionFilter implements ContainerRequestFilter {
	private static final String HEADER_NAME = "X-Requested-By";
	//关注点1:忽略方法集合
	private static final Set<String> METHODS_TO_IGNORE;
	static {
		HashSet<String> mti = new HashSet<String>();
		mti.add("GET");
		mti.add("OPTIONS");
		mti.add("HEAD");
		METHODS_TO_IGNORE = Collections.unmodifiableSet(mti);
	}
	
	@Override
	public void filter(ContainerRequestContext rc) throws IOException {
		System.out.println("container request filter...");
		//关注点2:判断方法名称是否符合条件
		if(!METHODS_TO_IGNORE.contains(rc.getMethod())&&!rc.getHeaders().containsKey(HEADER_NAME)){
			throw new BadRequestException();
		}
	}

}
