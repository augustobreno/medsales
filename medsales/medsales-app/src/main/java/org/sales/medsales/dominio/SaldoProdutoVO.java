package org.sales.medsales.dominio;

/**
 * VO para agrupamento dos dados de um Produto e sua quantidade existente em estoque. 
 * @author Augusto
 */
public class SaldoProdutoVO {

	/** Produto registrado em estoque */
	private Long idProduto;
	
	/** Número de itens disponível em estoque */
	private Long quantidade;

	public SaldoProdutoVO() {
		super();
	}

	/**
	 * @param produto Produto relativo ao saldo.
	 * @param quantidade Saldo encontrado em estoque
	 */
	public SaldoProdutoVO(Long produto, Long quantidade) {
		super();
		this.idProduto = produto;
		this.quantidade = quantidade != null ? quantidade : 0L;
	}

	public Long getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Long produto) {
		this.idProduto = produto;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idProduto == null) ? 0 : idProduto.hashCode());
		result = prime * result + ((quantidade == null) ? 0 : quantidade.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SaldoProdutoVO other = (SaldoProdutoVO) obj;
		if (idProduto == null) {
			if (other.idProduto != null)
				return false;
		} else if (!idProduto.equals(other.idProduto))
			return false;
		if (quantidade == null) {
			if (other.quantidade != null)
				return false;
		} else if (!quantidade.equals(other.quantidade))
			return false;
		return true;
	}
	
}
