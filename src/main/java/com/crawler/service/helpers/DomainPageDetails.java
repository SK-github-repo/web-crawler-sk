package com.crawler.service.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class DomainPageDetails {

    @Setter
    private String url;
    private final String pageDomain;
}
