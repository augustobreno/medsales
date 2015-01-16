package org.sales.medsales.dominio.movimentacao;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.sales.medsales.api.dominio.EntityBase;
import org.sales.medsales.api.dominio.types.HibernateEnumType;
import org.sales.medsales.dominio.Item;
import org.sales.medsales.dominio.Parceiro;

/**
 * Determina que os dados e comportamentos associados à movimentação de um item do
 * estoque. Os tipos naturais são ENTRADA e SAIDA.
 * O conjunto de todas as movimentações representam o estoque.
 * @author Augusto
 */
@SuppressWarnings("serial")

@TypeDefs ({
	@TypeDef(name = "operacao", typeClass = HibernateEnumType.class, 
		parameters = { @Parameter(name = "enumClass", value = "org.sales.medsales.dominio.movimentacao.Operacao"), }),

	@TypeDef(name = "status", typeClass = HibernateEnumType.class, 
		parameters = { @Parameter(name = "enumClass", value = "org.sales.medsales.dominio.movimentacao.Status"), })		
})

@Inheritance(strategy = InheritanceType.JOINED) 
@Entity
public abstract class MovimentacaoEstoque extends EntityBase<Long> {

	/**
	 * Data oficial da movimentação.
	 */
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date dataMovimentacao;
	
	/** determina o tipo de operação que este objeto deverá realizar. */
	@Type(type="operacao")
	@Column(nullable=false)
	private Operacao operacao;
	
	/**
	 * Determina o status atual desta movimentação
	 */
	@Type(type="status")
	@Column(nullable=false)
	private Status status;
	
	/**
	 * Itens associados a este tipo de movimentação.
	 */
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="movimentacaoEstoque")
	private List<Item> itens;

	/**
	 * Parceiro, de onde os itens foram adquiridos, ou para quem os itens foram vendidos.
	 * A denpender do tipo da movimentação.
	 */
	@ManyToOne
	private Parceiro parceiro;
	
	public MovimentacaoEstoque() {
		defineOperacao();
	}
	
	/**
	 * Ponto de extensão para configuração da operação padrão da entidade concreta.
	 */
	protected abstract void defineOperacao();
	
	public Operacao getOperacao() {
		return operacao;
	}

	public void setOperacao(Operacao operacao) {
		this.operacao = operacao;
	}

	public List<Item> getItens() {
		return itens;
	}

	public void setItens(List<Item> itens) {
		this.itens = itens;
	}

	public Parceiro getParceiro() {
		return parceiro;
	}

	public void setParceiro(Parceiro parceiro) {
		this.parceiro = parceiro;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getDataMovimentacao() {
		return dataMovimentacao;
	}

	public void setDataMovimentacao(Date dataMovimentacao) {
		this.dataMovimentacao = dataMovimentacao;
	}
	
	/**
	 * Label para apresentação do tipo da movimentação.
	 */
	public abstract String getLabel();
	
}
