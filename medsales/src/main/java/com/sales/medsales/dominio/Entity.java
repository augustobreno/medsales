package com.sales.medsales.dominio;

import java.io.Serializable;

/**
 * Interface para as entidades de domínio. 
 * @author augusto
 */
public interface Entity <PK> extends Serializable {

	public PK getId();
		
}
