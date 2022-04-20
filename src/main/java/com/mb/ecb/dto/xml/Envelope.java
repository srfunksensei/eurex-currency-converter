package com.mb.ecb.dto.xml;

import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * @author Milan Brankovic
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Envelope", namespace = "http://www.gesmes.org/xml/2002-08-01", propOrder = {
        "subject",
        "sender",
        "cube"
})
//@XmlRootElement(name="Envelope", namespace = "http://www.gesmes.org/xml/2002-08-01")
@Data
public class Envelope {

    @XmlElement(required = true)
    protected String subject;

    @XmlElement(name = "Sender", required = true)
    protected Sender sender;

    @XmlElement(name = "Cube", namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref", required = true)
    protected Cube cube;

}
