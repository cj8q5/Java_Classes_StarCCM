package starClasses;

import star.base.neo.NeoObjectVector;
import star.common.Boundary;
import star.common.PrimitiveFieldFunction;
import star.common.Region;
import star.common.Simulation;
import star.common.XyzInternalTable;

public class Tools 
{
	private Simulation m_sim;
	private XyzInternalTable m_internalTable;
	
	public Tools(Simulation sim)
	{
		m_sim = sim;
	}
	
	public void createXYZInternalTable(String regionName, String[] boundaryNames, String tableName)
	{
		m_internalTable = m_sim.getTableManager().createTable(XyzInternalTable.class);

	    Region region = m_sim.getRegionManager().getRegion("Fluid");
	    
	    for(int i = 0; i < boundaryNames.length; i++)
	    {
	    	Boundary boundary = region.getBoundaryManager().getBoundary("Fluid.FSI_Back");
	    	m_internalTable.getParts().addObjects(boundary);
	    }
	    m_internalTable.setPresentationName(tableName);
	}
	
	public void setXYZInternalTableFieldFunction(String fieldFunctionName)
	{
		PrimitiveFieldFunction primitiveFieldFunction_1 = 
				((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction(fieldFunctionName));
		m_internalTable.setFieldFunctions(new NeoObjectVector(new Object[] {primitiveFieldFunction_1}));
	}
	
	public void extractAndExportXYZInternalTableData(String outputFileLocation)
	{
		m_internalTable.extract();
		m_internalTable.export(outputFileLocation, ",");
	}
}

