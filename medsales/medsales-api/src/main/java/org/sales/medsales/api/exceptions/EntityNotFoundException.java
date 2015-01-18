package org.sales.medsales.api.exceptions;

import java.util.Collection;


/**
 * Exception para identificar erros ocorridos quando uma entidade esperada não é encontrada..
 */
@SuppressWarnings("serial")
public class EntityNotFoundException extends CodedAppException {

	public EntityNotFoundException(Collection<ExceptionMessage> mensagens, boolean allowRepeat) {
		super(mensagens, allowRepeat);
	}

	public EntityNotFoundException(Collection<ExceptionMessage> mensagens) {
		super(mensagens);
	}

	public EntityNotFoundException(String codigo, String mensagem, Boolean showCode, Object... parametros) {
		super(codigo, mensagem, showCode, parametros);
	}

	public EntityNotFoundException(String codigo, String mensagem, Object... parametros) {
		super(codigo, mensagem, parametros);
	}

	public EntityNotFoundException(String codigo, String mensagem, Throwable e, Object... parametros) {
		super(codigo, mensagem, e, parametros);
	}

}