package org.sales.medsales.api.dominio;

import java.io.Serializable;

import org.easy.qbeasy.api.Identifiable;

/**
 * Interface para as entidades de domínio. 
 * @author augusto
 */
public interface Entity <PK> extends Serializable, Identifiable {

	public PK getId();
		
	public void setId(PK pk);
}
