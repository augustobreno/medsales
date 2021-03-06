package org.sales.medsales.negocio.movimentacao.estoque;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import junit.framework.Assert;

import org.easy.testeasy.dataloader.LoadData;
import org.junit.Test;
import org.sales.medsales.MedSalesBaseTest;
import org.sales.medsales.api.exceptions.BusinessException;
import org.sales.medsales.api.exceptions.EntityNotFoundException;
import org.sales.medsales.api.exceptions.NullParameterException;
import org.sales.medsales.dataLoader.PrecoProdutoDataLoader;
import org.sales.medsales.dataLoader.ProdutosDataLoader;
import org.sales.medsales.dominio.movimento.estoque.EntradaEstoque;
import org.sales.medsales.dominio.movimento.estoque.Item;
import org.sales.medsales.dominio.movimento.estoque.MovimentoEstoque;
import org.sales.medsales.dominio.movimento.estoque.PrecoProduto;
import org.sales.medsales.dominio.movimento.estoque.SaidaEstoque;
import org.sales.medsales.dominio.movimento.estoque.Status;
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
			estoqueFacade.salvar((SaidaEstoque)null);
			Assert.fail();
		} catch (NullParameterException e) {
			Assert.assertTrue(e.hasCode(ExceptionCodes.MOVIMENTACAO_ESTOQUE.MOVIMENTACAO_REQUIRED));
		}
	}
	
	/**
	 * Garante a validação dos parâmetros de saída
	 */
    @Test
    public void cadastrarSaidaOperacaoNull() {
    	SaidaEstoque saidaEstoque = new SaidaEstoque();
    	saidaEstoque.setOperacao(null);
    	saidaEstoque.setDataMovimento(new Date());
    	
		try {
			estoqueFacade.salvar(saidaEstoque);
			Assert.fail();
		} catch (BusinessException e) {
			Assert.assertTrue(e.hasCode(ExceptionCodes.MOVIMENTACAO_ESTOQUE.OPERACAO_REQUIRED));
		}
    }
    
	/**
	 * Garante a validação dos parâmetros de saída
	 */
    @Test(expected=MovimentacaoSemItensException.class)
    public void cadastrarSaidaSemItens() {
    	SaidaEstoque saidaEstoque = new SaidaEstoque();
    	saidaEstoque.setDataMovimento(new Date());
    	saidaEstoque.setItens(null);
    	estoqueFacade.salvar(saidaEstoque);
    }
    
	/**
	 * Tenta cadastrar uma saída que possui apenas um produto, com preço registrado.
	 * @throws Exception 
	 */
    @Test
    @LoadData(dataLoader={ProdutosDataLoader.class, PrecoProdutoDataLoader.class})
    public void cadastrarSaidaSucesso() throws Exception {
    	// garante o estado anterior da base de dados
    	Assert.assertEquals(0L, getQuerier().count(SaidaEstoque.class).longValue());
    	Assert.assertEquals(0L, getQuerier().count(Item.class).longValue());
    	
    	
    	SaidaEstoque saidaEstoque = new SaidaEstoque();
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
    	Assert.assertEquals(1L, getQuerier().count(SaidaEstoque.class).longValue());
    	Assert.assertEquals(1L, getQuerier().count(Item.class).longValue());
    	
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
    		Assert.assertTrue(e.hasCode(ExceptionCodes.SAIDA_ESTOQUE.GERAR_SAIDA_ENTRADA_REQUIRED));
    	}
    }
    
    /**
     * Teste do método {@link EstoqueFacade#gerarSaida(Long)}, informando o ID de uma entrada inexistente.
     */
    @Test(expected=EntityNotFoundException.class)
    public void gerarSaidaEntradaInexistenteTest() {
    	Long idInexistente = 9999999L;
    	
    	// se certificando de que não existe uma entrada com este ID
    	MovimentoEstoque entradaInexistente = getQuerier().find(MovimentoEstoque.class, idInexistente);
    	Assert.assertNull(entradaInexistente);
    	
    	estoqueFacade.gerarSaida(idInexistente);
    }
    
    /**
     * Teste do método {@link EstoqueFacade#gerarSaida(Long)}, esperando o funcionamento correto.
     * @throws Exception 
     */
    @Test
    @LoadData(dataLoader={ProdutosDataLoader.class, PrecoProdutoDataLoader.class})
    public void gerarSaidaTest() throws Exception {
    	
    	// criando uma entrada para teste 
    	EntradaEstoque entradaEstoque = criarEntrada();
    	getEm().clear();
    	
    	SaidaEstoque saidaEstoque = estoqueFacade.gerarSaida(entradaEstoque.getId());
    	
    	Assert.assertNotNull(saidaEstoque);
    	Assert.assertEquals(Status.RASCUNHO, saidaEstoque.getStatus());
    	Assert.assertNotNull(saidaEstoque.getId());

    	/*
    	 * Buscando a saída direto da base de dados para verificar os dados persistidos
    	 */
    	getEm().clear();
    	saidaEstoque = (SaidaEstoque) estoqueRepository.findBy(saidaEstoque.getId(), "itens.precoProduto");
    	
    	Assert.assertEquals(entradaEstoque.getItens().size(), saidaEstoque.getItens().size());
    	verificarMesmosItens(entradaEstoque.getItens(), saidaEstoque.getItens());
    	
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
						&& esperado.getPrecoProduto().equals(encontrado.getPrecoProduto())) {
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

	private EntradaEstoque criarEntrada() {
		EntradaEstoque entradaEstoque = new EntradaEstoque();
    	entradaEstoque.setDataMovimento(new Date());
    	
    	List<Item> itens = new ArrayList<Item>();
    	for (int i = 0; i < 5; i++) {
    		Item item = new Item();
        	item.setPrecoProduto(getQuerier().findAt(PrecoProduto.class, i));
        	item.setQuantidade(10);
        	item.setMovimentoEstoque(entradaEstoque);
        	
        	itens.add(item);
		}
    	
    	entradaEstoque.setItens(itens);
    	entradaEstoque.setStatus(Status.CONCLUIDO);
    	
    	estoqueFacade.salvar(entradaEstoque);
		return entradaEstoque;
	}
}
