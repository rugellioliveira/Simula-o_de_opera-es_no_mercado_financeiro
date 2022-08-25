package aplicacao;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
* Classe que representa uma Conta Corrente, será utilizada para registrar 
* todas as operações de compra e venda que os clientes realizaram em arquivos
* texto. 
* 
* @author Rugelli Oliveira
* @version 1.0
*/

public class ContaCorrente {
	
	private double saldo;
	
	public ContaCorrente(double saldo) {
		
		this.saldo = saldo;
	}
	
	public void limparExtrato(String nome) {
		
		Path path = Paths.get("C:\\Users\\Rugelli\\Documents\\Eclipse\\AV1\\" + nome + ".txt");
				
		try {
			Files.delete(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public double getSaldo() {
	    return saldo;
	}

	public void setSaldo(double saldo) {
	    this.saldo = saldo;
	}
	
	public void salvarExtrato(String nome, double quantiaAtual, String operacao) {
		
		try {
			/*Se o segundo argumento de FileWrite for true, os bytes serão gravados no final do arquivo e não no início. 
			 * não haverá substituição, somente adição de novos dados*/
			FileWriter arq = new FileWriter(nome + ".txt", true);
			arq.write(operacao + ";" + quantiaAtual + "\n");
		    arq.close();
		}
		catch (Exception e) {}
	}
	

}
