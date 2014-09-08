package starClasses;

import star.common.Simulation;
import star.cosimulation.abaqus.*;
import star.cosimulation.common.*;

public class CoSimMapper 
{
	private AbaqusCoSimulation m_abaqusCoSimulation;
	
	public CoSimMapper(Simulation m_sim)
	{
		m_abaqusCoSimulation = ((AbaqusCoSimulation) m_sim.get(CoSimulationManager.class).getObject("Abaqus Co-Simulation 1"));
	}
	
	/**
	 * This method sets mapper's tolerance settings
	 * @param normalTolerance
	 * @param perimeterTolerance
	 */
	public void setMapperTolSettings(double normalTolerance, double perimeterTolerance)
	{
			m_abaqusCoSimulation.setMapperNormalTol(normalTolerance);
			m_abaqusCoSimulation.setMapperPerimeterTol(perimeterTolerance);
	}
}
