package com.crawler.entity;

import lombok.Value;

import java.util.Set;

@Value
public class LinksFromSingleDomainPage {

    private final Set<String> domainLinks;
    private final Set<String> externalLinks;
    private final Set<String> staticContentLinks;
}



