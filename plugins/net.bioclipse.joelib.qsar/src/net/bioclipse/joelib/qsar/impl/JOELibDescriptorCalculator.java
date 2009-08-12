/*******************************************************************************
 * Copyright (c) 2007,2009  Egon Willighagen <egonw@users.sf.net>
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.joelib.qsar.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import joelib.desc.DescResult;
import joelib.desc.DescriptorException;
import joelib.desc.result.BitResult;
import joelib.desc.result.DoubleArrayResult;
import joelib.desc.result.DoubleResult;
import joelib.desc.result.IntArrayResult;
import joelib.desc.result.IntResult;
import joelib.desc.types.BCUT;
import joelib.desc.types.BurdenEigenvalues;
import joelib.desc.types.NumberOfAtoms;
import joelib.io.MoleculeIOException;
import joelib.io.SimpleReader;
import joelib.molecule.JOEMol;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IMolecule;
import net.bioclipse.qsar.DescriptorType;
import net.bioclipse.qsar.descriptor.DescriptorResult;
import net.bioclipse.qsar.descriptor.IDescriptorCalculator;
import net.bioclipse.qsar.descriptor.IDescriptorResult;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

public class JOELibDescriptorCalculator implements IDescriptorCalculator {

    private static final Logger logger =
        Logger.getLogger(JOELibDescriptorCalculator.class);

    private final static String NS_BOQSAR =
        "http://www.blueobelisk.org/ontologies/chemoinformatics-algorithms/#";
    
    private final static String DESC_BCUT = 
        NS_BOQSAR + "BCUT";
    private final static String DESC_ATOMCOUNT = 
        NS_BOQSAR + "AtomCount";
    
    public Map<? extends IMolecule, List<IDescriptorResult>>
        calculateDescriptor(Map<IMolecule, List<DescriptorType>> moldesc,
            IProgressMonitor monitor) throws BioclipseException {
        
        if (monitor == null)
            monitor = new NullProgressMonitor();

        Map<IMolecule, List<IDescriptorResult>> allResults=
            new HashMap<IMolecule, List<IDescriptorResult>>();

        monitor.beginTask("Calculating descriptors...", moldesc.size());
        for (IMolecule mol : moldesc.keySet()){
            if (monitor.isCanceled()) return allResults;
            List<IDescriptorResult> retlist =
                calculate(mol, moldesc.get( mol ), monitor);
            allResults.put(mol, retlist);
            monitor.worked(1);
        }

        return allResults;
    }

    private List<IDescriptorResult> calculate(IMolecule mol,
            List<DescriptorType> list, IProgressMonitor monitor) {
        List<IDescriptorResult> results = new ArrayList<IDescriptorResult>();
        
        JOEMol joeMol = null;
        try {
            String cmlSerialization = mol.toCML();
            SimpleReader reader = new SimpleReader(
                new ByteArrayInputStream(cmlSerialization.getBytes()),
                "CML"
            );
            joeMol = new JOEMol();
            reader.readNext(joeMol);
        } catch (IOException e) {
            joeMol = null;
        } catch (MoleculeIOException e) {
            joeMol = null;
        } catch (BioclipseException e) {
            joeMol = null;
        }

        for (DescriptorType descType : list) {
            if (monitor.isCanceled()) return results;
            
            if (joeMol == null) {
                IDescriptorResult res = new DescriptorResult();
                res.setDescriptor( descType );
                res.setErrorMessage("Could not create a JOELib molecule.");
                res.setValues(new Float[0]);
                res.setLabels(new String[0]);
            } else if (DESC_BCUT.equals(descType.getOntologyid())) {
                IDescriptorResult res = new DescriptorResult();
                res.setDescriptor( descType );
                BCUT descriptor = new BCUT();
                res = calculateDescriptor(joeMol, descriptor, res);
            } else if (DESC_ATOMCOUNT.equals(descType.getOntologyid())) {
                IDescriptorResult res = new DescriptorResult();
                res.setDescriptor( descType );
                NumberOfAtoms descriptor = new NumberOfAtoms();
                res = calculateDescriptor(joeMol, descriptor, res);
            }
        }
        return results;
    }

    public IDescriptorResult calculateDescriptor(
        JOEMol mol, joelib.desc.Descriptor descriptor,
        IDescriptorResult result) {

        // get the values
        Float[] resultVals = new Float[0];
        try {
            DescResult joeResults = descriptor.calculate(mol);
            System.out.println("Class: " + joeResults.getClass().getName());
            if (joeResults instanceof IntResult) {
                resultVals = new Float[1];
                resultVals[0] = (float)((IntResult)joeResults).getInt();
            } else if (joeResults instanceof IntArrayResult) {
                int[] intResults = ((IntArrayResult)joeResults).getIntArray();
                resultVals = new Float[intResults.length];
                for (int j=0; j<resultVals.length; j++) {
                    resultVals[j] = (float)intResults[j];
                }
            } else if (joeResults instanceof DoubleResult) {
                resultVals = new Float[1];
                resultVals[0] = (float)((DoubleResult)joeResults).getDouble();
            } else if (joeResults instanceof BitResult) {
                int[] doubleResults = ((BitResult)joeResults).getBinaryValue().toIntArray();
                resultVals = new Float[doubleResults.length];
                for (int j=0; j<resultVals.length; j++) {
                    resultVals[j] = (float)doubleResults[j];
                }
            } else if (joeResults instanceof DoubleArrayResult) {
                double[] doubleResults = ((DoubleArrayResult)joeResults).getDoubleArray();
                resultVals = new Float[doubleResults.length];
                for (int j=0; j<resultVals.length; j++) {
                    resultVals[j] = (float)doubleResults[j];
                }
            } else {
                logger.error(
                    "No idea what to do with this result class: " +
                    joeResults.getClass().getName()
                );
            }
        } catch (DescriptorException e) {
            logger.error("Could not calculate JOELib descriptor: " + e.getMessage());
            e.printStackTrace();
            for (int j=0; j<resultVals.length; j++) {
                resultVals[j] = Float.NaN;
            }
        }

        // set up column labels
        String[] resultLabels = new String[resultVals.length];
        if (resultLabels.length == 1) {
            resultLabels[0] = descriptor.getDescInfo().getName();
        } else {
            for (int j=0; j<resultLabels.length; j++) {
                resultLabels[j] = descriptor.getDescInfo().getName() + (j+1);
            }
        }

        if (resultLabels.length != resultVals.length) {
            System.out.println(
                "WARN: #labels != #vals for " +
                descriptor.getDescInfo().getName()
            );
        }
        result.setValues(resultVals);
        result.setLabels(resultLabels);
        return result;
    }
}
