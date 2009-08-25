///////////////////////////////////////////////////////////////////////////////
//  Filename: $RCSfile: APropIntResult.java,v $
//  Purpose:  Counts the number of descriptors and molecules in a molecule file.
//  Language: Java
//  Compiler: JDK 1.4
//  Authors:  Joerg Kurt Wegner
//  Version:  $Revision: 1.9 $
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

import joelib2.molecule.types.BasicPairData;
import joelib2.molecule.types.PairData;

import joelib2.util.HelperMethods;

import java.io.LineNumberReader;
import java.io.StringReader;


/**
 *  Atom representation.
 *
 * @.author     wegnerj
 * @.license    GPL
 * @.cvsversion    $Revision: 1.9 $, $Date: 2005/02/17 16:48:30 $
 */
public class APropIntResult extends BasicPairData implements Cloneable,
    FeatureResult, NativeValue, java.io.Serializable
{
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final long serialVersionUID = 1L;
    private final static String basicFormat = "<atom_property>\n" +
        "32-bit integer";
    private final static String lineFormat = "<atom_property>\n" +
        "32-bit integer";

    //~ Instance fields ////////////////////////////////////////////////////////

    /**
     *  Description of the Field
     */
    public String atomProperty;

    /**
     *  Description of the Field
     */
    public int value;

    //~ Constructors ///////////////////////////////////////////////////////////

    /**
     *  Constructor for the IntResult object
     */
    public APropIntResult()
    {
        this.setKey(this.getClass().getName());
        this.setKeyValue(this);
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public Object clone()
    {
        APropIntResult newObj = new APropIntResult();

        return clone(newObj);
    }

    public APropIntResult clone(APropIntResult other)
    {
        super.clone(other);
        other.atomProperty = this.atomProperty;
        other.value = this.value;

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
        if (ioType.equals(BasicIOTypeHolder.instance().getIOType("SDF")))
        {
            return lineFormat;
        }
        else
        {
            return basicFormat;
        }
    }

    /**
     *  Description of the Method
     *
     * @param pairData  Description of the Parameter
     * @param ioType    Description of the Parameter
     * @return          Description of the Return Value
     */
    public boolean fromPairData(IOType ioType, PairData pairData)
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
     * @param sValue  Description of the Parameter
     * @param ioType  Description of the Parameter
     * @return        Description of the Return Value
     */
    public boolean fromString(IOType ioType, String sValue)
    {
        StringReader sr = new StringReader(sValue);
        LineNumberReader lnr = new LineNumberReader(sr);

        String tmp = null;

        // get property type
        if (ioType.equals(BasicIOTypeHolder.instance().getIOType("FLAT")) ==
                false)
        {
            try
            {
                atomProperty = lnr.readLine();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();

                return false;
            }
        }
        else
        {
            int index = sValue.indexOf("\n");
            tmp = sValue.substring(index).trim();
        }

        if (ioType.equals(BasicIOTypeHolder.instance().getIOType("UNDEFINED")))
        {
            int index = sValue.indexOf("\n");
            tmp = sValue.substring(index).trim();
        }
        else if (ioType.equals(BasicIOTypeHolder.instance().getIOType("FLAT")) ==
                false)
        {
            int index = sValue.indexOf("\n");
            tmp = sValue.substring(index).trim();
        }

        value = Integer.parseInt(tmp);

        return true;
    }

    /**
     * Gets the doubleNV attribute of the APropIntResult object
     *
     * @return   The doubleNV value
     */
    public double getDoubleNV()
    {
        return (double) value;
    }

    /**
     *  Gets the double attribute of the DoubleResult object
     *
     * @return   The double value
     */
    public int getInt()
    {
        return value;
    }

    /**
     * Gets the intNV attribute of the APropIntResult object
     *
     * @return   The intNV value
     */
    public int getIntNV()
    {
        return value;
    }

    /**
     * Gets the stringNV attribute of the APropIntResult object
     *
     * @return   The stringNV value
     */
    public String getStringNV()
    {
        return Integer.toString(value);
    }

    public boolean init(String _descName)
    {
        this.setKey(_descName);

        return true;
    }

    /**
     * Gets the doubleNV attribute of the APropIntResult object
     *
     * @return   The doubleNV value
     */
    public boolean isDoubleNV()
    {
        return false;
    }

    /**
     * Gets the intNV attribute of the APropIntResult object
     *
     * @return   The intNV value
     */
    public boolean isIntNV()
    {
        return true;
    }

    /**
     * Sets the doubleNV attribute of the APropIntResult object
     *
     * @param _value  The new doubleNV value
     */
    public void setDoubleNV(double _value)
    {
        value = (int) _value;
    }

    /**
     *  Sets the double attribute of the DoubleResult object
     *
     * @param _v  The new double value
     */
    public void setInt(int _v)
    {
        value = _v;
    }

    /**
     * Sets the intNV attribute of the APropIntResult object
     *
     * @param _value  The new intNV value
     */
    public void setIntNV(int _value)
    {
        value = _value;
    }

    /**
     * Sets the stringNV attribute of the APropIntResult object
     *
     * @param _value  The new stringNV value
     */
    public void setStringNV(String _value)
    {
        value = Integer.parseInt(_value);
    }

    /**
     *  Description of the Method
     *
     * @param ioType  Description of the Parameter
     * @return        Description of the Return Value
     */
    public String toString(IOType ioType)
    {
        StringBuffer sb = new StringBuffer();

        if (ioType.equals(BasicIOTypeHolder.instance().getIOType("FLAT")))
        {
            sb.append(value);
        }
        else
        {
            sb.append(atomProperty);
            sb.append(HelperMethods.eol);
            sb.append(value);
        }

        return sb.toString();
    }
}

///////////////////////////////////////////////////////////////////////////////
//  END OF FILE.
///////////////////////////////////////////////////////////////////////////////
