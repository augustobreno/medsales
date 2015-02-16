package org.sales.medsales.negocio.movimentacao.estoque;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.sales.medsales.api.exceptions.EntityNotFoundException;
import org.sales.medsales.api.exceptions.NullParameterException;
import org.sales.medsales.dominio.movimentacao.estoque.EntradaEstoque;
import org.sales.medsales.dominio.movimentacao.estoque.Item;
import org.sales.medsales.dominio.movimentacao.estoque.MovimentacaoEstoque;
import org.sales.medsales.dominio.movimentacao.estoque.SaidaEstoque;
import org.sales.medsales.dominio.movimentacao.estoque.Status;
import org.sales.medsales.exceptions.ExceptionCodes;
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
			itemSaida.setMovimentacaoEstoque(saidaEstoque);
			itemSaida.setProduto(itemEntrada.getProduto());
			itemSaida.setQuantidade(itemEntrada.getQuantidade());
			
			itensSaida.add(itemSaida);
		}
		
		saidaEstoque.setItens(itensSaida);
		saidaEstoque.setCiclo(entradaEstoque.getCiclo());
		saidaEstoqueBO.cadastrar(saidaEstoque);
		
		return saidaEstoque;
	}

	private void validarParametros(Long idEntrada) {
		if (idEntrada == null) {
			throw new NullParameterException(ExceptionCodes.SAIDA_ESTOQUE.GERAR_SAIDA_ENTRADA_REQUIRED, "É necessário informar o código da entrada.");
		}
		
		MovimentacaoEstoque movimentacao = estoqueRepository.findBy(idEntrada);
		if (movimentacao == null ) {
			throw new EntityNotFoundException(null, "O código da entrada informarda não existe.");
		}
		
		if (!EntradaEstoque.class.isAssignableFrom(movimentacao.getClass())) {
			throw new EntityNotFoundException(null, "O código informado não pretence a uma entrada, mas a outro tipo de movimentação.");
		}
	}
}
