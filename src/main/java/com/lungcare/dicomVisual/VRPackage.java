package com.lungcare.dicomVisual;

import vtk.vtkColorTransferFunction;
import vtk.vtkGPUVolumeRayCastMapper;
import vtk.vtkImageData;
import vtk.vtkPiecewiseFunction;
import vtk.vtkVolume;
import vtk.vtkVolumeProperty;

public class VRPackage {

    // Fields
    private vtkColorTransferFunction _colorFun;
    private vtkPiecewiseFunction _opacityFun;
    private vtkVolumeProperty _property;
    vtkVolume _volume;
    vtkGPUVolumeRayCastMapper _volumeMapper;



    public VRPackage(RendererPackage rendererPackage, double opacityWindow, double opacityLevel, vtkImageData imageData)
    {
        this._volumeMapper =new vtkGPUVolumeRayCastMapper();
        //this._volumeMapper = this._volumeMapper;
        this._volumeMapper.SetBlendModeToComposite();
        this._volumeMapper.SetInput(imageData);
        this._volumeMapper.SetMaxMemoryInBytes(1024*1024*400);
        this._colorFun =new vtkColorTransferFunction();
        this._opacityFun =new vtkPiecewiseFunction();
        this._property =new vtkVolumeProperty();
        this._property.SetColor(this._colorFun);
        this._property.SetScalarOpacity(this._opacityFun);
        this._property.SetInterpolationTypeToNearest();
        this._volume =new vtkVolume();
        //this._volume = this._volume;
        this._volume.SetProperty(this._property);
        this._volume.SetMapper(this._volumeMapper);
        this.UpdateWWWL(opacityWindow, opacityLevel);
        this._volumeMapper.SetBlendModeToComposite();
        rendererPackage._renderer.AddViewProp(this._volume);
        rendererPackage.ResetCamera();
    }


    public void Dispose() {
        _colorFun.Delete();
        _opacityFun.Delete();
        _property.Delete();
        _volumeMapper.Delete();
        _volume.Delete();
    }


    public void Switch2MaxIP()
    {
        this._volumeMapper.SetBlendModeToMaximumIntensity();
    }

    public void Switch2MinIP()
    {
        this._volumeMapper.SetBlendModeToMinimumIntensity();
    }

    public void Switch2VR()
    {
        this._volumeMapper.SetBlendModeToComposite();
    }

    void UpdateWWWL(double opacityWindow, double opacityLevel)
    {
        this._colorFun.RemoveAllPoints();
        this._opacityFun.RemoveAllPoints();
        this._colorFun.AddRGBSegment(opacityLevel - (0.5 * opacityWindow), 0.0, 0.0, 0.0, opacityLevel + (0.5 * opacityWindow), 1.0, 1.0, 1.0);
        this._opacityFun.AddSegment(opacityLevel - (0.5 * opacityWindow), 0.0, opacityLevel + (0.5 * opacityWindow), 1.0);
    }

    public void VisOff()
    {
        this._volume.VisibilityOff();
    }

    public void VisOn()
    {
        this._volume.VisibilityOn();
    }
}
