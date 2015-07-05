package org.sales.medsales.negocio.movimentacao.estoque.produto;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sales.medsales.negocio.movimentacao.estoque.produto.ImportarProdutoBO.Pair;

/**
 * Contém a lógica para processar o arquivo de importação de preço de dados de
 * produtos.
 * 
 * @author Augusto
 *
 */
@Stateless
@Transactional
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ImportarProdutoEJBBatch {

	@Inject
	private AddPrecoProdutoBO addPrecoProdutoBO;

	/**
	 * Importa preços e produtos.
	 * 
	 * @param produtosInput
	 *            Stream para arquivo de TEXTO com o formato: <br/>
	 * <br/>
	 *            [código de barras]##[nome do produto]##[preco] <br/>
	 * <br/>
	 *            7898149937911##ZYDENA 100MG C/1 CPR##36,55
	 */
	public void importar(List<Pair> pares) {
		if (pares != null) {
			for (Pair pair : pares) {
				addPrecoProdutoBO.replacePreco(pair.getProduto(), pair.getPrecoProduto());
			}
		}
	}

}
