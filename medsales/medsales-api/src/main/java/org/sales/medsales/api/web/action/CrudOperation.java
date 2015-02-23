package org.sales.medsales.api.web.action;

/** Constantes para controle da operação corrente */
public enum CrudOperation {
	SEARCH("S"), EDIT("E"), INSERT("I");
	
	private String operation;
	
	CrudOperation(String operation) {
		this.operation = operation;
	}
	
	/**
	 * @param key Valor que representa uma operação CRUD.
	 * @return CrudOperation compatível com a chave informada.
	 */
	public static CrudOperation find(String key) {
		CrudOperation cp = null;
		for (CrudOperation crudOperation : CrudOperation.values()) {
			if (crudOperation.equals(key)) {
				cp = crudOperation;
				break;
			}
		}
		
		return cp;
	}
	
	/**
	 * Verifica se a operação informada (String) é equivalente a este enum (this).
	 * @param anOperation Representação em String de uma operação para comparação.
	 * @return true se esta representação em String for equivalente ao nome ou à operação deste enum (this)
	 */
	public boolean equals(String anOperation) {
		return this.operation.equalsIgnoreCase(anOperation)
				|| this.name().equalsIgnoreCase(anOperation);
	}

	/**
	 * Verifica se o CrudOperation corrente é igual a pelo menos um dos informados via parâmetro.
	 * @param operations Operations para verificação. 
	 * @return true se houve pelo menos uma igualdade.
	 */
	public boolean in(String...operations) {
		boolean isItIn = false;
		for (String op : operations) {
			isItIn = equals(op);
			if (isItIn) {
				break;
			}
		}
		return isItIn;
	}
	
	/**
	 * Verifica se o CrudOperation corrente é igual a pelo menos um dos informados via parâmetro.
	 * @return true se houve pelo menos uma igualdade.
	 */
	public boolean in(String op1, String op2) {
		return in(new String[]{op1, op2});
	}
	
	public String getOperation() {
		return operation;
	}
	
}