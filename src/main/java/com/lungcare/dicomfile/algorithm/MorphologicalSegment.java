package com.lungcare.dicomfile.algorithm;

import java.io.File;

import matlabmorphy.Morphy;

import com.mathworks.toolbox.javabuilder.MWException;

public class MorphologicalSegment {
    private MorphologicalSegment() {
    }

    private static MorphologicalSegment morphologicalSegment = null;
    private static Morphy moSegment = null;

    public static MorphologicalSegment getMorphologicalSegment() {
        if (morphologicalSegment == null) {
            try {
                morphologicalSegment = new MorphologicalSegment();
                moSegment = new Morphy();
            } catch (MWException e) {
                // TODO: handle exception
            }

        }

        return morphologicalSegment;
    }

    public void MorphyDicom(String dicomPathString, String bmpSavePath) {
        try {
        	
//        	Morphy morphy = new Morphy();
//			dicomPathString = "D:\\1049664802\\FileRecv\\CT\\";
//			bmpSavrAxialString = "D:\\1049664802\\FileRecv\\seg\\1\\";
//			bmpSaveCoronalString = "D:\\1049664802\\FileRecv\\seg\\2\\";
//			bmpSaveSagittalString = "D:\\1049664802\\FileRecv\\seg\\3\\";
        	String bmpSavrAxialString = bmpSavePath + "axial\\";
        	String bmpSaveCoronalString = bmpSavePath + "coronal\\";
        	String bmpSaveSagittalString = bmpSavePath + "sagittal\\"; 
			File folder = new File(bmpSavrAxialString);
			folder.mkdirs();
			File folder1 = new File(bmpSaveCoronalString);
			folder1.mkdir();
			File folder2 = new File(bmpSaveSagittalString);
			folder2.mkdir();
			moSegment.demo_zlf_boundingbox(dicomPathString,bmpSavrAxialString, 100 ,2 ,0,1);
			//moSegment.demo_zlf(dicomPathString,bmpSaveCoronalString,1,1);
			//moSegment.demo_zlf(dicomPathString,bmpSaveSagittalString,1,0);
            //moSegment.demo(dicomPath, bmpSavePath);
        } catch (MWException e) {
            e.printStackTrace();
        }
    }
}

