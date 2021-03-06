package org.sales.medsales.negocio.movimentacao.estoque;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import org.sales.medsales.api.exceptions.BusinessException;
import org.sales.medsales.api.exceptions.NullParameterException;
import org.sales.medsales.dominio.movimento.estoque.MovimentoEstoque;
import org.sales.medsales.exceptions.ExceptionCodes;
import org.sales.medsales.exceptions.MovimentacaoSemItensException;
import org.sales.medsales.persistencia.repository.EstoqueRepository;

/**
 * Classe para conter as operações e validações comuns para Entrada e Saída.
 * @author Augusto
 *
 */
@SuppressWarnings("serial")
public abstract class MovimentacaoEstoqueBO<MOV extends MovimentoEstoque> implements Serializable {

	@Inject
	private EstoqueRepository estoqueRepository;

	/**
	 * Cadastra uma movimentacao de produtos.
	 */
	public void salvar(MOV movimentacao) {
		validarCadastrar(movimentacao);
		
		if (movimentacao.getDataMovimento() == null) {
			movimentacao.setDataMovimento(new Date());
		}
		
		persistir(movimentacao);
	}

	/**
	 * Salva de fato a movimentação. O fluxo de inclusão ou atialização
	 * será definido pela presença do ID.
	 */
	protected void persistir(MOV movimentacao) {
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
			throw new BusinessException(ExceptionCodes.MOVIMENTACAO_ESTOQUE.OPERACAO_REQUIRED, "É necessário informar o tipo da operação da movimentação.");
		}
		if (movimentacao.getItens() == null || movimentacao.getItens().isEmpty()) {
			throw new MovimentacaoSemItensException(null, "É necessário incluir Itens na movimentação.");
		}
	}		
	
	protected EstoqueRepository getEstoqueRepository() {
		return estoqueRepository;
	}
	
}
