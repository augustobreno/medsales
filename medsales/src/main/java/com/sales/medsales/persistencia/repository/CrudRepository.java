package com.sales.medsales.persistencia.repository;

import java.io.Serializable;
import java.util.List;

import com.sales.medsales.dominio.Entity;

/**
 * Interface para operações de consulta de dados orientado à uma entidade de domínio.
 * @author augusto
 *
 * @param <ENTITY>
 */
public interface CrudRepository<ENTITY extends Entity<PK>, PK extends Serializable>{

	public Long count();
	
	public ENTITY findBy(PK pk);
	
	public List<ENTITY> findAll();

	public void remove(ENTITY entity);

	public ENTITY insert(ENTITY entity);
	
	public ENTITY update(ENTITY entity);

}