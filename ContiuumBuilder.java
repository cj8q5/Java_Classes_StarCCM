package starClasses;

import star.base.neo.NeoObjectVector;
import star.common.ConstantVectorProfileMethod;
import star.common.MeshContinuum;
import star.common.MeshManager;
import star.common.PhysicsContinuum;
import star.common.Region;
import star.common.Simulation;
import star.coupledflow.CoupledImplicitSolver;
import star.flow.VelocityProfile;
import star.meshing.AbsoluteMinimumSize;
import star.meshing.BaseSize;
import star.meshing.GenericAbsoluteSize;
import star.meshing.MeshPipelineController;
import star.meshing.RelativeOrAbsoluteOption;
import star.meshing.SurfaceSize;
import star.prismmesher.NumPrismLayers;
import star.prismmesher.PrismLayerStretching;
import star.prismmesher.PrismMesherModel;
import star.prismmesher.PrismThickness;
import star.trimmer.TrimmerMeshingModel;

/** This class contains methods for creating the physics and mesh continuum in the simulation
 * 
 * @author cj8q5
 *
 */
public class ContiuumBuilder
{
	private Simulation m_sim;
	
	public ContiuumBuilder(Simulation sim)
	{
		m_sim = sim;
	}
	
	 /** This creates a physics continua with a specified name 
	 * 
	 * @param name presentation name of the physics continua
	 */
	public PhysicsContinuum createPhysicsContinua(String name)
	{
	    PhysicsContinuum physics = m_sim.getContinuumManager().createContinuum(PhysicsContinuum.class);
	    physics.setPresentationName(name);
	    return physics;
	    
	}//end method createPhysicsContinua
	
	/**
	 * This changes the name of a physics continuum
	 * @param oldName
	 * @param newName
	 */
	public PhysicsContinuum setPhysicsName(String oldName, String newName)
	{
		PhysicsContinuum physics = (PhysicsContinuum) m_sim.getContinuumManager().getContinuum(oldName);
		physics.setPresentationName(newName);
		return physics;
	}
	
	/**
	 * This method sets the courant number for the coupled implicit solver
	 * @param courantNumber
	 */
	public void setCourantNumber(double courantNumber)
	{
		m_sim.getSolverManager().getSolver(CoupledImplicitSolver.class).setCFL(courantNumber);
	}
	
	/** This method sets the initial velocity conditions in the fluid domains 
	 * 
	 * @param continuumName name of the physics continua where inlet velocity will be set
	 * @param initialVel value for the initial velocity
	 */
	public void setInitialConditionsVel(PhysicsContinuum physics, double[] initialVel)
	{
		// Setting the initial condition of the velocity for the physics of the model
	    VelocityProfile velocity = physics.getInitialConditions().get(VelocityProfile.class);
	    velocity.getMethod(ConstantVectorProfileMethod.class).getQuantity().setComponents(initialVel[0], initialVel[1], initialVel[2]);
	}//end method setInitialConditions
	
	/** This method creates a mesh continua with a specified name 
	 * 
	 * @param name presentation name for the new mesh continua
	 */
	public void createMeshContinua(String name)
	{
		MeshContinuum meshContinuum = m_sim.getContinuumManager().createContinuum(MeshContinuum.class);
	    meshContinuum.setPresentationName(name);
	    
	}//end method createMeshContinua
	
	/**
	 * This method converts a 3d mesh to a 2d mesh
	 * @param regionName
	 * @param tolerance
	 */
	public void convertMeshTo2D(String regionName, double tolerance)
	{
		MeshManager meshManager = m_sim.getMeshManager();

	    Region region = m_sim.getRegionManager().getRegion(regionName);

	    meshManager.convertTo2d(tolerance, new NeoObjectVector(new Object[] {region}), true);
	}
	
	/** This method activates the trimmer mesh and sets its respective global mesh settings 
	 * 
	 * @param continuumName the name of the mesh continua where global mesh settings will be set
	 * @param baseSize value for the global base size
	 * @param minSurfSize value for the minimum surface size 
	 * @param targetSurfSize value for the target surface size
	 */
	public void activateTrimmerMesh(String continuumName, double baseSize, double minSurfSize, double targetSurfSize)
	{
		MeshContinuum trimMesh = ((MeshContinuum) m_sim.getContinuumManager().getContinuum(continuumName));
		trimMesh.enable(TrimmerMeshingModel.class);
		
		trimMesh.getReferenceValues().get(BaseSize.class).setValue(baseSize);
	    
	    SurfaceSize surfaceSize = trimMesh.getReferenceValues().get(SurfaceSize.class);
	    surfaceSize.getRelativeOrAbsoluteOption().setSelected(RelativeOrAbsoluteOption.ABSOLUTE);

	    AbsoluteMinimumSize absoluteMinimumSize_0 = surfaceSize.getAbsoluteMinimumSize();
	    absoluteMinimumSize_0.getValue().setValue(minSurfSize);
	    absoluteMinimumSize_0.getValue().setValue(targetSurfSize);
	    
	}//end method activateTrimmerMesh
	
	/** This method activates the prism layer mesh and sets its respective global mesh settings 
	 * 
	 * @param continuumName the mesh continua where the prism layer mesher will be activated
	 * @param layerStretching value for the stretching in the prism layer
	 * @param numLayer value for the number of prism layers 
	 * @param layerThickness value for the thickness of the first prism layer
	 */
	public void activatePrismMesh(String continuumName, double layerStretching, int numLayer, double layerThickness)
	{
		MeshContinuum prismMesh = ((MeshContinuum) m_sim.getContinuumManager().getContinuum(continuumName));
		prismMesh.enable(PrismMesherModel.class);
		
		PrismLayerStretching prismLayerStretching_0 = prismMesh.getReferenceValues().get(PrismLayerStretching.class);
	    prismLayerStretching_0.setStretching(layerStretching);

	    NumPrismLayers numPrismLayers_0 = prismMesh.getReferenceValues().get(NumPrismLayers.class);
	    numPrismLayers_0.setNumLayers(numLayer);

	    PrismThickness prismThickness_0 = prismMesh.getReferenceValues().get(PrismThickness.class);
	    prismThickness_0.getRelativeOrAbsoluteOption().setSelected(RelativeOrAbsoluteOption.ABSOLUTE);
	    
	    GenericAbsoluteSize genericAbsoluteSize_0 = ((GenericAbsoluteSize) prismThickness_0.getAbsoluteSize());
	    genericAbsoluteSize_0.getValue().setValue(layerThickness);
	    
	}//end method activatePrismMesh
	
	/** This method starts the volume mesher 
	 */
	public void startVolumeMesh()
	{
		MeshPipelineController meshPipelineController_0 = m_sim.get(MeshPipelineController.class);
	    meshPipelineController_0.generateVolumeMesh();
	    
	}//end method startVolumeMesh
			
}//end class ContiuumBuilder
