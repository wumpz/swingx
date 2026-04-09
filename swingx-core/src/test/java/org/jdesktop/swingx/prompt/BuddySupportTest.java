package org.jdesktop.swingx.prompt;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTextField;
import org.jdesktop.swingx.plaf.BuddyLayoutAndBorder;
import org.jdesktop.swingx.plaf.BuddyTextFieldUI;
import org.jdesktop.swingx.prompt.BuddySupport.Position;
import org.junit.jupiter.api.Test;

public class BuddySupportTest {
	private JTextField tf = new JTextField();

	@Test
	public void testAdd() throws Exception {
		JButton button = new JButton();
		BuddySupport.addLeft(button, tf);
		assertSame(BuddyLayoutAndBorder.class, tf.getLayout().getClass());
		assertSame(1, tf.getComponentCount());
		assertSame(button, tf.getComponent(0));
	}

	@Test
	public void testDoubleAdd() throws Exception {
		JButton button = new JButton();
		BuddySupport.addLeft(button, tf);
		try {
			BuddySupport.addLeft(button, tf);
			fail();
		} catch (IllegalStateException e) {

		}
		try {
			BuddySupport.addRight(button, tf);
			fail();
		} catch (IllegalStateException e) {

		}
	}

	@Test
	public void testGet() throws Exception {
		assertSame(0, BuddySupport.getLeft(tf).size());
		JButton button = new JButton();
		BuddySupport.addLeft(button, tf);
		assertSame(1, BuddySupport.getLeft(tf).size());
	}

	@Test
	public void testRemove() throws Exception {
		JButton button = new JButton();
		BuddySupport.addLeft(button, tf);
		BuddySupport.remove(button, tf);
		assertSame(0, BuddySupport.getLeft(tf).size());
		assertSame(0, tf.getComponentCount());
	}

	@Test
	public void testRemoveAll() throws Exception {
		JButton button1 = new JButton();
		JButton button2 = new JButton();
		BuddySupport.addLeft(button1, tf);
		BuddySupport.addRight(button2, tf);
		BuddySupport.removeAll(tf);
		assertSame(0, BuddySupport.getLeft(tf).size());
		assertSame(0, BuddySupport.getRight(tf).size());
		assertSame(0, tf.getComponentCount());
	}

	@Test
	public void testEnsureBuddiesInComponentHierarchy() throws Exception {
		JButton button = new JButton();
		BuddySupport.addLeft(button, tf);
		tf.remove(button);
		BuddySupport.ensureBuddiesAreInComponentHierarchy(tf);
		assertSame(1, BuddySupport.getLeft(tf).size());
		assertSame(1, tf.getComponentCount());
	}

	@Test
	public void testGetAfterPlainRemove() throws Exception {
		JButton button = new JButton();
		BuddySupport.addLeft(button, tf);
		tf.remove(button);
	}

	@Test
	public void testIsBuddy() throws Exception {
		JButton button = new JButton();
		assertFalse(BuddySupport.isBuddy(button, tf));
		tf.add(button);
		assertFalse(BuddySupport.isBuddy(button, tf));
		BuddySupport.addLeft(button, tf);
		assertTrue(BuddySupport.isBuddy(button, tf));
		tf.remove(button);
	}

	@Test
	public void testInstall() throws Exception {
		BuddySupport.add(new JButton(), Position.LEFT, tf);

		assertSame(BuddyTextFieldUI.class, tf.getUI().getClass());
		assertSame(BuddyLayoutAndBorder.class, tf.getBorder().getClass());
		assertSame(BuddyLayoutAndBorder.class, tf.getLayout().getClass());
	}

	@Test
	public void testLookAndFeelChange() throws Exception {
		BuddySupport.addLeft(new BuddyButton(), tf);
		BuddySupport.addRight(new BuddyButton(), tf);

		assertSame(2, tf.getComponentCount());
		tf.updateUI();
		assertSame(2, tf.getComponentCount());
	}

	@Test
	public void testCreateNegativeGap() throws Exception {
		Component c = BuddySupport.createGap(-1);
		assertSame(-1, c.getPreferredSize().width);
	}
}
