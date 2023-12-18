package br.com.fabricio.ama.amadesafio.utils;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import br.com.fabricio.ama.amadesafio.dtos.ProdutoResponseDTO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

@Data
@Component
public class GeradorXlsx {
    private List<ProdutoResponseDTO> listaProdutos;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    List<String> listaCampos;

    public GeradorXlsx(List<ProdutoResponseDTO> listaProdutos, List<String> listaCampos){
        this.listaProdutos = listaProdutos;
        this.workbook = new XSSFWorkbook();
        this.listaCampos = listaCampos;
    }

    private void gerarCabecalho(){
        this.setSheet(this.workbook.createSheet("Produto"));
        Row row = this.sheet.createRow(0);
        CellStyle style = this.workbook.createCellStyle();
        XSSFFont font = this.workbook.createFont();
        font.setBold(true);
        font.setFontHeight((short) 16);
        style.setFont(font);
        for (int i = 0; i < listaCampos.size(); i++) {
            String campo = listaCampos.get(i);
            criarCelula(row, i, campo, style);
        }
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
        XSSFFont font = this.workbook.createFont();
        font.setFontHeight((short) 14);
        style.setFont(font);

        for(ProdutoResponseDTO record: listaProdutos){
            Row row = this.sheet.createRow(rowCount++);
            Integer contador = 0;
            if(listaCampos.contains("ID")){
                criarCelula(row, contador++, record.getId(), style);
            }
            if(listaCampos.contains("Nome")) {
                criarCelula(row, contador++, record.getId(), style);
            }
            if(listaCampos.contains("Sku")) {
                criarCelula(row, contador++, record.getSku(), style);
            }
            if(listaCampos.contains("ID Categoria")) {
                criarCelula(row, contador++, record.getCategoria().getId(), style);
            }
            if(listaCampos.contains("Icms")) {
                criarCelula(row, contador++, record.getIcms(), style);
            }
            if(listaCampos.contains("Valor de Custo")) {
                criarCelula(row, contador++, record.getValorDeCusto(), style);
            }
            if(listaCampos.contains("Valor de Venda")) {
                criarCelula(row, contador++, record.getValorDeVenda(), style);
            }
            if(listaCampos.contains("Quantidade em Estoque")) {
                criarCelula(row, contador++, record.getQuantidadeEmEstoque(), style);
            }
        
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
