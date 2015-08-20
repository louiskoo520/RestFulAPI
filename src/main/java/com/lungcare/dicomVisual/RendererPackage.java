package com.lungcare.dicomVisual;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import vtk.vtkActor;
import vtk.vtkBMPWriter;
import vtk.vtkCamera;
import vtk.vtkInteractorStyleFlight;
import vtk.vtkInteractorStyleImage;
import vtk.vtkInteractorStyleJoystickCamera;
import vtk.vtkInteractorStyleTrackballActor;
import vtk.vtkInteractorStyleTrackballCamera;
import vtk.vtkInteractorStyleUser;
import vtk.vtkLight;
import vtk.vtkPicker;
import vtk.vtkRenderWindow;
import vtk.vtkRenderWindowInteractor;
import vtk.vtkRenderer;
import vtk.vtkTransform;
import vtk.vtkUnsignedCharArray;
import vtk.vtkVolumePicker;
import vtk.vtkWindowToImageFilter;

public class RendererPackage {

    //ublic static final String Renderer
	// Fields
    private vtkRenderWindowInteractor _iren;
    public vtkRenderer _renderer;
    private  vtkRenderWindow _renwin;
    private  vtkPicker _vtkPicker;
    private  vtkVolumePicker _vtkWorldPointPicker;
    //private Control control;
    

    // Methods
     RendererPackage()
    {
        this._vtkWorldPointPicker =new vtkVolumePicker();
        this._vtkPicker =new vtkPicker();
        
        this._renwin =new vtkRenderWindow();
        this._renderer =new vtkRenderer();
        this._renwin.AddRenderer(this._renderer);
        this._iren =new vtkRenderWindowInteractor();
        this._iren.SetRenderWindow(this._renwin);
        vtkInteractorStyleTrackballCamera camera =new vtkInteractorStyleTrackballCamera();
        this._iren.SetInteractorStyle(camera);
        this._iren.SetPicker(this._vtkWorldPointPicker);
    }


    public void Dispose()
    {
        
        if (_renwin != null) {
            _vtkWorldPointPicker.Delete();
            _vtkPicker.Delete();

            _renwin.RemoveRenderer(_renderer);
            _renderer.Delete();
            _renwin.Delete();
            _iren.Delete();
        }
        
        //
    }
   
    

     RendererPackage(vtkRenderer renderer, vtkRenderWindow renwin)
    {
        this._vtkWorldPointPicker =new vtkVolumePicker();
        this._vtkPicker =new vtkPicker();
        
        this._renderer = renderer;
        this._renwin = renwin;
    }

     void AddActor(vtkActor actor)
    {
        this._renderer.AddActor(actor);
    }

     

    

     void FinishRefreshAll()
    {
        if (this._renwin != null)
        {
            this._renwin.Frame();
        }
    }

  

     void InteractorStyleFlight()
    {
        this._iren.SetInteractorStyle(new vtkInteractorStyleFlight());
    }

     void InteractorStyleImage()
    {
        this._iren.SetInteractorStyle(new vtkInteractorStyleImage());
    }

     void InteractorStyleJoystickCamera()
    {
        this._iren.SetInteractorStyle(new vtkInteractorStyleJoystickCamera());
    }

     void InteractorStyleTrackballActor()
    {
        this._iren.SetInteractorStyle(new vtkInteractorStyleTrackballActor());
    }

     void InteractorStyleTrackballCamera()
    {
        this._iren.SetInteractorStyle(new vtkInteractorStyleTrackballCamera());
    }

     void InteractorStyleUser()
    {
        this._iren.SetInteractorStyle(new vtkInteractorStyleUser());
    }

     void InteractorStyleZoom()
    {
        this._iren.SetInteractorStyle(new vtkInteractorStyleImage());
    }

   
   
    void RefreshAll()
    {
        if (this._renwin != null)
        {
            this._renwin.Render();
        }
    }

    void RemoveActor(vtkActor actor)
    {
        this._renderer.RemoveActor(actor);
    }

    void ResetCamera()
    {
        this._renderer.ResetCameraClippingRange();
        this._renderer.ResetCamera();
    }

    void ResetCamera(double[] bound)
    {
        this._renderer.ResetCameraClippingRange();
        this._renderer.ResetCamera(bound[0], bound[1], bound[2], bound[3], bound[4], bound[5]);
    }

    void ResetCameraAndRefreshAll()
    {
        this.ResetCamera();
        this.RefreshAll();
    }

    void ResetCameraClippingRange()
    {
        this._renderer.ResetCameraClippingRange();
    }

    void ResetCameraClippingRangeAndRefreshAll()
    {
        this.ResetCameraClippingRange();
        this.RefreshAll();
    }

    void RestCameraClippingRangeAndRefreshAll()
    {
        this._renderer.ResetCameraClippingRange();
        this.RefreshAll();
    }

    void Start()
    {
        this._renwin.Render();
        this._renderer.GetActiveCamera().SetUserTransform(new vtkTransform());
        this._renderer.GetActiveCamera().Roll(90.0);
        this._renderer.GetActiveCamera().Azimuth(90.0);
        this._renderer.GetActiveCamera().Azimuth(90.0);
        this._renderer.GetActiveCamera().Azimuth(90.0);
        this._renderer.GetActiveCamera().Roll(90.0);
        this._renderer.GetActiveCamera().Roll(90.0);
        this._renderer.GetActiveCamera().Roll(90.0);
        this._iren.Start();
        //new Thread(() => this._iren.Start()).Start();
    }

    void StartRefreshAll()
    {
        if (this._renwin != null)
        {
            this._renwin.SwapBuffersOff();
            this._renwin.SetOffScreenRendering(1);
            this._renwin.Render();
            this._renwin.SwapBuffersOn();
        }
    }

     void Stop()
    {
        this._iren.TerminateApp();
        this._iren.Disable();
        this._iren.ExitEvent();
        this._iren.TerminateApp();
        this._iren.Delete();
        this._renwin.Delete();
        this._renderer.Delete();
        this._vtkWorldPointPicker.Delete();
    }

     void UpdateBrightLight(int attenuationValue)
    {
        UpdateBrightLight(this._renderer.GetActiveCamera(), this._renderer, attenuationValue, this._renwin);
    }

    private static void UpdateBrightLight(vtkCamera aCamera, vtkRenderer _renderer, int attenuationValue, vtkRenderWindow _renwin)
    {
        _renderer.LightFollowCameraOff();
        _renderer.AutomaticLightCreationOff();
        _renderer.RemoveAllLights();
        vtkLight light =new vtkLight();
        light.SetLightTypeToCameraLight();
        double[] position = aCamera.GetPosition();
        double[] numArray2 = aCamera.GetPosition();
        position[0] -= aCamera.GetDirectionOfProjection()[0] * 10.0;
        position[1] -= aCamera.GetDirectionOfProjection()[1] * 10.0;
        position[2] -= aCamera.GetDirectionOfProjection()[2] * 10.0;
        light.SetPosition(position[0], position[1], position[2]);
        light.SetFocalPoint(aCamera.GetFocalPoint()[0], aCamera.GetFocalPoint()[1], aCamera.GetFocalPoint()[2]);
        numArray2[0] = (aCamera.GetFocalPoint()[0] + numArray2[0]) / 2.0;
        numArray2[1] = (aCamera.GetFocalPoint()[1] + numArray2[1]) / 2.0;
        numArray2[2] = (aCamera.GetFocalPoint()[2] + numArray2[2]) / 2.0;
        light.SetConeAngle(80.0);
        light.SetIntensity(1.0);
        light.SetColor(0.76470588235294112, 0.34901960784313724, 0.396078431372549);
        light.SetAttenuationValues(0.0, 0.0, 0.0001 * attenuationValue);
        _renderer.AddLight(light);
        _renwin.Render();
    }

    /*void UpdateCamera(vtkCamera mainViewCamera)
    {
        vtkCamera camera = this.Camera;
        double[] position = mainViewCamera.GetPosition();
        double[] focalPoint = mainViewCamera.GetFocalPoint();
        double[] viewUp = mainViewCamera.GetViewUp();
        camera.SetPosition(position[0], position[1], position[2]);
        camera.SetFocalPoint(focalPoint[0], focalPoint[1], focalPoint[2]);
        camera.SetViewUp(viewUp[0], viewUp[1], viewUp[2]);
        camera.ComputeViewPlaneNormal();
        camera.ComputeProjAndViewParams();
        camera.OrthogonalizeViewUp();
        this._renderer.ResetCameraClippingRange();
    }*/

    void UpdateFPS()
    {
    }

    void UpdateLight(int attenuationValue)
    {
        UpdateLight(this._renderer.GetActiveCamera(), this._renderer, attenuationValue, this._renwin);
    }

    private static void UpdateLight(vtkCamera aCamera, vtkRenderer _renderer, int attenuationValue, vtkRenderWindow _renwin)
    {
        _renderer.LightFollowCameraOff();
        _renderer.AutomaticLightCreationOff();
        _renderer.RemoveAllLights();
        vtkLight light =new vtkLight();
        light.SetLightTypeToCameraLight();
        double[] position = aCamera.GetPosition();
        double[] numArray2 = aCamera.GetPosition();
        position[0] -= aCamera.GetDirectionOfProjection()[0] * 10.0;
        position[1] -= aCamera.GetDirectionOfProjection()[1] * 10.0;
        position[2] -= aCamera.GetDirectionOfProjection()[2] * 10.0;
        light.SetPosition(position[0], position[1], position[2]);
        light.SetFocalPoint(aCamera.GetFocalPoint()[0], aCamera.GetFocalPoint()[1], aCamera.GetFocalPoint()[2]);
        numArray2[0] = (aCamera.GetFocalPoint()[0] + numArray2[0]) / 2.0;
        numArray2[1] = (aCamera.GetFocalPoint()[1] + numArray2[1]) / 2.0;
        numArray2[2] = (aCamera.GetFocalPoint()[2] + numArray2[2]) / 2.0;
        light.PositionalOn();
        _renderer.AddLight(light);
        _renwin.Render();
    }

    public void 冠状位()
    {
        this.正面观();
    }

    public void 矢状位()
    {
        vtkCamera activeCamera = this._renderer.GetActiveCamera();
        activeCamera.SetFocalPoint(0.0, 0.0, 0.0);
        activeCamera.SetPosition(100.0, 0.0, 0.0);
        activeCamera.SetViewUp(0.0, 0.0, -1.0);
    }

    public void 正面观()
    {
        vtkCamera activeCamera = this._renderer.GetActiveCamera();
        double[] numArray = new double[6];

        activeCamera.SetFocalPoint((numArray[0] + numArray[1]) / 2.0, (numArray[2] + numArray[3]) / 2.0, (numArray[4] + numArray[5]) / 2.0);
        activeCamera.SetPosition((numArray[0] + numArray[1]) / 2.0, 1000.0, (numArray[4] + numArray[5]) / 2.0);
        activeCamera.SetViewUp(0.0, 0.0, -1.0);
    }

    public void 正面观(double[] numArray)
    {
        vtkCamera activeCamera = this._renderer.GetActiveCamera();
        //double[] numArray = new double[6];

        activeCamera.SetFocalPoint((numArray[0] + numArray[1]) / 2.0, (numArray[2] + numArray[3]) / 2.0, (numArray[4] + numArray[5]) / 2.0);
        activeCamera.SetPosition((numArray[0] + numArray[1]) / 2.0, 300.0, (numArray[4] + numArray[5]) / 2.0);
        activeCamera.SetViewUp(0.0, 0.0, -1.0);
    }

    public void 轴位()
    {
        vtkCamera activeCamera = this._renderer.GetActiveCamera();
        activeCamera.SetFocalPoint(0.0, 0.0, 0.0);
        activeCamera.SetPosition(0.0, 0.0, 1000.0);
        activeCamera.SetViewUp(0.0, 1.0, 0.0);
    }

    
    public void FastScreenShot1(String bmpSavedPath) throws IOException
    {
    	vtkWindowToImageFilter windowToImageFilter = new vtkWindowToImageFilter();
    	windowToImageFilter.SetInput(_renwin);
    	windowToImageFilter.SetMagnification(3);
    	windowToImageFilter.SetInputBufferTypeToRGBA();
    	windowToImageFilter.ReadFrontBufferOff();
    	windowToImageFilter.Update();
    	vtkBMPWriter writer = new vtkBMPWriter();
    	writer.SetFileName(bmpSavedPath);
    	writer.SetInputConnection(windowToImageFilter.GetOutputPort());
    	writer.Write();
    	_renwin.Render();
    	_renderer.ResetCamera();
    	_renwin.Render();
    	_iren.Start();
    }
    
    
    
    
    // Properties
    public void FastScreenShot3(String bmpSavedPath) throws IOException
    {
        int imgWidth = 0, imgHeight = 0;
        imgWidth = _renwin.GetSize()[0];
        imgHeight = _renwin.GetSize()[1];

        vtkUnsignedCharArray data = new vtkUnsignedCharArray();
        _renwin.GetPixelData(0, 0, imgWidth - 1, imgHeight - 1, 0, data);
        
        byte[] vtkScreenBuffer = data.GetJavaArray();
            
        //_renwin.GetPixelData(id0, id1, id2, id3, id4, id5);
        
        File bmp = new File(bmpSavedPath);
        BufferedImage bi = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        for(int h=0;h<imgHeight;h++){
        	for(int w=0;w<imgWidth;w++){
        		
//        		if(vtkScreenBuffer[h*imgWidth*3+w*3]<0){
//        			System.out.print(h*imgWidth*3+w*3+":"+vtkScreenBuffer[h*imgWidth*3+w*3]+",");
//        			System.out.print(h*imgWidth*3+w*3+1+":"+vtkScreenBuffer[h*imgWidth*3+w*3+1]+",");
//        			System.out.print(h*imgWidth*3+w*3+2+":"+vtkScreenBuffer[h*imgWidth*3+w*3+2]+",");
//        			System.out.println();
//        		}
        		
        		Color color = new Color(vtkScreenBuffer[h*imgWidth*3+w*3]+128, vtkScreenBuffer[h*imgWidth*3+w*3+1]+128, vtkScreenBuffer[h*imgWidth*3+w*3+2]+128);
        		g2.setColor(color);
        		g2.drawLine(w, h, w+1, h+1);
        	}
        }
        try {
			ImageIO.write(bi, "bmp", bmp);
		} catch (IOException e) {
			e.printStackTrace();
		}
        vtkScreenBuffer = null;
        
      //Before is how to change ByteArray back to Image
//        ByteArrayInputStream bis = new ByteArrayInputStream(vtkScreenBuffer);
//        Iterator<?> readers = ImageIO.getImageReadersByFormatName("bmp");
//        //ImageIO is a class containing static convenience methods for locating ImageReaders
//        //and ImageWriters, and performing simple encoding and decoding. 
//         
//        ImageReader reader = (ImageReader) readers.next();
//        Object source = bis; // File or InputStream, it seems file is OK
//         
//        ImageInputStream iis = ImageIO.createImageInputStream(source);
//        //Returns an ImageInputStream that will take its input from the given Object
//         
//        reader.setInput(iis, true);
//        ImageReadParam param = reader.getDefaultReadParam();
//         
//        Image image = reader.read(0, param);
//        //got an image file
//         
//        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
//        //bufferedImage is the RenderedImage to be written
//        Graphics2D g2 = bufferedImage.createGraphics();
//        g2.drawImage(image, null, null);
//        File imageFile = new File(bmpSavedPath);
//        ImageIO.write(bufferedImage, "bmp", imageFile);
//        //"jpg" is the format of the image
//        //imageFile is the file to be written to.
//         
//        System.out.println(imageFile.getPath());


    
    }
}
