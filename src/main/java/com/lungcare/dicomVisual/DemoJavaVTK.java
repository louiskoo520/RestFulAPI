package com.lungcare.dicomVisual;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import vtk.vtkActor;
import vtk.vtkBMPReader;
import vtk.vtkContourFilter;
import vtk.vtkCutter;
import vtk.vtkDICOMImageReader;
import vtk.vtkImageData;
import vtk.vtkLookupTable;
import vtk.vtkMetaImageReader;
import vtk.vtkMetaImageWriter;
import vtk.vtkNativeLibrary;
import vtk.vtkPanel;
import vtk.vtkPlane;
import vtk.vtkPolyDataMapper;
import vtk.vtkSampleFunction;
import vtk.vtkSphere;
import vtk.vtkSphereSource;
import vtk.vtkStringArray;

/* ************************************************************
 * Demo applications showcasing how to use VTK with Java
 * 
 * Based on SimpleVTK.java example distributed with VTK
 * 
 * For more information see:
 * http://www.particleincell.com/2011/vtk-java-visualization
 * 
 * Information about VTK can be found at:
 * http://vtk.org/
 * 
 * ***********************************************************/

public class DemoJavaVTK extends JPanel implements ActionListener 
{
    private static final long serialVersionUID = 1L;
    private vtkPanel renWin;
    private vtkActor cutActor;
    private vtkActor isoActor;
	
    private JPanel buttons;
    private JToggleButton slicesButton;
    private JToggleButton isoButton;
    private JButton exitButton;

    /* Load VTK shared librarires (.dll) on startup, print message if not found */
    static 
    {				
    	if (!vtkNativeLibrary.LoadAllNativeLibraries()) {
			for (vtkNativeLibrary lib : vtkNativeLibrary.values()) {
				File dir = new File(System.getProperty("java.library.path"));
				File libPath = new File(dir, System.mapLibraryName(lib
						.GetLibraryName()));
				if (libPath.exists()) {
					try {
						Runtime.getRuntime().load(libPath.getAbsolutePath());
						System.out
								.println(lib.GetLibraryName() + " not loaded");
					} catch (UnsatisfiedLinkError e) {
						System.out.println(e.getMessage());
					}
				}

			}

			System.out.println("Make sure the search path is correct: ");
			
			System.out.println(System.getProperty("java.library.path"));
			
    	}

        vtkNativeLibrary.DisableOutputWindow(null);
    }

    /* Constructor - generates visualization pipeline and adds actors*/
    public DemoJavaVTK() 
    {
        super(new BorderLayout()); /* large center and small border areas*/
		
        double radius = 0.8;		/*sphere radius*/
		
	/**** 1) INPUT DATA: Sphere Implicit Function ****/
	vtkSphere sphere = new vtkSphere();
	sphere.SetRadius(radius);
		
	vtkSampleFunction sample = new vtkSampleFunction();
	sample.SetSampleDimensions(50,50,50);
	sample.SetImplicitFunction(sphere);
		
	/**** 2) PIPELINE 1: Isosurface Actor ****/
		
	/* contour filter - will generate isosurfaces from 3D data*/
	vtkContourFilter contour = new vtkContourFilter();
	contour.SetInputConnection(sample.GetOutputPort());
	contour.GenerateValues(3,0,1);
		
	/* mapper, translates polygonal representation to graphics primitives */
	vtkPolyDataMapper isoMapper = new vtkPolyDataMapper();
        isoMapper.SetInputConnection(contour.GetOutputPort());
		
	/*isosurface actor*/
        isoActor = new vtkActor();
        isoActor.SetMapper(isoMapper);
	
	/**** 3) PIPELINE 2: Cutting Plane Actor ****/
		
	/* define a plane in x-y plane and passing through the origin*/
	vtkPlane plane = new vtkPlane();
	plane.SetOrigin(0,0,0);
	plane.SetNormal(0,0,1);
		
	/* cutter, basically interpolates source data onto the plane */
	vtkCutter planeCut = new vtkCutter();
	planeCut.SetInputConnection(sample.GetOutputPort());
	planeCut.SetCutFunction(plane);
	/*this will actually create 3 planes at the subspace where the implicit
	 * function evaluates to -0.7, 0, 0.7 (0 would be original plane). In 
	 * our case this will create three x-y planes passing through 
	 * z=-0.7, z=0, and z=+0.7*/
	planeCut.GenerateValues(3,-0.7,0.7);
		
	/* look up table, we want to reduce number of values to get discrete bands */
	vtkLookupTable lut = new vtkLookupTable();
	lut.SetNumberOfTableValues(5);
		
	/* mapper, using our custom LUT */
	vtkPolyDataMapper cutMapper = new vtkPolyDataMapper();
        cutMapper.SetInputConnection(planeCut.GetOutputPort());
	cutMapper.SetLookupTable(lut);
		
	/* cutting plane actor, looks much better with flat shading */
	cutActor = new vtkActor();
        cutActor.SetMapper(cutMapper);
	cutActor.GetProperty().SetInterpolationToFlat();
		
	/**** 4) PIPELINE 3: Surface Geometry Actor ****/
		
	/* create polygonal representation of a sphere */
	vtkSphereSource surf = new vtkSphereSource();
	surf.SetRadius(radius);
		
	/* another mapper*/
	vtkPolyDataMapper surfMapper = new vtkPolyDataMapper();
	surfMapper.SetInputConnection(surf.GetOutputPort());
		
	/* surface geometry actor, turn on edges and apply flat shading*/
	vtkActor surfActor = new vtkActor();
	surfActor.SetMapper(surfMapper);
	surfActor.GetProperty().EdgeVisibilityOn();
	surfActor.GetProperty().SetEdgeColor(0.2,0.2,0.2);
	surfActor.GetProperty().SetInterpolationToFlat();

	/**** 5) RENDER WINDOW ****/
		
	/* vtkPanel - this is the interface between Java and VTK */
	renWin = new vtkPanel();
		
	/* add the surface geometry plus the isosurface */
	renWin.GetRenderer().AddActor(surfActor);
	renWin.GetRenderer().AddActor(isoActor);
		
	/* the default zoom is whacky, zoom out to see the whole domain */
        renWin.GetRenderer().GetActiveCamera().Dolly(0.2); 
	renWin.GetRenderer().SetBackground(1, 1, 1);
		
	/**** 6) CREATE PANEL FOR BUTTONS ****/
	buttons  = new JPanel();
	buttons.setLayout(new GridLayout(1,0));
		
        /* isosurface button, clicked by default */
	isoButton = new JToggleButton("Isosurfaces",true);
        isoButton.addActionListener(this);
		
	/* cutting planes button */
        slicesButton = new JToggleButton("Slices");
        slicesButton.addActionListener(this);
		
	/* exit button */
	exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
		
	/* add buttons to the panel */
	buttons.add(isoButton); 
	buttons.add(slicesButton);
	buttons.add(exitButton); 

	/**** 7) POPULATE MAIN PANEL ****/
        add(renWin, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);	
    }

    /* ActionListener that responds to button clicks
     * Toggling iso/slices buttons results in addition or removal
     * of the corresponding actor */
    public void actionPerformed(ActionEvent e) 
    {
	/*cutting planes button, add or remove cutActor */
	if (e.getSource().equals(slicesButton))
	{
		if (slicesButton.isSelected())
			renWin.GetRenderer().AddActor(cutActor);
		else
			renWin.GetRenderer().RemoveActor(cutActor);
			
		renWin.Render();
	}
	/*isosurface button, add or remove isoActor */
	else if (e.getSource().equals(isoButton))
	{
		if (isoButton.isSelected())
			renWin.GetRenderer().AddActor(isoActor);
		else
			renWin.GetRenderer().RemoveActor(isoActor);
		renWin.Render();
	}
	/*exit button, end application */
	else if (e.getSource().equals(exitButton)) 
	{
            System.exit(0);
        }
    }

    public static void Dicom2MHD(String dicomDirectory, String mhdFilePath)
    {
        vtkDICOMImageReader reader = new vtkDICOMImageReader();
        reader.SetDirectoryName(dicomDirectory);
        reader.Update();

        vtkMetaImageWriter write = new vtkMetaImageWriter();
        write.SetFileName(mhdFilePath);
        write.SetInput(reader.GetOutput());
        reader.Delete();
        write.Write();
        write.Delete();
    }
    
    public static void WriteImageData(String bmpPath, String imageDataPath, String spacingStr)
    {
    	File dir = new File(bmpPath);
    	
        String[] fileNames = dir.list();
        vtkStringArray array = new vtkStringArray();
        for (int i = 0; i < fileNames.length; i++)
        {	
            array.InsertNextValue(bmpPath+File.separator+fileNames[i]);
        }



        vtkBMPReader bmpRead = new vtkBMPReader();
        bmpRead.Allow8BitBMPOn();

        bmpRead.SetFileNames(array);
        bmpRead.Update();
        vtkImageData imagedata = bmpRead.GetOutput();
        bmpRead.Delete();

        vtkMetaImageWriter write = new vtkMetaImageWriter();
        write.SetFileName(imageDataPath);
        write.SetInput(imagedata);
        write.Write();
        write.Delete();
        
        ReplaceMHDValue(imageDataPath, spacingStr);
    }
    
    public static void ReplaceMHDValue(String fileName, String newValue)
    {
		try {
			String path = fileName;
			FileInputStream fs = new FileInputStream(new File(path));
			
			int size = fs.available();
			byte[] buffer = new byte[size];
			fs.read(buffer);
			fs.close();
			String content = new String(buffer,"UTF-8");
			System.out.println(content);
			int startIndex = content.indexOf("ElementSpacing");
			System.out.println(startIndex);
			int endIndex = content.indexOf("DimSize ");
			System.out.println(endIndex);
			
			String selectedContent = content.substring(startIndex + 17, endIndex);
			content = content.replace(selectedContent, newValue + "\n");
			System.out.println(selectedContent);
			FileOutputStream fos = new FileOutputStream(new File(path));
			fos.write(content.getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

    }
    
    public static vtkImageData ReadMetaImage(String mhdFileName)
    {
        //File.ReadAllBytes(mhdFileName);
        vtkMetaImageReader reader =new vtkMetaImageReader();
        reader.SetFileName(mhdFileName);
        reader.Update();
        vtkImageData output = reader.GetOutput();
        reader.Delete();
        reader = null;
        return output;
    }
    
    /* main, creates a new JFrame and populates it with the DemoJavaVTK panel */
    public static void main(String s[]) throws IOException 
    {
//        SwingUtilities.invokeLater(new Runnable() 
//	{
//            @Override
//            public void run() 
//	    {
//                JFrame frame = new JFrame("Java and VTK Demo");
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                frame.getContentPane().setLayout(new BorderLayout());
//                frame.getContentPane().add(new DemoJavaVTK(), BorderLayout.CENTER);
//                frame.setSize(400, 400);
//                frame.setLocationRelativeTo(null);
//                frame.setVisible(true);
//            }
//        });
    	
    	String path1 = "D:\\1049664802\\FileRecv\\2\\testCT\\";
		String path2 = "D:\\1049664802\\FileRecv\\2\\ct.mhd";
		System.out.println("000");
		Dicom2MHD(path1, path2);
		
    	String bmpPathString = "D:\\1049664802\\FileRecv\\2\\1";
		String imageDataPath = "D:\\1049664802\\FileRecv\\2\\111.mhd";
		String spacingString = "0.82 0.82 1";
		WriteImageData(bmpPathString, imageDataPath, spacingString);
		
    	String morphyMhdSavedFilePath = "D:\\1049664802\\FileRecv\\2\\111.mhd";
    	String morphyWholeBmpPath = "D:\\1049664802\\FileRecv\\2\\morphy.bmp";
    	String MetaImageFName = morphyMhdSavedFilePath;
    	File file = new File(MetaImageFName);
        if (file.exists()) 
        {
            RendererPackage rendererPackage = new RendererPackage();
            rendererPackage.StartRefreshAll();
            vtkImageData _metaImage = ReadMetaImage(MetaImageFName);
            VRPackage package2 = new VRPackage(rendererPackage, 255.0, 128.0, _metaImage);

            double[] bounds = _metaImage.GetBounds();
            rendererPackage.正面观(bounds);
            rendererPackage.ResetCameraAndRefreshAll();
            rendererPackage.StartRefreshAll();
            rendererPackage.FastScreenShot1(morphyWholeBmpPath);

            //Thread.Sleep(3000);
            package2.Dispose();
            rendererPackage.Dispose();
        }
    }
}
