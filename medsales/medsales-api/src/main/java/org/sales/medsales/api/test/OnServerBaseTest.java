package org.sales.medsales.api.test;

import java.io.File;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filter;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.junit.runner.RunWith;

/**
 * Classe base para testes realizados no servidor de aplicação.
 * Suas principais atividades são: Ativar o Arquillian; Configurar o arquivo para
 * deploy dos testes no servidores de aplicação; Gerenciar a transação via Arquillian (realiza
 * rollback por padrão) 
 * @author Augusto
 *
 */
@RunWith(Arquillian.class)
@Transactional(value=TransactionMode.ROLLBACK)
@ArquillianSuiteDeployment // garante a reutilização do @Deployment entre as classes de teste
public class OnServerBaseTest {

	private static final String ORG_SALES_MEDSALES = "org/sales/medsales";
	
	private static final String ORG_SALES_MEDSALES_API_SHORT = "orgsalesmedsalesapi";

	/**
	 * EM para acesso direto à base
	 */
	@Inject
	private EntityManager em;
	
	@Inject
	private QuerierUtil querierUtil;
	
    @Deployment
    public static Archive<?> createDeployment() {
    	
    	// carregando configuração de dependências do pom
    	PomEquippedResolveStage pom = Maven.resolver().loadPomFromFile("pom.xml");
    	
    	// seleciona as dependências do projeto
        File[] libs = pom.importDependencies(ScopeType.COMPILE).resolve().withTransitivity().asFile();
    	
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
        	.addPackages(true, getClassFilter(), ORG_SALES_MEDSALES)
        	.addAsLibraries(libs)
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource("test-beans.xml", "beans.xml");
        
        System.out.println(war.toString(true));
		return war;
    }	
    
    /**
     * @return FIltro que evita todas as classes do pacote de medsales-api.
     */
    protected static Filter<ArchivePath> getClassFilter() {
		return new Filter<ArchivePath>() {
			@Override
			public boolean include(ArchivePath path) {
				String classPath = path.get();
				classPath = classPath.replaceAll("/", "");
				return !classPath.startsWith(ORG_SALES_MEDSALES_API_SHORT);
			}
		};
	}

	/**
     * Provoca uma pausa na execução da thread corrente.
     * @param time Tempo em ms de pausa. 
     */
    protected void sleep(int time) {
    	try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			throw new RuntimeException("Não foi possível fazer a thread dormir.", e);
		}
	}

	protected EntityManager getEm() {
		return em;
	}

	protected void setEm(EntityManager em) {
		this.em = em;
	}

	protected QuerierUtil getQuerierUtil() {
		return querierUtil;
	}

	protected void setQuerierUtil(QuerierUtil querierUtil) {
		this.querierUtil = querierUtil;
	}
}
