package starClasses;

/**
 * This class receives data from the input data file and contains getter 
 * methods which can be used to set the geometry parameters in the 
 * simulation.
 */
public class GeoData
{
    // Member variables
	private double m_plateLength = 0;
	private double m_plateHeight = 0;
	private double m_plateWidth = 0;
	
	private double m_smallChannelHeight = 0;
	private double m_largeChannelHeight = 0;
	
	private double m_inletLength = 0;
	private double m_outletLength = 0;
	
    // Constructor
    public GeoData(double plateLength, double plateHeight, double plateWidth, 
    		double smallChannelHeight, double largeChannelHeight, 
    		double inletLength, double outletLength)
    {
        // Assign input parameter to member variable
    	m_plateLength = plateLength;
    	m_plateHeight = plateHeight;
    	m_plateWidth = plateWidth;
    	
    	m_smallChannelHeight = smallChannelHeight;
    	m_largeChannelHeight = largeChannelHeight;
    	
    	m_inletLength = inletLength;
    	m_outletLength = outletLength;
    }

    // Getter methods to provide access to the member variables
    public double getPlateLength() 
    {
        return m_plateLength;
    }
    
    public double getPlateHeight()
    {
    	return m_plateHeight;
    }
    
    public double getPlateWidth()
    {
    	return m_plateWidth;
    }

    public double getSmallChannelHeight() 
    {
        return m_smallChannelHeight;
    }

    public double getLargeChannelHeight() 
    {
        return m_largeChannelHeight;
    }

    public double getInletLength() 
    {
        return m_inletLength;
    }

    public double getOutletLength() 
    {
        return m_outletLength;
    }
    
}//end GeoData
