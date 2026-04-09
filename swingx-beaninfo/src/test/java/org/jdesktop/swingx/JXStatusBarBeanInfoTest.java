package org.jdesktop.swingx;

import org.jdesktop.beans.AbstractBeanInfoTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JXStatusBarBeanInfoTest extends AbstractBeanInfoTest<JXStatusBar> {
	@Override
	protected JXStatusBar createInstance() {
		return new JXStatusBar();
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
