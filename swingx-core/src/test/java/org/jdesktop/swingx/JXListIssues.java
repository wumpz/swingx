/*
 * Created on 07.10.2005
 *
 */
package org.jdesktop.swingx;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JList;
import javax.swing.plaf.UIResource;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.test.PropertyChangeReport;
import org.junit.jupiter.api.Test;

public class JXListIssues extends JXListTest {

	public static void main(String[] args) {
		setSystemLF(true);
		JXListIssues test = new JXListIssues();
		try {
			test.runInteractiveTests();
			//            test.runInteractiveTests("interactive.*Rollover.*");
		} catch (Exception e) {
			System.err.println("exception when executing interactive tests:");
			e.printStackTrace();
		}
	}

	/**
	 * Issue #1232-swingx: JXList must fire property change on setCellRenderer.
	 *
	 * The very first setting fires twice: once from super, once from
	 * the forced. Keep in mind, but do nothing for now (waiting for complaints ;-)
	 *
	 */
	@Test
	public void testRendererNotificationFirst() {
		JXList list = new JXList();
		PropertyChangeReport report = new PropertyChangeReport(list);
		list.setCellRenderer(new DefaultListRenderer());
		assertEquals(1, report.getEventCount());
		assertEquals(1, report.getEventCount("cellRenderer"));
	}

	/**
	 * Issue #601-swingx: allow LAF to hook in LAF provided renderers.
	 *
	 * Expected: plain ol' list does install UIResource (while tree doesn't)
	 */
	@Test
	public void testLAFRendererList() {
		JList list = new JList();
		assertNotNull(list.getCellRenderer(), "default renderer installed");
		assertTrue(
				list.getCellRenderer() instanceof UIResource,
				"expected UIResource, but was: " + list.getCellRenderer().getClass());
	}

	/**
	 * Issue #601-swingx: allow LAF to hook in LAF provided renderers.
	 *
	 * Expected: plain ol' list does install UIResource (while tree doesn't)
	 */
	@Test
	public void testLAFRendererXList() {
		JXList list = new JXList();
		assertNotNull(list.getCellRenderer(), "default renderer installed");
		assertTrue(
				list.getCellRenderer() instanceof UIResource,
				"expected UIResource, but was: " + list.getCellRenderer().getClass());
	}

	@Test
	public void testDummy() {}
}
