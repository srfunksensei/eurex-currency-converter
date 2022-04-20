package com.mb.ecb.parser;

import com.mb.ecb.dto.xml.CurrencyCube;
import com.mb.ecb.exception.MalformedXmlException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author Milan Brankovic
 */
public class XmlEcbParserTest {

    private final XmlEcbParser underTest = new XmlEcbParser();

    @Test
    public void parse_malformedXml() throws URISyntaxException {
        final ClassLoader classLoader = getClass().getClassLoader();
        final Path path = Paths.get(classLoader.getResource("xml/malformed.xml").toURI());
        Assertions.assertThrows(MalformedXmlException.class,
                () -> underTest.parse(Files.readString(path)),
                "Expected parse() to throw, but it didn't");
    }

    @Test
    public void parse_correctlyFormedXml() throws URISyntaxException, IOException, MalformedXmlException {
        final ClassLoader classLoader = getClass().getClassLoader();
        final Path path = Paths.get(classLoader.getResource("xml/correctly-formed.xml").toURI());
        final Map<LocalDate, List<CurrencyCube>> result = underTest.parse(Files.readString(path));
        Assertions.assertNotNull(result, "Expected result");
        Assertions.assertFalse(result.keySet().isEmpty(), "Expected parsed bucket key");

        final LocalDate key = result.keySet().stream().findFirst().get();
        Assertions.assertNotNull(result.get(key), "Expected list for parsed bucket key");
        Assertions.assertFalse(result.get(key).isEmpty(), "Expected list with elements for parsed bucket key");
    }

    @Test
    public void parseDate() {
        final String dateString = "2022-02-05";
        final LocalDate result = XmlEcbParser.parseDate(dateString);
        Assertions.assertNotNull(result, "Expected parsed date");
        Assertions.assertEquals(5, result.getDayOfMonth(), "Expected different result");
        Assertions.assertEquals(2, result.getMonthValue(), "Expected different result");
        Assertions.assertEquals(2022, result.getYear(), "Expected different result");
    }

    @Test
    public void parseExchangeRate() {
        final String exchangeRateString = "1.1435";
        final BigDecimal result = XmlEcbParser.parseExchangeRate(exchangeRateString);
        Assertions.assertNotNull(result, "Expected parsed value");
    }

    @Test
    public void convert() throws DatatypeConfigurationException {
        final String dateString = "2022-02-05";
        final XMLGregorianCalendar result = XmlEcbParser.convert(dateString);
        Assertions.assertNotNull(result, "Expected parsed value");
        Assertions.assertEquals(5, result.getDay(), "Expected different result");
        Assertions.assertEquals(2, result.getMonth(), "Expected different result");
        Assertions.assertEquals(2022, result.getYear(), "Expected different result");
    }
}
