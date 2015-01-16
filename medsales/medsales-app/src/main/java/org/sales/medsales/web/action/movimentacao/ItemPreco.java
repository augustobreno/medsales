package org.sales.medsales.web.action.movimentacao;

import org.sales.medsales.dominio.Item;
import org.sales.medsales.dominio.PrecoProduto;

/**
 * Decorator para exibição conjunta dos dados do Item e o preço cadastro.
 * 
 * @author Augusto
 */
public class ItemPreco {

	private Item item;
	private PrecoProduto preco;

	public ItemPreco() {
		super();
	}

	public ItemPreco(Item item, PrecoProduto preco) {
		super();
		this.item = item;
		this.preco = preco;
	}

	public PrecoProduto getPreco() {
		return preco;
	}

	public void setPreco(PrecoProduto preco) {
		this.preco = preco;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * A chave de comparação real é o código de barras em
	 * itemPreco.item.produto.codigoBarras
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof ItemPreco) {
			ItemPreco outroItem = (ItemPreco) obj;
			return this.getItem().getProduto().getCodigoBarras()
					.equals(outroItem.getItem().getProduto().getCodigoBarras());
		}
		return false;
	}
}
