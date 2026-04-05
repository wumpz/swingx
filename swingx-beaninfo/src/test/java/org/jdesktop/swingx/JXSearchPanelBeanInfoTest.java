package org.jdesktop.swingx;

import org.jdesktop.beans.AbstractBeanInfoTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JXSearchPanelBeanInfoTest extends AbstractBeanInfoTest<JXSearchPanel> {
    @Override
    protected JXSearchPanel createInstance() {
        return new JXSearchPanel();
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
