package org.sales.medsales.dominio;

import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import junit.framework.Assert;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sales.medsales.dominio.Parceiro;
import org.sales.medsales.dominio.EntityBase;
import org.sales.medsales.negocio.ParceiroFacade;
import org.sales.medsales.test.OnServerBaseTest;

/**
 * Testa o comportamento básico de uma entidade de domínio {@link EntityBase}.
 * @author Augusto
 *
 */
@RunWith(Arquillian.class)
public class EntityBaseTest extends OnServerBaseTest {

	@Inject
	private ParceiroFacade clienteFacade;
	
	@Inject
	private EntityManager em;
	
	/**
	 * Testa o evento @PrePersist
	 */
    @Test
    public void prePersistEventTest() {

    	// cadastrando um cliente com os dados mais básicos.
    	Parceiro cliente = new Parceiro();
    	cliente.setNome("Cliente 1");
    	clienteFacade.save(cliente);

    	/*
    	 * Salvando a data em que o objeto foi persistido para futura comparação. No entanto,
    	 * é sabido que deve haver uma minúscula diferença de milésimos de segundos entre esta data
    	 * e a data encontrada no cliente.
    	 */
    	Date saveDate = new Date();
    	
    	// limpando o cache para verificação
    	em.clear();
    	
    	// buscando o cliente
    	Parceiro found = clienteFacade.findBy(cliente.getId());
    	
    	// checando as propriedades preenchidas no evento
    	Assert.assertNotNull(found);
    	Assert.assertNotNull(found.getDataCriacao());
    	Assert.assertNotNull(found.getDataAlteracao());
    	Assert.assertEquals(found.getDataCriacao(), found.getDataAlteracao());
    	
    	// checando a corretude dos valores. será considerado aceitável
    	// uma diferença de até 1 segundo (valor escolhido sem muito critério)
    	long differenceMilliSeconds = found.getDataCriacao().getTime() - saveDate.getTime();
    	Assert.assertTrue(differenceMilliSeconds < 1000);
    	
    }
    
    /**
	 * Testa o evento @PreUpdate
	 */
    @Test
    public void preUpdateEventTest() {

    	// cadastrando um cliente com os dados mais básicos.
    	Parceiro cliente = new Parceiro();
    	cliente.setNome("Cliente 1");
    	clienteFacade.save(cliente);

    	// limpando o cache para verificação
    	em.clear();
    	
    	// criando um atraso de 100ms para garantir uma diferença mínima entre a operação 
    	// de inclusão e alteração. 
    	sleep(100);
    	
    	// buscando o cliente
    	Parceiro cliente2 = clienteFacade.findBy(cliente.getId());
    	
    	// checando as propriedades preenchidas no evento
    	Assert.assertEquals(cliente2.getDataCriacao(), cliente2.getDataAlteracao());
    	
    	// alterando o objeto para disparar o evento
    	cliente2.setNome("Cliente 2");
    	clienteFacade.save(cliente2);
    	
    	em.clear();
    	
    	cliente2 = clienteFacade.findBy(cliente.getId());
    	
    	// checando a corretude dos valores. será considerado aceitável
    	// uma diferença de até 5 segundos (valor escolhido sem muito critério)
    	long differenceMilliSeconds = cliente2.getDataAlteracao().getTime() - cliente2.getDataCriacao().getTime();
    	Assert.assertTrue(differenceMilliSeconds >= 100);
    	
    }

}
