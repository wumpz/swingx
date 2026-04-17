/*
 * $Id$
 *
 * Copyright 2008 Sun Microsystems, Inc., 4150 Network Circle,
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
 */
package org.jdesktop.swingx.autocomplete;

import static java.awt.event.KeyEvent.CHAR_UNDEFINED;
import static java.awt.event.KeyEvent.KEY_PRESSED;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_END;
import static java.awt.event.KeyEvent.VK_HOME;
import static java.awt.event.KeyEvent.VK_PAGE_DOWN;
import static java.awt.event.KeyEvent.VK_PAGE_UP;
import static java.awt.event.KeyEvent.VK_UP;
import static java.lang.System.currentTimeMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;
import org.jdesktop.test.EDTRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 *
 * @author Karl George Schaefer
 */
@ExtendWith(EDTRunner.class)
public class AutoCompleteDecoratorTest {
	private JComboBox combo;

	@BeforeEach
	public void setUp() throws Exception {
		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		combo = new JComboBox(new String[] {"Alpha", "Bravo", "Charlie", "Delta"});
	}

	/**
	 * Issue #1570-swingx: decorating Jlist without converter throws NPE
	 * test listAdapter
	 */
	@Test
	public void testDefaultConverterListAdapterTwo() {
		JList list = new JList(combo.getModel());
		JTextComponent text = new JTextField();
		ListAdaptor adapter = new ListAdaptor(list, text);
		assertSame(
				ObjectToStringConverter.DEFAULT_IMPLEMENTATION,
				adapter.stringConverter,
				"default adapter in two-param constructor");
	}

	/**
	 * Issue #1570-swingx: decorating Jlist without converter throws NPE
	 * test listAdapter, three param constructor
	 */
	@Test
	public void testDefaultConverterListAdapterThree() {
		JList list = new JList(combo.getModel());
		JTextComponent text = new JTextField();
		ListAdaptor adapter = new ListAdaptor(list, text, null);
		assertSame(
				ObjectToStringConverter.DEFAULT_IMPLEMENTATION,
				adapter.stringConverter,
				"default adapter in three-param constructor");
	}

	/**
	 * Issue #1570-swingx: decorating Jlist without converter throws NPE
	 *
	 */
	@Test
	public void testDefaultConverterJList() {
		JList list = new JList(combo.getModel());
		JTextComponent text = new JTextField();
		AutoCompleteDecorator.decorate(list, text);
		text.setText("A");
	}

	/**
	 * Issue #1570-swingx: decorating list without converter throws NPE
	 * completeness: test default converter for textComponent with List
	 *
	 */
	@Test
	public void testDefaultConverterList() {
		List<?> list = Arrays.asList(new String[] {"Alpha", "Bravo", "Charlie", "Delta"});
		JTextComponent text = new JTextField();
		AutoCompleteDecorator.decorate(text, list, true);
		text.setText("A");
	}

	/**
	 * Issue #1570-swingx: decorating list without converter throws NPE
	 * completeness: test default converter for combo
	 */
	@Test
	public void testDefaultConverterCombo() {
		AutoCompleteDecorator.decorate(combo);
		JTextComponent text = (JTextComponent) combo.getEditor().getEditorComponent();
		text.setText("A");
	}

	/**
	 * SwingX Issue #299.
	 */
	@Test
	public void testUndecorateComboBox() {
		combo.setEditable(false);
		AutoCompleteDecorator.decorate(combo);
		AutoCompleteDecorator.undecorate(combo);

		for (PropertyChangeListener l : combo.getPropertyChangeListeners("editor")) {
			assertThat(l).isNotInstanceOf(AutoComplete.PropertyChangeListener.class);
		}

		assertThat(combo.getEditor()).isNotInstanceOf(AutoCompleteComboBoxEditor.class);

		JTextComponent editorComponent = (JTextComponent) combo.getEditor().getEditorComponent();

		for (KeyListener l : editorComponent.getKeyListeners()) {
			assertThat(l).isNotInstanceOf(AutoComplete.KeyAdapter.class);
		}

		for (InputMap map = editorComponent.getInputMap(); map != null; map = map.getParent()) {
			assertThat(map).isNotInstanceOf(AutoComplete.InputMap.class);
		}

		assertThat(editorComponent.getActionMap().get("nonstrict-backspace")).isNull();

		for (FocusListener l : editorComponent.getFocusListeners()) {
			assertThat(l).isNotInstanceOf(AutoComplete.FocusAdapter.class);
		}

		assertThat(editorComponent.getDocument()).isNotInstanceOf(AutoCompleteDocument.class);

		for (ActionListener l : combo.getActionListeners()) {
			assertThat(l).isNotInstanceOf(ComboBoxAdaptor.class);
		}
	}

	/**
	 * SwingX Issue #299.
	 */
	@Test
	public void testRedecorateComboBox() {
		AutoCompleteDecorator.decorate(combo);
		Component editor = combo.getEditor().getEditorComponent();

		int expectedFocusListenerCount = editor.getFocusListeners().length;
		int expectedKeyListenerCount = editor.getKeyListeners().length;
		int expectedPropListenerCount = combo.getPropertyChangeListeners("editor").length;
		int expectedActionListenerCount = combo.getActionListeners().length;

		AutoCompleteDecorator.decorate(combo);
		editor = combo.getEditor().getEditorComponent();

		assertThat(editor.getFocusListeners().length).isEqualTo(expectedFocusListenerCount);
		assertThat(editor.getKeyListeners().length).isEqualTo(expectedKeyListenerCount);
		assertThat(combo.getPropertyChangeListeners("editor").length).isEqualTo(expectedPropListenerCount);
		assertThat(combo.getActionListeners().length).isEqualTo(expectedActionListenerCount);
	}

	/**
	 * SwingX Issue #299.
	 */
	@Test
	public void testUndecorateList() {
		JList list = new JList();
		JTextField textField = new JTextField();
		AutoCompleteDecorator.decorate(list, textField);
		AutoCompleteDecorator.undecorate(list);

		for (ListSelectionListener l : list.getListSelectionListeners()) {
			assertThat(l).isNotInstanceOf(ListAdaptor.class);
		}
	}

	/**
	 * SwingX Issue #299.
	 */
	@Test
	public void testRedecorateList() {
		JList list = new JList();
		JTextField textField = new JTextField();
		AutoCompleteDecorator.decorate(list, textField);

		int expectedListSelectionListenerCount = list.getListSelectionListeners().length;

		AutoCompleteDecorator.decorate(list, textField);

		assertThat(list.getListSelectionListeners().length).isEqualTo(expectedListSelectionListenerCount);
	}

	/**
	 * SwingX Issue #299.
	 */
	@Test
	public void testUndecorateTextComponent() {
		JTextField textField = new JTextField();

		AutoCompleteDecorator.decorate(textField, Collections.emptyList(), true);

		AutoCompleteDecorator.undecorate(textField);

		assertThat(textField.getInputMap()).isNotInstanceOf(AutoComplete.InputMap.class);
		assertThat(textField.getActionMap().get("nonstrict-backspace")).isNull();
		for (FocusListener l : textField.getFocusListeners()) {
			assertThat(l).isNotInstanceOf(AutoComplete.FocusAdapter.class);
		}
		assertThat(textField.getDocument()).isNotInstanceOf(AutoCompleteDocument.class);
	}

	/**
	 * SwingX Issue #299.
	 */
	@Test
	public void testRedecorateTextComponent() {
		JTextField textField = new JTextField();
		AutoCompleteDecorator.decorate(textField, Collections.emptyList(), true);

		int expectedFocusListenerLength = textField.getFocusListeners().length;

		AutoCompleteDecorator.decorate(textField, Collections.emptyList(), true);

		assertThat(textField.getFocusListeners().length).isEqualTo(expectedFocusListenerLength);
	}

	@Test
	public void testDecoratingJTextPane() {
		List<String> strings = Arrays.asList("Alpha", "Bravo", "Charlie", "Delta");
		AutoCompleteDecorator.decorate(new JTextPane(), strings, true);
	}

	@Test
	public void testAddingItemsAfterDecorating() {
		AutoCompleteDecorator.decorate(combo);
		combo.addItem("Echo");
	}

	@Test
	public void testAddingItemsAfterDecoratingEmpty() {
		JComboBox box = new JComboBox();
		AutoCompleteDecorator.decorate(box);
		box.addItem("Alhpa");
	}

	@Test
	public void testRemovingItemsAfterDecorating() {
		AutoCompleteDecorator.decorate(combo);
		combo.removeAll();
	}

	/**
	 * SwingX Issue #1322.
	 */
	@Test
	@Tag("Visual")
	public void testNonStrictCompletionWithKeyMovement() {
		assumeFalse(GraphicsEnvironment.isHeadless());

		combo.setEditable(true);
		AutoCompleteDecorator.decorate(combo);
		combo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ("".equals(combo.getSelectedItem())) {
					fail("received illegal value");
				}
			}
		});

		JFrame frame = new JFrame();
		frame.add(combo);
		frame.pack();
		frame.setVisible(true);

		assertThat((String) combo.getSelectedItem()).isEqualTo("Alpha");
		assumeFalse(combo.isPopupVisible());

		combo.processKeyEvent(new KeyEvent(combo, KEY_PRESSED, currentTimeMillis(), 0, VK_DOWN, CHAR_UNDEFINED));
		assertThat(combo.isPopupVisible()).isEqualTo(true);

		combo.processKeyEvent(new KeyEvent(combo, KEY_PRESSED, currentTimeMillis(), 0, VK_DOWN, CHAR_UNDEFINED));
		assertThat((String) combo.getSelectedItem()).isEqualTo("Bravo");

		combo.processKeyEvent(new KeyEvent(combo, KEY_PRESSED, currentTimeMillis(), 0, VK_PAGE_DOWN, CHAR_UNDEFINED));
		assertThat((String) combo.getSelectedItem()).isEqualTo("Delta");

		combo.processKeyEvent(new KeyEvent(combo, KEY_PRESSED, currentTimeMillis(), 0, VK_UP, CHAR_UNDEFINED));
		assertThat((String) combo.getSelectedItem()).isEqualTo("Charlie");

		combo.processKeyEvent(new KeyEvent(combo, KEY_PRESSED, currentTimeMillis(), 0, VK_PAGE_UP, CHAR_UNDEFINED));
		assertThat((String) combo.getSelectedItem()).isEqualTo("Alpha");

		combo.processKeyEvent(new KeyEvent(combo, KEY_PRESSED, currentTimeMillis(), 0, VK_END, CHAR_UNDEFINED));
		assertThat((String) combo.getSelectedItem()).isEqualTo("Delta");

		combo.processKeyEvent(new KeyEvent(combo, KEY_PRESSED, currentTimeMillis(), 0, VK_HOME, CHAR_UNDEFINED));
		assertThat((String) combo.getSelectedItem()).isEqualTo("Alpha");
	}
}
