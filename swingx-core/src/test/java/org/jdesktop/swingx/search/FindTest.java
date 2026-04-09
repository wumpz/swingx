/*
 * $Id$
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.search;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.AbstractListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.BadLocationException;
import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXEditorPane;
import org.jdesktop.swingx.JXFindBar;
import org.jdesktop.swingx.JXFindPanel;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXSearchPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Contains unit test for search related classes.
 * PENDING JW: should be in package search - but references swingx package internals.
 *
 * @author Jeanette Winzenburg
 */
public class FindTest extends InteractiveTestCase {
	private static final Logger LOG = Logger.getLogger(FindTest.class.getName());

	public static void main(String args[]) {
		setSystemLF(true);
		//      Locale.setDefault(new Locale("es"));
		FindTest test = new FindTest();
		try {
			test.runInteractiveTests();
			//          test.runInteractiveTests("interactive.*Compare.*");
			//          test.runInteractiveTests("interactive.*Tree.*");
		} catch (Exception e) {
			System.err.println("exception when executing interactive tests:");
			e.printStackTrace();
		}
	}

	@BeforeEach
	public void setUp() throws Exception {
		// sanity: new instance for each test
		SearchFactory.setInstance(new SearchFactory());
	}

	@Test
	public void testSearchPanelHasXComboBox() {
		JXSearchPanel searchPanel = new JXSearchPanel();
		assertEquals(true, getChildOfType(searchPanel, JXComboBox.class) != null);
	}

	@SuppressWarnings("unchecked")
	public static <T extends JComponent> T getChildOfType(JComponent container, Class<T> clazz) {
		for (int i = 0; i < container.getComponentCount(); i++) {
			Component child = container.getComponent(i);
			if (clazz.isAssignableFrom(child.getClass())) {
				return (T) child;
			}
		}
		return null;
	}
	/**
	 * Findbugs: NPE in SearchFactory - showFindDialog for null target component.
	 *
	 */
	@Test
	public void testSearchFactoryNullTarget() {
		// This test will not work in a headless configuration.
		if (GraphicsEnvironment.isHeadless()) {
			LOG.fine("cannot run testSearchFactoryNullTarget - headless environment");
			return;
		}

		JXTable table = new JXTable(20, 2);
		SearchFactory.getInstance().showFindDialog(null, table.getSearchable());
	}

	/**
	 * Issue #718-swingx: shared FindPanel not updated on LF change.
	 *
	 * Here: check that containing dialog is disposed, new api with flag.
	 */
	@Test
	public void testFindDialogDispose() {
		// This test will not work in a headless configuration.
		if (GraphicsEnvironment.isHeadless()) {
			LOG.fine("cannot run test - headless environment");
			return;
		}
		JXFrame frame = new JXFrame();
		JXTable table = new JXTable();
		frame.add(table);
		JComponent findPanel = SearchFactory.getInstance().getSharedFindPanel();
		SearchFactory.getInstance().showFindDialog(table, table.getSearchable());
		Window window = SwingUtilities.getWindowAncestor(findPanel);
		assertSame(frame, window.getOwner());
		SearchFactory.getInstance().hideSharedFindPanel(true);
		assertFalse(window.isDisplayable(), "window must not be displayable");
		assertNull(findPanel.getParent(), "findPanel must be unparented");
	}

	/**
	 * Issue #718-swingx: shared FindPanel not updated on LF change.
	 *
	 * Here: check that containing dialog is not disposed.
	 */
	@Test
	public void testFindDialogHide() {
		// This test will not work in a headless configuration.
		if (GraphicsEnvironment.isHeadless()) {
			LOG.fine("cannot run test - headless environment");
			return;
		}
		JXFrame frame = new JXFrame();
		JXTable table = new JXTable();
		frame.add(table);
		JComponent findPanel = SearchFactory.getInstance().getSharedFindPanel();
		SearchFactory.getInstance().showFindDialog(table, table.getSearchable());
		Container parent = findPanel.getParent();
		Window window = SwingUtilities.getWindowAncestor(findPanel);
		assertSame(frame, window.getOwner());
		SearchFactory.getInstance().hideSharedFindPanel(false);
		assertFalse(window.isVisible(), "window must not be visible");
		assertSame(parent, findPanel.getParent(), "findPanel must parent must be unchanged");
		assertTrue(window.isDisplayable(), "window must be displayable");
	}

	/**
	 * test if internal state is reset to not found by
	 * passing a null searchstring.
	 *
	 */
	@Test
	public void testTableResetStateWithNullSearchString() {
		JXTable table = new JXTable(new TestTableModel());
		int row = 39;
		int firstColumn = 0;
		String firstSearchText = table.getValueAt(row, firstColumn).toString();
		PatternModel model = new PatternModel();
		model.setRawText(firstSearchText);
		// initialize searchable to "found state"
		int foundIndex = table.getSearchable().search(model.getPattern(), -1);
		// sanity asserts
		int foundColumn = ((TableSearchable) table.getSearchable()).lastSearchResult.foundColumn;
		assertEquals(row, foundIndex, "last line found");
		assertEquals(firstColumn, foundColumn, "column must be updated");
		// search with null searchstring
		int notFoundIndex = table.getSearchable().search((String) null);
		assertEquals(-1, notFoundIndex, "nothing found");
		assertEquals(
				-1, ((TableSearchable) table.getSearchable()).lastSearchResult.foundColumn, "column must be reset");
	}

	/**
	 * test if internal state is reset to not found by
	 * passing a empty (="") searchstring.
	 *
	 */
	@Test
	public void testTableResetStateWithEmptySearchString() {
		JXTable table = new JXTable(new TestTableModel());
		int row = 39;
		int firstColumn = 0;
		String firstSearchText = table.getValueAt(row, firstColumn).toString();
		PatternModel model = new PatternModel();
		model.setRawText(firstSearchText);
		// initialize searchable to "found state"
		int foundIndex = table.getSearchable().search(model.getPattern(), -1);
		// sanity asserts
		int foundColumn = ((TableSearchable) table.getSearchable()).lastSearchResult.foundColumn;
		assertEquals(row, foundIndex, "last line found");
		assertEquals(firstColumn, foundColumn, "column must be updated");
		// search with null searchstring
		int notFoundIndex = table.getSearchable().search("");
		assertEquals(-1, notFoundIndex, "nothing found");
		assertEquals(
				-1, ((TableSearchable) table.getSearchable()).lastSearchResult.foundColumn, "column must be reset");
	}

	/**
	 * test if search loops all columns of previous row (backwards search).
	 *
	 * Hmm... not testable?
	 * Needed to widen access for lastFoundColumn.
	 */
	@Test
	public void testTableFoundNextColumnInPreviousRow() {
		JXTable table = new JXTable(new TestTableModel());
		int lastColumn = table.getColumnCount() - 1;
		int row = 39;
		int firstColumn = lastColumn - 1;
		String firstSearchText = table.getValueAt(row, firstColumn).toString();
		// need a pattern for backwards search
		PatternModel model = new PatternModel();
		model.setRawText(firstSearchText);
		int foundIndex = table.getSearchable().search(model.getPattern(), -1, true);
		assertEquals(row, foundIndex, "last line found");
		int foundColumn = ((TableSearchable) table.getSearchable()).lastSearchResult.foundColumn;
		assertEquals(firstColumn, foundColumn, "column must be updated");
		// the last char(s) of all values is the row index
		// here we are searching for an entry in the next row relative to
		// the previous search and expect the match in the first column (index = 0);
		int previousRow = row - 1;
		String secondSearchText = String.valueOf(previousRow);
		model.setRawText(secondSearchText);
		int secondFoundIndex = table.getSearchable().search(model.getPattern(), previousRow, true);
		// sanity assert
		assertEquals(previousRow, secondFoundIndex, "must find match in same row");
		assertEquals(
				lastColumn,
				((TableSearchable) table.getSearchable()).lastSearchResult.foundColumn,
				"column must be updated");
	}

	/**
	 * test if search loops all columns of next row.
	 *
	 * Hmm... not testable?
	 * Needed to widen access for lastFoundColumn.
	 */
	@Test
	public void testTableFoundPreviousColumnInNextRow() {
		JXTable table = new JXTable(new TestTableModel());
		int row = 0;
		int firstColumn = 1;
		String firstSearchText = table.getValueAt(row, firstColumn).toString();
		int foundIndex = table.getSearchable().search(firstSearchText);
		assertEquals(row, foundIndex, "last line found");
		int foundColumn = ((TableSearchable) table.getSearchable()).lastSearchResult.foundColumn;
		assertEquals(firstColumn, foundColumn, "column must be updated");
		// the last char(s) of all values is the row index
		// here we are searching for an entry in the next row relative to
		// the previous search and expect the match in the first column (index = 0);
		int nextRow = row + 1;
		String secondSearchText = String.valueOf(nextRow);
		int secondFoundIndex = table.getSearchable().search(secondSearchText, nextRow);
		// sanity assert
		assertEquals(nextRow, secondFoundIndex, "must find match in same row");
		assertEquals(
				0, ((TableSearchable) table.getSearchable()).lastSearchResult.foundColumn, "column must be updated");
	}

	/**
	 * test if match in same row but different column is found in forward
	 * search.
	 *
	 */
	@Test
	public void testTableFoundNextColumnInSameRow() {
		JXTable table = new JXTable(new TestTableModel());
		int row = 90;
		int firstColumn = 0;
		String firstSearchText = table.getValueAt(row, firstColumn).toString();
		int foundIndex = table.getSearchable().search(firstSearchText);
		assertEquals(row, foundIndex, "last line found");
		String secondSearchText = table.getValueAt(row, firstColumn + 1).toString();
		int secondFoundIndex = table.getSearchable().search(secondSearchText, foundIndex);
		assertEquals(foundIndex, secondFoundIndex, "must find match in same row");
	}

	/**
	 * Issue #718-swingx: shared FindPanel not updated on LF change.
	 *
	 * Here: check that dialog is new for different owner and old has been disposed.
	 */
	@Test
	public void testFindDialogNew() {
		// This test will not work in a headless configuration.
		if (GraphicsEnvironment.isHeadless()) {
			LOG.fine("cannot run test - headless environment");
			return;
		}
		JXFrame frame = new JXFrame();
		JXTable table = new JXTable();
		frame.add(table);
		JComponent findPanel = SearchFactory.getInstance().getSharedFindPanel();
		// show search dialog for a searchable
		SearchFactory.getInstance().showFindDialog(table, table.getSearchable());
		Window window = SwingUtilities.getWindowAncestor(findPanel);
		assertSame(frame, window.getOwner());
		// setup of second searchable
		JXFrame second = new JXFrame();
		JXTree tree = new JXTree();
		second.add(tree);
		// show search dialog for a searchable
		SearchFactory.getInstance().showFindDialog(tree, tree.getSearchable());
		assertFalse(window.isDisplayable(), "previous window must not be displayable");
		assertSame(second, SwingUtilities.getWindowAncestor(findPanel).getOwner());
	}

	/**
	 * Issue #720: JXTree - selection lost on release the searchable.
	 *
	 * assert JXList behaviour.
	 * Note: JXList not clearing the selection has been an accidental, undocumented
	 * side-effect of DefaultListSelectionModel.setSelectionInterval - it backs
	 * out silently on negative indices.
	 */
	@Test
	public void testKeepSelectionOnNotFoundList() {
		JXList list = new JXList(new TestListModel());
		PatternModel model = new PatternModel();
		model.setRawText("one9");
		int row = list.getSearchable().search(model.getPattern());
		// sanity: found at expected row
		assertEquals(90, row);
		// sanity: selection is match marker
		assertEquals(row, list.getSelectedIndex());
		list.getSearchable().search((Pattern) null);
		assertEquals(row, list.getSelectedIndex(), "not found must not reset selection");
	}

	/**
	 * Issue #720: JXTree - selection lost on release the searchable.
	 *
	 * assert JXTable behaviour.
	 */
	@Test
	public void testKeepSelectionOnNotFoundTable() {
		JXTable table = new JXTable(new TestTableModel());
		PatternModel model = new PatternModel();
		model.setRawText("one9");
		int row = table.getSearchable().search(model.getPattern());
		// sanity: found at expected row
		assertEquals(9, row);
		// sanity: selection is match marker
		assertEquals(row, table.getSelectedRow());
		table.getSearchable().search((Pattern) null);
		assertEquals(row, table.getSelectedRow(), "not found must not reset selection");
	}

	/**
	 * Issue #720: JXTree - selection lost on release the searchable.
	 *
	 * Base problem was a slight difference in the tree- vs. ListSelectionModel
	 * (not completely documented). The TreeSelection
	 * interprets a null path array as clearSelection, same for null path in
	 * setSelectionPaths and setSelectionPath. A null element in the array is
	 * undocumented, but setPaths protects itself and interprets an array containing
	 * null paths only as clearSelection as well.
	 *
	 */
	@Test
	public void testKeepSelectionOnNotFoundTree() {
		JXTree tree = new JXTree();
		PatternModel model = new PatternModel();
		model.setRawText("foo");
		int row = tree.getSearchable().search(model.getPattern());
		// sanity: found at expected row
		assertEquals(3, row);
		// sanity: selection is match marker
		assertEquals(row, tree.getMinSelectionRow());
		tree.getSearchable().search((Pattern) null);
		assertEquals(row, tree.getMinSelectionRow(), "not found must not reset selection");
	}

	/**
	 * Issue #487-swingx: NPE if instantiating with not null Searchable
	 */
	@SuppressWarnings("unused")
	@Test
	public void testFindBarNPEConstructor() {
		JXFindBar findBar = new JXFindBar(new JXTable().getSearchable());
	}
	/**
	 * Issue #487-swingx: NPE if setting a not-null Searchable before
	 * showing
	 */
	@Test
	public void testFindBarNPE() {
		Searchable searchable = new JXTable().getSearchable();
		JXFindBar findBar = new JXFindBar();
		findBar.setSearchable(searchable);
	}

	/**
	 * Issue #374-swingx: default search keystroke never found.
	 *
	 * This was probably introduced when fixing #353-swingx:
	 * swingx must not try to leave sandbox.
	 *
	 */
	@Test
	public void testSearchKeyStroke() {
		if (System.getSecurityManager() != null) {
			LOG.info("cannot run testSearchKeyStroke - SecurityManager installed");
			return;
		}
		// This test will not work in a headless configuration.
		if (GraphicsEnvironment.isHeadless()) {
			LOG.fine("cannot run testSearchKeyStroke - headless environment");
			return;
		}
		assertNotNull(
				SearchFactory.getInstance().getSearchAccelerator(), "searchfactory must return search accelerator");
	}

	/**
	 * Test SearchHighlight used in incremental search of JXTable.
	 *
	 */
	@Test
	public void testTableIncrementalHighlighter() {
		JXTable table = new JXTable(new TestTableModel());
		table.putClientProperty(AbstractSearchable.MATCH_HIGHLIGHTER, Boolean.TRUE);
		int row = 3;
		int column = 1;
		String firstSearchText = "wo" + row;
		PatternModel model = new PatternModel();
		model.setRawText(firstSearchText);
		// make sure we had a match
		int foundIndex = table.getSearchable().search(model.getPattern(), -1);
		assertEquals(row, foundIndex, "must return be found");
		Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
		assertEquals(Color.YELLOW.brighter(), comp.getBackground());
	}

	/**
	 * test incremental search in JXTable.
	 *
	 */
	@Test
	public void testTableIncremental() {
		JXTable table = new JXTable(new TestTableModel());
		String firstSearchText = "on";
		PatternModel model = new PatternModel();
		model.setRawText(firstSearchText);
		// make sure we had a match
		int foundIndex = table.getSearchable().search(model.getPattern(), -1);
		assertEquals(0, foundIndex, "must return be found");
		// extended searchstring
		String secondSearchText = "one";
		model.setRawText(secondSearchText);
		// start search with row >> getRowCount()
		int secondFoundIndex = table.getSearchable().search(model.getPattern(), foundIndex);
		assertEquals(foundIndex, secondFoundIndex, "must not be found");
	}

	/**
	 * test if starting a search in a non-contained index results
	 * in not-found state.
	 *
	 */
	@Test
	public void testTableInvalidStartIndex() {
		JXTable table = new JXTable(new TestTableModel());
		String firstSearchText = "one";
		PatternModel model = new PatternModel();
		model.setRawText(firstSearchText);
		// make sure we had a match
		int foundIndex = table.getSearchable().search(model.getPattern(), -1);
		assertEquals(0, foundIndex, "must return be found");
		// start search with row >> getRowCount()
		int notFoundIndex = table.getSearchable().search(model.getPattern(), table.getRowCount() * 5, false);
		assertEquals(-1, notFoundIndex, "must not be found");
	}
	/**
	 * test if match in same row but different column is found in backwards
	 * search.
	 *
	 */
	@Test
	public void testTableFoundPreviousColumnInSameRow() {
		JXTable table = new JXTable(new TestTableModel());
		int lastColumn = table.getColumnCount() - 1;
		int row = 90;
		String firstSearchText = table.getValueAt(row, lastColumn).toString();
		// need a pattern for backwards search
		PatternModel model = new PatternModel();
		model.setRawText(firstSearchText);
		int foundIndex = table.getSearchable().search(model.getPattern(), -1, true);
		assertEquals(row, foundIndex, "last line found");
		String secondSearchText = table.getValueAt(row, lastColumn - 1).toString();
		model.setRawText(secondSearchText);
		int secondFoundIndex = table.getSearchable().search(model.getPattern(), foundIndex, true);
		assertEquals(foundIndex, secondFoundIndex, "must find match in same row");
	}

	/**
	 * check if not-wrapping returns not found marker (-1).
	 *
	 */
	@Test
	public void testTableNotFoundWithoutWrapping() {
		JXTable table = new JXTable(new TestTableModel());
		int row = 90;
		String searchText = table.getValueAt(row, 0).toString();
		int foundIndex = table.getSearchable().search(searchText);
		assertEquals(row, foundIndex, "last line found");
		int notFoundIndex = table.getSearchable().search(searchText, foundIndex);
		assertEquals(-1, notFoundIndex, "nothing found after last line");
	}

	/**
	 * test if not-wrapping returns not found marker (-1) if the
	 * last found position is the last row.
	 *
	 */
	@Test
	public void testTableNotFoundLastRowWithoutWrapping() {
		JXTable table = new JXTable(new TestTableModel());
		int row = table.getRowCount() - 1;
		String searchText = table.getValueAt(row, 0).toString();
		int foundIndex = table.getSearchable().search(searchText);
		assertEquals(row, foundIndex, "last line found");
		int notFoundIndex = table.getSearchable().search(searchText, foundIndex);
		assertEquals(-1, notFoundIndex, "nothing found after last line");
	}

	/**
	 * test if not-wrapping returns not found marker (-1) if the
	 * last found position is the first row.
	 *
	 */
	@Test
	public void testTableNotFoundFirstRowWithoutWrapping() {
		JXTable table = new JXTable(new TestTableModel());
		int row = 0;
		PatternModel model = new PatternModel();
		String searchText = table.getValueAt(row, 0).toString();
		model.setRawText(searchText);
		int foundIndex = table.getSearchable().search(model.getPattern(), row + 1, true);
		assertEquals(row, foundIndex, "last line found");
		int notFoundIndex = table.getSearchable().search(model.getPattern(), foundIndex, true);
		assertEquals(-1, notFoundIndex, "nothing found after last line");
	}

	/**
	 * Issue #??-swingx: backwards search not implemented in JXEditorPane.
	 *
	 */
	@Test
	public void testEditorBackwards() {
		JXEditorPane editor = new JXEditorPane();
		String text = "fou four";
		editor.setText(text);
		int first = 2;
		try {
			editor.getDocument().getText(first, editor.getDocument().getLength() - first);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PatternModel model = new PatternModel();
		model.setRawText("fo");
		int foIndex = editor.getSearchable().search(model.getPattern(), text.length() - 1, true);
		assertEquals(text.lastIndexOf("fo"), foIndex, "found index must be last occurence");
	}

	/**
	 * Test incremental searching: index must be the same if
	 * extended text still matches.
	 *
	 */
	@Test
	public void testEditorIncremental() {
		JXEditorPane editor = new JXEditorPane();
		String text = "fou four";
		editor.setText(text);
		String search = "fo";
		int first = editor.getSearchable().search(search, 0);
		assertEquals(0, first);
		String searchExt = search + "u";
		int second = editor.getSearchable().search(searchExt, first);
		assertEquals(first, second, "index must be same if extension matches");
	}

	/**
	 * NPE on no-match?
	 */
	@Test
	public void testEditorIncrementalNotFound() {
		JXEditorPane editor = new JXEditorPane();
		String text = "fou four";
		editor.setText(text);
		String search = "ou";
		int first = editor.getSearchable().search(search, 0);
		assertEquals(1, first);
		String searchExt = search + "u";
		try {
			int second = editor.getSearchable().search(searchExt, first);
			assertEquals(-1, second, "not found");

		} catch (NullPointerException npe) {
			fail("npe");
		}
	}

	/**
	 * test that search moves forward.
	 *
	 */
	@Test
	public void testEditorFindNext() {
		JXEditorPane editor = new JXEditorPane();
		String text = "fou four";
		editor.setText(text);
		String search = "fou";
		int first = editor.getSearchable().search(search, -1);
		assertEquals(0, first);
		int second = editor.getSearchable().search(search, first);
		assertEquals(4, second);
	}
	/**
	 * Testing graceful handling of start index out-of range
	 * of document size.
	 *
	 */
	@Test
	public void testEditorTolerateExceedingStartIndex() {
		JXEditorPane editor = new JXEditorPane();
		editor.setText("fou four");
		try {
			int foIndex = editor.getSearchable().search("fo", 20);
			assertEquals(-1, foIndex);

		} catch (Exception ex) {
			fail("search must not throw if index out off range");
		}
	}

	@Test
	public void testEditorEmptyDocument() {
		JXEditorPane editor = new JXEditorPane();
		int foIndex = editor.getSearchable().search("fo", -1);
		assertEquals(-1, foIndex);
		foIndex = editor.getSearchable().search("fo", 0);
		assertEquals(-1, foIndex);
	}

	/**
	 * testing incremental search:
	 * must start search at given position (inclusive).
	 *
	 * This implies that search(xx, -1) is equivalent to
	 * search(xx, 0) if the match is at position 0.
	 *
	 */
	@Test
	public void testEditorBoundarySearchIndex() {
		JXEditorPane editor = new JXEditorPane();
		editor.setText("f");
		// can't test in one method - the searchable has internal state
		int startOff = editor.getSearchable().search("f", -1);
		assertEquals(0, startOff, "must return first occurence if startIndex if off");
		// sanity - must not find mismatch if longer
		int foIndex = editor.getSearchable().search("fo", -1);
		assertEquals(-1, foIndex, "must not find exceeding text");
		foIndex = editor.getSearchable().search("f", 0);
		assertEquals(0, foIndex, "must return first occurence from startIndex inclusively");
	}

	/**
	 * Issue #100-swingx: expect to return the start of the match.
	 * Only then it's possible to implement a reasonably behaved
	 * incremental search.
	 *
	 */
	@Test
	public void testEditorFindMatchPosition() {
		JXEditorPane editor = new JXEditorPane();
		editor.setText("fou four");
		int foIndex = editor.getSearchable().search("fo", -1);
		assertEquals(0, foIndex);
	}

	/**
	 * testing Searchable assumption along the lines of:
	 * found = searchable.search(text)
	 * searchable.getValueAt(found).startsWith(text) or
	 * searchable.getValueAt(found).contains(text)
	 *
	 */
	@Test
	public void testEditorFindMatch() {
		JXEditorPane editor = new JXEditorPane();
		editor.setText("fou four");
		int foIndex = editor.getSearchable().search("fo", -1);
		assertEquals("fo", editor.getSelectedText(), "selected text must be equals to input");
		try {
			String textAt = editor.getText(foIndex, 2);
			assertEquals("fo", textAt);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testTable() {
		JXTable table = new JXTable(new TestTableModel());
		// There are 100 instances of "One" in the test document
		int useIndex = -1;
		int lastIndex = -1;
		for (int i = 0; i < 100; i++) {
			lastIndex = table.getSearchable().search("One", useIndex);
			assertTrue(lastIndex != -1);
			assertTrue(lastIndex != useIndex);

			assertEquals(lastIndex, table.getSelectedRow(), "Row not selected");
			assertEquals(0, table.getSelectedColumn(), "Column not selected");

			String value = (String) table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
			assertTrue(value.startsWith("One"));

			useIndex = lastIndex;
		}
	}

	@Test
	public void testNullSearchable() {
		// This test will not work in a headless configuration.
		if (GraphicsEnvironment.isHeadless()) {
			return;
		}
		JXFindPanel find = new JXFindPanel();
		find.match();
	}

	public static class TestTableModel extends AbstractTableModel {

		public static String[] data = {"One", "Two", "Three", "Four", "Five"};

		@Override
		public int getRowCount() {
			return 100;
		}

		@Override
		public int getColumnCount() {
			return data.length;
		}

		@Override
		public Object getValueAt(int row, int column) {
			checkCoordinates(row, column);
			StringBuffer buffer = new StringBuffer(data[column]);
			buffer.append(row);
			return buffer.toString();
		}

		private void checkCoordinates(int row, int column) {
			if ((row < 0) || (row >= getRowCount()) || (column < 0) || (column >= getColumnCount()))
				throw new IllegalArgumentException("coordinates invalid - row/column: " + row + "/" + column);
		}
	}

	public static class TestListModel extends AbstractListModel {

		@Override
		public int getSize() {
			return 100;
		}

		@Override
		public Object getElementAt(int index) {
			int dataPos = index % TestTableModel.data.length;
			return TestTableModel.data[dataPos] + index;
		}
	}
	/**
	 * A small class that implements the Searchable interface.
	 */
	public static class TestSearchable extends JLabel implements Searchable {

		private boolean succeed;

		public TestSearchable() {
			this(false);
		}

		/**
		 * @param succeed flag to indicate that all searches succeed.
		 */
		public TestSearchable(boolean succeed) {
			this.succeed = succeed;
		}

		@Override
		public int search(String searchString) {
			return search(searchString, -1);
		}

		@Override
		public int search(String searchString, int startIndex) {
			return succeed ? 100 : -1;
		}

		@Override
		public int search(String searchString, int startIndex, boolean backward) {
			return succeed ? 100 : -1;
		}

		@Override
		public int search(Pattern pattern) {
			return search(pattern, -1);
		}

		@Override
		public int search(Pattern pattern, int startIndex) {
			return succeed ? 100 : -1;
		}

		@Override
		public int search(Pattern pattern, int startIndex, boolean backwards) {
			return succeed ? 100 : -1;
		}
	}
}
