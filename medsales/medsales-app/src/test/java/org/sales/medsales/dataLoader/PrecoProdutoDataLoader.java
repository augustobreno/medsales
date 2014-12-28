package org.sales.medsales.dataLoader;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.easy.testeasy.dataloader.HibernateDataLoader;
import org.sales.medsales.dominio.PrecoProduto;
import org.sales.medsales.dominio.Produto;
import org.sales.medsales.test.QuerierUtil;

/**
 * Data loader que cria uma base de dados de preços de Produtos.
 * @author Augusto
 *
 */
public class PrecoProdutoDataLoader extends HibernateDataLoader {

	@Inject
	private QuerierUtil querier;
	
	/**
	 * Cadastra um preço para cada produto existente na base de dados
	 */
	@Override
	public void load() throws Exception {
	
		List<Produto> produtos = querier.findAll(Produto.class);
		
		int precoInicial = 10;
		for (Produto produto : produtos) {
			PrecoProduto precoProduto = new PrecoProduto();
			precoProduto.setProduto(produto);
			precoProduto.setValidoEm(new Date());

			// simulando uma variação de preco.
			precoInicial += 10;
			precoProduto.setValor(new BigDecimal(precoInicial));

			getEntityManager().persist(precoProduto);
		}
		
		getEntityManager().flush();
	}

}
