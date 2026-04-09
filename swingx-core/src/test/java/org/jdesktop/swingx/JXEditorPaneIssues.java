/*
 * $Id$
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Logger;
import javax.swing.Action;
import org.jdesktop.swingx.action.ActionFactory;
import org.jdesktop.swingx.action.ActionManager;
import org.jdesktop.swingx.action.TargetManager;
import org.junit.jupiter.api.Test;

/**
 * A unit test for the JXEditorPane
 *
 * @author Mark Davidson
 */
public class JXEditorPaneIssues extends JXEditorPaneTest {
	@SuppressWarnings("all")
	private static final Logger LOG = Logger.getLogger(JXEditorPaneIssues.class.getName());

	private static String testText = "This is an example of some text";

	@Test
	public void testEditorActionManagerWoes() {
		JXEditorPane editable = new JXEditorPane();
		assertTrue(editable.isEditable());
		ActionManager.getInstance().addAction(ActionFactory.createTargetableAction("undo", "Undo", "U"));
		TargetManager.getInstance().addTarget(editable);
	}

	/**
	 * Issue #289-swingx: JXEditorPane actions should be disabled if not
	 * applicable.
	 *
	 * undo must not perform on non-editable editor.
	 *
	 */
	@Test
	public void testXUndoActionNotEditable() {
		JXEditorPane editor = new JXEditorPane();
		editor.setEditable(false);
		editor.setText(testText);
		assertEquals(testText, editor.getText());
		Action action = editor.getActionMap().get("undo");
		action.actionPerformed(null);
		assertEquals(testText, editor.getText(), "undo must have no effect");
	}
}
