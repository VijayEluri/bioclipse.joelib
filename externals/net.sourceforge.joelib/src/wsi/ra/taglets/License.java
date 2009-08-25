///////////////////////////////////////////////////////////////////////////////
//  Filename: $RCSfile: License.java,v $
//  Purpose:  Atom representation.
//  Language: Java
//  Compiler: JDK 1.4
//  Authors:  Joerg Kurt Wegner
//  Version:  $Revision: 1.6 $
//            $Date: 2005/02/17 16:48:43 $
//            $Author: wegner $
//  Original Author: Patrick Tullmann <taglets@tullmann.org>
//  Original Version: ???
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
package wsi.ra.taglets;

import java.util.HashMap;
import java.util.Map;

import com.sun.javadoc.Tag;


/**
 * A Taglet that defines the <code>@.license</code> tag for Javadoc
 * comments.
 *
 * <p>Defined at package and type scope (not field, method, etc).
 *
 * @todo Finish this tag
 * @todo Support source license and doc license.
 * @todo Support preferences-based customization.
 * @.author Patrick Tullmann &lt;<a href="mailto:taglets@tullmann.org">taglets@tullmann.org</a>&gt;
 */
public class License extends ListTag
{
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final Map licenseTextMap = new HashMap(5);

    static
    {
        licenseTextMap.put("GPL",
            "Licensed under the terms of the <A href=\"http://www.gnu.org/copyleft/gpl.html\">GNU General Public License (GPL)</A>.");
        licenseTextMap.put("LGPL",
            "Licensed under the terms of the <A href=\"http://www.gnu.org/copyleft/library.html\">GNU Lesser General Public License (LGPL)</A>.");
        licenseTextMap.put("pubdom",
            "Released to the public domain.  No restrictions.");
    }

    //~ Constructors ///////////////////////////////////////////////////////////

    /**
     * Create a new License tag.
     */
    public License()
    {
        super(".license", "License:", ListTag.UNORDERED_LIST);
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Register this taglet with the given name.
     */
    public static void register(Map tagletMap)
    {
        ListTag.register(tagletMap, new License());
    }

    public boolean inConstructor()
    {
        return false;
    }

    public boolean inField()
    {
        return false;
    }

    public boolean inMethod()
    {
        return false;
    }

    public boolean inOverview()
    {
        return false;
    }

    public boolean inPackage()
    {
        return true;
    }

    public boolean inType()
    {
        return true;
    }

    public boolean isInlineTag()
    {
        return false;
    }

    public String toString(Tag tag)
    {
        StringBuffer sbuf = new StringBuffer(1000);

        // XXX make it an option to emit single entries with the list header/etc.
        startingTags();

        emitHeader(sbuf, false);
        emitTag(getLicenseTag(tag), sbuf, false);
        emitFooter(sbuf, false);

        endingTags(sbuf);

        return sbuf.toString();
    }

    public String toString(Tag[] tags)
    {
        if (tags.length == 0)
        {
            return "";
        }

        StringBuffer sbuf = new StringBuffer(200 + (800 * tags.length));

        startingTags();

        emitHeader(sbuf, true);

        for (int i = 0; i < tags.length; i++)
        {
            emitTag(getLicenseTag(tags[i]), sbuf, true);
        }

        emitFooter(sbuf, true);

        endingTags(sbuf);

        return sbuf.toString();
    }

    protected final String getLicenseTag(final Tag tag)
    {
        String expLicense = (String) licenseTextMap.get(tag.text());

        if (expLicense == null)
        {
            System.err.println("WARNING: Could not find license " + tag.text());
        }
        else
        {
            return expLicense;
        }

        return "";
    }
}

///////////////////////////////////////////////////////////////////////////////
//  END OF FILE.
///////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
//  END OF FILE.
///////////////////////////////////////////////////////////////////////////////
