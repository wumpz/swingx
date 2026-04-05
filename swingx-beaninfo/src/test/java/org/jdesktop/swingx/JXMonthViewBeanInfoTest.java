package org.jdesktop.swingx;

import org.jdesktop.beans.AbstractBeanInfoTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JXMonthViewBeanInfoTest extends AbstractBeanInfoTest<JXMonthView> {
    @Override
    protected JXMonthView createInstance() {
        return new JXMonthView();
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
