package com.crawler.controller;

import com.crawler.controller.request.RequestUrlPath;
import com.crawler.controller.response.LinksFromAllDomainPages;
import com.crawler.service.CrawlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/crawler")
@RequiredArgsConstructor
public class CrawlerController {

    private final CrawlerService crawlerService;

    @PostMapping("/pagesContent")
    public LinksFromAllDomainPages getPages(@RequestBody RequestUrlPath requestUrlPath) {
        log.debug("Crawl provided website: " + requestUrlPath.getPageUrl());
        LinksFromAllDomainPages extractedLinks = crawlerService.getLinksFromGivenDomain(requestUrlPath.getPageUrl());
        log.debug("Finished successfully");

        return extractedLinks;
    }
}
