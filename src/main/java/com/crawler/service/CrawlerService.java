package com.crawler.service;

import com.crawler.controller.response.LinksFromAllDomainPages;
import com.crawler.exception.ErrorMessage;
import com.crawler.exception.IncorrectUrlException;
import com.crawler.service.helpers.LinksExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlerService {

    private final LinksExtractor linksExtractor;

    public LinksFromAllDomainPages getLinksFromGivenDomain(String url) {
        try {
            URL givenURL = new URL(url);
            String pageDomainName = givenURL.getHost();

            log.debug("Extracting links from provided page: " + url);

            return linksExtractor.crawlDomain(url, pageDomainName);
        } catch (MalformedURLException malformedUrlException) {
            log.error(String.format(ErrorMessage.ERROR_INVALID_URL.getErrorMessage(), url) +
                    String.format(ErrorMessage.ERROR_MESSAGE.getErrorMessage(), malformedUrlException.getMessage()));
            log.debug(String.format(ErrorMessage.ERROR_INVALID_URL.getErrorMessage(), url) +
                    String.format(ErrorMessage.ERROR_DETAILS.getErrorMessage(), Arrays.toString(malformedUrlException.getStackTrace())));
            throw new IncorrectUrlException(String.format(ErrorMessage.ERROR_INVALID_URL.getErrorMessage(), url));
        }
    }
}
