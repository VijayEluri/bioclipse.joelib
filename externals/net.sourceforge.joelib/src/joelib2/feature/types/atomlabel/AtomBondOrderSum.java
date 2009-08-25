///////////////////////////////////////////////////////////////////////////////
//  Filename: $RCSfile: AtomBondOrderSum.java,v $
//  Purpose:  Atom valence.
//  Language: Java
//  Compiler: JDK 1.4
//  Authors:  Joerg Kurt Wegner
//  Version:  $Revision: 1.13 $
//            $Date: 2005/02/17 16:48:31 $
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
package joelib2.feature.types.atomlabel;

import joelib2.data.IdentifierExpertSystem;

import joelib2.feature.AbstractDynamicAtomProperty;
import joelib2.feature.BasicFeatureInfo;
import joelib2.feature.FeatureException;
import joelib2.feature.FeatureHelper;

import joelib2.feature.result.DynamicArrayResult;

import joelib2.molecule.Atom;
import joelib2.molecule.Molecule;

import joelib2.molecule.types.AtomPropertyHelper;

import joelib2.util.iterator.AtomIterator;

import org.apache.log4j.Category;


/**
 * Atom valence.
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.13 $, $Date: 2005/02/17 16:48:31 $
 */
public class AtomBondOrderSum extends AbstractDynamicAtomProperty
{
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final String VENDOR = "http://joelib.sf.net";
    private static final String RELEASE_VERSION = "$Revision: 1.13 $";
    private static final String RELEASE_DATE = "$Date: 2005/02/17 16:48:31 $";
    private static Category logger = Category.getInstance(AtomBondOrderSum.class
            .getName());

    private static final Class[] DEPENDENCIES = new Class[]{};

    //~ Constructors ///////////////////////////////////////////////////////////

    /**
     *  Constructor for the KierShape1 object
     */
    public AtomBondOrderSum()
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Initialize " + this.getClass().getName());
        }

        descInfo = FeatureHelper.generateFeatureInfo(this.getClass(),
                BasicFeatureInfo.TYPE_NO_COORDINATES, null,
                joelib2.feature.result.AtomDynamicResult.class.getName());
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public static Class[] getDependencies()
    {
        return DEPENDENCIES;
    }

    /**
     *  Description of the Method
     *
     * @param  atom  Description of the Parameter
     * @return       Description of the Return Value
     */
    public static int getIntValue(Atom atom)
    {
        int bosum = 0;

        try
        {
            bosum = AtomPropertyHelper.getIntAtomProperty(atom, getName(),
                    true);
        }
        catch (FeatureException e1)
        {
            logger.error(e1.getMessage());
        }

        return bosum;
    }

    public static String getName()
    {
        return AtomBondOrderSum.class.getName();
    }

    public static String getReleaseDate()
    {
        return VENDOR;
    }

    public static String getReleaseVersion()
    {
        return IdentifierExpertSystem.transformCVStag(RELEASE_VERSION);
    }

    public static String getVendor()
    {
        return IdentifierExpertSystem.transformCVStag(RELEASE_DATE);
    }

    public Object getAtomPropertiesArray(Molecule mol)
    {
        int[] boSum = (int[]) DynamicArrayResult.getNewArray(
                DynamicArrayResult.INT, mol.getAtomsSize());
        int arrIdx = 0;
        Atom atom;
        AtomIterator ait = mol.atomIterator();

        while (ait.hasNext())
        {
            atom = ait.nextAtom();
            boSum[arrIdx++] = AbstractBondOrderSum.calculate(atom);
        }

        return boSum;
    }

    public int hashedDependencyTreeVersion()
    {
        return IdentifierExpertSystem.getDependencyTreeHash(getName());
    }
}
///////////////////////////////////////////////////////////////////////////////
//  END OF FILE.
///////////////////////////////////////////////////////////////////////////////
