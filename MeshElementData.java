package starClasses;

/**
 * This class receives data from the input data file and contains getter 
 * methods which can be used to set the mesh parameters, etc. in the 
 * simulation. The input data includes the mesh parameters where the mesh density is
 * computed.
 */
public class MeshElementData
{
    // Member variables
	private int m_extrudeCell;
	
	// Inlet plenum mesh parameters
	private int m_inletX;
	private int m_largeInletY;
	private int m_smallInletY;
	private int m_plateInletY;
	
	// Outlet plenum mesh parameters
	private int m_outletX;
	private int m_largeOutletY;
	private int m_smallOutletY;
	private int m_plateOutletY;
	
	// Small channel mesh parameters
	private int m_smChannelX;
	private int m_smChannelY;
	
	// Large channel mesh parameters
	private int m_lgChannelX;
	private int m_lgChannelY;
	
	// Random variables
    private double m_meshDensity;
    private double[] m_maxPressures;
    private double[] m_minPressures;
    private double[] m_pressureDrops;

    public MeshElementData(int extrudeCell, int inletX, int largeInletY, int smallInletY, int plateInletY, 
    		int outletX, int largeOutletY, int smallOutletY, int plateOutletY,
    		int smChannelX, int smChannelY, int lgChannelX, int lgChannelY)
    {
        // Assign input parameter to member variable
    	m_extrudeCell = extrudeCell;
    	m_inletX = inletX;
    	m_largeInletY = largeInletY;
    	m_smallInletY = smallInletY;
    	m_plateInletY = plateInletY;
    	
    	m_outletX = outletX;
    	m_largeOutletY = largeOutletY;
    	m_smallOutletY = smallOutletY;
    	m_plateOutletY = plateOutletY;
    	
    	m_smChannelX = smChannelX;
    	m_smChannelY = smChannelY;
    	m_lgChannelX = lgChannelX;
    	m_lgChannelY = lgChannelY;
        m_meshDensity = (((largeInletY + smallInletY + plateInletY)*inletX) +
        		((largeOutletY + smallOutletY + plateOutletY)*outletX) + 
        		(smChannelX*smChannelY) + (lgChannelX*lgChannelY))*extrudeCell;
    }

    // Getter methods to provide access to the member variables
    public int getExtrudeCell() 
    {
        return m_extrudeCell;
    }
    
    public int getInletX()
    {
    	return m_inletX;
    }
    
    public int getOutletX()
    {
    	return m_outletX;
    }

    public int getSmallInletY() 
    {
        return m_smallInletY;
    }

    public int getLargeInletY() 
    {
        return m_largeInletY;
    }

    public int getPlateInletY() 
    {
        return m_plateInletY;
    }

    public int getSmallOutletY() 
    {
        return m_smallOutletY;
    }
    
    public int getLargeOutletY()
    {
    	return m_largeOutletY;
    }
    
    public int getPlateOutletY()
    {
    	return m_plateOutletY;
    }
    
    public int getSmChannelX()
    {
    	return m_smChannelX;
    }
    
    public int getSmChannelY()
    {
    	return m_smChannelY;
    }
    
    public int getLgChannelX()
    {
    	return m_lgChannelX;
    }
    
    public int getLgChannelY()
    {
    	return m_lgChannelY;
    }

    public double[] getMaxPressures()
    {
    	return m_maxPressures;
    }
    
    public double[] getMinPressures()
    {
    	return m_minPressures;
    }
    
    public double[] getPressureDrops()
    {
    	m_pressureDrops[0] = m_maxPressures[0] - m_minPressures[0];
    	m_pressureDrops[1] = m_maxPressures[1] - m_minPressures[1];
    	return m_pressureDrops;
    }
        
    public double getMeshDensity()
    {
    	return m_meshDensity; 
    }
    
    /**
     * When the simulation has run, important values will be stored in
     * the MeshData object using the method.
     */            
    public void setMaxPressures(double maxPressureLg, double maxPressureSm) 
    {
        m_maxPressures[0] = maxPressureLg;
        m_maxPressures[1] = maxPressureSm;
    }
    
    public void setMinPressures(double minPressureLg, double minPressureSm) 
    {
        m_minPressures[0] = minPressureLg;
        m_minPressures[1] = minPressureSm;
    }
    
}//end MeshData
