package org.jdesktop.swingx;

import org.jdesktop.beans.AbstractBeanInfoTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JXTipOfTheDayBeanInfoTest extends AbstractBeanInfoTest<JXTipOfTheDay> {
	@Override
	protected JXTipOfTheDay createInstance() {
		return new JXTipOfTheDay();
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
