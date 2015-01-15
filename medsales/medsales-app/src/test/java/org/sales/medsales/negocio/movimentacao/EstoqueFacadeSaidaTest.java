package org.sales.medsales.negocio.movimentacao;

import java.util.Arrays;
import java.util.Date;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.sales.medsales.api.exceptions.NullParameterException;
import org.sales.medsales.api.test.InServerBaseTest;
import org.sales.medsales.dataLoader.ProdutosDataLoader;
import org.sales.medsales.dominio.Item;
import org.sales.medsales.dominio.Produto;
import org.sales.medsales.dominio.movimentacao.Saida;
import org.sales.medsales.dominio.movimentacao.Status;
import org.sales.medsales.exceptions.ExceptionCodes;
import org.sales.medsales.exceptions.MovimentacaoSemItensException;

/**
 * Testa as operações de saída do estoque.
 * @author Augusto
 */
public class EstoqueFacadeSaidaTest extends InServerBaseTest {

	@Inject
	private EstoqueFacade estoqueFacade;
	
	@Inject
	private ProdutosDataLoader produtosDataLoader;
	
	@Test
	public void cadastrarSaidaNull() {
		try {
			estoqueFacade.cadastrar((Saida)null);
			Assert.fail();
		} catch (NullParameterException e) {
			Assert.assertTrue(e.hasCode(ExceptionCodes.MOVIMENTACAO.MOVIMENTACAO_REQUIRED));
		}
	}
	
	/**
	 * Garante a validação dos parâmetros de saída
	 */
    @Test
    public void cadastrarEntradaOperacaoNull() {
    	Saida saida = new Saida();
    	saida.setOperacao(null);
    	saida.setDataMovimentacao(new Date());
    	
		try {
			estoqueFacade.cadastrar(saida);
			Assert.fail();
		} catch (NullParameterException e) {
			Assert.assertTrue(e.hasCode(ExceptionCodes.MOVIMENTACAO.OPERACAO_REQUIRED));
		}
    }
    
	/**
	 * Garante a validação dos parâmetros de saída
	 */
    @Test(expected=MovimentacaoSemItensException.class)
    public void cadastrarEntradaSemItens() {
    	Saida saida = new Saida();
    	saida.setDataMovimentacao(new Date());
    	saida.setItens(null);
    	estoqueFacade.cadastrar(saida);
    }
    
	/**
	 * Tenta cadastrar uma saída que possui apenas um produto, com preço registrado.
	 * @throws Exception 
	 */
    @Test
    public void cadastrarSaidaSucesso() throws Exception {
    	// workaround enquanto interceptor + arquillian continua um mistério
    	produtosDataLoader.load();
    	
    	// garante o estado anterior da base de dados
    	Assert.assertEquals(0L, getQuerierUtil().count(Saida.class).longValue());
    	Assert.assertEquals(0L, getQuerierUtil().count(Item.class).longValue());
    	
    	
    	Saida saida = new Saida();
    	saida.setDataMovimentacao(new Date());
    	
    	Item item = new Item();
    	item.setProduto(getQuerierUtil().findAny(Produto.class));
    	item.setQuantidade(10);
    	item.setMovimentacaoEstoque(saida);
    	
    	saida.setItens(Arrays.asList(item));
    	saida.setStatus(Status.CONCLUIDO);
    	
    	estoqueFacade.cadastrar(saida);
    	
    	getEm().clear();
    	
    	// verifica se os dados foram inseridos
    	Assert.assertEquals(1L, getQuerierUtil().count(Saida.class).longValue());
    	Assert.assertEquals(1L, getQuerierUtil().count(Item.class).longValue());
    	
    }
	
}
