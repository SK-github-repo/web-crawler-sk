package com.crawler.service.helpers;

import com.crawler.controller.response.LinksFromAllDomainPages;
import com.crawler.entity.LinksFromSingleDomainPage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static com.crawler.exception.ErrorMessage.*;

@Slf4j
@Component
@AllArgsConstructor
public class LinksExtractor {

    private static final String STATIC_ELEMENT_ATTRIBUTE = "[src]";
    private static final String EXTRACT_STATIC_ELEMENT = "abs:src";
    private static final String LINK_ATTRIBUTE = "a[href]";
    private static final String EXTRACT_LINK = "abs:href";

    private static final String REGEX_FOR_STATIC_CONTENT = "(.*)*.+\\.(png|jpg|gif|bmp|jpeg|PNG|JPG|GIF|BMP|JPEG)$";

    private PageReceiver pageContentReceiver;

    public LinksFromAllDomainPages crawlDomain(String url, String pageDomain) {
        Map<String, LinksFromSingleDomainPage> extractedLinksFromPages = new HashMap<>();
        Set<String> visitedPages = new HashSet<>();
        DomainPageDetails domainPageDetails = new DomainPageDetails(url, pageDomain);

        collectLinksForGivenDomainPage(domainPageDetails, visitedPages, extractedLinksFromPages);

        return new LinksFromAllDomainPages(extractedLinksFromPages);
    }

    private void collectLinksForGivenDomainPage(DomainPageDetails domainPageDetails, Set<String> visitedPages, Map<String, LinksFromSingleDomainPage> extractedLinksFromPages) {
        LinksFromSingleDomainPage singlePage;
        List<Set<String>> linksFoundOnPage = prepareLinkCollectorForFoundLinks();

        log.info("Get all links from given page: " + domainPageDetails.getUrl());
        Optional<Document> doc = pageContentReceiver.getPageContent(domainPageDetails.getUrl());
        visitedPages.add(domainPageDetails.getUrl());

        if (doc.isPresent()) {
            findLinksAndAddToProperList(domainPageDetails.getPageDomain(), doc.get(), linksFoundOnPage);
            findStaticContentAndAddToStaticList(doc.get(), linksFoundOnPage);

            singlePage = assignFoundLinksToSinglePage(linksFoundOnPage);
            extractedLinksFromPages.put(domainPageDetails.getUrl(), singlePage);

            crawlRecursivelyAllFoundDomainPages(domainPageDetails, visitedPages, extractedLinksFromPages, linksFoundOnPage);
        }
    }

    private List<Set<String>> prepareLinkCollectorForFoundLinks() {
        List<Set<String>> allLinksFromPage = new ArrayList<>();
        Set<String> domainLinks = new HashSet<>();
        Set<String> externalLinks = new HashSet<>();
        Set<String> staticContentLinks = new HashSet<>();

        allLinksFromPage.add(domainLinks);
        allLinksFromPage.add(externalLinks);
        allLinksFromPage.add(staticContentLinks);

        return allLinksFromPage;
    }

    private void findLinksAndAddToProperList(String pageDomain, Document doc, List<Set<String>> singlePageLinks) {
        Elements linksFromPage = getAllLinksFromPage(doc);
        linksFromPage.forEach(link -> addLinkToProperList(pageDomain, link, singlePageLinks));
    }

    private Elements getAllLinksFromPage(Document doc) {
        return doc.select(LINK_ATTRIBUTE);
    }

    private void addLinkToProperList(String pageDomain, Element link, List<Set<String>> singlePageLinks) {
        String extractedLink = link.attr(EXTRACT_LINK);
        try {
            if (!extractedLink.isEmpty()) {
                URL receivedUrl = new URL(extractedLink);
                if (receivedUrl.getHost().contains(pageDomain)) {
                    addLinkToDomainOrStatic(extractedLink, receivedUrl, singlePageLinks);
                } else {
                    singlePageLinks.get(LinkType.EXTERNAL_LINK.getValue()).add(extractedLink);
                    log.debug("Added link to external link list: " + extractedLink);
                }
            }
        } catch (MalformedURLException malformedUrlException) {
            log.error(String.format(ERROR_IN_PAGE_LINKS.getErrorMessage(), extractedLink) +
                    String.format(ERROR_MESSAGE.getErrorMessage(), malformedUrlException.getMessage()));
            log.debug(String.format(ERROR_IN_PAGE_LINKS.getErrorMessage(), extractedLink) +
                    String.format(ERROR_DETAILS.getErrorMessage(), Arrays.toString(malformedUrlException.getStackTrace())));
        }
    }

    private void addLinkToDomainOrStatic(String extractedLink, URL receivedUrl, List<Set<String>> singlePageLinks) {
        if (isDomainStaticContent(receivedUrl)) {
            singlePageLinks.get(LinkType.STATIC_LINK.getValue()).add(extractedLink);
            log.debug("Added link to static link list instead of domestic: " + extractedLink);
        } else {
            singlePageLinks.get(LinkType.DOMAIN_LINK.getValue()).add(extractedLink);
            log.debug("Added link to domain link list: " + extractedLink);
        }
    }

    private boolean isDomainStaticContent(URL receivedUrl) {
        return receivedUrl.getPath().matches(REGEX_FOR_STATIC_CONTENT);
    }

    private void findStaticContentAndAddToStaticList(Document doc, List<Set<String>> singlePageLinks) {
        Elements allStaticContents = getStaticContent(doc);
        allStaticContents.forEach(staticElement -> {
            String extractedLink = staticElement.attr(EXTRACT_STATIC_ELEMENT);
            singlePageLinks.get(LinkType.STATIC_LINK.getValue()).add(extractedLink);
            log.debug("Added link to static link list: " + extractedLink);
        });
    }

    private Elements getStaticContent(Document doc) {
        return doc.select(STATIC_ELEMENT_ATTRIBUTE);
    }

    private LinksFromSingleDomainPage assignFoundLinksToSinglePage(List<Set<String>> linksFoundOnPage) {
        return new LinksFromSingleDomainPage(linksFoundOnPage.get(LinkType.DOMAIN_LINK.getValue()),
                linksFoundOnPage.get(LinkType.EXTERNAL_LINK.getValue()),
                linksFoundOnPage.get(LinkType.STATIC_LINK.getValue()));
    }

    private void crawlRecursivelyAllFoundDomainPages(DomainPageDetails domainPageDetails, Set<String> visitedPages, Map<String, LinksFromSingleDomainPage> extractedLinksFromPages, List<Set<String>> linksFoundOnPage) {
        linksFoundOnPage.get(LinkType.DOMAIN_LINK.getValue()).forEach(domainSubPageUrl -> {
            if (!visitedPages.contains(domainSubPageUrl)) {
                domainPageDetails.setUrl(domainSubPageUrl);
                collectLinksForGivenDomainPage(domainPageDetails, visitedPages, extractedLinksFromPages);
            }
        });
    }
}
