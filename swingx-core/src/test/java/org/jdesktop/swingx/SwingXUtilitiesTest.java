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
package org.jdesktop.swingx;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.RepaintManager;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.UIResource;
import org.jdesktop.swingx.JXCollapsiblePane.CollapsiblePaneContainer;
import org.jdesktop.swingx.plaf.basic.BasicDatePickerUI;
import org.jdesktop.test.EDTRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Contains tests for SwingXUtilities.
 *
 * @author Jeanette Winzenburg
 */
public class SwingXUtilitiesTest extends InteractiveTestCase {
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(SwingXUtilitiesTest.class.getName());

	public static void main(String args[]) {
		setSystemLF(true);
		//        Locale.setDefault(new Locale("es"));
		SwingXUtilitiesTest test = new SwingXUtilitiesTest();
		try {
			test.runInteractiveTests();
			//            test.runInteractiveTests("interactive.*Compare.*");
			//            test.runInteractiveTests("interactive.*Tree.*");
		} catch (Exception e) {
			System.err.println("exception when executing interactive tests:");
			e.printStackTrace();
		}
	}

	/**
	 * Test doc'ed contract of isUIInstallable.
	 */
	@Test
	public void testUIInstallable() {
		assertEquals(true, SwingXUtilities.isUIInstallable(null), "null must be uiInstallable ");
		assertUIInstallable(new Color(10, 10, 10));
		assertUIInstallable(new ColorUIResource(10, 10, 10));
	}

	/**
	 * @param color
	 */
	private void assertUIInstallable(Object color) {
		assertEquals(color instanceof UIResource, SwingXUtilities.isUIInstallable(color), "uiInstallabe must be same ");
	}

	@Test
	public void testUpdateAllComponentTreeUIs() {
		// This test will not work in a headless configuration.
		if (GraphicsEnvironment.isHeadless()) {
			LOG.fine("cannot run test - headless environment");
			return;
		}
		if (isCrossPlatformLFSameAsSystem()) {
			LOG.info("cannot run test - no safe LFs to toggle");
			return;
		}
		List<RootPaneContainer> toplevels = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			JXFrame frame = new JXFrame();
			toplevels.add(frame);
			toplevels.add(new JDialog(frame));
			toplevels.add(new JWindow(frame));
		}
		// sanity
		if (!UIManager.getLookAndFeel().isNativeLookAndFeel()) {
			LOG.warning("Assumption is to start with native LaF. Found " + UIManager.getLookAndFeel() + " instead.");
		}
		setSystemLF(false);
		SwingXUtilities.updateAllComponentTreeUIs();
		// sanity
		for (RootPaneContainer window : toplevels) {
			JRootPane rootPane = window.getRootPane();
			assertEquals(
					UIManager.get(rootPane.getUIClassID()),
					rootPane.getUI().getClass().getName());
		}
	}

	/**
	 * @return boolean indicating if system and
	 *   cross platform LF are different.
	 */
	private boolean isCrossPlatformLFSameAsSystem() {
		return UIManager.getCrossPlatformLookAndFeelClassName().equals(UIManager.getSystemLookAndFeelClassName());
	}

	@BeforeEach
	public void setUp() throws Exception {
		setSystemLF(true);
	}

	@Test
	public void testGetTranslucentRepaintManagerWithNull() {
		assertThrows(NullPointerException.class, () -> SwingXUtilities.getTranslucentRepaintManager(null));
	}

	@Test
	public void testGetTranslucentRepaintManagerWithTranslucent() {
		RepaintManagerX rmx = new RepaintManagerX(new RepaintManager());
		RepaintManager rm = SwingXUtilities.getTranslucentRepaintManager(rmx);

		assertSame(rmx, rm);
	}

	@Test
	public void testGetTranslucentRepaintManagerWithNonTranslucent() {
		RepaintManager rm = new RepaintManager();
		RepaintManager rmx = SwingXUtilities.getTranslucentRepaintManager(rm);

		assertNotSame(rm, rmx);
		assertTrue(rmx.getClass().isAnnotationPresent(TranslucentRepaintManager.class));
	}

	@Test
	public void testGetTranslucentRepaintManagerWithForwardingAndTranslucent() {
		RepaintManagerX rmx = new RepaintManagerX(new RepaintManager());
		ForwardingRepaintManager frm = new ForwardingRepaintManager(rmx);
		RepaintManager rm = SwingXUtilities.getTranslucentRepaintManager(frm);

		assertSame(frm, rm);
	}

	@Test
	public void testGetTranslucentRepaintManagerWithForwardingAndNonTranslucent() {
		ForwardingRepaintManager frm = new ForwardingRepaintManager(new RepaintManager());
		RepaintManager rm = SwingXUtilities.getTranslucentRepaintManager(frm);

		assertNotSame(frm, rm);
		assertTrue(rm.getClass().isAnnotationPresent(TranslucentRepaintManager.class));
	}

	@Test
	public void testInvokeLater() {
		assertThat(SwingUtilities.isEventDispatchThread()).isEqualTo(false);

		Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				assertThat(SwingUtilities.isEventDispatchThread()).isEqualTo(true);

				// wait a long time
				Thread.sleep(1000);

				return null;
			}
		};

		long start = System.currentTimeMillis();
		FutureTask<Void> task = SwingXUtilities.invokeLater(callable);
		assertThat((System.currentTimeMillis() - start) < 100).isEqualTo(true);
	}

	@Test
	public void testInvokeAndWait() throws Exception {
		assertThat(SwingUtilities.isEventDispatchThread()).isEqualTo(false);

		Callable<Boolean> callable = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				assertThat(SwingUtilities.isEventDispatchThread()).isEqualTo(true);

				// wait a long time
				Thread.sleep(1000);

				return true;
			}
		};

		long start = System.currentTimeMillis();
		assertThat(SwingXUtilities.invokeAndWait(callable)).isEqualTo(true);
		assertThat((System.currentTimeMillis() - start) > 1000).isEqualTo(true);
	}

	@Nested
	@ExtendWith(EDTRunner.class)
	public class GetAncestorTest {
		private Component source;

		@BeforeEach
		public void setUp() {
			JXTaskPane pane = new JXTaskPane();
			source = pane.add(new JButton());

			JXTaskPaneContainer tpc = new JXTaskPaneContainer();
			tpc.add(pane);

			JPanel panel = new JPanel();
			panel.add(tpc);
		}

		@Test
		public void testNullClass() {
			assertNull(SwingXUtilities.getAncestor(null, source));
		}

		@Test
		public void testNullSource() {
			assertThat(SwingXUtilities.getAncestor(JPanel.class, null)).isNull();
		}

		@Test
		public void testFindAncestorClass() {
			assertThat(SwingXUtilities.getAncestor(JXTaskPane.class, source)).isNotNull();
		}

		@Test
		public void testFindAncestorInterface() {
			assertThat(SwingXUtilities.getAncestor(CollapsiblePaneContainer.class, source))
					.isNotNull();
		}

		@Test
		public void testFindMissingAncestorClass() {
			assertThat(SwingXUtilities.getAncestor(JComboBox.class, source)).isNull();
		}

		@Test
		public void testFindMissingAncestorInterface() {
			assertThat(SwingXUtilities.getAncestor(PropertyChangeListener.class, source))
					.isNull();
		}
	}

	@Test
	public void testDescendingNull() {
		assertFalse(SwingXUtilities.isDescendingFrom(null, null), "both nulls are not descending");
		assertFalse(SwingXUtilities.isDescendingFrom(null, new JScrollPane()), "null comp is not descending");
		assertFalse(SwingXUtilities.isDescendingFrom(new JLabel(), null), "comp is not descending null parent");
	}

	@Test
	public void testDescendingSame() {
		JComponent comp = new JLabel();
		assertTrue(SwingXUtilities.isDescendingFrom(comp, comp), "same component must be interpreted as descending");
	}

	@Test
	public void testDescendingPopup() throws InterruptedException, InvocationTargetException {
		if (GraphicsEnvironment.isHeadless()) {
			LOG.fine("cannot run - headless");
			return;
		}
		final JXDatePicker picker = new JXDatePicker();
		JXFrame frame = new JXFrame("showing", false);
		frame.add(picker);
		frame.pack();
		frame.setVisible(true);
		assertFalse(SwingXUtilities.isDescendingFrom(picker.getMonthView(), picker));
		Action togglePopup = picker.getActionMap().get("TOGGLE_POPUP");
		togglePopup.actionPerformed(null);
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				assertTrue(((BasicDatePickerUI) picker.getUI()).isPopupVisible(), "popup visible ");
				assertTrue(SwingXUtilities.isDescendingFrom(picker.getMonthView(), picker));
			}
		});
		frame.dispose();
	}
}
