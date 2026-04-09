package org.jdesktop.swingx;

import org.jdesktop.beans.AbstractBeanInfoTest;
import org.junit.jupiter.api.Disabled;

@Disabled
public class JXFrameBeanInfoTest extends AbstractBeanInfoTest<JXFrame> {
	@Override
	protected JXFrame createInstance() {
		// TODO we must have a nullary constructor
		return new JXFrame();
	}
}
