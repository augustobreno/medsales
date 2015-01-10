package org.sales.medsales.startup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;

import org.sales.medsales.api.test.QuerierUtil;
import org.sales.medsales.dominio.Item;
import org.sales.medsales.dominio.Parceiro;
import org.sales.medsales.dominio.PrecoProduto;
import org.sales.medsales.dominio.Produto;
import org.sales.medsales.dominio.movimentacao.Entrada;
import org.sales.medsales.negocio.EstoqueFacade;
import org.sales.medsales.negocio.ParceiroFacade;
import org.sales.medsales.negocio.ProdutoFacade;

/**
 * Carrega dados iniciais para testes do sistema. 
 * @author Augusto
 */
//@Singleton
//@Startup
@Named
public class DataStartupService {
	
	@Inject
	private Logger logger;

	/** Tamanho default para cadastro de dados na base de dados para testes. */
	private static final int DEFAULT_SIZE = 10;

	@Inject
	private ParceiroFacade parceiroFacade;
	
	@Inject
	private ProdutoFacade produtoFacade;
	
	@Inject
	private EstoqueFacade estoqueFacade;
	
	@Inject
	private QuerierUtil querier; // FIXME QuerierUtil pertence ao pacote de testes. :-/
	
	public void load() {
		loadProdutos();
		loadParceiro();
		loadEntradas();
	}

	private void loadEntradas() {

		List<Produto> produtos = querier.findAll(Produto.class);
		List<Parceiro> parceiros = querier.findAll(Parceiro.class);
    	for (int i = 0; i < DEFAULT_SIZE; i++) {
    		Entrada entrada = new Entrada();

    		List<Item> itens = new ArrayList<>();
    		for (int j = 0; j < DEFAULT_SIZE; j++) {
	    		Item item = new Item();
	    		item.setProduto(produtos.get(j)); 
	    		item.setQuantidade(10);
	    		item.setMovimentacaoEstoque(entrada);
	    		itens.add(item);
    		}	

    		entrada.setItens(itens);
    		
			entrada.setParceiro(parceiros.get(i));
    		estoqueFacade.cadastrarEntrada(entrada);
    	}
    	
		
	}

	private void loadParceiro() {
		Parceiro parceiro;
		
		for (int i = 0; i < DEFAULT_SIZE; i++) {
			parceiro = new Parceiro();
			parceiro.setNome("Cliente " + i);
			parceiroFacade.save(parceiro);
		}
	}

	private void loadProdutos() {
		Produto produto;
		int precoInicial = DEFAULT_SIZE;
		for (int i = 0; i < DEFAULT_SIZE; i++) {
			produto = new Produto();
			produto.setNome("Produto " + i);
			produto.setCodigoBarras("" + i + i + i + i + i + i);
			
			// criando o preço
			PrecoProduto precoProduto = new PrecoProduto();
			precoProduto.setProduto(produto);
			precoProduto.setValidoEm(new Date()); 

			// simulando uma variação de preco.
			precoInicial += 10;
			precoProduto.setValor(new BigDecimal(precoInicial));
			
			produtoFacade.save(produto, precoProduto);
		}		
	}
}
