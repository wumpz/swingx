package org.jdesktop.test.matchers;

import java.beans.PropertyChangeEvent;
import org.mockito.ArgumentMatcher;

@SuppressWarnings("nls")
class PropertyChangeEventMatcher implements ArgumentMatcher<PropertyChangeEvent> {
	private final String propertyName;
	private final Object oldValue;
	private final Object newValue;

	public PropertyChangeEventMatcher(String propertyName, Object oldValue, Object newValue) {
		this.propertyName = propertyName;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	@Override
	public boolean matches(PropertyChangeEvent pce) {
		boolean result = propertyName.equals(pce.getPropertyName());
		result &=
				oldValue == null || pce.getOldValue() == null || EquivalentMatcher.isEqual(oldValue, pce.getOldValue());
		result &=
				newValue == null || pce.getNewValue() == null || EquivalentMatcher.isEqual(newValue, pce.getNewValue());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return " " + propertyName;
	}
}
