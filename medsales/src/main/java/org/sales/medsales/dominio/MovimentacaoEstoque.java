package org.sales.medsales.dominio;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.sales.medsales.dominio.types.HibernateEnumType;

/**
 * Determina que os dados e comportamentos associados à movimentação de um item do
 * estoque. Os tipos naturais são ENTRADA e SAIDA.
 * O conjunto de todas as movimentações representam o estoque.
 * @author Augusto
 */
@SuppressWarnings("serial")

@TypeDef(name = "operacao", typeClass = HibernateEnumType.class, 
		parameters = { @Parameter(name = "enumClass", value = "org.sales.medsales.dominio.Operacao"), })

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public abstract class MovimentacaoEstoque extends EntityBase<Long> {

	/** determina o tipo de operação que este objeto deverá realizar. */
	@Type(type="operacao")
	private Operacao operacao;
	
	/**
	 * Itens associados a este tipo de movimentação.
	 */
	@OneToMany(fetch=FetchType.LAZY)
	private List<Item> itens;

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
	
}
