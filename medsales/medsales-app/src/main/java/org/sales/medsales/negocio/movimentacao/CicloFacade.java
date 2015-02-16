package org.sales.medsales.negocio.movimentacao;

import java.math.BigDecimal;

import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.sales.medsales.api.exceptions.NullParameterException;
import org.sales.medsales.api.negocio.CrudFacadeBase;
import org.sales.medsales.dominio.Ciclo;
import org.sales.medsales.dominio.movimentacao.Investimento;
import org.sales.medsales.dominio.movimentacao.Valor;
import org.sales.medsales.exceptions.ExceptionCodes;
import org.sales.medsales.persistencia.repository.CicloRepository;

@SuppressWarnings("serial")
@Stateless
public class CicloFacade extends CrudFacadeBase<CicloRepository, Ciclo, Long>{

	@Inject
	private Instance<InvestimentoBO> investimentoBOInstance;
	
	@Inject
	private Instance<ValorBO> valorBOInstance;
	
	/**
	 * Adiciona um investimento ao ciclo identificado.
	 * @param ciclo Ciclo que receberá o investimento.
	 * @param investimento Investimento a ser adicionado ao ciclo. O parceiro inverstidor será considerado o mesmo do ciclo.
	 * @return 
	 */
	public Investimento addInvestimento(Ciclo ciclo, BigDecimal investimento) {
		Investimento entity = new Investimento();
		entity.setCiclo(ciclo);
		entity.setParceiro(ciclo != null ? ciclo.getInvestidor() : null);
		entity.setValor(investimento);
		
		return getInvestimentoBO().add(entity);
	}
	
	/**
	 * Adiciona um valor ao ciclo identificado.
	 * @param ciclo Ciclo que receberá o valor.
	 * @param valor Valor a ser adicionado ao ciclo.
	 * @return 
	 */
	public Valor add(Valor valor) {
		return getValorBO().add(valor);
	}

	private InvestimentoBO getInvestimentoBO() {
		return investimentoBOInstance.get();
	}
	
	private ValorBO getValorBO() {
		return valorBOInstance.get();
	}
	
	@Override
	protected void validateSave(Ciclo entity) {
		super.validateSave(entity);
		validarDadosObrigatorios(entity);
	}

	private void validarDadosObrigatorios(Ciclo entity) {
		
		if (entity == null) {
			throw new NullParameterException(ExceptionCodes.CICLO.CICLO_REQUIRED, "É necessário informar um Ciclo.");
		}
		if (entity.getInicio() == null) {
			throw new NullParameterException(ExceptionCodes.CICLO.INICIO_REQUIRED, "É necessário informar a Data de início do Ciclo.");
		}
		
	}
	
}
