package starClasses;

import star.base.neo.DoubleVector;
import star.base.neo.IntVector;
import star.base.neo.NeoObjectVector;
import star.common.Coordinate;
import star.common.LabCoordinateSystem;
import star.common.Simulation;
import star.common.Units;
import star.vis.ConstrainedPlaneSection;
import star.vis.LinePart;
import star.vis.PlaneSection;
import star.common.Region;

/**
 * This class is for creating all of the derived parts in the simulation
 * 
 * @author cj8q5
 *
 */
public class DerivedParts 
{
	private Simulation m_sim;
	private String[] m_regionNames;
	private Units m_units;
	
	public DerivedParts(Simulation sim, String[] regionNames)
	{
		m_sim = sim;
		m_regionNames = regionNames;
		m_units = m_sim.getUnitsManager().
				getPreferredUnits(new IntVector(new int[] {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));

	}
	
	/** This method is for creating a plane within a simulation 
	 * 
	 * @param regionName the region within which the plane will be in
	 * @param directionVector the direction vector for specifying the direction of the plane
	 * @param locationVector the coordinates of the plane
	 * @param planeName presentation name of the plane
	 */
	public void createSectionPlane(double[] directionVector, double[] locationVector, String planeName)
	{
		Object[] regionVector = null;
		if (m_regionNames.length > 1)
		{
			regionVector = new Object[m_regionNames.length];
			for (int i = 0; i < m_regionNames.length; i++)
			{
				Region region = m_sim.getRegionManager().getRegion(m_regionNames[i]);
				regionVector[i] = region;
				
			}
		}
		if (m_regionNames.length == 1)
		{
			regionVector = new Object[m_regionNames.length];
			Region region = m_sim.getRegionManager().getRegion(m_regionNames[0]);
			regionVector[0] = region;
		}
		//Region region = sim.getRegionManager().getRegion(regionName);
		//PlaneSection CenterPlaneYZ = (PlaneSection) sim.getPartManager().createImplicitPart(new NeoObjectVector(new Object[] {region}), 
		PlaneSection CenterPlaneYZ = (PlaneSection) m_sim.getPartManager().createImplicitPart(new NeoObjectVector(regionVector),
	    	new DoubleVector(directionVector),
	    	new DoubleVector(locationVector), 0, 1,
	    	new DoubleVector(new double[] {0.0}));
	    CenterPlaneYZ.setValueMode(0);
	    CenterPlaneYZ.setPresentationName(planeName);
	    
	}//end method createSectionPlane
	
	/**
	 * This method creates a constrained plane
	 * @param normalVector	the normal vector for the constrained plane
	 * @param origin	the origin of the normal vector
	 * @param contourCoordinates	coordinates of points that define the countour of the constrained plane
	 */
	public ConstrainedPlaneSection createConstrainedPlane(String planeName, double[] normalVector, double[] origin, double[] contourCoordinates)
	{
		
	    Region region = m_sim.getRegionManager().getRegion(m_regionNames[0]);

	    ConstrainedPlaneSection constrainedPlaneSection = 
	      (ConstrainedPlaneSection) m_sim.getPartManager().createConstrainedPlaneImplicitPart(
	    		  new NeoObjectVector(new Object[] {region}), new DoubleVector(contourCoordinates), m_units);
	    constrainedPlaneSection.setReevaluateStatus(false);

	    LabCoordinateSystem labCoordinateSystem = 
	    		m_sim.getCoordinateSystemManager().getLabCoordinateSystem();
	    constrainedPlaneSection.setCoordinateSystem(labCoordinateSystem);

	    Coordinate coordinate_0 = 
	      constrainedPlaneSection.getOriginCoordinate();
	    coordinate_0.setValue(new DoubleVector(origin));

	    Coordinate coordinate_1 = 
	      constrainedPlaneSection.getNormalCoordinate();
	    coordinate_1.setValue(new DoubleVector(normalVector));

	    coordinate_0.setCoordinate(m_units, m_units, m_units, new DoubleVector(origin));
	    coordinate_1.setCoordinate(m_units, m_units, m_units, new DoubleVector(normalVector));

	    constrainedPlaneSection.setReevaluateStatus(true);
	    constrainedPlaneSection.setPresentationName(planeName);
	    return constrainedPlaneSection;
	}
	
	/** This method is for creating a line probe within a simulation
	 * 
	 */
	public LinePart createLineProbe(double[] coordinate_0, double[] coordinate_1, int lineResolution, String lineProbeName)
	{
		Object[] regionVector = new Object[m_regionNames.length];
		for (int i = 0; i < m_regionNames.length; i++)
		{
			Region region = m_sim.getRegionManager().getRegion(m_regionNames[i]);
			regionVector[i] = region;
		}
		LinePart linePart = m_sim.getPartManager().createLinePart(new NeoObjectVector(regionVector), 
				new DoubleVector(coordinate_0), 
				new DoubleVector(coordinate_1), lineResolution);
		linePart.setPresentationName(lineProbeName);
		return linePart;
	}
}
