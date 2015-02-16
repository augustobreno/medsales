package org.sales.medsales.negocio;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.sales.medsales.MedSalesBaseTest;
import org.sales.medsales.api.exceptions.AlreadyRegisteredException;
import org.sales.medsales.dominio.Parceiro;

public class ParceiroFacadeTest extends MedSalesBaseTest {

	@Inject
	private ParceiroFacade parceiroFacade;
	
    @Test
    public void simpleInsertAndFindTest() {

    	// cadastrando um cliente com os dados mais básicos.
    	Parceiro cliente = new Parceiro();
    	cliente.setNome("Cliente 1");
    	parceiroFacade.save(cliente);
    	
    	// limpando o cache para verificação
    	getEm().clear();
    	
    	// buscando o cliente
    	Parceiro found = parceiroFacade.findBy(cliente.getId());
    	
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
    	parceiroFacade.save(cliente);
    	
    	// limpando o cache para verificação
    	getEm().clear();
    	
    	// cadastrando outro cliente com o mesmo nome
    	cliente = new Parceiro();
    	cliente.setNome("Cliente 1");
    	
    	try {
    		parceiroFacade.save(cliente);
    		Assert.fail("deveria ter ocorrido duplicidade");
    	} catch (AlreadyRegisteredException e) {
    		// exceção esperada
		}
    	
    }
}
