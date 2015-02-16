package org.sales.medsales.negocio.movimentacao;

import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;

import org.easy.testeasy.dataloader.LoadData;
import org.junit.Assert;
import org.junit.Test;
import org.sales.medsales.MedSalesBaseTest;
import org.sales.medsales.api.exceptions.BusinessException;
import org.sales.medsales.api.exceptions.CodedAppException;
import org.sales.medsales.dataLoader.ParceirosDataLoader;
import org.sales.medsales.dominio.Ciclo;
import org.sales.medsales.dominio.Parceiro;
import org.sales.medsales.dominio.movimentacao.Operacao;
import org.sales.medsales.dominio.movimentacao.Valor;
import org.sales.medsales.exceptions.ExceptionCodes;

/**
 * Testes de inclusão de um {@link Valor} em um {@link Ciclo}
 * 
 * @author Augusto
 */
@LoadData(dataLoader = ParceirosDataLoader.class)
public class CicloValorTest extends MedSalesBaseTest {

	@Inject
	private CicloFacade cicloFacade;

	/**
	 * Testa as validações básicas
	 */
	@Test
	public void validacaoParametrosPrimariosTest() {

		try {
			cicloFacade.add((Valor) null);
			Assert.fail();
		} catch (CodedAppException e) {
			Assert.assertTrue(e.hasCode(ExceptionCodes.VALOR.VALOR_REQUIRED));
		}

	}
	
	/**
	 * Testa as validações básicas
	 */
	@Test
	public void validacaoParametrosObrigatoriosTest() {

		try {
			cicloFacade.add(new Valor());
			Assert.fail();
		} catch (BusinessException e) {
			Assert.assertTrue(e.hasCode(ExceptionCodes.VALOR.VALOR_FINANCEIRO_REQUIRED));
			Assert.assertTrue(e.hasCode(ExceptionCodes.VALOR.CICLO_REQUIRED));
			Assert.assertTrue(e.hasCode(ExceptionCodes.VALOR.OPERACAO_REQUIRED));
		}

	}

	/**
	 * Verifica o cadastro com sucesso.
	 */
	@Test
	public void cadastroValorSucessoTest() {

		// cadastrando um ciclo básico.
		Ciclo ciclo = new Ciclo();
		ciclo.setInvestidor(getQuerier().findAt(Parceiro.class, 0));
		ciclo.setInicio(new Date());
		cicloFacade.save(ciclo);

		Valor valor = new Valor();
		valor.setCiclo(ciclo);
		valor.setParceiro(getQuerier().findAt(Parceiro.class, 1));
		valor.setValor(new BigDecimal(10));
		valor.setOperacao(Operacao.ENTRADA);
		
		cicloFacade.add(valor);
		
		// consulta o ciclo e garante a presenca do investimento
		getEm().clear();
		
		Ciclo found = cicloFacade.findBy(ciclo.getId(), "movimentacoes");
		Assert.assertEquals(1, found.getMovimentacoes().size());
		Assert.assertEquals(valor, found.getMovimentacoes().get(0));
		
	}

}
