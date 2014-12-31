package org.sales.medsales.dataLoader;

import org.easy.testeasy.dataloader.HibernateDataLoader;
import org.sales.medsales.dominio.Produto;

/**
 * Data loader que cria uma base de dados de Produtos.
 * @author Augusto
 *
 */
public class ProdutosDataLoader extends HibernateDataLoader {

	@Override
	public void load() throws Exception {
		Produto produto;
		
		for (int i = 0; i < 10; i++) {
			produto = new Produto();
			produto.setDescricao("Produto " + i);
			getEntityManager().persist(produto);
		}
		
		getEntityManager().flush();
	}

}