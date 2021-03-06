package org.sales.medsales.dominio.movimento;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.sales.medsales.api.dominio.EntityBase;
import org.sales.medsales.api.dominio.types.HibernateEnumType;
import org.sales.medsales.dominio.Parceiro;

/**
 * Tipo genérico que define o estado básico de um movimento realizado em um estoque ou ciclo.
 * @author Augusto
 *
 */

@TypeDefs ({
	@TypeDef(name = "operacao", typeClass = HibernateEnumType.class, 
		parameters = { @Parameter(name = "enumClass", value = "org.sales.medsales.dominio.movimento.Operacao"), }),
})

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

	@Column(length=200)
	private String observacao;

	/**
	 * Data oficial da movimentação.
	 */
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date dataMovimento;
	
	@PrePersist
	public void initTimeStamps() {
		super.initTimeStamps();
		if (dataMovimento == null) {
			dataMovimento = new Date();
		}
	}
	
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

	public Date getDataMovimento() {
		return dataMovimento;
	}

	public void setDataMovimento(Date dataMovimentacao) {
		this.dataMovimento = dataMovimentacao;
	}
}
