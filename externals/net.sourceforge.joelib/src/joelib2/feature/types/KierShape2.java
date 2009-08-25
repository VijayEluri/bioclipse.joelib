///////////////////////////////////////////////////////////////////////////////
//  Filename: $RCSfile: KierShape2.java,v $
//  Purpose:  Calculates the Kier Shape for paths with length two.
//  Language: Java
//  Compiler: JDK 1.4
//  Authors:  Joerg Kurt Wegner
//  Version:  $Revision: 1.9 $
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
package joelib2.feature.types;

import joelib2.data.IdentifierExpertSystem;

import joelib2.feature.AbstractDouble;
import joelib2.feature.BasicFeatureInfo;
import joelib2.feature.FeatureHelper;

import joelib2.feature.types.atomlabel.AtomIsHydrogen;

import joelib2.molecule.Atom;
import joelib2.molecule.Molecule;

import joelib2.util.iterator.AtomIterator;
import joelib2.util.iterator.NbrAtomIterator;

import org.apache.log4j.Category;


/**
 *  Calculates the Kier Shape for paths with length two.
 *
 * @.author     wegnerj
 * @.license    GPL
 * @.cvsversion    $Revision: 1.9 $, $Date: 2005/02/17 16:48:31 $
 */
public class KierShape2 extends AbstractDouble
{
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final String VENDOR = "http://joelib.sf.net";
    private static final String RELEASE_VERSION = "$Revision: 1.9 $";
    private static final String RELEASE_DATE = "$Date: 2005/02/17 16:48:31 $";
    private static Category logger = Category.getInstance(KierShape2.class
            .getName());
    private static final Class[] DEPENDENCIES =
        new Class[]{AtomIsHydrogen.class};

    //~ Constructors ///////////////////////////////////////////////////////////

    /**
     *  Constructor for the KierShape2 object
     */
    public KierShape2()
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Initialize " + this.getClass().getName());
        }

        descInfo = FeatureHelper.generateFeatureInfo(this.getClass(),
                BasicFeatureInfo.TYPE_NO_COORDINATES, null,
                "joelib2.feature.result.DoubleResult");
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public static Class[] getDependencies()
    {
        return DEPENDENCIES;
    }

    public static String getName()
    {
        return KierShape2.class.getName();
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

    /**
     * Gets the doubleValue attribute of the KierShape2 object
     *
     * @param mol  Description of the Parameter
     * @return     The doubleValue value
     */
    public double getDoubleValue(Molecule mol)
    {
        double nodes = 0;
        AtomIterator ait = mol.atomIterator();
        NbrAtomIterator nbrait;
        NbrAtomIterator nbrait2;
        Atom node;
        Atom nbrNode;
        Atom nbrNode2;
        double p = 0;
        double kier;

        while (ait.hasNext())
        {
            node = ait.nextAtom();

            //Iteration over all nodes, "node" is the current node of the Iteration
            if (!AtomIsHydrogen.isHydrogen(node))
            {
                //Graph should be Hydrogens depleted
                nodes++;
                nbrait = node.nbrAtomIterator();

                //Iteration over all NeighborAtoms of the current node
                while (nbrait.hasNext())
                {
                    nbrNode = nbrait.nextNbrAtom();
                    nbrait2 = nbrNode.nbrAtomIterator();

                    while (nbrait2.hasNext())
                    {
                        //Iteration over all Neighbor Atoms form current NeighborAtom
                        nbrNode2 = nbrait2.nextNbrAtom();

                        if ((!AtomIsHydrogen.isHydrogen(nbrNode2)) &&
                                (nbrNode2.getIndex() != node.getIndex()))
                        {
                            p++;
                        }
                    }
                }
            }
        }

        p = (p / 2);

        //each path has been counted twice, so divide by two
        if (p > 0)
        {
            kier = (((nodes - 1) * ((nodes - 2) * (nodes - 2))) / (p * p));
        }
        else
        {
            return 0.0;
        }

        //System.out.println("Kier2 paths: " +p +"\nNodes: " +nodes);
        return kier;
    }

    public int hashedDependencyTreeVersion()
    {
        return IdentifierExpertSystem.getDependencyTreeHash(getName());
    }
}

///////////////////////////////////////////////////////////////////////////////
//  END OF FILE.
///////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
//  END OF FILE.
///////////////////////////////////////////////////////////////////////////////
