/*
 * $Id$
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.search;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.jdesktop.test.PropertyChangeReport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Testing PatternModel.
 * 
 * @author Jeanette Winzenburg
 */
public class PatternModelTest {

    final static String startAnchor = "^";
    final static String endAnchor = "$";
    final static String middleStartAnchor = "some" + startAnchor + "one";
    final static String middleEndAnchor = "some" + endAnchor + "one";
    private PropertyChangeReport propertyReport;


    @BeforeEach
    public void setUpJ4() throws Exception {
        setUp();
    }
    
    @AfterEach
    public void tearDownJ4() throws Exception {
    }
    
    /**
     * test initial autoAdjust of foundIndex in backward search.
     *
     */
    @Test
    public void testFoundIndexAutoAdjustBackward() {
        PatternModel model = new PatternModel();
        model.setBackwards(true);
        assertTrue(model.isAutoAdjustFoundIndex(), "is autoAdjust by default");
        model.addPropertyChangeListener(propertyReport);
        int foundIndex = 10;
        model.setFoundIndex(foundIndex);
        // assert changed value
        assertEquals(foundIndex -1, model.getFoundIndex(), "foundIndex must be set");
        assertTrue(propertyReport.hasEvents("foundIndex"), 
                "changing foundIndex must have fired foundIndex property");
    }

    /**
     * test initial autoAdjust of foundIndex in forward search.
     *
     */
    @Test
    public void testFoundIndexAutoAdjustNotFound() {
        PatternModel model = new PatternModel();
        assertTrue(model.isAutoAdjustFoundIndex(), "is autoAdjust by default");
        model.addPropertyChangeListener(propertyReport);
        int foundIndex = -1;
        model.setFoundIndex(foundIndex);
        // assert changed value
        assertEquals(foundIndex, model.getFoundIndex(), "foundIndex must be set");
    }


    /**
     * test initial autoAdjust of foundIndex in forward search.
     *
     */
    @Test
    public void testFoundIndexAutoAdjustForward() {
        PatternModel model = new PatternModel();
        assertTrue(model.isAutoAdjustFoundIndex(), "is autoAdjust by default");
        model.addPropertyChangeListener(propertyReport);
        int foundIndex = 10;
        model.setFoundIndex(foundIndex);
        // assert changed value
        assertEquals(foundIndex + 1, model.getFoundIndex(), "foundIndex must be set");
        assertTrue(propertyReport.hasEvents("foundIndex"), 
                "changing foundIndex must have fired foundIndex property");
    }

    /**
     * test initial value and notification of "foundIndex" property.
     *
     */
    @Test
    public void testFoundIndex() {
        PatternModel model = new PatternModel();
        model.setIncremental(true);
        // assert initial value
        assertEquals(-1, model.getFoundIndex(), "not found on start");
        model.addPropertyChangeListener(propertyReport);
        model.setFoundIndex(10);
        // assert changed value
        assertEquals(10, model.getFoundIndex(), "foundIndex must be set");
        assertTrue(propertyReport.hasEvents("foundIndex"), 
                "changing foundIndex must have fired foundIndex property");
    }
    
    @Test
    public void testCaseSensitive() {
        PatternModel model = new PatternModel();
        model.setRawText("tab");
        assertTrue(model.getPattern().matcher("JTABLE").find(), 
                "must find not case sensitive by default");
        model.addPropertyChangeListener(propertyReport);
        model.setCaseSensitive(true);
        assertTrue(propertyReport.hasEvents("caseSensitive"), 
                "changing case sensitive must fire casesensitive property");
        assertTrue(propertyReport.hasEvents("pattern"), 
                "changing case sensitive must fire pattern property");
        
    }
    @Test
    public void testAvailableMatchRules() {
        PatternModel model = new PatternModel();
        List<?> rules = model.getMatchRules();
        assertNotNull(rules, "rules must not be null");
    }
    
    @Test
    public void testRegexCreator() {
        PatternModel model = new PatternModel();
        model.addPropertyChangeListener(propertyReport);
        model.setRegexCreatorKey(PatternModel.REGEX_UNCHANGED);
        assertEquals(PatternModel.REGEX_UNCHANGED, model.getRegexCreatorKey(), "search string mode must be");
        assertTrue(propertyReport.hasEvents("regexCreatorKey"));
        
//        model.setSearchStringMode(PatternModel.SEARCH_STRING_ANCHORED);
//        model.setSearchStringMode(PatternModel.SEARCH_STRING_WILDCARD);
//        model.setSearchStringMode(PatternModel.SEARCH_STRING_EXPLICIT);
    }
    
    @Test
    public void testMatchRule() {
        PatternModel model = new PatternModel();
        model.addPropertyChangeListener(propertyReport);
        // default searchStringMode
        assertEquals(PatternModel.REGEX_MATCH_RULES, 
                model.getRegexCreatorKey(), "search string mode must be");
        // default searchCategory
        assertEquals(PatternModel.MATCH_RULE_CONTAINS, 
                model.getMatchRule(), "search category must be ");
        // change category and test if property change is fired
        model.setMatchRule(PatternModel.MATCH_RULE_EQUALS);
        assertTrue(propertyReport.hasEvents("matchRule"), "model must have fired " + "matchRule ");
    }
    
    @Test
    public void testChangeMatchRule() {
        PatternModel model = new PatternModel();
        String contained = "t";
        model.setRawText(contained);
        String match = "x" + contained + "x";
        assertTrue(model.getPattern().matcher(match).find(), "pattern must find " + match);
        model.addPropertyChangeListener(propertyReport);
        model.setMatchRule(PatternModel.MATCH_RULE_EQUALS);
        assertTrue(propertyReport.hasEvents("pattern"), "model must have fire pattern change");
        assertFalse(model.getPattern().matcher(match).find(), "pattern must reject " + match);
        model.setMatchRule(PatternModel.MATCH_RULE_STARTSWITH);
        match = "txx";
        assertTrue(model.getPattern().matcher(match).find(), "pattern must find " + match);
        model.setMatchRule(PatternModel.MATCH_RULE_ENDSWITH);
        match = "xxt";
        assertTrue(model.getPattern().matcher(match).find(), "pattern must find " + match);
    }
    
    /**
     * test if rawtext is treated as literal.
     *
     */
    @Test
    public void testRawMiddleAnchorTokens() {
        PatternModel patternModel = new PatternModel();
        patternModel.setRawText(startAnchor);
        String literalAnchor = "some" + startAnchor +"one";
        assertTrue(patternModel.getPattern().matcher(literalAnchor).find(), "must find literal containing startAnchor " + literalAnchor);
        String literal = "someone";
        assertFalse(patternModel.getPattern().matcher(literal).find(), 
                "must reject literal not containing startAnchor " + literal);
    }
    
    /**
     * test if rawtext is treated as literal.
     *
     */
    @Test
    public void testRawStartAnchor() {
        PatternModel patternModel = new PatternModel();
        String anchored = startAnchor + "hap";
        patternModel.setRawText(anchored);
        String literalAnchor = startAnchor + "happy";
        assertTrue(patternModel.getPattern().matcher(literalAnchor).find(), "must find literal containing startAnchor " + literalAnchor);
        String literal = "happy";
        assertFalse(patternModel.getPattern().matcher(literal).find(), 
                "must reject literal not containing startAnchor " + literal);
    }

    @BeforeEach
    public void setUp()  {
        propertyReport = new PropertyChangeReport();
    }
    
    
}
