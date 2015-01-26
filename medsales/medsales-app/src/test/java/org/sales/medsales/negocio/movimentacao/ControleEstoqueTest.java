package org.sales.medsales.negocio.movimentacao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.easy.testeasy.dataloader.LoadData;
import org.easy.testeasy.dataloader.LoadDatas;
import org.junit.Assert;
import org.junit.Test;
import org.sales.medsales.MedSalesBaseTest;
import org.sales.medsales.api.exceptions.NullParameterException;
import org.sales.medsales.dataLoader.PrecoProdutoDataLoader;
import org.sales.medsales.dataLoader.ProdutosDataLoader;
import org.sales.medsales.dominio.Item;
import org.sales.medsales.dominio.Produto;
import org.sales.medsales.dominio.SaldoProdutoVO;
import org.sales.medsales.dominio.movimentacao.Entrada;
import org.sales.medsales.dominio.movimentacao.Saida;
import org.sales.medsales.dominio.movimentacao.Status;
import org.sales.medsales.exceptions.ExceptionCodes;

/**
 * Teste de controle de estoque diante das operações de entrada e saída.
 * @author Augusto
 */
public class ControleEstoqueTest extends MedSalesBaseTest {

	@Inject
	private EstoqueFacade estoqueFacade;

	/**
	 * Consulta o saldo sem informar um produto
	 */
	@Test
	@LoadData(dataLoader=ProdutosDataLoader.class)
	public void consultaSaldoProdutoNullTest() {
		
		try {
			estoqueFacade.consultarEstoque((Produto)null);
			Assert.fail();
		} catch (NullParameterException e) {
			Assert.assertTrue(e.hasCode(ExceptionCodes.MOVIMENTACAO.SALDO_PRODUTO_REQUIRED));
		}
			
	}

	/**
	 * Consulta o saldo de um produto quando não há nenhum item cadastrado
	 * no estoque.
	 */
	@Test
	@LoadData(dataLoader=ProdutosDataLoader.class)
	public void consultaSaldoVazioTest() {
		Produto produto = getQuerier().findAt(Produto.class, 0);
		SaldoProdutoVO saldoProduto = estoqueFacade.consultarEstoque(produto);

		Assert.assertNotNull(saldoProduto);
		Assert.assertEquals(produto.getId(), saldoProduto.getIdProduto());
		Assert.assertEquals(0L, saldoProduto.getQuantidade().longValue());
	}
	
	/**
	 * Consulta o saldo de um produto após o cadastro de uma entrada no estoque.
	 */
	@Test
	@LoadDatas ({
		@LoadData(dataLoader=ProdutosDataLoader.class),
		@LoadData(dataLoader=PrecoProdutoDataLoader.class)
	})
	public void consultaSaldoEntradaProdutoTest() {
		// cadastra uma entrada para um produto
		Produto produto = getQuerier().findAt(Produto.class, 0);
		
    	cadastrarEntrada(produto, 10);

    	// verifica o saldo
		SaldoProdutoVO saldoProduto = estoqueFacade.consultarEstoque(produto);

		Assert.assertNotNull(saldoProduto);
		Assert.assertEquals(produto.getId(), saldoProduto.getIdProduto());
		Assert.assertEquals(10L, saldoProduto.getQuantidade().longValue());
	}

	private Entrada cadastrarEntrada(Produto produto, int quantidade) {
		Entrada entrada = new Entrada();
    	entrada.setDataMovimentacao(new Date());
    	
    	Item item = new Item();
		item.setProduto(produto);
    	item.setQuantidade(quantidade);
    	item.setMovimentacaoEstoque(entrada);
    	
    	entrada.setItens(Arrays.asList(item));
    	entrada.setStatus(Status.CONCLUIDO);
    	
    	estoqueFacade.cadastrar(entrada);
    	
    	return entrada;
	}
	
	/**
	 * Consulta o saldo de um produto após operações de entrada e saída, restando ainda produtos no estoque.
	 */
	@Test
	@LoadDatas ({
		@LoadData(dataLoader=ProdutosDataLoader.class),
		@LoadData(dataLoader=PrecoProdutoDataLoader.class)
	})
	public void consultaSaldoEntradaSaidaProdutoTest() {
		// cadastra uma entrada e saida para um produto
		Produto produto = getQuerier().findAt(Produto.class, 0);
		
    	cadastrarEntrada(produto, 10);
    	
    	Saida saida = new Saida();
    	
    	Item item = new Item();
		item.setProduto(produto);
    	item.setQuantidade(5);
    	item.setMovimentacaoEstoque(saida);
    	
    	saida.setItens(Arrays.asList(item));
    	saida.setStatus(Status.CONCLUIDO);
    	
    	estoqueFacade.cadastrar(saida);
    	
    	// verifica o saldo
		SaldoProdutoVO saldoProduto = estoqueFacade.consultarEstoque(produto);

		Assert.assertNotNull(saldoProduto);
		Assert.assertEquals(produto.getId(), saldoProduto.getIdProduto());
		Assert.assertEquals(5L, saldoProduto.getQuantidade().longValue());
	}
	
	/**
	 * Consulta o saldo de um produto após operações de entrada e saída, zerando o estoque.
	 */
	@Test
	@LoadDatas ({
		@LoadData(dataLoader=ProdutosDataLoader.class),
		@LoadData(dataLoader=PrecoProdutoDataLoader.class)
	})
	public void consultaSaldoEntradaSaidaProdutoZeradoTest() {
		// cadastra uma entrada e saida para um produto
		Produto produto = getQuerier().findAt(Produto.class, 0);
		
    	cadastrarEntrada(produto, 10);
    	
    	Saida saida = new Saida();
    	
    	Item item = new Item();
		item.setProduto(produto);
    	item.setQuantidade(10);
    	item.setMovimentacaoEstoque(saida);
    	
    	saida.setItens(Arrays.asList(item));
    	saida.setStatus(Status.CONCLUIDO);
    	
    	estoqueFacade.cadastrar(saida);
    	
    	// verifica o saldo
		SaldoProdutoVO saldoProduto = estoqueFacade.consultarEstoque(produto);

		Assert.assertNotNull(saldoProduto);
		Assert.assertEquals(produto.getId(), saldoProduto.getIdProduto());
		Assert.assertEquals(0L, saldoProduto.getQuantidade().longValue());
	}	
	
	/**
	 * Consulta o saldo de um produto após operações de entrada e saída, negativando o estoque.
	 */
	@Test
	@LoadDatas ({
		@LoadData(dataLoader=ProdutosDataLoader.class),
		@LoadData(dataLoader=PrecoProdutoDataLoader.class)
	})
	public void consultaSaldoEntradaSaidaProdutoNegativoTest() {
		// cadastra uma entrada e saida para um produto
		Produto produto = getQuerier().findAt(Produto.class, 0);
		
    	cadastrarEntrada(produto, 10);
    	
    	Saida saida = new Saida();
    	
    	Item item = new Item();
		item.setProduto(produto);
    	item.setQuantidade(15);
    	item.setMovimentacaoEstoque(saida);
    	
    	saida.setItens(Arrays.asList(item));
    	saida.setStatus(Status.CONCLUIDO);
    	
    	estoqueFacade.cadastrar(saida);
    	
    	// verifica o saldo
		SaldoProdutoVO saldoProduto = estoqueFacade.consultarEstoque(produto);

		Assert.assertNotNull(saldoProduto);
		Assert.assertEquals(produto.getId(), saldoProduto.getIdProduto());
		Assert.assertEquals(-5L, saldoProduto.getQuantidade().longValue());
	}
	
	/**
	 * Consulta o saldo sem informar um produto
	 */
	@Test
	@LoadData(dataLoader=ProdutosDataLoader.class)
	public void consultaSaldoProdutosNullTest() {
		
		try {
			estoqueFacade.consultarEstoque((Produto[]) null);
			Assert.fail();
		} catch (NullParameterException e) {
			Assert.assertTrue(e.hasCode(ExceptionCodes.MOVIMENTACAO.SALDO_PRODUTO_REQUIRED));
		}
			
	}

	/**
	 * Consulta o saldo de um produto quando não há nenhum item cadastrado
	 * no estoque.
	 */
	@Test
	@LoadData(dataLoader=ProdutosDataLoader.class)
	public void consultaSaldoProdutosVazioTest() {
		Produto produto1 = getQuerier().findAt(Produto.class, 0);
		Produto produto2 = getQuerier().findAt(Produto.class, 1);
		
		List<SaldoProdutoVO> saldos = estoqueFacade.consultarEstoque(produto1, produto2);

		List<SaldoProdutoVO> resultadoEsperado = new ArrayList<SaldoProdutoVO>();
		resultadoEsperado.add(new SaldoProdutoVO(produto1.getId(), 0L));
		resultadoEsperado.add(new SaldoProdutoVO(produto2.getId(), 0L));
		
		Assert.assertNotNull(saldos);
		assertContentEqual(resultadoEsperado, saldos);
	}
}
