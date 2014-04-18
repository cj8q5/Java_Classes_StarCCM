package starClasses;

import star.base.neo.NeoObjectVector;
import star.common.PrimitiveFieldFunction;
import star.common.Simulation;
import star.common.StarUpdate;
import star.post.SolutionHistoryManager;
import star.post.SolutionHistory;

public class SolutionHistoryCreator 
{

	private Simulation m_sim;
	private SolutionHistory m_solutionHistory;
	private StarUpdate m_starUpdate;
	
	public SolutionHistoryCreator(Simulation sim, String simhFileLocation)
	{
	    m_solutionHistory = sim.get(SolutionHistoryManager.class).createForFile(simhFileLocation, false);
	    m_sim = sim;
	}
	
	public void setUpdateSettings(String timestepOrIteration, int updateFrequency)
	{
	    m_starUpdate = m_solutionHistory.getUpdate();
	    if (timestepOrIteration.equals("Iteration"))
	    {
	    	m_starUpdate.setUpdateMode(1);
	    }
	    else if (timestepOrIteration.equals("Time Step"))
	    {
	    	m_starUpdate.setUpdateMode(2);
	    }
	}
	
	public void addScalarFieldFunction(String[] fieldFunction)
	{
		if (fieldFunction.length == 1)
		{
			PrimitiveFieldFunction primitiveFieldFunction_0 = 
					((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction(fieldFunction[0]));
			m_solutionHistory.setScalars(new NeoObjectVector(new Object[] {primitiveFieldFunction_0}));
		}
		else if (fieldFunction.length > 1)
		{
			Object[] fieldFunctionVector =  new Object[fieldFunction.length];
			for (int i = 0; i < fieldFunction.length; i++)
			{
				PrimitiveFieldFunction primitiveFieldFunction = 
						((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction(fieldFunction[i]));
				fieldFunctionVector[i] = primitiveFieldFunction;
			}
			m_solutionHistory.setScalars(new NeoObjectVector(fieldFunctionVector));
		}
	}
	
	public void addVectorFieldFunction(String[] fieldFunction)
	{
		if (fieldFunction.length == 1)
		{
			PrimitiveFieldFunction primitiveFieldFunction_0 = 
					((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction(fieldFunction[0]));
			m_solutionHistory.setVectors(new NeoObjectVector(new Object[] {primitiveFieldFunction_0}));
		}
		else if (fieldFunction.length > 1)
		{
			Object[] fieldFunctionVector =  new Object[fieldFunction.length];
			for (int i = 0; i < fieldFunction.length; i++)
			{
				PrimitiveFieldFunction primitiveFieldFunction = 
						((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction(fieldFunction[i]));
				fieldFunctionVector[i] = primitiveFieldFunction;
			}
			m_solutionHistory.setVectors(new NeoObjectVector(fieldFunctionVector));
		}
	}
}
