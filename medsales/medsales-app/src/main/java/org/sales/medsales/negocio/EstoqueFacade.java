package org.sales.medsales.negocio;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.easy.qbeasy.api.Filter;
import org.sales.medsales.api.negocio.BusinessExceptionHandler;
import org.sales.medsales.api.negocio.Facade;
import org.sales.medsales.dominio.movimentacao.Entrada;
import org.sales.medsales.dominio.movimentacao.MovimentacaoEstoque;
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
public class EstoqueFacade implements Facade {

	@Inject
	private EntradaBO entradaBO; 
	
	@Inject
	private EstoqueRepository estoqueRepository;
	
	/**
	 * Cadastra uma entrada de produtos.
	 */
	public void cadastrarEntrada(Entrada entrada) {
		entradaBO.cadastrar(entrada);
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
	public long count(Filter<MovimentacaoEstoque> filter) {
		return estoqueRepository.count(filter);
	}
}
