package com.crawler.service.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LinkType {
    DOMAIN_LINK(0),
    EXTERNAL_LINK(1),
    STATIC_LINK(2);

    private int value;
}
