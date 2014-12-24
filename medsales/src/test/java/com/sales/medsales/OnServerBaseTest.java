package com.sales.medsales;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.junit.runner.RunWith;

import com.sales.medsales.exceptions.AppException;

/**
 * Classe base para testes realizados no servidor de aplicação.
 * @author Augusto
 *
 */
@RunWith(Arquillian.class)
public class OnServerBaseTest {

    @Deployment
    public static Archive<?> createDeployment() {
    	
    	// carregando configuração de dependências do pom
    	PomEquippedResolveStage pom = Maven.resolver().loadPomFromFile("pom.xml");
    	
    	// seleciona as dependências do projeto
        File[] libs = pom.importDependencies(ScopeType.COMPILE).resolve().withTransitivity().asFile();
    	
        return ShrinkWrap.create(WebArchive.class, "test.war")
        	.addPackages(true, "com.sales.medsales")
        	.addAsLibraries(libs)
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }	
    
    /**
     * Provoca uma pausa na execução da thread corrente.
     * @param time Tempo em ms de pausa. 
     */
    protected void sleep(int time) {
    	try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			throw new AppException("Não foi possível fazer a thread dormir.", e);
		}
	}
}
