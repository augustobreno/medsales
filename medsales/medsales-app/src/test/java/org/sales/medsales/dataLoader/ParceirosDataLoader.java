package org.sales.medsales.dataLoader;

import org.easy.testeasy.dataloader.HibernateDataLoader;
import org.sales.medsales.dominio.Parceiro;

/**
 * Data loader que cria uma base de dados de Parceiros.
 * @author Augusto
 *
 */
public class ParceirosDataLoader extends HibernateDataLoader {

	@Override
	public void load() throws Exception {
		Parceiro parceiro;
		
		for (int i = 0; i < 10; i++) {
			parceiro = new Parceiro();
			parceiro.setNome("Produto " + i);
			getEntityManager().persist(parceiro);
		}
		
		getEntityManager().flush();
	}

}
