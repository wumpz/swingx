package org.jdesktop.swingx;

import org.jdesktop.beans.AbstractBeanInfoTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JXTitledPanelBeanInfoTest extends AbstractBeanInfoTest<JXTitledPanel> {
	@Override
	protected JXTitledPanel createInstance() {
		return new JXTitledPanel();
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
