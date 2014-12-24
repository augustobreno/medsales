package com.sales.medsales;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.sales.medsales.dominio.EntityBaseTest;
import com.sales.medsales.negocio.ClienteFacadeTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
   EntityBaseTest.class,
   ClienteFacadeTest.class
})
public class MedSalesSuiteTest {

}
