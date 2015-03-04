package org.sales.medsales.negocio.movimentacao.estoque;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.inject.Inject;

import junit.framework.Assert;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.sales.medsales.MedSalesBaseTest;
import org.sales.medsales.dominio.movimento.estoque.EntradaEstoque;
import org.sales.medsales.dominio.movimento.estoque.Item;
import org.sales.medsales.dominio.movimento.estoque.PrecoProduto;
import org.sales.medsales.dominio.movimento.estoque.Produto;
import org.sales.medsales.dominio.movimento.estoque.Status;
import org.sales.medsales.exceptions.ProdutoCodBarrasJaExisteException;
import org.sales.medsales.exceptions.ProdutoSemPrecoException;
import org.sales.medsales.exceptions.RemoverProdutoComMovimentacaoException;
import org.sales.medsales.negocio.movimentacao.estoque.EstoqueFacade;
import org.sales.medsales.negocio.movimentacao.estoque.ProdutoFacade;

public class ProdutoFacadeTest extends MedSalesBaseTest {

	private static final double PRECO_10 = 10D;

	@Inject
	private ProdutoFacade produtoFacade;

	@Inject
	private EstoqueFacade estoqueFacade;

	@Test(expected = ProdutoSemPrecoException.class)
	public void inserirSimples() {
		Produto produto = new Produto();
		produto.setNome("Produto Teste");
		produto.setCodigoBarras("00000");

		produtoFacade.save(produto);
	}

	@Test
	public void inserirProdutoComPreco() {
		Produto produto = criarProduto();
		getEm().clear();

		// verificando insert
		Produto produtoEncontrado = produtoFacade.findBy(produto.getId());
		Assert.assertNotNull(produtoEncontrado);
		Assert.assertNotNull(produtoEncontrado.getPrecos());
		Assert.assertTrue(produtoEncontrado.getPrecos().size() == 1);

	}

	@Test
	public void inserirProdutoComPrecoNaoAlterado() {
		Produto produto = criarProduto();

		getEm().clear();

		// cria um novo preco com mesmo valor do anterior
		adicionarSalvarPrecoProduto(produto, PRECO_10);

		// verificando insert e se não foi inserido um preco duplicado
		Produto produtoEncontrado = produtoFacade.findBy(produto.getId(), "precos");
		Assert.assertNotNull(produtoEncontrado);
		Assert.assertNotNull(produtoEncontrado.getPrecos());
		Assert.assertTrue(produtoEncontrado.getPrecos().size() == 1);
	}

	@Test
	public void inserirProdutoComPrecoAlterado() {
		Produto produto = criarProduto();

		getEm().clear();

		// cria um novo preco com mesmo valor do anterior
		adicionarSalvarPrecoProduto(produto, PRECO_10 + 10);

		// verificando insert e se não foi inserido um preco duplicado
		Produto produtoEncontrado = produtoFacade.findBy(produto.getId(), "precos");
		Assert.assertNotNull(produtoEncontrado);
		Assert.assertNotNull(produtoEncontrado.getPrecos());
		Assert.assertTrue(produtoEncontrado.getPrecos().size() == 2);
	}

	@Test(expected = ProdutoCodBarrasJaExisteException.class)
	public void inserirProdutoComCodigoBarrasDuplicado() {
		Produto produto = new Produto();
		produto.setNome("Produto Teste");
		produto.setCodigoBarras("00000");
		adicionarPrecoProduto(produto, PRECO_10);

		produtoFacade.save(produto);

		getEm().clear();

		produto = new Produto();
		produto.setNome("Outro Produto");
		produto.setCodigoBarras("00000");
		adicionarPrecoProduto(produto, PRECO_10);

		produtoFacade.save(produto);
	}

	@Test
	public void removerSimples() {

		Produto produto = criarProduto();
		getEm().clear();

		produtoFacade.remove(produto);
		getEm().clear();

		Produto found = produtoFacade.findBy(produto.getId());
		Assert.assertNull(found);
	}

	private Produto criarProduto() {
		// cadastrando um produto básico.
		Produto produto = new Produto();
		produto.setNome("Produto Teste");
		produto.setCodigoBarras("00000");
		adicionarPrecoProduto(produto, PRECO_10);

		produtoFacade.save(produto);
		return produto;
	}

	@Test
	public void removerComDependenciaPrecoPooduto() {

		Produto produto = criarProduto();

		// limpando o cache para verificação
		getEm().clear();

		produtoFacade.remove(produto);

		// verificando que o produto foi removido, bem como o preço
		getEm().clear();
		Produto produtoEncontrado = produtoFacade.findBy(produto.getId());
		Assert.assertNull(produtoEncontrado);
	}

	private PrecoProduto adicionarSalvarPrecoProduto(Produto produto, Double valor) {
		PrecoProduto preco = adicionarPrecoProduto(produto, valor);
		produtoFacade.save(produto, preco);
		return preco;
	}

	private PrecoProduto adicionarPrecoProduto(Produto produto, Double valor) {
		return adicionarPrecoProduto(produto, new Date(), valor);
	}

	private PrecoProduto adicionarPrecoProduto(Produto produto, Date validoEm, Double valor) {
		PrecoProduto preco = new PrecoProduto();
		preco.setProduto(produto);
		preco.setValidoEm(validoEm);
		preco.setValor(new BigDecimal(valor).setScale(2)); // setScale
															// equivalente ao
															// mapeamento jpa

		if (produto.getPrecos() == null) {
			produto.setPrecos(new ArrayList<PrecoProduto>());
		}

		produto.getPrecos().add(preco);
		return preco;
	}

	@Test(expected = RemoverProdutoComMovimentacaoException.class)
	public void removerComDependenciaMovimentacao() {

		Produto produto = criarProduto();

		// cadastrando uma movimentacao
		EntradaEstoque entradaEstoque = new EntradaEstoque();
		entradaEstoque.setDataMovimento(new Date());

		Item item = new Item();
		item.setPrecoProduto(produto.getPrecos().get(0));
		item.setQuantidade(10);
		item.setMovimentoEstoque(entradaEstoque);

		entradaEstoque.setItens(Arrays.asList(item));
		entradaEstoque.setStatus(Status.CONCLUIDO);

		estoqueFacade.salvar(entradaEstoque);

		getEm().clear();

		// tentando remover o produto
		produtoFacade.remove(produto);

		// verificando se o produto ainda existe
		Produto produtoEncontrado = produtoFacade.findBy(produto.getId());
		Assert.assertNotNull(produtoEncontrado);
	}

	@Test
	public void buscarPrecoProdutoInexistente() {
		PrecoProduto precoProduto = produtoFacade.buscarPrecoProduto("inexistente");
		Assert.assertNull(precoProduto);
	}

	@Test
	public void buscarPrecoProdutoMaisRecente() throws ParseException {

		/*
		 * Cadastra um produto com um único preço
		 */
		Produto produto = new Produto();
		produto.setNome("Produto Teste");
		produto.setCodigoBarras("00000");

		Date data1 = DateUtils.parseDate("01/01/2010", new String[] { "dd/MM/yyyy" });
		PrecoProduto preco1 = adicionarPrecoProduto(produto, data1, PRECO_10);

		produtoFacade.save(produto, preco1);

		getEm().clear();

		PrecoProduto precoProduto = produtoFacade.buscarPrecoProduto("00000");
		Assert.assertNotNull(precoProduto);
		Assert.assertTrue(DateUtils.isSameDay(data1, precoProduto.getValidoEm()));

		/*
		 * Adiciona um novo preco ao produto, mais recente, com preco diferente
		 * para não ser descartado
		 */
		Date data2 = DateUtils.parseDate("01/02/2010", new String[] { "dd/MM/yyyy" });
		PrecoProduto preco2 = adicionarPrecoProduto(produto, data2, PRECO_10 + 10);
		produtoFacade.save(produto, preco2);

		precoProduto = produtoFacade.buscarPrecoProduto("00000");
		Assert.assertNotNull(precoProduto);
		Assert.assertTrue(DateUtils.isSameDay(data2, precoProduto.getValidoEm()));
	}
}
