package com.crawler.service.helpers;

import com.crawler.exception.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class PageReceiver {

    private JSoupChrome jSoupChrome;

    public Optional<Document> getPageContent(String inspectedUrl) {
        Optional<Document> doc = Optional.empty();
        try {
            log.debug("Running jsoup for: " + inspectedUrl);
            Connection receivedConnection = jSoupChrome.connectToPageUsingChrome(inspectedUrl);
            doc = Optional.of(receivedConnection.get());
        } catch (IOException ioException) {
            log.error(String.format(ErrorMessage.ERROR_NO_PAGE.getErrorMessage(), inspectedUrl) +
                    String.format(ErrorMessage.ERROR_MESSAGE.getErrorMessage(), ioException.getMessage()));
            log.debug(String.format(ErrorMessage.ERROR_NO_PAGE.getErrorMessage(), inspectedUrl) +
                    String.format(ErrorMessage.ERROR_DETAILS.getErrorMessage(), Arrays.toString(ioException.getStackTrace())));
        }
        return doc;
    }
}
