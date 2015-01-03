package org.sales.medsales.api.web.action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

import org.sales.medsales.api.exceptions.BusinessException;

/**
 * Esta anotação produz um efeito de tratamento genérico de exceções. Deve ser colocada sobre classes
 * da camada de controle (Action). As exceções que "subirem" da camada de negócio seerão devidamente tratadas. 
 * Exceções do tipo {@link BusinessException} serão convertidas em mensagens para o usuário, as demais
 * exceções produzirão o redirecionamento para a página principal da aplicação, com uma mensagem
 * de "erro inesperado".
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@InterceptorBinding
public @interface ActionExceptionHandler {

}
