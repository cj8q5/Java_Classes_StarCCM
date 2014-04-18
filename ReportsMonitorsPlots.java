package starClasses;

import star.base.neo.DoubleVector;
import star.base.report.*;
import star.common.Axes;
import star.common.Axis;
import star.common.AxisTitle;
import star.common.AxisType;
import star.common.Boundary;
import star.common.Coordinate;
import star.common.InternalDataSet;
import star.common.MonitorNormalizeOption;
import star.common.MonitorPlot;
import star.common.MonitorTriggerOption;
import star.common.Region;
import star.common.ResidualMonitor;
import star.common.Simulation;
import star.common.StarPlot;
import star.common.Units;
import star.common.XYPlot;
import star.common.YAxisType;
import star.flow.ForceCoefficientReport;
import star.flow.ForceReportForceOption;
import star.vis.FrontalAreaReport;
import star.vis.LinePart;

/**
 * This class is for creating reports, monitors, and plots
 * 
 * @author cj8q5
 *
 */
public class ReportsMonitorsPlots 
{
	private Simulation m_sim;
	private Units m_units;
	
	public ReportsMonitorsPlots(Simulation sim)
	{
		m_sim = sim;
	    m_units = ((Units) m_sim.getUnitsManager().getObject("m"));
	}
	
	/** This method is for creating a maximum report 
	 * 
	 * @param objectPath vector string containing a region and then a boundary, if only a region is found the report 
	 * 		will be created for the entire region
	 * @param fieldFunction the name of the field function to be used in the max report such as "Pressure" or "Temperature"
	 * @param reportName presentation name for the max report
	 */
	public MaxReport createMaxReport(String[] objectPath, String reportName)
	{
	    MaxReport maxReport = m_sim.getReportManager().createReport(MaxReport.class);
		
	    if (objectPath.length == 1)
	    {
	    	Region region = m_sim.getRegionManager().getRegion(objectPath[0]);
		    maxReport.getParts().setObjects(region);
	    }
	    if (objectPath.length > 1)
	    {
	    	Region region = m_sim.getRegionManager().getRegion(objectPath[0]);
	    	for(int i = 1; i < objectPath.length; i++)
	    	{
	    		Boundary boundary = region.getBoundaryManager().getBoundary(objectPath[i]);
			    maxReport.getParts().addObjects(boundary);
	    	}
	    }
		
		maxReport.setPresentationName(reportName);
		return maxReport;
		
	}//end method createMaxReport
	
	/** This method creates a minimum report
	 * 
	 * @param objectPath vector string containing a region and then a boundary, if only a region is found the report 
	 * 		will be created for the entire region 
	 * @param reportName presentation name for the min report
	 */
	public MinReport createMinReport(String[] objectPath, String reportName)
	{
		MinReport minReport = m_sim.getReportManager().createReport(MinReport.class);
		
		if (objectPath.length == 1)
	    {
	    	Region region = m_sim.getRegionManager().getRegion(objectPath[0]);
		    minReport.getParts().setObjects(region);
	    }
		if (objectPath.length > 1)
	    {
	    	Region region = m_sim.getRegionManager().getRegion(objectPath[0]);
	    	for(int i = 1; i < objectPath.length; i++)
	    	{
	    		Boundary boundary = region.getBoundaryManager().getBoundary(objectPath[i]);
	    		minReport.getParts().addObjects(boundary);
	    	}
	    }
		
	    minReport.setPresentationName(reportName);
	    return minReport;
	    
	}//end method createMinReport
	
	/** This method creates an average report 
	 * 
	 * @param regionName the name of the region where the avg report will be reporting from
	 * @param boundaryName the name of the boundary where the field function will be averaged 
	 * @param fieldFunction the name of the field function that the avg report will use such as "Pressure" and "Temperature"
	 * @param reportName presentation name for the avg report
	 */
	public AreaAverageReport createAverageReport(String[] objectPath, String reportName)
	{
		AreaAverageReport areaAverageReport = m_sim.getReportManager().createReport(AreaAverageReport.class);

	    if (objectPath.length == 1)
	    {
	    	Region region = m_sim.getRegionManager().getRegion(objectPath[0]);
	    	areaAverageReport.getParts().setObjects(region);
	    }
	    else
	    {
	    	Region region = m_sim.getRegionManager().getRegion(objectPath[0]);
	    	for(int i = 1; i < objectPath.length; i++)
	    	{
	    		Boundary boundary = region.getBoundaryManager().getBoundary(objectPath[i]);
	    		areaAverageReport.getParts().addObjects(boundary);
	    	}
	    }
	    areaAverageReport.setPresentationName(reportName);
	    return areaAverageReport;
	    
	}//end method createAverageReport
	
	/**
	 * This method creates an frontal area report for reporting the area of a part 
	 * @param partNames	names of the parts for the desired area
	 * @param reportName	name of the report
	 * @param viewUpVector	view up vector, must be orthogonal to the normal vector
	 * @param normalVector	the normal vector
	 * @return
	 */
	public FrontalAreaReport createAreaReport(String reportName, double[] viewUpVector, double[] normalVector)
	{
		FrontalAreaReport frontalAreaReport = m_sim.getReportManager().createReport(FrontalAreaReport.class);
		frontalAreaReport.setPresentationName(reportName);

		Coordinate coordinate_0 = frontalAreaReport.getViewUpCoordinate();
		coordinate_0.setCoordinate(m_units, m_units, m_units, new DoubleVector(viewUpVector));

	    Coordinate coordinate_1 = frontalAreaReport.getNormalCoordinate();
	    coordinate_1.setCoordinate(m_units, m_units, m_units, new DoubleVector(normalVector));
		return frontalAreaReport;
	}
	
	/**
	 * This method creates a force coefficient report
	 * @param regionName	name of the region for the report to be created for
	 * @param boundaryNames	name of the boundaries
	 * @param forceOption	either "Pressure", "Shear", OR "Pressure and Shear"
	 * @param refPressure	the reference pressure (atmospheric)
	 * @param forceDirection	direction of the force 
	 * @param numOfBands	number of bands the 
	 */
	public void createForceCoefficientReportMonitorPlot(String regionName, String[] boundaryNames, String forceOption, 
			double refPressure, double refVelocity, double refArea, double refDensity, double[] forceDirection, String reportName)
	{
		ForceCoefficientReport forceCoefficientReport = m_sim.getReportManager().createReport(ForceCoefficientReport.class);

		forceCoefficientReport.getReferenceDensity().setValue(refDensity);
		forceCoefficientReport.getReferenceVelocity().setValue(refVelocity);
		forceCoefficientReport.getReferenceArea().setValue(refArea);
		if(forceOption == "Pressure")
		{
			forceCoefficientReport.getForceOption().setSelected(ForceReportForceOption.PRESSURE);
		}
		if(forceOption == "Shear")
		{
			forceCoefficientReport.getForceOption().setSelected(ForceReportForceOption.SHEAR);
		}
		if(forceOption == "Pressure and Shear")
		{
			forceCoefficientReport.getForceOption().setSelected(ForceReportForceOption.PRESSURE_AND_SHEAR);
		}
		forceCoefficientReport.getReferencePressure().setValue(refPressure);
		forceCoefficientReport.getDirection().setComponents(forceDirection[0], forceDirection[1], forceDirection[2]);
	    Region region = m_sim.getRegionManager().getRegion(regionName);

	    for(int i = 0; i < boundaryNames.length; i++)
	    {
	    	Boundary boundary = region.getBoundaryManager().getBoundary(boundaryNames[i]);
	    	forceCoefficientReport.getParts().addPart(boundary);
	    }
	    forceCoefficientReport.setPresentationName(reportName);
	    
	    ReportMonitor forceMonitor = forceCoefficientReport.createMonitor();
	    MonitorPlot monitorPlot = m_sim.getPlotManager().createMonitorPlot();
		monitorPlot.getMonitors().addObjects(forceMonitor);
		monitorPlot.setPresentationName(reportName);
	}
	
	/** 
	 * This method turns off the "auto" normalization for the residual monitors in a simulation
	 */
	public void residualNormalization(String[] residualNames)
	{
		//String[] residualNames = {"Continuity", "Tdr", "Tke", "X-momentum", "Y-momentum", "Z-momentum"};
		
		for (int i = 0; i < residualNames.length; i++)
		{
			ResidualMonitor residualMonitor = ((ResidualMonitor) m_sim.getMonitorManager().getMonitor(residualNames[i]));
		    residualMonitor.getNormalizeOption().setSelected(MonitorNormalizeOption.OFF);
		}
		
	}// end method residualNormalization
	
	/** This method creates monitors and a monitor plot with them using two reports
	 * 
	 * @param reportNames the name of the reports to be made into monitors and to be placed on a monitor plot
	 * @param plotName presentation name of the plot
	 * @param axesTitles presentation name of the X and Y axes
	 * @param flag true for a plot with a max, min, and average reports and false for a plot with two average reports
	 */
	public void createMonitorPlot2(String[] reportNames, String plotName, String[] axesTitles, boolean flag)
	{
		if (flag == true)
		{
			MinReport minReport = ((MinReport) m_sim.getReportManager().getReport(reportNames[0]));
			ReportMonitor reportMonitor_0 = minReport.createMonitor();
			reportMonitor_0.getTriggerOption().setSelected(MonitorTriggerOption.ITERATION);

			MaxReport maxReport = ((MaxReport) m_sim.getReportManager().getReport(reportNames[1]));
			ReportMonitor reportMonitor_1 = maxReport.createMonitor();
			reportMonitor_1.getTriggerOption().setSelected(MonitorTriggerOption.ITERATION);
			
			AreaAverageReport aveReport = ((AreaAverageReport) m_sim.getReportManager().getReport(reportNames[2]));
			ReportMonitor reportMonitor_2 = aveReport.createMonitor();
			reportMonitor_2.getTriggerOption().setSelected(MonitorTriggerOption.ITERATION);
			
			MonitorPlot monitorPlot = m_sim.getPlotManager().createMonitorPlot();
		    monitorPlot.setPresentationName(plotName);
			monitorPlot.getMonitors().addObjects(reportMonitor_0, reportMonitor_1, reportMonitor_2);
			
			IterationMonitor iterationMonitor = ((IterationMonitor) m_sim.getMonitorManager().getMonitor("Iteration"));
		    monitorPlot.setXAxisMonitor(iterationMonitor);
		}
		
		if (flag == false)
		{
			AreaAverageReport aveReport_0 = ((AreaAverageReport) m_sim.getReportManager().getReport(reportNames[0]));
			ReportMonitor reportMonitor_0 = aveReport_0.createMonitor();
			reportMonitor_0.getTriggerOption().setSelected(MonitorTriggerOption.ITERATION);
			
			AreaAverageReport aveReport_1 = ((AreaAverageReport) m_sim.getReportManager().getReport(reportNames[1]));
		    ReportMonitor reportMonitor_1 = aveReport_1.createMonitor();
			reportMonitor_1.getTriggerOption().setSelected(MonitorTriggerOption.ITERATION);

		    MonitorPlot monitorPlot_1 = m_sim.getPlotManager().createMonitorPlot();
		    monitorPlot_1.setPresentationName(plotName);
		    monitorPlot_1.getMonitors().addObjects(reportMonitor_0, reportMonitor_1);
		}
	    
		StarPlot monitorPlot = m_sim.getPlotManager().getPlot(plotName);
	    Axes axes = monitorPlot.getAxes();

	    Axis axis_0 = axes.getXAxis();
	    AxisTitle axisTitle_0 = axis_0.getTitle();
	    axisTitle_0.setText(axesTitles[0]);

	    Axis axis_1 = axes.getYAxis();
	    AxisTitle axisTitle_1 = axis_1.getTitle();
	    axisTitle_1.setText(axesTitles[1]);
	}//end method createMonitorPlot2
	
	/** This method creates a XY plot of a field function versus position
	 * 
	 * @param regionNames names of the regions where the boundaries can be found
	 * @param boundaryNames names of the boundaries that will be plotted on the XY plot
	 * @param positionVector a position vector that will be used on the X axis of the XY plot, Example: {0.0 1.0 0.0}
	 * @param fieldFunction the field function to be used on the Y axis of the XY plot
	 * @param plotName the presentation name of the XY plot
	 */
	public XYPlot createXYPlot(double[] positionVector, String plotName, String yAxisTitle)
	{
		XYPlot xYPlot_1 = m_sim.getPlotManager().createXYPlot();
	    xYPlot_1.setPresentationName(plotName);
	    
		AxisType axisType_1 = xYPlot_1.getXAxisType();
	    axisType_1.setDirection(new DoubleVector(positionVector));

	    Axes axes_1 = xYPlot_1.getAxes();
	    Axis axis_2 = axes_1.getXAxis();
	    AxisTitle axisTitle_2 = axis_2.getTitle();
		axisTitle_2.setText("Position [" + positionVector[0] + ", " + positionVector[1] + ", " + positionVector[2] + "] (m)");
		
		Axis axis_3 = axes_1.getYAxis();
		AxisTitle axisTitle_3 = axis_3.getTitle();
		axisTitle_3.setText(yAxisTitle);
		
		return xYPlot_1;
		
	}// end method createXYPlot
	
	/** This method adds boundaries to a XY plot 
	 * 
	 * @param xYPlot the Star CCM+ object XYPlot 
	 * @param regionName the name of the region that will be added to the XY plot
	 * @param boundaryNames the name of the boundaries that will be added to the XY plot must be contained in the previously defined region
	 */
	public void addObjects2XYPlot(XYPlot xYPlot, String regionName, String[] boundaryNames)
	{
		Region region = m_sim.getRegionManager().getRegion(regionName);
		YAxisType yAxisType_1 = ((YAxisType) xYPlot.getYAxes().getAxisType("Y Type 1"));
		for (int i = 0; i < boundaryNames.length; i++)
		{
			Boundary boundary = region.getBoundaryManager().getBoundary(boundaryNames[i]);
			xYPlot.getParts().addObjects(boundary);
			InternalDataSet internalDataSet = ((InternalDataSet) yAxisType_1.getDataSets().getDataSet(regionName + ": " + boundaryNames[i]));
			internalDataSet.setSeriesName(regionName + ": " + boundaryNames[i]);
		}//end for loop
		
	}// end of method addObjects2XYPlot
	
	/** This method adds a boundary to a XY plot 
	 * 
	 * @param xYPlot the Star CCM+ object XYPlot 
	 * @param regionName the name of the region that will be added to the XY plot
	 * @param boundaryName the name of the boundary that will be added to the XY plot must be contained in the previously defined region
	 */
	public void addObject2XYPlot(XYPlot xYPlot, String regionName, String boundaryName)
	{
		Region region = m_sim.getRegionManager().getRegion(regionName);
		Boundary boundary = region.getBoundaryManager().getBoundary(boundaryName);
		xYPlot.getParts().addObjects(boundary);
		
		YAxisType yAxisType_1 = ((YAxisType) xYPlot.getYAxes().getAxisType("Y Type 1"));
		InternalDataSet internalDataSet = ((InternalDataSet) yAxisType_1.getDataSets().getDataSet(regionName + ": " + boundaryName));
		internalDataSet.setSeriesName(regionName + ": " + boundaryName);
	}// end of method addObject2XYPlot
	
	
	/** This method adds a line probe to an XY plot
	 * 
	 * @param xYPlot the Star-CCM+ object XYPlot
	 * @param derivedPart the name of the line probe
	 */
	public void addLineProbe2XYPlot(XYPlot xYPlot, LinePart lineProbe)
	{
		xYPlot.getParts().addObjects(lineProbe);	
	}
	
}// end class ReportsMonitorsPlots