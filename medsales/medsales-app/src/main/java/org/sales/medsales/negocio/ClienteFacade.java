package org.sales.medsales.negocio;

import javax.ejb.Stateless;

import org.sales.medsales.dominio.Cliente;
import org.sales.medsales.exceptions.AlreadyRegisteredException;
import org.sales.medsales.persistencia.repository.ClienteRepository;

@SuppressWarnings("serial")
@Stateless
public class ClienteFacade extends CrudFacadeBase<ClienteRepository, Cliente, Long>{

	@Override
	protected void validateSave(Cliente entity) {
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
	protected void validarUnicidade(Cliente entity) {
		if (!checkUniqueConstraint(entity, "nome")) {
			throw new AlreadyRegisteredException(null, "Já existe um Cliente cadastrado com este nome.");
		}
	}
	
}
