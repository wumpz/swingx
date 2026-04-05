/*
 * $Id$
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.treetable;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.logging.Logger;

import javax.swing.tree.TreePath;

import org.jdesktop.swingx.InteractiveTestCase;
import org.junit.jupiter.api.Test;

/**
 * 
 * Known issues around TreeTableModel and related classes.
 * 
 */
public class TreeTableModelIssues extends InteractiveTestCase {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
        .getLogger(TreeTableModelIssues.class.getName());
    
    public static void main(String[] args) {
        TreeTableModelIssues test = new TreeTableModelIssues();
        try {
            test.runInteractiveTests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * TreePath issue: null must not be allowed as path element.
     * The constructor doesn't cope with array containing null.
     */
    @Test
    public void testTreeStructureChangedEmptyPath() {
       TreePath path = new TreePath(new Object[] {null});
       assertNotNull(path.getLastPathComponent(), 
               "TreePath must not contain null path elements"); 
    }


}
