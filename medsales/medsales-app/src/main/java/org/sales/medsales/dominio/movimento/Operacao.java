package org.sales.medsales.dominio.movimento;

/**
 * Tipo da operação a ser realizada sobre a movimentação no cálculo dos
 * totalizadores. 
 */
public enum Operacao {
	
	ENTRADA("E", "Entrada", "+"), SAIDA("S", "Saída", "-");
	
	/** Id desta operação, a ser armazenada na base de dados */
	private String id;
	
	/** Descrição para ser exibida na aplicação */
	private String descricao;

	private String labelOperador;

	private Operacao(String id, String descricao, String labelOperador) {
		this.id = id;
		this.descricao = descricao;
		this.labelOperador = labelOperador;
	}
	public String getId() {
		return id;
	}
	public String getDescricao() {
		return descricao;
	}
	public String getLabelOperador() {
		return labelOperador;
	}
}