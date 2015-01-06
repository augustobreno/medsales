package org.sales.medsales.exceptions;

import java.util.Collection;

import org.sales.medsales.api.exceptions.BusinessException;
import org.sales.medsales.api.exceptions.ExceptionMessage;

@SuppressWarnings("serial")
public class ProdutoCodBarrasJaExisteException extends BusinessException {

	public ProdutoCodBarrasJaExisteException(Collection<ExceptionMessage> mensagens,
			boolean allowRepeat) {
		super(mensagens, allowRepeat);
		// TODO Auto-generated constructor stub
	}

	public ProdutoCodBarrasJaExisteException(Collection<ExceptionMessage> mensagens) {
		super(mensagens);
		// TODO Auto-generated constructor stub
	}

	public ProdutoCodBarrasJaExisteException(String codigo, String mensagem,
			Boolean showCode, Object... parametros) {
		super(codigo, mensagem, showCode, parametros);
		// TODO Auto-generated constructor stub
	}

	public ProdutoCodBarrasJaExisteException(String codigo, String mensagem,
			Object... parametros) {
		super(codigo, mensagem, parametros);
		// TODO Auto-generated constructor stub
	}

	public ProdutoCodBarrasJaExisteException(String codigo, String mensagem, Throwable e,
			Object... parametros) {
		super(codigo, mensagem, e, parametros);
		// TODO Auto-generated constructor stub
	}

}
