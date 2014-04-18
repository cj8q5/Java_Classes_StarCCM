package starClasses;

import star.base.report.AreaAverageReport;
import star.base.report.MaxReport;
import star.base.report.MinReport;
import star.common.Simulation;
/** 
 * 
 * @author cj8q5
 *
 * This class sets various conditions in the simulation using data read in from each Mesh Data object
 */
public class SimRunner 
{
	private Simulation m_sim;
    private MaxReport m_maxPressureLargeCh;
    private MinReport m_minPressureLargeCh;
    private MaxReport m_maxPressureSmallCh;
    private MinReport m_minPressureSmallCh;
    
    // Constructor receives the current simulation as a Simulation object
    public SimRunner(Simulation sim) 
    {
        m_sim = sim;
        m_maxPressureLargeCh = ((MaxReport) m_sim.getReportManager().getReport("MaxPressure_LargeCh"));
        m_minPressureLargeCh = ((MinReport) m_sim.getReportManager().getReport("MinPressure_LargeCh"));
        m_maxPressureSmallCh = ((MaxReport) m_sim.getReportManager().getReport("MaxPressure_SmallCh"));
        m_minPressureSmallCh = ((MinReport) m_sim.getReportManager().getReport("MinPressure_SmallCh"));
    }

    // Method to set variables, clear solution, and run the simulation
    public void runCase(MeshElementData mED, MeshSpacingData mSD, int iterations) 
    {
    	int extrudeCell = mED.getExtrudeCell();
    	int inletX = mED.getInletX();
    	int largeInletY = mED.getLargeInletY();
    	int smallInletY = mED.getSmallInletY();
    	int plateInletY = mED.getPlateInletY();
    	
    	int largeChannelX = mED.getLgChannelX();
    	int largeChannelY = mED.getLgChannelY();
    	int smallChannelX = mED.getSmChannelX();
    	int smallChannelY = mED.getSmChannelY();
    	
    	int outletX = mED.getOutletX();
    	int largeOutletY = mED.getLargeOutletY();
    	int smallOutletY = mED.getSmallOutletY();
    	int plateOutletY = mED.getPlateOutletY();
    	
    	double inletSpacingX = mSD.getInletSpacingX();
    	double inletSpacingY = mSD.getInletSpacingY();
    	double outletSpacingX = mSD.getOutletSpacingX();
    	double outletSpacingY = mSD.getOutletSpacingY();
    	double smChannelSpacingX = mSD.getSmChannelSpacingX();
    	double smChannelSpacingY = mSD.getSmChannelSpacingY();
    	double lgChannelSpacingX = mSD.getLgChannelSpacingX();
    	double lgChannelSpacingY = mSD.getLgChannelSpacingY();
    	
    	
        // Print line to output window to show how far the process has reached
        m_sim.println("Inside runCase with fluid mesh density at " + mED.getMeshDensity());

        // Clear any previous solution
        m_sim.clearSolution();

        // Run for x iterations
        m_sim.getSimulationIterator().run(iterations);

        // Get the max pressure in left channel
        double maxPressureLg = m_maxPressureLargeCh.getReportMonitorValue();
        double minPressureLg = m_minPressureLargeCh.getReportMonitorValue();
        double maxPressureSm = m_maxPressureSmallCh.getReportMonitorValue();
        double minPressureSm = m_minPressureSmallCh.getReportMonitorValue();
        
        mED.setMaxPressures(maxPressureLg, maxPressureSm);
        mED.setMinPressures(minPressureLg, minPressureSm);
    }
}
