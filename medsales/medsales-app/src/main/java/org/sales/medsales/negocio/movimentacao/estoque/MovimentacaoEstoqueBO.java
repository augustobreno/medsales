package org.sales.medsales.negocio.movimentacao.estoque;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import org.sales.medsales.api.exceptions.NullParameterException;
import org.sales.medsales.dominio.movimentacao.estoque.MovimentacaoEstoque;
import org.sales.medsales.exceptions.ExceptionCodes;
import org.sales.medsales.exceptions.MovimentacaoSemItensException;
import org.sales.medsales.persistencia.repository.EstoqueRepository;

/**
 * Classe para conter as operações e validações comuns para Entrada e Saída.
 * @author Augusto
 *
 */
@SuppressWarnings("serial")
public abstract class MovimentacaoEstoqueBO<MOV extends MovimentacaoEstoque> implements Serializable {

	@Inject
	private EstoqueRepository estoqueRepository;

	/**
	 * Cadastra uma movimentacao de produtos.
	 */
	public void cadastrar(MOV movimentacao) {
		validarCadastrar(movimentacao);
		
		if (movimentacao.getDataMovimentacao() == null) {
			movimentacao.setDataMovimentacao(new Date());
		}
		
		salvar(movimentacao);
	}

	/**
	 * Salva de fato a movimentação. O fluxo de inclusão ou atialização
	 * será definido pela presença do ID.
	 */
	protected void salvar(MOV movimentacao) {
		if (movimentacao.getId() == null) {
			estoqueRepository.insert(movimentacao);
		} else {
			estoqueRepository.update(movimentacao);
		}
	}
	
	protected void validarCadastrar(MOV movimentacao) {
		validarDadosObrigatorias(movimentacao);
	}

	protected void validarDadosObrigatorias(MOV movimentacao) {
		if (movimentacao == null) {
			throw new NullParameterException(ExceptionCodes.MOVIMENTACAO_ESTOQUE.MOVIMENTACAO_REQUIRED, "É necessário informar uma movimentação.");
		}
		if (movimentacao.getOperacao() == null) {
			throw new NullParameterException(ExceptionCodes.MOVIMENTACAO_ESTOQUE.OPERACAO_REQUIRED, "É necessário informar o tipo da operação da movimentação.");
		}
		if (movimentacao.getItens() == null || movimentacao.getItens().isEmpty()) {
			throw new MovimentacaoSemItensException(null, "É necessário incluir Itens na movimentação.");
		}
	}		
	
	protected EstoqueRepository getEstoqueRepository() {
		return estoqueRepository;
	}
	
}
