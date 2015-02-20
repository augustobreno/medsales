package org.sales.medsales.negocio.movimentacao.valor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.sales.medsales.api.exceptions.BusinessException;
import org.sales.medsales.api.exceptions.ExceptionMessage;
import org.sales.medsales.api.exceptions.NullParameterException;
import org.sales.medsales.dominio.movimento.valor.Investimento;
import org.sales.medsales.exceptions.ExceptionCodes;
import org.sales.medsales.persistencia.repository.InvestimentoRepository;

@SuppressWarnings("serial")
public class InvestimentoBO implements Serializable {

	@Inject
	private InvestimentoRepository investimentoRepository;

	/**
	 * Adiciona um investimento ao ciclo identificado.
	 * 
	 * @param investimento
	 *            Investimento a ser adicionado ao ciclo. O parceiro investidor
	 *            será considerado o mesmo do ciclo.
	 * @return 
	 */
	public Investimento add(Investimento investimento) {
		validarNovoInvestimento(investimento);
		investimentoRepository.insert(investimento);
		return investimento;
	}

	private void validarNovoInvestimento(Investimento entity) {
		if (entity == null) {
			throw new NullParameterException(ExceptionCodes.INVESTIMENTO.INVESTIMENTO_REQUIRED,
					"É necessário informar o Investimento a ser realizado.");
		}
		
		Collection<ExceptionMessage> msgs = new ArrayList<ExceptionMessage>();
		if (entity.getCiclo() == null) {
			msgs.add(new ExceptionMessage(ExceptionCodes.INVESTIMENTO.CICLO_REQUIRED,
					"É necessário informar o Ciclo associado a este Investimento."));
		}
		if (entity.getValor() == null) {
			msgs.add(new ExceptionMessage(ExceptionCodes.INVESTIMENTO.VALOR_REQUIRED,
					"É necessário informar o valor do Investimento."));
		}

		if (entity.getParceiro() != null && !entity.getParceiro().equals(entity.getCiclo().getInvestidor())) {
			msgs.add(new ExceptionMessage(ExceptionCodes.INVESTIMENTO.INVESTIDOR_DIFERENTE_CICLO,
					"O Parceiro do Investimento deve ser o mesmo que o do Ciclo."));
		}
		
		if (!msgs.isEmpty()) {
			throw new BusinessException(msgs);
		}
	}
}
