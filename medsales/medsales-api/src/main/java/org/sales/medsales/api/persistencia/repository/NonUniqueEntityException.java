package org.sales.medsales.api.persistencia.repository;

import javax.ejb.ApplicationException;

import org.sales.medsales.api.exceptions.AppException;

/**
 * Exceção que expressa a falta de um resultado único em uma consulta realizada na base de dados. 
 * @author augusto
 */
@SuppressWarnings("serial")
@ApplicationException(rollback=true)
public class NonUniqueEntityException extends AppException {

	public NonUniqueEntityException() {
		super();
	}

	public NonUniqueEntityException(String messagem, Throwable e) {
		super(messagem, e);
	}

	public NonUniqueEntityException(String mensagem) {
		super(mensagem);
	}

	public NonUniqueEntityException(Throwable e) {
		super(e);
	}

}
