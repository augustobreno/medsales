package org.sales.medsales.api.exceptions;

import java.util.Collection;

import javax.ejb.ApplicationException;

/**
 * Classe para definições de exceções de negócio e validações.
 * Exceções deste tipo são consideradas controladas pela aplicação. Realiza rollback da transação mas não 
 * compromete a conversação (não causa erro 500).
 * @author augusto
 *
 */
@ApplicationException(rollback=true)
@SuppressWarnings("serial")
public class BusinessException extends CodedAppException {

	public BusinessException(Collection<ExceptionMessage> mensagens, boolean allowRepeat) {
		super(mensagens, allowRepeat);
	}

	public BusinessException(Collection<ExceptionMessage> mensagens) {
		super(mensagens);
	}

	public BusinessException(String codigo, String mensagem, Boolean showCode, Object... parametros) {
		super(codigo, mensagem, showCode, parametros);
	}

	public BusinessException(String codigo, String mensagem, Object... parametros) {
		super(codigo, mensagem, parametros);
	}

	public BusinessException(String codigo, String mensagem, Throwable e, Object... parametros) {
		super(codigo, mensagem, e, parametros);
	}
	
}
