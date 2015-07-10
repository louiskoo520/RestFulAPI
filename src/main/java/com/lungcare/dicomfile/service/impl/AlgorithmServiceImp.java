package com.lungcare.dicomfile.service.impl;

import com.lungcare.dicomfile.dao.IAlgorithmDAO;
import com.lungcare.dicomfile.service.IAlgorithmService;

public class AlgorithmServiceImp implements IAlgorithmService {
	private IAlgorithmDAO algorithmDAOImp;
	
	public void dicomSegmentation(String dicomPathString ,String bmpSavePath){
		algorithmDAOImp.dicomSegmentation(dicomPathString, bmpSavePath);
	}
}
