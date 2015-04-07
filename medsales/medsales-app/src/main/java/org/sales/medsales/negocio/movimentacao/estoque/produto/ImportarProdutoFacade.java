package org.sales.medsales.negocio.movimentacao.estoque.produto;

import java.io.InputStream;
import java.io.Serializable;

import javax.inject.Inject;

import org.sales.medsales.api.negocio.BusinessExceptionHandler;

/*
 * Propositadamente não é um EJB, devido o grande tamanho desta operação, o EJB ficou na ponta do processamente,
 * realizando a importação em partes menores (infelizmente a operação não é totalmente atômica).
 */
@SuppressWarnings("serial")
@BusinessExceptionHandler
public class ImportarProdutoFacade implements Serializable {

	@Inject
	private ImportarProdutoBO importarProdutoBo;
	
    /**
     * Importa preços e produtos.
     * @param produtosInput Stream para arquivo de TEXTO com o formato:
     * <br/><br/>
     * [código de barras]##[nome do produto]##[preco]
     * <br/><br/>
     * 7898149937911##ZYDENA 100MG C/1 CPR##36,55 
     */
	public void importar(InputStream produtosInput) {
		importarProdutoBo.importar(produtosInput);
	}

}
