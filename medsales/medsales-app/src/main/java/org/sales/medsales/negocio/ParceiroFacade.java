package org.sales.medsales.negocio;

import javax.ejb.Stateless;

import org.sales.medsales.api.exceptions.AlreadyRegisteredException;
import org.sales.medsales.api.negocio.CrudFacadeBase;
import org.sales.medsales.dominio.Parceiro;
import org.sales.medsales.persistencia.repository.ParceiroRepository;

@SuppressWarnings("serial")
@Stateless
public class ParceiroFacade extends CrudFacadeBase<ParceiroRepository, Parceiro, Long>{

	@Override
	protected void validateSave(Parceiro entity) {
		super.validateSave(entity);
		validarUnicidade(entity);
	}

	/**
	 * Verifica se já há um objeto cadastro com os dados de unicidade informados. Para o fluxo de inclusão,
	 * apenas verifica se há outro objeto com as mesmas informações. Para o fluxo de alteração, verifica se há outro objeto
	 * diferente deste com as mesmas informações.
	 * @param entity Entidade a ser validada.
	 * @param uniqueProperties Propriedades que, juntas, representam uma regra de unicidade. 
	 */
	protected void validarUnicidade(Parceiro entity) {
		if (!checkUniqueConstraint(entity, "nome")) {
			throw new AlreadyRegisteredException(null, "Já existe um Cliente cadastrado com este nome.");
		}
	}
	
}
