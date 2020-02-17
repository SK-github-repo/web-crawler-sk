package com.crawler.controller;

import com.crawler.controller.request.RequestUrlPath;
import com.crawler.controller.response.LinksFromAllDomainPages;
import com.crawler.entity.LinksFromSingleDomainPage;
import com.crawler.service.CrawlerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CrawlerControllerTest {

    private static final String BASE_URL = "/crawler";

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CrawlerService crawlerService;

    private CrawlerController crawlerController;

    @BeforeEach
    void setUp() {
        crawlerController = new CrawlerController(crawlerService);
    }

    @Test
    void getPages_givenCorrectUrl_thenReturnExtractedPages() throws Exception {
        //GIVEN
        RequestUrlPath requestUrlPath = new RequestUrlPath("http://www.mechanik.pl/");
        LinksFromAllDomainPages extractedLinks;
        LinksFromSingleDomainPage singlePage = new LinksFromSingleDomainPage(new HashSet<>(), new HashSet<>(), new HashSet<>());
        Map<String, LinksFromSingleDomainPage> extractedPages = Map.of("test", singlePage);
        extractedLinks= new LinksFromAllDomainPages(extractedPages);
        LinksFromAllDomainPages result;

        when(crawlerService.getLinksFromGivenDomain(any())).thenReturn(extractedLinks);

        //WHEN
        result = crawlerController.getPages(requestUrlPath);

        //THEN
        assertThat(result.getLinksFoundOnEachDomainPage()).containsKeys("test");
    }

    @Test
    void getPages_givenCorrectUrl_thenStatusOK() throws Exception {
        //GIVEN
        String url = BASE_URL + "/pagesContent";
        RequestUrlPath requestUlrPath = new RequestUrlPath("http://www.mechanik.pl/");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(requestUlrPath.getPageUrl());

        //WHEN-THEN
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void getPages_givenIncorrectUrl_thenResponseNotFound() throws Exception {
        //GIVEN
        String url = BASE_URL + "/pagesContent";
        RequestUrlPath requestUlrPath = new RequestUrlPath("xxx");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(requestUlrPath.getPageUrl());

        //WHEN-THEN
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPages_givenXmlInput_thenResponseBadRequest() throws Exception {
        //GIVEN
        String url = BASE_URL + "/pagesContent";
        RequestUrlPath requestUlrPath = new RequestUrlPath("xxx");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(requestUlrPath);

        //WHEN-THEN
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_ATOM_XML_VALUE)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }
}
