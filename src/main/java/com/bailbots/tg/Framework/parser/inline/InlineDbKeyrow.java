package com.bailbots.tg.Framework.parser.inline;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "inline-db-keyrow")
public class InlineDbKeyrow {

    @JacksonXmlProperty(isAttribute = true)
    private String entity;

    @JacksonXmlProperty(isAttribute = true)
    private String maxItemsOnPage;

    @JacksonXmlProperty(isAttribute = true)
    private String methodForName;

    @JacksonXmlProperty(isAttribute = true)
    private String methodForCallback;

}
