package com.crawler.service.helpers;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JSoupChrome {
    public Connection connectToPageUsingChrome(String inspectedUrl) throws HttpStatusException {
        log.debug("Get jsoup connection to page: " + inspectedUrl);
        return Jsoup.connect(inspectedUrl).userAgent("Chrome");
    }
}
