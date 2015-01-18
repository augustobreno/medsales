package org.sales.medsales.api.exceptions;

import java.util.Collection;


/**
 * Exception para identificar erros ocorridos pela ausência
 * de informação de parâmetros (null) em operações.
 */
@SuppressWarnings("serial")
public class NullParameterException extends CodedAppException {

	public NullParameterException(Collection<ExceptionMessage> mensagens, boolean allowRepeat) {
		super(mensagens, allowRepeat);
	}

	public NullParameterException(Collection<ExceptionMessage> mensagens) {
		super(mensagens);
	}

	public NullParameterException(String codigo, String mensagem, Boolean showCode, Object... parametros) {
		super(codigo, mensagem, showCode, parametros);
	}

	public NullParameterException(String codigo, String mensagem, Object... parametros) {
		super(codigo, mensagem, parametros);
	}

	public NullParameterException(String codigo, String mensagem, Throwable e, Object... parametros) {
		super(codigo, mensagem, e, parametros);
	}

}