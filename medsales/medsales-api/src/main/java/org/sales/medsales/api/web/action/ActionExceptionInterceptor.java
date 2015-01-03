package org.sales.medsales.api.web.action;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.sales.medsales.api.exceptions.AppException;
import org.sales.medsales.api.exceptions.BusinessException;
import org.sales.medsales.api.exceptions.ExceptionMessage;
import org.sales.medsales.api.util.StringUtil;
import org.sales.medsales.api.web.MessageUtil;

/**
 * Responsável pela interceptação das exceções e realização de tratamentos genéricos. Qualquer exceção capturada
 * terá sua mensagem transformada em uma mensagem JSF, para ser exibida na interface com o usuário, caso a tag 
 * "message" esteja declarada no xhtml. Também é realizado um tratamento específico para casos de validação do hibernate,
 * buscando exibir mensagens apropriadas.
 * 
 * Este intercepator é ativado através da utilização da anotação "@ExceptionHandler" em componentes Seam
 * da camada de controle.
 */

@SuppressWarnings("serial")
@Interceptor
@ActionExceptionHandler
public class ActionExceptionInterceptor implements Serializable {

	@Inject
	private Logger log;

	@Inject
	private MessageUtil messageUtil;
	
	/* para identificar chamadas recorrentes dentro do mesmo objeto */
	private boolean recurrent;

	/**
	 * Método interceptador que envolve a chamada ao componente
	 */
	@AroundInvoke
	public Object aroundInvoke(InvocationContext invocation) throws Exception {

		if (recurrent) {
			return invocation.proceed();
		} else {
			recurrent = true;
			Object result = null;
			
			try {
				// executa o método interceptado
				result = invocation.proceed();
				return result;
				
			} catch (BusinessException ne) {
				// trata mensagens de validação de negócio
				checkBusinessException(ne);
				return result;
				
			} catch (AppException e) {
				throw e;
			} catch (Throwable ne) {
				throw new Exception("Erro não esperado pela aplicação.", ne);
				
			} finally {
				recurrent = false;
			}
		}
	}
	
	/**
	 * Indica ao Seam se o intercepator está abilitado.
	 * Veja mais em {@link #isInterceptorEnabled()} 
	 */
	public boolean isInterceptorEnabled() {
		return true;
	}

	/**
	 * Cria mensagem do tipo ERROR a ser exibida para o usuário
	 */
	protected void checkBusinessException(BusinessException e) {

		for (ExceptionMessage em : e.getMessages()) {
			if (!StringUtil.isStringEmpty(em.getMessage())) {
				String mensagem = em.getCode() != null ? em.getCode()  + " - " + em.getMessage() : em.getMessage(); 
				messageUtil.showError(mensagem, em.getParameters());
				log.log(Level.FINE, "Adicionando mensagem para o usuário da validação de negócio : " + mensagem);
			}
		}
	}

}