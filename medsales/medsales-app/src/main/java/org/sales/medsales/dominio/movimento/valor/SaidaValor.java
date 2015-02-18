package org.sales.medsales.dominio.movimento.valor;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.sales.medsales.dominio.movimento.Operacao;

/**
 * Tipo abstrato de dados para definir o comportamento mínimo de uma 
 * movimentação de SAÍDA de valor em um ciclo.
 * @author Augusto
 *
 */
@SuppressWarnings("serial")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) 
@Entity
public abstract class SaidaValor extends MovimentoValor {

	protected void defineOperacao() {
		setOperacao(Operacao.SAIDA);
	}

}
