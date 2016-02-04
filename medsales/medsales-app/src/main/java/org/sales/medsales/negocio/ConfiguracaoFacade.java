package org.sales.medsales.negocio;

import javax.ejb.Stateless;

import org.sales.medsales.api.negocio.CrudFacadeBase;
import org.sales.medsales.dominio.Configuracao;
import org.sales.medsales.persistencia.repository.ConfiguracaoRepository;

@SuppressWarnings("serial")
@Stateless
public class ConfiguracaoFacade extends CrudFacadeBase<ConfiguracaoRepository, Configuracao, Long>{

	public Configuracao getConfiguracao() {
		return findBy(1L); // só deve haver uma configuracao cadastrada.
	}
}
