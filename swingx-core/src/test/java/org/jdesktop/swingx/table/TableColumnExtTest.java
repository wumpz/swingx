/*
 * $Id$
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.table;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.text.Collator;
import java.util.Comparator;
import java.util.Date;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.CompoundHighlighter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.plaf.UIDependent;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.test.PropertyChangeReport;
import org.jdesktop.test.SerializableSupport;
import org.jdesktop.test.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test of enhanced <code>TableColumnExt</code>.
 *
 * @author Jeanette Winzenburg
 */
public class TableColumnExtTest {

	private TableColumnExt columnExt;

	@Test
	public void testHideableOnHidden() {
		columnExt.setVisible(false);
		PropertyChangeReport report = new PropertyChangeReport();
		columnExt.addPropertyChangeListener(report);
		columnExt.setHideable(false);
		assertTrue(columnExt.isVisible(), "visibility must be forced to true");
		assertEquals(1, report.getEventCount("visible"));
		report.clear();
		columnExt.setHideable(true);
		assertFalse(columnExt.isVisible(), "real visibility value must be returned if hideable");
		assertEquals(1, report.getEventCount("visible"));
	}

	@Test
	public void testHideable() {
		assertTrue(columnExt.isHideable());
		PropertyChangeReport report = new PropertyChangeReport();
		columnExt.addPropertyChangeListener(report);
		columnExt.setHideable(false);
		TestUtils.assertPropertyChangeEvent(report, "hideable", true, false);
	}

	/**
	 * api change: let TableColumnExt implement UIDependent.
	 */
	@Test
	public void testUIDependent() {
		assertTrue(columnExt instanceof UIDependent);
	}

	/**
	 * Issue #822-swingx: replace cloneable by copy constructor.
	 * Here: test base properties copied.
	 */
	@Test
	public void testCopyConstructor() {
		TableColumnExt columnExt =
				new TableColumnExt(10, 200, new DefaultTableRenderer(), new DefaultCellEditor(new JTextField(20)));
		TableColumnExt copy = new TableColumnExt(columnExt);
		assertEquals(columnExt.getModelIndex(), copy.getModelIndex());
		assertEquals(columnExt.getWidth(), copy.getWidth());
		assertEquals(columnExt.getCellRenderer(), copy.getCellRenderer());
		assertEquals(columnExt.getCellEditor(), copy.getCellEditor());
	}
	/**
	 * test remove
	 *
	 */
	@Test
	public void testPutClientPropertyNullValue() {
		Object value = new Object();
		String key = "some";
		columnExt.putClientProperty(key, value);
		// sanity: got it
		assertSame(value, columnExt.getClientProperty(key));
		columnExt.putClientProperty(key, null);
		assertNull(columnExt.getClientProperty(key));
		// again - for going into the last untested line
		// but what to test?
		columnExt.putClientProperty(key, null);
		assertNull(columnExt.getClientProperty(key));
	}
	/**
	 * test doc'ed exceptions in putClientProperty.
	 *
	 */
	@Test
	public void testPutClientPropertyExc() {
		assertThrows(IllegalArgumentException.class, () -> columnExt.putClientProperty(null, "somevalue"));
	}
	/**
	 * Sanity test Serializable.
	 *
	 * @throws ClassNotFoundException
	 * @throws IOException
	 *
	 */
	@Test
	public void testSerializable() throws IOException, ClassNotFoundException {
		Object value = new Date();
		columnExt.putClientProperty("date", value);
		TableColumnExt serialized = SerializableSupport.serialize(columnExt);
		assertTrue(serialized.isVisible());
		assertEquals(value, serialized.getClientProperty("date"));
		assertEquals(15, serialized.getMinWidth());
		assertTrue(serialized.getResizable());
	}

	/**
	 * Issue #154-swingx.
	 *
	 * added property headerTooltip. Test initial value, propertyChange
	 * notification, cloned correctly.
	 *
	 */
	@Test
	public void testHeaderTooltip() {
		columnExt.setTitle("mytitle");
		assertNull(columnExt.getToolTipText(), "tooltip is null initially");
		String toolTip = "some column text";
		PropertyChangeReport report = new PropertyChangeReport();
		columnExt.addPropertyChangeListener(report);
		columnExt.setToolTipText(toolTip);
		assertEquals(toolTip, columnExt.getToolTipText());
		assertEquals(
				1, report.getEventCount("toolTipText"), "must have fired one propertyChangeEvent for toolTipText ");
		TableColumnExt copy = new TableColumnExt(columnExt);
		assertEquals(columnExt.getToolTipText(), copy.getToolTipText(), "tooltip property must be cloned");
	}

	/**
	 * Test the sortable property: must fire propertyChange and
	 * be cloned properly
	 *
	 */
	@Test
	public void testSortable() {
		boolean sortable = columnExt.isSortable();
		assertTrue(sortable, "columnExt isSortable by default");
		PropertyChangeReport report = new PropertyChangeReport();
		columnExt.addPropertyChangeListener(report);
		columnExt.setSortable(!sortable);
		// sanity assert: the change was taken
		assertEquals(sortable, !columnExt.isSortable());
		assertEquals(1, report.getEventCount("sortable"), "must have fired one propertyChangeEvent for sortable ");
		TableColumnExt copy = new TableColumnExt(columnExt);
		assertEquals(columnExt.isSortable(), copy.isSortable(), "sortable property must be cloned");
	}

	/**
	 * Issue #273-swingx: make Comparator a bound property of TableColumnExt.
	 * (instead of client property)
	 *
	 * test if setting comparator fires propertyChange.
	 */
	@Test
	public void testComparatorBoundProperty() {
		PropertyChangeReport report = new PropertyChangeReport();
		columnExt.addPropertyChangeListener(report);
		Comparator<?> comparator = Collator.getInstance();
		columnExt.setComparator(comparator);
		assertTrue(report.hasEvents());
		assertEquals(1, report.getEventCount("comparator"));
	}

	/**
	 * Issue #273-swingx: make Comparator a bound property of TableColumnExt.
	 * (instead of client property)
	 *
	 * test if comparator is cloned.
	 */
	@Test
	public void testCopyComparator() {
		Comparator<?> comparator = Collator.getInstance();
		columnExt.setComparator(comparator);
		TableColumnExt clone = new TableColumnExt(columnExt);
		assertEquals(comparator, clone.getComparator());
	}
	/**
	 * Issue #280-swingx: tableColumnExt doesn't fire propertyChange on
	 * putClientProperty.
	 *
	 */
	@Test
	public void testClientPropertyNotification() {
		PropertyChangeReport report = new PropertyChangeReport();
		columnExt.addPropertyChangeListener(report);
		Object value = 3;
		columnExt.putClientProperty("somevalue", value);
		assertTrue(report.hasEvents());
		assertEquals(1, report.getEventCount("somevalue"));
	}

	/**
	 * Issue #279-swingx: getTitle throws NPE.
	 *
	 */
	@Test
	public void testTitle() {
		columnExt.getTitle();
	}

	/**
	 * user friendly resizable flag.
	 *
	 */
	@Test
	public void testResizable() {
		// sanity assert
		assertTrue(columnExt.getMinWidth() < columnExt.getMaxWidth(), "min < max");
		// sanity assert
		assertTrue(columnExt.getResizable(), "resizable default");
		columnExt.setMinWidth(columnExt.getMaxWidth());
		assertFalse(columnExt.getResizable(), "must not be resizable with equal min-max");
		TableColumnExt copy = new TableColumnExt(columnExt);
		// sanity
		assertEquals(copy.getMinWidth(), copy.getMaxWidth(), "min-max of clone");
		assertFalse(copy.getResizable(), "must not be resizable with equal min-max");
		copy.setMinWidth(0);
		// sanity assert
		assertTrue(copy.getMinWidth() < copy.getMaxWidth(), "min < max");
		assertTrue(copy.getResizable(), "cloned base resizable");
	}

	/**
	 * Issue #39-swingx:
	 * Client properties not preserved when cloning.
	 *
	 */
	@Test
	public void testCopyClientProperty() {
		String key = "property";
		Object value = new Object();
		columnExt.putClientProperty(key, value);
		TableColumnExt copy = new TableColumnExt(columnExt);
		assertEquals(value, copy.getClientProperty(key), "client property must be in cloned");

		key = "single";
		columnExt.putClientProperty(key, value);
		// sanity check
		assertSame(value, columnExt.getClientProperty(key));

		assertNull(copy.getClientProperty(key), "cloned client properties must be in independant");
	}
	// begin SwingX Issue #770 checks
	// PENDING JW: consider to remove the  "HighlighterClient"
	// related testing here - that part is done in TableColumnExtAsHighlighterClient in package
	// swingx.
	/**
	 * Check for setHighlighters portion of #770.
	 */
	@Test
	public void testSetHighlighters() {
		PropertyChangeReport hcl = new PropertyChangeReport();
		columnExt.addPropertyChangeListener(hcl);

		Highlighter h1 = new ColorHighlighter();
		Highlighter h2 = new ColorHighlighter();

		// sanity check
		assertEquals(0, hcl.getEventCount());

		// base case no highlighters
		assertSame(CompoundHighlighter.EMPTY_HIGHLIGHTERS, columnExt.getHighlighters());

		columnExt.setHighlighters(h1);
		assertEquals(1, hcl.getEventCount());
		assertEquals("highlighters", hcl.getLastProperty());
		assertEquals(1, columnExt.getHighlighters().length);
		assertSame(h1, columnExt.getHighlighters()[0]);

		// reset state
		hcl.clear();

		columnExt.removeHighlighter(h1);
		assertEquals(1, hcl.getEventCount());
		assertEquals("highlighters", hcl.getLastProperty());
		// we have a compound, but empty highlighter
		assertEquals(0, columnExt.getHighlighters().length);
		// JW: changed CompoundHighlighter to return its EMPTY_HIGHLIGHTERS if empty
		assertSame(CompoundHighlighter.EMPTY_HIGHLIGHTERS, columnExt.getHighlighters());

		// reset state
		hcl.clear();

		columnExt.setHighlighters(h1, h2);
		assertEquals(1, hcl.getEventCount());
		assertEquals("highlighters", hcl.getLastProperty());
		assertEquals(2, columnExt.getHighlighters().length);
		assertSame(h1, columnExt.getHighlighters()[0]);
		assertSame(h2, columnExt.getHighlighters()[1]);
	}

	/**
	 * Check for addHighlighter portion of #770.
	 */
	@Test
	public void testAddHighlighter() {
		PropertyChangeReport hcl = new PropertyChangeReport();
		columnExt.addPropertyChangeListener(hcl);

		Highlighter h1 = new ColorHighlighter();
		Highlighter h2 = new ColorHighlighter();

		// sanity check
		assertEquals(0, hcl.getEventCount());

		// base case no highlighters
		assertSame(CompoundHighlighter.EMPTY_HIGHLIGHTERS, columnExt.getHighlighters());

		columnExt.addHighlighter(h1);
		assertEquals(1, hcl.getEventCount());
		assertEquals("highlighters", hcl.getLastProperty());
		assertEquals(1, columnExt.getHighlighters().length);
		assertSame(h1, columnExt.getHighlighters()[0]);

		// reset state
		hcl.clear();

		columnExt.removeHighlighter(h1);
		assertEquals(1, hcl.getEventCount());
		assertEquals("highlighters", hcl.getLastProperty());
		// we have a compound, but empty highlighter
		assertEquals(0, columnExt.getHighlighters().length);
		// JW: changed CompoundHighlighter to return its EMPTY_HIGHLIGHTERS if empty
		assertSame(CompoundHighlighter.EMPTY_HIGHLIGHTERS, columnExt.getHighlighters());

		columnExt.setHighlighters(h1);

		// reset state
		hcl.clear();

		columnExt.addHighlighter(h2);
		assertEquals(1, hcl.getEventCount());
		assertEquals("highlighters", hcl.getLastProperty());
		assertEquals(2, columnExt.getHighlighters().length);
		assertSame(h1, columnExt.getHighlighters()[0]);
		assertSame(h2, columnExt.getHighlighters()[1]);
	}

	/**
	 * Check for removeHighlighter portion of #770.
	 */
	@Test
	public void testRemoveHighlighter() {
		PropertyChangeReport hcl = new PropertyChangeReport();
		columnExt.addPropertyChangeListener(hcl);

		Highlighter h1 = new ColorHighlighter();
		Highlighter h2 = new ColorHighlighter();
		Highlighter h3 = new ColorHighlighter();

		// sanity check
		assertEquals(0, hcl.getEventCount());

		// ensure that nothing goes awry
		columnExt.removeHighlighter(h1);
		assertEquals(0, hcl.getEventCount());

		columnExt.setHighlighters(h1, h2, h3);

		// reset state
		hcl.clear();

		columnExt.removeHighlighter(h2);
		assertEquals(1, hcl.getEventCount());
		assertEquals("highlighters", hcl.getLastProperty());
		assertEquals(2, columnExt.getHighlighters().length);
		assertSame(h1, columnExt.getHighlighters()[0]);
		assertSame(h3, columnExt.getHighlighters()[1]);
	}

	/**
	 * Check to ensure that the clone returns the highlighters correctly. Part of #770.
	 */
	@Test
	public void testCopyHighlighters() {
		Highlighter h1 = new ColorHighlighter();
		Highlighter h2 = new ColorHighlighter();
		Highlighter h3 = new ColorHighlighter();

		columnExt.setHighlighters(h1, h2);

		TableColumnExt clone = new TableColumnExt(columnExt);

		Highlighter[] columnHighlighters = columnExt.getHighlighters();
		Highlighter[] cloneHighlighters = clone.getHighlighters();

		assertEquals(2, columnHighlighters.length);
		assertEquals(columnHighlighters.length, cloneHighlighters.length);
		assertSame(h1, columnHighlighters[0]);
		assertSame(columnHighlighters[0], cloneHighlighters[0]);
		assertSame(h2, columnHighlighters[1]);
		assertSame(columnHighlighters[1], cloneHighlighters[1]);

		columnExt.addHighlighter(h3);

		columnHighlighters = columnExt.getHighlighters();
		cloneHighlighters = clone.getHighlighters();

		assertEquals(3, columnHighlighters.length);
		assertEquals(columnHighlighters.length, cloneHighlighters.length + 1);
		assertSame(h1, columnHighlighters[0]);
		assertSame(columnHighlighters[0], cloneHighlighters[0]);
		assertSame(h2, columnHighlighters[1]);
		assertSame(columnHighlighters[1], cloneHighlighters[1]);
		assertSame(h3, columnHighlighters[2]);
	}

	/**
	 * @inherited <p>
	 */
	@BeforeEach
	public void setUp() throws Exception {
		columnExt = new TableColumnExt();
	}
}
