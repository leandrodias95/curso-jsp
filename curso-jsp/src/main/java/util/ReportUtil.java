package util;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import jakarta.servlet.ServletContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ReportUtil implements Serializable {
	private static final long serialVersionUID = 2993717931852675994L;
	
	public byte[] geraRelatorioPDF( List listaDados, String nomeRelatorio, ServletContext servletContext) throws Exception {
		
		JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listaDados); //cria a lista de dados que vem do sql da consulta feita
		String caminhoJasper = servletContext.getRealPath("relatorio")+ File.separator + nomeRelatorio + ".jasper"; //busca o pacote relatorio e cocatena o relatório
		JasperPrint impressoraJasper = JasperFillManager.fillReport(caminhoJasper, new HashMap(), jrbcds);
		return JasperExportManager.exportReportToPdf(impressoraJasper);
	}
}
