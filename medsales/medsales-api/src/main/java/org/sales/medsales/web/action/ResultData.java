package org.sales.medsales.web.action;

import java.util.List;

import org.sales.medsales.dominio.Entity;

/**
 * Wraper para encapsular o resultado de uma consulta, contendo a lista e o número total
 * de registros. 
 * @author augusto
 */
public class ResultData<ENTITY extends Entity<?>> {
	
	private List<ENTITY> resultList;
	private Integer countAll;
	
	public ResultData(List<ENTITY> resultList, Integer countAll) {
		super();
		this.resultList = resultList;
		this.countAll = countAll;
	}
	
	public List<ENTITY> getResultList() {
		return resultList;
	}
	public void setResultList(List<ENTITY> resultList) {
		this.resultList = resultList;
	}
	public Integer getCountAll() {
		return countAll;
	}
	public void setCountAll(Integer countAll) {
		this.countAll = countAll;
	}
	
}