package aplicacao;

import java.io.BufferedReader;
import java.io.FileReader;

/*
* Classe que será utilizada para implementar
* a técnica de reconciliação de dados, com o 
* intuito de verificar se os dados do Caixa Geral 
* estão condizentes com os dados das Contas Corrente 
* dos Clientes.
* 
* @author Rugelli Oliveira
* @version 1.0
*/

public class Reconciliacao {
	
	private int qtdTotalComprasClientes = 0;
	private int qtdTotalVendasClientes = 0;
	
	private int qtdComprasCaixaGeral = 0;
	private int qtdVendasCaixaGeral = 0;
	
	public Reconciliacao() {
		
		
	}
	
	private void carregarContaCliente(String nome) {
		
		try {
			BufferedReader arq = new BufferedReader(new FileReader(nome + ".txt"));
			String linha = arq.readLine();
			while (linha != null) {
				String[] termos = linha.split(";");
				
				if(termos[0].equals("Compra")) {
					
					qtdTotalComprasClientes++;
				}
				else {
					qtdTotalVendasClientes++;
				}
				linha = arq.readLine();
			}
			arq.close();
		}
		catch (Exception e) {}
		
	}
	
	private void carregarCaixaGeral() {
		
		try {
			BufferedReader arq = new BufferedReader(new FileReader("Caixa Geral da Corretora Avançada.txt"));
			String linha = arq.readLine();
			while (linha != null) {
				String[] termos = linha.split(";");
				if(termos[1].equals("C")) {
					qtdComprasCaixaGeral++;
				}
				else {
					qtdVendasCaixaGeral++;
				}
				linha = arq.readLine();
			}
			arq.close();
		}
		catch (Exception e) {}
	}
	
	private void reconcilia() {
		
		carregarContaCliente("José");
		carregarContaCliente("João");
		carregarContaCliente("Maria");
		carregarContaCliente("Ana");
		carregarContaCliente("Joaquim");
		carregarContaCliente("Mariana");
		carregarContaCliente("Sebastião");
		carregarContaCliente("Renata");
		carregarContaCliente("Antônio");
		carregarContaCliente("Beatriz");
		
		carregarCaixaGeral();
		
	}
	
	public void exibirInformacoes() {
		
		reconcilia();
		
		System.out.println("Quantidade de vendas registradas no caixa geral da corretora: " + qtdVendasCaixaGeral);
		
		System.out.println("Quantidade total de operações de venda realizadas por todos os clientes  : " + qtdTotalVendasClientes);
		
		System.out.println("Quantidade de compras registradas no caixa geral da corretora: " + qtdComprasCaixaGeral);
		
		System.out.println("Quantidade total de operações de compra realizadas por todos os clientes : " + qtdTotalComprasClientes);
		
		if(qtdVendasCaixaGeral == qtdTotalVendasClientes && qtdTotalComprasClientes == qtdComprasCaixaGeral) {
			
			System.out.println();
			System.out.println("As operações no Caixa Geral da Corretora estão condizentes com as contas-correntes dos 10 Clientes!");
		}
		else {
			
			System.out.println();
			System.out.println("As operações no Caixa Geral da Corretora não estão condizentes com as contas-correntes dos 10 Clientes!");
		}
		
	}

}
