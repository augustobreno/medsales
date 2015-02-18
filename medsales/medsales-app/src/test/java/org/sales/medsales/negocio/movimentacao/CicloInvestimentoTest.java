package org.sales.medsales.negocio.movimentacao;

import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;

import org.easy.testeasy.dataloader.LoadData;
import org.junit.Assert;
import org.junit.Test;
import org.sales.medsales.MedSalesBaseTest;
import org.sales.medsales.api.exceptions.BusinessException;
import org.sales.medsales.dataLoader.ParceirosDataLoader;
import org.sales.medsales.dominio.Ciclo;
import org.sales.medsales.dominio.Parceiro;
import org.sales.medsales.dominio.movimento.valor.Investimento;
import org.sales.medsales.exceptions.ExceptionCodes;
import org.sales.medsales.negocio.movimentacao.InvestimentoBO;

/**
 * Testes de inclusão de um {@link Investimento} em um {@link Ciclo}
 * 
 * @author Augusto
 */
@LoadData(dataLoader = ParceirosDataLoader.class)
public class CicloInvestimentoTest extends MedSalesBaseTest {

	@Inject
	private CicloFacade cicloFacade;

	@Inject
	private InvestimentoBO bo;
	
	/**
	 * Testa as validações básicas de um investimento
	 */
	@Test
	public void validacaoParametrosPrimariosTest() {

		try {
			cicloFacade.addInvestimento(null, null);
			Assert.fail();
		} catch (BusinessException e) {
			Assert.assertTrue(e.hasCode(ExceptionCodes.INVESTIMENTO.CICLO_REQUIRED));
			Assert.assertTrue(e.hasCode(ExceptionCodes.INVESTIMENTO.VALOR_REQUIRED));
		}

	}

	/**
	 * Verifica a condição do parceiro do investimento em relação ao investidor
	 * do ciclo.
	 */
	@Test
	public void validacaoParceiroTest() {

		// cadastrando um ciclo básico.
		Ciclo ciclo = new Ciclo();
		ciclo.setInvestidor(getQuerier().findAt(Parceiro.class, 0));
		ciclo.setInicio(new Date());
		cicloFacade.save(ciclo);

		// tentando criar um investimento com um parceiro diferente
		Investimento investimento = null;
		try {
			investimento = new Investimento();
			investimento.setCiclo(ciclo);
			investimento.setValor(new BigDecimal(10));
			investimento.setParceiro(getQuerier().findAt(Parceiro.class, 1));
			
			bo.add(investimento);
			Assert.fail();
		} catch (BusinessException e) {
			Assert.assertTrue(e.hasCode(ExceptionCodes.INVESTIMENTO.INVESTIDOR_DIFERENTE_CICLO));
		}
		
	}
	
	/**
	 * Verifica o cadastro com sucesso de um investimento.
	 */
	@Test
	public void cadastroInvestimentoSucessoTest() {

		// cadastrando um ciclo básico.
		Ciclo ciclo = new Ciclo();
		ciclo.setInvestidor(getQuerier().findAt(Parceiro.class, 0));
		ciclo.setInicio(new Date());
		cicloFacade.save(ciclo);

		Investimento investimento = cicloFacade.addInvestimento(ciclo, new BigDecimal(10));
		
		// consulta o ciclo e garante a presenca do investimento
		getEm().clear();
		
		Ciclo found = cicloFacade.findBy(ciclo.getId(), "movimentacoes");
		Assert.assertEquals(1, found.getMovimentacoes().size());
		Assert.assertEquals(investimento, found.getMovimentacoes().get(0));
		
	}

}
