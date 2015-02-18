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
import org.sales.medsales.dominio.Ciclo;
import org.sales.medsales.dominio.Parceiro;
import org.sales.medsales.dominio.movimento.Operacao;
import org.sales.medsales.dominio.movimento.estoque.EntradaEstoque;
import org.sales.medsales.dominio.movimento.estoque.Item;
import org.sales.medsales.dominio.movimento.estoque.PrecoProduto;
import org.sales.medsales.dominio.movimento.estoque.Produto;
import org.sales.medsales.dominio.movimento.estoque.SaidaEstoque;
import org.sales.medsales.dominio.movimento.estoque.Status;
import org.sales.medsales.dominio.movimento.valor.Valor;
import org.sales.medsales.negocio.ParceiroFacade;
import org.sales.medsales.negocio.movimentacao.CicloFacade;
import org.sales.medsales.negocio.movimentacao.estoque.EstoqueFacade;
import org.sales.medsales.negocio.movimentacao.estoque.ProdutoFacade;

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
	private CicloFacade cicloFacade;
	
	@Inject
	private QuerierUtil querier; // FIXME QuerierUtil pertence ao pacote de testes. :-/
	
	@PersistenceUnit
	private EntityManagerFactory emf;
	
	public void load() throws Exception {
		Locale.getDefault();
		Locale.setDefault(new Locale("PT", "BR")); // TODO verificar a melhor maneira de fazer isso
		loadProdutos();
		loadParceiro();
		loadCiclos();
		loadEntradas();
		
	}

	private void loadCiclos() {
		Ciclo ciclo = new Ciclo();
		ciclo.setInicio(new Date());
		ciclo.setInvestidor(querier.findAt(Parceiro.class, 0));
		cicloFacade.save(ciclo);
		
		cicloFacade.addInvestimento(ciclo, new BigDecimal(1000.00));
		
		// Entrada
		EntradaEstoque entradaEstoque = new EntradaEstoque();
		List<Produto> produtos = querier.findAll(Produto.class);
		List<Item> itens = new ArrayList<>();
		for (int j = 0; j < DEFAULT_SIZE; j++) {
    		Item item = new Item();
    		item.setProduto(produtos.get(j)); 
    		item.setQuantidade(10);
    		item.setMovimentoEstoque(entradaEstoque);
    		itens.add(item);
		}	
		entradaEstoque.setItens(itens);
//		entradaEstoque.setCiclo(ciclo);
		entradaEstoque.setParceiro(ciclo.getInvestidor());
		entradaEstoque.setStatus(Status.CONCLUIDO);
		estoqueFacade.cadastrar(entradaEstoque);

		// saida
		SaidaEstoque saida = estoqueFacade.gerarSaida(entradaEstoque.getId());
		saida.setStatus(Status.CONCLUIDO);
		estoqueFacade.cadastrar(saida);
		
		// valor avulso
		Valor valorEntrada = new Valor();
		valorEntrada.setCiclo(ciclo);
		valorEntrada.setOperacao(Operacao.ENTRADA);
		valorEntrada.setParceiro(querier.findAt(Parceiro.class, 1));
		valorEntrada.setValor(BigDecimal.valueOf(500.00));
		cicloFacade.add(valorEntrada);
		
		Valor valorSaida = new Valor();
		valorSaida.setCiclo(ciclo);
		valorSaida.setOperacao(Operacao.SAIDA);
		valorSaida.setParceiro(querier.findAt(Parceiro.class, 2));
		valorSaida.setValor(BigDecimal.valueOf(100.00));
		cicloFacade.add(valorSaida);
		
	}

	private void loadEntradas() {

		List<Produto> produtos = querier.findAll(Produto.class);
		List<Parceiro> parceiros = querier.findAll(Parceiro.class);
    	for (int i = 0; i < DEFAULT_SIZE; i++) {
    		EntradaEstoque entradaEstoque = new EntradaEstoque();

    		List<Item> itens = new ArrayList<>();
    		for (int j = 0; j < DEFAULT_SIZE; j++) {
	    		Item item = new Item();
	    		item.setProduto(produtos.get(j)); 
	    		item.setQuantidade(10);
	    		item.setMovimentoEstoque(entradaEstoque);
	    		itens.add(item);
    		}	

    		entradaEstoque.setItens(itens);
    		
			entradaEstoque.setParceiro(parceiros.get(i));
			entradaEstoque.setStatus(Status.CONCLUIDO);
    		estoqueFacade.cadastrar(entradaEstoque);
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
