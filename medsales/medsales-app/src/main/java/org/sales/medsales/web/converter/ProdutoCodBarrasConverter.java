package org.sales.medsales.web.converter;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.sales.medsales.dominio.movimentacao.estoque.Produto;

/**
 * Conversor que utiliza o código de barras do produto como chave.
 * @author Augusto
 *
 */
@FacesConverter(value = "produtoCodBarrasConverter")
public class ProdutoCodBarrasConverter implements Converter {

	@Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        try {
        	Produto produto = new Produto();
        	produto.setCodigoBarras(s);
            return produto;
        } catch (Exception e) {
            final String msg = "Erro ao converter código de barras para produto.";
            throw new ConverterException(new FacesMessage(msg));
        }
    }
     
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        return ((Produto) o).getCodigoBarras();
    }
}