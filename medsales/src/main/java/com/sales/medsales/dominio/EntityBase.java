package com.sales.medsales.dominio;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings({ "serial", "rawtypes" })
@MappedSuperclass
public abstract class EntityBase<PK> implements Entity<PK> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private PK id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAlteracao;

	public PK getId() {
		return id;
	}

	public void setId(PK id) {
		this.id = id;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date criacao) {
		this.dataCriacao = criacao;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date alteracao) {
		this.dataAlteracao = alteracao;
	}

	protected String getLabel() {
		return this.getClass().getSimpleName() + " [" + getId() + "]";
	}
	
	@Override
	public String toString() {
		return getLabel();
	}

	@PrePersist
	public void initTimeStamps() {
		// we do this for the purpose of the demo, this lets us create our own
		// creation dates. Typically we would just set the createdOn field.
		if (dataCriacao == null) {
			dataCriacao = new Date();
		}
		dataAlteracao = dataCriacao;
	}

	@PreUpdate
	public void updateTimeStamp() {
		dataAlteracao = new Date();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityBase other = (EntityBase) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}