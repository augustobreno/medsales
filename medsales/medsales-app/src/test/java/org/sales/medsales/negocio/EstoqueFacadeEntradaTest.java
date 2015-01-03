package org.sales.medsales.negocio;

import java.util.Arrays;

import javax.inject.Inject;

import junit.framework.Assert;

import org.easy.testeasy.dataloader.LoadData;
import org.junit.Test;
import org.sales.medsales.api.exceptions.NullParameterException;
import org.sales.medsales.api.test.OnServerBaseTest;
import org.sales.medsales.dataLoader.PrecoProdutoDataLoader;
import org.sales.medsales.dataLoader.ProdutosDataLoader;
import org.sales.medsales.dominio.Entrada;
import org.sales.medsales.dominio.Item;
import org.sales.medsales.dominio.Produto;
import org.sales.medsales.exceptions.MovimentacaoNeedsItemException;
import org.sales.medsales.exceptions.NoPrecoProdutoException;

public class EstoqueFacadeEntradaTest extends OnServerBaseTest {

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
    	estoqueFacade.cadastrarEntrada(null);
    }
    
	/**
	 * Garante a validação dos parâmetros de entrada
	 */
    @Test(expected=NullParameterException.class)
    public void cadastrarEntradaOperacaoNull() {
    	Entrada entrada = new Entrada();
    	entrada.setOperacao(null);
    	estoqueFacade.cadastrarEntrada(entrada);
    }
    
	/**
	 * Garante a validação dos parâmetros de entrada
	 */
    @Test(expected=MovimentacaoNeedsItemException.class)
    public void cadastrarEntradaSemItens() {
    	Entrada entrada = new Entrada();
    	entrada.setItens(null);
    	estoqueFacade.cadastrarEntrada(entrada);
    }
    
	/**
	 * Tenta cadastrar uma entrada que possui um ou mais produtos 
	 * sem preços cadastrados.
	 * @throws Exception 
	 */
    @Test(expected=NoPrecoProdutoException.class)
    @LoadData(dataLoader=ProdutosDataLoader.class) // TODO implementar interceptorr + arquillian?
    public void cadastrarEntradaProdutoSemPreco() throws Exception {
    	// workaround enquanto interceptor + arquillian continua um mistério
    	produtosDataLoader.load();    	
    	
    	// garante o estado anterior da base de dados
    	Assert.assertEquals(0L, getQuerierUtil().count(Entrada.class).longValue());
    	Assert.assertEquals(0L, getQuerierUtil().count(Item.class).longValue());
    	
    	
    	Entrada entrada = new Entrada();
    	
    	Item item = new Item();
    	item.setProduto(getQuerierUtil().findAny(Produto.class));
    	item.setQuantidade(10);
    	item.setTipoMovimentacao(entrada);
    	
    	entrada.setItens(Arrays.asList(item));
    	
    	estoqueFacade.cadastrarEntrada(entrada);
    	
    	getEm().clear();
    	
    	// verifica se os dados foram inseridos
    	Assert.assertEquals(1L, getQuerierUtil().count(Entrada.class).longValue());
    	Assert.assertEquals(1L, getQuerierUtil().count(Item.class).longValue());
    	
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
    	Assert.assertEquals(0L, getQuerierUtil().count(Entrada.class).longValue());
    	Assert.assertEquals(0L, getQuerierUtil().count(Item.class).longValue());
    	
    	
    	Entrada entrada = new Entrada();
    	
    	Item item = new Item();
    	item.setProduto(getQuerierUtil().findAny(Produto.class));
    	item.setQuantidade(10);
    	item.setTipoMovimentacao(entrada);
    	
    	entrada.setItens(Arrays.asList(item));
    	
    	estoqueFacade.cadastrarEntrada(entrada);
    	
    	getEm().clear();
    	
    	// verifica se os dados foram inseridos
    	Assert.assertEquals(1L, getQuerierUtil().count(Entrada.class).longValue());
    	Assert.assertEquals(1L, getQuerierUtil().count(Item.class).longValue());
    	
    }
    
    
}
