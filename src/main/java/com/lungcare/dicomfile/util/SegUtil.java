package com.lungcare.dicomfile.util;

import java.io.File;

import matlabmorphy.Morphy;

public class SegUtil {
	
	
	public void segmantion(String dicomPathString,String bmpSavrAxialString,String bmpSaveCoronalString,String bmpSaveSagittalString){	
		try {
			Morphy morphy = new Morphy();
			dicomPathString = "D:\\1049664802\\FileRecv\\CT\\";
			bmpSavrAxialString = "D:\\1049664802\\FileRecv\\seg\\1\\";
			bmpSaveCoronalString = "D:\\1049664802\\FileRecv\\seg\\2\\";
			bmpSaveSagittalString = "D:\\1049664802\\FileRecv\\seg\\3\\";
			File folder = new File(bmpSavrAxialString);
			folder.mkdirs();
			File folder1 = new File(bmpSaveCoronalString);
			folder1.mkdir();
			File folder2 = new File(bmpSaveSagittalString);
			folder2.mkdir();
			morphy.demo(dicomPathString,bmpSavrAxialString,0,1);
			morphy.demo(dicomPathString,bmpSaveCoronalString,1,1);
			morphy.demo(dicomPathString,bmpSaveSagittalString,1,0);
			System.out.println("over");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
