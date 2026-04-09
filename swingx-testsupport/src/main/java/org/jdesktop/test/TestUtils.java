/*
 * TestUtils.java
 *
 * Created on October 31, 2006, 9:08 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jdesktop.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;

/**
 * Extends assert to get all the ease-of-use assert methods
 * @author rbair
 */
public final class TestUtils extends Assertions {
	private static final Logger LOG = Logger.getLogger(TestUtils.class.getName());

	private TestUtils() {}

	public static void assertContainsType(Object[] objects, Class<?> clazz, int count) {
		if (objects.length == 0 && count == 0) return;
		assertTrue(
				count <= objects.length, "not enough elements: expected == " + count + " but was == " + objects.length);
		int found = 0;
		for (Object object : objects) {
			if (clazz.isAssignableFrom(object.getClass())) {
				found++;
			}
		}
		;
		assertEquals(count, found, "unexpected number of elements of type " + clazz);
	}

	/**
	 * Asserts the last received propertyChangeEvent of the
	 * report against the expected values.
	 *
	 * @param report the PropertyReport which received the event
	 * @param source the expected event source
	 * @param property the expected name of the property
	 * @param oldValue the expected old value
	 * @param newValue the expected new value
	 */
	public static void assertPropertyChangeEvent(
			PropertyChangeReport report, Object source, String property, Object oldValue, Object newValue) {
		assertPropertyChangeEvent(report, property, oldValue, newValue);
		assertEquals(source, report.getLastSource(), "event source");
	}

	/**
	 * Asserts the last received propertyChangeEvent of the
	 * report against the expected values.
	 *
	 * @param report the PropertyReport which received the event
	 * @param property the expected name of the property
	 * @param oldValue the expected old value
	 * @param newValue the expected new value
	 */
	public static void assertPropertyChangeEvent(
			PropertyChangeReport report, String property, Object oldValue, Object newValue) {
		if (report.getEventCount() > 1) {
			LOG.info("events: " + report.getEventNames());
		}
		assertEquals(1, report.getEventCount(), "exactly one event");
		assertEquals(property, report.getLastProperty(), "property");
		assertEquals(oldValue, report.getLastOldValue(), "last old value");
		assertEquals(newValue, report.getLastNewValue(), "last new value");
	}

	/**
	 * Asserts the last received propertyChangeEvent of the
	 * report against the expected values.
	 *
	 * @param report the PropertyReport which received the event
	 * @param property the expected name of the property
	 * @param oldValue the expected old value
	 * @param newValue the expected new value
	 * @param single flag to denote if we expect one event only
	 */
	public static void assertPropertyChangeEvent(
			PropertyChangeReport report, String property, Object oldValue, Object newValue, boolean single) {
		if (report.getEventCount() > 1) {
			LOG.info("events: " + report.getEventNames());
		}
		if (single) {
			assertEquals(1, report.getEventCount(), "exactly one event");
			assertEquals(property, report.getLastProperty(), "property");
			assertEquals(oldValue, report.getLastOldValue(), "last old value");
			assertEquals(newValue, report.getLastNewValue(), "last new value");
		} else {
			assertEquals(1, report.getEventCount(property), "one event of property " + property);
			assertEquals(oldValue, report.getLastOldValue(property), "old property");
			assertEquals(newValue, report.getLastNewValue(property), "new property");
		}
	}

	/**
	 * Asserts the last received propertyChangeEvent of the
	 * report against the expected values.
	 *
	 * @param report the PropertyReport which received the event
	 * @param property the expected name of the property
	 * @param oldValue the expected old value
	 * @param newValue the expected new value
	 * @param single flag to denote if we expect one event only
	 * @param verifyArrayItems check array items one by one rather then whole arrays
	 */
	public static void assertPropertyChangeEvent(
			PropertyChangeReport report,
			String property,
			Object oldValue,
			Object newValue,
			boolean single,
			boolean verifyArrayItems) {
		if (report.getEventCount() > 1) {
			LOG.info("events: " + report.getEventNames());
		}
		if (single) {
			assertEquals(1, report.getEventCount(), "exactly one event");
			assertEquals(property, report.getLastProperty(), "property");
			if (verifyArrayItems && oldValue != null && oldValue.getClass().isArray()) {
				List l1 = Arrays.asList((Object[]) oldValue);
				List l2 = Arrays.asList((Object[]) report.getLastOldValue());
				assertEquals(l1.size(), l2.size(), "last old value");
				for (int i = 0; i < l1.size(); i++) {
					assertEquals(l1.get(i), l2.get(i), "last old value");
				}
			} else {
				assertEquals(oldValue, report.getLastOldValue(), "last old value");
			}
			if (verifyArrayItems && newValue != null && newValue.getClass().isArray()) {
				List l1 = Arrays.asList(newValue);
				List l2 = Arrays.asList(report.getLastNewValue());
				assertEquals(l1.size(), l2.size(), "last new value");
				for (int i = 0; i < l1.size(); i++) {
					assertEquals(l1.get(i), l2.get(i), "last new value");
				}
			} else {
				assertEquals(newValue, report.getLastNewValue(), "last new value");
			}
		} else {
			assertEquals(1, report.getEventCount(property), "one event of property " + property);
			if (verifyArrayItems && oldValue != null && oldValue.getClass().isArray()) {
				List l1 = Arrays.asList((Object[]) oldValue);
				List l2 = Arrays.asList((Object[]) report.getLastOldValue(property));
				assertEquals(l1.size(), l2.size(), "old value");
				for (int i = 0; i < l1.size(); i++) {
					assertEquals(l1.get(i), l2.get(i), "old value");
				}
			} else {
				assertEquals(oldValue, report.getLastOldValue(property), "old property " + property);
			}
			if (verifyArrayItems && newValue != null && newValue.getClass().isArray()) {
				Collection l1 =
						newValue instanceof Collection ? (Collection) newValue : Arrays.asList((Object[]) newValue);
				Collection l2 = report.getLastNewValue(property) instanceof Collection
						? (Collection) report.getLastNewValue(property)
						: Arrays.asList((Object[]) report.getLastNewValue(property));
				assertEquals(l1.size(), l2.size(), "new value of property " + property);
				int index = 0;
				for (Iterator i1 = l1.iterator(), i2 = l2.iterator(); i1.hasNext() && i2.hasNext(); ) {
					Object o1 = i1.next();
					Object o2 = i2.next();
					//                    if (o1 instanceof Date) {
					//                        o1 = ((Date) o1).getTime();
					//                        o2 = ((Date) o2).getTime();
					//                    }
					assertEquals(o1, o2, "new value [" + index++ + "] of property " + property);
				}
			} else {
				assertEquals(newValue, report.getLastNewValue(property), "new value of property " + property);
			}
		}
	}

	/**
	 * Asserts the last received propertyChangeEvent of the
	 * report against the expected values (arrays).
	 *
	 * @param report the PropertyReport which received the event
	 * @param property the expected name of the property
	 * @param oldValue the expected old aray value
	 * @param newValue the expected new array value
	 */
	public static void assertPropertyChangeEvent(
			PropertyChangeReport report, String property, Object[] oldValue, Object[] newValue) {
		if (report.getEventCount() > 1) {
			LOG.info("events: " + report.getEventNames());
		}
		assertEquals(1, report.getEventCount(), "exactly one event");
		assertEquals(property, report.getLastProperty(), "property");
		assertTrue(Arrays.equals(oldValue, (Object[]) report.getLastOldValue()), "last old array value");
		assertTrue(Arrays.equals(newValue, (Object[]) report.getLastNewValue()), "last new array value");
	}
}
