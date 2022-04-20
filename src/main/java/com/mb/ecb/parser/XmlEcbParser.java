package com.mb.ecb.parser;

import com.mb.ecb.dto.xml.CurrencyCube;
import com.mb.ecb.exception.MalformedXmlException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Milan Brankovic
 */
@Component
@Slf4j
public class XmlEcbParser {

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public Map<LocalDate, List<CurrencyCube>> parse(final String xmlString) throws MalformedXmlException {
        final Map<LocalDate, List<CurrencyCube>> exchangeRateMap = new HashMap<>();

        try {
            final InputStream targetStream = new ByteArrayInputStream(xmlString.getBytes());

            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document doc = builder.parse(targetStream);
//            final Document doc = builder.parse(new InputSource(new StringReader(xmlString)))

            // normalize XML response
            doc.getDocumentElement().normalize();

            // skip envelope

            final NodeList cubeNodes = doc.getElementsByTagName("Cube");
            exchangeRateMap.putAll(extractExchangeRate(cubeNodes));

        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
            throw new MalformedXmlException(ex.getLocalizedMessage());
        }

        return exchangeRateMap;
    }

    protected Map<LocalDate, List<CurrencyCube>> extractExchangeRate(final NodeList cubeNodes) {
        final Map<LocalDate, List<CurrencyCube>> exchangeRateMap = new HashMap<>();

        if (cubeNodes != null) {

            List<CurrencyCube> currencyCubes = new ArrayList<>();
            for (int i = 0; i < cubeNodes.getLength(); i++) {
                final Node cubeNode = cubeNodes.item(i);

                if (cubeNode.getNodeType() == Node.ELEMENT_NODE) {
                    final Element cubeElem = (Element) cubeNode;

                    final String attrDate = cubeElem.getAttribute("time");
                    if (!attrDate.isBlank()) {
                        final LocalDate date = parseDate(attrDate);
                        currencyCubes.clear();
                        exchangeRateMap.put(date, currencyCubes);
                        continue;
                    }

                    final String attrCurrency = cubeElem.getAttribute("currency"),
                            attrRate = cubeElem.getAttribute("rate");
                    if (!attrCurrency.isBlank() && !attrRate.isBlank()) {
                        final CurrencyCube currencyCube = CurrencyCube.builder()
                                .currency(attrCurrency)
                                .rate(parseExchangeRate(attrRate))
                                .build();
                        currencyCubes.add(currencyCube);
                    }
                }
            }
        }

        return exchangeRateMap;
    }

    public static LocalDate parseDate(final String dateString) {
        return LocalDate.parse(dateString, DATE_TIME_FORMATTER);
    }

    public static XMLGregorianCalendar convert(final String dateString) throws DatatypeConfigurationException {
        final LocalDate date = parseDate(dateString);

        final GregorianCalendar cal = GregorianCalendar.from(date.atStartOfDay(ZoneId.systemDefault()));
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
    }

    public static BigDecimal parseExchangeRate(final String exchangeRateString) {
        return BigDecimal.valueOf(Double.parseDouble(exchangeRateString));
    }
}
