///////////////////////////////////////////////////////////////////////////////
//  Filename: $RCSfile: Ghemical.java,v $
//  Purpose:  Reader/Writer for Undefined files.
//  Language: Java
//  Compiler: JDK 1.4
//  Authors:  Joerg Kurt Wegner
//  Version:  $Revision: 1.8 $
//            $Date: 2005/02/17 16:48:34 $
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
package joelib2.io.types;

import cformat.PrintfStream;

import joelib2.io.MoleculeFileIO;
import joelib2.io.MoleculeIOException;

import joelib2.molecule.Molecule;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;

import org.apache.log4j.Category;


/**
 * Atom representation.
 *
 * @.wikipedia  Chemical file format
 * @.wikipedia  File format
 *
 */
public class Ghemical implements MoleculeFileIO
{
    //~ Static fields/initializers /////////////////////////////////////////////

    /**
     *  Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance(
            "joelib2.io.types.Ghemical");
    private static final String description = "Ghemical MM";
    private static final String[] extensions = new String[]{"mmlgp", "qmlgp"};

    //~ Instance fields ////////////////////////////////////////////////////////

    private LineNumberReader lnr;
    private PrintfStream ps;

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     *  Description of the Method
     *
     * @exception  IOException  Description of the Exception
     */
    public void closeReader() throws IOException
    {
        lnr.close();
    }

    /**
     *  Description of the Method
     *
     * @exception  IOException  Description of the Exception
     */
    public void closeWriter() throws IOException
    {
        ps.close();
    }

    public void initReader(InputStream is) throws IOException
    {
        lnr = new LineNumberReader(new InputStreamReader( /*(ZipInputStream)*/
                    is));
    }

    /**
     *  Description of the Method
     *
     * @param  os               Description of the Parameter
     * @exception  IOException  Description of the Exception
     */
    public void initWriter(OutputStream os) throws IOException
    {
        ps = new PrintfStream(os);
    }

    public String inputDescription()
    {
        return description;
    }

    public String[] inputFileExtensions()
    {
        return extensions;
    }

    public String outputDescription()
    {
        return description;
    }

    public String[] outputFileExtensions()
    {
        return extensions;
    }

    /**
     *  Reads an molecule entry as (unparsed) <tt>String</tt> representation.
     *
     * @return                  <tt>null</tt> if the reader contains no more
     *      relevant data. Otherwise the <tt>String</tt> representation of the
     *      whole molecule entry is returned.
     * @exception  IOException  typical IOException
     */
    public String read() throws IOException
    {
        logger.error(
            "Reading Ghemical data as String representation is not implemented yet !!!");

        return null;
    }

    /**
     *  Description of the Method
     *
     * @param  mol                      Description of the Parameter
     * @return                          Description of the Return Value
     * @exception  IOException          Description of the Exception
     * @exception  MoleculeIOException  Description of the Exception
     */
    public synchronized boolean read(Molecule mol) throws IOException,
        MoleculeIOException
    {
        return read(mol, null);
    }

    /**
     *  Loads an molecule in MDL SD-MOL format and sets the title. If <tt>title
     *  </tt> is <tt>null</tt> the title line in the molecule file is used.
     *
     * @param  mol                      Description of the Parameter
     * @param  title                    Description of the Parameter
     * @return                          Description of the Return Value
     * @exception  IOException          Description of the Exception
     * @exception  MoleculeIOException  Description of the Exception
     */
    public synchronized boolean read(Molecule mol, String title)
        throws IOException, MoleculeIOException
    {
        /*                int i;
                        int natoms, nbonds;
                        char buffer[BUFF_SIZE];
                        string str, str1;
                        float x, y, z;
                        OEAtom * atom;
                        vector < string > vs;
                        char bobuf[100];
                        string bostr;
                        int bgn, end, order;
                        bool hasPartialCharges = false;

                        mol.BeginModify();
                        ttab.SetFromType("ATN");
                        ttab.SetToType("INT");

                        // Get !Header line with version number
                        ifs.getline(buffer, BUFF_SIZE);
                        sscanf(buffer, "%*s %*s %d", & i);
                        if (!i || i > 100)
                                return false;

                        // Get !Info line with number of coord sets
                        ifs.getline(buffer, BUFF_SIZE);
                        sscanf(buffer, "%*s %d", & i);
                        if (!i || i != 1)
                                return false;

                        // Get !Atoms line with number
                        ifs.getline(buffer, BUFF_SIZE);
                        sscanf(buffer, "%*s %d", & natoms);
                        if (!natoms)
                                return (false);

                        for (i = 1; i <= natoms; i++)
                        {
                                if (!ifs.getline(buffer, BUFF_SIZE))
                                        return (false);
                                tokenize(vs, buffer);
                                if (vs.size() != 2)
                                        return (false);
                                atom = mol.NewAtom();
                                atom - > SetAtomicNum(atoi(vs[1].c_str()));
                                ttab.Translate(str, vs[1]);
                                atom - > SetType(str);
                        }

                        // Get !Bonds line with number
                        ifs.getline(buffer, BUFF_SIZE);
                        sscanf(buffer, "%*s %d", & nbonds);
                        if (nbonds != 0)
                                for (i = 0; i < nbonds; i++)
                                {
                                        if (!ifs.getline(buffer, BUFF_SIZE))
                                                return (false);
                                        sscanf(buffer, "%d%d%s", & bgn, & end, bobuf);
                                        bostr = bobuf;
                                        order = 1;
                                        if (bostr == "D")
                                                order = 2;
                                        else if (bostr == "T")
                                                order = 3;
                                        else if (bostr == "C")
                                                order = Bond.JOE_AROMATIC_BOND_ORDER; // Conjugated ~= Aromatic
                                        mol.AddBond(bgn + 1, end + 1, order);
                                }

                        // Get !Coord line
                        ifs.getline(buffer, BUFF_SIZE);
                        for (i = 1; i <= natoms; i++)
                        {
                                if (!ifs.getline(buffer, BUFF_SIZE))
                                        return (false);
                                tokenize(vs, buffer);
                                if (vs.size() != 4)
                                        return (false);
                                atom = mol.GetAtom(i);
                                x = 10.0 * atof((char *) vs[1].c_str());
                                y = 10.0 * atof((char *) vs[2].c_str());
                                z = 10.0 * atof((char *) vs[3].c_str());
                                atom - > SetVector(x, y, z); //set coordinates
                        }

                        if (ifs.getline(buffer, BUFF_SIZE) && strstr(buffer, "!Charges") != NULL)
                        {
                                hasPartialCharges = true;
                                for (i = 1; i <= natoms; i++)
                                {
                                        if (!ifs.getline(buffer, BUFF_SIZE))
                                                return (false);
                                        tokenize(vs, buffer);
                                        if (vs.size() != 2)
                                                return (false);
                                        atom = mol.GetAtom(i);
                                        atom - > SetPartialCharge(atof((char *) vs[1].c_str()));
                                }
                        }

                        mol.EndModify();
                        if (hasPartialCharges)
                                mol.SetPartialChargesPerceived();
                        mol.SetTitle(title);
        */
        return (true);
    }

    public boolean readable()
    {
        return true;
    }

    /**
     *  Description of the Method
     *
     * @return                  Description of the Return Value
     * @exception  IOException  Description of the Exception
     */
    public boolean skipReaderEntry() throws IOException
    {
        return true;
    }

    /**
     *  Description of the Method
     *
     * @param  mol              Description of the Parameter
     * @return                  Description of the Return Value
     * @exception  IOException  Description of the Exception
     */
    public boolean write(Molecule mol) throws IOException
    {
        return write(mol, null);
    }

    /**
     *  Description of the Method
     *
     * @param  mol              Description of the Parameter
     * @param  title            Description of the Parameter
     * @return                  Description of the Return Value
     * @exception  IOException  Description of the Exception
     */
    public boolean write(Molecule mol, String title) throws IOException
    {
        /*                unsigned int i;
                        char buffer[BUFF_SIZE];
                        char bond_char;

                        // Ghemical header -- here "version 1.0" format
                        ofs << "!Header mm1gp 100" << endl;
                        ttab.SetFromType("INT");
                        ttab.SetToType("ATN");

                        // Number of coordinate sets
                        ofs << "!Info 1" << endl;

                        // Atom definitions
                        sprintf(buffer, "!Atoms %d", mol.NumAtoms());
                        ofs << buffer << endl;

                        OEAtom * atom;
                        string str, str1;
                        for (i = 1; i <= mol.NumAtoms(); i++)
                        {
                                atom = mol.GetAtom(i);
                                ofs << (i - 1) << " " << atom - > GetAtomicNum() << endl;
                        }

                        // Bond definitions
                        sprintf(buffer, "!Bonds %d", mol.NumBonds());
                        ofs << buffer << endl;

                        OEBond * bond;
                        vector < OEBond * > : : iterator j;

                        for (bond = mol.BeginBond(j); bond; bond = mol.NextBond(j))
                        {
                                switch (bond - > GetBO())
                                {
                                        case 1 :
                                                bond_char = 'S';
                                                break;
                                        case 2 :
                                                bond_char = 'D';
                                                break;
                                        case 3 :
                                                bond_char = 'T';
                                                break;
                                        case 4 :
                                                bond_char = 'C';
                                                break;
                                        case Bond.JOE_AROMATIC_BOND_ORDER :
                                                bond_char = 'C';
                                                break;
                                        default :
                                                bond_char = 'S';
                                }
                                sprintf(buffer, "%d %d %c", bond - > GetBeginAtomIdx() - 1, bond - > GetEndAtomIdx() - 1, bond_char);
                                ofs << buffer << endl;
                        }

                        // Coordinate sets (here only 1)
                        ofs << "!Coord" << endl;

                        for (i = 1; i <= mol.NumAtoms(); i++)
                        {
                                atom = mol.GetAtom(i);
                                sprintf(
                                        buffer,
                                        "%d %f %f %f",
                                        i - 1,
                                        atom - > GetX() / 10.0,
                                        atom - > GetY() / 10.0,
                                        atom - > GetZ() / 10.0);

                                ofs << buffer << endl;
                        }

                        // Calculated charges
                        ofs << "!Charges" << endl;

                        for (i = 1; i <= mol.NumAtoms(); i++)
                        {
                                atom = mol.GetAtom(i);
                                sprintf(buffer, "%d %f", i - 1, atom - > GetPartialCharge());

                                ofs << buffer << endl;
                        }

                        ofs << "!End" << endl;
        */
        return (true);
    }

    public boolean writeable()
    {
        return true;
    }
}

///////////////////////////////////////////////////////////////////////////////
//  END OF FILE.
///////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
//  END OF FILE.
///////////////////////////////////////////////////////////////////////////////
