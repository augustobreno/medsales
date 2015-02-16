package org.sales.medsales.dominio.movimentacao;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Tipo abstrato de dados para definir o comportamento mínimo de uma 
 * movimentação de ENTRADA de valor em um ciclo.
 * @author Augusto
 *
 */
@SuppressWarnings("serial")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) 
@Entity
public abstract class EntradaValor extends MovimentacaoValor {

	protected void defineOperacao() {
		setOperacao(Operacao.ENTRADA);
	}

}
