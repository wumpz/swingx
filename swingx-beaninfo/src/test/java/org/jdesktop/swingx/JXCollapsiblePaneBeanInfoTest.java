package org.jdesktop.swingx;

import org.jdesktop.beans.AbstractBeanInfoTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JXCollapsiblePaneBeanInfoTest extends AbstractBeanInfoTest<JXCollapsiblePane> {
	@Override
	protected JXCollapsiblePane createInstance() {
		return new JXCollapsiblePane();
	}

	/**
	 * {@inheritDoc}
	 */
	@Test
	@Override
	@Disabled("serialization fails")
	public void testSerialization() {
		super.testSerialization();
	}
}
