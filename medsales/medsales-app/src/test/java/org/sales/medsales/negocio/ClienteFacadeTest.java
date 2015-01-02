package org.sales.medsales.negocio;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.sales.medsales.dominio.Parceiro;
import org.sales.medsales.exceptions.AlreadyRegisteredException;
import org.sales.medsales.test.OnServerBaseTest;

public class ClienteFacadeTest extends OnServerBaseTest {

	@Inject
	private ParceiroFacade clienteFacade;
	
    @Test
    public void simpleInsertAndFindTest() {

    	// cadastrando um cliente com os dados mais básicos.
    	Parceiro cliente = new Parceiro();
    	cliente.setNome("Cliente 1");
    	clienteFacade.save(cliente);
    	
    	// limpando o cache para verificação
    	getEm().clear();
    	
    	// buscando o cliente
    	Parceiro found = clienteFacade.findBy(cliente.getId());
    	
    	Assert.assertNotNull(found);
    }
    
    /**
     * Não deve ser possível cadastrar mais de um cliente com o mesmo nome.
     */
    @Test
    public void duplicidadeTest() {

    	// cadastrando um cliente com os dados mais básicos.
    	Parceiro cliente = new Parceiro();
    	cliente.setNome("Cliente 1");
    	clienteFacade.save(cliente);
    	
    	// limpando o cache para verificação
    	getEm().clear();
    	
    	// cadastrando outro cliente com o mesmo nome
    	cliente = new Parceiro();
    	cliente.setNome("Cliente 1");
    	
    	try {
    		clienteFacade.save(cliente);
    		Assert.fail("deveria ter ocorrido duplicidade");
    	} catch (AlreadyRegisteredException e) {
    		// exceção esperada
		}
    	
    }
}
