package com.mb.ecb.dto.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Milan Brankovic
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Sender", namespace = "http://www.gesmes.org/xml/2002-08-01", propOrder = {
        "name"
})
@Data
public class Sender {

    @XmlElement(required = true)
    protected String name;
}
