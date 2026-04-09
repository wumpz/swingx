package org.jdesktop.swingx;

import org.jdesktop.beans.AbstractBeanInfoTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JXMultiThumbSliderBeanInfoTest extends AbstractBeanInfoTest<JXMultiThumbSlider> {
	@Override
	protected JXMultiThumbSlider createInstance() {
		return new JXMultiThumbSlider();
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
