package org.sales.medsales.web.converter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;

public class NumberUtils {
    public static final String BIG_DECIMAL_FORMAT = "0.00";
    public static final DecimalFormat format = new DecimalFormat(BIG_DECIMAL_FORMAT);
    public static final int RE_SCALE = 3;
 
    static {
        format.setParseBigDecimal(true);
    }
 
    public static BigDecimal parseBigDecimal(String s) throws ParseException {
        final int i = s.indexOf(".");
        if (i != -1) {
            throw new ParseException(s, i);
        }
        final BigDecimal bigDecimal = (BigDecimal) NumberUtils.format.parse(s);
        if (bigDecimal.scale() > RE_SCALE) {
            throw new ParseException(s, i);
        }
        return bigDecimal.setScale(RE_SCALE, RoundingMode.DOWN);
    }
}