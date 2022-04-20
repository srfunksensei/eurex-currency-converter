package com.mb.ecb.dto.xml;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;

/**
 * @author Milan Brankovic
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CurrencyCube")
@Data
@Builder
public class CurrencyCube {

    @NotNull
    @XmlAttribute(name = "currency", required = true)
    protected String currency;

    @NotNull
    @XmlAttribute(name = "rate", required = true)
    protected BigDecimal rate;
}
