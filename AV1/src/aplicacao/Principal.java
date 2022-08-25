 package aplicacao;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/*
 * Classe Principal, contém o método Main
 * será utilizada para executar o programa e simular
 * os acessos de Clientes aos caixa da Corretora.
 * 
 * @author Rugelli Oliveira
 * @version 1.0
 */

public class Principal {

	public static void main(String[] args) throws Exception {
		
		
		int numeroDePermicoes = 1;
		Semaphore semaphore = new Semaphore(numeroDePermicoes);
		Semaphore semaphore1 = new Semaphore(numeroDePermicoes);
		
		Corretora c = new Corretora("Avançada", new CaixaGeral("Avançada"), "USDCAD_AtivoA.csv", "EURUSD_AtivoB.csv", 
				"USDJPY_AtivoC.csv", "USDCHF_AtivoD.csv", semaphore, semaphore1);
		
        ArrayList<Cliente> clientes = new ArrayList<>();
		
		clientes.add(new Cliente("José", new ContaCorrente(900.0), c));
		clientes.add(new Cliente("João", new ContaCorrente(800.0), c));
		clientes.add(new Cliente("Maria", new ContaCorrente(700.0), c));
		clientes.add(new Cliente("Ana", new ContaCorrente(600.0), c));
		clientes.add(new Cliente("Joaquim", new ContaCorrente(500.0), c));
		clientes.add(new Cliente("Mariana", new ContaCorrente(2000.0), c));
		clientes.add(new Cliente("Sebastião", new ContaCorrente(3000.0), c));
		clientes.add(new Cliente("Renata", new ContaCorrente(4000.0), c));
		clientes.add(new Cliente("Antônio", new ContaCorrente(5000.0), c));
		clientes.add(new Cliente("Beatriz", new ContaCorrente(6000.0), c));
		
		for(int i = 0; i < clientes.size(); i++) {
			
			clientes.get(i).start();
				
			}
	} 
}

