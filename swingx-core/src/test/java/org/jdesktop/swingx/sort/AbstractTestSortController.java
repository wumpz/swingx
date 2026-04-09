/*
 * $Id$
 *
 * Copyright 2009 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package org.jdesktop.swingx.sort;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Common unit test for DefaultSortController implementations.
 *
 * @author Jeanette Winzenburg
 */
public abstract class AbstractTestSortController<SC extends DefaultSortController<M>, M> extends InteractiveTestCase {
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(AbstractTestSortController.class.getName());

	protected SC controller;
	/**
	 * A custom StringValue for Color. Maps to a string composed of the
	 * prefix "R/G/B: " and the Color's rgb value.
	 */
	protected StringValue sv;

	/** writable version */
	protected StringValueRegistry registry;

	@Test
	public void testStringValueProviderNotNull() {
		assertNotNull(controller.getStringValueProvider());
	}

	@Test
	public void testSetStringValueProvider() {
		controller.setStringValueProvider(registry);
		assertEquals(registry, controller.getStringValueProvider());
	}

	@Test
	public void testLastColumn() {
		controller.toggleSortOrder(getColumnCount() - 1);
		// was silly mistake ...
		controller.toggleSortOrder(getColumnCount() - 1);
	}

	@Test
	public void testNPEOnNullSortOrderCycle() {
		assertThrows(NullPointerException.class, () -> controller.setSortOrderCycle((SortOrder[]) null));
	}

	@Test
	public void testNPEOnNullSortOrderCycleElements() {
		assertThrows(NullPointerException.class, () -> controller.setSortOrderCycle((SortOrder) null));
	}

	@Test
	public void testUseSortOrderCycle() {
		// empty cylce - does nothing
		controller.setSortOrderCycle(new SortOrder[0]);
		controller.toggleSortOrder(0);
		assertEquals(SortOrder.UNSORTED, controller.getSortOrder(0));
	}

	@Test
	public void testSortOrderCycleSet() {
		SortOrder[] cycle = new SortOrder[] {};
		controller.setSortOrderCycle(cycle);
		assertEqualsArrayByElements(cycle, controller.getSortOrderCycle());
	}

	/**
	 * Need to completely take over the toggleSortOrder to support custom cycle.
	 * So need to check if the exception is thrown as expected
	 */
	@Test
	public void testColumnIndexCheckedOnToggle() {
		assertThrows(IndexOutOfBoundsException.class, () -> controller.toggleSortOrder(getColumnCount()));
	}
	/**
	 * @param string
	 * @param first
	 * @param second
	 */
	private void assertEqualsArrayByElements(Object[] first, Object[] second) {
		assertEquals(first.length, second.length, "must have same size");
		for (int i = 0; i < second.length; i++) {
			assertEquals(first[i], second[i], "item must be same at index:  " + i);
		}
	}

	/**
	 * Test that sortsOnUpdate property is true by default.
	 * That's different from core (which is false)
	 */
	@Test
	public void testSortsOnUpdateDefault() {
		assertEquals(true, controller.getSortsOnUpdates(), "sortsOnUpdates must be true by default");
	}
	/**
	 * Test that toggle sort order has no effect if column not sortable.
	 */
	@Test
	public void testToggleSortOrderIfSortableFalse() {
		controller.setSortable(0, false);
		controller.toggleSortOrder(0);
		assertEquals(SortOrder.UNSORTED, controller.getSortOrder(0));
	}
	/**
	 * Test that set sort order has no effect if column not sortable.
	 *
	 */
	@Test
	public void testSetSortOrderIfSortableFalse() {
		controller.setSortable(0, false);
		controller.setSortOrder(0, SortOrder.ASCENDING);
		assertEquals(SortOrder.UNSORTED, controller.getSortOrder(0));
	}

	/**
	 * Test that resetSortOrders doesn't change sorts at all if controller is not sortable.
	 * Does nothing if columnCount < 2.
	 */
	@Test
	public void testResetSortOrdersIfSortableFalse() {
		if (getColumnCount() < 2) return;
		controller.toggleSortOrder(0);
		controller.toggleSortOrder(1);
		controller.setSortable(false);
		List<? extends SortKey> keys = controller.getSortKeys();
		assertEquals(2, keys.size());
		controller.resetSortOrders();
		assertEquals(keys, controller.getSortKeys(), "resetSortOrders must not touch unsortable columns");
	}

	/**
	 * Test that resetSortOrders doesn't change sorts at all if controller is not sortable.
	 * Does nothing if columnCount < 2.
	 */
	@Test
	public void testResetSortOrdersIfColumnSortableFalse() {
		if (getColumnCount() < 2) return;
		controller.toggleSortOrder(0);
		controller.toggleSortOrder(1);
		controller.setSortable(0, false);
		List<? extends SortKey> keys = controller.getSortKeys();
		assertEquals(2, keys.size());
		controller.resetSortOrders();
		assertEquals(1, controller.getSortKeys().size(), "resetSortOrders must not touch unsortable columns");
	}
	/**
	 * Test that resetSortOrders removes all sorts.
	 * Does nothing if columnCount < 2.
	 */
	@Test
	public void testResetSortOrders() {
		if (getColumnCount() < 2) return;
		controller.toggleSortOrder(0);
		controller.toggleSortOrder(1);
		assertEquals(2, controller.getSortKeys().size());
		controller.resetSortOrders();
		assertEquals(0, controller.getSortKeys().size());
	}
	/**
	 * Test that toggle cycles through sort order cycle.
	 */
	@Test
	public void testToggleSortOrder() {
		// PENDING JW: use custom cycle to really test
		SortOrder[] cycle = controller.getSortOrderCycle();
		for (SortOrder cycle1 : cycle) {
			controller.toggleSortOrder(0);
			assertEquals(cycle1, controller.getSortOrder(0));
		}
	}

	@Test
	public void testGetSortOrder() {
		assertEquals(SortOrder.UNSORTED, controller.getSortOrder(0));
	}

	@Test
	public void testSetSortOrder() {
		SortOrder toSet = SortOrder.ASCENDING;
		controller.setSortOrder(0, toSet);
		assertEquals(toSet, controller.getSortOrder(0));
	}

	/**
	 * Test per-column sortable setter
	 */
	@Test
	public void testSortableColumn() {
		controller.setSortable(0, false);
		assertFalse(controller.isSortable(0));
	}
	/**
	 * Test that per-controller and per-column sortable getters return false if
	 * controller sortable is false.
	 */
	@Test
	public void testSortableFalse() {
		controller.setSortable(false);
		assertFalse(controller.isSortable(), "controller must not be sortable");
		for (int i = 0; i < getColumnCount(); i++) {
			assertFalse(controller.isSortable(i), "columns must not be sortable if controller isn't");
		}
	}

	/**
	 * Controller sortable is true by default.
	 */
	@Test
	public void testSortableDefault() {
		assertTrue(controller.isSortable(), "controller must be sortable by default");
	}

	/**
	 * per-column sortable is true by default.
	 */
	@Test
	public void testSortableColumnDefault() {
		for (int i = 0; i < getColumnCount(); i++) {
			assertTrue(controller.isSortable(i), "columns must be sortable by default");
		}
	}

	// -------------------- utility methods and setup, abstract methods
	/**
	 * The model's column count. This value is used in some tests defined here.
	 * @return
	 */
	protected abstract int getColumnCount();

	/**
	 * Create and return a model as appropriate for the concrete subclass.
	 * Called in setup only.
	 * @return
	 */
	protected abstract M createModel();
	/**
	 * Create and return a SortController as appropriate. Note that the
	 * parameter is the model as returned by createModel. Called in setup only.
	 *
	 * @param model the model to use in the SortController
	 * @return
	 */
	protected abstract SC createDefaultSortController(M model);
	/**
	 * initialize model dependent state. Note that the model is the same as returned
	 * from createModel. Called in setup only
	 * @param model
	 */
	protected abstract void setupModelDependentState(M model);

	/**
	 * Creates and returns a StringValue which maps a Color to it's R/G/B rep,
	 * prepending "R/G/B: "
	 *
	 * @return the StringValue for color.
	 */
	private StringValue createColorStringValue() {
		StringValue sv = new StringValue() {

			@Override
			public String getString(Object value) {
				if (value instanceof Color) {
					Color color = (Color) value;
					return "R/G/B: " + color.getRGB();
				}
				return StringValues.TO_STRING.getString(value);
			}
		};
		return sv;
	}

	@BeforeEach
	public void setUp() throws Exception {
		sv = createColorStringValue();
		registry = new StringValueRegistry();
		M teamModel = createModel();
		controller = createDefaultSortController(teamModel);
		setupModelDependentState(teamModel);
	}
}
