package starClasses;

import star.base.neo.DoubleVector;
import star.base.neo.IntVector;
import star.base.neo.NeoObjectVector;
import star.cadmodeler.CadModel;
import star.cadmodeler.CanonicalSketchPlane;
import star.cadmodeler.ExtrusionMerge;
import star.cadmodeler.PointSketchPrimitive;
import star.cadmodeler.Sketch;
import star.cadmodeler.SolidModelManager;
import star.cadmodeler.SolidModelPart;
import star.common.BrickVolumeShape;
import star.common.LabCoordinateSystem;
import star.common.PartSurface;
import star.common.Simulation;
import star.common.SimulationPartManager;
import star.common.Units;
import star.common.VolumeShapeManager;
import star.meshing.LeafMeshPart;
import star.meshing.MeshOperationManager;
import star.meshing.MeshOperationPart;
import star.meshing.SubtractPartsOperation;

/** This class creates geometry for a Star CCM+ simulation
 * 
 * @author cj8q5
 *
 */
public class GeometryBuilder
{
	private Simulation m_sim;
	private String m_partName;
	private CadModel m_cadModel;
	private CanonicalSketchPlane m_sketchingPlane;
	private Units m_units;
	
	public GeometryBuilder(Simulation sim, String partName, String sketchPlane)
	{
		m_sim = sim;
		m_partName = partName;
		m_cadModel = m_sim.get(SolidModelManager.class).createSolidModel();
		m_cadModel.setPresentationName(m_partName);
		m_sketchingPlane = ((CanonicalSketchPlane) m_cadModel.getFeatureManager().getObject(sketchPlane));
		m_units = m_sim.getUnitsManager().getPreferredUnits(
				new IntVector(
						new int[] {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));

	}
	
	/** This method builds a 3D model of a rectangular box and makes it a part in the geometry tree 
	 * 
	 * @param sketchPlane is the plane to sketch on either XY, XZ, or YZ
	 * @param X vector of two x coordinates one for the lower left corner and one for the upper right corner
	 * @param Y vector of two y coordinates one for the lower left corner and one for the upper right corner
	 * @param Z value for the length of the extrusion
	 * @param partName name of the part
	 */
	public void boxBuilder(double[] X, double[] Y, double Z) 
	{
	    // Sketching the rectangle
	    Sketch sketch = m_cadModel.getFeatureManager().createSketch(m_sketchingPlane);
	    m_cadModel.getFeatureManager().startSketchEdit(sketch);
	    sketch.createRectangle(new DoubleVector(new double[] {X[0], Y[0]}), new DoubleVector(new double[] {X[1], Y[1]}));
	    m_cadModel.getFeatureManager().stopSketchEdit(sketch, true);

	    // Extruding the sketched rectangle
	    ExtrusionMerge extrusionMerge = m_cadModel.getFeatureManager().createExtrusionMerge(sketch);
	    extrusionMerge.getDistance().setValue(Z);
	    
	    m_cadModel.getFeatureManager().execute(extrusionMerge);
	    m_cadModel.createParts("SharpEdges", 30.0, 2, true);
	    		    
	    SolidModelPart solidModelPart = ((SolidModelPart) m_sim.get(SimulationPartManager.class).getPart("Body 1"));
	    solidModelPart.setPresentationName(m_partName);
	}//end method partBuilder
	
	/** This method creates a rectangular part with a void through the center
	 * 
	 * @param sketchPlane is the plane to be sketched on (XY, YZ, or XZ)
	 * @param X array of x coordinates the first two values create the outer rectangle and the second two create the inner rectangle
	 * @param Y array of y coordinates the first two values create the outer rectangle and the second two create the inner rectangle 
	 * @param Z value for the size of the extrusion
	 * @param partName name of the new part
	 */
	public void boxWithVoidBuilder(double[] X, double[] Y, double Z)
	{
		// Sketching the rectangles
		Sketch sketch = m_cadModel.getFeatureManager().createSketch(m_sketchingPlane);
		m_cadModel.getFeatureManager().startSketchEdit(sketch);
	    sketch.createRectangle(new DoubleVector(new double[] {X[0], Y[0]}), new DoubleVector(new double[] {X[1], Y[1]}));
	    sketch.createRectangle(new DoubleVector(new double[] {X[2], Y[2]}), new DoubleVector(new double[] {X[3], Y[3]}));
	    m_cadModel.getFeatureManager().stopSketchEdit(sketch, true);
	    
	    // Extruding the sketched rectangle
	    ExtrusionMerge extrusionMerge = m_cadModel.getFeatureManager().createExtrusionMerge(sketch);
	    extrusionMerge.getDistance().setValue(Z);
	    
	    m_cadModel.getFeatureManager().execute(extrusionMerge);
	    m_cadModel.createParts("SharpEdges", 30.0, 2, true);
	    		    
	    SolidModelPart solidModelPart = ((SolidModelPart) m_sim.get(SimulationPartManager.class).getPart("Body 1"));
	    solidModelPart.setPresentationName(m_partName);
	}
	
	/**
	 * This method creates a cylinder part
	 * @param sketchPlane	the sketch plane will be perpendicular to the axial direction of the cylinder
	 * @param pipeCenter	the coordinates for the center of the pipe
	 * @param radius	the radius of the cylinder
	 * @param pipeLength	the length of the cylinder
	 */
	public void cylinderBuilder(double[] pipeCenter, double radius, double pipeLength)
	{
		Sketch sketch = m_cadModel.getFeatureManager().createSketch(m_sketchingPlane);
		m_cadModel.getFeatureManager().startSketchEdit(sketch);
		sketch.createCircle(new DoubleVector(pipeCenter), radius);
		m_cadModel.getFeatureManager().stopSketchEdit(sketch, true);
		
		ExtrusionMerge extrusionMerge = m_cadModel.getFeatureManager().createExtrusionMerge(sketch);
		extrusionMerge.getDistance().setValue(pipeLength);
		
		m_cadModel.setPresentationName(m_partName);
		m_cadModel.getFeatureManager().execute(extrusionMerge);
		m_cadModel.createParts("SharpEdges", 30.0, 2, true);
		
		SolidModelPart solidModelPart = ((SolidModelPart) m_sim.get(SimulationPartManager.class).getPart("Body 1"));
		solidModelPart.setPresentationName(m_partName);
	}
	
	/**
	 * This method creates a box with an arc on the left side, useful for airfoil bulk fluid
	 * @param inletCircleDiameter	diameter of the inlet arc
	 * @param outletBoxLength	length of the outlet box
	 */
	public void airfoilBulkFluidBuilder(double inletCircleDiameter, double outletBoxLength, double extrudeLength)
	{
		Sketch sketch = m_cadModel.getFeatureManager().createSketch(m_sketchingPlane);
		m_cadModel.getFeatureManager().startSketchEdit(sketch);
		
		// Creating the outlet box
		sketch.createLine(new DoubleVector(new double[] {0.0, -inletCircleDiameter}), 
				new DoubleVector(new double[] {outletBoxLength, -inletCircleDiameter}));
		
		sketch.createLine(new DoubleVector(new double[] {outletBoxLength, -inletCircleDiameter}),
				new DoubleVector(new double[] {outletBoxLength, inletCircleDiameter}));
		
		sketch.createLine(new DoubleVector(new double[] {outletBoxLength, inletCircleDiameter}),
				new DoubleVector(new double[] {0.0, inletCircleDiameter}));
		
		// Creating the circular inlet arc
		sketch.createCircularArc(new DoubleVector(new double[] {0.0, 0.0}), 
				new DoubleVector(new double[] {0.0, inletCircleDiameter}), 
				new DoubleVector(new double[] {0.0, -inletCircleDiameter}));
	    
	    m_cadModel.getFeatureManager().stopSketchEdit(sketch, true);
	    
	    // Creating the extruded length of the fluid
	    ExtrusionMerge extrusionMerge = m_cadModel.getFeatureManager().createExtrusionMerge(sketch);
		extrusionMerge.getDistance().setValue(extrudeLength);
		
		m_cadModel.setPresentationName(m_partName);
		m_cadModel.getFeatureManager().execute(extrusionMerge);
		m_cadModel.createParts("SharpEdges", 30.0, 2, true);
		
		SolidModelPart solidModelPart = ((SolidModelPart) m_sim.get(SimulationPartManager.class).getPart("Body 1"));
		solidModelPart.setPresentationName(m_partName);
	}
	
	/** 
	 * This method creates the sketch
	 * @return
	 */
	public Sketch createSketch()
	{
		Sketch sketch = m_cadModel.getFeatureManager().createSketch(m_sketchingPlane);
		return sketch;
	}
	
	/** 
	 * This method creates a sketch point on the sketch
	 * @param pointcoordinates
	 * @param sketchPlane
	 */
	public PointSketchPrimitive createSketchPoint(Sketch sketch, double[] pointcoordinates)
	{
		// Creating sketch point
		PointSketchPrimitive pointSketchPrimitive = sketch.createPoint(new DoubleVector(pointcoordinates));
		return pointSketchPrimitive;
	}
	
	/** 
	 * This method creates a spline around specified points
	 * @param sketch
	 * @param xySplineCoordinates
	 * @param firstPoint
	 * @param secondPoint
	 */
	public void createSpline(Sketch sketch, double[] xySplineCoordinates, PointSketchPrimitive firstPoint, PointSketchPrimitive secondPoint)
	{
		// Creating the spline
		sketch.createSpline(true, firstPoint, false, secondPoint, new DoubleVector(xySplineCoordinates));
	}
	/** 
	 * This method creates an extrude and creates a part given a sketch
	 * @param sketch
	 * @param extrudedLength
	 */
	public void createPart(Sketch sketch, double extrudedLength)
	{
		ExtrusionMerge extrusionMerge = m_cadModel.getFeatureManager().createExtrusionMerge(sketch);
		extrusionMerge.getDistance().setValue(extrudedLength);
		m_cadModel.setPresentationName(m_partName);
		m_cadModel.getFeatureManager().execute(extrusionMerge);
		m_cadModel.createParts("SharpEdges", 30.0, 2, true);
		
		SolidModelPart solidModelPart = ((SolidModelPart) m_sim.get(SimulationPartManager.class).getPart("Body 1"));
		solidModelPart.setPresentationName(m_partName);
	}
	
	/** 
	 * This method translates a part in the simulation
	 * @param partName name of the part that is to be translated
	 * @param X distance to translate in the x direction
	 * @param Y distance to translate in the y direction
	 * @param Z distance to translate in the z direction
	 */
	public void partTranslate(double[] xyzCoords)
	{
		SolidModelPart solidModelPart = ((SolidModelPart) m_sim.get(SimulationPartManager.class).getPart(m_partName));

		LabCoordinateSystem labCoordinateSystem = m_sim.getCoordinateSystemManager().getLabCoordinateSystem();

		m_sim.get(SimulationPartManager.class).translateParts(new NeoObjectVector(new Object[] {solidModelPart}), 
			    		new DoubleVector(xyzCoords), 
			    		new NeoObjectVector(new Object[] {m_units, m_units, m_units}), labCoordinateSystem);
	}//end method partTranslate
	
	/** 
	 * This method subtracts one part from the other
	 * @param partBase name of the part that is to be subtracted from
	 * @param partSubtract name of the part that is subtracting from the base part
	 * @param newPartName name of the new subtracted part
	 */
	public void partSubtract(String partBase, String partSubtract, String newPartName)
	{
		SolidModelPart solidPartBase = ((SolidModelPart) m_sim.get(SimulationPartManager.class).getPart(partBase));
	    SolidModelPart solidPartSubtract = ((SolidModelPart) m_sim.get(SimulationPartManager.class).getPart(partSubtract));

	    SubtractPartsOperation subtractPartsOperation = (SubtractPartsOperation) m_sim.get(MeshOperationManager.class).createSubtractPartsOperation();
	    subtractPartsOperation.getInputGeometryObjects().setObjects(solidPartBase, solidPartSubtract);
	    subtractPartsOperation.setMergePartSurfaces(false);
	    subtractPartsOperation.setMergePartCurves(true);
	    subtractPartsOperation.setTargetPart(solidPartBase);
	    subtractPartsOperation.execute();
	    
	    MeshOperationPart subtractedPart = ((MeshOperationPart) m_sim.get(SimulationPartManager.class).getPart("Subtract"));
	    subtractedPart.detach();
	    
	    LeafMeshPart leafMeshPart = ((LeafMeshPart) m_sim.get(SimulationPartManager.class).getPart("Subtract"));
	    leafMeshPart.setPresentationName(newPartName);
	}//end method partSubtract
	
	/**
	 * This method rotates the part by a specified angle
	 * @param rotationAngle	angle in radian by which the part will be rotated
	 * @param rotationAxis	the axis the part will rotate on
	 */
	public void partRotate(double rotationAngle, double[] rotationAxis)
	{
	    SolidModelPart solidModelPart = ((SolidModelPart) m_sim.get(SimulationPartManager.class).getPart(m_partName));

	    LabCoordinateSystem labCoordinateSystem = m_sim.getCoordinateSystemManager().getLabCoordinateSystem();

	    // Rotating the part
	    m_sim.get(SimulationPartManager.class).rotateParts(
	    		new NeoObjectVector(new Object[] {solidModelPart}), 
	    		new DoubleVector(rotationAxis), 
	    		new NeoObjectVector(new Object[] {m_units, m_units, m_units}), 
	    		rotationAngle, labCoordinateSystem);
	}
	
	/**
	 *  This method splits the surface of a part by an angle and names them
	 * @param partName name of the part where its surfaces will be split
	 * @param angle value of the angle to be used when splitting the surfaces
	 * @param surfaceNames vector of surface names that will be used to name the new surfaces
	 * @param partType true if the part is a solid model part and false if the part is a leaf mesh part created from a subtract operation
	 */
	public void splitSurface(double angle, String[] surfaceNames, boolean partType)
	{
		if (partType == true)
		{
			SolidModelPart part2Split = ((SolidModelPart) m_sim.get(SimulationPartManager.class).getPart(m_partName));
			PartSurface splitSurface = part2Split.getPartSurfaceManager().getPartSurface("Default");
		    
		    part2Split.getPartSurfaceManager().splitPartSurfacesByAngle(new NeoObjectVector(new Object[] {splitSurface}), angle);
		    splitSurface.setPresentationName("Default 1");
		    
		    for (int i = 0; i < surfaceNames.length; i++)
		    {
		    	PartSurface surface = part2Split.getPartSurfaceManager().getPartSurface("Default " + (i + 1));
		    	surface.setPresentationName(surfaceNames[i]);
		    }//end for loop
		}
		if (partType == false)
		{
			LeafMeshPart part2Split = ((LeafMeshPart) m_sim.get(SimulationPartManager.class). getPart(m_partName));
			PartSurface splitSurface = part2Split.getPartSurfaceManager().getPartSurface("Default");
		    
		    part2Split.getPartSurfaceManager().splitPartSurfacesByAngle(new NeoObjectVector(new Object[] {splitSurface}), angle);
		    splitSurface.setPresentationName("Default 1");
		    
		    for (int i = 0; i < surfaceNames.length; i++)
		    {
		    	PartSurface surface = part2Split.getPartSurfaceManager().getPartSurface("Default " + (i + 1));
		    	surface.setPresentationName(surfaceNames[i]);
		    }//end for loop
		}
	    
	}//end method splitSurface
	
	/**
	 * This method creates a volume shape block 
	 * @param coordinate_0	bottom corner of the block
	 * @param coordinate_1	top corner of the block
	 */
	public BrickVolumeShape createVolumeShapeBlock(double[] coordinate_0, double[] coordinate_1)
	{
		BrickVolumeShape brickVolumeShape_0 = (BrickVolumeShape) m_sim.get(VolumeShapeManager.class).createBrickVolumeShape();
		
	    brickVolumeShape_0.getCorner1().setCoordinate(m_units, m_units, m_units, new DoubleVector(coordinate_0));
	    brickVolumeShape_0.getCorner2().setCoordinate(m_units, m_units, m_units, new DoubleVector(coordinate_1));
	    return brickVolumeShape_0;
	}

}
