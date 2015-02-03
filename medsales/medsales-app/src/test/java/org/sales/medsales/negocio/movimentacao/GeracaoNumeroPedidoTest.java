package org.sales.medsales.negocio.movimentacao;

import java.util.Arrays;
import java.util.Date;

import javax.inject.Inject;

import junit.framework.Assert;

import org.easy.testeasy.dataloader.LoadData;
import org.easy.testeasy.dataloader.LoadDatas;
import org.junit.Test;
import org.sales.medsales.MedSalesBaseTest;
import org.sales.medsales.dataLoader.ParceirosDataLoader;
import org.sales.medsales.dataLoader.ProdutosDataLoader;
import org.sales.medsales.dominio.Item;
import org.sales.medsales.dominio.Parceiro;
import org.sales.medsales.dominio.Produto;
import org.sales.medsales.dominio.movimentacao.Saida;
import org.sales.medsales.dominio.movimentacao.Status;

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
		@LoadData(dataLoader=ProdutosDataLoader.class),
		@LoadData(dataLoader=ParceirosDataLoader.class)
	})
	public void cadastrarSaidaConcluidaTest() {
    	
    	Saida saida = new Saida();
    	saida.setParceiro(getQuerier().findAny(Parceiro.class));
    	saida.setDataMovimentacao(new Date());
    	
    	Item item = new Item();
    	item.setProduto(getQuerier().findAny(Produto.class));
    	item.setQuantidade(10);
    	item.setMovimentacaoEstoque(saida);
    	
    	saida.setItens(Arrays.asList(item));
    	saida.setStatus(Status.CONCLUIDO);
    	
    	estoqueFacade.cadastrar(saida);
    	
    	getEm().clear();
    	
    	// verifica se os dados foram inseridos
    	saida = getQuerier().find(Saida.class, saida.getId());
    	Assert.assertNotNull(saida.getNumeroPedido());
    	Assert.assertEquals((int)1, (int)saida.getNumeroPedido());
	}
	
	/**
	 * Cadastrada uma saída já com status Rasunho, e espera que o número NÃO seja criado.
	 */
	@Test
	@LoadDatas ({
		@LoadData(dataLoader=ProdutosDataLoader.class),
		@LoadData(dataLoader=ParceirosDataLoader.class)
	})
	public void cadastrarSaidaRascunhoTest() {
    	
    	Saida saida = new Saida();
    	saida.setDataMovimentacao(new Date());
    	saida.setParceiro(getQuerier().findAny(Parceiro.class));
    	
    	Item item = new Item();
    	item.setProduto(getQuerier().findAny(Produto.class));
    	item.setQuantidade(10);
    	item.setMovimentacaoEstoque(saida);
    	
    	saida.setItens(Arrays.asList(item));
    	saida.setStatus(Status.RASCUNHO);
    	
    	estoqueFacade.cadastrar(saida);
    	
    	getEm().clear();
    	
    	// verifica se os dados foram inseridos
    	saida = getQuerier().find(Saida.class, saida.getId());
    	Assert.assertNull(saida.getNumeroPedido());
	}
	
	/**
	 * Cadastrada duas saídas para o mesmo Cliente, e espera-se o incremento do número do pedido.
	 */
	@Test
	@LoadDatas ({
		@LoadData(dataLoader=ProdutosDataLoader.class),
		@LoadData(dataLoader=ParceirosDataLoader.class)
	})
	public void cadastrarDuasSaidasMesmoClienteTest() {
    	
		Parceiro parceiro = getQuerier().findAny(Parceiro.class);
		
		// saida 1
    	Saida saida1 = new Saida();
    	saida1.setParceiro(parceiro);
    	saida1.setDataMovimentacao(new Date());
    	
    	Item item = new Item();
    	item.setProduto(getQuerier().findAt(Produto.class, 0));
    	item.setQuantidade(10);
    	item.setMovimentacaoEstoque(saida1);
    	
    	saida1.setItens(Arrays.asList(item));
    	saida1.setStatus(Status.CONCLUIDO);
    	
    	estoqueFacade.cadastrar(saida1);
    	
    	// saida 2
    	Saida saida2 = new Saida();
    	saida2.setParceiro(parceiro);
    	saida2.setDataMovimentacao(new Date());
    	
    	item = new Item();
    	item.setProduto(getQuerier().findAt(Produto.class, 1));
    	item.setQuantidade(15);
    	item.setMovimentacaoEstoque(saida2);
    	
    	saida2.setItens(Arrays.asList(item));
    	saida2.setStatus(Status.CONCLUIDO);
    	
    	estoqueFacade.cadastrar(saida2);
    	
    	getEm().clear();
    	
    	// verifica se os dados foram inseridos
    	Saida saida = getQuerier().find(Saida.class, saida1.getId());
    	Assert.assertNotNull(saida.getNumeroPedido());
    	Assert.assertEquals((int)1, (int)saida.getNumeroPedido());
    	
    	saida = getQuerier().find(Saida.class, saida2.getId());
    	Assert.assertNotNull(saida.getNumeroPedido());
    	Assert.assertEquals((int)2, (int)saida.getNumeroPedido());
	}
	
	/**
	 * Cadastrada duas saídas para o clientes diferentes, e espera-se o número do pedido = 1 em ambas.
	 */
	@Test
	@LoadDatas ({
		@LoadData(dataLoader=ProdutosDataLoader.class),
		@LoadData(dataLoader=ParceirosDataLoader.class)
	})
	public void cadastrarDuasSaidasClienteDiferenteTest() {
    	
		Parceiro parceiro1 = getQuerier().findAt(Parceiro.class, 0);
		Parceiro parceiro2 = getQuerier().findAt(Parceiro.class, 1);
		
		// saida 1
    	Saida saida1 = new Saida();
    	saida1.setParceiro(parceiro1);
    	saida1.setDataMovimentacao(new Date());
    	
    	Item item = new Item();
    	item.setProduto(getQuerier().findAt(Produto.class, 0));
    	item.setQuantidade(10);
    	item.setMovimentacaoEstoque(saida1);
    	
    	saida1.setItens(Arrays.asList(item));
    	saida1.setStatus(Status.CONCLUIDO);
    	
    	estoqueFacade.cadastrar(saida1);
    	
    	// saida 2
    	Saida saida2 = new Saida();
    	saida2.setParceiro(parceiro2);
    	saida2.setDataMovimentacao(new Date());
    	
    	item = new Item();
    	item.setProduto(getQuerier().findAt(Produto.class, 1));
    	item.setQuantidade(15);
    	item.setMovimentacaoEstoque(saida2);
    	
    	saida2.setItens(Arrays.asList(item));
    	saida2.setStatus(Status.CONCLUIDO);
    	
    	estoqueFacade.cadastrar(saida2);
    	
    	getEm().clear();
    	
    	// verifica se os dados foram inseridos
    	Saida saida = getQuerier().find(Saida.class, saida1.getId());
    	Assert.assertNotNull(saida.getNumeroPedido());
    	Assert.assertEquals((int)1, (int)saida.getNumeroPedido());
    	
    	saida = getQuerier().find(Saida.class, saida2.getId());
    	Assert.assertNotNull(saida.getNumeroPedido());
    	Assert.assertEquals((int)1, (int)saida.getNumeroPedido());
	}

}
