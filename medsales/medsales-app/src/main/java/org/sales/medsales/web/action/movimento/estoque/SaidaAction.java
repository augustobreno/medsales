package org.sales.medsales.web.action.movimento.estoque;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import org.sales.medsales.dominio.movimento.estoque.Produto;
import org.sales.medsales.dominio.movimento.estoque.SaidaEstoque;
import org.sales.medsales.dominio.movimento.estoque.SaldoProdutoVO;

/**
 * Mantém o fluxo de cadastro e manutenção de saída (venda) de produtos.
 * 
 * @author Augusto
 *
 */
@SuppressWarnings("serial")
@Named
@ConversationScoped
public class SaidaAction extends CriarMovimentacaoBaseAction<SaidaEstoque> {

	/** armazena os saldos dos produtos consultados */
	private Map<Long, SaldoProdutoVO> saldos = new HashMap<>();
	
	@Override
	protected void initMovimentacao() {
		SaidaEstoque saidaEstoque = new SaidaEstoque();
		saidaEstoque.setDataMovimento(new Date());
		setMovimentacao(saidaEstoque);
	}
	
	@Override
	protected void salvar() {
		getEstoqueFacade().cadastrar(getMovimentacao());
	}
	
	@Override
	protected void postAdicionarItem(ItemPreco itemPreco) {
		super.postAdicionarItem(itemPreco);
		consultarSaldo(itemPreco.getItem().getProduto());
	}

	@Override
	protected void postLoadId() {
		super.postLoadId();
		consultarSaldo(getProdutos());
	}
	
	private Produto[] getProdutos() {
		List<Produto> produtos = new ArrayList<Produto>();
		
		for (ItemPreco itemPreco : getItens()) {
			produtos.add(itemPreco.getItem().getProduto());
		}
		
		return produtos.toArray(new Produto[]{});
	}

	/**
	 * Consulta o saldo em estoque dos produtos.
	 */
	private void consultarSaldo(Produto...produtos) {
		// quando adicionar apenas um produto, verifica se o saldo já foi consultado
		if (produtos != null 
				&& produtos.length > 0	
				&& (produtos.length > 1 || !isSaldoConsultado(produtos[0]))) {
			
			List<SaldoProdutoVO> saldoList = getEstoqueFacade().consultarEstoque(produtos);
			for (SaldoProdutoVO saldo : saldoList) {
				saldos.put(saldo.getIdProduto(), saldo);
			}
			
		}	
	}
	
	private boolean isSaldoConsultado(Produto produto) {
		return saldos.containsKey(produto.getId());
	}

	/** @return O saldo do produto consultado */
	public SaldoProdutoVO getSaldo(Produto produto) {
		return saldos.get(produto.getId());
	}
}
