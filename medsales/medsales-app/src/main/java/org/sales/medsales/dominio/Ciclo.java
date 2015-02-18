package org.sales.medsales.dominio;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.sales.medsales.api.dominio.EntityBase;
import org.sales.medsales.dominio.movimento.valor.MovimentoValor;

/**
 * Representa um ciclo de movimentação de fluxo de caixa que envolve
 * diversas entradas e saídas de valores: investimento, empréstimo de cartão,
 * entrada e saída no estoque, pagamento de comissão.
 * 
 * Todo ciclo está associado a um parceiro específico, que será o investidor. Todas
 * as movimentações realizadas buscarão alcançar o equilíbrio entre o valor investido
 * e os valores de saída, resultando em um saldo que representa o lucro.
 * 
 * Exemplo de ciclo:
 * 
 * Investimento: + 1000
 * Empréstimo Cartão: -1000
 * EntradaEstoque: + 1350
 * Comissão: - 40,5 (3%)
 * Saída Estoque: - 1100
 * 
 * = Saldo/lucro: 209,5 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Entity
public class Ciclo extends EntityBase<Long>{

	/**
	 * Representa um dos principais elementos do ciclo, que é aquele que o alimenta
	 * com um ou mais valores, permitindo o ciclo "rodar".
	 */
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	private Parceiro investidor;
	
	/**
	 * Data de início do ciclo.
	 */
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date inicio;
	
	/**
	 * Data de encerramento do ciclo.
	 */
	@Temporal(TemporalType.DATE)
	private Date fim;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="ciclo")
	private List<MovimentoValor> movimentacoes;

	public Parceiro getInvestidor() {
		return investidor;
	}

	public void setInvestidor(Parceiro investidor) {
		this.investidor = investidor;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	public List<MovimentoValor> getMovimentacoes() {
		return movimentacoes;
	}

	public void setMovimentacoes(List<MovimentoValor> movimentacoes) {
		this.movimentacoes = movimentacoes;
	}
	
	
}
