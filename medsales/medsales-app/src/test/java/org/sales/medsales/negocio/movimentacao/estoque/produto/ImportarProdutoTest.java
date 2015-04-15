package org.sales.medsales.negocio.movimentacao.estoque.produto;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.easy.qbeasy.QBEFilter;
import org.easy.qbeasy.api.operator.Operators;
import org.junit.Assert;
import org.junit.Test;
import org.sales.medsales.MedSalesBaseTest;
import org.sales.medsales.dominio.movimento.estoque.Produto;
import org.sales.medsales.web.converter.NumberUtils;

/**
 * Testa a importação de produtos e prelos.
 * @author Augusto
 *
 */
public class ImportarProdutoTest extends MedSalesBaseTest {

	/*
	 * OBS: A implementação desta funcionalidade utiliza um controle de transação customizado, fazendo uso
	 * de um REQUIRES_NEW para processar os produtos em batch. Isto afeta o comportamento do teste, que não consegue
	 * garantir rowback desta transação específica. Portanto, é importante lidar com os dados mais cuidadosamente
	 * para que não haja dependência entre os métodos de teste.
	 */
	
	@Inject
	ProdutoFacade produtoFacade;
	
	@Inject
	ImportarProdutoFacade importarProdutoFacade;
	
	/**
	 * Importa produtos que não existe na base de dados.
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@Test
	public void importarNovosProdutos() throws IOException, ParseException {
		
		String input = 
				"import1111111##AAS INF 20X10 COMP              	 ##68,52" + System.lineSeparator() + 
				"import2222222##AAS INF C/120 COMP              	 ##38,21" + System.lineSeparator() +
				"import3333333##AAS INF C/30 COMP               	 ##9,95"; 
		
		InputStream in = IOUtils.toInputStream(input, "UTF-8");
		importarProdutoFacade.importar(in);
		
		// verificando
		List<Produto> produtos = produtoFacade.findAll();
		Assert.assertTrue(!produtos.isEmpty());

		Produto produto = buscarProduto("import1111111");
		Assert.assertEquals(1, produto.getPrecos().size());
		Assert.assertEquals(NumberUtils.parseBigDecimal("68,52"), produto.getPrecos().get(0).getValor());
		
		produto = buscarProduto("import2222222");
		Assert.assertEquals(1, produto.getPrecos().size());
		Assert.assertEquals(NumberUtils.parseBigDecimal("38,21"), produto.getPrecos().get(0).getValor());
		
		produto = buscarProduto("import3333333");
		Assert.assertEquals(1, produto.getPrecos().size());
		Assert.assertEquals(NumberUtils.parseBigDecimal("9,95"), produto.getPrecos().get(0).getValor());
		
	}
	
	/**
	 * Importa preços de produtos já existentes.
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@Test
	public void importarNovosPrecosProdutosExistentes() throws IOException, ParseException {
		
		/*
		 * Cadastra 2 produtos como dados iniciais
		 */
		String input1 = 
				"import4444444##AAS INF 20X10 COMP              	 ##10,10" + System.lineSeparator() + 
				"import5555555##AAS INF C/120 COMP              	 ##20,20" + System.lineSeparator() +
				"import6666666##AAS INF C/30 COMP               	 ##30,30";
		
		InputStream in = IOUtils.toInputStream(input1, "UTF-8");
		importarProdutoFacade.importar(in);
		
		
		/*
		 * Simula uma nova importação, incrementando o preço de 2 produtos, mantendo 1 sem alteração, e adicionando um novo.
		 */
		String input2 = 
				"import4444444##AAS INF 20X10 COMP ALTERADO        	 ##11,10" + System.lineSeparator() + 
				"import5555555##AAS INF C/120 COMP              	 ##22,20" + System.lineSeparator() +
				"import6666666##AAS INF C/30 COMP               	 ##30,30" + System.lineSeparator() +
				"import7777777##NOVO PRODUTO                         ##40,40";
		
		in = IOUtils.toInputStream(input2, "UTF-8");
		importarProdutoFacade.importar(in);		
		
		// verificnado
		List<Produto> produtos = produtoFacade.findAll();
		Assert.assertTrue(!produtos.isEmpty());

		Produto produto = buscarProduto("import4444444");
		Assert.assertEquals(2, produto.getPrecos().size());
		Assert.assertEquals(NumberUtils.parseBigDecimal("11,10"), produto.getPrecoAtual().getValor());
		// nesse caso, o nome também foi alterado 
		Assert.assertTrue(produto.getNome().endsWith("ALTERADO"));
		
		produto = buscarProduto("import5555555");
		Assert.assertEquals(2, produto.getPrecos().size());
		Assert.assertEquals(NumberUtils.parseBigDecimal("22,20"), produto.getPrecoAtual().getValor());
		
		produto = buscarProduto("import6666666");
		Assert.assertEquals(1, produto.getPrecos().size());
		Assert.assertEquals(NumberUtils.parseBigDecimal("30,30"), produto.getPrecos().get(0).getValor());
		
		produto = buscarProduto("import7777777");
		Assert.assertEquals(1, produto.getPrecos().size());
		Assert.assertEquals(NumberUtils.parseBigDecimal("40,40"), produto.getPrecos().get(0).getValor());
		
	}

	private Produto buscarProduto(String codigoBarras) {
		
		QBEFilter<Produto> filter = new QBEFilter<>(Produto.class);
		filter.filterBy("codigoBarras", Operators.equal(), codigoBarras);
		return produtoFacade.findBy(filter);
		
	}

}
