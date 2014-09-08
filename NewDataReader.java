package starClasses;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

public class NewDataReader 
{

	private Map<String, Double> m_doubleParameters = new HashMap<String, Double>();
	private Map<String, Integer> m_intParameters = new HashMap<String, Integer>();
	private Map<String, String> m_stringParameters = new HashMap<String, String>();
	
	public void readGeometryData(String file2Read) throws NumberFormatException, IOException
	{
        FileReader file;
		try 
		{
			file = new FileReader(file2Read);
			BufferedReader reader = new BufferedReader(file);
			
			String line;
			while((line=reader.readLine()) != null)
			{
				if(line.startsWith("#"))
				{
					continue;
				}
				else if(line.trim().length() == 0)
				{
					continue;
				}
				else
				{
					line = line.replaceAll("\\t", "");
					String[] lineParameters = line.split(":");
					
					if(lineParameters[1].equals("float"))
					{
						m_doubleParameters.put(lineParameters[0], Double.parseDouble(lineParameters[2]));
					}
					else if(lineParameters[1].equals("integer"))
					{
						m_intParameters.put(lineParameters[0], Integer.parseInt(lineParameters[2]));
					}
					else if(lineParameters[1].equals("string"))
					{
						m_stringParameters.put(lineParameters[0], lineParameters[2]);
					}
				}
			}
			reader.close();
			
		} 
		catch (FileNotFoundException e) 
		{
			JOptionPane.showMessageDialog(null, e.toString());
		}
	}// end method readGeometryData
	
	public double getDoubleData(String variableName)
	{
		double variable = m_doubleParameters.get(variableName);
		return variable;
	}
	
	public int getIntData(String variableName)
	{
		int variable = m_intParameters.get(variableName);
		return variable;
	}
	
	public String getStringData(String variableName)
	{
		String variable = m_stringParameters.get(variableName);
		return variable;
	}
}

