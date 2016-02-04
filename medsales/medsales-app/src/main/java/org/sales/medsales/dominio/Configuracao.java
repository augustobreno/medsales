package org.sales.medsales.dominio;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.sales.medsales.api.dominio.EntityBase;

/**
 * Para persitência de parâmetros de configuração da aplicação.
 * @author Augusto
 */
@SuppressWarnings("serial")
@Entity
public class Configuracao extends EntityBase<Long> {

	/**
	 * Porcentagem para calculo do investimento sobre o valor da nota.
	 */
	@Column(precision=4, scale=2)
	private BigDecimal indiceInvestimento;
	
	/**
	 * Porcentagem para calculo da comissão paga.
	 */
	@Column(precision=4, scale=2)
	private BigDecimal indiceComissao;

	public BigDecimal getIndiceInvestimento() {
		return indiceInvestimento;
	}
	
	public void setIndiceInvestimento(BigDecimal indiceInvestimento) {
		this.indiceInvestimento = indiceInvestimento;
	}

	public BigDecimal getIndiceComissao() {
		return indiceComissao;
	}

	public void setIndiceComissao(BigDecimal indiceComissao) {
		this.indiceComissao = indiceComissao;
	}	
	
}
