package org.sales.medsales.dominio.movimentacao;

/**
 * Status para controle da movimentação no cálculo dos
 * totalizadores do estoque. 
 */
public enum Status {
	
	RASCUNHO("R", "Rascunho"), CONCLUIDO("C", "Concluído");
	
	/** Id deste status, a ser armazenada na base de dados */
	private String id;
	
	/** Descrição para ser exibida na aplicação */
	private String descricao;

	private Status(String id, String descricao) {
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