package starClasses;

import star.common.Boundary;
import star.common.PrimitiveFieldFunction;
import star.common.Region;
import star.common.Simulation;
import star.common.VectorComponentFieldFunction;
import star.common.VectorMagnitudeFieldFunction;
import star.vis.PlaneSection;
import star.vis.ScalarDisplayer;
import star.vis.Scene;

/**
 * This class is for creating scenes in the simulation for post processing
 * 
 * @author cj8q5
 *
 */
public class Scenes 
{
	private Simulation m_sim;
	private String m_sceneName;
	private Scene m_scene;
	private ScalarDisplayer m_scalarDisplayer;
	
	public Scenes(Simulation sim, String sceneName)
	{
		m_sim = sim;
		m_sceneName = sceneName;
	}
	
	/** This method creates a scene with specified parts and functions
	 * 
	 * @param sceneName the presentation name for the new scalar scene
	 */
	public Scene createScalarScene()
	{
		m_sim.getSceneManager().createScalarScene("Scalar Scene", "Outline", "Scalar");
	    
	    m_scene = m_sim.getSceneManager().getScene("Scalar Scene 1");
	    m_scene.setPresentationName(m_sceneName);
	    m_scalarDisplayer = ((ScalarDisplayer) m_scene.getDisplayerManager().getDisplayer("Scalar 1"));
	    
	    m_scene.close(true);
		return m_scene;
	}//end createScalarScene
	
	/**
	 * This method sets scalar scene for a primitive field function
	 * @param primitiveFieldFunction the desired field function for the scene
	 * @param scalarOrVector	for setting the field function as a scalar or a vector
	 * for a scalar input "4" for a vector magnitude input "3" for a vector component input "0", "1", or "2"
	 */
	public void setSceneFieldFunction(PrimitiveFieldFunction primitiveFieldFunction, int scalarOrVector)
	{
		if(scalarOrVector == 4)
		{
			m_scalarDisplayer.getScalarDisplayQuantity().setFieldFunction(primitiveFieldFunction);
		}
		if(scalarOrVector == 0 || scalarOrVector == 1 || scalarOrVector == 2)
		{
			VectorComponentFieldFunction vectorComponentFieldFunction = 
				      ((VectorComponentFieldFunction) primitiveFieldFunction.getComponentFunction(scalarOrVector));

			m_scalarDisplayer.getScalarDisplayQuantity().setFieldFunction(vectorComponentFieldFunction);
		}
		else
		{
			VectorMagnitudeFieldFunction vectorMagnitudeFieldFunction = 
				      ((VectorMagnitudeFieldFunction) primitiveFieldFunction.getMagnitudeFunction());

			m_scalarDisplayer.getScalarDisplayQuantity().setFieldFunction(vectorMagnitudeFieldFunction);
		}
	}
	
	/** This method adds boundaries to a scene 
	 * 
	 * @param scene the Star CCM+ object scene 
	 * @param regionName the name of the region that will be added to the scalar scene
	 * @param boundaryNames the name of the boundaries that will be added to the scalar scene must be contained in the previously defined region
	 */
	public void addObject2Scene(Scene scene, String regionName, String[] boundaryNames)
	{
		ScalarDisplayer scalarDisplayer = ((ScalarDisplayer) scene.getDisplayerManager().getDisplayer("Scalar 1"));
		Region region = m_sim.getRegionManager().getRegion(regionName);
		for (int i = 0; i < boundaryNames.length; i++)
		{
			Boundary boundary = region.getBoundaryManager().getBoundary(boundaryNames[i]);
			scalarDisplayer.getParts().addObjects(boundary);
		}
	}// end of method addObject2Scene
	
	/** This method adds derived parts to a scene 
	 * 
	 * @param scene the Star CCM+ object scene 
	 * @param partNames the name of the derived parts that will be added to the scalar scene
	 */
	public void addDerivedPart2Scene(Scene scene, String[] partNames)
	{
		ScalarDisplayer scalarDisplayer = ((ScalarDisplayer) scene.getDisplayerManager().getDisplayer("Scalar 1"));
		for (int i = 0; i < partNames.length; i++)
		{
			PlaneSection plane =  ((PlaneSection) m_sim.getPartManager().getObject(partNames[i]));
			scalarDisplayer.getParts().addObjects(plane);
		}
	}// end of method addDerivedPart2Scene

}// end of class Scenes
