package org.sales.medsales.negocio.movimentacao.estoque;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.sales.medsales.api.exceptions.EntityNotFoundException;
import org.sales.medsales.api.exceptions.NullParameterException;
import org.sales.medsales.dominio.Configuracao;
import org.sales.medsales.dominio.movimento.estoque.EntradaEstoque;
import org.sales.medsales.dominio.movimento.estoque.Item;
import org.sales.medsales.dominio.movimento.estoque.MovimentoEstoque;
import org.sales.medsales.dominio.movimento.estoque.SaidaEstoque;
import org.sales.medsales.dominio.movimento.estoque.Status;
import org.sales.medsales.exceptions.ExceptionCodes;
import org.sales.medsales.negocio.ConfiguracaoFacade;
import org.sales.medsales.persistencia.repository.EstoqueRepository;

/**
 * BO responsável por gerar uma Saída a partir de um entrada pré-cadastrada.
 * @author Augusto
 *
 */
@SuppressWarnings("serial")
public class GerarSaidaEstoqueBO implements Serializable {

	@Inject
	private EstoqueRepository estoqueRepository;
	
	@Inject
	private SaidaEstoqueBO saidaEstoqueBO;
	
	@Inject
	private ConfiguracaoFacade configuracaoFacade;
	
	/**
	 * Gera uma saída a partir de uma entrada pré-cadastrada
	 * @param idEntrada ID da entrada base.
	 * @return Saída gerada, com status de Rascunho.
	 */
	public SaidaEstoque gerar(Long idEntrada) {
		validarParametros(idEntrada);
		
		/*
		 * Recuperando todos os itens da entrada e criando uma réplica para a saída.
		 */
		SaidaEstoque saidaEstoque = new SaidaEstoque();
		saidaEstoque.setStatus(Status.RASCUNHO);
		
		EntradaEstoque entradaEstoque = (EntradaEstoque) estoqueRepository.findBy(idEntrada);
		List<Item> itensSaida = new ArrayList<Item>();
		for (Item itemEntrada : entradaEstoque.getItens()) {
			Item itemSaida = new Item();
			itemSaida.setMovimentoEstoque(saidaEstoque);
			itemSaida.setPrecoProduto(itemEntrada.getPrecoProduto());
			itemSaida.setQuantidade(itemEntrada.getQuantidade());
			
			itensSaida.add(itemSaida);
		}
		
		saidaEstoque.setItens(itensSaida);
//		saidaEstoque.setCiclo(entradaEstoque.getCiclo());
		
		Configuracao configuracao = configuracaoFacade.getConfiguracao();
		saidaEstoque.setIndiceComissao(configuracao.getIndiceComissao());
		saidaEstoque.setIndiceInvestimento(configuracao.getIndiceInvestimento());
		
		saidaEstoqueBO.salvar(saidaEstoque);
		
		return saidaEstoque;
	}

	private void validarParametros(Long idEntrada) {
		if (idEntrada == null) {
			throw new NullParameterException(ExceptionCodes.SAIDA_ESTOQUE.GERAR_SAIDA_ENTRADA_REQUIRED, "É necessário informar o código da entrada.");
		}
		
		MovimentoEstoque movimentacao = estoqueRepository.findBy(idEntrada);
		if (movimentacao == null ) {
			throw new EntityNotFoundException(null, "O código da entrada informarda não existe.");
		}
		
		if (!EntradaEstoque.class.isAssignableFrom(movimentacao.getClass())) {
			throw new EntityNotFoundException(null, "O código informado não pretence a uma entrada, mas a outro tipo de movimentação.");
		}
	}
}
