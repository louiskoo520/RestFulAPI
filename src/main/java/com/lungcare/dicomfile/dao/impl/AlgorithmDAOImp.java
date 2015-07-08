package com.lungcare.dicomfile.dao.impl;

import com.lungcare.dicomfile.algorithm.MorphologicalSegment;
import com.lungcare.dicomfile.dao.IAlgorithmDAO;

public class AlgorithmDAOImp implements IAlgorithmDAO {
	@Override
	public void MorphySegment(String dicomPath, String bmpSavePath) {
		// TODO Auto-generated method stub
		MorphologicalSegment morphologicalSegment = MorphologicalSegment
				.getMorphologicalSegment();
		morphologicalSegment.MorphyDicom(dicomPath, bmpSavePath);
	}
}
