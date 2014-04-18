package starClasses;

import star.base.report.ReportMonitor;
import star.common.InnerIterationStoppingCriterion;
import star.common.MonitorIterationStoppingCriterion;
import star.common.MonitorIterationStoppingCriterionAsymptoticType;
import star.common.MonitorIterationStoppingCriterionMaxLimitType;
import star.common.MonitorIterationStoppingCriterionMinLimitType;
import star.common.MonitorIterationStoppingCriterionOption;
import star.common.PhysicalTimeStoppingCriterion;
import star.common.ResidualMonitor;
import star.common.Simulation;
import star.common.SolverStoppingCriterionLogicalOption;
import star.common.StepStoppingCriterion;

/**
 * This class is creating and setting parameters for stopping criteria
 * 
 * @author cj8q5
 *
 */
public class StoppingCriteria 
{
	private Simulation m_sim = null;
	
	public StoppingCriteria(Simulation sim)
	{
		m_sim = sim;
	}
	
	/** 
	 * This method creates a stopping criteria of type minimum
	 * 
	 * @param monitorName name of the monitor to be used in the stopping criteria
	 * @param monitorType type of monitor being used either "Residual" or "Report"
	 * @param innerOrOuterIterations for stopping inner or outer iterations
	 * @param minLimit the minimum limit for the stopping criteria
	 */
	public void createMinStoppingCriteria(String monitorName, String monitorType, double minLimit, String innerOrOuterIterations, boolean Switch)
	{
		if (monitorType == "Residual")
		{
			ResidualMonitor residualMonitor = ((ResidualMonitor) m_sim.getMonitorManager().getMonitor(monitorName));
			MonitorIterationStoppingCriterion monitorStoppingCriterion = residualMonitor.createIterationStoppingCriterion();
			
			((MonitorIterationStoppingCriterionOption) monitorStoppingCriterion.getCriterionOption()).setSelected(MonitorIterationStoppingCriterionOption.MINIMUM);
			MonitorIterationStoppingCriterionMinLimitType minLimitType = ((MonitorIterationStoppingCriterionMinLimitType) monitorStoppingCriterion.getCriterionType());
			minLimitType.getLimit().setValue(minLimit);
			monitorStoppingCriterion.setIsUsed(Switch);
		}
		
		if (monitorType == "Report")
		{
			ReportMonitor reportMonitor = ((ReportMonitor) m_sim.getMonitorManager().getMonitor(monitorName));
			MonitorIterationStoppingCriterion monitorStoppingCriterion = reportMonitor.createIterationStoppingCriterion();
			
			((MonitorIterationStoppingCriterionOption) monitorStoppingCriterion.getCriterionOption()).setSelected(MonitorIterationStoppingCriterionOption.MINIMUM);
			MonitorIterationStoppingCriterionMinLimitType minLimitType = ((MonitorIterationStoppingCriterionMinLimitType) monitorStoppingCriterion.getCriterionType());
			minLimitType.getLimit().setValue(minLimit);
			monitorStoppingCriterion.setIsUsed(Switch);
			if (innerOrOuterIterations == "Inner")
			{
				monitorStoppingCriterion.setInnerIterationCriterion(true);
				monitorStoppingCriterion.setOuterIterationCriterion(false);
			}
			if (innerOrOuterIterations == "Outer")
			{
				monitorStoppingCriterion.setInnerIterationCriterion(false);
				monitorStoppingCriterion.setOuterIterationCriterion(true);
			}
		}
				
	}//end method createMinStoppingCriteria
	
	/** 
	 * This method creates a stopping criteria of type maximum
	 * 
	 * @param monitorName name of the monitor to be used in the stopping criteria
	 * @param monitorType type of monitor being used either "Residual" or "Report"
	 * @param maxLimit the minimum limit for the stopping criteria
	 */
	public void createMaxStoppingCriteria(String monitorName, String monitorType, double maxLimit, String innerOrOuterIterations, boolean Switch)
	{
		if (monitorType == "Residual")
		{
			ResidualMonitor residualMonitor = ((ResidualMonitor) m_sim.getMonitorManager().getMonitor(monitorName));
			MonitorIterationStoppingCriterion monitorStoppingCriterion = residualMonitor.createIterationStoppingCriterion();
			
			((MonitorIterationStoppingCriterionOption) monitorStoppingCriterion.getCriterionOption()).setSelected(MonitorIterationStoppingCriterionOption.MAXIMUM);
			MonitorIterationStoppingCriterionMaxLimitType maxLimitType = ((MonitorIterationStoppingCriterionMaxLimitType) monitorStoppingCriterion.getCriterionType());
			maxLimitType.getLimit().setValue(maxLimit);
			monitorStoppingCriterion.setIsUsed(Switch);
		}
		
		if (monitorType == "Report")
		{
			ReportMonitor reportMonitor = ((ReportMonitor) m_sim.getMonitorManager().getMonitor(monitorName));
			MonitorIterationStoppingCriterion monitorStoppingCriterion = reportMonitor.createIterationStoppingCriterion();
			
			((MonitorIterationStoppingCriterionOption) monitorStoppingCriterion.getCriterionOption()).setSelected(MonitorIterationStoppingCriterionOption.MAXIMUM);
			MonitorIterationStoppingCriterionMaxLimitType maxLimitType = ((MonitorIterationStoppingCriterionMaxLimitType) monitorStoppingCriterion.getCriterionType());
			maxLimitType.getLimit().setValue(maxLimit);
			monitorStoppingCriterion.setIsUsed(Switch);
			if (innerOrOuterIterations == "Inner")
			{
				monitorStoppingCriterion.setInnerIterationCriterion(true);
				monitorStoppingCriterion.setOuterIterationCriterion(false);
			}
			if (innerOrOuterIterations == "Outer")
			{
				monitorStoppingCriterion.setInnerIterationCriterion(false);
				monitorStoppingCriterion.setOuterIterationCriterion(true);
			}
		}
				
	}//end method createMaxStoppingCriteria
	
	/** 
	 * This method creates a stopping criteria of type asymptotic
	 * 
	 * @param monitorName name of the monitor to be used in the stopping criteria
	 * @param monitorType type of monitor being used either "Residual" or "Report"
	 * @param maxLimit the minimum limit for the stopping criteria
	 */
	public void createAsymStoppingCriteria(String monitorName, String monitorType, double rangeMaxMin, int numSamples, 
			String logic, String innerOrOuterIterations, boolean Switch)
	{
		if (monitorType == "Residual")
		{
			ResidualMonitor residualMonitor = ((ResidualMonitor) m_sim.getMonitorManager().getMonitor(monitorName));
			MonitorIterationStoppingCriterion monitorStoppingCriterion = residualMonitor.createIterationStoppingCriterion();
			
			((MonitorIterationStoppingCriterionOption) monitorStoppingCriterion.getCriterionOption()).setSelected(MonitorIterationStoppingCriterionOption.ASYMPTOTIC);
			MonitorIterationStoppingCriterionAsymptoticType monitorIterationStopping = ((MonitorIterationStoppingCriterionAsymptoticType) monitorStoppingCriterion.getCriterionType());
			monitorIterationStopping.getMaxWidth().setValue(rangeMaxMin);
			monitorIterationStopping.setNumberSamples(numSamples);
			monitorStoppingCriterion.setIsUsed(Switch);
			
			if (logic == "AND")
			{
				monitorStoppingCriterion.getLogicalOption().setSelected(SolverStoppingCriterionLogicalOption.AND);
			}
			
			if (logic == "OR")
			{
				monitorStoppingCriterion.getLogicalOption().setSelected(SolverStoppingCriterionLogicalOption.OR);
			}
			
		}
		
		if (monitorType == "Report")
		{
			ReportMonitor reportMonitor = ((ReportMonitor) m_sim.getMonitorManager().getMonitor(monitorName));
			MonitorIterationStoppingCriterion monitorStoppingCriterion = reportMonitor.createIterationStoppingCriterion();
			
			((MonitorIterationStoppingCriterionOption) monitorStoppingCriterion.getCriterionOption()).setSelected(MonitorIterationStoppingCriterionOption.ASYMPTOTIC);
			MonitorIterationStoppingCriterionAsymptoticType monitorIterationStopping = ((MonitorIterationStoppingCriterionAsymptoticType) monitorStoppingCriterion.getCriterionType());
			monitorIterationStopping.getMaxWidth().setValue(rangeMaxMin);
			monitorIterationStopping.setNumberSamples(numSamples);
			monitorStoppingCriterion.setIsUsed(Switch);
			
			if (logic == "AND")
			{
				monitorStoppingCriterion.getLogicalOption().setSelected(SolverStoppingCriterionLogicalOption.AND);
			}
			
			if (logic == "OR")
			{
				monitorStoppingCriterion.getLogicalOption().setSelected(SolverStoppingCriterionLogicalOption.OR);
			}
			if (innerOrOuterIterations == "Inner")
			{
				monitorStoppingCriterion.setInnerIterationCriterion(true);
				monitorStoppingCriterion.setOuterIterationCriterion(false);
			}
			if (innerOrOuterIterations == "Outer")
			{
				monitorStoppingCriterion.setInnerIterationCriterion(false);
				monitorStoppingCriterion.setOuterIterationCriterion(true);
			}
		}
				
	}//end method createAsymStoppingCriteria
	
	/** This method is for controlling the inner iteration stopping criteria that is automatically added when an unsteady solver is enabled
	 * 
	 * @param numInnerIterations the maximum number of inner iterations to make before moving on to the next time step
	 * @param logic either "AND" or "OR" 
	 * @param Switch boolean for enabling or disabling the inner iteration stopping criteria
	 */
	public void innerIterationStoppingCriteriaController(int numInnerIterations, String logic, boolean Switch)
	{
		InnerIterationStoppingCriterion innerIterationStopCrit = 
			      ((InnerIterationStoppingCriterion) m_sim.getSolverStoppingCriterionManager().getSolverStoppingCriterion("Maximum Inner Iterations"));

		innerIterationStopCrit.setIsUsed(Switch);

		innerIterationStopCrit.setMaximumNumberInnerIterations(numInnerIterations);

		if (logic == "AND")
		{
			innerIterationStopCrit.getLogicalOption().setSelected(SolverStoppingCriterionLogicalOption.AND);
		}
		if (logic == "OR")
		{
			innerIterationStopCrit.getLogicalOption().setSelected(SolverStoppingCriterionLogicalOption.OR);
		}
	}// end of method innerIterationStoppingCriteriaController
	
	/** This method is for controlling the max physical time stopping criteria
	 * 
	 * @param maxTime the maximum physical time
	 * @param logic either "AND" or "OR"
	 * @param Switch boolean for enabling or disabling the inner iteration stopping criteria
	 */
	public void maxPhysicalTime(double maxTime, String logic, boolean Switch)
	{
		PhysicalTimeStoppingCriterion physicalTime = 
				((PhysicalTimeStoppingCriterion) m_sim.getSolverStoppingCriterionManager().getSolverStoppingCriterion("Maximum Physical Time"));

		physicalTime.setIsUsed(Switch);

		physicalTime.getMaximumTime().setValue(maxTime);

		if (logic == "AND")
		{
			physicalTime.getLogicalOption().setSelected(SolverStoppingCriterionLogicalOption.AND);
		}
		if (logic == "OR")
		{
			physicalTime.getLogicalOption().setSelected(SolverStoppingCriterionLogicalOption.OR);
		}
	}// end of method maxPhysicalTime
	
	/** This method is for controlling the max steps stopping criteria 
	 * 
	 * @param numSteps the max number of time steps
	 * @param logic either "AND" or "OR"
	 * @param Switch boolean for enabling or disabling the max steps stopping criteria
	 */
	public void maxSteps(int numSteps, String logic, boolean Switch)
	{
		StepStoppingCriterion stepStoppingCriterion = 
			      ((StepStoppingCriterion) m_sim.getSolverStoppingCriterionManager().getSolverStoppingCriterion("Maximum Steps"));

	    stepStoppingCriterion.setIsUsed(Switch);
	    
	    stepStoppingCriterion.setMaximumNumberSteps(numSteps);
	    
	    if (logic == "AND")
		{
	    	stepStoppingCriterion.getLogicalOption().setSelected(SolverStoppingCriterionLogicalOption.AND);
		}
		if (logic == "OR")
		{
			stepStoppingCriterion.getLogicalOption().setSelected(SolverStoppingCriterionLogicalOption.OR);
		}
	    
	}// end of method maxSteps
	
}//end class StoppingCriteria
