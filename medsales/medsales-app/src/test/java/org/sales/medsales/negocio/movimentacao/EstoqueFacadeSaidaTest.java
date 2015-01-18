package org.sales.medsales.negocio.movimentacao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.sales.medsales.MedSalesBaseTest;
import org.sales.medsales.api.exceptions.EntityNotFoundException;
import org.sales.medsales.api.exceptions.NullParameterException;
import org.sales.medsales.dataLoader.PrecoProdutoDataLoader;
import org.sales.medsales.dataLoader.ProdutosDataLoader;
import org.sales.medsales.dominio.Item;
import org.sales.medsales.dominio.Produto;
import org.sales.medsales.dominio.movimentacao.Entrada;
import org.sales.medsales.dominio.movimentacao.MovimentacaoEstoque;
import org.sales.medsales.dominio.movimentacao.Saida;
import org.sales.medsales.dominio.movimentacao.Status;
import org.sales.medsales.exceptions.ExceptionCodes;
import org.sales.medsales.exceptions.MovimentacaoSemItensException;
import org.sales.medsales.persistencia.repository.EstoqueRepository;

/**
 * Testa as operações de saída do estoque.
 * @author Augusto
 */
public class EstoqueFacadeSaidaTest extends MedSalesBaseTest {

	@Inject
	private EstoqueFacade estoqueFacade;
	
	@Inject
	private ProdutosDataLoader produtosDataLoader;
	
	@Inject
	private PrecoProdutoDataLoader precoProdutoDataLoader;
	
	@Inject
	private EstoqueRepository estoqueRepository;
	
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

    /**
     * Teste do método {@link EstoqueFacade#gerarSaida(Long)}, informando null para o ID da entrada.
     */
    @Test
    public void gerarSaidaEntradaNullTest() {
    	try {
    		estoqueFacade.gerarSaida(null);
    		Assert.fail();
    	} catch (NullParameterException e) {
    		Assert.assertTrue(e.hasCode(ExceptionCodes.SAIDA.GERAR_SAIDA_ENTRADA_REQUIRED));
    	}
    }
    
    /**
     * Teste do método {@link EstoqueFacade#gerarSaida(Long)}, informando o ID de uma entrada inexistente.
     */
    @Test(expected=EntityNotFoundException.class)
    public void gerarSaidaEntradaInexistenteTest() {
    	Long idInexistente = 9999999L;
    	
    	// se certificando de que não existe uma entrada com este ID
    	MovimentacaoEstoque entradaInexistente = getQuerierUtil().find(MovimentacaoEstoque.class, idInexistente);
    	Assert.assertNull(entradaInexistente);
    	
    	estoqueFacade.gerarSaida(idInexistente);
    }
    
    /**
     * Teste do método {@link EstoqueFacade#gerarSaida(Long)}, esperando o funcionamento correto.
     * @throws Exception 
     */
    @Test
    public void gerarSaidaTest() throws Exception {
    	// workaround enquanto interceptor + arquillian continua um mistério
    	produtosDataLoader.load();
    	precoProdutoDataLoader.load();
    	
    	// criando uma entrada para teste 
    	Entrada entrada = criarEntrada();
    	getEm().clear();
    	
    	Saida saida = estoqueFacade.gerarSaida(entrada.getId());
    	
    	Assert.assertNotNull(saida);
    	Assert.assertEquals(Status.RASCUNHO, saida.getStatus());
    	Assert.assertNotNull(saida.getId());

    	/*
    	 * Buscando a saída direto da base de dados para verificar os dados persistidos
    	 */
    	getEm().clear();
    	saida = (Saida) estoqueRepository.findBy(saida.getId(), "itens.produto");
    	
    	Assert.assertEquals(entrada.getItens().size(), saida.getItens().size());
    	verificarMesmosItens(entrada.getItens(), saida.getItens());
    	
    }

    /**
     * Verifica se as listas são equivalentes em produtos e quantidades.
     */
	private void verificarMesmosItens(List<Item> esperados, List<Item> encontrados) {
		List<Item> esperadosCopia = new ArrayList<Item>(esperados);
		List<Item> encontradosCopia = new ArrayList<Item>(encontrados);
		
		// varre as listas removendo os iguais.
		for (Iterator<Item> iterator = esperadosCopia.iterator(); iterator.hasNext();) {
			Item esperado = iterator.next();
			
			for (Iterator<Item> iterator2 = encontradosCopia.iterator(); iterator2.hasNext();) {
				Item encontrado = iterator2.next();
				
				if(esperado.getQuantidade() == encontrado.getQuantidade()
						&& esperado.getProduto().equals(encontrado.getProduto())) {
					iterator.remove();
					iterator2.remove();
					continue;
				}
				
			}
		}
		
		// ao final, ambas as listas devem estar vazias
		Assert.assertTrue(esperadosCopia.isEmpty());
		Assert.assertTrue(encontradosCopia.isEmpty());
	}

	private Entrada criarEntrada() {
		Entrada entrada = new Entrada();
    	entrada.setDataMovimentacao(new Date());
    	
    	List<Item> itens = new ArrayList<Item>();
    	for (int i = 0; i < 5; i++) {
    		Item item = new Item();
        	item.setProduto(getQuerierUtil().findAt(Produto.class, i));
        	item.setQuantidade(10);
        	item.setMovimentacaoEstoque(entrada);
        	
        	itens.add(item);
		}
    	
    	entrada.setItens(itens);
    	entrada.setStatus(Status.CONCLUIDO);
    	
    	estoqueFacade.cadastrar(entrada);
		return entrada;
	}
}
