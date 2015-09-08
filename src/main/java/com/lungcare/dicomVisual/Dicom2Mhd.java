package com.lungcare.dicomVisual;

import vtk.vtkDICOMImageReader;
import vtk.vtkMetaImageWriter;

public class Dicom2Mhd {
	public static void main(String[] args) {

		String path1 = "D:\\1049664802\\FileRecv\\2\\testCT\\";
		String path2 = "D:\\1049664802\\FileRecv\\2\\";
		System.out.println("000");
		Dicom2MHD(path1, path2);

	}

	public static void Dicom2MHD(String dicomDirectory, String mhdFilePath) {

		System.out.println("111");
		vtkDICOMImageReader reader = new vtkDICOMImageReader();
		reader.SetDirectoryName(dicomDirectory);
		reader.Update();

		vtkMetaImageWriter write = new vtkMetaImageWriter();
		write.SetFileName(mhdFilePath);
		write.SetInputData(reader.GetOutput());
		reader.Delete();
		write.Write();
		write.Delete();

	}
}
