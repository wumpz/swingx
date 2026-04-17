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
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.test.EDTRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Unit test for <code>JXButton</code>.
 * <p>
 *
 * All test methods in this class are expected to pass.
 *
 * @author rah003
 * @author Karl Schaefer
 */
@SuppressWarnings("nls")
@ExtendWith(EDTRunner.class)
public class JXButtonTest {
	private JXButton button;

	@BeforeEach
	public void setUp() {
		assumeTrue(GraphicsEnvironment.isHeadless());
		button = new JXButton();
	}

	/**
	 *
	 */
	@Test
	public void testDefaultIsNoPainters() {
		assertThat(button.getForegroundPainter()).isNull();
		assertThat(button.getBackgroundPainter()).isNull();
	}

	/**
	 * SWINGX-1449: ensures that the font is not reset when the background is changed
	 */
	@Test
	public void ensureFontIsMaintainedAfterBackgroundSet() {
		Font font = Font.decode("Arial-BOLDITALIC-14");
		assumeFalse(button.getFont().equals(font));

		button.setFont(font);
		button.setBackground(Color.RED);

		assertThat(button.getFont()).isEqualTo(font);
	}

	/**
	 * SWINGX-1449: ensures that the font is not reset when the background painter is changed
	 */
	@Test
	public void ensureFontIsMaintainedAfterBackgroundPainterSet() {
		Font font = Font.decode("Arial-BOLDITALIC-14");
		assumeFalse(button.getFont().equals(font));

		button.setFont(font);
		button.setBackgroundPainter(new MattePainter(Color.RED));

		assertThat(button.getFont()).isEqualTo(font);
	}
}
