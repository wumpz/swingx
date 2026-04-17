/*
 * $Id$
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
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
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.awt.Color;
import java.util.logging.Logger;
import javax.swing.plaf.ColorUIResource;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.plaf.PainterUIResource;
import org.jdesktop.test.EDTRunner;
import org.jdesktop.test.PropertyChangeReport;
import org.jdesktop.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Tests JXPanel.
 *
 * @author Karl Schaefer
 */
@ExtendWith(EDTRunner.class)
@SuppressWarnings({"rawtypes", "nls"})
public class JXPanelTest {
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(JXPanelTest.class.getName());

	/**
	 * Issue #1199-swingx: must listen to change on painter
	 */
	@Test
	public void testPainterChangeListener() {
		JXPanel panel = new JXPanel();
		final MattePainter painter = new MattePainter(Color.RED);
		int listenerCount = painter.getPropertyChangeListeners().length;
		panel.setBackgroundPainter(painter);
		assertEquals(listenerCount + 1, painter.getPropertyChangeListeners().length);
		panel.setBackgroundPainter(null);
		assertEquals(listenerCount, painter.getPropertyChangeListeners().length);
	}

	@Test
	public void testScrollableWidthTrackProperty() {
		JXPanel panel = new JXPanel();
		ScrollableSizeHint oldTrack = panel.getScrollableWidthHint();
		PropertyChangeReport report = new PropertyChangeReport(panel);
		ScrollableSizeHint none = ScrollableSizeHint.PREFERRED_STRETCH;
		panel.setScrollableWidthHint(none);
		assertSame(none, panel.getScrollableWidthHint());
		TestUtils.assertPropertyChangeEvent(report, "scrollableWidthHint", oldTrack, none);
	}

	@Test
	public void testScrollableHeightTrackProperty() {
		JXPanel panel = new JXPanel();
		ScrollableSizeHint oldTrack = panel.getScrollableHeightHint();
		PropertyChangeReport report = new PropertyChangeReport(panel);
		ScrollableSizeHint none = ScrollableSizeHint.PREFERRED_STRETCH;
		panel.setScrollableHeightHint(none);
		assertSame(none, panel.getScrollableHeightHint());
		TestUtils.assertPropertyChangeEvent(report, "scrollableHeightHint", oldTrack, none);
	}

	@Test
	public void testScrollableHeightTrackNull() {
		assertThrows(NullPointerException.class, () -> new JXPanel().setScrollableHeightHint(null));
	}

	@Test
	public void testScrollableWidthTrackNull() {
		assertThrows(NullPointerException.class, () -> new JXPanel().setScrollableWidthHint(null));
	}

	/**
	 * Initial value (for backward compatibility: FIT)
	 *
	 */
	@Test
	public void testScrollableSizeTrackProperty() {
		JXPanel panel = new JXPanel();
		assertSame(ScrollableSizeHint.FIT, panel.getScrollableWidthHint());
		assertSame(ScrollableSizeHint.FIT, panel.getScrollableHeightHint());
	}

	/**
	 * Sanity: compatibility for width tracking.
	 */
	@Test
	public void testScrollableTracksWidth() {
		JXPanel panel = new JXPanel();
		assertTrue(panel.getScrollableTracksViewportWidth());
		panel.setScrollableTracksViewportWidth(false);
		assertFalse(panel.getScrollableTracksViewportWidth());
	}

	/**
	 * Sanity: compatibility for height tracking.
	 */
	@Test
	public void testScrollableTracksHeight() {
		JXPanel panel = new JXPanel();
		assertTrue(panel.getScrollableTracksViewportHeight());
		panel.setScrollableTracksViewportHeight(false);
		assertFalse(panel.getScrollableTracksViewportHeight());
	}

	/**
	 * SwingX #962: ensure that background painter is initially {@code null}.
	 */
	@Test
	public void testBackgroundPainterIsNull() {
		Painter<?> painter = new JXPanel().getBackgroundPainter();

		assertThat(painter).isNull();
	}

	/**
	 * SwingX #964: ensure setting background color sets painter.
	 */
	@Test
	public void testSetColorDoesNotOverrideNullBackgroundPainter() {
		JXPanel panel = new JXPanel();

		// assure painter is null
		panel.setBackgroundPainter(null);

		panel.setBackground(Color.BLACK);

		assertThat(panel.getBackgroundPainter()).isNull();
	}

	/**
	 * SwingX #964: ensure setting background color sets painter.
	 */
	@Test
	public void testSetColorOverridesUIResourceBackgroundPainter() {
		JXPanel panel = new JXPanel();

		// assure painter is null
		panel.setBackgroundPainter(new PainterUIResource<JXPanel>(new MattePainter(Color.RED)));

		panel.setBackground(Color.BLACK);

		assertThat(panel.getBackgroundPainter()).isNotNull();
	}

	/**
	 * SwingX #964: ensure setting background color sets painter with a {@code
	 * UIResource} set the background painter with a {@code UIResource} if the
	 * background painter is {@code null} or a {@code UIResource}.
	 */
	@Test
	public void testSetUIResourceColorDoesNotOverrideUIResourceBackgroundPainter() {
		JXPanel panel = new JXPanel();

		Painter myResource = new PainterUIResource<JXPanel>(new MattePainter(Color.BLACK));
		panel.setBackgroundPainter(myResource);

		panel.setBackground(new ColorUIResource(Color.BLACK));

		assertThat(panel.getBackgroundPainter()).isEqualTo(myResource);
	}

	/**
	 * SwingX #964: ensure setting background color sets painter with a {@code
	 * UIResource} set the background painter with a {@code UIResource} if the
	 * background painter is {@code null} or a {@code UIResource}.
	 */
	@Test
	public void testSetUIResourceColorDoesNotOverrideBackgroundPainter() {
		JXPanel panel = new JXPanel();

		Painter painter = new MattePainter(Color.BLACK);
		panel.setBackgroundPainter(painter);

		panel.setBackground(new ColorUIResource(Color.BLACK));

		assertThat(panel.getBackgroundPainter()).isSameAs(painter);
	}

	@Test
	public void testSetAlphaWithLessThanZero() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new JXPanel().setAlpha(Math.nextAfter(0f, Float.NEGATIVE_INFINITY)));
	}

	@Test
	public void testSetAlphaWithGreaterThanOne() {
		assertThrows(IllegalArgumentException.class, () -> new JXPanel().setAlpha(Math.nextUp(1f)));
	}

	@Test
	public void testSetAlphaLessThanOneMakesPanelNonOpaque() {
		JXPanel panel = new JXPanel();
		assumeTrue(panel.isOpaque());

		panel.setAlpha(.99f);
		assertThat(panel.isOpaque()).isEqualTo(false);
	}

	@Test
	public void testRestoreOpacityWhenAlphaSetToOne() {
		JXPanel panel = new JXPanel();
		assumeTrue(panel.isOpaque());

		panel.setAlpha(.99f);
		panel.setAlpha(1f);
		assertThat(panel.isOpaque()).isEqualTo(true);

		panel.setOpaque(false);
		panel.setAlpha(.99f);
		panel.setAlpha(1f);
		assertThat(panel.isOpaque()).isEqualTo(false);
	}

	@Test
	public void testGetEffectiveAlphaWithoutInherit() {
		JXPanel panel = new JXPanel();
		assertThat(panel.getEffectiveAlpha()).isEqualTo(panel.getAlpha());

		panel.setAlpha(.5f);
		assertThat(panel.getEffectiveAlpha()).isEqualTo(panel.getAlpha());
	}

	@Test
	public void testGetEffectiveAlphaWithInheritGetsSmallestAlpha() {
		JXPanel p1 = new JXPanel();
		p1.setAlpha(.1f);
		p1.setInheritAlpha(true);
		JXPanel p2 = new JXPanel();
		p2.setAlpha(.2f);
		p2.setInheritAlpha(true);

		p1.add(p2);
		assertThat(p2.getEffectiveAlpha()).isEqualTo(p1.getAlpha());

		p1.removeAll();
		p2.add(p1);
		assertThat(p1.getEffectiveAlpha()).isEqualTo(p1.getAlpha());
	}
}
