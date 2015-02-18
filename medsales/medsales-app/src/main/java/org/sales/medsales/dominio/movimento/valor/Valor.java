package org.sales.medsales.dominio.movimento.valor;

import javax.persistence.Entity;

/**
 * Representa uma entrada ou saída de valor sem um objetivo previsto no sistema. 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Entity
public class Valor extends MovimentoValor {

	@Override
	protected void defineOperacao() {
		// não há implementação padrão, a operação deverá ser configurada no objeto.
	}


}
