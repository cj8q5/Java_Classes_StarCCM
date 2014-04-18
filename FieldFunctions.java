package starClasses;

import star.common.CellQualityFunction;
import star.common.PrimitiveFieldFunction;
import star.common.Simulation;
import star.common.VectorComponentFieldFunction;
import star.common.VectorMagnitudeFieldFunction;
import star.common.XYPlot;
import star.common.YAxisType;
import star.metrics.CellAspectRatioFunction;
import star.vis.ScalarDisplayer;
import star.vis.Scene;

public class FieldFunctions 
{
	private Simulation m_sim;
	private PrimitiveFieldFunction m_staticPressure;
	private PrimitiveFieldFunction m_TDR;
	private PrimitiveFieldFunction m_TKE;
	private PrimitiveFieldFunction m_uStar;
	private PrimitiveFieldFunction m_volume;
	private PrimitiveFieldFunction m_wallYPlus;
	private PrimitiveFieldFunction m_nodalDisplacement;
	private CellAspectRatioFunction m_cellAspectRatio;
	private CellQualityFunction m_cellQuality;
	private PrimitiveFieldFunction m_velocity;
	private PrimitiveFieldFunction m_shearStress;
	
	public FieldFunctions(Simulation sim)
	{
		m_sim = sim;
	}
	
	public PrimitiveFieldFunction getStaticPressureFunction()
	{
		m_staticPressure = ((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction("StaticPressure"));
		return m_staticPressure;
	}
	
	public PrimitiveFieldFunction getTurbDissRateFunction()
	{
		m_TDR = ((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction("TurbulentDissipationRate"));
		return m_TDR;
	}
	
	public PrimitiveFieldFunction getTurbKinEnergyFunction()
	{
		m_TKE = ((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction("TurbulentKineticEnergy"));
		return m_TKE;
	}
	
	public PrimitiveFieldFunction getUStarFunction()
	{
		m_uStar = ((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction("Ustar"));
		return m_uStar;
	}
	
	public PrimitiveFieldFunction getVolumeFunction()
	{
		m_volume = ((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction("Volume"));
		return m_volume;
	}
	
	public PrimitiveFieldFunction getWallYPlusFunction()
	{
		m_wallYPlus = ((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction("WallYplus"));
		return m_wallYPlus;
	}
	
	public PrimitiveFieldFunction getNodalDisplacement()
	{
		m_nodalDisplacement = ((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction("NodalDisplacement"));
		return m_nodalDisplacement;
	}
	
	public CellAspectRatioFunction getCellQualityFunction()
	{
		m_cellAspectRatio = ((CellAspectRatioFunction) m_sim.getFieldFunctionManager().getFunction("CellAspectRatio"));
		return m_cellAspectRatio;
	}
	
	public CellQualityFunction getCellAspectRatioFunction()
	{
		m_cellQuality = ((CellQualityFunction) m_sim.getFieldFunctionManager().getFunction("CellQuality"));
		return m_cellQuality;
	}
	
	public PrimitiveFieldFunction getVelocityFunction()
	{
		m_velocity = ((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction("Velocity"));
		return m_velocity;
	}
	
	public PrimitiveFieldFunction getWallShearStress()
	{
		m_shearStress = ((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction("WallShearStress"));
		return m_shearStress;
	}
	
	/** This method sets the field function for a scalar scene 
	 * 
	 * @param sceneName the scene where the scalar field function will be set
	 * @param fieldFunctionName name of the field function choices are:
	 * 		StaticPressure, TurbulentDissipationRate, TurbulentKineticEnergy, Ustar, Volume, WallYplus, CellAspectRatio, CellQuality, Velocity, WallShearStress
	 * @param vectorDirection name of the field function choices are:
	 * 		Magnitude, 0, 1, 2
	 */
	public void setSceneFieldFunction(Scene scene, String fieldFunctionName, String vectorDirection)
	{
		VectorComponentFieldFunction vectorComponentFieldFunction;
		VectorMagnitudeFieldFunction vectorMagnitudeFieldFunction;
		ScalarDisplayer scalarDisplayer = ((ScalarDisplayer) scene.getDisplayerManager().getDisplayer("Scalar 1"));

		if (fieldFunctionName == "StaticPressure" || fieldFunctionName == "TurbulentDissipationRate" ||
	    		fieldFunctionName == "TurbulentKineticEnergy" || fieldFunctionName == "Ustar" || fieldFunctionName == "Volume" ||
	    		fieldFunctionName == "WallYplus")
	    {
	    	PrimitiveFieldFunction primitiveFieldFunction = ((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction(fieldFunctionName));
	    	scalarDisplayer.getScalarDisplayQuantity().setFieldFunction(primitiveFieldFunction);
	    }
		if (fieldFunctionName == "CellAspectRatio")
		{
			CellAspectRatioFunction cellAspectRatioFunction = ((CellAspectRatioFunction) m_sim.getFieldFunctionManager().getFunction("CellAspectRatio"));
		    scalarDisplayer.getScalarDisplayQuantity().setFieldFunction(cellAspectRatioFunction);
		}
		if (fieldFunctionName == "CellQuality")
		{
			CellQualityFunction cellQualityFunction = ((CellQualityFunction) m_sim.getFieldFunctionManager().getFunction("CellQuality"));
		    scalarDisplayer.getScalarDisplayQuantity().setFieldFunction(cellQualityFunction);
		}
		if (fieldFunctionName == "Velocity" || fieldFunctionName == "WallShearStress" || fieldFunctionName == "Morpher Displacement")
		{
			PrimitiveFieldFunction primitiveFieldFunction = ((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction(fieldFunctionName));
			
			if (vectorDirection == "Magnitude")
			{
				vectorMagnitudeFieldFunction = ((VectorMagnitudeFieldFunction) primitiveFieldFunction.getMagnitudeFunction());
				scalarDisplayer.getScalarDisplayQuantity().setFieldFunction(vectorMagnitudeFieldFunction);
			}
			if (vectorDirection == "0")
			{
				vectorComponentFieldFunction = ((VectorComponentFieldFunction) primitiveFieldFunction.getComponentFunction(0));
				scalarDisplayer.getScalarDisplayQuantity().setFieldFunction(vectorComponentFieldFunction);
			}
			if (vectorDirection == "1")
			{
				vectorComponentFieldFunction = ((VectorComponentFieldFunction) primitiveFieldFunction.getComponentFunction(1));
				scalarDisplayer.getScalarDisplayQuantity().setFieldFunction(vectorComponentFieldFunction);
			}
			if (vectorDirection == "2")
			{
				vectorComponentFieldFunction = ((VectorComponentFieldFunction) primitiveFieldFunction.getComponentFunction(2));
				scalarDisplayer.getScalarDisplayQuantity().setFieldFunction(vectorComponentFieldFunction);
			}
		}
	}// end method setSceneFieldFunction
	
	/** This method sets the field function for a XYPlot scene 
	 * 
	 * @param scene the scene where the scalar field function will be set
	 * @param fieldFunctionName name of the field function choices are:
	 * 		StaticPressure, TurbulentDissipationRate, TurbulentKineticEnergy, Ustar, Volume, WallYplus, CellAspectRatio, CellQuality, Velocity, WallShearStress
	 * @param vectorDirection name of the field function choices are:
	 * 		Magnitude, 0, 1, 2
	 */
	public void setXYPlotFieldFunction(XYPlot xYPlot, String fieldFunctionName, String vectorDirection)
	{
		VectorComponentFieldFunction vectorComponentFieldFunction;
		VectorMagnitudeFieldFunction vectorMagnitudeFieldFunction;
		YAxisType yAxisType_0 = ((YAxisType) xYPlot.getYAxes().getAxisType("Y Type 1"));

		if (fieldFunctionName == "StaticPressure" || fieldFunctionName == "TurbulentDissipationRate" ||
	    		fieldFunctionName == "TurbulentKineticEnergy" || fieldFunctionName == "Ustar" || fieldFunctionName == "Volume" ||
	    		fieldFunctionName == "WallYplus")
	    {
	    	PrimitiveFieldFunction primitiveFieldFunction = ((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction(fieldFunctionName));
	    	yAxisType_0.setFieldFunction(primitiveFieldFunction);
	    }
		if (fieldFunctionName == "Velocity" || fieldFunctionName == "WallShearStress")
		{
			PrimitiveFieldFunction primitiveFieldFunction = ((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction(fieldFunctionName));
			
			if (vectorDirection == "Magnitude")
			{
				vectorMagnitudeFieldFunction = ((VectorMagnitudeFieldFunction) primitiveFieldFunction.getMagnitudeFunction());
				yAxisType_0.setFieldFunction(vectorMagnitudeFieldFunction);
			}
			if (vectorDirection == "0")
			{
				vectorComponentFieldFunction = ((VectorComponentFieldFunction) primitiveFieldFunction.getComponentFunction(0));
				yAxisType_0.setFieldFunction(vectorComponentFieldFunction);
			}
			if (vectorDirection == "1")
			{
				vectorComponentFieldFunction = ((VectorComponentFieldFunction) primitiveFieldFunction.getComponentFunction(1));
				yAxisType_0.setFieldFunction(vectorComponentFieldFunction);
			}
			if (vectorDirection == "2")
			{
				vectorComponentFieldFunction = ((VectorComponentFieldFunction) primitiveFieldFunction.getComponentFunction(2));
				yAxisType_0.setFieldFunction(vectorComponentFieldFunction);
			}
		}
	}// end method setXYPlotFieldFunction
	
	/** This method sets the field function for a report 
	 * 
	 * @param fieldFunctionName name of the field function choices are:
	 * 		StaticPressure, TurbulentDissipationRate, TurbulentKineticEnergy, Ustar, Volume, WallYplus, CellAspectRatio, CellQuality, Velocity, WallShearStress
	 * @return the scalar field function
	 */
	public PrimitiveFieldFunction getFieldFunctionScalar(String fieldFunctionName)
	{
    	PrimitiveFieldFunction primitiveFieldFunction = ((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction(fieldFunctionName));
    	return primitiveFieldFunction;
		
	}// end method setSceneFieldFunction
	
	/** This method gets the vector magnitude of a field function
	 * 
	 * @param fieldFunctionName the name of the desired field function that is a vector magnitude
	 * @return the vector magnitude field function 
	 */
	public VectorMagnitudeFieldFunction getFieldFunctionVectorMag(String fieldFunctionName)
	{
		PrimitiveFieldFunction primitiveFieldFunction = ((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction(fieldFunctionName));
		VectorMagnitudeFieldFunction vectorMagnitudeFieldFunction = ((VectorMagnitudeFieldFunction) primitiveFieldFunction.getMagnitudeFunction());
		return vectorMagnitudeFieldFunction;
		
	}// end method getFieldFunctionVectorMag
	
	/** This method gets the vector component of a field function 
	 * 
	 * @param fieldFunctionName the name of the field function 
	 * @param vectorComponent the component that is required either a "0" 
	 * @return
	 */
	public VectorComponentFieldFunction getFieldFunctionVectorComp(String fieldFunctionName, int vectorComponent)
	{
		PrimitiveFieldFunction primitiveFieldFunction = ((PrimitiveFieldFunction) m_sim.getFieldFunctionManager().getFunction(fieldFunctionName));
		VectorComponentFieldFunction vectorComponentFieldFunction = null;
		
		if (vectorComponent == 0 || vectorComponent == 1 || vectorComponent == 2)
		{
			vectorComponentFieldFunction = ((VectorComponentFieldFunction) primitiveFieldFunction.getComponentFunction(vectorComponent));
		}
		return vectorComponentFieldFunction;
		
	}//end method getFieldFunctionVectorComponent
	
}// end class FieldFunctions
