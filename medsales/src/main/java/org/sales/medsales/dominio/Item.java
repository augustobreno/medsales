package org.sales.medsales.dominio;

/**
 * Item para composição de um pedido. 
 * @author Augusto
 */
@SuppressWarnings("serial")
public class Item extends EntityBase<Long> {

	/**
	 * Preço do produto na data de criação do pedido.
	 */
	private PrecoProduto precoProduto;
	
	/**
	 * Quantidade de produtos pedidos.
	 */
	private Long quantidade;
	
}
