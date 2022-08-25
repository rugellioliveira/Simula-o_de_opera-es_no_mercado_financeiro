package aplicacao;

import java.util.ArrayList;

/*
* Classe que representa um Cliente(Thread), o cliente acessa recurso da 
* Corretora para realizar operações de compra e venda.
* 
* @author Rugelli Oliveira
* @version 1.0
*/

public class Cliente extends Thread{
	
	private String nome;
	
	private int qtdOperacoes = 0;
	
	private ContaCorrente contaCorrente;
	
	private Processamento processamentoA;
	private Processamento processamentoA2;
	private Processamento processamentoB;
	private Processamento processamentoB2;
	private Processamento processamentoC;
	private Processamento processamentoC2;
	private Processamento processamentoD;
	private Processamento processamentoD2;
	
	private boolean ativoAcomprado = false;
	private boolean ativoBcomprado = false;
	private boolean ativoCcomprado = false;
	private boolean ativoDcomprado = false;
	
	private double maiorPrecoAtivoA = -999999999999.0;
	private double maiorPrecoAtivoB = -999999999999.0; 
	private double maiorPrecoAtivoC = -999999999999.0;
	private double maiorPrecoAtivoD = -999999999999.0;
	
	private double menorPrecoAtivoA = 999999999999.0;
	private double menorPrecoAtivoB = 999999999999.0;
	private double menorPrecoAtivoC = 999999999999.0;
	private double menorPrecoAtivoD = 999999999999.0;
	
	private Corretora corretora;
	
	public Cliente(String nome, ContaCorrente contaCorrente, Corretora corretora) {
		
		this.nome = nome;
		
		this.contaCorrente = contaCorrente;
		
		this.corretora = corretora;
		
		//Corretora disponibiliza aos clientes dados dos ativos
		processamentoA = new Processamento(corretora.getAtivo("A"));
		processamentoA2 = new Processamento(corretora.getAtivo("A"));
		processamentoB = new Processamento(corretora.getAtivo("B"));
		processamentoB2 = new Processamento(corretora.getAtivo("B"));
		processamentoC = new Processamento(corretora.getAtivo("C"));
		processamentoC2 = new Processamento(corretora.getAtivo("C"));
		processamentoD = new Processamento(corretora.getAtivo("D"));
		processamentoD2 = new Processamento(corretora.getAtivo("D"));
		
		definirMaior_Menor("A", maiorPrecoAtivoA, menorPrecoAtivoA);
		definirMaior_Menor("B", maiorPrecoAtivoB, menorPrecoAtivoB);
		definirMaior_Menor("C", maiorPrecoAtivoC, menorPrecoAtivoC);
		definirMaior_Menor("D", maiorPrecoAtivoD, menorPrecoAtivoD);
		
		//exclui arquivo de registro de conta corrente para poder criar outro atualizado
		contaCorrente.limparExtrato(this.getNome());
		
	}
	
	public String getNome() {
        return nome;
    }
	
	public ContaCorrente getContaCorrente() {
		
		return contaCorrente;
	}

	@Override
	public void run() {
				
		realizarOperacoes();
		
	}
	
	private void realizarOperacoes() {
		
		int tamanho = processamentoA.getMediaMovelSimplesCIL(100).size();
		int contador = 0;
		
		//Para garantir que cada cliente realize no mínimo 30 operações.
		while(contador<tamanho || qtdOperacoes<31) {

			//Métodos que analisam os indicadores de um ativo e toma a decisão de compra ou venda.
			analisarAtivoA(contador);
			analisarAtivoB(contador);
			analisarAtivoC(contador);
			analisarAtivoD(contador);

			contador++;
		
		}
		
	}
	
	
	private void analisarAtivoA(int i) {
		
		String tipo = "A";
		ArrayList<Double> aux = new ArrayList<>();
		ArrayList<Double> aux2 = new ArrayList<>();
		ArrayList<Double> dp = new ArrayList<>();
		aux.clear();
		aux2.clear();
		dp.clear();
		
		aux = processamentoA.getMediaMovelSimplesCIL(15);
		aux2 = processamentoA2.getMediaMovelSimplesCIL(100);
		dp = processamentoA.getDesvioPadrao(15);
		
		//Compra se ocorre o cruzamento para cima da média curta com a média longa
		//E se o desvio padrão estiver abaixo do limite especificado
		//Clientes não podem realizar duas operações seguidas de compra/venda
        if(aux.get(i) > aux2.get(i) && ativoAcomprado==false ) {
        	if(calculaDrawdown(tipo, dp.get(i))) {
        		this.setPriority(NORM_PRIORITY); //voltando em seguida para a prioridade normal (5).
        		if(corretora.acessarCaixa(this, "C", tipo, i)) {
        			
        			ativoAcomprado=true;
        			//Operações registradas na própria conta-corrente, impactando no saldo.
        			contaCorrente.salvarExtrato(this.getNome(), getContaCorrente().getSaldo(), "Compra");
        			qtdOperacoes++;
        			aguardar();

        		}
        	}
			
		}
        //Vende se ocorre o cruzamento para baixo da média curta com a média longa
      	//E se o desvio padrão estiver abaixo do limite especificado
		else if(aux.get(i) < aux2.get(i) && ativoAcomprado==true){
			if(calculaDrawdown(tipo, dp.get(i))) {
				this.setPriority(NORM_PRIORITY); //voltando em seguida para a prioridade normal (5).
				if(corretora.acessarCaixa(this, "V", tipo, i)) {
					
					ativoAcomprado=false;
					contaCorrente.salvarExtrato(this.getNome(), getContaCorrente().getSaldo(), "Venda");
					qtdOperacoes++;
					aguardar();

				}
		    }
		}
		
	}
	
    private void analisarAtivoB(int i) {
		
		String tipo = "B";
		ArrayList<Double> aux = new ArrayList<>();
		ArrayList<Double> aux2 = new ArrayList<>();
		ArrayList<Double> dp = new ArrayList<>();
		aux.clear();
		aux2.clear();
		dp.clear();
		
		aux = processamentoB.getMediaMovelSimplesCIL(15);
		aux2 = processamentoB2.getMediaMovelSimplesCIL(100);
		dp = processamentoB.getDesvioPadrao(15);
		
        if(aux.get(i) > aux2.get(i) && ativoBcomprado==false ) {
        	if(calculaDrawdown(tipo, dp.get(i))) {
        		this.setPriority(NORM_PRIORITY); //voltando em seguida para a prioridade normal (5).
        		if(corretora.acessarCaixa(this, "C", tipo, i)) {
        			
        			ativoBcomprado=true;
        			contaCorrente.salvarExtrato(this.getNome(), getContaCorrente().getSaldo(), "Compra");
        			qtdOperacoes++;
        			aguardar();

        		}
        	}
		}
		else if(aux.get(i) < aux2.get(i) && ativoBcomprado==true){
			if(calculaDrawdown(tipo, dp.get(i))) {
				this.setPriority(NORM_PRIORITY); //voltando em seguida para a prioridade normal (5).
				if(corretora.acessarCaixa(this, "V", tipo, i)) {
					
					ativoBcomprado=false;
					contaCorrente.salvarExtrato(this.getNome(), getContaCorrente().getSaldo(), "Venda");
					qtdOperacoes++;
					aguardar();

				}
			}
		}
		
	}
	
    private void analisarAtivoC(int i) {
		
		String tipo = "C";
		ArrayList<Double> aux = new ArrayList<>();
		ArrayList<Double> aux2 = new ArrayList<>();
		ArrayList<Double> dp = new ArrayList<>();
		aux.clear();
		aux2.clear();
		dp.clear();
		
		aux = processamentoC.getMediaMovelSimplesCIL(15);
		aux2 = processamentoC2.getMediaMovelSimplesCIL(100);
		dp = processamentoC.getDesvioPadrao(15);
		
        if(aux.get(i) > aux2.get(i) && ativoCcomprado==false ) {
        	if(calculaDrawdown(tipo, dp.get(i))) {
        		this.setPriority(NORM_PRIORITY); //voltando em seguida para a prioridade normal (5).
        		if(corretora.acessarCaixa(this, "C", tipo, i)) {
        			
        			ativoCcomprado=true;
        			contaCorrente.salvarExtrato(this.getNome(), getContaCorrente().getSaldo(), "Compra");
        			qtdOperacoes++;
        			aguardar();
        		}

        	}
        }
		else if(aux.get(i) < aux2.get(i) && ativoCcomprado==true){
			if(calculaDrawdown(tipo, dp.get(i))) {
				this.setPriority(NORM_PRIORITY); //voltando em seguida para a prioridade normal (5).
				if(corretora.acessarCaixa(this, "V", tipo, i)) {
					
					ativoCcomprado=false;
					contaCorrente.salvarExtrato(this.getNome(), getContaCorrente().getSaldo(), "Venda");
					qtdOperacoes++;
					aguardar();

				}
			}
		}
		
	}
    
    private void analisarAtivoD(int i) {
		
		String tipo = "D";
		ArrayList<Double> aux = new ArrayList<>();
		ArrayList<Double> aux2 = new ArrayList<>();
		ArrayList<Double> dp = new ArrayList<>();
		aux.clear();
		aux2.clear();
		dp.clear();
		
		aux = processamentoD.getMediaMovelSimplesCIL(15);
		aux2 = processamentoD2.getMediaMovelSimplesCIL(100);
		dp = processamentoD.getDesvioPadrao(15);
		
        if(aux.get(i) > aux2.get(i) && ativoDcomprado==false ) {
        	if(calculaDrawdown(tipo, dp.get(i))) {
        		this.setPriority(NORM_PRIORITY); //voltando em seguida para a prioridade normal (5).
        		if(corretora.acessarCaixa(this, "C", tipo, i)) {
        			
        			ativoDcomprado=true;
        			contaCorrente.salvarExtrato(this.getNome(), getContaCorrente().getSaldo(), "Compra");
        			qtdOperacoes++;
        			aguardar();

        		}
        	}
		}
		else if(aux.get(i) < aux2.get(i) && ativoDcomprado==true){
			if(calculaDrawdown(tipo, dp.get(i))) {
				this.setPriority(NORM_PRIORITY); //voltando em seguida para a prioridade normal (5).
				if(corretora.acessarCaixa(this, "V", tipo, i)) {
					
					ativoDcomprado=false;
					contaCorrente.salvarExtrato(this.getNome(), getContaCorrente().getSaldo(), "Venda");
					qtdOperacoes++;
					aguardar();

				}
			}
		}
		
	}
    
    //Ao realizar uma compra ou venda um Cliente deve aguardar pelo menos 500ms para realizar nova operação.
    private void aguardar() {
    	
    	try {
			Cliente.sleep(500);
		} 
    	catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    private void definirMaior_Menor(String tipo, double maior, double menor){
    	
    	ArrayList<Double> aux = new ArrayList<>();
		aux.clear();
		aux = corretora.getAtivo(tipo).getCloses();
    	
		for (int i = 0; i < aux.size(); i++) {
		    if (aux.get(i) > maior) {
		    	maior = aux.get(i);
		    }
        }
		
		for (int i = 0; i < aux.size(); i++) {
		    if (aux.get(i) < menor) {
		    	menor = aux.get(i);
		    }
        }
		
    }
    
    //mecanismos de drawdown para controlar o risco em uma operação de compra ou venda de um Ativo
    private boolean calculaDrawdown(String tipo, double risco) {
    	
    	//No momento de execução de uma operação de drawdown, a Thread Cliente em questão poderá ter maior prioridade (10);
    	this.setPriority(MAX_PRIORITY);
    	
    	double drawdown = 0;
    	boolean retorno = false;
    	
    	if(tipo.equals("A")) {
    		
    		drawdown = ((maiorPrecoAtivoA - menorPrecoAtivoA)/maiorPrecoAtivoA);
    		
    		if(risco < drawdown) {
    			retorno = true;
    		}
    		
    	}
    	else if(tipo.equals("B")) {
    		
    		drawdown = ((maiorPrecoAtivoB - menorPrecoAtivoB)/maiorPrecoAtivoB);
    		
    		if(risco < drawdown) {
    			retorno = true;
    		}
    		
    	}
    	else if(tipo.equals("C")) {
	
        	drawdown = ((maiorPrecoAtivoC - menorPrecoAtivoC)/maiorPrecoAtivoC);

        	if(risco < drawdown) {
        		retorno = true;
        	}
	
        }
    	else if(tipo.equals("D")) {
    		
    		drawdown = ((maiorPrecoAtivoD - menorPrecoAtivoD)/maiorPrecoAtivoD);
    		
    		if(risco < drawdown) {
    			retorno = true;
    		}
    		
    	}
    	
    	return retorno;
    	
    }

}
