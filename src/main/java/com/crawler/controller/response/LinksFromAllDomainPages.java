package com.crawler.controller.response;


import com.crawler.entity.LinksFromSingleDomainPage;
import lombok.Value;

import java.util.Map;

@Value
public class LinksFromAllDomainPages {

    private final Map<String, LinksFromSingleDomainPage> linksFoundOnEachDomainPage;
}
