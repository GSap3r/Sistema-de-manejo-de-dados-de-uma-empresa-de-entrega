package org.example.javafx.reports;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import org.example.javafx.db.entidades.Pedido;

import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class PedidoReports {
    public static void pedidosPeriodo(List<Pedido> pedidoList, LocalDate inicio, LocalDate fim){
        String dest = "pedidos_" + inicio + "_" + fim + ".pdf";
        try (PdfWriter writer = new PdfWriter(dest)){

            // Criando o documento  Pdf
            PdfDocument pdf = new PdfDocument(writer);

            // Criando o Document
            Document doc = new Document(pdf);

            //Definindo uma fonte grande
            PdfFont fonteTitulo = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
            //Definindo um texto com o período
            String titleText = String.format("Relação de Pedidos: %s até %s", inicio.toString(), fim.toString());
            Text titulo = new Text(titleText);
            titulo.setFont(fonteTitulo);
            titulo.setFontSize(20);
            Paragraph paragraph = new Paragraph(titulo);
            doc.add(paragraph);

            // Espaço
            doc.add(new Paragraph(" "));

            // Criando a tabela com 4 colunas: ID, Data, Cliente, Total
            float[] pointColumnWidths = {60F, 120F, 220F, 80F};
            Table table = new Table(pointColumnWidths);

            // Cabeçalhos
            table.addCell(new Cell().add("ID").setBackgroundColor(Color.LIGHT_GRAY));
            table.addCell(new Cell().add("Data").setBackgroundColor(Color.LIGHT_GRAY));
            table.addCell(new Cell().add("Cliente").setBackgroundColor(Color.LIGHT_GRAY));
            table.addCell(new Cell().add("Total").setBackgroundColor(Color.LIGHT_GRAY));

            // Linhas com dados reais
            for (Pedido p : pedidoList) {
                table.addCell(new Cell().add(String.valueOf(p.getId())));
                table.addCell(new Cell().add(p.getData().toString()));
                table.addCell(new Cell().add(p.getNomeCliente()));
                table.addCell(new Cell().add(String.format(Locale.US, "%.2f", p.getTotal())));
            }

            doc.add(table);

            // Fechando o documento
            doc.close();

            //abrindo e mostrando o PDF
            Desktop.getDesktop().open(new File(dest));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
