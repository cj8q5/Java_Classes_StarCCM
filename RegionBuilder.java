package starClasses;

import star.base.neo.*;
import star.cadmodeler.SolidModelPart;
import star.common.Boundary;
import star.common.ConstantScalarProfileMethod;
import star.common.ConstantVectorProfileMethod;
import star.common.DirectBoundaryInterface;
import star.common.InletBoundary;
import star.common.InterfaceConfigurationOption;
import star.common.MeshContinuum;
import star.common.PhysicsContinuum;
import star.common.PressureBoundary;
import star.common.Region;
import star.common.Simulation;
import star.common.SimulationPartManager;
import star.common.SymmetryBoundary;
import star.energy.HeatFluxProfile;
import star.energy.WallThermalOption;
import star.flow.FlowDirectionOption;
import star.flow.FlowDirectionProfile;
import star.flow.VelocityMagnitudeProfile;
import star.meshing.AbsoluteMinimumSize;
import star.meshing.AbsoluteTargetSize;
import star.meshing.GenericAbsoluteSize;
import star.meshing.LeafMeshPart;
import star.meshing.MeshConditionManager;
import star.meshing.MeshValueManager;
import star.meshing.RelativeOrAbsoluteOption;
import star.meshing.SurfaceSize;
import star.meshing.SurfaceSizeOption;
import star.prismmesher.CustomizeBoundaryPrismsOption;
import star.prismmesher.NumPrismLayers;
import star.prismmesher.PrismLayerStretching;
import star.prismmesher.PrismThickness;

/**
 * This class creates the regions in the part and its associated boundaries, flag = 0 for leaftMeshPart and flag = 1 for solidModelPart
 * 
 * @author cj8q5
 *
 */
public class RegionBuilder 
{
	private Simulation m_sim;
	private String m_regionName;
	private Region m_region;
	
	public RegionBuilder(Simulation sim, String regionName)
	{
		m_sim = sim;
		m_regionName = regionName;
	}
	
	/** 
	 * This method creates regions from parts, flag = true for leafMeshPart and flag = false for solidModelPart
	 * 
	 * @param partName name of the part to be used in creating a region
	 * @param flag true for a leaf mesh part and false for a solid model part
	 */
	@SuppressWarnings("unchecked")
	public void part2Region(String partName, boolean flag)
	{
		if (flag == true)
		{
			LeafMeshPart leafMeshPart = ((LeafMeshPart) m_sim.get(SimulationPartManager.class).getPart(partName));
			m_sim.getRegionManager().newRegionsFromParts(new NeoObjectVector(new Object[] {leafMeshPart}), 
					"OneRegionPerPart", null, "OneBoundaryPerPartSurface", null, "OneFeatureCurve", null, true);
		}
		if (flag == false)
		{
			SolidModelPart solidModelPart = ((SolidModelPart) m_sim.get(SimulationPartManager.class).getPart(partName));
			m_sim.getRegionManager().newRegionsFromParts(new NeoObjectVector(new Object[] {solidModelPart}), 
					"OneRegionPerPart", null, "OneBoundaryPerPartSurface", null, "OneFeatureCurve", null, true);
			
			m_region = m_sim.getRegionManager().getRegion(m_regionName);
		}
	}//end method part2Region
	
	/** 
	 * This method creates a region that contains multiple parts
	 * 
	 * @param partNames string array with the names of the parts
	 * @param regionName presentation name of the region
	 */
	@SuppressWarnings("unchecked")
	public void parts2Region(String[] partNames)
	{
		int numParts = partNames.length;
		Object[] partVector = new Object[numParts];
		for (int i = 0; i < numParts; i++)
		{
			SolidModelPart part = ((SolidModelPart) m_sim.get(SimulationPartManager.class).getPart(partNames[i]));	
			partVector[i] = part;
		}//end for loop
		
		m_sim.getRegionManager().newRegionsFromParts(new NeoObjectVector(partVector), 
				"OneRegion", null, "OneBoundaryPerPartSurface", null, "OneFeatureCurve", null, true);
		m_sim.getRegionManager().getRegion("Region 1").setPresentationName(m_regionName);
		
		m_region = m_sim.getRegionManager().getRegion(m_regionName);
	}//end method parts2Region
	
	/**
	 * This method deletes a boundary from a region
	 * @param boundaryName name of the boundary to be deleted
	 */
	public void deleteBoundary(String boundaryName)
	{
		Region region = m_sim.getRegionManager().getRegion(m_regionName);
		Boundary boundary = region.getBoundaryManager().getBoundary(boundaryName); 
		region.getBoundaryManager().remove(boundary);
	}
	
	/**
	 * This method sets a physics continua to a particular region
	 * 
	 * @param regionName name of the region to be set to physics continua
	 * @param continua Name name of the continua to be set to the specified region
	 */
	public void setRegionPhysics(String continuaName)
	{
		Region region = m_sim.getRegionManager().getRegion(m_regionName);
		
		PhysicsContinuum physics = ((PhysicsContinuum) m_sim.getContinuumManager().getContinuum(continuaName));
	    physics.add(region);
	}//end method setRegionPhysics
	
	/**
	 * This method sets the prism layer meshing settings on specified boundaries within a region
	 * 
	 * @param boundaryName name of the boundary that the prism mesher settings will be set
	 * @param Switch false for switching the prism layer off for that boundary
	 */
	public void setBoundaryPrismOption(String boundaryName, boolean Switch)
	{
		Boundary boundary = m_region.getBoundaryManager().getBoundary(boundaryName);
		if (Switch == false)
		{
			boundary.get(MeshConditionManager.class).get(CustomizeBoundaryPrismsOption.class).setSelected(CustomizeBoundaryPrismsOption.DISABLE);
		}
		else
		{
			boundary.get(MeshConditionManager.class).get(CustomizeBoundaryPrismsOption.class).setSelected(CustomizeBoundaryPrismsOption.DEFAULT);
		}
	}
	
	/**
	 * This method creates a new boundary group for organizing large numbers of boundaries within a region
	 * 
	 * @param groupName presentation name of the group
	 * @param boundaryNames name of the all the boundaries that are in the new group
	 */
	@SuppressWarnings("unchecked")
	public void createBoundaryGroup(String groupName, String[] boundaryNames)
	{
		m_region.getBoundaryManager().getGroupsManager().createGroup(groupName);
		Object[] boundaryVector = new Object[boundaryNames.length];
	    for (int i = 0; i < boundaryNames.length; i++)
	    {
	    	Boundary boundary = m_region.getBoundaryManager().getBoundary(boundaryNames[i]);
	    	boundaryVector[i] = boundary;
	    }
	    
	    ((ClientServerObjectGroup) m_region.getBoundaryManager().getGroupsManager().getObject(groupName)).getGroupsManager().
	    groupObjects(groupName, new NeoObjectVector(boundaryVector), true);
	}
	
	/**
	 *  This method creates an interaction between two boundaries within a region
	 *  
	 * @param regionNames name of the regions where the interaction will be created
	 * @param boundaryNames name of the two boundaries where the interaction will exist
	 * @param interactionName name of the interaction that is to be created
	 */
	public void createInterface(String[] regionNames, String[] boundaryNames, String interactionName)
	{
		Region region_0 = m_sim.getRegionManager().getRegion(regionNames[0]);
		Region region_1 = m_sim.getRegionManager().getRegion(regionNames[1]);
		
		Boundary boundary_0 = region_0.getBoundaryManager().getBoundary(boundaryNames[0]);    
	    Boundary boundary_1 = region_1.getBoundaryManager().getBoundary(boundaryNames[1]);
	    
	    DirectBoundaryInterface boundaryInterface = m_sim.getInterfaceManager().createDirectInterface(boundary_0, boundary_1, "In-place");

	    boundaryInterface.getTopology().setSelected(InterfaceConfigurationOption.IN_PLACE);
	    boundaryInterface.setPresentationName(interactionName);
	}//end method createInteraction
	
	/** 
	 * This method sets the type of boundary condition and the value at that position 
	 * 
	 * @param regionName name of the region where the boundary condition will be set
	 * @param boundaryName name of the boundary on the specified region to set the boundary condition
	 * @param boundaryType type of boundary condition either "Velocity Inlet" or "Pressure Outlet" or "Symmetry"
	 * @param boundaryValue specified value for the boundary condition
	 */
	public void setBoundaryCondition(String boundaryName, String boundaryType, 
			double[] components, double boundaryValue)
	{
		Region region = m_sim.getRegionManager().getRegion(m_regionName);
		Boundary boundary = region.getBoundaryManager().getBoundary(boundaryName);
		if (boundaryType == "Velocity Inlet")
		{
			boundary.setBoundaryType(InletBoundary.class);
			
			boundary.getConditions().get(FlowDirectionOption.class).setSelected(FlowDirectionOption.COMPONENTS);
 
		    boundary.getValues().get(FlowDirectionProfile.class).getMethod(ConstantVectorProfileMethod.class).
		    	getQuantity().setComponents(components[0], components[1], components[2]);
			
		    boundary.getValues().get(VelocityMagnitudeProfile.class).
		    	getMethod(ConstantScalarProfileMethod.class).getQuantity().setValue(boundaryValue);
		}
		if (boundaryType == "Pressure Outlet")
		{
			boundary.setBoundaryType(PressureBoundary.class);
		}
		if (boundaryType == "Symmetry")
		{
			boundary.setBoundaryType(SymmetryBoundary.class);
		}
	}// end nested method setBoundaryCondition
	
	/** 
	 * This method sets a heat flux on a boundary 
	 * 
	 * @param regionName name of the where a boundary heat flux will be set
	 * @param boundaryName specific boundary on the region where the heat flux will be applied
	 * @param heatFluxMagnitude magnitude of the heat flux
	 */
	public void setBoundaryHeatFlux(String boundaryName, double heatFluxMagnitude)
	{
		Region region = m_sim.getRegionManager().getRegion(m_regionName);
		Boundary boundary = region.getBoundaryManager().getBoundary(boundaryName);
	    boundary.getConditions().get(WallThermalOption.class).setSelected(WallThermalOption.HEAT_FLUX);
	    HeatFluxProfile heatFluxProfile_0 = boundary.getValues().get(HeatFluxProfile.class);
	    heatFluxProfile_0.getMethod(ConstantScalarProfileMethod.class).getQuantity().setValue(heatFluxMagnitude);
	}//end nested method setBoundaryHeatFlux
	
	/**
	 *  This method sets the Surface mesh settings within a region's boundary 
	 *  
	 * @param regionName name of the region where mesh settings will be specified
	 * @param boundaryName name of the boundary on the region where the mesh will be specified
	 * @param meshName name of the mesh continua
	 * @param minSurfSize the minimum mesh surface size
	 * @param targSurfSize the target mesh surface size
	 */
	public void setBoundarySurfaceMeshSettings(String boundaryName, String meshName, 
			double minSurfSize, double targSurfSize)
	{
		// Grabbing the region, mesh, and boundary
		Region region = m_sim.getRegionManager().getRegion(m_regionName);
		MeshContinuum mesh = ((MeshContinuum) m_sim.getContinuumManager().getContinuum(meshName));
	    mesh.add(region);
	    Boundary boundary = region.getBoundaryManager().getBoundary(boundaryName);

	    SurfaceSizeOption surfaceSizeOption = boundary.get(MeshConditionManager.class).get(SurfaceSizeOption.class);
	    surfaceSizeOption.setSurfaceSizeOption(true);
	    
	    SurfaceSize surfaceSize_1 = boundary.get(MeshValueManager.class).get(SurfaceSize.class);
	    surfaceSize_1.getRelativeOrAbsoluteOption().setSelected(RelativeOrAbsoluteOption.ABSOLUTE);

	    AbsoluteMinimumSize absoluteMinimumSize_1 = surfaceSize_1.getAbsoluteMinimumSize();
	    absoluteMinimumSize_1.getValue().setValue(minSurfSize);

	    AbsoluteTargetSize absoluteTargetSize_0 = surfaceSize_1.getAbsoluteTargetSize();
	    absoluteTargetSize_0.getValue().setValue(targSurfSize);
	}//end nested method setBoundarySurfaceMeshSetttings
	
	/** 
	 * This method sets the prism mesh settings within a region's boundary 
	 * 
	 * @param regionName the name of the region where the prism mesh settings will be specified
	 * @param boundaryName the name of the boundary where the prism mesh will be specified
	 * @param meshName the name of the mesh continua
	 * @param numLayer the number of layers in the prism layer mesh
	 * @param layerStretch specifying the layer stretching in the prism layer mesh
	 * @param layerThickness specifying the first layer's thickness (I believe anyway)
	 */
	public void setBoundaryPrismMeshSettings(String boundaryName, String meshName,
			int numLayer, double layerStretch, double layerThickness)
	{
		// Grabbing the region, mesh, and boundary
		Region region = m_sim.getRegionManager().getRegion(m_regionName);
		MeshContinuum mesh = ((MeshContinuum) m_sim.getContinuumManager().getContinuum(meshName));
	    mesh.add(region);
	    Boundary boundary = region.getBoundaryManager().getBoundary(boundaryName);
	    
	    // Prism layer mesh settings
	    boundary.get(MeshConditionManager.class).get(CustomizeBoundaryPrismsOption.class).setSelected(CustomizeBoundaryPrismsOption.CUSTOM_VALUES);
		
	    NumPrismLayers numPrismLayers_1 = boundary.get(MeshValueManager.class).get(NumPrismLayers.class);
	    numPrismLayers_1.setNumLayers(numLayer);

	    PrismLayerStretching prismLayerStretching_1 = boundary.get(MeshValueManager.class).get(PrismLayerStretching.class);
	    prismLayerStretching_1.setStretching(layerStretch);

	    PrismThickness prismThickness_1 = boundary.get(MeshValueManager.class).get(PrismThickness.class);
	    prismThickness_1.getRelativeOrAbsoluteOption().setSelected(RelativeOrAbsoluteOption.ABSOLUTE);

	    GenericAbsoluteSize genericAbsoluteSize_1 = ((GenericAbsoluteSize) prismThickness_1.getAbsoluteSize());
	    genericAbsoluteSize_1.getValue().setValue(layerThickness);
	}//end nested method setBoundaryPrismMeshSettings
}
