package cz.muni.fi.pv260.productfilter;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


public class AtLeastNOfFilterTest {
    @Test
    public void testConstructorThrowsFilterNeverSucceeds() {
        Filter filter = mock(Filter.class);
        assertThrows(FilterNeverSucceeds.class, () -> new AtLeastNOfFilter<Integer>(2, filter));
    }

    @Test
    public void testConstructorThrowsIllegalArgumentExceptionZeroN() {
        Filter filter = mock(Filter.class);
        assertThrows(IllegalArgumentException.class, () -> new AtLeastNOfFilter<Integer>(0, filter));
    }

    @Test
    public void testConstructorThrowsIllegalArgumentExceptionNegativeN() {
        Filter filter = mock(Filter.class);

        assertThrows(IllegalArgumentException.class, () -> new AtLeastNOfFilter<Integer>(-5, filter));
    }

    @Test
    public void testPassesEqualN() {
        Filter filter = mock(Filter.class);
        Filter filter2 = mock(Filter.class);
        when(filter.passes(1)).thenReturn(true);
        when(filter2.passes(1)).thenReturn(true);

        Filter testedFilter = new AtLeastNOfFilter(2, filter, filter2);
        assertTrue(testedFilter.passes(1));
    }

    @Test
    public void testPassesMoreThanN() {
        Filter filter = mock(Filter.class);
        Filter filter2 = mock(Filter.class);
        Filter filter3 = mock(Filter.class);
        when(filter.passes(1)).thenReturn(true);
        when(filter2.passes(1)).thenReturn(true);
        when(filter3.passes(1)).thenReturn(true);

        Filter testedFilter = new AtLeastNOfFilter(2, filter, filter2, filter3);
        assertTrue(testedFilter.passes(1));
    }

    @Test
    public void testFailsLessThanN() {
        Filter filter = mock(Filter.class);
        Filter filter2 = mock(Filter.class);
        Filter filter3 = mock(Filter.class);
        when(filter.passes(1)).thenReturn(true);
        when(filter2.passes(1)).thenReturn(false);
        when(filter3.passes(1)).thenReturn(false);

        Filter testedFilter = new AtLeastNOfFilter(2, filter, filter2, filter3);
        assertFalse(testedFilter.passes(1));
    }

}