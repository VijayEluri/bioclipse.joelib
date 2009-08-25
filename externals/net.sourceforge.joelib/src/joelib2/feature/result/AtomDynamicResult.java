///////////////////////////////////////////////////////////////////////////////
//  Filename: $RCSfile: AtomDynamicResult.java,v $
//  Purpose:  Atom representation.
//  Language: Java
//  Compiler: JDK 1.4
//  Authors:  Joerg Kurt Wegner
//  Version:  $Revision: 1.13 $
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

import joelib2.molecule.types.AtomProperties;

import wsi.ra.text.DecimalFormatHelper;


/**
 * Dynamic results of atom properties.
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.13 $, $Date: 2005/02/17 16:48:30 $
 */
public class AtomDynamicResult extends DynamicArrayResult
    implements AtomProperties, java.io.Serializable
{
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final long serialVersionUID = 1L;
    public static final String ATOM_PROPERTY = "atom label";

    //~ Constructors ///////////////////////////////////////////////////////////

    /**
     *  Constructor for the DoubleResult object
     */
    public AtomDynamicResult()
    {
        super();
        setPropertyType(ATOM_PROPERTY);
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public Object clone()
    {
        AtomDynamicResult newObj = new AtomDynamicResult();

        int size = -1;

        if (array instanceof double[])
        {
            size = ((double[]) this.array).length;
            newObj.array = new double[size];
        }
        else if (array instanceof int[])
        {
            size = ((int[]) this.array).length;
            newObj.array = new int[size];
        }
        else if (array instanceof boolean[])
        {
            size = ((boolean[]) this.array).length;
            newObj.array = new boolean[size];
        }

        return clone(newObj, size);
    }

    public AtomDynamicResult clone(AtomDynamicResult _target, int size)
    {
        return (AtomDynamicResult) super.clone(_target, size);
    }

    /**
     *  Gets the doubleValue attribute of the AtomDynamicResult object
     *
     * @param  idx  Description of the Parameter
     * @return          The doubleValue value
     */
    public double getDoubleValue(int idx)
    {
        double value = Double.NaN;

        if (array instanceof double[])
        {
            value = ((double[]) array)[idx - 1];
        }
        else if (array instanceof int[])
        {
            value = (double) ((int[]) array)[idx - 1];
        }
        else if (array instanceof boolean[])
        {
            value = ((((boolean[]) array)[idx - 1] == true) ? 1.0 : 0.0);
        }

        return value;
    }

    /**
     *  Gets the intValue attribute of the AtomDynamicResult object.
     *
     * @param  idx  Description of the Parameter
     * @return          The intValue value
     */
    public int getIntValue(int idx)
    {
        int value = Integer.MAX_VALUE;

        //System.out.println("requested("+this.length()+"):"+idx);
        if (array instanceof double[])
        {
            value = (int) ((double[]) array)[idx - 1];
        }
        else if (array instanceof int[])
        {
            value = ((int[]) array)[idx - 1];
        }
        else if (array instanceof boolean[])
        {
            value = ((((boolean[]) array)[idx - 1] == true) ? 1 : 0);
        }

        return value;
    }

    /**
     *  Gets the stringValue attribute of the AtomDynamicResult object
     *
     * @param  idx  Description of the Parameter
     * @return          The stringValue value
     */
    public String getStringValue(int idx)
    {
        String value = null;

        if (array instanceof double[])
        {
            value = DecimalFormatHelper.instance().format(
                    ((double[]) array)[idx - 1]);
        }
        else if (array instanceof int[])
        {
            value = Integer.toString(((int[]) array)[idx - 1]);
        }
        else if (array instanceof boolean[])
        {
            value = ((((boolean[]) array)[idx - 1] == true) ? "1" : "0");
        }

        return value;
    }

    /**
     *  Gets the value attribute of the AtomDynamicResult object
     *
     * @param  idx  Description of the Parameter
     * @return          The value value
     */
    public Object getValue(int idx)
    {
        Object value = null;

        if (array instanceof double[])
        {
            value = new Double(((double[]) array)[idx - 1]);
        }
        else if (array instanceof int[])
        {
            value = new Integer(((int[]) array)[idx - 1]);
        }
        else if (array instanceof boolean[])
        {
            if (((boolean[]) array)[idx - 1])
            {
                value = Boolean.TRUE;
            }
            else
            {
                value = Boolean.FALSE;
            }
        }

        return value;
    }

    /**
     *  Sets the doubleValue attribute of the AtomDynamicResult object
     *
     * @param  idx  The new doubleValue value
     * @param  _value   The new doubleValue value
     */
    public void setDoubleValue(int idx, double _value)
    {
        if (array instanceof double[])
        {
            ((double[]) array)[idx - 1] = _value;
        }
        else if (array instanceof int[])
        {
            ((int[]) array)[idx - 1] = (int) _value;
        }
        else if (array instanceof boolean[])
        {
            ((boolean[]) array)[idx - 1] = ((_value != 0.0) ? true : false);
        }
    }

    /**
     *  Sets the intValue attribute of the AtomDynamicResult object
     *
     * @param  idx  The new intValue value
     * @param  _value   The new intValue value
     */
    public void setIntValue(int idx, int _value)
    {
        if (array instanceof double[])
        {
            ((double[]) array)[idx - 1] = (double) _value;
        }
        else if (array instanceof int[])
        {
            ((int[]) array)[idx - 1] = _value;
        }
        else if (array instanceof boolean[])
        {
            ((boolean[]) array)[idx - 1] = ((_value != 0) ? true : false);
        }
    }

    /**
     *  Sets the stringValue attribute of the AtomDynamicResult object
     *
     * @param  idx  The new stringValue value
     * @param  _value   The new stringValue value
     */
    public void setStringValue(int idx, String _value)
    {
        if (array instanceof double[])
        {
            ((double[]) array)[idx - 1] = Double.parseDouble(_value);
        }
        else if (array instanceof int[])
        {
            ((int[]) array)[idx - 1] = Integer.parseInt(_value);
        }
        else if (array instanceof boolean[])
        {
            ((boolean[]) array)[idx - 1] = ((_value.equals("1") ||
                        _value.equals("true")) ? true : false);
        }
    }

    /**
     *  Sets the value attribute of the AtomDynamicResult object
     *
     * @param  idx  The new value value
     * @param  _value   The new value value
     */
    public void setValue(int idx, Object _value)
    {
        if (array instanceof double[])
        {
            ((double[]) array)[idx - 1] = ((Double) _value).doubleValue();
        }
        else if (array instanceof int[])
        {
            ((int[]) array)[idx - 1] = ((Integer) _value).intValue();
        }
        else if (array instanceof boolean[])
        {
            ((boolean[]) array)[idx - 1] = ((Boolean) _value).booleanValue();
        }
    }
}

///////////////////////////////////////////////////////////////////////////////
//  END OF FILE.
///////////////////////////////////////////////////////////////////////////////
