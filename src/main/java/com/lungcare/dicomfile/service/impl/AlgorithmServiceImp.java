package com.lungcare.dicomfile.service.impl;

import com.lungcare.dicomfile.dao.IAlgorithmDAO;
import com.lungcare.dicomfile.service.IAlgorithmService;

public class AlgorithmServiceImp implements IAlgorithmService {
	IAlgorithmDAO algorithmDAO;

	@Override
	public void MorphySegment(String dicomPath, String bmpSavePath) {
		// TODO Auto-generated method stub
		algorithmDAO.MorphySegment(dicomPath, bmpSavePath);
	}
}
