package org.sales.medsales.negocio.movimentacao.estoque.produto;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.inject.Inject;

import org.easy.qbeasy.QBEFilter;
import org.easy.qbeasy.api.operator.Operators;
import org.sales.medsales.api.exceptions.BusinessException;
import org.sales.medsales.dominio.movimento.estoque.PrecoProduto;
import org.sales.medsales.dominio.movimento.estoque.Produto;
import org.sales.medsales.persistencia.repository.ProdutoRepository;
import org.sales.medsales.web.converter.NumberUtils;

/**
 * Contém a lógica para processar o arquivo de importação de preço de dados de
 * produtos.
 * 
 * @author Augusto
 *
 */
public class ImportarProdutoBO {

	@Inject
	private ProdutoRepository produtoRepository;
	
	@Inject
	private ImportarProdutoEJBBatch importarProdutoEJBBatch;

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
	public void importar(InputStream produtosInput) {

		try {
			
			// pares para realizar batch com um subconjunto de operações
			List<Pair> pares = new ArrayList<Pair>();
			
			Scanner scanner = new Scanner(produtosInput);
			
			int contador = 0;
			while (scanner.hasNext()) {
				
				contador++;
				
				String linha = scanner.nextLine();

				System.out.println("importar: " + contador + " " + linha);
				
				/*
				 * Quebrando a linha em tokens. Primeira parte é o código de
				 * barras, depois o nome, e o preço
				 */
				String[] tokens = linha.split("##");

				String codigoBarras = tokens[0].trim();
				String nome = tokens[1].trim();
				BigDecimal preco = NumberUtils.parseBigDecimal(tokens[2]);

				/*
				 * Se o produto já existe, acrescenta um preco, se nao existe,
				 * cria um novo.
				 */
				Produto produto = findProduto(codigoBarras, nome);
				

				if (produto == null) {
					produto = new Produto();
					produto.setCodigoBarras(codigoBarras);
				} 	
				
				produto.setNome(nome); // atualiza o nome
				

				// criando o preço
				PrecoProduto precoProduto = new PrecoProduto();
				precoProduto.setProduto(produto);
				precoProduto.setValidoEm(new Date());
				precoProduto.setValor(preco);

				// adiciona no cache para eventual processamento
				pares.add(new Pair(produto, precoProduto));
				
				// processa a cada contigente de 500 pares
				if (contador % 500 == 0) {
					processar(pares);
					pares.clear();
				}
			}

			// processa os remanescentes
			processar(pares);
			
			scanner.close();

		} catch (Exception e) {
			throw new BusinessException(null, "Ocorreu um erro ao processar o arquivo.", e);
		}

	}

	private Produto findProduto(String codigoBarras, String nome) {
		QBEFilter<Produto> filter = new QBEFilter<>(Produto.class);
		filter.filterBy("codigoBarras", Operators.equal(), codigoBarras);

		List<Produto> produtos = produtoRepository.findAllBy(filter);
		
		/*
		 * A pesquisa é realizada pelo código de barras, se encontrar mais de um, verifica se o nome é compatível
		 */
		Produto produto = null;
		if (produtos.size() == 1) {
			produto = produtos.get(0);
		} else {
			for (Produto result : produtos) {
				if (result.getNome().equals(nome)) {
					produto = result;
					break;
				}
			}
		}

		
		return produto;
	}

	private void processar(List<Pair> pares) {
		if (pares != null && !pares.isEmpty()) {
			importarProdutoEJBBatch.importar(pares);
		}
	}

	class Pair {
		
		Produto produto;
		PrecoProduto precoProduto;
		
		public Pair(Produto produto, PrecoProduto precoProduto) {
			super();
			this.produto = produto;
			this.precoProduto = precoProduto;
		}
		
		public Produto getProduto() {
			return produto;
		}
		public PrecoProduto getPrecoProduto() {
			return precoProduto;
		}
		
	}
}
