package org.jdesktop.swingx.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;

public class GraphicsUtilitiesTest {
	@Test
	public void testClear() {
		BufferedImage img = GraphicsUtilities.createCompatibleImage(1, 1);
		GraphicsUtilities.clear(img);
		assertThat(GraphicsUtilities.getPixels(img, 0, 0, 1, 1, null)[0]).isEqualTo(0);
	}
}
