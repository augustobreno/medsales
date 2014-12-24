package com.sales.medsales.negocio;

import javax.ejb.Stateless;

import com.sales.medsales.dominio.Cliente;
import com.sales.medsales.persistencia.repository.ClienteRepository;

@SuppressWarnings("serial")
@Stateless
public class ClienteFacade extends CrudFacadeBase<ClienteRepository, Cliente, Long>{

	@Override
	protected void validateSave(Cliente entity) {
		super.validateSave(entity);
		// implementar validação de unicidade. Importar QBE?
	}
}
