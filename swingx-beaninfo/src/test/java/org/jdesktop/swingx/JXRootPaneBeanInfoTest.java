package org.jdesktop.swingx;

import org.jdesktop.beans.AbstractBeanInfoTest;
import org.junit.jupiter.api.Disabled;

@Disabled
public class JXRootPaneBeanInfoTest extends AbstractBeanInfoTest<JXRootPane> {
	@Override
	protected JXRootPane createInstance() {
		return new JXRootPane();
	}
}
