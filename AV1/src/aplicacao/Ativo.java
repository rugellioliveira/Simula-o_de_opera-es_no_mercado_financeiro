package aplicacao;

import java.util.ArrayList;

/*
* Classe que representa um Ativo
* 
* @author Rugelli Oliveira
* @version 1.0
*/

public class Ativo {
	
	//Atributos (títulos no cabeçalho do arquivo .csv)
	private ArrayList<String> datas;
	private ArrayList<Double> opens;
	private ArrayList<Double> highs;
	private ArrayList<Double> lows;
	private ArrayList<Double> closes;
	private ArrayList<Integer> tickvols;
	private ArrayList<Double> vols;
	private ArrayList<Integer> spreads;
	
	
	public Ativo() {
		
	}
	
	public ArrayList<String> getDatas() {
		
		return datas;
	}
	public void setDatas(ArrayList<String> datas) {
		this.datas = datas;
	}
	
	public ArrayList<Double> getOpens() {
		return opens;
	}
	public void setOpens(ArrayList<Double> opens) {
		this.opens = opens;
	}
	
	public ArrayList<Double> getHighs() {
		return highs;
	}
	public void setHighs(ArrayList<Double> highs) {
		this.highs = highs;
	}
	
	public ArrayList<Double> getLows() {
		return lows;
	}
	public void setLows(ArrayList<Double> lows) {
		this.lows = lows;
	}
	
	public ArrayList<Double> getCloses() {
		return closes;
	}
	public void setCloses(ArrayList<Double> closes) {
		this.closes = closes;
	}
	
	public ArrayList<Integer> getTickvols() {
		return tickvols;
	}
	public void setTickvols(ArrayList<Integer> tickvols) {
		this.tickvols = tickvols;
	}
	
	public ArrayList<Double> getVols() {
		return vols;
	}
	public void setVols(ArrayList<Double> vols) {
		this.vols = vols;
	}
	
	public ArrayList<Integer> getSpreads() {
		return spreads;
	}
	public void setSpreads(ArrayList<Integer> spreads) {
		this.spreads = spreads;
	}
	
}
