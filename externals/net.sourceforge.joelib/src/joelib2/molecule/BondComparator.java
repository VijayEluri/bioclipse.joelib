///////////////////////////////////////////////////////////////////////////////
//  Filename: $RCSfile: BondComparator.java,v $
//  Purpose:  Atom representation.
//  Language: Java
//  Compiler: JDK 1.4
//  Authors:  Joerg Kurt Wegner
//  Version:  $Revision: 1.6 $
//            $Date: 2005/02/17 16:48:36 $
//            $Author: wegner $
//  Original Author: ???, OpenEye Scientific Software
//  Original Version: babel 2.0a1
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
package joelib2.molecule;

import java.util.Comparator;


/**
 *  {@link Comparator} for bonds.
 *
 * @.author     wegnerj
 * @.wikipedia Chemical bond
 * @.license GPL
 * @.cvsversion    $Revision: 1.6 $, $Date: 2005/02/17 16:48:36 $
 * @see        Comparator
 * @see        java.util.Arrays#sort(Object[], Comparator)
 * @see        joelib2.sort.InsertSort
 * @see        joelib2.sort.QuickInsertSort
 */
public class BondComparator implements Comparator
{
    //~ Constructors ///////////////////////////////////////////////////////////

    /**
     *  Initializes the bond comparator.
     */
    public BondComparator()
    {
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     *  Compares two objects.
     *
     * @param  o1                   the first object to be compared.
     * @param  o2                   the second object to be compared.
     * @return                      a negative integer, zero, or a positive
     *      integer as the first argument is less than, equal to, or greater than
     *      the second.
     * @throws  ClassCastException  if the arguments' types prevent them from
     *      being compared by this Comparator.
     */
    public int compare(Object o1, Object o2) throws ClassCastException
    {
        if ((o1 instanceof Bond) || (o2 instanceof Bond))
        {
            throw new ClassCastException("Objects must of type BondComparator");
        }

        Bond b1 = (Bond) o1;
        Bond b2 = (Bond) o2;

        if (b1.getBeginIndex() == b2.getBeginIndex())
        {
            return b1.getEndIndex() - b2.getEndIndex();
        }

        return b1.getBeginIndex() - b2.getBeginIndex();
    }

    /**
     *  Indicates whether some other object is &quot;equal to&quot; this
     *  Comparator. This method must obey the general contract of <tt>
     *  Object.equals(Object)</tt> . Additionally, this method can return <tt>true
     *  </tt> <i>only</i> if the specified Object is also a comparator and it
     *  imposes the same ordering as this comparator. Thus, <tt>comp1.equals(comp2)</tt>
     *  implies that <tt>sgn(comp1.compare(o1, o2))==sgn(comp2.compare(o1, o2))
     *  </tt> for every object reference <tt>o1</tt> and <tt>o2</tt> .<p>
     *
     *  Note that it is <i>always</i> safe <i>not</i> to override <tt>
     *  Object.equals(Object)</tt> . However, overriding this method may, in some
     *  cases, improve performance by allowing programs to determine that two
     *  distinct Comparators impose the same order.
     *
     * @param  obj  the reference object with which to compare.
     * @return      <tt>true</tt> only if the specified object is also a
     *      comparator and it imposes the same ordering as this comparator.
     * @see         java.lang.Object#equals(java.lang.Object)
     * @see         java.lang.Object#hashCode()
     */
    public boolean equals(Object obj)
    {
        if (obj instanceof BondComparator)
        {
            return true;
        }

        return false;
    }

    public int hashCode()
    {
        //nothing to hash
        return 0;
    }
}

///////////////////////////////////////////////////////////////////////////////
//  END OF FILE.
///////////////////////////////////////////////////////////////////////////////
