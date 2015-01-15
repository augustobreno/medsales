package org.sales.medsales.api.exceptions;

import java.util.Collection;


/**
 * Exception para identificar erros ocorridos pela ausência
 * de informação de parâmetros (null) em operações.
 */
@SuppressWarnings("serial")
public class NullParameterException extends BusinessException {

	public NullParameterException(Collection<ExceptionMessage> mensagens, boolean allowRepeat) {
		super(mensagens, allowRepeat);
		// TODO Auto-generated constructor stub
	}

	public NullParameterException(Collection<ExceptionMessage> mensagens) {
		super(mensagens);
		// TODO Auto-generated constructor stub
	}

	public NullParameterException(String codigo, String mensagem, Boolean showCode, Object... parametros) {
		super(codigo, mensagem, showCode, parametros);
		// TODO Auto-generated constructor stub
	}

	public NullParameterException(String codigo, String mensagem, Object... parametros) {
		super(codigo, mensagem, parametros);
		// TODO Auto-generated constructor stub
	}

	public NullParameterException(String codigo, String mensagem, Throwable e, Object... parametros) {
		super(codigo, mensagem, e, parametros);
		// TODO Auto-generated constructor stub
	}

}