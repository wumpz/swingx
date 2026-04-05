package org.jdesktop.swingx.graphics;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import java.awt.Composite;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class FilterCompositeTest {
    @Test
    public void checkThrowOnNullComposite() {
        assertThrows(NullPointerException.class, () -> {
            new FilterComposite(null);
        });
    }
    
    @Test
    public void checkUnaryConstructorHasNoFilter() {
        Composite composite = mock(Composite.class);
        FilterComposite fc = new FilterComposite(composite);
        assertThat(fc.getFilter(), is(nullValue()));
    }
}
