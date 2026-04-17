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

import java.awt.Insets;
import org.jdesktop.swingx.painter.effects.AreaEffect;
import org.junit.jupiter.api.Test;

/**
 * Test for ImagePainter.
 */
@SuppressWarnings({"rawtypes"})
public class ImagePainterTest extends AbstractAreaPainterTest {
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ImagePainter createTestingPainter() {
		return new ImagePainter();
	}

	@Test
	@Override
	public void testDefaults() {
		assertThat(p.getFilters().length).isEqualTo(0);
		assertThat(p.getInterpolation()).isEqualTo(AbstractPainter.Interpolation.NearestNeighbor);
		assertThat(p.isAntialiasing()).isEqualTo(true);
		assertThat(p.isCacheable()).isEqualTo(true);
		assertThat(p.isCacheCleared()).isEqualTo(true);
		assertThat(p.isDirty()).isEqualTo(false);
		assertThat(p.isInPaintContext()).isEqualTo(false);
		assertThat(p.isVisible()).isEqualTo(true);
		assertThat(p.shouldUseCache()).isEqualTo(p.isCacheable());

		AbstractLayoutPainter alp = (AbstractLayoutPainter) p;
		assertThat(alp.getHorizontalAlignment()).isEqualTo(AbstractLayoutPainter.HorizontalAlignment.CENTER);
		assertThat(alp.getInsets()).isEqualTo(new Insets(0, 0, 0, 0));
		assertThat(alp.getVerticalAlignment()).isEqualTo(AbstractLayoutPainter.VerticalAlignment.CENTER);
		assertThat(alp.isFillHorizontal()).isEqualTo(false);
		assertThat(alp.isFillVertical()).isEqualTo(false);

		AbstractAreaPainter aap = (AbstractAreaPainter) p;
		assertThat(aap.getAreaEffects()).containsExactly(new AreaEffect[0]);
		assertThat(aap.getBorderPaint()).isNull();
		assertThat(aap.getBorderWidth()).isEqualTo(0f);
		assertThat(aap.getFillPaint()).isNull();
		assertThat(aap.getStyle()).isEqualTo(AbstractAreaPainter.Style.BOTH);

		ImagePainter ip = (ImagePainter) p;
		assertThat(ip.getImage()).isNull();
		assertThat(ip.getImageScale()).isEqualTo(1d);
		assertThat(ip.getScaleType()).isEqualTo(ImagePainter.ScaleType.InsideFit);
		assertThat(ip.isHorizontalRepeat()).isEqualTo(false);
		assertThat(ip.isScaleToFit()).isEqualTo(false);
		assertThat(ip.isVerticalRepeat()).isEqualTo(false);
	}
}
