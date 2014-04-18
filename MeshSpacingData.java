package starClasses;

/**
 * This class receives data from the input data file and contains getter 
 * methods which can be used to set the mesh parameters, etc. in the 
 * simulation. The input data includes the mesh parameters where the mesh density is
 * computed.
 */
public class MeshSpacingData
{
	// Inlet plenum mesh parameters
	private double m_inletSpacingX = 0;
	private double m_inletSpacingY = 0;
	
	// Outlet plenum mesh parameters	
	private double m_outletSpacingX = 0;
	private double m_outletSpacingY = 0;
	
	// Small channel mesh parameters	
	private double m_smChannelSpacingX = 0;
	private double m_smChannelSpacingY = 0;
	
	// Large channel mesh parameters	
	private double m_lgChannelSpacingX = 0;
	private double m_lgChannelSpacingY = 0;

	
    public MeshSpacingData(double inletSpacingX, double inletSpacingY, double outletSpacingX, 
    		double outletSpacingY, double smChannelSpacingX, double smChannelSpacingY, 
    		double lgChannelSpacingX, double lgChannelSpacingY)
    {
        // Assign input parameter to member variable
    	m_inletSpacingX = inletSpacingX;
    	m_inletSpacingY = inletSpacingY;
    	m_outletSpacingX = outletSpacingX;
    	m_outletSpacingY = outletSpacingY;
    	m_lgChannelSpacingX = lgChannelSpacingX;
    	m_lgChannelSpacingY = lgChannelSpacingY;
    	m_smChannelSpacingX = smChannelSpacingX;
    	m_smChannelSpacingY = smChannelSpacingY;
    }

    public double getInletSpacingX()
    {
    	return m_inletSpacingX;
    }
    
    public double getInletSpacingY()
    {
    	return m_inletSpacingY;
    }
    
    public double getOutletSpacingX()
    {
    	return m_outletSpacingX;
    }
    
    public double getOutletSpacingY()
    {
    	return m_outletSpacingY;
    }
    
    public double getSmChannelSpacingX()
    {
    	return m_smChannelSpacingX;
    }
    
    public double getSmChannelSpacingY()
    {
    	return m_smChannelSpacingY;
    }
    
    public double getLgChannelSpacingX()
    {
    	return m_lgChannelSpacingX;
    }
    
    public double getLgChannelSpacingY()
    {
    	return m_lgChannelSpacingY;
    }
      
}//end MeshSpacingData
