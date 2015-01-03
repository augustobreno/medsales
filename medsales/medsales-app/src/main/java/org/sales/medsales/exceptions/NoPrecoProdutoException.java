package org.sales.medsales.exceptions;

import java.util.Collection;

import org.sales.medsales.api.exceptions.BusinessException;
import org.sales.medsales.api.exceptions.ExceptionMessage;

@SuppressWarnings("serial")
public class NoPrecoProdutoException extends BusinessException {

	public NoPrecoProdutoException(Collection<ExceptionMessage> mensagens,
			boolean allowRepeat) {
		super(mensagens, allowRepeat);
		// TODO Auto-generated constructor stub
	}

	public NoPrecoProdutoException(Collection<ExceptionMessage> mensagens) {
		super(mensagens);
		// TODO Auto-generated constructor stub
	}

	public NoPrecoProdutoException(String codigo, String mensagem,
			Boolean showCode, Object... parametros) {
		super(codigo, mensagem, showCode, parametros);
		// TODO Auto-generated constructor stub
	}

	public NoPrecoProdutoException(String codigo, String mensagem,
			Object... parametros) {
		super(codigo, mensagem, parametros);
		// TODO Auto-generated constructor stub
	}

	public NoPrecoProdutoException(String codigo, String mensagem, Throwable e,
			Object... parametros) {
		super(codigo, mensagem, e, parametros);
		// TODO Auto-generated constructor stub
	}

}
