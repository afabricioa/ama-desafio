package br.com.fabricio.ama.amadesafio.utils;

import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import br.com.fabricio.ama.amadesafio.dtos.ProdutoResponseDTO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

@Data
@Component
public class GeradorXlsx {
    private List<ProdutoResponseDTO> listaProdutos;
    private Workbook workbook;
    private Sheet sheet;

    public GeradorXlsx(List<ProdutoResponseDTO> listaProdutos){
        this.listaProdutos = listaProdutos;
        this.workbook = new HSSFWorkbook();
    }

    private void gerarCabecalho(){
        this.setSheet(this.workbook.createSheet("Produto"));
        Row row = this.sheet.createRow(0);
        CellStyle style = this.workbook.createCellStyle();
        Font font = this.workbook.createFont();
        font.setBold(true);
        font.setFontHeight((short) 16);
        style.setFont(font);
        criarCelula(row, 0, "ID", style);
        criarCelula(row, 1, "Nome", style);
        criarCelula(row, 2, "SKU", style);
        criarCelula(row, 3, "Valor", style);
    }

    private void criarCelula(Row row, Integer contadorColuna, Object valorCelula, CellStyle style){
        sheet.autoSizeColumn(contadorColuna);
        Cell cell = row.createCell(contadorColuna);
        if(valorCelula instanceof Integer){
            cell.setCellValue((Integer) valorCelula);
        } else if(valorCelula instanceof Float){
            cell.setCellValue((Float) valorCelula);
        } else if(valorCelula instanceof String){
            cell.setCellValue((String) valorCelula);
        } else {
            cell.setCellValue((Boolean) valorCelula);
        }
        cell.setCellStyle(style);
    }

    private void write(){
        Integer rowCount = 1;
        CellStyle style = this.workbook.createCellStyle();
        Font font = this.workbook.createFont();
        font.setFontHeight((short) 14);
        style.setFont(font);

        for(ProdutoResponseDTO record: listaProdutos){
            System.out.println("record " + record);
            Row row = this.sheet.createRow(rowCount++);
            Integer contador = 0;
            criarCelula(row, contador++, record.getId(), style);
            criarCelula(row, contador++, record.getNome(), style);
            criarCelula(row, contador++, record.getSku(), style);
            criarCelula(row, contador++, record.getValorDeCusto(), style);
        }
    }

    public void gerarXlsxFile(HttpServletResponse response) throws IOException {
        gerarCabecalho();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        this.workbook.write(outputStream);
        this.workbook.close();
        outputStream.close();
    }
}
