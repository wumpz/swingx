package org.jdesktop.swingx;

import org.jdesktop.beans.AbstractBeanInfoTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JXLoginPaneBeanInfoTest extends AbstractBeanInfoTest<JXLoginPane> {
    @Override
    protected JXLoginPane createInstance() {
        return new JXLoginPane();
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
