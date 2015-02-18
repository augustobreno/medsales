package org.sales.medsales.dataLoader;

import org.easy.testeasy.dataloader.HibernateDataLoader;
import org.sales.medsales.dominio.movimento.estoque.Produto;

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
			produto.setNome("Produto " + i);
			produto.setCodigoBarras("" + i + i + i + i + i + i);
			getEntityManager().persist(produto);
		}
		
		getEntityManager().flush();
	}

}
