import vtk.vtkActor;
import vtk.vtkCleanPolyData;
import vtk.vtkCurvatures;
import vtk.vtkDecimatePro;
import vtk.vtkIterativeClosestPointTransform;
import vtk.vtkLookupTable;
import vtk.vtkObject;
import vtk.vtkPLYReader;
import vtk.vtkPLYWriter;
import vtk.vtkPolyData;
import vtk.vtkPolyDataMapper;
import vtk.vtkRenderWindow;
import vtk.vtkRenderWindowInteractor;
import vtk.vtkRenderer;
import vtk.vtkSmoothPolyDataFilter;
import vtk.vtkSphereSource;
import vtk.vtkSuperquadricSource;
import vtk.vtkTriangleFilter;

public class Test {

	static {
		System.loadLibrary("vtkCommonJava");
		System.loadLibrary("vtkFilteringJava");
		System.loadLibrary("vtkIOJava");
		System.loadLibrary("vtkImagingJava");
		System.loadLibrary("vtkGraphicsJava");
		System.loadLibrary("vtkRenderingJava");
		System.loadLibrary("vtkHybridJava");
	}

	public static void main(String[] args) {
		// vtkSphereSource sphere = new vtkSphereSource();
		// sphere.SetRadius(1.0);
		// sphere.SetThetaResolution(18);
		// sphere.SetPhiResolution(18);
		//
		// vtkPolyDataMapper map = new vtkPolyDataMapper();
		// map.SetInput(sphere.GetOutput());
		//
		// vtkActor aSphere = new vtkActor();
		// aSphere.SetMapper(map);
		// aSphere.GetProperty().SetColor(0, 0, 1);
		//
		// vtkSuperquadricSource torus = new vtkSuperquadricSource();
		// torus.SetCenter(0, 0, 0);
		// torus.SetPhiResolution(64);
		// torus.SetThetaResolution(64);
		// torus.SetToroidal(1);
		//
		//
		// vtkTriangleFilter filtreTriangle = new vtkTriangleFilter();
		// filtreTriangle.SetInput(torus.GetOutput());
		//
		// vtkCleanPolyData clean = new vtkCleanPolyData();
		// clean.SetTolerance(0.005);
		// clean.SetInput(filtreTriangle.GetOutput());
		//
		// vtkLookupTable lookupTable = new vtkLookupTable();
		//
		// vtkCurvatures courbes = new vtkCurvatures();
		// courbes.SetCurvatureTypeToMean();
		// courbes.SetInput(clean.GetOutput());
		//
		// vtkPolyDataMapper map = new vtkPolyDataMapper();
		// map.SetInput(courbes.GetOutput());
		// map.SetLookupTable(lookupTable);
		//
		// vtkActor aTorus = new vtkActor();
		// aTorus.GetProperty().SetColor(0, 0.8, 1);
		// aTorus.SetPosition(-1, 0, 0);
		// aTorus.SetMapper(map);
		//
		// vtkActor aTorus2 = new vtkActor();
		// aTorus2.GetProperty().SetColor(0, 0.8, 1);
		// aTorus2.SetPosition(1, 0, 0);
		// aTorus2.SetMapper(map);

		// vtkPLYReader reader = new vtkPLYReader();
		// reader.SetFileName("./cow.ply");
		// vtkPolyData cow = reader.GetOutput();
		//
		// vtkDecimatePro filterDecimate = new vtkDecimatePro();
		// filterDecimate.SetInput(cow);
		//
		// vtkSmoothPolyDataFilter filterSmooth = new vtkSmoothPolyDataFilter();
		// filterSmooth.SetInput(filterDecimate.GetOutput());
		//
		// vtkPolyDataMapper map = new vtkPolyDataMapper();
		// map.SetInput(filterSmooth.GetOutput());
		//
		// vtkPLYWriter writer = new vtkPLYWriter();
		// writer.SetInput(filterSmooth.GetOutput());
		// writer.Write();
		//
		// vtkActor aCow = new vtkActor();
		vtkPLYReader reader = new vtkPLYReader();
		reader.SetFileName("./Model1.ply");
		vtkPolyData model1 = reader.GetOutput();
		reader.SetFileName("./Model2.ply");
		vtkPolyData model2 = reader.GetOutput();

		vtkPolyDataMapper map1 = new vtkPolyDataMapper();
		map1.SetInput(model1);
		vtkPolyDataMapper map2 = new vtkPolyDataMapper();
		map2.SetInput(model2);
		
		vtkActor aM1 = new vtkActor();
		aM1.GetProperty().SetColor(0.3, 0.3, 0.3);
		aM1.SetMapper(map1);
		vtkActor aM2 = new vtkActor();
		aM2.GetProperty().SetColor(0.8, 0.1, 0.1);
		aM2.SetMapper(map2);
		
		vtkIterativeClosestPointTransform icpt = new vtkIterativeClosestPointTransform();

		vtkRenderer ren1 = new vtkRenderer();
		// ren1.AddActor(aTorus);
		// ren1.AddActor(aTorus2);
		ren1.AddActor(aM1);
		ren1.AddActor(aM2);
		ren1.SetBackground(1, 1, 1);

		vtkRenderWindow renWin = new vtkRenderWindow();
		renWin.AddRenderer(ren1);
		renWin.SetSize(300, 300);

		vtkRenderWindowInteractor iren = new vtkRenderWindowInteractor();
		iren.SetRenderWindow(renWin);

		renWin.Render();
		iren.Start();

	}
}
