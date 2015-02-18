package org.sales.medsales.dominio.movimentacao;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.sales.medsales.api.dominio.types.HibernateEnumType;
import org.sales.medsales.dominio.Ciclo;

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
public abstract class MovimentoValor extends Movimento{

	/**
	 * Ciclo que contém todas as movimentações de valores inter-relacionadas.
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	private Ciclo ciclo;
	
	/**
	 * Valor total desta movimentação.
	 */
	@Column(precision=19, scale=2)
	private BigDecimal valor;
	
	public MovimentoValor() {
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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}
