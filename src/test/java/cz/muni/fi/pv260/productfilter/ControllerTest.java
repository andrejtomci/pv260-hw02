package cz.muni.fi.pv260.productfilter;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;


import static org.mockito.Mockito.*;

public class ControllerTest {
    @Test
    public void testSelectProductFilter() throws ObtainFailedException {
        Output mockedOut = mock(Output.class);
        Input mockedIn = mock(Input.class);
        Product mockedProduct1 = mock(Product.class);
        Product mockedProduct2 = mock(Product.class);
        Filter mockedFilter = mock(Filter.class);

        when(mockedIn.obtainProducts()).thenReturn(Arrays.asList(mockedProduct1, mockedProduct2));

        when(mockedFilter.passes(mockedProduct1)).thenReturn(true);
        when(mockedFilter.passes(mockedProduct2)).thenReturn(false);

        Controller controller = new Controller(mockedIn, mockedOut, mock(Logger.class));

        controller.select(mockedFilter);
        verify(mockedOut).postSelectedProducts(Mockito.any()); // so ist called only once
        verify(mockedOut).postSelectedProducts(Arrays.asList(mockedProduct1)); // and with the right arg
    }

    @Test
    public void testLoggerOnSuccess() throws ObtainFailedException {
        Input mockedIn = mock(Input.class);
        Logger mockedLogger = mock(Logger.class);
        Product mockedProduct1 = mock(Product.class);
        Product mockedProduct2 = mock(Product.class);
        Filter mockedFilter = mock(Filter.class);

        when(mockedIn.obtainProducts()).thenReturn(Arrays.asList(mockedProduct1, mockedProduct2));

        when(mockedFilter.passes(mockedProduct1)).thenReturn(true);
        when(mockedFilter.passes(mockedProduct2)).thenReturn(false);

        Controller controller = new Controller(mockedIn, mock(Output.class), mockedLogger);

        controller.select(mockedFilter);
        verify(mockedLogger).setLevel("INFO");
        verify(mockedLogger).log(Controller.TAG_CONTROLLER, "Successfully selected 1 out of 2 available products." );
    }

    @Test
    public void testLoggerException() throws ObtainFailedException {
        Input mockedIn = mock(Input.class);
        Logger mockedLogger = mock(Logger.class);
        ObtainFailedException mockedException = mock(ObtainFailedException.class);
        Filter mockedFilter = mock(Filter.class);

        when(mockedException.toString()).thenReturn("Test exception string");
        when(mockedIn.obtainProducts()).thenThrow(mockedException);

        Controller controller = new Controller(mockedIn, mock(Output.class), mockedLogger);

        controller.select(mockedFilter);
        verify(mockedLogger).setLevel("ERROR");
        verify(mockedLogger).log(Controller.TAG_CONTROLLER, "Filter procedure failed with exception: " + mockedException.toString());
    }

    @Test
    public void testExceptionOutputNotCalled() throws ObtainFailedException {
        Input mockedIn = mock(Input.class);
        Output mockedOut = mock(Output.class);
        Logger mockedLogger = mock(Logger.class);
        ObtainFailedException mockedException = mock(ObtainFailedException.class);
        Filter mockedFilter = mock(Filter.class);

        when(mockedException.toString()).thenReturn("Test exception string");
        when(mockedIn.obtainProducts()).thenThrow(mockedException);

        Controller controller = new Controller(mockedIn, mockedOut, mockedLogger);

        controller.select(mockedFilter);
        verify(mockedOut, never()).postSelectedProducts(Mockito.any());
    }
}


