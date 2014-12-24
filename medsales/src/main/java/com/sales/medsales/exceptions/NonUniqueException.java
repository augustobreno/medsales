package com.sales.medsales.exceptions;

import java.util.Collection;

/**
 * Criada para permitir reclamar de casos de tentativa de cadastro
 * de objetos com dados repetidos (já cadastrados). 
 * @author Augusto
 */
@SuppressWarnings("serial")
public class NonUniqueException extends BusinessException {

	public NonUniqueException(String codigo, String mensagem,
			Object... parametros) {
		super(codigo, mensagem, parametros);
		// TODO Auto-generated constructor stub
	}

	public NonUniqueException(String codigo, String mensagem, Boolean showCode,
			Object... parametros) {
		super(codigo, mensagem, showCode, parametros);
		// TODO Auto-generated constructor stub
	}

	public NonUniqueException(String codigo, String mensagem, Throwable e,
			Object... parametros) {
		super(codigo, mensagem, e, parametros);
		// TODO Auto-generated constructor stub
	}

	public NonUniqueException(Collection<ExceptionMessage> mensagens,
			boolean allowRepeat) {
		super(mensagens, allowRepeat);
		// TODO Auto-generated constructor stub
	}

	public NonUniqueException(Collection<ExceptionMessage> mensagens) {
		super(mensagens);
		// TODO Auto-generated constructor stub
	}

}
