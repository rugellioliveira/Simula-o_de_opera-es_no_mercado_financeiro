package aplicacao;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
* Classe que representa o Caixa Geral de uma Corretora, será utilizada para registrar
* informações referentes às operações de compra ou venda de um Cliente(Thread) em 
* arquivo texto.
* 
* @author Rugelli Oliveira
* @version 1.0
*/

public class CaixaGeral {
	
	private String nomeCorretora;
	
	public CaixaGeral(String nomeCorretora) {
		
		this.nomeCorretora = nomeCorretora;
		
	}
	
    public void salvarDados(String timeStamp, double valor, String operacao) {
		
		try {
			FileWriter arq = new FileWriter("Caixa Geral da Corretora " + nomeCorretora + ".txt", true);
			arq.write(timeStamp + "||" + valor + ";" + operacao + "\n");
		    arq.close();
		}
		catch (Exception e) {}
	}

	
	
	public void limparDados() {
		
		Path path = Paths.get("C:\\Users\\Rugelli\\Documents\\Eclipse\\AV1\\Caixa Geral da Corretora "  + nomeCorretora + ".txt");
		
		try {
			Files.delete(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	

}
