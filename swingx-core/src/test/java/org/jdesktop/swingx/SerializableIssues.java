/*
 * $Id$
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package org.jdesktop.swingx;

import static org.junit.jupiter.api.Assertions.fail;

import javax.swing.JPanel;

import org.jdesktop.test.SerializableSupport;
import org.junit.jupiter.api.Test;

/**
 * Test to exposed known issues of serializable.
 * 
 * @author Jeanette Winzenburg
 */
public class SerializableIssues extends InteractiveTestCase {


    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable. <p>
     * 
     * First blow: RolloverController
     * 
     */
    @Test
    public void testTreeTable() {
        JXTreeTable component = new JXTreeTable();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }


    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable. <p>
     * 
     * First blow: TreeAdapter
     * 
     */
    @Test
    public void testTree() {
        JXTree component = new JXTree();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }


    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable. <p>
     * 
     * First blow: PainterUIResource
     * 
     */
    @Test
    public void testTitledPanel() {
        JXTitledPanel component = new JXTitledPanel();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }


    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable. <p>
     * 
     * First blow: ui delegate
     * 
     */
    @Test
    public void testTipOfTheDay() {
        JXTipOfTheDay component = new JXTipOfTheDay();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }


    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable. <p>
     * 
     * First blow: VerticalLayout
     * 
     */
    @Test
    public void testTaskPaneContainer() {
        JXTaskPaneContainer component = new JXTaskPaneContainer();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }


    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable. <p>
     * 
     * First blow: VerticalLayout
     * 
     */
    @Test
    public void testTaskPane() {
        JXTaskPane component = new JXTaskPane();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }

    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable. <p>
     * 
     * First blow: rolloverController
     * 
     */
    @Test
    public void testTable() {
        JXTable component = new JXTable();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }

    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable. <p>
     * 
     * First blow: ui-delegate
     * 
     */
    @Test
    public void testStatusBar() {
        JXStatusBar component = new JXStatusBar();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }

    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable. <p>
     * 
     * First blow: EventHandler
     * 
     */
    @Test
    public void testSearchPanel() {
        JXSearchPanel component = new JXSearchPanel();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }


    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable. <p>
     * 
     * First blow: DefaultMultiThumbModel.
     * 
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testMultiThumbSlider() {
        JXMultiThumbSlider component = new JXMultiThumbSlider();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }

    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable.<p>
     * 
     * First blow: MultiSplitPaneLayout.
     * 
     */
    @Test
    public void testMultiSplitPane() {
        JXMultiSplitPane component = new JXMultiSplitPane();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }

    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable.<p>
     * 
     * First blow: BufferedImage.
     * 
     */
    @Test
    public void testLoginPanel() {
        JXLoginPane component = new JXLoginPane();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }

    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable.<p>
     * 
     * First blow: DelegatingRenderer.
     * 
     */
    @Test
    public void testList() {
        JXList component = new JXList();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }

    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable.<p>
     * 
     * first blow: PainterUIResource
     */
    @Test
    public void testHeader() {
        JXHeader component = new JXHeader();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }

    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable.<p>
     * 
     * Frist blow: Rectangle inner class
     */
    @Test
    public void testGraph() {
        JXGraph component = new JXGraph();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }

    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable.<p>
     * 
     * First blow: BufferedImage
     */
    @Test
    public void testGradientChooser() {
        JXGradientChooser component = new JXGradientChooser();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }


    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable.<p>
     * 
     * First blow: inner class?.
     *
     */
    @Test
    public void testFrame() {
        JXFrame component = new JXFrame();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }

    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable.<p>
     * 
     * First blow: EventHandler
     */
    @Test
    public void testFindPanel() {
        JXFindPanel component = new JXFindPanel();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }

    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable.<p>
     * 
     * First blow: EventHandler
     */
    @Test
    public void testFindBar() {
        JXFindBar component = new JXFindBar();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }

    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable.<p>
     * 
     * First blow: ui-delegate 
     */
    @Test
    public void testErrorPane() {
        JXErrorPane component = new JXErrorPane();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }

    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable.<p>
     * 
     *
     */
    @Test
    public void testEditorPane() {
        JXEditorPane component = new JXEditorPane();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }

    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable.<p>
     * 
     *
     */
    @Test
    public void testDialog() {
        JXDialog component = new JXDialog(new JPanel());
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }


    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable.<p>
     * 
     *
     */
    @Test
    public void testDatePicker() {
        JXDatePicker component = new JXDatePicker();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }

    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable.<p>
     * 
     *
     */
    @Test
    public void testColorSelectionButton() {
        JXColorSelectionButton component = new JXColorSelectionButton();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }

    /**
     * Issue #423-swingx: all descendants of JComponent must be 
     * serializable.<p>
     * 
     *
     */
    @Test
    public void testCollapsiblePane() {
        JXCollapsiblePane component = new JXCollapsiblePane();
        try {
            SerializableSupport.serialize(component);
        } catch (Exception e) {
            fail("not serializable " + e);
        } 
    }

}
