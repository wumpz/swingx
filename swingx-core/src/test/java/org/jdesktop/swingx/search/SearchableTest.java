/*
 * $Id$
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
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
package org.jdesktop.swingx.search;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.BorderHighlighter;
import org.jdesktop.swingx.decorator.SearchPredicate;
import org.jdesktop.swingx.search.FindTest.TestListModel;
import org.jdesktop.swingx.search.FindTest.TestTableModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Contains unit tests for Searchable implementations.
 * 
 * @author Jeanette Winzenburg
 */
public class SearchableTest {
    
    @Test
    public void testTreeSearchableFailsOnNullTree() {
        assertThrows(NullPointerException.class, () -> {
            new TreeSearchable(null);
        });
    }

    /** 
     * Issue #1209-swingx: SearchPredicate must be updated to last found 
     *   after dynamic setting of a new highlighter.
     *   
     *   
     */
    @Test
    public void testSetMatchHighlighterCleanup() {
        JXTable table = new JXTable(new TestTableModel());
        table.putClientProperty(AbstractSearchable.MATCH_HIGHLIGHTER, Boolean.TRUE);
        // move first column to end
        int firstColumn = 0;
        int row = 39;
        String firstSearchText = table.getStringAt(row, firstColumn);
        PatternModel model = new PatternModel();
        model.setRawText(firstSearchText);
        // initialize searchable to "found state"
        table.getSearchable().search(model.getPattern(), -1);
        AbstractHighlighter hl = ((AbstractSearchable) table.getSearchable()).getMatchHighlighter();
        SearchPredicate predicate = (SearchPredicate) hl.getHighlightPredicate();
        AbstractHighlighter replaceHL = new BorderHighlighter();
        ((AbstractSearchable) table.getSearchable()).setMatchHighlighter(replaceHL);
        assertTrue(replaceHL.getHighlightPredicate() instanceof SearchPredicate,
                "replaced highlighter must have searchPredicate ");
        assertEquals(predicate.getHighlightRow(), 
                ((SearchPredicate) replaceHL.getHighlightPredicate()).getHighlightRow(), 
                "replaced search predicate must be installed with old matching row");
        assertSame(replaceHL, 
                table.getHighlighters()[table.getHighlighters().length - 1], "replaced renderer must be added");
        assertFalse(Arrays.asList(table.getHighlighters()).contains(hl), "previous matchhighlighter must be removed");
        
    }

    
    /** 
     * test that the search predicate's highlight column index is in 
     * model coordinates
     *
     */
    @Test
    public void testTableMovedColumn() {
        JXTable table = new JXTable(new TestTableModel());
        table.putClientProperty(AbstractSearchable.MATCH_HIGHLIGHTER, Boolean.TRUE);
        
        // move first column to end
        int firstColumn = table.getColumnCount() - 1;
        table.getColumnModel().moveColumn(0, firstColumn);
        int row = 39;
        String firstSearchText = table.getValueAt(row, firstColumn).toString();
        PatternModel model = new PatternModel();
        model.setRawText(firstSearchText);
        // initialize searchable to "found state"
        table.getSearchable().search(model.getPattern(), -1);
        // column index in view coordinates
        int foundColumn = ((AbstractSearchable) table.getSearchable()).lastSearchResult.foundColumn;
        assertEquals(firstColumn, foundColumn, "column must be updated");
        AbstractHighlighter hl = ((AbstractSearchable) table.getSearchable()).getMatchHighlighter();
        assertTrue(hl.getHighlightPredicate() instanceof SearchPredicate, "searchPredicate");
        SearchPredicate predicate = (SearchPredicate) hl.getHighlightPredicate();
        assertEquals(table.convertColumnIndexToModel(firstColumn), predicate.getHighlightColumn());
    }

    /** 
     * test that the search predicate's highlight column index is in 
     * model coordinates
     *
     */
    @Test
    public void testTableUseMatchHighlighter() {
        JXTable table = new JXTable(new TestTableModel());
        table.putClientProperty(AbstractSearchable.MATCH_HIGHLIGHTER, Boolean.TRUE);
        assertTrue(((AbstractSearchable) table.getSearchable()).markByHighlighter(), "use match highlighter");
        AbstractHighlighter hl = ((AbstractSearchable) table.getSearchable()).getMatchHighlighter();
        assertNotNull(hl);
    }
    
    /** 
     * test that the search predicate's highlight column index is in 
     * model coordinates
     *
     */
    @Test
    public void testListUseMatchHighlighter() {
        JXList table = new JXList(new TestListModel());
        table.putClientProperty(AbstractSearchable.MATCH_HIGHLIGHTER, Boolean.TRUE);
        assertTrue(((AbstractSearchable) table.getSearchable()).markByHighlighter(), "use match highlighter");
        AbstractHighlighter hl = ((AbstractSearchable) table.getSearchable()).getMatchHighlighter();
        assertNotNull(hl);
    }
    
    /** 
     * test that the search predicate's highlight column index is in 
     * model coordinates
     *
     */
    @Test
    public void testTreeUseMatchHighlighter() {
        JXTree table = new JXTree();
        table.putClientProperty(AbstractSearchable.MATCH_HIGHLIGHTER, Boolean.TRUE);
        assertTrue(((AbstractSearchable) table.getSearchable()).markByHighlighter(), "use match highlighter");
        AbstractHighlighter hl = ((AbstractSearchable) table.getSearchable()).getMatchHighlighter();
        assertNotNull(hl);
    }

    /**
     * Task: new api in AbstractSearchable to support match highlighter.
     */
    @Test
    public void testTargetTable() {
        JXTable table = new JXTable();
        AbstractSearchable searchable = (AbstractSearchable) table.getSearchable();
        assertSame(table, searchable.getTarget(), "get target same as table");
    }
    
    /**
     * Task: new api in AbstractSearchable to support match highlighter.
     */
    @Test
    public void testTargetList() {
        JXList table = new JXList();
        AbstractSearchable searchable = (AbstractSearchable) table.getSearchable();
        assertSame(table, searchable.getTarget(), "get target same as table");
    }

    /**
     * Task: new api in AbstractSearchable to support match highlighter.
     */
    @Test
    public void testTargetTree() {
        JXTree table = new JXTree();
        AbstractSearchable searchable = (AbstractSearchable) table.getSearchable();
        assertSame(table, searchable.getTarget(), "get target same as table");
    }


    @BeforeEach
    public void setUp() throws Exception {
        // sanity: new instance for each test
        SearchFactory.setInstance(new SearchFactory());
    }
    

}
