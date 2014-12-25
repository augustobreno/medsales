package org.sales.medsales.negocio;

import javax.ejb.Stateless;

import org.easy.qbeasy.QBEFilter;
import org.easy.qbeasy.api.operator.Operators;
import org.sales.medsales.dominio.Cliente;
import org.sales.medsales.exceptions.AlreadyRegisteredException;
import org.sales.medsales.persistencia.repository.ClienteRepository;
import org.sales.medsales.util.ReflectionUtil;

@SuppressWarnings("serial")
@Stateless
public class ClienteFacade extends CrudFacadeBase<ClienteRepository, Cliente, Long>{

	@Override
	protected void validateSave(Cliente entity) {
		super.validateSave(entity);
		validateUniqueConstraint(entity, "nome");
	}

	/**
	 * Verifica se já há um objeto cadastro com os dados de unicidade informados. Para o fluxo de inclusão,
	 * apenas verifica se há outro objeto com as mesmas informações. Para o fluxo de alteração, verifica se há outro objeto
	 * diferente deste com as mesmas informações.
	 * @param entity Entidade a ser validada.
	 * @param uniqueProperties Propriedades que, juntas, representam uma regra de unicidade. 
	 */
	protected void validateUniqueConstraint(Cliente entity, String...uniqueProperties) {
		QBEFilter<? extends Cliente> filter = new QBEFilter<>(entity.getClass());
		
		if (entity.getId() != null) {
			// fluxo de alteração
			filter.filterBy("id", Operators.notEqual(), entity.getId());
		}
		
		for (String uniqueProp : uniqueProperties) {
			Object uniqueValue = ReflectionUtil.getValue(entity, uniqueProp);
			filter.filterBy(uniqueProp, Operators.equal(), uniqueValue);
		}
		
		Long count = getRepository().count(filter);
		if (count > 0) {
			throw new AlreadyRegisteredException(null, "Já existe um Cliente cadastrado com este nome.");
		}
		
	}
}
