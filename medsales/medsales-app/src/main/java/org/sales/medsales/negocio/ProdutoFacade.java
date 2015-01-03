package org.sales.medsales.negocio;

import javax.ejb.Stateless;

import org.sales.medsales.api.negocio.CrudFacadeBase;
import org.sales.medsales.dominio.Produto;
import org.sales.medsales.persistencia.repository.ProdutoRepository;

@SuppressWarnings("serial")
@Stateless
public class ProdutoFacade extends CrudFacadeBase<ProdutoRepository, Produto, Long>{

}
