///////////////////////////////////////////////////////////////////////////////
//Filename: $RCSfile: MoleculeFileExport.java,v $
//Purpose:  TODO description.
//Language: Java
//Compiler: JDK 1.4
//Authors:  Joerg Kurt Wegner
//Version:  $Revision: 1.3 $
//        $Date: 2005/02/17 16:48:34 $
//        $Author: wegner $
//
//Copyright OELIB:          OpenEye Scientific Software, Santa Fe,
//                       U.S.A., 1999,2000,2001
//Copyright JOELIB/JOELib2: Dept. Computer Architecture, University of
//                       Tuebingen, Germany, 2001,2002,2003,2004,2005
//Copyright JOELIB/JOELib2: ALTANA PHARMA AG, Konstanz, Germany,
//                       2003,2004,2005
//
//This program is free software; you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation version 2 of the License.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
///////////////////////////////////////////////////////////////////////////////
package joelib2.io;

import joelib2.molecule.Molecule;

import java.io.IOException;
import java.io.OutputStream;


/**
 * TODO description.
 *
 * @.author     wegnerj
 * @.wikipedia  Chemical file format
 * @.wikipedia  File format
 * @.license    GPL
 * @.cvsversion $Revision: 1.3 $, $Date: 2005/02/17 16:48:34 $
 */
public interface MoleculeFileExport
{
    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     *  Description of the Method
     *
     * @exception  IOException  Description of the Exception
     */
    void closeWriter() throws IOException;

    /**
     *  Description of the Method
     *
     * @param  os               Description of the Parameter
     * @exception  IOException  Description of the Exception
     */
    void initWriter(OutputStream os) throws IOException;

    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    String outputDescription();

    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    String[] outputFileExtensions();

    /**
     *  Reads an molecule entry from the initialized molecule reader.
     *
     * @param  mol                        the molecule to store the data
     * @return                            <tt>false</tt> if the reader contains no
     *      more relevant data. <tt>true</tt> if a molecule was succesfull readed.
     *      If the molecule is incorrect or can't be parsed use <tt>
     *      MoleculeIOException</tt>.
     * @exception  IOException            typical IOException
     * @exception  MoleculeIOException  If an serious molecule parsing error
     *      occured. This is important to skip molecule entries.
     */
    boolean write(Molecule mol) throws IOException, MoleculeIOException;

    /**
     *  Reads an molecule entry from the initialized molecule reader.
     *
     * @param  mol                        the molecule to store the data
     * @param  title                      the title of the molecule. If
     *                                    <tt>title==null</tt> the title
     *                                    entry from the molecule <tt>mol.getTitle()</tt>
     *                                    should be used
     * @return                            <tt>false</tt> if the reader contains no
     *      more relevant data. <tt>true</tt> if a molecule was succesfull readed.
     *      If the molecule is incorrect or can't be parsed use <tt>
     *      MoleculeIOException</tt>.
     * @exception  IOException            typical IOException
     * @exception  MoleculeIOException  If an serious molecule parsing error
     *      occured. This is important to skip molecule entries.
     */
    boolean write(Molecule mol, String title) throws IOException,
        MoleculeIOException;

    /**
     * Returns <tt>true</tt> if this molecule data type is writeable.
     *
     * @return    Description of the Return Value
     */
    boolean writeable();
}

///////////////////////////////////////////////////////////////////////////////
//END OF FILE.
///////////////////////////////////////////////////////////////////////////////
