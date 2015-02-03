package org.sales.medsales.startup;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.sales.medsales.api.util.QuerierUtil;
import org.sales.medsales.dominio.Item;
import org.sales.medsales.dominio.Parceiro;
import org.sales.medsales.dominio.PrecoProduto;
import org.sales.medsales.dominio.Produto;
import org.sales.medsales.dominio.movimentacao.Entrada;
import org.sales.medsales.dominio.movimentacao.Status;
import org.sales.medsales.negocio.ParceiroFacade;
import org.sales.medsales.negocio.ProdutoFacade;
import org.sales.medsales.negocio.movimentacao.EstoqueFacade;

/**
 * Carrega dados iniciais para testes do sistema. 
 * @author Augusto
 */
//@Singleton
//@Startup
@Named
@Stateless
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
	
	@PersistenceUnit
	private EntityManagerFactory emf;
	
	public void load() throws Exception {
		Locale.getDefault();
		Locale.setDefault(new Locale("PT", "BR")); // TODO verificar a melhor maneira de fazer isso
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
			entrada.setStatus(Status.CONCLUIDO);
    		estoqueFacade.cadastrar(entrada);
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

	/**
	 * Lê produtos a partir do arquivo lista_produtos_eticos.txt
	 */
	private void loadProdutos() throws Exception {
		
		// stateless session para otimizar o loop com inserts.
		SessionFactory sessionFactory = emf.unwrap(SessionFactory.class);
		StatelessSession statelessSession = sessionFactory.openStatelessSession();
		Transaction tx = statelessSession.beginTransaction();
		
		Produto produto;
		
		InputStream produtosInput = Thread.currentThread().getContextClassLoader().getResourceAsStream("lista_produtos_eticos.txt"); 
		Scanner scanner = new Scanner(produtosInput);
		while (scanner.hasNext()) {
			String linha = scanner.nextLine();
			
			/*
			 * Quebrando a linha em tokens. Primeira parte é o código de barras,
			 * depois o nome, e o preço
			 */
			String[] tokens = linha.split("##");
			
			produto = new Produto(); 
			produto.setNome(tokens[1]);
			produto.setCodigoBarras(tokens[0]);
			
			// criando o preço
			PrecoProduto precoProduto = new PrecoProduto();
			precoProduto.setProduto(produto);
			precoProduto.setValidoEm(new Date()); 

			Locale brasil = new Locale ("pt", "BR");  
	        DecimalFormat df = new DecimalFormat ("#,##0.00", new DecimalFormatSymbols (brasil));  
	        df.setParseBigDecimal(true);
	        BigDecimal preco = (BigDecimal) df.parse(tokens[2]);
	        
			precoProduto.setValor(preco);
			
			statelessSession.insert(produto);
			statelessSession.insert(precoProduto);
//			produtoFacade.save(produto, precoProduto);
		}
		
		scanner.close();
		tx.commit();
		statelessSession.close();
	}
}
