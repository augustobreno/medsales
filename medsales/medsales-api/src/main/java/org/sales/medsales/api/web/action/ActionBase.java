package org.sales.medsales.api.web.action;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import org.sales.medsales.api.web.MessageUtil;

/**
 * Classe base para controle da camada de apresentação.
 * @author augusto
 *
 */
@SuppressWarnings("serial")
@ActionExceptionHandler
public class ActionBase implements Serializable {
	
    @Inject
    protected Logger log;
    
	@Inject
	private MessageUtil messageUtil; 
	
	/**
	 * Método invocado no momento  em que o componente é instanciado pelo próprio Seam.
	 */
	@PostConstruct
	public void init() {
	}	
	
	/**
	 * @param conversation Conversation para iniciar ou continuar.
	 */
	protected void beginOrJoin(Conversation conversation) {
		if (conversation.isTransient()) {
			conversation.begin();
		}
	}
	
	/**
	 * Exibe mensagem de informação na tela.
	 * @param mensagem Mensagem ou chave.
	 * @param parametros para montagem da mensagem.
	 */
	protected void showInfoMessage(String mensagem, Object...parametros) {
		messageUtil.showMessage(FacesMessage.SEVERITY_INFO, mensagem, parametros);
	}

	/**
	 * Exibe mensagem de Advertencia na tela.
	 * @param mensagem Mensagem ou chave.
	 * @param parametros para montagem da mensagem.
	 */
	protected void showWarnMessage(String mensagem, Object...parametros) {
		messageUtil.showMessage(FacesMessage.SEVERITY_WARN, mensagem, parametros);
	}	
	
	/**
	 * Exibe mensagem de erro na tela.
	 * @param mensagem Mensagem ou chave.
	 * @param parametros para montagem da mensagem 
	 */
	protected void showErrorMessage(String mensagem, Object...parametros) {
		messageUtil.showMessage(FacesMessage.SEVERITY_ERROR, mensagem, parametros);
	}
	
}
