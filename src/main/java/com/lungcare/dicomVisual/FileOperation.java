package com.lungcare.dicomVisual;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import vtk.vtkBMPReader;
import vtk.vtkDICOMImageReader;
import vtk.vtkImageData;
import vtk.vtkMetaImageWriter;
import vtk.vtkStringArray;

public class FileOperation {
	public static void main(String[] args) {
		String bmpPathString = "D:\\1049664802\\FileRecv\\2\\1";
		String imageDataPath = "D:\\1049664802\\FileRecv\\2\\111.mhd";
		String spacingString = "1 1 1";
		WriteImageData(bmpPathString, imageDataPath, spacingString);
	}

	public static void WriteImageData(String bmpPath, String imageDataPath,
			String spacingStr) {
		File dir = new File(bmpPath);
		String[] fileNames = dir.list();

		vtkStringArray array = new vtkStringArray();
		for (int i = 0; i < fileNames.length; i++) {
			array.InsertNextValue(fileNames[i]);
		}

		vtkBMPReader bmpRead = new vtkBMPReader();
		bmpRead.Allow8BitBMPOn();

		bmpRead.SetFileNames(array);
		bmpRead.Update();
		vtkImageData imagedata = bmpRead.GetOutput();
		bmpRead.Delete();

		vtkMetaImageWriter write = new vtkMetaImageWriter();
		write.SetFileName(imageDataPath);
		write.SetInputData(imagedata);
		write.Write();
		write.Delete();

		// ReplaceMHDValue(imageDataPath, spacingStr);

	}

	public static void ReplaceMHDValue(String fileName, String newValue) {
		try {
			String path = fileName;
			FileInputStream fs = new FileInputStream(new File(path));

			int size = fs.available();
			byte[] buffer = new byte[size];
			fs.read(buffer);
			fs.close();
			String content = new String(buffer, "UTF-8");
			int startIndex = content.indexOf("ElementSpacing");
			int endIndex = content.indexOf("DimSize ");

			String selectedContent = content.substring(startIndex + 17,
					endIndex - startIndex - 17);
			content = content.replace(selectedContent, newValue + "\n");
			System.out.println(selectedContent);
			FileOutputStream fos = new FileOutputStream(new File(path));
			fos.write(content.getBytes());
			// BufferedWriter write = new BufferedWriter(fos);
			// write.write(content);
			// write.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void Dicom2MHD(String dicomDirectory, String mhdFilePath) {
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

	public static boolean isDicom(File file) {
		FileInputStream fis = null;
		if (!file.isDirectory()) {
			try {
				fis = new FileInputStream(file);
				byte[] data = new byte[132];
				fis.read(data, 0, data.length);
				int b0 = data[0] & 255;
				int b1 = data[1] & 255;
				// int b2 = data[2] & 255;
				int b3 = data[3] & 255;

				if (data[128] == 68 && data[129] == 73 && data[130] == 67
						&& data[131] == 77) {
					return true;
				} else if ((b0 == 8 || b0 == 2) && b1 == 0 && b3 == 0) {
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
					}
				}
			}
		}
		return false;
	}
}
