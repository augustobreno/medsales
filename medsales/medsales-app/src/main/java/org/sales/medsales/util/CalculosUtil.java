package org.sales.medsales.util;

import java.math.BigDecimal;

import javax.inject.Named;

/**
 * Classe utilitária para realização de cálculos matemáticos.
 * @author Augusto
 *
 */
@Named
public class CalculosUtil {

	private static final BigDecimal CEM = new BigDecimal(100);
	
	/**
	 * Calcula o valor de um desconto.
	 * @param valor Valor base para cálculo.
	 * @param descontoPorcent Porcentagem do desconto.
	 * @return Valor do desconto cáculo.
	 */
	public BigDecimal calcularDesconto(BigDecimal valor, double descontoPorcent) {
		return	valor.multiply(new BigDecimal(descontoPorcent).divide(CEM));
	}

	/**
	 * Calcula o valor final deste produto aplicando um desconto.
	 * @param desconto Desconto a ser aplicado no valor deste produto.
	 * @param valor Valor base para cálculo.
	 * @return Valor final com desconto aplicado.
	 */
	public static BigDecimal aplicarDesconto(BigDecimal valor, double desconto) {
		BigDecimal comDesconto = valor;
		if (valor != null || desconto > 0) {
			double porcentagem = 100 - desconto;
			comDesconto = valor.multiply(new BigDecimal(porcentagem).divide(CEM));
		}
		return comDesconto;
	}
}
