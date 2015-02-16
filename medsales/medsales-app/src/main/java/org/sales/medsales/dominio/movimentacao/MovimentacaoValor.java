package org.sales.medsales.dominio.movimentacao;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.sales.medsales.api.dominio.EntityBase;
import org.sales.medsales.api.dominio.types.HibernateEnumType;
import org.sales.medsales.dominio.Ciclo;
import org.sales.medsales.dominio.Parceiro;

/**
 * Tipo abstrato que representa uma movimentação de valores em um ciclo. Uma movimentação
 * de valor sempre soma ou diminui um valor no saldo do ciclo. 
 * @author Augusto
 */

@SuppressWarnings("serial")

@TypeDefs ({
	@TypeDef(name = "operacao", typeClass = HibernateEnumType.class, 
		parameters = { @Parameter(name = "enumClass", value = "org.sales.medsales.dominio.movimentacao.Operacao"), }),
})

@Inheritance(strategy = InheritanceType.JOINED) 
@Entity
public abstract class MovimentacaoValor extends EntityBase<Long>{

	/**
	 * Ciclo que contém todas as movimentações de valores inter-relacionadas.
	 */
	// TODO deve ser obrigatório
	@ManyToOne(fetch=FetchType.LAZY)
	private Ciclo ciclo;
	
	/**
	 * Aquele que dá ou recebe o valor associado a esta movimentação.
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	private Parceiro parceiro;
	
	/**
	 * Valor total desta movimentação.
	 */
	@Column(precision=19, scale=2)
	private BigDecimal valor;
	
	/** determina o tipo de operação que este objeto deverá realizar (entrada, saída...) */
	@Type(type="operacao")
	@Column(nullable=false)
	private Operacao operacao;

	public MovimentacaoValor() {
		defineOperacao();
	}
	
	/**
	 * Ponto de extensão para configuração da operação padrão da entidade concreta.
	 */
	protected abstract void defineOperacao();
	
	public Ciclo getCiclo() {
		return ciclo;
	}

	public void setCiclo(Ciclo ciclo) {
		this.ciclo = ciclo;
	}

	public Parceiro getParceiro() {
		return parceiro;
	}

	public void setParceiro(Parceiro parceiro) {
		this.parceiro = parceiro;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Operacao getOperacao() {
		return operacao;
	}

	public void setOperacao(Operacao operacao) {
		this.operacao = operacao;
	}
	
}
