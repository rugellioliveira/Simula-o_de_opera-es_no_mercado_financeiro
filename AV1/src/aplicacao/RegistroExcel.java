package aplicacao;

import java.io.File;

import java.util.ArrayList;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/*
* Classe que responsável por registrar todos os elementos obtidos
* no Processamento (superclasse) em um aquivo Excel.
* 
* OBS: Utiliza a API JExcel.
* 
* @author Rugelli Oliveira
* @version 1.0
*/

public class RegistroExcel extends Processamento {

	private WritableWorkbook planilha;
	private WritableSheet aba;
	private String caminhoSalvar;
	private String nomeAba;
	
	public RegistroExcel(String arquivo, String caminhoSalvar, String nomeAba) {
		
		super(arquivo);
		
		this.caminhoSalvar = caminhoSalvar;
		this.nomeAba = nomeAba;
	}
	
	public void gerarResgistro() {
		
		try {
			
			//Cria planilha no local recebido por parâmetro 
			planilha = Workbook.createWorkbook(new File(caminhoSalvar));
			
			//Adicionando o nome da aba
			aba = planilha.createSheet(nomeAba, 0);
			
			//Cabeçalhos
			String cabecalho[] = new String[6];
			cabecalho[0] = "Media Simples";
			cabecalho[1] = "Media Exponencial";
			cabecalho[2] = "Media Curta";
			cabecalho[3] = "Media Intermediaria";
			cabecalho[4] = "Media Longa";
			cabecalho[5] = "Desvio Padrao";
			
			//Configurações das celulas 
			WritableCellFormat cellFormat = new WritableCellFormat();
			cellFormat.setBackground(Colour.BLUE);
			cellFormat.setAlignment(Alignment.CENTRE);
			
			//Configurações da fonte
			WritableFont fonte = new WritableFont(WritableFont.ARIAL);
			fonte.setColour(Colour.BLACK);
			fonte.setBoldStyle(WritableFont.BOLD);
			cellFormat.setFont(fonte);
			
			//Escreve o cabeçalho no arquivo excel
			for (int i = 0; i < cabecalho.length; i++) {
				//Parâmetro (coluna, linha, conteúdo)
				Label label = new Label(i, 0, cabecalho[i]);
				aba.addCell(label);
				WritableCell cell = aba.getWritableCell(i, 0);
				cell.setCellFormat(cellFormat);
			}
		
			preencheCelulas(super.getMediaMovelSimplesCIL(10), 0);  //Média Simples
			preencheCelulas(super.getMediaMovelExponencial(10), 1); //Média Exponencial
			preencheCelulas(super.getMediaMovelSimplesCIL(15), 2);  //Média Curta
			preencheCelulas(super.getMediaMovelSimplesCIL(50), 3);  //Média Intermediária
			preencheCelulas(super.getMediaMovelSimplesCIL(100), 4); //Média Longa
			preencheCelulas(super.getDesvioPadrao(15), 5);  //Desvio Padrão
			
			//Escreve dados na planilha
			planilha.write();
			//Fecha o arquivo
			planilha.close();
			
		}
		catch(Exception e){
			
			e.printStackTrace();
		}
	}
	
	
	//método deve obrigatoriamente capturar possíveis exceções lançadas
	private void preencheCelulas(ArrayList<Double> dados, int coluna) throws RowsExceededException, WriteException {
		
		for(int linha=0; linha<dados.size(); linha++) {
			
			WritableCellFormat cellFormat = new WritableCellFormat();
			cellFormat.setAlignment(Alignment.CENTRE);
			
			Number number = new Number(coluna, linha+1, dados.get(linha));
			aba.addCell(number);
			
			WritableCell cell = aba.getWritableCell(coluna, linha+1);
			cell.setCellFormat(cellFormat);
			
	     }	
	}
	
}
