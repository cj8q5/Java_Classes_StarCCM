package starClasses;

import star.common.ImplicitUnsteadySolver;
import star.common.Simulation;
import star.common.TimeDiscretizationOption;
import star.coupledflow.CoupledImplicitSolver;
import star.keturb.KeTurbSolver;
import star.cosimulation.abaqus.*;
import star.morpher.*;

/**
 * This class is setting various values and settings in the solvers node
 * 
 * @author cj8q5
 *
 */
public class SolversNode 
{
	private Simulation m_sim;
	private AbaqusCoSimulationSolver m_abaqusCoSimSolver;
	private MovingMeshSolver m_movingMeshSolver;
	
	public SolversNode(Simulation sim)
	{
		m_sim = sim;
	}
	
	public void setSSorFSI(boolean SSorFSI)
	{
		m_abaqusCoSimSolver = ((AbaqusCoSimulationSolver) m_sim.getSolverManager().getSolver(AbaqusCoSimulationSolver.class));
		m_movingMeshSolver = ((MovingMeshSolver) m_sim.getSolverManager().getSolver(MovingMeshSolver.class));
		m_abaqusCoSimSolver.setFrozen(SSorFSI);
		m_movingMeshSolver.setFrozen(SSorFSI);
	}
	
	/** This method sets the k-epsilon under relaxation factor
	 * 
	 * @param underRelaxFactor the desired under relaxation factor
	 */
	public void setKepsilonRelax(double underRelaxFactor)
	{
		// Setting the K-epsilon Turbulence model under relaxation factor
	    KeTurbSolver keTurbSolver_0 = ((KeTurbSolver) m_sim.getSolverManager().getSolver(KeTurbSolver.class));
	    keTurbSolver_0.setUrf(underRelaxFactor);
	
	}// end method setKepsilonRelax
	
	/**
	 * This method sets the courant number in the Implicit Solver
	 * @param courantNumber	desired value for the courant number
	 */
	public void setCourantNumber(double courantNumber)
	{
		CoupledImplicitSolver coupledSolver = ((CoupledImplicitSolver) m_sim.getSolverManager().getSolver(CoupledImplicitSolver.class));
		coupledSolver.setCFL(courantNumber);
	}
	
	/** This method sets the unsteady time step in an implicit unsteady solver and the discretization option to either 1st or 2nd order
	 * 
	 * @param timeStep the desired time step in seconds
	 * @param timeDiscretizationOption either enter an int of 1 for first order or an int of 2 for second order 
	 */
	public void setUnsteadyTimeStep(double timeStep, int timeDiscretizationOption)
	{
		ImplicitUnsteadySolver implicitUnsteadySolver = ((ImplicitUnsteadySolver) m_sim.getSolverManager().getSolver(ImplicitUnsteadySolver.class));
		implicitUnsteadySolver.getTimeStep().setValue(timeStep);
		
		if (timeDiscretizationOption == 1)
		{
			implicitUnsteadySolver.getTimeDiscretizationOption().setSelected(TimeDiscretizationOption.FIRST_ORDER);
		}
		if (timeDiscretizationOption == 2)
		{
			implicitUnsteadySolver.getTimeDiscretizationOption().setSelected(TimeDiscretizationOption.SECOND_ORDER);
		}
		
	}// end method setUnsteadyTimeStep

}// end class SolversNode

