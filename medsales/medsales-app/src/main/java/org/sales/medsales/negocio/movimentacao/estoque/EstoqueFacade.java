package org.sales.medsales.negocio.movimentacao.estoque;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.easy.qbeasy.api.Filter;
import org.sales.medsales.api.negocio.BusinessExceptionHandler;
import org.sales.medsales.api.negocio.ServerPaginationFacade;
import org.sales.medsales.dominio.movimento.estoque.EntradaEstoque;
import org.sales.medsales.dominio.movimento.estoque.MovimentoEstoque;
import org.sales.medsales.dominio.movimento.estoque.Produto;
import org.sales.medsales.dominio.movimento.estoque.SaidaEstoque;
import org.sales.medsales.dominio.movimento.estoque.SaldoProdutoVO;
import org.sales.medsales.persistencia.repository.EstoqueRepository;

/**
 * Implementa todas as operações de entrada, saída, manutenção e 
 * recuperação de dados do Estoque. Coração do módulo de estoque
 * @author Augusto
 *
 */
@SuppressWarnings("serial")
@BusinessExceptionHandler
@LocalBean
@Stateless
public class EstoqueFacade implements ServerPaginationFacade<MovimentoEstoque, Long> {

	@Inject
	private EntradaEstoqueBO entradaEstoqueBO; 
	
	@Inject
	private SaidaEstoqueBO saidaEstoqueBO;
	
	@Inject
	private GerarSaidaEstoqueBO gerarSaidaEstoqueBO;
	
	@Inject
	private SaldoEstoqueBO saldoEstoqueBO;
	
	@Inject
	private EstoqueRepository estoqueRepository;
	
	/**
	 * Cadastra uma entrada de produtos.
	 */
	public void salvar(EntradaEstoque entradaEstoque) {
		entradaEstoqueBO.salvar(entradaEstoque);
	}
	
	/**
	 * Cadastra uma saída de produtos.
	 * @param saidaEstoque Saída com os itens para cadastro.
	 */
	public void salvar(SaidaEstoque saidaEstoque) {
		saidaEstoqueBO.salvar(saidaEstoque);
	}
	
	/**
	 * Cria uma saída a partir de uma entrada pré-cadastrada. A saída será criada no modo Rascunho.
	 * @param idEntrada
	 */
	public SaidaEstoque gerarSaida(Long idEntrada) {
		return gerarSaidaEstoqueBO.gerar(idEntrada);
	}
	
	/**
	 * Remove uma movimentação da base de dados.
	 * @param movimentacao A ser removida.
	 */
	public void remover(MovimentoEstoque movimentacao) {
		estoqueRepository.remove(movimentacao);
	}
	
	/**
	 * @param filter Filtro para consulta.
	 * @return Todos os registros alcançados pelo filtro.
	 */
	public List<MovimentoEstoque> findAllBy(Filter<MovimentoEstoque> filter) {
		return estoqueRepository.findAllBy(filter);
	}
	
	/**
	 * @param filter Filtro para consulta.
	 * @return Número de registros alcançados pelo filtro.
	 */
	public Long count(Filter<MovimentoEstoque> filter) {
		return estoqueRepository.count(filter);
	}

	/**
	 * Consulta uma movimentação a partir de um filtro. O Resultado deve ser um único registro. 
	 * @param filter Filtro com restrições para consulta.
	 * @return Movimentação relacionada pelo filtro.
	 */
    public MovimentoEstoque findBy(Filter<MovimentoEstoque> filter) {
        return estoqueRepository.findBy(filter);
    }

    /**
     * Consulta o saldo em estoque de um determinado produto.
     * @param produto Produto para consulta do saldo.
     * @return Saldo existente em estoque.
     */
	public SaldoProdutoVO consultarEstoque(Produto produto) {
		return saldoEstoqueBO.consultar(produto); 
	}
	
    /**
     * Consulta o saldo em estoque de alguns produtos.
     * @param produtos Produto para consulta do saldo.
     * @return Lista com saldos existentes em estoque.
     */
	public List<SaldoProdutoVO> consultarEstoque(Produto...produtos) {
		return consultarEstoque(null, produtos); 
	}
	
    /**
     * Consulta o saldo em estoque de alguns produtos.
     * @param produtos Produto para consulta do saldo.
     * @param desconsiderar Saida que deverá ser desconsiderada no cálculo do estoque.
     * @return Lista com saldos existentes em estoque.
     */
	public List<SaldoProdutoVO> consultarEstoque(SaidaEstoque desconsiderar, Produto...produtos) {
		return saldoEstoqueBO.consultar(desconsiderar, produtos); 
	}
}
