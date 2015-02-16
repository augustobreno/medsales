package org.sales.medsales.negocio;

import javax.ejb.Stateless;

import org.sales.medsales.api.exceptions.NullParameterException;
import org.sales.medsales.api.negocio.CrudFacadeBase;
import org.sales.medsales.dominio.Ciclo;
import org.sales.medsales.exceptions.ExceptionCodes;
import org.sales.medsales.persistencia.repository.CicloRepository;

@SuppressWarnings("serial")
@Stateless
public class CicloFacade extends CrudFacadeBase<CicloRepository, Ciclo, Long>{

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
