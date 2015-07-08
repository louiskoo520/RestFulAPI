package com.lungcare.dicomfile.algorithm;

import Morphy.MorphyClass;

import com.mathworks.toolbox.javabuilder.MWException;

public class MorphologicalSegment {
	private MorphologicalSegment() {
	}

	private static MorphologicalSegment morphologicalSegment = null;
	private static MorphyClass moSegment = null;

	public static MorphologicalSegment getMorphologicalSegment() {
		if (morphologicalSegment == null) {
			try {
				morphologicalSegment = new MorphologicalSegment();
				moSegment = new MorphyClass();
			} catch (MWException e) {
				// TODO: handle exception
			}

		}

		return morphologicalSegment;
	}

	public void MorphyDicom(String dicomPath, String bmpSavePath) {
		try {

			moSegment.demo(dicomPath, bmpSavePath);
		} catch (MWException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
