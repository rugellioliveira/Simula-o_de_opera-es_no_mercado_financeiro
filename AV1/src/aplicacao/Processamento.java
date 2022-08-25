package aplicacao;

import java.util.ArrayList;

/*
* Classe que responsável por realizar o processamento dos dados obtidos 
* a partir da leitura de um arquivo do tipo .csv
* Calcular a Média Simples, Média Exponencial, Média Curta, Média Intermediária
* Média Longa e Desvio Padrão.
* 
* @author Rugelli Oliveira
* @version 1.0
*/

public class Processamento {
	
	private Ativo ativo;
	private ArrayList<Double> mediaMSCIL;
	private ArrayList<Double> mediaME;
	private ArrayList<Double> desvioP;
	private ArrayList<Integer> dias;
	private ArrayList<Integer> meses;
	private ArrayList<Integer> anos;
	
	public Processamento(String arquivo) {
		
		ativo = new LeituraArquivoCSV().carregar(arquivo);
		instanciaArrays();
		
	}
	
	public Processamento(Ativo ativo) {
		
		this.ativo = ativo;
		instanciaArrays();
	}
	
	private void instanciaArrays() {
		
		mediaMSCIL = new ArrayList<Double>();
		mediaME = new ArrayList<Double>();
		desvioP = new ArrayList<Double>();
		dias = new ArrayList<Integer>();
		meses = new ArrayList<Integer>();
		anos = new ArrayList<Integer>();
		
	}
	
	public ArrayList<Integer> getDias(int periodo){
		
		processaData(periodo);
			
		return dias;
			
	}
	
    public ArrayList<Integer> getMeses(int periodo){
		
		processaData(periodo);
			
		return meses;
			
	}

    public ArrayList<Integer> getAnos(int periodo){
	
	    processaData(periodo);
		
	    return anos;
		
    }
		
	
	private void processaData(int periodo) {
		
		anos.clear();
		meses.clear();
		dias.clear();

		String aux = "";
		
		for(int i = periodo-1; i<ativo.getDatas().size(); i++) {
			
			aux = ativo.getDatas().get(i);
			anos.add(Integer.parseInt(aux.substring(0, 4)));
			meses.add(Integer.parseInt(aux.substring(5, 7)));
			dias.add(Integer.parseInt(aux.substring(8, 10)));
		}
	}
	
	public ArrayList<Double> getMediaMovelSimplesCIL(int periodo) {
		mediaMovelSimplesCIL(periodo);
		return mediaMSCIL;
	}
	
	private void mediaMovelSimplesCIL(int periodo) {
				
		mediaMSCIL.clear(); 
		double soma;
		
		for(int i=periodo-1; i<ativo.getCloses().size(); i++) {
			
			soma = 0;
			int j = i;
			int contador = periodo;
			
			while(contador>0) {
				
				soma += ativo.getCloses().get(j);
				j--;
				contador--;
				
			}
			
			mediaMSCIL.add(soma/periodo);
		}
		
	}
	
	public ArrayList<Double> getMediaMovelExponencial(int periodo) {
		mediaMovelExponencial(periodo);
		return mediaME;
	}
	
	
	//Primeiro, calcule a Média Móvel Aritmética. Uma Média Móvel Exponencial (MME) precisa começar de algum lugar, portanto uma Média Móvel Aritmética é usada como se fosse a MME do período anterior, no primeiro cálculo.
	//Em segundo lugar, calcule o coeficiente de multiplicação.
	//Em terceiro lugar, calcule a Média Móvel Exponencial.
	private void mediaMovelExponencial(int periodo) {
		
		mediaME.clear();
		
		mediaMovelSimplesCIL(periodo);
		
		double inicio = mediaMSCIL.get(0);
		
		mediaME.add(inicio);
		
		double divisor = periodo + 1;
		double multiplicador = 2/divisor;
		
		for(int i=1; i<ativo.getCloses().size(); i++) {
			mediaME.add((((ativo.getCloses().get(i-1) - mediaME.get(i-1)) * multiplicador) + mediaME.get(i-1))) ;
		}
		
	}
	
	private void desvioPadrao(int periodo) {
		
		desvioP.clear();
		
		mediaMovelSimplesCIL(periodo);
		
		int k = -1;
		
		for(int i = periodo-1; i<ativo.getCloses().size(); i++) {
			
			int j = i;
			int contador = periodo;
			double somatorio = 0;
			double valor = 0;
			double media = 0;
			double diferenca = 0;
			double resultado = 0;
			k++;
			
			while(contador>0 && k<mediaMSCIL.size()) {
				
				media = mediaMSCIL.get(k);
				valor = ativo.getCloses().get(j);
				diferenca = valor - media;
				somatorio += Math.pow(diferenca, 2);
				j--;
				contador--;
				
			}
			
			resultado = Math.sqrt((somatorio/periodo));

			desvioP.add(resultado);
		}
		
	}
	
	public ArrayList<Double> getDesvioPadrao(int periodo){
	
		desvioPadrao(periodo);
		
		return desvioP;
			
	}
	
}
