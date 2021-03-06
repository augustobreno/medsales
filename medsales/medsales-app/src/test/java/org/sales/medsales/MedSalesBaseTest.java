package org.sales.medsales;

import java.io.File;

import javax.inject.Inject;

import org.easy.testeasy.DeployableBaseTest;
import org.easy.testeasy.arquillian.extension.dataloader.ArquillianDataLoaderExtension;
import org.easy.testeasy.dataloader.DataLoader;
import org.easy.testeasy.dataloader.HibernateDataLoader;
import org.easy.testeasy.dataloader.LoadData;
import org.easy.testeasy.dataloader.LoadDatas;
import org.easy.testeasy.dataloader.SqlDataLoader;
import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filter;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.container.ManifestContainer;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.junit.Ignore;
import org.sales.medsales.api.util.QuerierUtil;

/**
 * Classe base para testes realizados no servidor de aplicação.
 * Suas principais atividades são: Ativar o Arquillian; Configurar o arquivo para
 * deploy dos testes no servidores de aplicação; Gerenciar a transação via Arquillian (realiza
 * rollback por padrão) 
 * @author Augusto
 *
 */
@ArquillianSuiteDeployment // garante a reutilização do @Deployment entre as classes de teste
@Ignore
public class MedSalesBaseTest extends DeployableBaseTest {

	private static final String ORG_SALES_MEDSALES = "org/sales/medsales";
	
	private static final String ORG_SALES_MEDSALES_API_SHORT = "orgsalesmedsalesapi";

	
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
        
        installDataLoaderExtension(war);
        
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
	 * Instala a extensão do Arquillian que permite o funcionamento das soluções
	 * de DataLoader.
	 * 
	 * @param archive
	 *            Arquivo que receberá a ativação da extensão de Data Loader
	 * 
	 * @see LoadData
	 * @see LoadDatas
	 * @see DataLoader
	 * @see HibernateDataLoader
	 * @see SqlDataLoader
	 */
	public static void installDataLoaderExtension(ManifestContainer<?> archive) {
		archive.addAsServiceProvider(RemoteLoadableExtension.class,
				ArquillianDataLoaderExtension.class);
	}

	protected QuerierUtil getQuerier() {
		return querierUtil;
	}

	protected void setQuerierUtil(QuerierUtil querierUtil) {
		this.querierUtil = querierUtil;
	}
}
