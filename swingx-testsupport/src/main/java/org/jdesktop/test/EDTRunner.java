/**
 * 
 */
package org.jdesktop.test;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.SwingUtilities;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.mockito.invocation.Invocation;
 
/**
 * A test runner for JUnit that ensures that all tests are run on the EDT.
 * 
 * This one is an example of junit 5 documentation.
 */
public class EDTRunner implements InvocationInterceptor {
    @Override
    public void interceptTestMethod(Invocation<Void> invocation,
            ReflectiveInvocationContext<Method> invocationContext,
            ExtensionContext extensionContext) throws Throwable {

        AtomicReference<Throwable> throwable = new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            try {
                invocation.proceed();
            }
            catch (Throwable t) {
                throwable.set(t);
            }
        });
        Throwable t = throwable.get();
        if (t != null) {
            throw t;
        }
    }
}
