package starClasses;

import star.base.neo.DoubleVector;
import star.base.neo.NeoObjectVector;
import star.base.neo.StringVector;
import star.cadmodeler.SolidModelPart;
import star.common.PartSurface;
import star.common.Simulation;
import star.common.SimulationPartManager;
import star.meshing.MeshOperationManager;
import star.sweptmesher.DirectedMeshOperation;
import star.sweptmesher.DirectedMeshPartCollection;
import star.sweptmesher.DirectedPatchSourceMesh;
import star.sweptmesher.DirectedSideMeshDistribution;
import star.sweptmesher.PatchCurve;
import star.sweptmesher.PatchVertex;
import star.sweptmesher.StretchingFunction;
import star.vis.Scene;

/** This class creates directed meshes on rectangular parts
 * 
 * @author cj8q5
 *
 */

public class DirectedMesher8_02_008 
{
	private Simulation m_sim = null;
	private Scene m_scene = null;
	private SolidModelPart m_part2Mesh = null;
	private DirectedMeshOperation m_directedMeshOperation = null;
	private DirectedMeshPartCollection m_directedMeshPartCollection = null;
	private DirectedPatchSourceMesh m_directedPatchSourceMesh = null;
	private PatchCurve m_patchCurveX = null;
	private PatchCurve m_patchCurveY = null;
	
	@SuppressWarnings("unchecked")
	public DirectedMesher8_02_008(Simulation sim, String partName)
	{
		m_sim = sim;
		
		m_scene = sim.getSceneManager().createScene("Directed Mesh");

	    m_part2Mesh = ((SolidModelPart) sim.get(SimulationPartManager.class).getPart(partName));

	    m_directedMeshOperation = (DirectedMeshOperation) sim.get(MeshOperationManager.class).
	    		createDirectedMeshOperation(m_scene, new NeoObjectVector(new Object[] {m_part2Mesh}));
	    
	    m_directedMeshPartCollection = 
	    		((DirectedMeshPartCollection) m_directedMeshOperation.getGuidedMeshPartCollectionManager().getObject(partName));
	}
	
	@SuppressWarnings("unchecked")
	public void setSourceTargetSurfaces(String sourceSurf, String targetSurf)
	{
	    // Setting the target and source surfaces
	    PartSurface sourceSurface = m_part2Mesh.getPartSurfaceManager().getPartSurface(sourceSurf);
	    m_directedMeshOperation.getSourceSurfaceGroup().setObjects(sourceSurface);
	    
	    PartSurface targetSurface = m_part2Mesh.getPartSurfaceManager().getPartSurface(targetSurf);
	    m_directedMeshOperation.getTargetSurfaceGroup().setObjects(targetSurface);
	    
	    m_directedMeshOperation.getGuidedSurfaceMeshBaseManager().validateConfigurationForPatchMeshCreation(m_directedMeshPartCollection, 
	    		new NeoObjectVector(new Object[] {sourceSurface}), new NeoObjectVector(new Object[] {targetSurface}));
	    m_directedMeshOperation.getGuidedSurfaceMeshBaseManager().createPatchSourceMesh(
	    		new NeoObjectVector(new Object[] {sourceSurface}), m_directedMeshPartCollection);
	}
	
	/** 
	 * This method creates a patch mesh for the directed mesher and autopopulates the feature edges of the model
	 */
	public void createPatchMesh()
	{
		// Creating the patch mesh
		m_directedPatchSourceMesh = ((DirectedPatchSourceMesh) m_directedMeshOperation.getGuidedSurfaceMeshBaseManager().getObject("Patch Mesh"));
		
		// Auto populating the edges of the box
		m_directedPatchSourceMesh.autopopulateFeatureEdges();
	}
	
	/** 
	 * This method splits a specified patch curve at a specified location
	 * 
	 * @param patchCurveName	name of the patch curve to be split
	 * @param patchCurveSplitLocation X, Y, and Z coordinates of the split (must be along the line that the patch curve lies)
	 */
	public void splitPatchCurve(int patchCurveNumber, double[] patchCurveSplitLocation)
	{
		// Getting the patch curve from the model that will split into two new patch curves
		PatchCurve patchCurve = ((PatchCurve) m_directedPatchSourceMesh.getPatchCurve(patchCurveNumber));
		
		// Splitting the patch curve into two new patch curves
		m_directedPatchSourceMesh.splitPatchCurve(patchCurve, new DoubleVector(patchCurveSplitLocation));
	}
	
	/** 
	 * This method creates a patch curve between two patch vertexes
	 * 
	 * @param patchCurve_0	the first vertex point of the patch curve
	 * @param patchCurve_1	the second vertex point of the patch curve
	 */
	public void createPatchCurve(int patchVertexNumber_0, Integer patchVertexNumber_1)
	{
		// Getting the two patch vertexes
		PatchVertex patchVertex_0 = ((PatchVertex) m_directedPatchSourceMesh.getPatchVertex(patchVertexNumber_0));
		PatchVertex patchVertex_1 = ((PatchVertex) m_directedPatchSourceMesh.getPatchVertex(patchVertexNumber_1));
		
		// Creating the patch curve between the two points
		m_directedPatchSourceMesh.createPatchCurve(patchVertex_0, patchVertex_1, new DoubleVector(new double[] {}), new StringVector(new String[] {}));
		m_directedPatchSourceMesh.initializePatchCurveCreation();
	}
	
	/**
	 * This method sets the mesh settings of a specified patch curve
	 * 
	 * @param patchCurveNumber
	 * @param numberOfDivisions
	 * @param stretchingFunctionSide_0
	 * @param stretchingFunctionSide_1
	 * @param isDirectionReversed
	 */
	public void setPatchCurveParameters(int patchCurveNumber, int numberOfDivisions, 
			double stretchingFunctionSide_0, double stretchingFunctionSide_1, boolean isDirectionReversed, String stretchingFunction)
	{
		m_directedPatchSourceMesh.rebuildPatchPolygonSourceMesh(true);
		PatchCurve patchCurve = ((PatchCurve) m_directedPatchSourceMesh.getPatchCurve(patchCurveNumber));
		
		if(stretchingFunction == "Two Sided Hyperbolic")
		{
			patchCurve.getStretchingFunction().setSelected(StretchingFunction.TWO_SIDED_HYPERBOLIC);
		}
		
		if(stretchingFunction == "Constant")
		{
			patchCurve.getStretchingFunction().setSelected(StretchingFunction.CONSTANT);
		}
	    m_directedPatchSourceMesh.defineMeshPatchCurve(patchCurve, patchCurve.getStretchingFunction(), 
	    		stretchingFunctionSide_0, stretchingFunctionSide_1, 
	    		numberOfDivisions, isDirectionReversed, false);
	}
	
	public void definePatchCurveParameters(int cellX, int cellY, double spacingX, double spacingY, 
			String stretchingFnX, String stretchingFnY, boolean isDirectionReversedX, boolean isDirectedReservedY)
	{
		m_directedPatchSourceMesh = ((DirectedPatchSourceMesh) m_directedMeshOperation.getGuidedSurfaceMeshBaseManager().getObject("Patch Mesh"));
		
		// Auto populating the edges of the box
		m_directedPatchSourceMesh.autopopulateFeatureEdges();
		m_patchCurveX = ((PatchCurve) m_directedPatchSourceMesh.getPatchCurveManager().getObject("PatchCurve 0"));
		m_patchCurveY = ((PatchCurve) m_directedPatchSourceMesh.getPatchCurveManager().getObject("PatchCurve 2"));
		
		// Defining the number of Cells in the Y direction 
	    if (stretchingFnY == "One")
	    {   
	    	m_patchCurveY.getStretchingFunction().setSelected(StretchingFunction.ONE_SIDED_HYPERBOLIC);
	    	m_directedPatchSourceMesh.defineMeshPatchCurve(m_patchCurveY, m_patchCurveY.getStretchingFunction(), spacingY, spacingY, cellY, isDirectedReservedY, true);
	    }
	    if (stretchingFnY == "Two")
	    {
	    	m_patchCurveY.getStretchingFunction().setSelected(StretchingFunction.TWO_SIDED_HYPERBOLIC);
	    	m_directedPatchSourceMesh.defineMeshPatchCurve(m_patchCurveY, m_patchCurveY.getStretchingFunction(), spacingY, spacingY, cellY, isDirectedReservedY, true);
	    }
	    if (stretchingFnY == "Constant")
	    {
	    	m_patchCurveY.getStretchingFunction().setSelected(StretchingFunction.CONSTANT);
		    m_directedPatchSourceMesh.defineMeshPatchCurve(m_patchCurveY, m_patchCurveY.getStretchingFunction(), spacingY, spacingY, cellY, isDirectedReservedY, false);
	    }
		
	    // Defining the number of Cells in the X direction
    	if (stretchingFnX == "One")
    	{
    		m_patchCurveX.getStretchingFunction().setSelected(StretchingFunction.ONE_SIDED_HYPERBOLIC);
    		m_directedPatchSourceMesh.defineMeshPatchCurve(m_patchCurveX, m_patchCurveX.getStretchingFunction(), spacingX, spacingX, cellX, isDirectionReversedX, true);
    		
    	}
    	if (stretchingFnX == "Two")
    	{
    		m_patchCurveX.getStretchingFunction().setSelected(StretchingFunction.TWO_SIDED_HYPERBOLIC);
    		m_directedPatchSourceMesh.defineMeshPatchCurve(m_patchCurveX, m_patchCurveX.getStretchingFunction(), spacingX, spacingX, cellX, isDirectionReversedX, true);
    	}
    	if (stretchingFnX == "Constant")
    	{
    		m_patchCurveX.getStretchingFunction().setSelected(StretchingFunction.CONSTANT);
    	    m_directedPatchSourceMesh.defineMeshPatchCurve(m_patchCurveX, m_patchCurveX.getStretchingFunction(), spacingX, spacingX, cellX, isDirectionReversedX, false);
    	}
    	
    	// Ending the patch mesh
	    m_directedPatchSourceMesh.stopEditPatchOperation();
	}
	
	@SuppressWarnings("unchecked")
	public void createDirectedVolumeMesh(int cellExtrude)
	{
		m_directedMeshOperation.getGuidedSurfaceMeshBaseManager().createGuidedSideMeshDistribution(new NeoObjectVector(new Object[] {m_directedMeshPartCollection}));

		// Creating the volume mesh and setting the number of cells in the extruded direction
	    DirectedSideMeshDistribution directedSideMeshDistribution_0 = ((DirectedSideMeshDistribution) m_directedMeshOperation.getGuidedSurfaceMeshBaseManager().getObject("Volume Distribution"));
	    directedSideMeshDistribution_0.setNumLayers(cellExtrude);
	    m_directedMeshOperation.generateVolumeMeshOnPartCollections(new NeoObjectVector(new Object[] {m_directedMeshPartCollection}));
		
		m_directedMeshOperation.stopEditingDirectedMeshOperation();
		m_sim.getSceneManager().deleteScenes(new NeoObjectVector(new Object[] {m_scene}));
	}
	
	@SuppressWarnings("unchecked")
	public void stopDirectedMesher()
	{
		// Ending the patch mesh
	    m_directedPatchSourceMesh.stopEditPatchOperation();
	    
	    m_directedMeshOperation.stopEditingDirectedMeshOperation();
		m_sim.getSceneManager().deleteScenes(new NeoObjectVector(new Object[] {m_scene}));
	}
}
