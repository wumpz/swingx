package org.jdesktop.swingx.plaf;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.swing.JTextField;
import org.jdesktop.swingx.JXTextField;
import org.junit.jupiter.api.Test;

public class PromptTextFieldUITest extends PromptTextUITest_Base {
	JTextField txtField;

	@Override
	public void setup() {
		textComponent = txtField = new JXTextField();
	}

	@Override
	@Test
	public void testGetLabelComponent() {
		super.testGetLabelComponent();
		txtField.setHorizontalAlignment(JTextField.CENTER);
		JTextField lbl = (JTextField) ui.getPromptComponent(txtField);

		assertEquals(txtField.getHorizontalAlignment(), lbl.getHorizontalAlignment());
		assertEquals(txtField.getColumns(), lbl.getColumns());
	}
}
