package com.lungcare.dicomfile.dao.impl;

import com.lungcare.dicomfile.algorithm.MorphologicalSegment;
import com.lungcare.dicomfile.dao.IAlgorithmDAO;

public class AlgorithmDAOImp implements IAlgorithmDAO {
	
	public void dicomSegmentation(String dicomPathString, String bmpSavePath) {
		MorphologicalSegment.getMorphologicalSegment().MorphyDicom(dicomPathString, bmpSavePath);
	}
	
	
	
}
