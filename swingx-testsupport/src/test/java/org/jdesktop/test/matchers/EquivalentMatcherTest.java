package org.jdesktop.test.matchers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.jdesktop.test.SerializableSupport.serialize;

import javax.swing.JButton;
import static org.hamcrest.MatcherAssert.assertThat;

import org.jdesktop.test.EDTRunner;
import static org.jdesktop.test.matchers.Matchers.equivalentTo;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EDTRunner.class)
public class EquivalentMatcherTest {
    @Test
    public void ensureEqualIsEquivalent() {
        assertThat(1, is(equalTo(1)));
        assertTrue(equivalentTo(1).matches(1));
    }
    
    @Test
    public void testUnequalBaseObjects() {
        JButton button1 = new JButton();
        JButton button2 = new JButton();
        
        assertThat(button1, is(not(equalTo(button2))));
        assertTrue(equivalentTo(button2).matches(button1));
    }
    
    @Test
    public void testSerializedObjects() {
        JButton button1 = new JButton();
        JButton button2 = serialize(button1);
        
        assertThat(button1, is(not(equalTo(button2))));
        assertTrue(equivalentTo(button2).matches(button1));
    }
}
