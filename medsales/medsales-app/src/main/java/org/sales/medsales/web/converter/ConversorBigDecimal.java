package org.sales.medsales.web.converter;
import java.text.DecimalFormat;
import java.text.ParseException;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "BigDecimalConverter")
public class ConversorBigDecimal implements Converter {

	@Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) { 
        try {
            return NumberUtils.parseBigDecimal(s);
        } catch (ParseException e) {
            final String msg = "Erro ao converter BigDecimal";
            throw new ConverterException(new FacesMessage(msg));
        }
    }
     
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        return new DecimalFormat("#,##0.00").format(o);
    }
}