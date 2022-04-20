package com.mb.ecb.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Milan Brankovic
 */
@Slf4j
public class MoneySerializerTest {

    @Test
    public void testSerializationOfBigDecimal() throws Exception {
        final String key = "test_number";

        final Map<String, Object> input = new HashMap<>();
        input.put(key, BigDecimal.valueOf(10.0));

        final ObjectMapper objectMapper = new ObjectMapper();

        final String json = objectMapper.writeValueAsString(input);
        log.info("Converted input to {}", json);

        final Map<String, Object> parsed = objectMapper.readValue(json, Map.class);
        final Object value = parsed.get(key);
        Assertions.assertNotNull(value);
        Assertions.assertTrue((value instanceof Number));
        Assertions.assertEquals(10.0, (Double) value, 0.0000001);
    }
}
