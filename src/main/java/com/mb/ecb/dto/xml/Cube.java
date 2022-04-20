package com.mb.ecb.dto.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * @author Milan Brankovic
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Cube", propOrder = {
        "cube"
})
@Data
public class Cube {

    @XmlElement(name = "Cube")
    protected List<TimeCube> cube;
}
