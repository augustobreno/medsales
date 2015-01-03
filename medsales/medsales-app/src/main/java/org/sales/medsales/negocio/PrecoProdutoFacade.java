package org.sales.medsales.negocio;

import javax.ejb.Stateless;

import org.sales.medsales.api.negocio.CrudFacadeBase;
import org.sales.medsales.dominio.PrecoProduto;
import org.sales.medsales.persistencia.repository.PrecoProdutoRepository;

@SuppressWarnings("serial")
@Stateless
public class PrecoProdutoFacade extends CrudFacadeBase<PrecoProdutoRepository, PrecoProduto, Long>{

}
