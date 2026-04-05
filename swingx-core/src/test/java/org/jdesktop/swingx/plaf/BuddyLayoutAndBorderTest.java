package org.jdesktop.swingx.plaf;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.jdesktop.swingx.prompt.BuddySupport;
import org.jdesktop.test.EDTRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(EDTRunner.class)
public class BuddyLayoutAndBorderTest {
	private BuddyLayoutAndBorder blab;
	private JTextField textField;

	@BeforeEach
	public void setup() {
		textField = new JTextField();
		blab = new BuddyLayoutAndBorder();
		blab.install(textField);
	}

	@Test
	public void testBorder() throws Exception {
		Border newBorder = BorderFactory.createEmptyBorder();
		textField.setBorder(newBorder);
		assertNotSame(newBorder, textField.getBorder(), "Border should have been wrapped.");
	}

	@Test
	public void testUninstall() throws Exception {
		blab.uninstall();
		Border newBorder = BorderFactory.createEmptyBorder();
		textField.setBorder(newBorder);
		assertSame(newBorder, textField.getBorder(), "Border should NOT have been wrapped.");
	}

	@Test
	public void testPreferredWidth() throws Exception {
		JButton btn = new JButton("hey");
		int txtWidth = textField.getPreferredSize().width;
//		int btnWidth = btn.getPreferredSize().width;

		assertSame(txtWidth, blab.preferredLayoutSize(textField).width + 1);

		BuddySupport.addLeft(btn, textField);

		assertSame(txtWidth,
				blab.preferredLayoutSize(textField).width + 1, String.format("preferred layout size should be %d", txtWidth));

		btn.setVisible(false);
		assertSame(txtWidth, blab.preferredLayoutSize(textField).width + 1);
	}

	@Test
	public void testBorderInsets() throws Exception {
		JButton btn = new JButton("hey");
		int left = blab.getBorderInsets(textField).left;
		int btnWidth = btn.getPreferredSize().width;

		BuddySupport.addLeft(btn, textField);
		assertSame(btn, BuddySupport.getLeft(textField).get(0));

		assertSame(left + btnWidth, blab.getBorderInsets(textField).left);

		btn.setVisible(false);
		assertSame(left, blab.getBorderInsets(textField).left);
	}
	
	@Test
	public void testGetBorderInsetsWithNullDelegate() {
	    textField.setBorder(null);
	    blab.install(textField);
	    assertThat(blab.getRealBorderInsets(), is(new Insets(0, 0, 0, 0)));
	}
}
