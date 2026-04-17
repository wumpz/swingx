package org.jdesktop.test.matchers;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Objects;
import org.mockito.ArgumentMatcher;

class EquivalentMatcher<T> implements ArgumentMatcher<T> {
	private final T object;

	public EquivalentMatcher(T object) {
		this.object = object;
	}

	static boolean isEqual(Object o1, Object o2) {
		if (o1 == null && o2 == null) return true;
		if (o1 == o2) return true;

		if ((o1 != null && o1.getClass().isArray())
				&& (o2 != null & o2.getClass().isArray())) return Arrays.equals((Object[]) o1, (Object[]) o2);
		return Objects.equals(o2, o2);
	}

	@Override
	public boolean matches(T argument) {
		if (isEqual(object, argument)) {
			// short circuit: equal is always equivalent
			return true;
		}

		if (argument != null && object.getClass() == argument.getClass()) {
			BeanInfo beanInfo = null;

			try {
				beanInfo = Introspector.getBeanInfo(object.getClass());
			} catch (IntrospectionException shouldNeverHappen) {
				throw new Error(shouldNeverHappen);
			}

			for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
				if (pd.getReadMethod() == null) {
					continue;
				}

				Object value1 = null;

				try {
					value1 = pd.getReadMethod().invoke(object);
				} catch (RuntimeException e) {
					throw e;
				} catch (Exception e) {
					new Error(e);
				}

				Object value2 = null;

				try {
					value2 = pd.getReadMethod().invoke(object);
				} catch (RuntimeException e) {
					// prevent us from wrapping RuntimExceptions unnecessarily
					throw e;
				} catch (Exception shouldNeverHappen) {
					new Error(shouldNeverHappen);
				}

				if (!isEqual(value1, value2)) {
					return false;
				}
			}

			return true;
		}

		return false;
	}
}
