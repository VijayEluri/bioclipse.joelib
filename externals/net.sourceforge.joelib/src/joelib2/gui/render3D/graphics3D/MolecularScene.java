///////////////////////////////////////////////////////////////////////////////
//  Filename: $RCSfile: MolecularScene.java,v $
//  Purpose:  Counts the number of descriptors and molecules in a molecule file.
//  Language: Java
//  Compiler: JDK 1.4
//  Authors:  Joerg Kurt Wegner
//  Version:  $Revision: 1.6 $
//            $Date: 2005/02/17 16:48:33 $
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
package joelib2.gui.render3D.graphics3D;

import joelib2.gui.render3D.molecule.ViewerAtom;
import joelib2.gui.render3D.molecule.ViewerAtoms;
import joelib2.gui.render3D.molecule.ViewerMolecule;
import joelib2.gui.render3D.util.MolViewerEvent;
import joelib2.gui.render3D.util.MolViewerEventListener;

import joelib2.molecule.Molecule;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingLeaf;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Group;
import javax.media.j3d.LinearFog;
import javax.media.j3d.Locale;
import javax.media.j3d.Node;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.Switch;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.media.j3d.VirtualUniverse;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;


/**
 * Description of the Class
 *
 * @.author     wegnerj
 * @.license    GPL
 * @.cvsversion    $Revision: 1.6 $, $Date: 2005/02/17 16:48:33 $
 */
public class MolecularScene
{
    //~ Instance fields ////////////////////////////////////////////////////////

    /** This field holds a list of registered ActionListeners.
     *  Vector is internally synchronized to prevent race conditions
     * @serial
     */
    protected List listeners = new Vector();
    private BoundingSphere bounds = null;
    private Canvas3D canvas;
    private BranchGroup fastGroup;
    private Hashtable fastNodes;
    private LinearFog fog = null;
    private boolean isFast = false;
    private boolean isWire = false;
    private Locale locale;
    private BranchGroup localeRoot = null;
    private Hashtable molecules = new Hashtable();
    private BranchGroup niceGroup;
    private Hashtable niceNodes;
    private List others = new Vector();
    private PickHighlightBehavior pickBehFast;
    private PickHighlightBehavior pickBehNice;
    private BranchGroup root = null;
    private TransformGroup rootTrans = null;
    private RenderTable rTable = RenderTable.getTable();
    private TransformGroup sceneOffset;
    private Switch sceneSwitch;
    private TransformGroup sceneTrans;
    private VirtualUniverse universe;
    private View view;

    //~ Constructors ///////////////////////////////////////////////////////////

    /**
     *Constructor for the MolecularScene object
     *
     * @param c  Description of the Parameter
     */
    public MolecularScene(Canvas3D c)
    {
        this.canvas = c;

        niceNodes = new Hashtable();
        fastNodes = new Hashtable();

        createUniverse();

        initSceneGraph();

        initLighting();

        // Attach the branch graph to the universe, via the Locale.
        // The scene graph is now live!
        root.compile();
        localeRoot.addChild(root);
        locale.addBranchGraph(localeRoot);
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Adds a feature to the Molecule attribute of the MolecularScene object
     *
     * @param m  The feature to be added to the Molecule attribute
     */
    public void addMolecule(Molecule mol)
    {
        ViewerMolecule m = new ViewerMolecule(mol);
        molecules.put(mol, m);

        detachRoot();
        centerMolecule(m);
        setBounds(m);

        MoleculeNode node = new MoleculeNode(m);
        niceNodes.put(m, node);

        node.compile();
        niceGroup.addChild(node);

        node = new MoleculeNode(m, RenderStyle.WIRE);
        fastNodes.put(m, node);
        node.compile();
        fastGroup.addChild(node);
        root.compile();
        attachRoot();
    }

    // remove everything

    /**
     * Description of the Method
     */
    public void clear()
    {
        detachRoot();

        // find the child
        Enumeration e = niceNodes.keys();

        while (e.hasMoreElements())
        {
            ViewerMolecule m = (ViewerMolecule) e.nextElement();

            //System.out.println("clear "+m.getName());
            removeMolNode(m, niceNodes, niceGroup);
            removeMolNode(m, fastNodes, fastGroup);

            //removeMolecule(m);
        }

        niceNodes = null;
        fastNodes = null;
        molecules = null;
        RenderTable.getTable().clear();

        //              System.out.println("nice group children: "+niceGroup.numChildren());
        //              System.out.println("fast group children: "+fastGroup.numChildren());
        //              System.out.println("local root children: "+localeRoot.numChildren());
        //              System.out.println("root children: "+root.numChildren());
        niceNodes = new Hashtable();
        fastNodes = new Hashtable();
        molecules = new Hashtable();
        root.compile();
        attachRoot();

        if (pickBehNice != null)
        {
            pickBehNice.clearHighlight();
        }

        if (pickBehFast != null)
        {
            pickBehFast.clearHighlight();
        }
    }

    public Enumeration getMolecules()
    {
        return molecules.elements();
    }

    /**
     * Gets the fast attribute of the MolecularScene object
     *
     * @return   The fast value
     */
    public boolean isFast()
    {
        return isFast;
    }

    /**
     * Description of the Method
     *
     * @param m  Description of the Parameter
     */
    public void removeMolecule(Molecule mol)
    {
        ViewerMolecule m = (ViewerMolecule) molecules.remove(mol);

        removeMolecule(m);
    }

    public void removeMolecule(ViewerMolecule m)
    {
        //        Node n = (Node) niceNodes.get(m);
        if (m == null)
        {
            System.out.println("Could not find node for molecule: " + m);

            return;
        }

        detachRoot();
        removeMolNode(m, niceNodes, niceGroup);
        removeMolNode(m, fastNodes, fastGroup);
        root.compile();
        attachRoot();
    }

    /**
     * Sets the fast attribute of the MolecularScene object
     */
    public void setFast()
    {
        if (!isWire)
        {
            sceneSwitch.setWhichChild(1);
            isFast = true;
        }
    }

    /**
     * Sets the nice attribute of the MolecularScene object
     */
    public void setNice()
    {
        if (!isWire)
        {
            sceneSwitch.setWhichChild(0);
            isFast = false;
        }
    }

    /**
     * Sets the renderStyle attribute of the MolecularScene object
     *
     * @param style  The new renderStyle value
     */
    public void setRenderStyle(int style)
    {
        canvas.stopRenderer();

        if (style == RenderStyle.WIRE)
        {
            setFast();
            isWire = true;
        }
        else
        {
            isWire = false;
            rTable.setStyle(style);

            if (isFast)
            {
                setNice();
            }
        }

        canvas.startRenderer();

        //if(pickBehNice!=null)pickBehNice.clearHighlight();
        //if(pickBehFast!=null)pickBehFast.clearHighlight();
    }

    public void useAtomPropertyColoring(String atomPropertyName)
    {
        RenderTable.getTable().atomPropertyName = atomPropertyName;

        //              ViewerMolecule viewerMol;
        //              for (Enumeration e = molecules.elements(); e.hasMoreElements();)
        //              {
        //                      viewerMol=(ViewerMolecule)e.nextElement();
        //                      System.out.println("SET: "+viewerMol.getName()+" "+atomPropertyName);
        //                      viewerMol.getAtomPropertyColoring().useAtomPropertyColoring(viewerMol.getJOEMol(), atomPropertyName);
        //              }
    }

    /**
     * Register an action listener to be notified when a button is pressed
     */
    protected void addMolViewerEventListener(MolViewerEventListener l)
    {
        listeners.add(l);
    }

    /**
     * Send an event to all registered listeners
     */
    protected void fireEvent(MolViewerEvent e)
    {
        // Make a copy of the list and fire the events using that copy.
        // This means that listeners can be added or removed from the original
        // list in response to this event.  We ought to be able to just use an
        // enumeration for the vector, but that doesn't copy the list internally.
        List list = (List) ((Vector) listeners).clone();

        for (int i = 0; i < list.size(); i++)
        {
            MolViewerEventListener listener = (MolViewerEventListener) list.get(
                    i);

            switch (e.getType())
            {
            case MolViewerEvent.REPLACE_MOLECULE:
                listener.centralDisplayChange(e);

                break;

            case MolViewerEvent.ATOM_PICKED:
                listener.atomPicked(e);

                break;

            case MolViewerEvent.BOND_PICKED:
                listener.bondPicked(e);

                break;
            }
        }
    }

    /**
     * Remove an answer listener from our list of interested listeners
     */
    protected void removeMolViewerEventListener(MolViewerEventListener l)
    {
        listeners.remove(l);
    }

    /**
     * Adds a feature to the Node attribute of the MolecularScene object
     *
     * @param node  The feature to be added to the Node attribute
     */
    void addNode(Node node)
    {
        BranchGroup b = new BranchGroup();
        b.addChild(node);
        detachRoot();
        others.add(node);
        niceGroup.addChild(b);
        root.compile();
        attachRoot();
    }

    /**
     * Description of the Method
     */
    void attachRoot()
    {
        localeRoot.addChild(root);
    }

    void centerMolecule(Molecule mol)
    {
        ViewerMolecule m = (ViewerMolecule) molecules.get(mol);

        centerMolecule(m);
    }

    /**
     * Description of the Method
     *
     * @param m  Description of the Parameter
     */
    void centerMolecule(ViewerMolecule m)
    {
        m.findBB();

        float cenX = 0.5f * (m.getXmin() + m.getXmax());
        float cenY = 0.5f * (m.getYmin() + m.getYmax());
        float cenZ = 0.5f * (m.getZmin() + m.getZmax());
        ViewerAtoms av = m.getMyAtoms();
        int nAtoms = av.size();

        for (int i = 0; i < nAtoms; i++)
        {
            ViewerAtom a = av.getAtom(i);
            a.setX(a.getX() - cenX);
            a.setY(a.getY() - cenY);
            a.setZ(a.getZ() - cenZ);
        }

        m.findBB();
    }

    /**
     * Description of the Method
     */
    void createUniverse()
    {
        // Establish a virtual universe, with a single hi-res Locale
        universe = new VirtualUniverse();
        locale = new Locale(universe);

        // Create a PhysicalBody and Physical Environment object
        PhysicalBody body = new PhysicalBody();
        PhysicalEnvironment environment = new PhysicalEnvironment();

        // Create a View and attach the Canvas3D and the physical
        // body and environment to the view.
        view = new View();
        view.addCanvas3D(canvas);
        view.setPhysicalBody(body);
        view.setPhysicalEnvironment(environment);
        view.setBackClipDistance(500.0);

        //System.out.println("Clip front/back: "+view.getFrontClipDistance()+"/"+
        //                       view.getBackClipDistance());
        // Create a branch group node for the view platform
        root = new BranchGroup();
        localeRoot = new BranchGroup();
        localeRoot.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        localeRoot.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        localeRoot.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        root.setCapability(BranchGroup.ALLOW_DETACH);
        root.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        root.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        root.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);

        ViewPlatform vp = new ViewPlatform();
        rootTrans = new TransformGroup();

        bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);

        rootTrans.addChild(vp);

        BoundingLeaf boundingLeaf = new BoundingLeaf(bounds);
        rootTrans.addChild(boundingLeaf);
        localeRoot.addChild(rootTrans);
        view.attachViewPlatform(vp);
    }

    /**
     * Description of the Method
     */
    void detachRoot()
    {
        root.detach();
    }

    /**
     * Description of the Method
     */
    void initLighting()
    {
        Color3f dlColor = new Color3f(1.0f, 1.0f, 1.0f);

        Vector3f lDirect1 = new Vector3f(1.0f, -1.0f, -1.0f);
        Vector3f lDirect2 = new Vector3f(-1.0f, 1.0f, 1.0f);

        DirectionalLight lgt1 = new DirectionalLight(dlColor, lDirect1);
        lgt1.setInfluencingBounds(bounds);

        DirectionalLight lgt2 = new DirectionalLight(dlColor, lDirect2);
        lgt2.setInfluencingBounds(bounds);

        Color3f alColor = new Color3f(0.65f, 0.65f, 0.65f);
        AmbientLight aLgt = new AmbientLight(alColor);
        aLgt.setInfluencingBounds(bounds);

        root.addChild(aLgt);
        root.addChild(lgt1);
        root.addChild(lgt2);

        // Try some fog for depth cueing
        fog = new LinearFog();
        fog.setCapability(LinearFog.ALLOW_DISTANCE_WRITE);
        fog.setColor(new Color3f(0.0f, 0.0f, 0.0f));
        fog.setFrontDistance(70.0);
        fog.setBackDistance(500.0);

        //fog.setInfluencingBounds(bounds);
        //fog.addScope(sceneTrans);
        //sceneTrans.addChild(fog);
    }

    /**
     * Description of the Method
     */
    void initSceneGraph()
    {
        Transform3D t = new Transform3D();
        t.set(new Vector3f(0.0f, 0.0f, -100.0f));
        sceneOffset = new TransformGroup(t);
        sceneOffset.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        sceneOffset.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        sceneTrans = new TransformGroup();
        sceneTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        sceneTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        sceneSwitch = new Switch(Switch.CHILD_MASK);
        sceneSwitch.setCapability(Switch.ALLOW_SWITCH_READ);
        sceneSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);

        fastGroup = new BranchGroup();
        niceGroup = new BranchGroup();
        fastGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        fastGroup.setCapability(Group.ALLOW_CHILDREN_READ);
        fastGroup.setCapability(Group.ALLOW_CHILDREN_WRITE);

        niceGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        niceGroup.setCapability(Group.ALLOW_CHILDREN_READ);
        niceGroup.setCapability(Group.ALLOW_CHILDREN_WRITE);

        sceneSwitch.addChild(niceGroup);
        sceneSwitch.addChild(fastGroup);
        sceneSwitch.setWhichChild(0);

        // Create the drag behavior node
        MouseFastRotate behavior = new MouseFastRotate(sceneTrans, this);
        behavior.setSchedulingBounds(bounds);
        sceneTrans.addChild(behavior);

        // Create the zoom behavior node
        MouseZoom behavior2 = new MouseZoom(sceneTrans);
        behavior2.setSchedulingBounds(bounds);
        sceneTrans.addChild(behavior2);

        // Create the zoom behavior node
        MouseTranslate behavior3 = new MouseTranslate(sceneTrans);
        behavior3.setSchedulingBounds(bounds);
        sceneTrans.addChild(behavior3);

        // Now create the simple picking behavior
        //pickBehFast = new PickHighlightBehavior(this, canvas, fastGroup, bounds);
        pickBehNice = new PickHighlightBehavior(this, canvas, niceGroup,
                bounds);

        sceneTrans.addChild(sceneSwitch);

        sceneOffset.addChild(sceneTrans);
        rootTrans.addChild(sceneOffset);
    }

    /**
     * Description of the Method
     *
     * @param m      Description of the Parameter
     * @param nodes  Description of the Parameter
     * @param group  Description of the Parameter
     */
    void removeMolNode(ViewerMolecule m, Hashtable nodes, BranchGroup group)
    {
        Node n = (Node) nodes.get(m);
        int nc = group.numChildren();

        for (int i = 0; i < nc; i++)
        {
            if (group.getChild(i) == n)
            {
                //System.out.println("remove "+m.getName());
                group.removeChild(i);
                nodes.remove(m);

                return;
            }
        }

        m.clear();
    }

    void setBounds(Molecule mol)
    {
        ViewerMolecule m = (ViewerMolecule) molecules.get(mol);

        setBounds(m);
    }

    void setBounds(ViewerMolecule m)
    {
        float x = m.getXmax() - m.getXmin();
        float y = m.getYmax() - m.getYmin();
        float ex = Math.max(x, y);

        //      System.out.println("ex: "+ex+" "+m.getZmin()+" "+m.getZmax());
        float backPlane = (m.getZmax() > 150.0f) ? (m.getZmax() + 50.0f)
                                                 : 150.0f;
        view.setBackClipDistance(backPlane);

        fog.setFrontDistance(0.5f * (m.getZmin() + m.getZmax()));
        fog.setBackDistance(backPlane);

        Transform3D t = new Transform3D();
        t.set(new Vector3f(0.0f, 0.0f, (m.getZmin() - (2.0f * ex))));

        //t.setScale(15.0f/ex);
        sceneOffset.setTransform(t);
    }

    // Can be removed from code
}

///////////////////////////////////////////////////////////////////////////////
//  END OF FILE.
///////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
//  END OF FILE.
///////////////////////////////////////////////////////////////////////////////
