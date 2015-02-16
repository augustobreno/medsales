package org.sales.medsales.negocio;

import java.util.Date;

import javax.inject.Inject;

import junit.framework.Assert;

import org.easy.testeasy.dataloader.LoadData;
import org.junit.Test;
import org.sales.medsales.MedSalesBaseTest;
import org.sales.medsales.dataLoader.ParceirosDataLoader;
import org.sales.medsales.dominio.Ciclo;
import org.sales.medsales.dominio.Parceiro;

/**
 * Testes de integração da classe {@link CicloFacade}
 * @author Augusto
 */
@LoadData(dataLoader=ParceirosDataLoader.class)
public class CicloFacadeTest extends MedSalesBaseTest {

	@Inject
	private CicloFacade cicloFacade;

    @Test
    public void simpleInsertAndFindTest() {

    	// cadastrando um ciclo básico.
    	Ciclo ciclo = new Ciclo();
    	ciclo.setInvestidor(getQuerier().findAny(Parceiro.class));
    	ciclo.setInicio(new Date());
    	
    	cicloFacade.save(ciclo);
    	
    	// limpando o cache para verificação
    	getEm().clear();
    	
    	// buscando o ciclo
    	Ciclo found = cicloFacade.findBy(ciclo.getId());
    	Assert.assertNotNull(found);
    }
    
}
