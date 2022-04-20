package com.mb.ecb.dto.xml;

import lombok.Data;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

/**
 * @author Milan Brankovic
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TimeCube", propOrder = {
        "cube"
})
@Data
public class TimeCube {

    @XmlElement(name = "Cube")
    protected List<CurrencyCube> cube;

    @XmlAttribute(name = "time")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar time;
}
