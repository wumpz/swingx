package org.jdesktop.swingx;

import org.jdesktop.beans.AbstractBeanInfoTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JXComboBoxBeanInfoTest extends AbstractBeanInfoTest<JXComboBox> {
    @Override
    protected JXComboBox createInstance() {
        return new JXComboBox();
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
