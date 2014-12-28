package org.sales.medsales.exceptions;


/**
 * Exception para identificar erros ocorridos pela ausência
 * de informação de parâmetros (null) em operações.
 */
@SuppressWarnings("serial")
public class NullParameterException extends AppException {

	public NullParameterException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NullParameterException(String messagem, Throwable e) {
		super(messagem, e);
		// TODO Auto-generated constructor stub
	}

	public NullParameterException(String mensagem) {
		super(mensagem);
		// TODO Auto-generated constructor stub
	}

	public NullParameterException(Throwable e) {
		super(e);
		// TODO Auto-generated constructor stub
	}

}