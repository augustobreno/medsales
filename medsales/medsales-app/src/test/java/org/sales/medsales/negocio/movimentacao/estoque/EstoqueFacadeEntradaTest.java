package org.sales.medsales.negocio.movimentacao.estoque;

import java.util.Arrays;
import java.util.Date;

import javax.inject.Inject;

import junit.framework.Assert;

import org.easy.testeasy.dataloader.LoadData;
import org.junit.Test;
import org.sales.medsales.MedSalesBaseTest;
import org.sales.medsales.api.exceptions.NullParameterException;
import org.sales.medsales.dataLoader.PrecoProdutoDataLoader;
import org.sales.medsales.dataLoader.ProdutosDataLoader;
import org.sales.medsales.dominio.movimentacao.estoque.EntradaEstoque;
import org.sales.medsales.dominio.movimentacao.estoque.Item;
import org.sales.medsales.dominio.movimentacao.estoque.Produto;
import org.sales.medsales.dominio.movimentacao.estoque.Status;
import org.sales.medsales.exceptions.MovimentacaoSemItensException;
import org.sales.medsales.exceptions.ProdutoSemPrecoException;
import org.sales.medsales.negocio.movimentacao.estoque.EstoqueFacade;

public class EstoqueFacadeEntradaTest extends MedSalesBaseTest {

	@Inject
	private EstoqueFacade estoqueFacade;
	
	@Inject
	private ProdutosDataLoader produtosDataLoader;
	
	@Inject
	private PrecoProdutoDataLoader precoProdutoDataLoader;
	
	/**
	 * Garante a validação dos parâmetros de entrada
	 */
    @Test(expected=NullParameterException.class)
    public void cadastrarEntradaNull() {
    	estoqueFacade.cadastrar((EntradaEstoque)null);
    }
    
	/**
	 * Garante a validação dos parâmetros de entrada
	 */
    @Test(expected=NullParameterException.class)
    public void cadastrarEntradaOperacaoNull() {
    	EntradaEstoque entradaEstoque = new EntradaEstoque();
    	entradaEstoque.setOperacao(null);
    	entradaEstoque.setDataMovimentacao(new Date());
    	estoqueFacade.cadastrar(entradaEstoque);
    }
    
	/**
	 * Garante a validação dos parâmetros de entrada
	 */
    @Test(expected=MovimentacaoSemItensException.class)
    public void cadastrarEntradaSemItens() {
    	EntradaEstoque entradaEstoque = new EntradaEstoque();
    	entradaEstoque.setDataMovimentacao(new Date());
    	entradaEstoque.setItens(null);
    	estoqueFacade.cadastrar(entradaEstoque);
    }
    
	/**
	 * Tenta cadastrar uma entrada que possui um ou mais produtos 
	 * sem preços cadastrados.
	 * @throws Exception 
	 */
    @Test(expected=ProdutoSemPrecoException.class)
    @LoadData(dataLoader=ProdutosDataLoader.class) // TODO implementar interceptorr + arquillian?
    public void cadastrarEntradaProdutoSemPreco() throws Exception {
    	// workaround enquanto interceptor + arquillian continua um mistério
    	produtosDataLoader.load();    	
    	
    	// garante o estado anterior da base de dados
    	Assert.assertEquals(0L, getQuerier().count(EntradaEstoque.class).longValue());
    	Assert.assertEquals(0L, getQuerier().count(Item.class).longValue());
    	
    	
    	EntradaEstoque entradaEstoque = new EntradaEstoque();
    	entradaEstoque.setDataMovimentacao(new Date());
    	
    	Item item = new Item();
    	item.setProduto(getQuerier().findAny(Produto.class));
    	item.setQuantidade(10);
    	item.setMovimentacaoEstoque(entradaEstoque);
    	
    	entradaEstoque.setItens(Arrays.asList(item));
    	entradaEstoque.setStatus(Status.CONCLUIDO);
    	
    	estoqueFacade.cadastrar(entradaEstoque);
    	
    	getEm().clear();
    	
    	// verifica se os dados foram inseridos
    	Assert.assertEquals(1L, getQuerier().count(EntradaEstoque.class).longValue());
    	Assert.assertEquals(1L, getQuerier().count(Item.class).longValue());
    	
    }
    
	/**
	 * Tenta cadastrar uma entrada que possui apenas um produto, com preço registrado.
	 * @throws Exception 
	 */
    @Test
    public void cadastrarEntradaSucesso() throws Exception {
    	// workaround enquanto interceptor + arquillian continua um mistério
    	produtosDataLoader.load();
    	precoProdutoDataLoader.load();
    	
    	// garante o estado anterior da base de dados
    	Assert.assertEquals(0L, getQuerier().count(EntradaEstoque.class).longValue());
    	Assert.assertEquals(0L, getQuerier().count(Item.class).longValue());
    	
    	
    	EntradaEstoque entradaEstoque = new EntradaEstoque();
    	entradaEstoque.setDataMovimentacao(new Date());
    	
    	Item item = new Item();
    	item.setProduto(getQuerier().findAny(Produto.class));
    	item.setQuantidade(10);
    	item.setMovimentacaoEstoque(entradaEstoque);
    	
    	entradaEstoque.setItens(Arrays.asList(item));
    	entradaEstoque.setStatus(Status.CONCLUIDO);
    	
    	estoqueFacade.cadastrar(entradaEstoque);
    	
    	getEm().clear();
    	
    	// verifica se os dados foram inseridos
    	Assert.assertEquals(1L, getQuerier().count(EntradaEstoque.class).longValue());
    	Assert.assertEquals(1L, getQuerier().count(Item.class).longValue());
    	
    }
    
	/**
	 * testa a remoção de uma entrada.
	 */
    @Test
    public void removerEntrada() throws Exception {
    	// workaround enquanto interceptor + arquillian continua um mistério
    	produtosDataLoader.load();
    	precoProdutoDataLoader.load();
    	
    	EntradaEstoque entradaEstoque = new EntradaEstoque();
    	entradaEstoque.setDataMovimentacao(new Date());
    	
    	Item item = new Item();
    	item.setProduto(getQuerier().findAny(Produto.class));
    	item.setQuantidade(10);
    	item.setMovimentacaoEstoque(entradaEstoque);
    	
    	entradaEstoque.setItens(Arrays.asList(item));
    	entradaEstoque.setStatus(Status.CONCLUIDO);
    	
    	estoqueFacade.cadastrar(entradaEstoque);
    	
    	getEm().clear();
    	
    	// confirma que os dados foram inseridos
    	Assert.assertEquals(1L, getQuerier().count(EntradaEstoque.class).longValue());
    	Assert.assertEquals(1L, getQuerier().count(Item.class).longValue());
    	
    	// remove a entrada e re-confere os dados
    	estoqueFacade.remover(entradaEstoque);
    	
    	Assert.assertEquals(0L, getQuerier().count(EntradaEstoque.class).longValue());
    	Assert.assertEquals(0L, getQuerier().count(Item.class).longValue());
    	
    }
    
}
