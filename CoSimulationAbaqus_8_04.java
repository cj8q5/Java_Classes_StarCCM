package starClasses;

import star.base.neo.NeoObjectVector;
import star.common.Boundary;
import star.common.PrimitiveFieldFunction;
import star.common.Region;
import star.common.Simulation;
import star.cosimulation.abaqus.AbaqusCoSimulation;
import star.cosimulation.abaqus.AbaqusCoSimulationSolver;
import star.cosimulation.abaqus.AbaqusExecution;
import star.cosimulation.abaqus.AbaqusIterUrfOptions;
import star.cosimulation.abaqus.AbaqusRendezvousOptions;
import star.cosimulation.abaqus.CouplingAlgorithm;
import star.cosimulation.abaqus.FieldExchangeControls;
import star.cosimulation.common.CoSimulationManager;

public class CoSimulationAbaqus_8_04 
{
	private Simulation m_sim;
	private Region m_region;
	private AbaqusCoSimulation m_abaqusCoSimulation;
	
	public CoSimulationAbaqus_8_04(Simulation sim, String regionName)
	{
		m_sim = sim;
		m_region = m_sim.getRegionManager().getRegion(regionName);
		
		m_abaqusCoSimulation = m_sim.get(CoSimulationManager.class).createCoSimulation(AbaqusCoSimulation.class, "Abaqus Co-Simulation");
		
		PrimitiveFieldFunction primitiveFieldFunction_0 = ((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction("StaticPressure"));
	    PrimitiveFieldFunction primitiveFieldFunction_1 = ((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction("WallShearStress"));
	    m_abaqusCoSimulation.setExportedFieldFunctions(new NeoObjectVector(new Object[] {primitiveFieldFunction_0, primitiveFieldFunction_1}));
	    m_abaqusCoSimulation.setExportedFieldFunctions(new NeoObjectVector(new Object[] {primitiveFieldFunction_0, primitiveFieldFunction_1}));
		
	    PrimitiveFieldFunction primitiveFieldFunction_2 = ((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction("NodalDisplacement"));
	    PrimitiveFieldFunction primitiveFieldFunction_3 = ((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction("Velocity"));
	    m_abaqusCoSimulation.setImportedFieldFunctions(new NeoObjectVector(new Object[] {primitiveFieldFunction_2, primitiveFieldFunction_3}));
	    m_abaqusCoSimulation.setImportedFieldFunctions(new NeoObjectVector(new Object[] {primitiveFieldFunction_2, primitiveFieldFunction_3}));
	}
	
	/**
	 * This method sets the coupled boundaries for the Abaqus Co-Simulation
	 * @param boundaryNames	names of the coupled boundaries to be included in the FSI simulation
	 */
	public void setCouplingBoundaries(String[] boundaryNames)
	{
		for(int i = 0; i < boundaryNames.length; i++)
		{
			Boundary boundary = m_region.getBoundaryManager().getBoundary(boundaryNames[i]);
			m_abaqusCoSimulation.getCouplingSurfaces().addObjects(boundary);
		}
	}
	
	/**
	 * This method sets up the Abaqus execution settings in Star-CCM+
	 * @param jobName	desired name of the Abaqus job
	 * @param inputFilePath	file path for the Abaqus input file, example 
	 * 						"D:\\Users\\cj8q5\\Simulations\\March 2013\\FSI_Comb_9\\Abaqus\\CombPlate.inp"
	 * @param abaqusExecutableName	executable file name, example "abq6122.bat"
	 * @param numCPUs	number of CPU's to use on the Abaqus side of the FSI simulation
	 */
	public void setAbaqusExecutionSettings(String jobName, String inputFilePath, String abaqusExecutableName, int numCPUs)
	{
  	  	//File inputFile = new File(inputFilePath);
		
		AbaqusExecution abaqusExecution_0 = m_abaqusCoSimulation.getAbaqusExecution();
	    abaqusExecution_0.setJobName(jobName);
	    //abaqusExecution_0.setJobFileName(inputFile);
	    abaqusExecution_0.setJobFileNameSilently(inputFilePath);
	    abaqusExecution_0.setExecutableName(abaqusExecutableName);
	    abaqusExecution_0.setNumCpus(numCPUs);
	}
	
	/**
	 * This method sets up the coupling algorithm settings
	 * @param couplingScheme	"implicit" for implicit coupling "explicit" for explicit
	 * @param rendezvousOption	"Star Leads" for letting Star-CCM+ lead and "Abaqus Leads" for letting Abaqus lead
	 * @param couplingTimeStep	the coupling time step for the co-simulation
	 */
	public void abaqusCouplingAlgorithm(String couplingScheme, String rendezvousOption, double couplingTimeStep)
	{
		// Setting the coupling scheme
		CouplingAlgorithm couplingAlgorithm = m_abaqusCoSimulation.getCouplingAlgorithm();
		if(couplingScheme.equals("Implicit"))
		{
			couplingAlgorithm.setCouplingScheme(1);
		}
		if(couplingScheme.equals("Explicit"))
		{
			couplingAlgorithm.setCouplingScheme(0);
		}
	    
		// Setting if Star or Abaqus leads the co-simulation
	    if(rendezvousOption.equals("Star Leads"))
	    {
	    	couplingAlgorithm.getRendezvousOptions().setSelected(AbaqusRendezvousOptions.STARCCM_LEADS);
	    }
	    if(rendezvousOption.equals("Abaqus Leads"))
	    {
	    	couplingAlgorithm.getRendezvousOptions().setSelected(AbaqusRendezvousOptions.ABAQUS_LEADS);
	    }
	    
	    // Setting the constant coupling time step
	    couplingAlgorithm.getConstCouplingDtQty().setValue(couplingTimeStep);
	}
	
	/**
	 * This method sets the field exchange controls for the Abaqus Co-Simulation
	 * @param minNumExchangesPerTS
	 * @param numIterationsExchange
	 * @param deflectionUnderRelax
	 */
	public void setFieldExchangeControls(int minNumExchangesPerTS, int numIterationsExchange, double deflectionUnderRelax)
	{
		AbaqusCoSimulationSolver abaqusCoSimulationSolver = 
				((AbaqusCoSimulationSolver) m_sim.getSolverManager().getSolver(AbaqusCoSimulationSolver.class));
	    FieldExchangeControls fieldExchangeControls = abaqusCoSimulationSolver.getFieldExchangeControls();
	    
	    // Setting the number of exchanges and inner iterations per exchange
	    fieldExchangeControls.setNumExchangesMin(minNumExchangesPerTS);
	    fieldExchangeControls.setNumIterations(numIterationsExchange);
	    
	    // Setting the inputed Abaqus deflection under relaxation factor 
	    fieldExchangeControls.getIterUrfOptions().setSelected(AbaqusIterUrfOptions.CONST);
	    fieldExchangeControls.setIncomingFieldURF(deflectionUnderRelax);
	}
}