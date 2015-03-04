package org.sales.medsales.web.util;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class ViewFunctions {

	/**
	 * Concatena 2 valores.
	 */
	public String concat(Object valor1, Object valor2) {
		return getNotNull(valor1) + getNotNull(valor2);
	}
	
	/**
	 * Concatena 3 valores.
	 */
	public String concat(Object valor1, Object valor2, Object valor3) {
		return getNotNull(valor1) + getNotNull(valor2) + getNotNull(valor3);
	}

	private String getNotNull(Object valor) {
		return valor == null ? "" : valor.toString();
	}

}
