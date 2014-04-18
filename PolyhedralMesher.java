package starClasses;

import star.common.Boundary;
import star.common.MeshContinuum;
import star.common.Region;
import star.common.Simulation;
import star.meshing.BaseSize;
import star.meshing.GenericAbsoluteSize;
import star.meshing.MeshPipelineController;
import star.meshing.RelativeMinimumSize;
import star.meshing.RelativeOrAbsoluteOption;
import star.meshing.RelativeTargetSize;
import star.meshing.SurfaceSize;
import star.prismmesher.NumPrismLayers;
import star.prismmesher.PrismMesherModel;
import star.prismmesher.PrismStretchingOption;
import star.prismmesher.PrismThickness;
import star.prismmesher.PrismWallThickness;
import star.meshing.*;
import star.resurfacer.*;
import star.dualmesher.*;

public class PolyhedralMesher 
{
	private Simulation m_sim;
	private String m_regionName;
	private MeshContinuum m_mesh;
	private PrismMesherModel m_prism;
	private Region m_region;
	
	public PolyhedralMesher(Simulation activeSim, String regionName)
	{
		m_sim = activeSim;
		m_regionName = regionName;
		m_region = m_sim.getRegionManager().getRegion(m_regionName);
		
		m_mesh = m_sim.getContinuumManager().createContinuum(MeshContinuum.class);
		m_mesh.enable(ResurfacerMeshingModel.class);
		m_mesh.enable(DualMesherModel.class);
		m_mesh.enable(PrismMesherModel.class);
		m_mesh.add(m_region);
		
		m_prism = m_mesh.getModelManager().getModel(PrismMesherModel.class);
		m_prism.getPrismStretchingOption().setSelected(PrismStretchingOption.WALL_THICKNESS);
	}
	
	/**
	 * This method turns meshing in parallel on and off
	 * @param onOrOff boolean for turning meshing in parallel on and off
	 */
	public void meshInParallel(boolean onOrOff)
	{
		m_mesh.setMeshInParallel(onOrOff);
	}
	
	/**
	 * 
	 * @param baseCellSize
	 * @param targetCellSizePercentage
	 * @param minimumCellSizePercentage
	 * @param maximumCellSizePercentage
	 */
	public void setMesherSettings(double baseCellSize, double targetCellSizePercentage, double minimumCellSizePercentage)
	{
		m_mesh.getReferenceValues().get(BaseSize.class).setValue(baseCellSize);
		SurfaceSize surfaceSize = m_mesh.getReferenceValues().get(SurfaceSize.class);

	    RelativeTargetSize relativeTargetSize = surfaceSize.getRelativeTargetSize();
	    relativeTargetSize.setPercentage(targetCellSizePercentage);

	    RelativeMinimumSize relativeMinimumSize = surfaceSize.getRelativeMinimumSize();
	    relativeMinimumSize.setPercentage(minimumCellSizePercentage);
	}
	
	/**
	 * This method sets the surface growth rate for custom surface sizes
	 * @param surfaceGrowthRate	the value for the surface growth rate
	 */
	public void setSurfaceGrowthRate(double surfaceGrowthRate)
	{
		SurfaceGrowthRate surfaceGrowthRate_0 = m_mesh.getReferenceValues().get(SurfaceGrowthRate.class);
		surfaceGrowthRate_0.setGrowthRate(surfaceGrowthRate);
	}
	
	/**
	 * This method sets the minimum surface size and the target surface sizes for the overall mesh
	 * @param minSurfaceSize
	 * @param targetSurfaceSize
	 */
	public void setReferenceValuesSurfaceSize(double minSurfaceSize, double targetSurfaceSize)
	{
		SurfaceSize surfaceSize = m_mesh.getReferenceValues().get(SurfaceSize.class);
	    surfaceSize.getRelativeOrAbsoluteOption().setSelected(RelativeOrAbsoluteOption.ABSOLUTE);

	    AbsoluteMinimumSize absoluteMinimumSize = surfaceSize.getAbsoluteMinimumSize();
	    absoluteMinimumSize.getValue().setValue(minSurfaceSize);

	    AbsoluteTargetSize absoluteTargetSize = surfaceSize.getAbsoluteTargetSize();
	    absoluteTargetSize.getValue().setValue(targetSurfaceSize);
	}
	
	/**
	 * This method sets custom surface sizes on a specified region boundary
	 * @param minSurfSizePercentage
	 * @param targetSurfSizePercentage
	 */
	public void setCustomBoundarySurfaceSize(String regionName, String boundaryName, double minSurfSizePercentage, double targetSurfSizePercentage)
	{
	    Boundary boundary = m_region.getBoundaryManager().getBoundary(boundaryName);

	    SurfaceSizeOption surfaceSizeOption_0 = boundary.get(MeshConditionManager.class).get(SurfaceSizeOption.class);
	    surfaceSizeOption_0.setSurfaceSizeOption(true);

	    SurfaceSize surfaceSize = boundary.get(MeshValueManager.class).get(SurfaceSize.class);
	    RelativeMinimumSize relativeMinimumSize = surfaceSize.getRelativeMinimumSize();
	    relativeMinimumSize.setPercentage(minSurfSizePercentage);

	    RelativeTargetSize relativeTargetSize = surfaceSize.getRelativeTargetSize();
	    relativeTargetSize.setPercentage(targetSurfSizePercentage);
	}
	
	/**
	 * 
	 * @param prismLayerThickness
	 * @param wallThickness
	 * @param numPrismLayers
	 */
	public void setPrismLayerSettings(double prismLayerThickness, double wallThickness, int numPrismLayers)
	{
		m_mesh.getReferenceValues().get(PrismWallThickness.class).setValue(wallThickness);
		
		// Setting the number of prism layers for the prism layer mesher
		NumPrismLayers prismLayers = m_mesh.getReferenceValues().get(NumPrismLayers.class);
		prismLayers.setNumLayers(numPrismLayers);
		
		// Setting the overall thickness of all of the prism layers for the prism layer mesher
		PrismThickness prismThickness = m_mesh.getReferenceValues().get(PrismThickness.class);
	    prismThickness.getRelativeOrAbsoluteOption().setSelected(RelativeOrAbsoluteOption.ABSOLUTE);
	    GenericAbsoluteSize genericAbsoluteSize = ((GenericAbsoluteSize) prismThickness.getAbsoluteSize());
	    genericAbsoluteSize.getValue().setValue(prismLayerThickness);
	}
	
	public void generateMesh()
	{
		MeshPipelineController meshPipelineController = m_sim.get(MeshPipelineController.class);
	    meshPipelineController.generateVolumeMesh();
	}
	
}
