package org.sales.medsales.negocio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.sales.medsales.api.test.OnServerBaseTest;
import org.sales.medsales.dominio.Entrada;
import org.sales.medsales.dominio.Item;
import org.sales.medsales.dominio.PrecoProduto;
import org.sales.medsales.dominio.Produto;
import org.sales.medsales.exceptions.RemoverProdutoComMovimentacaoException;

public class ProdutoFacadeTest extends OnServerBaseTest {

	@Inject
	private ProdutoFacade produtoFacade;
	
	@Inject
	private EstoqueFacade estoqueFacade;
	
	@Test
	public void inserirSimples() {
    	Produto produto = criarProduto();
    	getEm().clear();

    	// verificando insert
    	Produto found = produtoFacade.findBy(produto.getId());
    	Assert.assertNotNull(found);
	}
	
	@Test
	public void inserirProdutoComPreco() {
    	Produto produto = criarProduto();
    	criarPrecoProduto(produto);
    	
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
    	criarPrecoProduto(produto, 10D);
    	
    	getEm().clear();

    	// cria um novo preco com mesmo valor do anterior
    	criarPrecoProduto(produto, 10D);
    	
    	// verificando insert e se não foi inserido um preco duplicado
    	Produto produtoEncontrado = produtoFacade.findBy(produto.getId(), "precos");
    	Assert.assertNotNull(produtoEncontrado);
    	Assert.assertNotNull(produtoEncontrado.getPrecos());
    	Assert.assertTrue(produtoEncontrado.getPrecos().size() == 1);
	}
	
	@Test
	public void inserirProdutoComPrecoAlterado() {
    	Produto produto = criarProduto();
    	criarPrecoProduto(produto, 10D);
    	
    	getEm().clear();

    	// cria um novo preco com mesmo valor do anterior
    	criarPrecoProduto(produto, 20D);
    	
    	// verificando insert e se não foi inserido um preco duplicado
    	Produto produtoEncontrado = produtoFacade.findBy(produto.getId(), "precos");
    	Assert.assertNotNull(produtoEncontrado);
    	Assert.assertNotNull(produtoEncontrado.getPrecos());
    	Assert.assertTrue(produtoEncontrado.getPrecos().size() == 2);
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
    	produtoFacade.save(produto);
		return produto;
	}
    
    @Test
    public void removerComDependenciaPrecoPooduto() {

    	Produto produto = criarProduto();
    	criarPrecoProduto(produto);
    	
    	// limpando o cache para verificação
    	getEm().clear();

   		produtoFacade.remove(produto);
    	
    	// verificando que o produto foi removido, bem como o preço
    	getEm().clear();
    	Produto produtoEncontrado = produtoFacade.findBy(produto.getId());
    	Assert.assertNull(produtoEncontrado);
    }

	private PrecoProduto criarPrecoProduto(Produto produto, Double valor) {
		PrecoProduto preco =  new PrecoProduto();
    	preco.setProduto(produto);
    	preco.setValidoEm(new Date());
    	preco.setValor(new BigDecimal(valor).setScale(2)); // setScale equivalente ao mapeamento jpa
    	
    	if (produto.getPrecos() == null) {
    		produto.setPrecos(new ArrayList<PrecoProduto>());
    	}
    	
    	produto.getPrecos().add(preco);
    	
    	produtoFacade.save(produto, preco);
    	
    	return preco;
	}
	
	private PrecoProduto criarPrecoProduto(Produto produto) {
    	return criarPrecoProduto(produto, 10D);
	}

    @Test(expected=RemoverProdutoComMovimentacaoException.class)
    public void removerComDependenciaMovimentacao() {
    	
    	Produto produto = criarProduto();
    	criarPrecoProduto(produto);
    	
    	// cadastrando uma movimentacao
    	Entrada entrada = new Entrada();
    	
    	Item item = new Item();
    	item.setProduto(produto);
    	item.setQuantidade(10);
    	item.setTipoMovimentacao(entrada);
    	
    	entrada.setItens(Arrays.asList(item));
    	
    	estoqueFacade.cadastrarEntrada(entrada);
    	
    	getEm().clear();

    	// tentando remover o produto
   		produtoFacade.remove(produto);
    
    	// verificando se o produto ainda existe
    	Produto produtoEncontrado = produtoFacade.findBy(produto.getId());
    	Assert.assertNotNull(produtoEncontrado);
    }
}
