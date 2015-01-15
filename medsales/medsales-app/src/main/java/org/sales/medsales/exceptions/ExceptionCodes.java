package org.sales.medsales.exceptions;

/**
 * Estrutura para registro dos códigos de exceções.
 * @author Augusto
 *
 */
public interface ExceptionCodes {

	/**
	 * Códigos das exceções relacionadas à saída do estoque.
	 */
	public interface MOVIMENTACAO {
		/** É necessário informar uma saída */
		public static final String MOVIMENTACAO_REQUIRED = "MOVIMENTACAO001";
		
		/** É obrigatória a configuração do tipo da operação */
		public static final String OPERACAO_REQUIRED = "MOVIMENTACAO002"; 
	}
}
