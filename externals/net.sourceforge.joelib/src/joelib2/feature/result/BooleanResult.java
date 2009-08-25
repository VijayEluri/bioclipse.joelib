///////////////////////////////////////////////////////////////////////////////
//  Filename: $RCSfile: BooleanResult.java,v $
//  Purpose:  Atom representation.
//  Language: Java
//  Compiler: JDK 1.4
//  Authors:  Joerg Kurt Wegner
//  Version:  $Revision: 1.11 $
//            $Date: 2005/02/17 16:48:30 $
//            $Author: wegner $
//
// Copyright OELIB:          OpenEye Scientific Software, Santa Fe,
//                           U.S.A., 1999,2000,2001
// Copyright JOELIB/JOELib2: Dept. Computer Architecture, University of
//                           Tuebingen, Germany, 2001,2002,2003,2004,2005
// Copyright JOELIB/JOELib2: ALTANA PHARMA AG, Konstanz, Germany,
//                           2003,2004,2005
//
//  This program is free software; you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation version 2 of the License.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
///////////////////////////////////////////////////////////////////////////////
package joelib2.feature.result;

import joelib2.feature.FeatureResult;
import joelib2.feature.NativeValue;

import joelib2.io.BasicIOTypeHolder;
import joelib2.io.IOType;

import joelib2.molecule.types.BasicPairDataCML;
import joelib2.molecule.types.PairData;

import org.apache.log4j.Category;


/**
 * Integer result.
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.11 $, $Date: 2005/02/17 16:48:30 $
 */
public class BooleanResult extends BasicPairDataCML implements Cloneable,
    FeatureResult, NativeValue, java.io.Serializable
{
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final long serialVersionUID = 1L;

    /**
     * Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance(BooleanResult.class
            .getName());

    //~ Instance fields ////////////////////////////////////////////////////////

    /**
     *  Description of the Field
     */
    public boolean value;

    //~ Constructors ///////////////////////////////////////////////////////////

    /**
     *  Constructor for the IntResult object
     */
    public BooleanResult()
    {
        this.setKey(this.getClass().getName());
        this.setKeyValue(this);
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public Object clone()
    {
        BooleanResult newObj = new BooleanResult();

        return clone(newObj);
    }

    public BooleanResult clone(BooleanResult other)
    {
        super.clone(other);
        other.value = value;

        return other;
    }

    /**
     *  Description of the Method
     *
     * @param ioType  Description of the Parameter
     * @return        Description of the Return Value
     */
    public String formatDescription(IOType ioType)
    {
        String format = "true or false";

        if (ioType.equals(BasicIOTypeHolder.instance().getIOType("SDF")) ||
                ioType.equals(BasicIOTypeHolder.instance().getIOType("FLAT")))
        {
            format = "1 or 0";
        }

        return format;
    }

    /**
     *  Description of the Method
     *
     * @param pairData                   Description of the Parameter
     * @param ioType                     Description of the Parameter
     * @return                           Description of the Return Value
     * @exception NumberFormatException  Description of the Exception
     */
    public boolean fromPairData(IOType ioType, PairData pairData)
        throws NumberFormatException
    {
        this.setKey(pairData.getKey());

        Object value = pairData.getKeyValue();
        boolean success = false;

        if ((value != null) && (value instanceof String))
        {
            success = fromString(ioType, (String) value);
        }

        return success;
    }

    /**
     *  Description of the Method
     *
     * @param sValue                     Description of the Parameter
     * @param ioType                     Description of the Parameter
     * @return                           Description of the Return Value
     * @exception NumberFormatException  Description of the Exception
     */
    public boolean fromString(IOType ioType, String sValue)
        throws NumberFormatException
    {
        //System.out.println("BOOLEAN::: "+ioType+" "+sValue);
        if (ioType.equals(BasicIOTypeHolder.instance().getIOType("SDF")) ||
                ioType.equals(BasicIOTypeHolder.instance().getIOType("FLAT")))
        {
            if (sValue.equals("1"))
            {
                value = true;
            }
            else if (sValue.equals("0"))
            {
                value = false;
            }
        }
        else if (ioType.equals(BasicIOTypeHolder.instance().getIOType("CML")))
        {
            try
            {
                value = ((sValue != null) && sValue.equalsIgnoreCase("true"));

                //System.out.println("PARSED:"+value);
            }
            catch (NumberFormatException ex)
            {
                logger.error(ex.toString());
                throw ex;

                //                      return false;
            }
        }
        else
        {
            try
            {
                value = ((sValue != null) && sValue.equalsIgnoreCase("true"));
            }
            catch (NumberFormatException ex)
            {
                logger.error(ex.toString());
                throw ex;

                //                      return false;
            }
        }

        return true;
    }

    /**
     *  Gets the double attribute of the DoubleResult object
     *
     * @return   The double value
     */
    public boolean getBoolean()
    {
        return value;
    }

    /**
     * Gets the doubleNV attribute of the IntResult object
     *
     * @return   The doubleNV value
     */
    public double getDoubleNV()
    {
        if (value)
        {
            return 1.0;
        }
        else
        {
            return 0.0;
        }
    }

    /**
     * Gets the intNV attribute of the IntResult object
     *
     * @return   The intNV value
     */
    public int getIntNV()
    {
        if (value)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    /**
     * Gets the stringNV attribute of the IntResult object
     *
     * @return   The stringNV value
     */
    public String getStringNV()
    {
        if (value)
        {
            return "1";
        }
        else
        {
            return "0";
        }
    }

    public boolean init(String _descName)
    {
        this.setKey(_descName);

        return true;
    }

    /**
     * Gets the doubleNV attribute of the IntResult object
     *
     * @return   The doubleNV value
     */
    public boolean isDoubleNV()
    {
        return false;
    }

    /**
     * Gets the intNV attribute of the IntResult object
     *
     * @return   The intNV value
     */
    public boolean isIntNV()
    {
        return true;
    }

    /**
     *  Sets the double attribute of the DoubleResult object
     *
     * @param _v  The new double value
     */
    public void setBoolean(boolean _v)
    {
        value = _v;
    }

    /**
     * Sets the doubleNV attribute of the IntResult object
     *
     * @param _value  The new doubleNV value
     */
    public void setDoubleNV(double _value)
    {
        if (_value == 0.0)
        {
            value = false;
        }
        else
        {
            value = true;
        }
    }

    /**
     * Sets the intNV attribute of the IntResult object
     *
     * @param _value  The new intNV value
     */
    public void setIntNV(int _value)
    {
        if (_value == 0)
        {
            value = false;
        }
        else
        {
            value = true;
        }
    }

    /**
     * Sets the stringNV attribute of the IntResult object
     *
     * @param _value  The new stringNV value
     */
    public void setStringNV(String _value)
    {
        if (_value.equals("0") || _value.equals("0.0") ||
                _value.equalsIgnoreCase("FALSE"))
        {
            value = false;
        }
        else
        {
            value = true;
        }
    }

    /**
     *  Description of the Method
     *
     * @param ioType  Description of the Parameter
     * @return        Description of the Return Value
     */
    public String toString(IOType ioType)
    {
        String valueString = Boolean.toString(value);

        if (ioType.equals(BasicIOTypeHolder.instance().getIOType("SDF")) ||
                ioType.equals(BasicIOTypeHolder.instance().getIOType("FLAT")))
        {
            if (value)
            {
                valueString = "1";
            }
            else
            {
                valueString = "0";
            }
        }
        else if (ioType.equals(BasicIOTypeHolder.instance().getIOType("CML")))
        {
            valueString = Boolean.toString(value);
        }

        return valueString;
    }
}

///////////////////////////////////////////////////////////////////////////////
//  END OF FILE.
///////////////////////////////////////////////////////////////////////////////
