package org.jdesktop.beans;

import static org.jdesktop.test.SerializableSupport.serialize;
import static org.jdesktop.test.matchers.Matchers.equivalentTo;
import static org.jdesktop.test.matchers.Matchers.property;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.awt.Insets;
import java.awt.LayoutManager;

import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.Introspector;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DropMode;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.RETURNS_MOCKS;
import org.mockito.exceptions.verification.NoInteractionsWanted;

@SuppressWarnings("nls")
public abstract class AbstractBeanInfoTest<T> {
    protected Logger logger = Logger.getLogger(getClass().getName());
    protected T instance;
    private BeanInfo beanInfo;
    private Map<Class<?>, Object> listeners;
    
    @BeforeEach
    public void setUp() throws Exception {
        instance = createInstance();
        beanInfo = Introspector.getBeanInfo(instance.getClass());
        listeners = new HashMap<Class<?>, Object>();
        
        for (EventSetDescriptor descriptor : beanInfo.getEventSetDescriptors()) {
            Class<?> eventClass = descriptor.getListenerType();
            Object listener = mock(eventClass);
            
            descriptor.getAddListenerMethod().invoke(instance, listener);
            listeners.put(eventClass, listener);
        }
    }
    
    protected abstract T createInstance();
		
		private final Set<String> boundPropertiesWithoutPropertyChangeEvent = Set.of("alignmentX", "alignmentY", "componentCount", "doubleBuffered", "focusAccelerator");
    
		/**
		 * How can this test be correct? There are many properties that are defined as bound but do
		 * not fire property change event.
		 * @throws Exception 
		 */
		@Test
    public final void testBoundProperties() throws Exception {
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
				   	//special-case this read-only property
						// this is strange, since the property ordering is different
						if ("UIClassID".equals(descriptor.getName())) {
								return;
						}
            if (descriptor.isBound()) {
								System.out.println("bound property " + descriptor.getDisplayName() + " " + descriptor.getPropertyType());
							
                if (descriptor.isHidden()) {
                    continue;
                }
								
                if (descriptor.getWriteMethod() == null) {         
                    fail("bound read-only property: " + descriptor.getName());
                }
                
								if (boundPropertiesWithoutPropertyChangeEvent.contains(descriptor.getName()))
									continue;
								
                Class<?> propertyType = descriptor.getPropertyType();
                
                if (isUnhandledType(propertyType)) {
                    continue;
                }
                
                Object defaultValue = descriptor.getReadMethod().invoke(instance);
                Object newValue = getNewValue(propertyType, defaultValue);
                
                descriptor.getWriteMethod().invoke(instance, newValue);
                
                PropertyChangeListener pcl = (PropertyChangeListener) listeners.get(PropertyChangeListener.class);
                verify(pcl).propertyChange(argThat(property(descriptor.getName(), defaultValue, newValue)));
                reset(pcl);
            }
        }
    }
    
    private boolean isUnhandledType(Class<?> type) {
        return type == null || Action.class.isAssignableFrom(type) || ActionMap.class.isAssignableFrom(type) || DropMode.class.isAssignableFrom(type) || LayoutManager.class.isAssignableFrom(type);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected Object getNewValue(Class<?> propertyType, Object defaultValue) {
        Object result = null;
        
        if (propertyType.isArray()) {
            int length = defaultValue == null ? 1 : ((Object[]) defaultValue).length + 1;
            result = Array.newInstance(propertyType.getComponentType(), length);
        } else if (propertyType.isEnum()) {
            EnumSet set = EnumSet.allOf((Class<? extends Enum>) propertyType);
            int size = set.size();
            
            if (size == 1) {
                result = defaultValue == null ? set.iterator().next() : null;
            } else {
                int ordinal = ((Enum) defaultValue).ordinal();
                ordinal = ordinal == size - 1 ? 0 : ordinal + 1;
                Iterator iter = set.iterator();
                
                for (int i = 0; i < ordinal + 1; i++) {
                    result = iter.next();
                }
            }
        } else if (propertyType.isPrimitive()) {
            //help short circuit all of these checks
            if (propertyType == boolean.class) {
                result = Boolean.FALSE.equals(defaultValue);
            } else if (propertyType == int.class) {
                result = ((Integer) defaultValue) + 1;
            } else if (propertyType == double.class) {
                result = ((Double) defaultValue) + 1d;
            } else if (propertyType == float.class) {
                result = ((Float) defaultValue) + 1f;
            }
        } else if (propertyType == String.class) {
            result = "original string: " + defaultValue;
        } else if (propertyType == Insets.class) {
            if (new Insets(0, 0, 0, 0).equals(defaultValue)) {
                result = new Insets(1, 1, 1, 1);
            } else {
                result = mock(propertyType);
            }
        } else {
            result = mock(propertyType, RETURNS_MOCKS);
        }
        
        return result;
    }

    /**
     * A simple serialization check. Ensures that the reconstituted object is equivalent to the
     * original.
     */
    @Test
    public void testSerialization() {
        if (!Serializable.class.isInstance(instance)) {
            return;
        }
        
        T serialized = serialize(instance);
        
        assertTrue(equivalentTo(instance).matches(instance));
    }
    
    @AfterEach
    public void tearDown() {
        for (Object listener : listeners.values()) {
            try {
                verifyNoMoreInteractions(listener);
            } catch (NoInteractionsWanted logAndIgnore) {
                logger.log(Level.WARNING, "unexpected listener notification", logAndIgnore);
            }
        }
    }
}
