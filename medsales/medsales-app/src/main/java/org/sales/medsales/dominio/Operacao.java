package org.sales.medsales.dominio;

/**
 * Tipo da operação a ser realizada sobre a movimentação no cálculo dos
 * totalizadores do estoque. 
 */
public enum Operacao {
	
	ENTRADA("E", "Entrada"), SAIDA("S", "Saída");
	
	/** Id desta operação, a ser armazenada na base de dados */
	private String id;
	
	/** Descrição para ser exibida na aplicação */
	private String descricao;

	private Operacao(String id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}