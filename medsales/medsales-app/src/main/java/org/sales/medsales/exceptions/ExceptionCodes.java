package org.sales.medsales.exceptions;

/**
 * Estrutura para registro dos códigos de exceções.
 * @author Augusto
 *
 */
public interface ExceptionCodes {

	/**
	 * Códigos das exceções relacionadas ao ciclo.
	 */
	public interface CICLO {

		/** É necessário informar um ciclo */
		public static final String CICLO_REQUIRED = "CICLO01";
		
		/** É obrigatória a configuração da de início */
		public static final String INICIO_REQUIRED = "CICLO02";

		
	}

	
	/**
	 * Códigos das exceções relacionadas às movimentações do estoque.
	 */
	public interface MOVIMENTACAO_ESTOQUE {
		/** É necessário informar uma saída */
		public static final String MOVIMENTACAO_REQUIRED = "MOVIMENTACAO01";
		
		/** É obrigatória a configuração do tipo da operação */
		public static final String OPERACAO_REQUIRED = "MOVIMENTACAO02";
		
		/** É obrigatória informação do produto para consulta do saldo */
		public static final String SALDO_PRODUTO_REQUIRED = "MOVIMENTACAO03";
	}
	
	public interface SAIDA_ESTOQUE {
		/** É necessário informar o código da Entrada */
		public static final String GERAR_SAIDA_ENTRADA_REQUIRED = "GERARSAIDA01";		
	}
}
