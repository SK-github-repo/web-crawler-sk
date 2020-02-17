package com.crawler.service.helpers;

import com.crawler.controller.response.LinksFromAllDomainPages;
import com.crawler.entity.LinksFromSingleDomainPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LinksExtractorTest {

    @MockBean
    private PageReceiver pageContentReceiver;

    private LinksExtractor linksExtractor;

    @BeforeEach
    public void setUp() {
        linksExtractor = new LinksExtractor(pageContentReceiver);
    }

    @Test
    void crawlDomain_givenCorrectUrl_thenReturnDividedLinks() throws IOException {
        //GIVEN
        String url = "http://www.mechanikasamochodowa.pl";
        String pageDomain = "www.mechanikasamochodowa.pl";
        File html = new File("sampleWebsite/exampleWebsite.html");
        Document doc = Jsoup.parse(html, "UTF-8", url);

        when(pageContentReceiver.getPageContent(url)).thenReturn(Optional.of(doc));

        //WHEN
        LinksFromAllDomainPages result = linksExtractor.crawlDomain(url, pageDomain);

        LinksFromSingleDomainPage linksFromSingleDomainPage = new LinksFromSingleDomainPage(
                result.getLinksFoundOnEachDomainPage().get(url).getDomainLinks(),
                result.getLinksFoundOnEachDomainPage().get(url).getExternalLinks(),
                result.getLinksFoundOnEachDomainPage().get(url).getStaticContentLinks()
        );

        //THEN

        assertThat(linksFromSingleDomainPage.getDomainLinks()).hasSize(13);
        assertThat(linksFromSingleDomainPage.getExternalLinks()).hasSize(3);
        assertThat(linksFromSingleDomainPage.getStaticContentLinks()).hasSize(39);
    }
}
