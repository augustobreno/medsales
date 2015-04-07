package org.sales.medsales.web.converter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

public class NumberUtils {
    public static final String BIG_DECIMAL_FORMAT = "0.00";
    public static final DecimalFormat format = new DecimalFormat(BIG_DECIMAL_FORMAT);
    public static final int RE_SCALE = 3;
 
    static {
        format.setParseBigDecimal(true);
    }
 
    public static BigDecimal parseBigDecimal(String s) throws ParseException {
		Locale brasil = new Locale("pt", "BR");
		DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(brasil));
		df.setParseBigDecimal(true);
		BigDecimal preco = (BigDecimal) df.parse(s);
		return preco;
    }
}