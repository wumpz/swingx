package org.jdesktop.swingx.plaf;

import java.beans.PropertyChangeEvent;
import javax.swing.JTextField;
import org.jdesktop.test.EDTRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(EDTRunner.class)
public class AbstractUIChangeHandlerTest {
	JTextField tf = new JTextField();
	AbstractUIChangeHandler ch;
	private boolean changed;

	@BeforeEach
	public void setUp() throws Exception {
		ch = new AbstractUIChangeHandler() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				changed = true;
			}
		};
	}

	@Test
	public void testInstall() {
		ch.install(tf);
		Assertions.assertFalse(changed);
		tf.updateUI();
		Assertions.assertTrue(changed);
	}

	@Test
	public void testUninstall() {
		ch.install(tf);
		ch.uninstall(tf);
		tf.updateUI();
		Assertions.assertFalse(changed);
	}

	@Test
	public void testDoubleInstall() {
		ch.install(tf);
		ch.install(tf);
		ch.uninstall(tf);
		tf.updateUI();
		Assertions.assertFalse(changed);
	}
}
