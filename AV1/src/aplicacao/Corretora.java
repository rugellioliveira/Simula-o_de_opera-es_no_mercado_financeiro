package aplicacao;

import java.util.Random;
import java.util.concurrent.Semaphore;

/*
* Classe que representa uma Corretora(Thread), responsável por
* controlar o acesso de clientes aos caixas.
* 
* @author Rugelli Oliveira
* @version 1.0
*/

public class Corretora implements Runnable{
	
	private String nome;
	
	private Ativo ativoA;
	private Ativo ativoB;
	private Ativo ativoC;
	private Ativo ativoD;
	
	private CaixaGeral caixaGeral;
	
	private int nOperacoes = 0;
	
	private Semaphore semaforo1;
	private Semaphore semaforo2;
	
	public Corretora(String nome, CaixaGeral caixaGeral, String arquivoA, String arquivoB, String arquivoC, String arquivoD, Semaphore semaforo1, Semaphore semaforo2) {
		
		this.nome = nome;
		
		ativoA = new LeituraArquivoCSV().carregar(arquivoA);
		ativoB = new LeituraArquivoCSV().carregar(arquivoB);
		ativoC = new LeituraArquivoCSV().carregar(arquivoC);
		ativoD = new LeituraArquivoCSV().carregar(arquivoD);
		
		this.semaforo1 = semaforo1;
		this.semaforo2 = semaforo2;
		
		this.caixaGeral = caixaGeral;
		
		//exclui arquivo de registro do caixa geral para poder criar outro atualizado
		caixaGeral.limparDados();
		
		System.out.println("********** SEJAM BEM-VINDOS À CORRETORA " + nome.toUpperCase() + "! **********");
		System.out.println("");
		
	}
	
	@Override
	public void run() {
		
		
	}
	
	
    public Ativo getAtivo(String id) {
		
		if(id.equals("A")) {
			return ativoA;
		}

		else if(id.equals("B")) {
			return ativoB;
		}

		if(id.equals("C")) {
			return ativoC;
		}

		else {
			return ativoD;
		}
		
	}
	
    //utilizou-se synchronized para que as demais Threads(clientes) esperem os caixas serem liberados
	public synchronized boolean acessarCaixa(Cliente cliente, String operacao, String tipo, int indice) {
		
		boolean retorno = false;
		
		//Corretora poderá no máximo fazer 1000 operações de compra e venda para o conjunto total de ativos
		if(nOperacoes < 1000) {
		
			Random gerador = new Random();
			int caixa = gerador.nextInt(2) + 1;

			if(caixa==1) {

				try {
					semaforo1.acquire();

					if(acessarCaixa1(cliente, operacao, tipo, indice)) {
						nOperacoes++;
						caixaGeral.salvarDados(getAtivo(tipo).getDatas().get(indice), getAtivo(tipo).getCloses().get(indice), operacao);
						retorno = true;
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					semaforo1.release();
					saiuDoCaixa1(cliente);

				}
			}
			else if(caixa==2) {

				try {
					semaforo2.acquire();

					if(acessarCaixa2(cliente, operacao, tipo, indice)) {
						nOperacoes++;
						caixaGeral.salvarDados(getAtivo(tipo).getDatas().get(indice), getAtivo(tipo).getCloses().get(indice), operacao);
						retorno = true;
					}


				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					semaforo2.release();
					saiuDoCaixa2(cliente);

				}
			}
		}
		else {
			
			System.out.println("");
			System.out.println("No momento, a Corretora " + nome + 
					" não pode realizar mais operações. Obrigado pela compreensão!");
			
			//Se a corretora já atingiu o limite de 1000 operações o programa é finalizado.
			System.exit(0);
		}
		
		return retorno;
		
	}
	   
	
    private boolean acessarCaixa1(Cliente cliente, String operacao, String tipo, int indice) {
		
    	boolean retorno = true;
    	
		System.out.println("Cliente: " + cliente.getNome() + " acessou o CAIXA #1");
		
		if(operacao.equals("C")) {
			
			if(!compra(cliente, tipo, indice)) {
				
				retorno = false;
			}
			
		}
		else {
			venda(cliente, tipo, indice);
		}
		
		return retorno;	
        
    }
	
	private boolean acessarCaixa2(Cliente cliente, String operacao, String tipo, int indice) {
		
		boolean retorno = true;
		
		System.out.println("Cliente: " + cliente.getNome()  + " acessou o CAIXA #2");
			
		if(operacao.equals("C")) {
				
			if(!compra(cliente, tipo, indice)) {
					
			retorno = false;
			}
		}
		else {
			venda(cliente, tipo, indice);
		}
			
		return retorno;
    }
	
	
    private void saiuDoCaixa1(Cliente cliente) {
    	
        System.out.println("Cliente: " + cliente.getNome() + " saiu do CAIXA #1");
        System.out.println("");
    }
	
    private void saiuDoCaixa2(Cliente cliente) {
    	
        System.out.println("Cliente: " + cliente.getNome() + " saiu do CAIXA #2");
        System.out.println("");
    }


	
	
	private void venda(Cliente cliente, String tipo, int indice){
        
	   double valor = getAtivo(tipo).getCloses().get(indice);
        
       //cliente tem o ativo que deseja vender (SIM)
	   System.out.println("Saldo atual: " + cliente.getContaCorrente().getSaldo());
       cliente.getContaCorrente().setSaldo(cliente.getContaCorrente().getSaldo() + valor);
       System.out.println("Vendeu o ativo " + tipo);
       System.out.println("Saldo atual: " + cliente.getContaCorrente().getSaldo());
        
    }
	
	private boolean compra(Cliente cliente, String tipo, int indice){
        
		double valor = getAtivo(tipo).getCloses().get(indice);
		
        //cliente tem saldo sulficiente para comprar (NÃO)
        if(cliente.getContaCorrente().getSaldo() < valor){
        	
             System.out.println("Saldo insuficiente para comprar o ativo " + tipo);
             
             return false;
        }
       //cliente tem saldo sulficiente para comprar (SIM)
        else {
        	System.out.println("Saldo atual: " + cliente.getContaCorrente().getSaldo());
        	cliente.getContaCorrente().setSaldo(cliente.getContaCorrente().getSaldo() - valor);
        	System.out.println("Comprou o ativo " + tipo);
        	System.out.println("Saldo atual: " + cliente.getContaCorrente().getSaldo());
        	
        	
        	return true;
        } 
    }
	
	public CaixaGeral getCaixaGeral(){
		
		return caixaGeral;
	}
   
}
