package org.sales.medsales.persistencia.operator;

import org.easy.qbeasy.api.Operator;
import org.easy.qbeasy.repository.criteria.CriteriaOperatorProcessorBase;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.sales.medsales.dominio.movimento.estoque.PrecoProduto;

/**
 * @author augusto
 */
@SuppressWarnings("serial")
public class PrecoMaisRecenteProcessor extends CriteriaOperatorProcessorBase<Object> {

	@Override
	protected void executeOperation(String propriedade, Operator<Object> operator, Object... valores) {
		
		DetachedCriteria maxId = DetachedCriteria.forClass(PrecoProduto.class);
		maxId.setProjection( Projections.max("id") );
		
		// produto_ faz referência ao padrão de nome de alias do qbe
		maxId.add(Restrictions.eqProperty("produto.id", "produto_.id"));
		
		getJunction().add(Subqueries.propertyEq("id", maxId));
	}
	
}
