package org.sales.medsales.dominio.movimentacao;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;
import org.sales.medsales.api.dominio.EntityBase;
import org.sales.medsales.dominio.Parceiro;

/**
 * Tipo genérico que define o estado básico de um movimento realizado em um estoque ou ciclo.
 * @author Augusto
 *
 */
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class Movimento extends EntityBase<Long> {

	/**
	 * Aquele que dá ou recebe o valor associado a esta movimentação.
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	private Parceiro parceiro;
	
	/** determina o tipo de operação que este objeto deverá realizar (entrada, saída...) */
	@Type(type="operacao")
	@Column(nullable=false)
	private Operacao operacao;

	@Column(length=500)
	private String observacao;

	public Parceiro getParceiro() {
		return parceiro;
	}

	public void setParceiro(Parceiro parceiro) {
		this.parceiro = parceiro;
	}

	public Operacao getOperacao() {
		return operacao;
	}

	public void setOperacao(Operacao operacao) {
		this.operacao = operacao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
}
