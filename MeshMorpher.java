package starClasses;

import star.common.Boundary;
import star.common.FunctionVectorProfileMethod;
import star.common.PrimitiveFieldFunction;
import star.common.Region;
import star.common.Simulation;
import star.morpher.MovingMeshOption;
import star.morpher.MovingMeshSolver;
import star.morpher.TotalDisplacementProfile;
import star.motion.MorphOrderOption;
import star.motion.MorphingMotion;
import star.motion.MotionManager;
import star.motion.MotionSpecification;

public class MeshMorpher 
{
	private Simulation m_sim;
	private MorphingMotion m_mesher;
	private PrimitiveFieldFunction m_fieldFunction;
	private Region m_region;
	
	public MeshMorpher(Simulation sim, String regionName)
	{
		m_sim = sim;
		m_mesher = m_sim.get(MotionManager.class).createMotion(MorphingMotion.class, "Morphing");
		m_mesher.getMorphOrderOption().setSelected(MorphOrderOption.REGIONWISE);
		
		m_region = m_sim.getRegionManager().getRegion(regionName);
		
		MotionSpecification motionSpecification = m_region.getValues().get(MotionSpecification.class);
	    motionSpecification.setMotion(m_mesher);
	}
	
	/** This method adds a boundary to the mesh morpher solver
	 * 
	 * @param regionName	name of the region where the boundary is
	 * @param boundaryName	name of the boundary where the mesh morhper solver where solve
	 */
	public void addRegionBoundary(String boundaryName, String FSIorSS)
	{
		Boundary boundary = m_region.getBoundaryManager().getBoundary(boundaryName);
	    
	    if(FSIorSS == "SS")
	    {
	    	boundary.getConditions().get(MovingMeshOption.class).setSelected(MovingMeshOption.TOTAL_DISPLACEMENT);

		    TotalDisplacementProfile totalDisplacementProfile = boundary.getValues().get(TotalDisplacementProfile.class);
		    totalDisplacementProfile.setMethod(FunctionVectorProfileMethod.class);
		    m_fieldFunction = ((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction("MappedVertexImportedDisplacement"));
		    totalDisplacementProfile.getMethod(FunctionVectorProfileMethod.class).setFieldFunction(m_fieldFunction);
	    }
	    if(FSIorSS == "FSI")
	    {
	    	boundary.getConditions().get(MovingMeshOption.class).setSelected(MovingMeshOption.ABAQUSCOSIMULATION);
	    }   
	}
	
	/**
	 * This method turns the morph at inner iterations on and off
	 * @param onOrOff
	 */
	public void innerIterationMorphing(boolean onOrOff)
	{
		MovingMeshSolver movingMeshSolver = ((MovingMeshSolver) m_sim.getSolverManager().getSolver(MovingMeshSolver.class));
		movingMeshSolver.setSolveInnerIterations(onOrOff);
	}
}
