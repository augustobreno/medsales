package org.sales.medsales.api.web;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

@SuppressWarnings("serial")
public class MessageUtil implements Serializable {

	public void showMessage(Severity severity, String mensagem, Object... parametros) {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		String messageBundleName = facesContext.getApplication().getMessageBundle();

		String msgText;
		if (messageBundleName != null) {
			ResourceBundle bundle = ResourceBundle.getBundle(messageBundleName);
			msgText = bundle.getString(mensagem);
		} else {
			// se não há mensagem associada não se trata de uma chave mas da
			// própria mensagem
			msgText = mensagem;
		}

		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(severity, MessageFormat.format(msgText, parametros), null));
	}

	/**
	 * Exibe mensagem de informação na tela.
	 * 
	 * @param mensagem
	 *            Mensagem ou chave.
	 * @param parametros
	 *            para montagem da mensagem.
	 */
	public void showInfoMessage(String mensagem, Object... parametros) {
		showMessage(FacesMessage.SEVERITY_INFO, mensagem, parametros);
	}

	/**
	 * Exibe mensagem de Advertencia na tela.
	 * 
	 * @param mensagem
	 *            Mensagem ou chave.
	 * @param parametros
	 *            para montagem da mensagem.
	 */
	public void showWarn(String mensagem, Object... parametros) {
		showMessage(FacesMessage.SEVERITY_WARN, mensagem, parametros);
	}

	/**
	 * Exibe mensagem de erro na tela.
	 * 
	 * @param mensagem
	 *            Mensagem ou chave.
	 * @param parametros
	 *            para montagem da mensagem
	 */
	public void showError(String mensagem, Object... parametros) {
		showMessage(FacesMessage.SEVERITY_ERROR, mensagem, parametros);
	}

}
