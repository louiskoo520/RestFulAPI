package com.lungcare.dicomfile.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.lungcare.dicomfile.dao.IAlgorithmDAO;
import com.lungcare.dicomfile.service.IAlgorithmService;

public class AlgorithmServiceImp implements IAlgorithmService {
	@Autowired
	private IAlgorithmDAO algorithmDAO;

	public void dicomSegmentation(String dicomPathString, String bmpSavePath) {
		algorithmDAO.dicomSegmentation(dicomPathString, bmpSavePath);
	}
}
