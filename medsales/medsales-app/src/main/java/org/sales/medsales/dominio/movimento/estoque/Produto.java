package org.sales.medsales.dominio.movimento.estoque;

import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.sales.medsales.api.dominio.EntityBase;
import org.sales.medsales.api.util.GenericComparator;

/**
 * Entidade que representa os dados de um produto comercializado. 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Entity
public class Produto extends EntityBase<Long> {

	/**
	 * Código de barras cadastrado.
	 */
	@Column(nullable=false)
	private String codigoBarras;
	
	/**
	 * Descrição do produto.
	 */
	@Column(nullable=false)
	private String nome;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="produto")
	private List<PrecoProduto> precos;
	
	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String descricao) {
		this.nome = descricao;
	}

	public List<PrecoProduto> getPrecos() {
		return precos;
	}

	public void setPrecos(List<PrecoProduto> precos) {
		this.precos = precos;
	}

	/**
	 * Retorna o preço atual (mais recente) do produto.
	 * @return 
	 */
	public PrecoProduto getPrecoAtual() {
		return (getPrecos() != null && !getPrecos().isEmpty())
				? Collections.max(getPrecos(), new GenericComparator(true, "validoEm", "id"))
				: null;
	}
	
	/**
	 * Ordena os preços de forma ascedente.
	 * @param asc true para Ascendente, false para descendente
	 */
	public void ordenarPrecos(boolean asc) {
		Collections.sort(getPrecos(), new GenericComparator(asc, "validoEm", "id"));
	}
}
