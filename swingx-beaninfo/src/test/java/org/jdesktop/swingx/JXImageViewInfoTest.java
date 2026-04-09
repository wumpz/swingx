package org.jdesktop.swingx;

import org.jdesktop.beans.AbstractBeanInfoTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JXImageViewInfoTest extends AbstractBeanInfoTest<JXImageView> {
	@Override
	protected JXImageView createInstance() {
		return new JXImageView();
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
