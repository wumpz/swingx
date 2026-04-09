package org.jdesktop.swingx.search;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JTextField;
import org.junit.jupiter.api.Test;

public class NativeSearchFieldSupportTest implements PropertyChangeListener {
	private JTextField tf = new JTextField();
	private boolean eventFired;

	@Test
	public void testSearchFieldPropertyChangeEvent() throws Exception {
		tf.addPropertyChangeListener(NativeSearchFieldSupport.MAC_TEXT_FIELD_VARIANT_PROPERTY, this);
		NativeSearchFieldSupport.setSearchField(tf, true);
		assertTrue(eventFired);
		eventFired = false;
		NativeSearchFieldSupport.setSearchField(tf, true);
		assertTrue(eventFired);
	}

	@Test
	public void testFindActionPropertyChangeEvent() throws Exception {
		tf.addPropertyChangeListener(NativeSearchFieldSupport.FIND_ACTION_PROPERTY, this);
		NativeSearchFieldSupport.setFindAction(tf, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eventFired = true;
			}
		});
		assertTrue(eventFired);
		eventFired = false;
		NativeSearchFieldSupport.setFindAction(tf, null);
		assertTrue(eventFired);
	}

	@Test
	public void testCancelActionPropertyChangeEvent() throws Exception {
		tf.addPropertyChangeListener(NativeSearchFieldSupport.CANCEL_ACTION_PROPERTY, this);
		NativeSearchFieldSupport.setCancelAction(tf, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eventFired = true;
			}
		});
		assertTrue(eventFired);
		eventFired = false;
		NativeSearchFieldSupport.setCancelAction(tf, null);
		assertTrue(eventFired);
	}

	@Test
	public void testSearchFieldUIChange() throws Exception {
		NativeSearchFieldSupport.setSearchField(tf, true);
		tf.addPropertyChangeListener(NativeSearchFieldSupport.MAC_TEXT_FIELD_VARIANT_PROPERTY, this);
		tf.updateUI();
		assertTrue(eventFired);

		NativeSearchFieldSupport.setSearchField(tf, false);
		eventFired = false;
		tf.updateUI();
		assertFalse(eventFired);
	}

	@Test
	public void testIsSearchField() throws Exception {
		NativeSearchFieldSupport.setSearchField(tf, true);
		assertTrue(NativeSearchFieldSupport.isSearchField(tf));

		NativeSearchFieldSupport.setSearchField(tf, false);
		assertFalse(NativeSearchFieldSupport.isSearchField(tf));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		eventFired = true;
	}
}
