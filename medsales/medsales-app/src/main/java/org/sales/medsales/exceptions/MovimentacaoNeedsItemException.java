package org.sales.medsales.exceptions;

import java.util.Collection;

/**
 * QUando uma movimentação é cadastrada em itens.
 */
@SuppressWarnings("serial")
public class MovimentacaoNeedsItemException extends BusinessException {

	public MovimentacaoNeedsItemException(String codigo, String mensagem,
			Object... parametros) {
		super(codigo, mensagem, parametros);
		// TODO Auto-generated constructor stub
	}

	public MovimentacaoNeedsItemException(String codigo, String mensagem, Boolean showCode,
			Object... parametros) {
		super(codigo, mensagem, showCode, parametros);
		// TODO Auto-generated constructor stub
	}

	public MovimentacaoNeedsItemException(String codigo, String mensagem, Throwable e,
			Object... parametros) {
		super(codigo, mensagem, e, parametros);
		// TODO Auto-generated constructor stub
	}

	public MovimentacaoNeedsItemException(Collection<ExceptionMessage> mensagens,
			boolean allowRepeat) {
		super(mensagens, allowRepeat);
		// TODO Auto-generated constructor stub
	}

	public MovimentacaoNeedsItemException(Collection<ExceptionMessage> mensagens) {
		super(mensagens);
		// TODO Auto-generated constructor stub
	}

}