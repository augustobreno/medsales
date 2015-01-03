package org.sales.medsales.api.persistencia;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;

import org.easy.qbeasy.api.OperatorProcessorRepository;
import org.easy.qbeasy.api.QBERepository;
import org.easy.qbeasy.repository.criteria.CriteriaQbeRepository;
import org.easy.qbeasy.repository.criteria.OperatorProcessorRepositoryFactory;

/**
 * Para produção de DAOs concretos inseridos em um contexto de injeção de dependência. 
 * @author augusto
 */
public class QbeFactory {

	/**
	 * Cria uma instância configurada de {@link QBERepository} para funcionar em uma estrutura de banco de dados.
	 * @param entityManager EntityManager default configurado na aplicação.
	 * @param processorRepository Repositório de processadores de operadores configurado com todos aqueles
	 * disponibilizado nativamente no módulo QBE.  
	 * 
	 * @return Repositório QBE configurado para operar sobre o banco de dados através do EntityManager.
	 * @throws Exception
	 */
	
	@Produces 
	@Default
	public QBERepository create(final EntityManager entityManager, final OperatorProcessorRepository processorRepository) throws Exception {
		CriteriaQbeRepository criteriaQbeRepository = new CriteriaQbeRepository(entityManager, processorRepository);
		return criteriaQbeRepository;	
	}
	
	/**
	 * @return Um repositório de processadores de operadores para uso em um mecanismo de QBE 
	 * @throws Exception
	 */
	@Produces 
	@Default
	public OperatorProcessorRepository create() throws Exception {
		OperatorProcessorRepository processorRepository = OperatorProcessorRepositoryFactory.create();
		return processorRepository;	
	}
}
