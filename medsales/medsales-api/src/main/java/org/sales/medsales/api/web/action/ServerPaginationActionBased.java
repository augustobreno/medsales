package org.sales.medsales.api.web.action;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.DataModel;

import org.easy.qbeasy.QBEFilter;
import org.easy.qbeasy.api.Filter;
import org.primefaces.model.LazyDataModel;
import org.sales.medsales.api.dominio.Entity;
import org.sales.medsales.api.negocio.ServerPaginationFacade;
import org.sales.medsales.api.web.action.pagination.LazyEntityDataModel;
import org.sales.medsales.api.web.action.pagination.LazyEntityProvider;

/**
 * 
 * @author Augusto
 */

// TODO utilizar injeção de comportamento para esta solução ou composição.
// Herança não é uma boa!
@SuppressWarnings("serial")
public abstract class ServerPaginationActionBased<ENTITY extends Entity<PK>, PK extends Serializable, FACADE extends ServerPaginationFacade<ENTITY, PK>>
		extends ActionBase implements LazyEntityProvider<ENTITY> {

	/**
	 * Lista para armazenar o resultado da consulta
	 */
	private DataModel<ENTITY> result;

	/**
	 * Exemplo para utilização no formulário de consulta e preenchimento do
	 * filtro de consulta
	 */
	private ENTITY example;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		initDataModel();
	}

	/**
	 * Inicia o tipo de dataModel concreto que deverá ser utilizado neste
	 * action.
	 */
	protected void initDataModel() {
		this.result = new LazyEntityDataModel<ENTITY>(this);
	}

	/**
	 * Consulta registros na base de dados de acordo com os dados preenchidos no
	 * filtro.
	 * 
	 * Este método foi projetado para ser utilizado diretamente por um botão a
	 * partir da interface WEB. No entanto, a feramenta de listagem deverá
	 * definir a forma de execução do fluxo de consulta desta funcionalidade.
	 * 
	 * Quando o DataTable utilizado for mais complexo, com paginação no servidor
	 * ativada ({@link LazyDataModel}), ele mesmo controla o fluxo de execução
	 * da consulta, não devendo o botão de pesquisa invocar nenhum action
	 * method, mas apenas re-renderizar o dataTable.
	 * 
	 * Para dataTables mais simples, com funcionamento em memória, o botão deve
	 * iniciar o fluxo de consulta explícitamente, invocando este método como
	 * actionMethod.
	 */
	public void search() {

		// sempre cria um novo filtro para evitar "lixo" de configurações
		// anteriores
		QBEFilter<ENTITY> searchFilter = createFilter();
		search(searchFilter);

	}

	@Override
	public ResultData<ENTITY> search(Filter<ENTITY> searchFilter) {

		preSearch(searchFilter);
		ResultData<ENTITY> resultData = doSearch(searchFilter);
		postSearch(searchFilter);

		return resultData;
	}

	/**
	 * Último passo do fluxo de consulta de dados.
	 * 
	 * @param searchFilter
	 *            Filtro utilizado especialmente neste fluxo de consulta.
	 */
	protected void postSearch(Filter<ENTITY> searchFilter) {

	}

	/**
	 * Primeiro passo do fluxo de consulta.
	 * 
	 * @param searchFilter
	 *            Filtro utilizado especialmente neste fluxo de consulta.
	 */
	protected void preSearch(Filter<ENTITY> searchFilter) {

		// inclui o exemplo no filtro
		searchFilter.setExample(example);

		// permite a configuração dinâmica da consulta
		configSearch(searchFilter);

	}

	/**
	 * Executa, de fato, a operação de consulta, armazenando o resultado em
	 * {@link #result}
	 * 
	 * @param searchFilter
	 *            Filtro para configuração e exeção desta consulta.
	 * @return Dados com o resultado da consulta realizada.
	 */
	protected ResultData<ENTITY> doSearch(Filter<ENTITY> searchFilter) {

		List<ENTITY> resultList = getFacade().findAllBy(searchFilter);
		getResult().setWrappedData(resultList);

		int count;
		// TODO estudar uma forma de evitar este instanceof
		if (getResult() instanceof LazyDataModel) {
			LazyDataModel<ENTITY> dataModel = (LazyDataModel<ENTITY>) getResult();
			count = getFacade().count(searchFilter).intValue();
			dataModel.setRowCount(count);
		} else {
			count = resultList.size();
		}

		return new ResultData<ENTITY>(resultList, count);

	}

	/**
	 * Permite à subclasse configurar os parâmetros desejados para a realização
	 * da consulta.
	 * 
	 * @param filter
	 *            Classe a ser configurada com os parâmetros para realização da
	 *            consulta
	 */
	protected void configSearch(Filter<? extends ENTITY> filter) {
		/*
		 * ponto de extensão para configuração da consulta pela sub-classe. a
		 * implementação default considera a configuração por anotações
		 */
	}

	/**
	 * Método de fábrica de um {@link Filter}
	 * 
	 * @return Nova instância de {@link Filter}
	 */
	protected QBEFilter<ENTITY> createFilter() {
		QBEFilter<ENTITY> qbeFilter = new QBEFilter<ENTITY>(getEntityType());
		return qbeFilter;
	}

	public DataModel<ENTITY> getResult() {
		return result;
	}

	protected void setResult(DataModel<ENTITY> result) {
		this.result = result;
	}

	protected abstract FACADE getFacade();

	public ENTITY getExample() {
		return example;
	}

	protected void setExample(ENTITY example) {
		this.example = example;
	}
}
