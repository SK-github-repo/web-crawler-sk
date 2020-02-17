package com.crawler.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum ErrorMessage {
    ERROR_INVALID_URL("Passed URL is incorrect: %s. For more detail please check log files. "),
    ERROR_NO_PAGE("No page was found for given url: %s. Please check the passed URL. "),
    ERROR_MESSAGE("Error message: %s"),
    ERROR_DETAILS("Details: %s."),
    ERROR_IN_PAGE_LINKS("Error occurred while assigning found links: %s . Link doesn't match to expected URL pattern.");

    private String errorMessage;
}
