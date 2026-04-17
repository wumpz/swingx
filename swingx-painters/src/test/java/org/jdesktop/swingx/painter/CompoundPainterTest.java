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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

/**
 * @author rbair
 */
@SuppressWarnings({"nls", "unchecked", "rawtypes"})
public class CompoundPainterTest extends AbstractPainterTest {
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected CompoundPainter createTestingPainter() {
		return new CompoundPainter();
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
		// TODO why does CompoundPainter start dirty?
		assertThat(p.isDirty()).isEqualTo(true);
		assertThat(p.isInPaintContext()).isEqualTo(false);
		assertThat(p.isVisible()).isEqualTo(true);
		assertThat(p.shouldUseCache()).isEqualTo(false);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Overridden for CompoundPainter defaults.
	 */
	@Test
	@Override
	public void testDefaults() {
		// TODO replace with super.testDefaults() when corrected
		copyOfSuper_testDefaultsWithCorrectedValues();
		//        super.testDefaults();

		CompoundPainter cp = (CompoundPainter) p;
		assertThat(cp.getPainters()).containsExactly(new Painter[0]);
		assertThat(cp.getTransform()).isNull();
		assertThat(cp.isCheckingDirtyChildPainters()).isEqualTo(true);
		assertThat(cp.isClipPreserved()).isEqualTo(false);
	}

	/**
	 * Issue #497-swingx: setPainters can't cope with null.
	 *
	 */
	@Test
	public void testSetNullPainters() {
		CompoundPainter<Object> painter = new CompoundPainter<>();
		// changed to cast to Painter, since uncasted it is equivalent to
		// Painter[], which is checked in the next test
		painter.setPainters((Painter<?>) null);
	}

	/**
	 * Issue #497-swingx: setPainters can't cope with null.
	 *
	 */
	@Test
	public void testSetEmptyPainters() {
		CompoundPainter<Object> painter = new CompoundPainter<>();
		// okay
		painter.setPainters();
		// fails
		painter.setPainters((Painter[]) null);
	}

	@Test
	public void testSetttingOnePainterDoesNotEnableCache() {
		((CompoundPainter) p).setPainters(mock(Painter.class));

		assertThat(p.shouldUseCache()).isEqualTo(false);
	}

	@Test
	@Disabled("not sure this is the right thing to do")
	public void testSettingMoreThanOnePainterEnablesCache() {
		((CompoundPainter) p).setPainters(mock(Painter.class), mock(Painter.class));

		assertThat(p.shouldUseCache()).isEqualTo(true);
	}

	/**
	 * Issue #1218-swingx: must fire property change if contained painter
	 *    changed.
	 */
	public void testDirtyNotification() {
		AbstractPainter<Object> child = spy(new DummyPainter());
		((CompoundPainter<?>) p).setPainters(child);

		assertThat(p.isDirty()).isEqualTo(true);
		verify(child, never()).setDirty(true);

		p.paint(g, null, 10, 10);

		assertThat(p.isDirty()).isEqualTo(false);

		PropertyChangeListener pcl = mock(PropertyChangeListener.class);
		p.addPropertyChangeListener(pcl);

		child.setDirty(true);

		assertThat(p.isDirty()).isEqualTo(true);

		ArgumentCaptor<PropertyChangeEvent> captor = ArgumentCaptor.forClass(PropertyChangeEvent.class);
		verify(pcl).propertyChange(captor.capture());

		assertThat(captor.getValue().getSource()).isSameAs(p);
		assertThat(captor.getValue().getPropertyName()).isEqualTo("dirty");
		assertThat(captor.getAllValues().size()).isEqualTo(1);
	}
}
