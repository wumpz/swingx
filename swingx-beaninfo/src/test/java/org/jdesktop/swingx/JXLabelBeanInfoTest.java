package org.jdesktop.swingx;

import org.jdesktop.beans.AbstractBeanInfoTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JXLabelBeanInfoTest extends AbstractBeanInfoTest<JXLabel> {
    @Override
    protected JXLabel createInstance() {
        return new JXLabel();
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
