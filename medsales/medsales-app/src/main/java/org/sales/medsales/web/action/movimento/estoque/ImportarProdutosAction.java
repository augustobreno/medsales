package org.sales.medsales.web.action.movimento.estoque;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;
import org.sales.medsales.api.exceptions.BusinessException;
import org.sales.medsales.api.web.action.ActionBase;
import org.sales.medsales.negocio.movimentacao.estoque.produto.ImportarProdutoFacade;

/**
 * Importa produtos e preços a partir de um arquivo.
 * 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Named
@ConversationScoped
public class ImportarProdutosAction extends ActionBase {

	@Inject
	private Conversation conversation;

	@Inject
	private ImportarProdutoFacade produtoFacade;
	
	private UploadedFile file;

	@PostConstruct
	public void init() {
		super.init();
		beginOrJoin(conversation);
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public void upload() {
		if (file != null) {
			try {
				produtoFacade.importar(file.getInputstream());
			} catch (IOException e) {
				throw new BusinessException(null, "Falha ao tentar processar arquivo de importação de produtos.", e);
			}	
			showInfoMessage("Dados importados com sucesso.");
		} else {
			showWarnMessage("Selecione um arquivo TXT...");
		}
	}
	
}
