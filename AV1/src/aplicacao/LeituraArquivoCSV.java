package aplicacao;

import java.io.FileReader;
import com.opencsv.CSVReader;
import com.opencsv.CSVParser;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVParserBuilder;
import java.util.ArrayList;

/*
* Classe que responsável por realizar a leitura de arquivos do tipo .csv
* 
* @author Rugelli Oliveira
* @version 1.0
*/

public class LeituraArquivoCSV {
	
	private static CSVReader csvReader;
	private static CSVParser parser;
	private static FileReader filereader;
	private Ativo ativo;
	
    public LeituraArquivoCSV() {
		
    	ativo = new Ativo();
	}
    
	
	public Ativo carregar(String arquivo){
		
		ArrayList<String> data = new ArrayList<>();
		ArrayList<Double> open = new ArrayList<>();
		ArrayList<Double> high = new ArrayList<>();
		ArrayList<Double> low = new ArrayList<>();
		ArrayList<Double> close = new ArrayList<>();
		ArrayList<Integer> tickvol = new ArrayList<>();
		ArrayList<Double> vol = new ArrayList<>();
		ArrayList<Integer> spread = new ArrayList<>();
		
		try {
		
			//cria objeto destinado à leitura de fluxos de caracteres.
	       filereader = new FileReader(arquivo);
	       
	       //configura separador das informações no arquivo (quebra da string)
	       // (\t) Tabulação horizontal. Move o cursor de tela para a próxima parada de tabulação (neste caso o início do próxima string).
	       parser = new CSVParserBuilder().
	    		   withSeparator('\t').
	    		   build();
	       
	       //cria o objeto da classe que lerá o arquivo recebido por parâmetro, pula o cabeçalho, e obdece o separador configurado
	       csvReader = new CSVReaderBuilder(filereader)
	    		   .withSkipLines(1).
	    		   withCSVParser(parser).
	    		   build();
	      
	       String termos[];
		
	       //O método readNext() retorna uma string com a próxima linha lida do arquivo.
	       //continua lendo as linhas do arquivo até ele terminar (situação na qual a variável csvReader ficará nula).
	       while ((termos = csvReader.readNext()) != null) {
	    	   
	    	   //Usou-se os métodos estáticos de parsing das classes Integer e Double para converter as strings nos tipos de dados necessários.
	    	   
	    	   data.add(termos[0]);
	    	   open.add(Double.parseDouble(termos[1]));  
	    	   high.add(Double.parseDouble(termos[2])); 
	    	   low.add(Double.parseDouble(termos[3])); 
	    	   close.add(Double.parseDouble(termos[4]));
	    	   tickvol.add(Integer.parseInt(termos[5])); 
	    	   vol.add(Double.parseDouble(termos[6]));
	    	   spread.add(Integer.parseInt(termos[7]));
	    	   
	       }
	       
	    }
		catch (Exception e) {
			//captura erro na leitura do arquivo
	        e.printStackTrace();
	    }
	
		ativo.setDatas(data);
		ativo.setOpens(open);
		ativo.setHighs(high);
		ativo.setLows(low);
		ativo.setCloses(close);
		ativo.setTickvols(tickvol);
		ativo.setVols(vol);
		ativo.setSpreads(spread);
		
		//retorno do método
		
		return ativo;
		
	}
	

}
