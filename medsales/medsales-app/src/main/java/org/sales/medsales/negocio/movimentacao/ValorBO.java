package org.sales.medsales.negocio.movimentacao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.sales.medsales.api.exceptions.BusinessException;
import org.sales.medsales.api.exceptions.ExceptionMessage;
import org.sales.medsales.api.exceptions.NullParameterException;
import org.sales.medsales.dominio.movimento.valor.Valor;
import org.sales.medsales.exceptions.ExceptionCodes;
import org.sales.medsales.persistencia.repository.ValorRepository;

@SuppressWarnings("serial")
public class ValorBO implements Serializable {

	@Inject
	private ValorRepository valorRepository;

	/**
	 * Adiciona um valor ao ciclo identificado.
	 * 
	 * @param valor
	 *            Valor a ser adicionado ao ciclo.
	 * @return 
	 */
	public Valor add(Valor valor) {
		validarNovoValor(valor);
		valorRepository.insert(valor); 
		return valor;
	}

	private void validarNovoValor(Valor entity) {
		if (entity == null) {
			throw new NullParameterException(ExceptionCodes.VALOR.VALOR_REQUIRED,
					"É necessário informar o Valor a ser adicionado.");
		} 
		
		Collection<ExceptionMessage> msgs = new ArrayList<ExceptionMessage>();
		if (entity.getCiclo() == null) {
			msgs.add(new ExceptionMessage(ExceptionCodes.VALOR.CICLO_REQUIRED,
					"É necessário informar o Ciclo associado a este Valor."));
		}
		if (entity.getValor() == null) {
			msgs.add(new ExceptionMessage(ExceptionCodes.VALOR.VALOR_FINANCEIRO_REQUIRED,
					"É necessário informar o valor a ser adicionado."));
		}

		if (entity.getOperacao() == null) {
			msgs.add(new ExceptionMessage(ExceptionCodes.VALOR.OPERACAO_REQUIRED,
					"O Valor deve ter uma operação definida."));
		}
		
		if (!msgs.isEmpty()) {
			throw new BusinessException(msgs);
		}
	}
}
