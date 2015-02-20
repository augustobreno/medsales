package org.sales.medsales.dominio.movimento.valor;

import javax.persistence.Entity;

/**
 * Representa uma entrada de valor a partir do investidor do ciclo. Geralmente é a primeira
 * movimentação em um ciclo normal. 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Entity
public class Investimento extends EntradaValor {

	@Override
	public String getLabel() {
		return "Investimento";
	}
}
