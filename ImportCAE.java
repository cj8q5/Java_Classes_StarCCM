/** This java script is to be run using the FSI_Abaqus_StarCCM.py python script within Star-CCM+
 * 		Written by Casey J. Jesse on June 2014 at the University of Missouri - Columbia	
 * 		Revisions:
 *			
 */
package starClasses;

import star.base.neo.*;
import star.common.*;
import star.cae.common.*;


public class ImportCAE 
{
	private Simulation m_sim;
	private CaeImportManager m_cae;
	private String m_fileLocation;
	private Units m_units;
	private ImportedModel m_importedModel;
	private String m_regionName;
	
	public ImportCAE(Simulation sim, String fileLocation, String regionName)
	{
		m_sim = sim;
		m_cae = m_sim.get(CaeImportManager.class);
		m_fileLocation = fileLocation;
		m_units = m_sim.getUnitsManager().getPreferredUnits(new IntVector(new int[] {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
		m_regionName = regionName;
	}
	
	/** This method creates a imported Abaqus model and region in Star-CCM+
	 * 
	 * @param fileName name of the file to be imported
	 * @param createRegion boolean for creating a region from the imported Abaqus model
	 */
	@SuppressWarnings("unchecked")
	public void importAbaqusInputFile(String fileName, boolean createRegion, boolean createImportedModel)
	{
		m_cae.importAbaqusModelFile(m_fileLocation + fileName + ".inp", m_units, createImportedModel, createRegion);

	    FvRepresentation fvRepresentation_0 = ((FvRepresentation) m_sim.getRepresentationManager().getObject("Volume Mesh"));
	    Region region = m_sim.getRegionManager().getRegion(m_regionName);
	    fvRepresentation_0.generateMeshReport(new NeoObjectVector(new Object[] {region}));
	}
	
	/** This method imports Abaqus solution from an Abaqus .odb file  
	 * 
	 * @param fileName	name of the .odb file to be imported
	 * @param stepName	name of the Abaqus step to use during the import
	 */
	public void importAbaqusOdbFile(String fileName, String stepName)
	{
	    m_importedModel = ((ImportedModel) m_sim.get(ImportedModelManager.class).getImportedModel("Abaqus: " + fileName));
	
	    m_cae.importAbaqusData(m_fileLocation + fileName + ".odb", 
	    		new NeoObjectVector(new Object[] {m_importedModel}), 1, "Displacement", 1, "abq6122.bat", stepName, "ALLMODES", m_units);
	}
	
	/** This method deforms the imported .odb file's deflection 
	 * 
	 * @param fileName	name of the file the .odb solution file
	 */
	public void deformImportedAbaqusModel()
	{
	    m_sim.get(ImportedModelManager.class).deformImportedModelsUsingDisplacements(new NeoObjectVector(new Object[] {m_importedModel}));
	}
	
	/** This method maps the imported deflection data to the fluid regions
	 * 
	 * @param fluidRegions names of the regions each boundary specified must have its corresponding region in this string
	 * @param fluidBoundaries names of the boundaries that are to have the plate deflections mapped
	 * @param solidBoundary name of the solid boundary in Abaqus that the fluid region is being mapped from
	 */
	public void mapAbaqusDeflectionData(String[] fluidRegions, String[] fluidBoundaries, String solidBoundary)
	{
		Object[] mappedBoundaries = new Object[fluidBoundaries.length];
		for (int i = 0; i < fluidBoundaries.length; i++)
		{
			Region region = m_sim.getRegionManager().getRegion(fluidRegions[i]);
			Boundary boundary = region.getBoundaryManager().getBoundary(fluidBoundaries[i]);
			mappedBoundaries[i] = boundary;
		}

	    ImportedSurface importedSurface = m_importedModel.getImportedSurfaceManager().getImportedSurface(solidBoundary);

	    m_sim.get(ImportedModelManager.class).mapImportedSurfaceVertexDataToBoundaryVertices(
	    		new NeoObjectVector(mappedBoundaries), 
	    		new NeoObjectVector(new Object[] {importedSurface}), "ImportedDisplacement", false, 1.0, false, true, 90.0, 
	    		new DoubleVector(new double[] {0.0, 0.0, 0.0}));
	}
}
