package com.lungcare.dicomfile.client;

import com.lungcare.dicomfile.util.SegUtil;


public class MathPlotTest {
	public static void main(String[] args) {
//		try {
//			MathPlot func = new MathPlot();
//			func.plotsin(0, 0.0f,0.01f,10*Math.PI);
//			func.plotcos(0, 0.0f,0.01f,10*Math.PI);
//			System.out.println("over");
//			Morphy morphy = new Morphy();
			String dicomPathString = "D:\\1049664802\\FileRecv\\CT\\";
			String bmpSavrAxialString = "D:\\1049664802\\FileRecv\\seg\\1\\";
			String bmpSaveCoronalString = "D:\\1049664802\\FileRecv\\seg\\2\\";
			String bmpSaveSagittalString = "D:\\1049664802\\FileRecv\\seg\\3\\";
//			File folder = new File(bmpSavrAxialString);
//			folder.mkdirs();
//			File folder1 = new File(bmpSaveCoronalString);
//			folder1.mkdir();
//			File folder2 = new File(bmpSaveSagittalString);
//			folder2.mkdir();
//			morphy.demo(dicomPathString,bmpSavrAxialString,0,1);
//			morphy.demo(dicomPathString,bmpSaveCoronalString,1,1);
//			morphy.demo(dicomPathString,bmpSaveSagittalString,1,0);
//			System.out.println("over");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		SegUtil segUtil = new SegUtil();
		segUtil.segmantion(dicomPathString, bmpSavrAxialString, bmpSaveCoronalString, bmpSaveSagittalString);
	}
}
