package com.sales.medsales.dominio;

import java.io.Serializable;

import br.jus.trt.lib.qbe.api.Identifiable;

/**
 * Interface para as entidades de domínio. 
 * @author augusto
 */
public interface Entity <PK> extends Serializable, Identifiable {

	public PK getId();
		
}
