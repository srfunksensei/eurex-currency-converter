package com.mb.ecb.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Milan Brankovic
 */
public class MoneySerializer extends JsonSerializer<BigDecimal> {

    public static final RoundingMode ROUND_LOGIC = RoundingMode.HALF_UP;

    @Override
    public void serialize(final BigDecimal value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException {
        jgen.writeNumber(applyMoneyScale(value));
    }

    public static BigDecimal applyMoneyScale(final BigDecimal value) {
        if (value == null) {
            return value;
        }
        return value.setScale(2, ROUND_LOGIC);
    }
}
