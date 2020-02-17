package com.crawler.service.helpers;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.jsoup.HttpStatusException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PageReceiverTest {

    @Mock
    Appender appender;

    @Captor
    ArgumentCaptor<ILoggingEvent> captor;

    @MockBean
    private JSoupChrome jSoupChrome;

    private PageReceiver pageContentReceiver;

    @BeforeEach
    public void setUp() {
        pageContentReceiver = new PageReceiver(jSoupChrome);

        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
                .getLogger(Logger.ROOT_LOGGER_NAME);
        when(appender.getName()).thenReturn("MOCK");
        when(appender.isStarted()).thenReturn(true);
        logger.addAppender(appender);
    }

    @Test
    void getPageContent_givenIncorrectUrl_thenLogError() throws IOException {
        //GIVEN
        String expectedLoggerErrorMessage = "No page was found for given url: TEST. Please check the passed URL. Error message: Invalid Url passed";

        HttpStatusException httpStatusException = new HttpStatusException("Invalid Url passed", 404, "test");
        when(jSoupChrome.connectToPageUsingChrome(any())).thenThrow(httpStatusException);

        //WHEN
        pageContentReceiver.getPageContent("TEST");

        //THEN
        verify(appender, times(1)).doAppend(captor.capture());

        List<ILoggingEvent> allValues = captor.getAllValues();
        allValues.forEach(iLoggingEvent -> {
            if (iLoggingEvent.getLevel().equals(Level.ERROR))
                assertThat(iLoggingEvent.getFormattedMessage()).isEqualTo(expectedLoggerErrorMessage);
        });
    }

}
