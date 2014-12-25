package org.sales.medsales;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.sales.medsales.dominio.EntityBaseTest;
import org.sales.medsales.negocio.ClienteFacadeTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
   EntityBaseTest.class,
   ClienteFacadeTest.class
})
public class MedSalesSuiteTest {

}
