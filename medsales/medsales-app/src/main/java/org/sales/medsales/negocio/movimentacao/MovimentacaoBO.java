package org.sales.medsales.negocio.movimentacao;

import java.io.Serializable;

import javax.inject.Inject;

import org.sales.medsales.api.exceptions.NullParameterException;
import org.sales.medsales.dominio.movimentacao.MovimentacaoEstoque;
import org.sales.medsales.exceptions.ExceptionCodes;
import org.sales.medsales.exceptions.MovimentacaoSemItensException;
import org.sales.medsales.persistencia.repository.EstoqueRepository;

/**
 * Classe para conter as operações e validações comuns para Entrada e Saída.
 * @author Augusto
 *
 */
@SuppressWarnings("serial")
public abstract class MovimentacaoBO implements Serializable {

	@Inject
	private EstoqueRepository estoqueRepository;

	/**
	 * Cadastra uma movimentacao de produtos.
	 */
	public void cadastrar(MovimentacaoEstoque movimentacao) {
		validarCadastrar(movimentacao);
		
		if (movimentacao.getId() == null) {
			estoqueRepository.insert(movimentacao);
		} else {
			estoqueRepository.update(movimentacao);
		}
	}
	
	protected void validarCadastrar(MovimentacaoEstoque movimentacao) {
		validarDadosObrigatorias(movimentacao);
	}

	protected void validarDadosObrigatorias(MovimentacaoEstoque movimentacao) {
		if (movimentacao == null) {
			throw new NullParameterException(ExceptionCodes.MOVIMENTACAO.MOVIMENTACAO_REQUIRED, "É necessário informar uma movimentação.");
		}
		if (movimentacao.getOperacao() == null) {
			throw new NullParameterException(ExceptionCodes.MOVIMENTACAO.OPERACAO_REQUIRED, "É necessário informar o tipo da operação da movimentação.");
		}
		if (movimentacao.getItens() == null || movimentacao.getItens().isEmpty()) {
			throw new MovimentacaoSemItensException(null, "É necessário incluir Itens na movimentação.");
		}
	}		
	
	protected EstoqueRepository getEstoqueRepository() {
		return estoqueRepository;
	}
	
}
