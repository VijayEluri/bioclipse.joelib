///////////////////////////////////////////////////////////////////////////////
//  Filename: $RCSfile: AtomIsAmideNitrogen.java,v $
//  Purpose:  Calculates a descriptor.
//  Language: Java
//  Compiler: JDK 1.4
//  Authors:  Joerg Kurt Wegner
//  Version:  $Revision: 1.11 $
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
import joelib2.feature.FeatureHelper;

import joelib2.feature.result.DynamicArrayResult;

import joelib2.molecule.Atom;
import joelib2.molecule.Bond;
import joelib2.molecule.Molecule;

import joelib2.molecule.types.AtomProperties;

import joelib2.util.iterator.BondIterator;

import java.util.zip.DataFormatException;

import org.apache.log4j.Category;


/**
 * Is this atom negatively charged atom.
 *
 * @.author wegnerj
 * @.license GPL
 * @.cvsversion $Revision: 1.11 $, $Date: 2005/02/17 16:48:31 $
 */
public class AtomIsAmideNitrogen extends AbstractDynamicAtomProperty
{
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final String VENDOR = "http://joelib.sf.net";
    private static final String RELEASE_VERSION = "$Revision: 1.11 $";
    private static final String RELEASE_DATE = "$Date: 2005/02/17 16:48:31 $";
    private static Category logger = Category.getInstance(
            AtomIsAmideNitrogen.class.getName());

    private static final Class[] DEPENDENCIES =
        new Class[]
        {
            AtomIsNitrogen.class, AtomIsOxygen.class, AtomIsSulfur.class
        };

    //~ Constructors ///////////////////////////////////////////////////////////

    public AtomIsAmideNitrogen()
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

    public static String getName()
    {
        return AtomIsAmideNitrogen.class.getName();
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
        int atomsSize = mol.getAtomsSize();
        boolean[] amideN = (boolean[]) DynamicArrayResult.getNewArray(
                DynamicArrayResult.BOOLEAN, atomsSize);

        Atom atom;

        for (int atomIdx = 1; atomIdx <= atomsSize; atomIdx++)
        {
            atom = mol.getAtom(atomIdx);
            amideN[atomIdx - 1] = isAmideNitrogen(atom);
        }

        return amideN;
    }

    public int hashedDependencyTreeVersion()
    {
        return IdentifierExpertSystem.getDependencyTreeHash(getName());
    }

    /**
     * Returns <tt>true</tt> if nitrogen is part of an amide.
     *
     * @return <tt>true</tt> if nitrogen is part of an amide
     */
    public boolean isAmideNitrogen(Atom thisAtom)
    {
        boolean isAmidN = false;

        // used cached version, if available
        if (thisAtom.getParent().hasData(getName()))
        {
            AtomProperties labelCache = (AtomProperties) thisAtom.getParent()
                                                                 .getData(
                    getName());

            if (labelCache != null)
            {
                try
                {
                    if (labelCache.getIntValue(thisAtom.getIndex()) != 0)
                    {
                        isAmidN = true;
                    }
                }
                catch (DataFormatException e)
                {
                    logger.error(e.getMessage());
                }
            }
        }
        else
        {
            if (!AtomIsNitrogen.isNitrogen(thisAtom))
            {
                isAmidN = false;
            }
            else
            {
                Atom nbratom;
                Bond abbond;
                Bond bond;
                BondIterator bit1 = thisAtom.bondIterator();

                while (bit1.hasNext())
                {
                    bond = bit1.nextBond();
                    nbratom = bond.getNeighbor(thisAtom);

                    BondIterator bit2 = nbratom.bondIterator();

                    while (bit2.hasNext())
                    {
                        abbond = bit2.nextBond();

                        boolean isOorS =
                            (AtomIsOxygen.isOxygen(abbond.getNeighbor(nbratom)) ||
                                AtomIsSulfur.isSulfur(
                                    abbond.getNeighbor(nbratom)));

                        if ((abbond.getBondOrder() == 2) && isOorS)
                        {
                            isAmidN = true;

                            break;
                        }

                        if (isAmidN)
                        {
                            break;
                        }
                    }
                }
            }
        }

        return isAmidN;
    }
}

///////////////////////////////////////////////////////////////////////////////
//  END OF FILE.
///////////////////////////////////////////////////////////////////////////////
