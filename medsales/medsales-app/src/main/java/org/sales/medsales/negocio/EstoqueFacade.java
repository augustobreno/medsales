package org.sales.medsales.negocio;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sales.medsales.dominio.Entrada;

/**
 * Implementa todas as operações de entrada, saída, manutenção e 
 * recuperação de dados do Estoque. Coração do módulo de estoque
 * @author Augusto
 *
 */
@SuppressWarnings("serial")
@BusinessExceptionHandler
@LocalBean
@Stateless
public class EstoqueFacade implements Facade {

	@Inject
	private EntradaBO entradaBO; 
	
	/**
	 * Cadastra uma entrada de produtos.
	 */
	public void cadastrarEntrada(Entrada entrada) {
		entradaBO.cadastrar(entrada);
	}
}
