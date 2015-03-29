package org.sales.medsales.negocio.movimentacao.estoque;

import java.util.Arrays;
import java.util.Date;

import javax.inject.Inject;

import junit.framework.Assert;

import org.easy.testeasy.dataloader.LoadData;
import org.easy.testeasy.dataloader.LoadDatas;
import org.junit.Test;
import org.sales.medsales.MedSalesBaseTest;
import org.sales.medsales.dataLoader.ParceirosDataLoader;
import org.sales.medsales.dataLoader.PrecoProdutoDataLoader;
import org.sales.medsales.dataLoader.ProdutosDataLoader;
import org.sales.medsales.dominio.Parceiro;
import org.sales.medsales.dominio.movimento.estoque.Item;
import org.sales.medsales.dominio.movimento.estoque.PrecoProduto;
import org.sales.medsales.dominio.movimento.estoque.SaidaEstoque;
import org.sales.medsales.dominio.movimento.estoque.Status;

/**
 * Testes para geração de número de pedidos em Saida.
 * @author Augusto
 *@see SaidaListener
 */
public class GeracaoNumeroPedidoTest extends MedSalesBaseTest {

	@Inject
	private EstoqueFacade estoqueFacade;
	
	/**
	 * Cadastrada uma saída já com status Concluído, e espera que o número seja criado.
	 */
	@Test
	@LoadDatas ({
		@LoadData(dataLoader={ProdutosDataLoader.class, PrecoProdutoDataLoader.class}),
		@LoadData(dataLoader=ParceirosDataLoader.class)
	})
	public void cadastrarSaidaConcluidaTest() {
    	
    	SaidaEstoque saidaEstoque = new SaidaEstoque();
    	saidaEstoque.setParceiro(getQuerier().findAny(Parceiro.class));
    	saidaEstoque.setDataMovimento(new Date());
    	
    	Item item = new Item();
    	item.setPrecoProduto(getQuerier().findAny(PrecoProduto.class));
    	item.setQuantidade(10);
    	item.setMovimentoEstoque(saidaEstoque);
    	
    	saidaEstoque.setItens(Arrays.asList(item));
    	saidaEstoque.setStatus(Status.CONCLUIDO);
    	
    	estoqueFacade.salvar(saidaEstoque);
    	
    	getEm().clear();
    	
    	// verifica se os dados foram inseridos
    	saidaEstoque = getQuerier().find(SaidaEstoque.class, saidaEstoque.getId());
    	Assert.assertNotNull(saidaEstoque.getNumeroPedido());
    	Assert.assertEquals("1", saidaEstoque.getNumeroPedido());
	}
	
	/**
	 * Cadastrada uma saída já com status Rasunho, e espera que o número NÃO seja criado.
	 */
	@Test
	@LoadDatas ({
		@LoadData(dataLoader={ProdutosDataLoader.class, PrecoProdutoDataLoader.class}),
		@LoadData(dataLoader=ParceirosDataLoader.class)
	})
	public void cadastrarSaidaRascunhoTest() {
    	
    	SaidaEstoque saidaEstoque = new SaidaEstoque();
    	saidaEstoque.setDataMovimento(new Date());
    	saidaEstoque.setParceiro(getQuerier().findAny(Parceiro.class));
    	
    	Item item = new Item();
    	item.setPrecoProduto(getQuerier().findAny(PrecoProduto.class));
    	item.setQuantidade(10);
    	item.setMovimentoEstoque(saidaEstoque);
    	
    	saidaEstoque.setItens(Arrays.asList(item));
    	saidaEstoque.setStatus(Status.RASCUNHO);
    	
    	estoqueFacade.salvar(saidaEstoque);
    	
    	getEm().clear();
    	
    	// verifica se os dados foram inseridos
    	saidaEstoque = getQuerier().find(SaidaEstoque.class, saidaEstoque.getId());
    	Assert.assertNull(saidaEstoque.getNumeroPedido());
	}
	
	/**
	 * Cadastrada duas saídas para o mesmo Cliente, e espera-se o incremento do número do pedido.
	 */
	@Test
	@LoadDatas ({
		@LoadData(dataLoader={ProdutosDataLoader.class, PrecoProdutoDataLoader.class}),
		@LoadData(dataLoader=ParceirosDataLoader.class)
	})
	public void cadastrarDuasSaidasMesmoClienteTest() {
    	
		Parceiro parceiro = getQuerier().findAny(Parceiro.class);
		
		// saida 1
    	SaidaEstoque saida1 = new SaidaEstoque();
    	saida1.setParceiro(parceiro);
    	saida1.setDataMovimento(new Date());
    	
    	Item item = new Item();
    	item.setPrecoProduto(getQuerier().findAt(PrecoProduto.class, 0));
    	item.setQuantidade(10);
    	item.setMovimentoEstoque(saida1);
    	
    	saida1.setItens(Arrays.asList(item));
    	saida1.setStatus(Status.CONCLUIDO);
    	
    	estoqueFacade.salvar(saida1);
    	
    	// saida 2
    	SaidaEstoque saida2 = new SaidaEstoque();
    	saida2.setParceiro(parceiro);
    	saida2.setDataMovimento(new Date());
    	
    	item = new Item();
    	item.setPrecoProduto(getQuerier().findAt(PrecoProduto.class, 1));
    	item.setQuantidade(15);
    	item.setMovimentoEstoque(saida2);
    	
    	saida2.setItens(Arrays.asList(item));
    	saida2.setStatus(Status.CONCLUIDO);
    	
    	estoqueFacade.salvar(saida2);
    	
    	getEm().clear();
    	
    	// verifica se os dados foram inseridos
    	SaidaEstoque saidaEstoque = getQuerier().find(SaidaEstoque.class, saida1.getId());
    	Assert.assertNotNull(saidaEstoque.getNumeroPedido());
    	Assert.assertEquals("1", saidaEstoque.getNumeroPedido());
    	
    	saidaEstoque = getQuerier().find(SaidaEstoque.class, saida2.getId());
    	Assert.assertNotNull(saidaEstoque.getNumeroPedido());
    	Assert.assertEquals("2", saidaEstoque.getNumeroPedido());
	}
	
	/**
	 * Cadastrada duas saídas para o clientes diferentes, e espera-se o número do pedido = 1 em ambas.
	 */
	@Test
	@LoadDatas ({
		@LoadData(dataLoader={ProdutosDataLoader.class, PrecoProdutoDataLoader.class}),
		@LoadData(dataLoader=ParceirosDataLoader.class)
	})
	public void cadastrarDuasSaidasClienteDiferenteTest() {
    	
		Parceiro parceiro1 = getQuerier().findAt(Parceiro.class, 0);
		Parceiro parceiro2 = getQuerier().findAt(Parceiro.class, 1);
		
		// saida 1
    	SaidaEstoque saida1 = new SaidaEstoque();
    	saida1.setParceiro(parceiro1);
    	saida1.setDataMovimento(new Date());
    	
    	Item item = new Item();
    	item.setPrecoProduto(getQuerier().findAt(PrecoProduto.class, 0));
    	item.setQuantidade(10);
    	item.setMovimentoEstoque(saida1);
    	
    	saida1.setItens(Arrays.asList(item));
    	saida1.setStatus(Status.CONCLUIDO);
    	
    	estoqueFacade.salvar(saida1);
    	
    	// saida 2
    	SaidaEstoque saida2 = new SaidaEstoque();
    	saida2.setParceiro(parceiro2);
    	saida2.setDataMovimento(new Date());
    	
    	item = new Item();
    	item.setPrecoProduto(getQuerier().findAt(PrecoProduto.class, 1));
    	item.setQuantidade(15);
    	item.setMovimentoEstoque(saida2);
    	
    	saida2.setItens(Arrays.asList(item));
    	saida2.setStatus(Status.CONCLUIDO);
    	
    	estoqueFacade.salvar(saida2);
    	
    	getEm().clear();
    	
    	// verifica se os dados foram inseridos
    	SaidaEstoque saidaEstoque = getQuerier().find(SaidaEstoque.class, saida1.getId());
    	Assert.assertNotNull(saidaEstoque.getNumeroPedido());
    	Assert.assertEquals("1", saidaEstoque.getNumeroPedido());
    	
    	saidaEstoque = getQuerier().find(SaidaEstoque.class, saida2.getId());
    	Assert.assertNotNull(saidaEstoque.getNumeroPedido());
    	Assert.assertEquals("1", saidaEstoque.getNumeroPedido());
	}

}
