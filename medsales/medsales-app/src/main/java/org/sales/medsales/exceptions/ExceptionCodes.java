package org.sales.medsales.exceptions;

/**
 * Estrutura para registro dos códigos de exceções.
 * @author Augusto
 *
 */
public interface ExceptionCodes {

	/**
	 * Códigos das exceções relacionadas às movimentações do estoque.
	 */
	public interface MOVIMENTACAO {
		/** É necessário informar uma saída */
		public static final String MOVIMENTACAO_REQUIRED = "MOVIMENTACAO001";
		
		/** É obrigatória a configuração do tipo da operação */
		public static final String OPERACAO_REQUIRED = "MOVIMENTACAO002";
		
		/** É obrigatória informação do produto para consulta do saldo */
		public static final String SALDO_PRODUTO_REQUIRED = "MOVIMENTACAO003";
	}
	
	public interface SAIDA {
		/** É necessário informar o código da Entrada */
		public static final String GERAR_SAIDA_ENTRADA_REQUIRED = "GERARSAIDA001";		
	}
}
