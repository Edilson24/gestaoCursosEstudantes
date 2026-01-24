package org.example.candidavagasjavafx.controller;

import net.sf.jasperreports.engine.*;

import java.awt.Desktop;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JasperReportController {

    public void gerar(String curso, Integer vagas, Float valor,
                      Date dataAbertura, Date dataEncerramento) {

        try {
            InputStream jrxml = getClass()
                    .getResourceAsStream("/org/example/candidavagasjavafx/reports/Anuncio.jrxml");

            InputStream background = getClass()
                    .getResourceAsStream("/org/example/candidavagasjavafx/imagem/lifterbackgroundjasper.png");

            if (jrxml == null) {
                throw new RuntimeException("JRXML não encontrado");
            }

            if (background == null) {
                throw new RuntimeException("Imagem não encontrada");
            }

            JasperReport report = JasperCompileManager.compileReport(jrxml);

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("curso", curso);
            parametros.put("vaga", vagas);
            parametros.put("valor", valor);
            parametros.put("dataAbertura", dataAbertura);
            parametros.put("dataEncerramento", dataEncerramento);
            parametros.put("backImage", background);

            JasperPrint print = JasperFillManager.fillReport(
                    report,
                    parametros,
                    new JREmptyDataSource()
            );

            // Exporta PDF
            String output = "Anuncio_Vaga.pdf";
            JasperExportManager.exportReportToPdfFile(print, output);

            // Abre o PDF
            Desktop.getDesktop().open(new File(output));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
