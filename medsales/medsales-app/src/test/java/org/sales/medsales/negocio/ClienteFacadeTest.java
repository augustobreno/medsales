package org.sales.medsales.negocio;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import junit.framework.Assert;

import org.junit.Test;
import org.sales.medsales.OnServerBaseTest;
import org.sales.medsales.dominio.Cliente;
import org.sales.medsales.exceptions.AlreadyRegisteredException;

public class ClienteFacadeTest extends OnServerBaseTest {

	@Inject
	private ClienteFacade clienteFacade;
	
	@Inject
	private EntityManager em;
	
    @Test
    public void simpleInsertAndFindTest() {

    	// cadastrando um cliente com os dados mais básicos.
    	Cliente cliente = new Cliente();
    	cliente.setNome("Cliente 1");
    	clienteFacade.save(cliente);
    	
    	// limpando o cache para verificação
    	em.clear();
    	
    	// buscando o cliente
    	Cliente found = clienteFacade.findBy(cliente.getId());
    	
    	Assert.assertNotNull(found);
    }
    
    /**
     * Não deve ser possível cadastrar mais de um cliente com o mesmo nome.
     */
    @Test
    public void duplicidadeTest() {

    	// cadastrando um cliente com os dados mais básicos.
    	Cliente cliente = new Cliente();
    	cliente.setNome("Cliente 1");
    	clienteFacade.save(cliente);
    	
    	// limpando o cache para verificação
    	em.clear();
    	
    	// cadastrando outro cliente com o mesmo nome
    	cliente = new Cliente();
    	cliente.setNome("Cliente 1");
    	
    	try {
    		clienteFacade.save(cliente);
    		Assert.fail("deveria ter ocorrido duplicidade");
    	} catch (AlreadyRegisteredException e) {
    		// exceção esperada
		}
    	
    }
}
