package org.jdesktop.test.matchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jdesktop.test.SerializableSupport.serialize;
import static org.jdesktop.test.matchers.Matchers.equivalentTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.swing.JButton;
import org.jdesktop.test.EDTRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(EDTRunner.class)
public class EquivalentMatcherTest {
	@Test
	public void ensureEqualIsEquivalent() {
		assertThat(1).isEqualTo(1);
		assertTrue(equivalentTo(1).matches(1));
	}

	@Test
	public void testUnequalBaseObjects() {
		JButton button1 = new JButton();
		JButton button2 = new JButton();

		assertThat(button1).isNotEqualTo(button2);
		assertTrue(equivalentTo(button2).matches(button1));
	}

	@Test
	public void testSerializedObjects() {
		JButton button1 = new JButton();
		JButton button2 = serialize(button1);

		assertThat(button1).isNotEqualTo(button2);
		assertTrue(equivalentTo(button2).matches(button1));
	}
}
