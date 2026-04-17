package org.jdesktop.swingx.action;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jdesktop.test.SerializableSupport.serialize;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.swing.JLabel;
import org.jdesktop.test.EDTRunner;
import org.jdesktop.test.matchers.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(EDTRunner.class)
public class BoundActionTest {
	private BoundAction action;

	@BeforeEach
	public void setUp() {
		action = new BoundAction();
	}

	@Test
	public void testSerialization() {
		BoundAction serialized = serialize(action);
		assertTrue(Matchers.equivalentTo(action).matches(serialized));
	}

	@Test
	public void testSerializationWithCallback() {
		action.registerCallback(new JLabel(), "updateUI");
		BoundAction serialized = serialize(action);

		assertTrue(Matchers.equivalentTo(action).matches(serialized));
		assertThat(serialized.getActionListeners().length).isEqualTo(1);
	}

	@Test
	public void testSerializationForToggleWithCallback() {
		action.setStateAction(true);
		action.registerCallback(new JLabel(), "updateUI");
		BoundAction serialized = serialize(action);

		assertTrue(Matchers.equivalentTo(action).matches(serialized));
		assertThat(serialized.getItemListeners().length).isEqualTo(1);
	}

	@Test
	public void testSerializationWithListener() {
		action.addActionListener(new BoundAction());
		BoundAction serialized = serialize(action);

		assertTrue(Matchers.equivalentTo(action).matches(serialized));
	}
}
