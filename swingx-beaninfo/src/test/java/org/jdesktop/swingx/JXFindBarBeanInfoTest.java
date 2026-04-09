package org.jdesktop.swingx;

import org.jdesktop.beans.AbstractBeanInfoTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JXFindBarBeanInfoTest extends AbstractBeanInfoTest<JXFindBar> {
	@Override
	protected JXFindBar createInstance() {
		return new JXFindBar();
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
