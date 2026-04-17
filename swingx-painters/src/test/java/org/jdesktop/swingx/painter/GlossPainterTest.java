/*
 * $Id$
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
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
package org.jdesktop.swingx.painter;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;
import org.junit.jupiter.api.Test;

/**
 * Test for GlossPainter.
 */
public class GlossPainterTest extends AbstractPainterTest {
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected GlossPainter createTestingPainter() {
		return new GlossPainter();
	}

	/**
	 * TODO remove when the compound painter does not start dirty
	 */
	private void copyOfSuper_testDefaultsWithCorrectedValues() {
		assertThat(p.getFilters().length).isEqualTo(0);
		assertThat(p.getInterpolation()).isEqualTo(AbstractPainter.Interpolation.NearestNeighbor);
		assertThat(p.isAntialiasing()).isEqualTo(true);
		assertThat(p.isCacheable()).isEqualTo(false);
		assertThat(p.isCacheCleared()).isEqualTo(true);
		// TODO this is because the constructor calls the setters
		assertThat(p.isDirty()).isEqualTo(true);
		assertThat(p.isInPaintContext()).isEqualTo(false);
		assertThat(p.isVisible()).isEqualTo(true);
		assertThat(p.shouldUseCache()).isEqualTo(false);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Overridden for GlossPainter defaults.
	 */
	@Test
	@Override
	public void testDefaults() {
		// TODO replace with super.testDefaults() when corrected
		copyOfSuper_testDefaultsWithCorrectedValues();
		//        super.testDefaults();

		GlossPainter gp = (GlossPainter) p;
		assertThat(gp.getPaint()).isEqualTo(new Color(1f, 1f, 1f, .2f));
		assertThat(gp.getPosition()).isEqualTo(GlossPainter.GlossPosition.TOP);
	}
}
