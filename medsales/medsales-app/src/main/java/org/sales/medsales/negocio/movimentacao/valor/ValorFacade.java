package org.sales.medsales.negocio.movimentacao.valor;

import javax.ejb.Stateless;

import org.sales.medsales.api.negocio.CrudFacadeBase;
import org.sales.medsales.dominio.movimento.valor.Valor;
import org.sales.medsales.persistencia.repository.ValorRepository;

@SuppressWarnings("serial")
@Stateless
public class ValorFacade extends CrudFacadeBase<ValorRepository, Valor, Long>{

	
}
