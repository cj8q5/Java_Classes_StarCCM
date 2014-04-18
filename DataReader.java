package starClasses;

import java.io.BufferedReader;

import java.io.FileReader;
import java.util.Scanner;

import starClasses.GeoData;
import javax.swing.JOptionPane;

/**
 * This class reads data from the various input file.
 */
public class DataReader
{
	private GeoData geoData;
	private MeshElementData meshElementData;
	private MeshSpacingData meshSpacingData;
	
    public void readGeometryData(String fileToRead) 
    {
        try 
        {
            // Read input file: fileToRead
            FileReader file = new FileReader(fileToRead);
            BufferedReader reader = new BufferedReader(file);
			Scanner scanner = new Scanner(reader);

            // Grabbing the geometry data for the plate
			scanner.nextLine();
			scanner.nextLine();
        	double plateLength = scanner.nextDouble();
        	double plateHeight = scanner.nextDouble();
        	double plateWidth = scanner.nextDouble();
            
            // Grabbing the geometry data for the channels
        	scanner.nextLine();
        	scanner.nextLine();
        	scanner.nextLine();
        	scanner.nextLine();
        	double smallChannelHeight = scanner.nextDouble();
        	double largeChannelHeight = scanner.nextDouble();
            
            // Grabbing the geometry data for the plenums
        	scanner.nextLine();
        	scanner.nextLine();
        	scanner.nextLine();
        	scanner.nextLine();
        	double inletLength = scanner.nextDouble();
        	double outletLength = scanner.nextDouble();
            
            geoData = new GeoData(plateLength, plateHeight, plateWidth, 
            		smallChannelHeight, largeChannelHeight, inletLength, outletLength);
        }
        
        catch (Exception e) 
        {
            // Create a window displaying the error message.
            JOptionPane.showMessageDialog(null, e.toString());
        }
        
    }
    
    public void readMeshElementData(String fileToRead)
    {
    	try 
        {
            // Read input file: fileToRead
            FileReader fr = new FileReader(fileToRead);
            BufferedReader br = new BufferedReader(fr);
			Scanner sc = new Scanner(br);

            // Grabbing the mesh data for the X and Z direction for the inlet, outlet, and extruded direction
            sc.nextLine();
            int extrudeCell = sc.nextInt();
        	int inletX = sc.nextInt();
        	int outletX = sc.nextInt();
            
            // Grabbing the mesh data for the inlet plenum parts
            sc.nextLine();
            sc.nextLine();
            int largeInletY = sc.nextInt();
        	int smallInletY = sc.nextInt();
        	int plateInletY = sc.nextInt();
            
            // Grabbing the mesh data for the outlet plenum parts
            sc.nextLine();
            sc.nextLine();
            int largeOutletY = sc.nextInt();
        	int smallOutletY = sc.nextInt();
        	int plateOutletY = sc.nextInt();
        	
        	// Grabbing the mesh data for the small channel part
        	sc.nextLine();
        	sc.nextLine();
        	int smChannelX = sc.nextInt();
        	int smChannelY = sc.nextInt();
        	
        	// Grabbing the mesh data for the large channel part
        	sc.nextLine();
        	sc.nextLine();
        	int lgChannelX = sc.nextInt();
        	int lgChannelY = sc.nextInt();
            
            meshElementData = new MeshElementData(extrudeCell, inletX, largeInletY, smallInletY, plateInletY, 
            		outletX, largeOutletY, smallOutletY, plateOutletY,
            		smChannelX, smChannelY, lgChannelX, lgChannelY);
        }
        
        catch (Exception e) 
        {
            // Create a window displaying the error message.
            JOptionPane.showMessageDialog(null, e.toString());
        }
    }
    
    public void readMeshSpacingData(String fileToRead)
    {
    	try 
        {
            // Read input file: fileToRead
            FileReader fr = new FileReader(fileToRead);
            BufferedReader br = new BufferedReader(fr);
			Scanner sc = new Scanner(br);

            // Grabbing the mesh data for the X and Z direction for the inlet, outlet, and extruded direction
            sc.nextLine();
            double inletSpacingX = sc.nextDouble();
            double inletSpacingY = sc.nextDouble();
            
            // Grabbing the mesh data for the inlet plenum parts
            sc.nextLine();
            sc.nextLine();
            double outletSpacingX = sc.nextDouble();
            double outletSpacingY = sc.nextDouble();
            
            // Grabbing the mesh data for the outlet plenum parts
            sc.nextLine();
            sc.nextLine();
            double smChannelSpacingX = sc.nextDouble();
            double smChannelSpacingY = sc.nextDouble();
        	
        	// Grabbing the mesh data for the small channel part
        	sc.nextLine();
        	sc.nextLine();
        	double lgChannelSpacingX = sc.nextDouble();
        	double lgChannelSpacingY = sc.nextDouble();
        	
            meshSpacingData = new MeshSpacingData(inletSpacingX, inletSpacingY, outletSpacingX, outletSpacingY, smChannelSpacingX, 
            		smChannelSpacingY, lgChannelSpacingX, lgChannelSpacingY);
        }
        
        catch (Exception e) 
        {
            // Create a window displaying the error message.
            JOptionPane.showMessageDialog(null, e.toString());
        }
    }

    // Returns the object GeoData that contains the geometry parameters
    public GeoData getGeoDetails() 
    {
        return geoData;
    }
    
    // Returns the object MeshElementData that contains the mesh element data
    public MeshElementData getMeshElementDetails()
    {
    	return meshElementData;
    }
    
    // Returns the object MeshSpacingData that contains the mesh element data
    public MeshSpacingData getMeshSpacingDetails()
    {
    	return meshSpacingData;
    }
    
}//end class DataReader