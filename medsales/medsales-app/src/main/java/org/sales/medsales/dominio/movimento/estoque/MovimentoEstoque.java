package org.sales.medsales.dominio.movimento.estoque;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.sales.medsales.api.dominio.types.HibernateEnumType;
import org.sales.medsales.dominio.movimento.Movimento;

/**
 * Determina que os dados e comportamentos associados à movimentação de um item
 * do estoque. Os tipos naturais são ENTRADA e SAIDA. O conjunto de todas as
 * movimentações representam o estoque.
 * 
 * @author Augusto
 */
@SuppressWarnings("serial")
@TypeDefs({ @TypeDef(name = "status", typeClass = HibernateEnumType.class, parameters = { @Parameter(name = "enumClass", value = "org.sales.medsales.dominio.movimento.estoque.Status"), }) })
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public abstract class MovimentoEstoque extends Movimento {

	/**
	 * Determina o status atual desta movimentação
	 */
	@Type(type = "status")
	@Column(nullable = false)
	private Status status;

	/**
	 * Itens associados a este tipo de movimentação.
	 */
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "movimentoEstoque", orphanRemoval = true)
	private List<Item> itens;

	public MovimentoEstoque() {
		defineOperacao();
	}

	/**
	 * Ponto de extensão para configuração da operação padrão da entidade
	 * concreta.
	 */
	protected abstract void defineOperacao();

	public List<Item> getItens() {
		return itens;
	}

	public void setItens(List<Item> itens) {
		this.itens = itens;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * Label para apresentação do tipo da movimentação.
	 */
	public abstract String getLabel();

	/**
	 * @return Valor total dos itens associados a este movimento, aplicando
	 *         qualquer ajuste ou fórmula específica da movimentação. A
	 *         implementação original retorna o mesmo valor de {@link #calcularSomaValoresItens()},
	 *         visto que não há uma fórmula genérica associada a {@link MovimentoEstoque}.
	 */
	public BigDecimal calcularTotal() {
		return calcularSomaValoresItens();
	}

	/**
	 * @return Valor total dos itens associados a este movimento.
	 */
	public BigDecimal calcularSomaValoresItens() {
		BigDecimal total = BigDecimal.ZERO;
		if (itens != null) {
			BigDecimal valor;
			BigDecimal quantidade;
			for (Item item : itens) {
				valor = item.getPrecoProduto().getValor();
				quantidade = new BigDecimal(item.getQuantidade());
				total = total.add(valor.multiply(quantidade));
			}
		}
		return total;
	}

}
