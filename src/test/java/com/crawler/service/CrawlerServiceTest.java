package com.crawler.service;

import com.crawler.controller.response.LinksFromAllDomainPages;
import com.crawler.entity.LinksFromSingleDomainPage;
import com.crawler.exception.IncorrectUrlException;
import com.crawler.service.helpers.LinksExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CrawlerServiceTest {

    @Mock
    private LinksExtractor linksExtractor;

    private CrawlerService crawlerService;

    @BeforeEach
    void setUp() {
        crawlerService = new CrawlerService(linksExtractor);
    }

    @Test
    void getLinksFromGivenDomain_GivenCorrectData_ThenPass() throws MalformedURLException {
        //GIVEN
        LinksFromAllDomainPages foundLinks;
        LinksFromSingleDomainPage singlePage = new LinksFromSingleDomainPage(new HashSet<>(), new HashSet<>(), new HashSet<>());
        Map<String, LinksFromSingleDomainPage> extractedPages = Map.of("test", singlePage);
        foundLinks = new LinksFromAllDomainPages(extractedPages);
        LinksFromAllDomainPages result;
        when(linksExtractor.crawlDomain(any(), any())).thenReturn(foundLinks);

        //WHEN
        result = crawlerService.getLinksFromGivenDomain("http://test.com/");

        //THEN
        assertThat(result.getLinksFoundOnEachDomainPage()).containsKeys("test");
    }

    @Test
    void getLinksFromGivenDomain_GivenIncorrectUrl_ThenThrowException() {
        //GIVEN
        String incorrectUrl = "xxx";

        //WHEN - THEN
        assertThrows(IncorrectUrlException.class, () -> crawlerService.getLinksFromGivenDomain(incorrectUrl));
    }
}
