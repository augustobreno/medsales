package org.sales.medsales.negocio.movimentacao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.sales.medsales.api.exceptions.EntityNotFoundException;
import org.sales.medsales.api.exceptions.NullParameterException;
import org.sales.medsales.dominio.Item;
import org.sales.medsales.dominio.movimentacao.Entrada;
import org.sales.medsales.dominio.movimentacao.MovimentacaoEstoque;
import org.sales.medsales.dominio.movimentacao.Saida;
import org.sales.medsales.dominio.movimentacao.Status;
import org.sales.medsales.exceptions.ExceptionCodes;
import org.sales.medsales.persistencia.repository.EstoqueRepository;

/**
 * BO responsável por gerar uma Saída a partir de um entrada pré-cadastrada.
 * @author Augusto
 *
 */
@SuppressWarnings("serial")
public class GerarSaidaBO implements Serializable {

	@Inject
	private EstoqueRepository estoqueRepository;
	
	@Inject
	private SaidaBO saidaBO;
	
	/**
	 * Gera uma saída a partir de uma entrada pré-cadastrada
	 * @param idEntrada ID da entrada base.
	 * @return Saída gerada, com status de Rascunho.
	 */
	public Saida gerar(Long idEntrada) {
		validarParametros(idEntrada);
		
		/*
		 * Recuperando todos os itens da entrada e criando uma réplica para a saída.
		 */
		Saida saida = new Saida();
		saida.setStatus(Status.RASCUNHO);
		
		Entrada entrada = (Entrada) estoqueRepository.findBy(idEntrada);
		List<Item> itensSaida = new ArrayList<Item>();
		for (Item itemEntrada : entrada.getItens()) {
			Item itemSaida = new Item();
			itemSaida.setMovimentacaoEstoque(saida);
			itemSaida.setProduto(itemEntrada.getProduto());
			itemSaida.setQuantidade(itemEntrada.getQuantidade());
			
			itensSaida.add(itemSaida);
		}
		
		saida.setItens(itensSaida);
		saidaBO.cadastrar(saida);
		
		return saida;
	}

	private void validarParametros(Long idEntrada) {
		if (idEntrada == null) {
			throw new NullParameterException(ExceptionCodes.SAIDA.GERAR_SAIDA_ENTRADA_REQUIRED, "É necessário informar o código da entrada.");
		}
		
		MovimentacaoEstoque movimentacao = estoqueRepository.findBy(idEntrada);
		if (movimentacao == null ) {
			throw new EntityNotFoundException(null, "O código da entrada informarda não existe.");
		}
		
		if (!Entrada.class.isAssignableFrom(movimentacao.getClass())) {
			throw new EntityNotFoundException(null, "O código informado não pretence a uma entrada, mas a outro tipo de movimentação.");
		}
	}
}
