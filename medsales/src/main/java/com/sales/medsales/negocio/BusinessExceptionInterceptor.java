package com.sales.medsales.negocio;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.apache.logging.log4j.Logger;

import com.sales.medsales.exceptions.AppException;
import com.sales.medsales.exceptions.BusinessException;

/**
 * Responsável pela interceptação das exceções e realização de tratamentos genéricos da camada de negócio. 
 * Qualquer exceção será encapsulada em uma TRT6Exception e relançada, para o devido tratamento na camada de controle.
 * 
 * Este intercepator é ativiado através da utilização da anotação "@ExceptionManagerHandler" em componentes Seam
 * da camada de controle.
 */

@SuppressWarnings("serial")
@BusinessExceptionHandler
@Interceptor
public class BusinessExceptionInterceptor implements Serializable {

	@Inject
	private Logger log;

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
				// relançando mensagem de negócio
				log.error("Relançando validação de negócio" , ne);
				throw ne;
				
			} catch (AppException ne) {
				log.error("Erro não identificado" , ne);
				throw ne;
				
			} catch (Throwable ne) {
				log.error("Erro não esperado pela aplicação. Encapsulando em um TRT6Expetion", ne);
				throw new AppException(ne);
				
			} finally {
				recurrent = false;
			}
		}
	}

}