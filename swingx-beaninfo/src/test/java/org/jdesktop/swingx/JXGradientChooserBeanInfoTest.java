package org.jdesktop.swingx;

import org.jdesktop.beans.AbstractBeanInfoTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JXGradientChooserBeanInfoTest extends AbstractBeanInfoTest<JXGradientChooser> {
    @Override
    protected JXGradientChooser createInstance() {
        return new JXGradientChooser();
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
