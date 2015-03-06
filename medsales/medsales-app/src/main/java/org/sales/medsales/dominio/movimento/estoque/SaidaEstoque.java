package org.sales.medsales.dominio.movimento.estoque;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.sales.medsales.dominio.movimento.Operacao;

/**
 * Caracteriza uma movimentação de Saída, que remove itens ao estoque. 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Entity
public class SaidaEstoque extends MovimentoEstoque {

	/**
	 * Porcentagem de desconto aplicado sobre uma movimentação de saída.
	 */
	@Column(precision=4, scale=2)
	private Double desconto;

	/**
	 * Numero sequencial para identificação de um pedido por cliente.
	 */
	private Integer numeroPedido;
	
	/**
	 * Valores das notas originais de compras dos produtos.
	 */
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="saidaEstoque", orphanRemoval=true)
	private List<NotaCompra> notasCompra;
	
	@Override
	protected void defineOperacao() {
		setOperacao(Operacao.SAIDA);
	}
	
	public Double getDesconto() {
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	@Override
	public String getLabel() {
		return "Saída";
	}

	public Integer getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(Integer numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public List<NotaCompra> getNotasCompra() {
		return notasCompra;
	}

	public void setNotasCompra(List<NotaCompra> notasCompra) {
		this.notasCompra = notasCompra;
	}
	
	/**
	 * @return O valor total das notas de compras originais.
	 */
	public BigDecimal calcularValorNotaOriginal() {
		BigDecimal valor = BigDecimal.ZERO;
		if (this.notasCompra != null) {
			for (NotaCompra notaCompra : notasCompra) {
				valor = valor.add(notaCompra.getValor());
			}
		}
		return valor;
	}
	
	/**
	 * @param valor Novo valor a ser adicionado na lista de notas de compras originais.
	 */
	public void addValorNota(BigDecimal valor) {
		if (this.notasCompra == null) {
			this.notasCompra = new ArrayList<NotaCompra>();
		}

		NotaCompra notaCompra = new NotaCompra();
		notaCompra.setValor(valor);
		notaCompra.setSaidaEstoque(this);
		this.notasCompra.add(notaCompra);
		
	}
	
	/**
	 * @param nota Nota a ser removida.
	 */
	public void removeValorNota(NotaCompra nota) {
		if (this.notasCompra != null) {
			this.notasCompra.remove(nota);
		}	
		
	}
}
