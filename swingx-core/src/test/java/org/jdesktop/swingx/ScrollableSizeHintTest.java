package org.jdesktop.swingx;

import static org.junit.jupiter.api.Assertions.fail;

import javax.swing.SwingConstants;
import org.junit.jupiter.api.Test;

@SuppressWarnings("nls")
public class ScrollableSizeHintTest {
	/**
	 * Test contract - NPE on null component
	 */
	@Test
	public void checkDocumentedNPE() {
		for (ScrollableSizeHint behaviour : ScrollableSizeHint.values()) {
			try {
				behaviour.getTracksParentSize(null, SwingConstants.VERTICAL);
				fail("null component must throw NPE, didn't on " + behaviour);
			} catch (NullPointerException e) {
				// expected
			}
		}
	}
}
