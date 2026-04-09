package org.jdesktop.swingx;

import org.jdesktop.beans.AbstractBeanInfoTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Deprecated
public class JXImagePanelBeanInfoTest extends AbstractBeanInfoTest<JXImagePanel> {
	@Override
	protected JXImagePanel createInstance() {
		return new JXImagePanel();
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
