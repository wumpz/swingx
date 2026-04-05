package org.jdesktop.swingx.action;

import static org.hamcrest.CoreMatchers.is;
import static org.jdesktop.test.SerializableSupport.serialize;

import javax.swing.JLabel;
import static org.hamcrest.MatcherAssert.assertThat;

import org.jdesktop.test.EDTRunner;
import org.jdesktop.test.matchers.Matchers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(EDTRunner.class)
public class BoundActionTest {
    private BoundAction action;
    
    @BeforeEach
    public void setUp() {
        action = new BoundAction();
    }
    
    @Test
    public void testSerialization() {
        BoundAction serialized = serialize(action);  
				assertTrue( Matchers.equivalentTo(action).matches(serialized));
    }
    
    @Test
    public void testSerializationWithCallback() {
        action.registerCallback(new JLabel(), "updateUI");
        BoundAction serialized = serialize(action);
        
        assertTrue( Matchers.equivalentTo(action).matches(serialized));
        assertThat(serialized.getActionListeners().length, is(1));
    }
    
    @Test
    public void testSerializationForToggleWithCallback() {
        action.setStateAction(true);
        action.registerCallback(new JLabel(), "updateUI");
        BoundAction serialized = serialize(action);
        
        assertTrue( Matchers.equivalentTo(action).matches(serialized));
        assertThat(serialized.getItemListeners().length, is(1));
    }
    
    @Test
    public void testSerializationWithListener() {
        action.addActionListener(new BoundAction());
        BoundAction serialized = serialize(action);
        
        assertTrue( Matchers.equivalentTo(action).matches(serialized));
    }
}
