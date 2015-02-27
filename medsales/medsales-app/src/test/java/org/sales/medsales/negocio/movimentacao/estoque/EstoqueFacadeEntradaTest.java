package org.sales.medsales.negocio.movimentacao.estoque;

import java.util.Arrays;
import java.util.Date;

import javax.inject.Inject;

import junit.framework.Assert;

import org.easy.testeasy.dataloader.LoadData;
import org.junit.Test;
import org.sales.medsales.MedSalesBaseTest;
import org.sales.medsales.api.exceptions.BusinessException;
import org.sales.medsales.api.exceptions.NullParameterException;
import org.sales.medsales.dataLoader.PrecoProdutoDataLoader;
import org.sales.medsales.dataLoader.ProdutosDataLoader;
import org.sales.medsales.dominio.movimento.estoque.EntradaEstoque;
import org.sales.medsales.dominio.movimento.estoque.Item;
import org.sales.medsales.dominio.movimento.estoque.PrecoProduto;
import org.sales.medsales.dominio.movimento.estoque.Status;
import org.sales.medsales.exceptions.ExceptionCodes;
import org.sales.medsales.exceptions.MovimentacaoSemItensException;

public class EstoqueFacadeEntradaTest extends MedSalesBaseTest {

	@Inject
	private EstoqueFacade estoqueFacade;
	
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
    @Test
    public void cadastrarEntradaOperacaoNull() {
    	EntradaEstoque entradaEstoque = new EntradaEstoque();
    	entradaEstoque.setOperacao(null);
    	entradaEstoque.setDataMovimento(new Date());
    	
    	try {
    		estoqueFacade.cadastrar(entradaEstoque);
    		Assert.fail();
		} catch (BusinessException e) {
			Assert.assertTrue(e.hasCode(ExceptionCodes.MOVIMENTACAO_ESTOQUE.OPERACAO_REQUIRED));
		}
    }
    
	/**
	 * Garante a validação dos parâmetros de entrada
	 */
    @Test(expected=MovimentacaoSemItensException.class)
    public void cadastrarEntradaSemItens() {
    	EntradaEstoque entradaEstoque = new EntradaEstoque();
    	entradaEstoque.setDataMovimento(new Date());
    	entradaEstoque.setItens(null);
    	estoqueFacade.cadastrar(entradaEstoque);
    }
    
	/**
	 * Tenta cadastrar uma entrada que possui apenas um produto, com preço registrado.
	 * @throws Exception 
	 */
    @Test
    @LoadData(dataLoader={ProdutosDataLoader.class, PrecoProdutoDataLoader.class})
    public void cadastrarEntradaSucesso() throws Exception {
    	// garante o estado anterior da base de dados
    	Assert.assertEquals(0L, getQuerier().count(EntradaEstoque.class).longValue());
    	Assert.assertEquals(0L, getQuerier().count(Item.class).longValue());
    	
    	
    	EntradaEstoque entradaEstoque = new EntradaEstoque();
    	entradaEstoque.setDataMovimento(new Date());
    	
    	Item item = new Item();
    	item.setPrecoProduto(getQuerier().findAny(PrecoProduto.class));
    	item.setQuantidade(10);
    	item.setMovimentoEstoque(entradaEstoque);
    	
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
    @LoadData(dataLoader={ProdutosDataLoader.class, PrecoProdutoDataLoader.class})
    public void removerEntrada() throws Exception {
    	EntradaEstoque entradaEstoque = new EntradaEstoque();
    	entradaEstoque.setDataMovimento(new Date());
    	
    	Item item = new Item();
    	item.setPrecoProduto(getQuerier().findAny(PrecoProduto.class));
    	item.setQuantidade(10);
    	item.setMovimentoEstoque(entradaEstoque);
    	
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
