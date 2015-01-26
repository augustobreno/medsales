package org.sales.medsales.negocio.movimentacao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.easy.qbeasy.api.Filter;
import org.sales.medsales.api.negocio.BusinessExceptionHandler;
import org.sales.medsales.api.negocio.ServerPaginationFacade;
import org.sales.medsales.dominio.SaldoProdutoVO;
import org.sales.medsales.dominio.Produto;
import org.sales.medsales.dominio.movimentacao.Entrada;
import org.sales.medsales.dominio.movimentacao.MovimentacaoEstoque;
import org.sales.medsales.dominio.movimentacao.Saida;
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
public class EstoqueFacade implements ServerPaginationFacade<MovimentacaoEstoque, Long> {

	@Inject
	private EntradaBO entradaBO; 
	
	@Inject
	private SaidaBO saidaBO;
	
	@Inject
	private GerarSaidaBO gerarSaidaBO;
	
	@Inject
	private SaldoEstoqueBO saldoEstoqueBO;
	
	@Inject
	private EstoqueRepository estoqueRepository;
	
	/**
	 * Cadastra uma entrada de produtos.
	 */
	public void cadastrar(Entrada entrada) {
		entradaBO.cadastrar(entrada);
	}
	
	/**
	 * Cadastra uma saída de produtos.
	 * @param saida Saída com os itens para cadastro.
	 */
	public void cadastrar(Saida saida) {
		saidaBO.cadastrar(saida);
	}
	
	/**
	 * Cria uma saída a partir de uma entrada pré-cadastrada. A saída será criada no modo Rascunho.
	 * @param idEntrada
	 */
	public Saida gerarSaida(Long idEntrada) {
		return gerarSaidaBO.gerar(idEntrada);
	}
	
	/**
	 * Remove uma movimentação da base de dados.
	 * @param movimentacao A ser removida.
	 */
	public void remover(MovimentacaoEstoque movimentacao) {
		estoqueRepository.remove(movimentacao);
	}
	
	/**
	 * @param filter Filtro para consulta.
	 * @return Todos os registros alcançados pelo filtro.
	 */
	public List<MovimentacaoEstoque> findAllBy(Filter<MovimentacaoEstoque> filter) {
		return estoqueRepository.findAllBy(filter);
	}
	
	/**
	 * @param filter Filtro para consulta.
	 * @return Número de registros alcançados pelo filtro.
	 */
	public Long count(Filter<MovimentacaoEstoque> filter) {
		return estoqueRepository.count(filter);
	}

	/**
	 * Consulta uma movimentação a partir de um filtro. O Resultado deve ser um único registro. 
	 * @param filter Filtro com restrições para consulta.
	 * @return Movimentação relacionada pelo filtro.
	 */
    public MovimentacaoEstoque findBy(Filter<MovimentacaoEstoque> filter) {
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
		return saldoEstoqueBO.consultar(produtos); 
	}
}
