package aplicacao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import org.jfree.data.xy.XYDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;

/*
 * Classe que cria uma janela e exibe graficamente   
 * elementos calculados referente ao contexto da aplicação.
 * 
 * OBS: Utiliza a API JfreeChart.
 * 
 * @author Rugelli Oliveira
 * @version 1.0
 */

public class Grafico extends Processamento implements ActionListener {
	
	//ATRIBUTOS...
	
	//private static final long serialVersionUID = 1L;
	
	// janela da aplicacao
    private JFrame janela;
    
    // botão será inserido na janela da aplicação
    private final JButton botaoPlotar;
    
    // Margem usada nos componentes de texto
    private Insets margem;
    
    // armazena dados para plotagem dos gráficos (séries temporais)
    private TimeSeries seriesMS, seriesME, seriesMC, seriesMI, seriesML, seriesDP;
    
    // armazena séries temporais
    private TimeSeriesCollection datasetMS, datasetME, datasetMC, datasetMI, datasetML, datasetDP;
    
    // usado para percorrer uma estrutura de dados e acessar seus elementos
    private int iterador;
    
    // usados para verificar se todos os dados já foram plotados nos gráficos
    private boolean terminouMS, terminouME, terminouMC, terminouMI, terminouML, terminouDP;
   
    //CONSTRUTOR...
    //****************************************************************************************************************************************
	@SuppressWarnings("deprecation")
	public Grafico(String arquivo, String nomeAtivo) {
		
		super(arquivo);
		
		//Cria objetos do tipo TimeSeries  
		seriesMS = new TimeSeries("Média Simples", Day.class);
		seriesME = new TimeSeries("Média Exponencial", Day.class);
		seriesMC = new TimeSeries("Média Curta", Day.class);
		seriesMI = new TimeSeries("Média Intermediaria", Day.class);
		seriesML = new TimeSeries("Média Longa", Day.class);
		seriesDP = new TimeSeries("Desvio Padrão", Day.class);
		
		//Cria  coleções de objetos de série temporal. (torna um conjunto de dados conveniente para plotagem em gráficos). 
		datasetMS = new TimeSeriesCollection(seriesMS);
		datasetME = new TimeSeriesCollection(seriesME);
		datasetMC = new TimeSeriesCollection(seriesMC);
		datasetMI = new TimeSeriesCollection(seriesMI);
		datasetML = new TimeSeriesCollection(seriesML);
		datasetDP = new TimeSeriesCollection(seriesDP);
		
		//cria e configura botão para eventos de clique
        botaoPlotar = new JButton("Adicionar Novo Dado");
        botaoPlotar.setActionCommand("ADICIONAR_DADO");
        botaoPlotar.addActionListener(this);
        
        //valor inicial do iterador
        iterador = 0;
        
        // atributso booleanos recebem "false"
        terminouMS = false;
        terminouME = false;
        terminouMC = false;
        terminouMI = false;
        terminouML = false;
        terminouDP = false;
        
        // criando a janela do gráfico
        criarJanela();
        
		//criando o painel superior onde é exibido um botão para iniciar a plotagem dos dados
        criarPainelSuperior(nomeAtivo);
        
        criaPainelParaGrafico("3.1", datasetMS, datasetME, "esquerdo");
        criaPainelParaGrafico("3.2", datasetMC, datasetMI, "central");
        criaPainelParaGrafico("3.2", datasetMC, datasetML, "direito");
        criaPainelParaGrafico("3.3", datasetDP, null, "inferior");
        
	}
	
	//MÉTODOS...
	//****************************************************************************************************************************************
	//método exibe a janela com o gráfico
	public void exibir() {
		
		//coloca janela no centro da tela
		RefineryUtilities.centerFrameOnScreen(janela);
		
        // exibe a janela 
        janela.setVisible(true);
	}
	
	//****************************************************************************************************************************************
	private void criarJanela() {        
	         
		// cria e configura a janela
		janela = new JFrame();
		// define o título da janela
		janela.setTitle("GRÁFICOS DOS INDICADORES DE UM ATIVO");
		// define o tipo de layout da janela
		janela.setLayout(new BorderLayout());
	    // indica que o programa deve ser finalizado ao fechar a janela
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	 }
	
	//****************************************************************************************************************************************
	 private void criarPainelSuperior(String descricaoAtivo) {
		
		 // cria o painel do superior e define seu tamanho e layout
	     JPanel painelSuperior = new JPanel(new BorderLayout());
	     
	     // Cria e adiciona um rótulo com a identificação do Ativo
	     JTextField tituloAtivo = new JTextField(descricaoAtivo); 
	     tituloAtivo.setHorizontalAlignment(SwingConstants.CENTER);
	     tituloAtivo.setFont(new Font("Consolas", Font.BOLD, 20));
	     tituloAtivo.setMargin(margem);
	     tituloAtivo.setEditable(false);
	     tituloAtivo.setBackground(janela.getBackground());
	     painelSuperior.add(tituloAtivo, BorderLayout.NORTH);
	     
		 //adiciona botão ao painel superior
		 painelSuperior.add(botaoPlotar);
		 
		 // adiciona o painel superior à janela
		 janela.add(painelSuperior, BorderLayout.NORTH);
	        
	 }
	 
	 //******************************************************************************************************************************
	 private void criaPainelParaGrafico(String titulo, XYDataset dataset1, XYDataset dataset2, String posicao) {
		 
		 JFreeChart grafico = criaGrafico(titulo, dataset1, dataset2);
		 
		 addGraficoNaJanela(grafico, posicao);
		 
	 }
	 
    //*******************************************************************************************************************************
     private JFreeChart criaGrafico(String titulo, XYDataset dataset1, XYDataset dataset2 ) {
    	
        JFreeChart grafico = ChartFactory.createTimeSeriesChart(
                titulo,           // Título do gráfico
                "Data",          // Nome do eixo X
                "Valor",           // Nome do eixo Y
                dataset1,          // dados do gráfico
                true, true, false  // legenda, tooltips, urls
        );
        
        XYPlot plot = grafico.getXYPlot();
        
        XYItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        plot.setRenderer(0, renderer);
        
        if(dataset2 != null) {
        	
        NumberAxis axis2 = new NumberAxis("");
        axis2.setAutoRangeIncludesZero(false);
        axis2.setVisible(false);
        
        plot.setDataset(1, dataset2);
        
        StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
        renderer2.setSeriesPaint(0, Color.BLUE);
        
        plot.setRenderer(1, renderer2);
        
        }
        
        return grafico;
    }
    
    //****************************************************************************************************************************************
    // adiciona o gráfico recebido por parâmeto na janela de acordo com a posição também recebida por parâmetro
    private void addGraficoNaJanela(JFreeChart chart, String posicao) {
    	
    	// adiciona o gráfico na janela de acordo com a posição recebida por parâmetro
        ChartPanel painelGrafico = new ChartPanel(chart);
        painelGrafico.setPreferredSize(new Dimension(400, 400)); 
        
        if(posicao.equals("direito")) {
	        janela.add(painelGrafico, BorderLayout.EAST); 
	        janela.pack();
        }
        else if(posicao.equals("central")) {
            janela.add(painelGrafico, BorderLayout.CENTER); 
            janela.pack();
        	
        }
        else if(posicao.equals("esquerdo")) {
            janela.add(painelGrafico, BorderLayout.WEST); 
            janela.pack();
        	
        }
        else if(posicao.equals("inferior")) {
            janela.add(painelGrafico, BorderLayout.SOUTH); 
            janela.pack();
        	
        }
    	
    }
    
    //******************************************************************************************************************************
    public int obterDiaMesAno(String tipo, int periodo, int indice) {
    	
    	int valor = 0;
    	
    	ArrayList<Integer> aux = new ArrayList<>();
    	
    	aux.clear();
    	
    	if(tipo.equals("dia")) {
    		
        	aux = super.getDias(periodo);
        	
        	valor = aux.get(indice);
        	
        	return valor;
    		
    	}
    	
        if(tipo.equals("mes")) {
        	
        	aux = super.getMeses(periodo);
        	
        	valor = aux.get(indice);
        	
        	return valor;
    		
    	}
    	
        else{
        	
        	aux = super.getAnos(periodo);
        	
        	valor = aux.get(indice);
        	
        	return valor;
    		
    	}
    	
    }
   
    //****************************************************************************************************************************************
    private Double obterValorMSCIL(int periodo, int indice) {
    	
    	double valor = 0;
    	
    	ArrayList<Double> aux = new ArrayList<Double>();
    	
    	aux.clear();
    	
    	aux = super.getMediaMovelSimplesCIL(periodo);
    	
    	valor = aux.get(indice);
    	
    	return valor;
    	
    }
    
    //****************************************************************************************************************************************
    private Double obterValorME(int periodo, int indice) {
    	
    	double valor = 0;
    	
    	ArrayList<Double> aux = new ArrayList<>();
    	
    	aux.clear();
    	
    	aux = super.getMediaMovelExponencial(periodo);
    	
    	valor = aux.get(indice);
    	
    	return valor;
    	
    }
    
    //****************************************************************************************************************************************
    private Double obterValorDP(int periodo, int indice) {
    	
    	double valor = 0;
    	
    	ArrayList<Double> aux = new ArrayList<>();
        
    	aux.clear();
    	
    	aux = super.getDesvioPadrao(periodo);
    	
    	valor = aux.get(indice);
    	
    	return valor;
    	
    }
    
    //****************************************************************************************************************************************
    public void actionPerformed(final ActionEvent e) {
        if (e.getActionCommand().equals("ADICIONAR_DADO")) {
        	
            if(!terminouMS && !terminouME && !terminouMC && !terminouMI && !terminouML && !terminouDP) {
        
		        	if(iterador < super.getMediaMovelSimplesCIL(10).size()) {
		        		
		        		seriesMS.addOrUpdate(new Day(obterDiaMesAno("dia", 10, iterador), obterDiaMesAno("mes", 10, iterador), obterDiaMesAno("ano", 10, iterador)), obterValorMSCIL(10, iterador));
		        	
		        	}
		        	else {
		        		
		        		terminouMS = true;
		        	}
		        	
		        	
		        	if(iterador < super.getMediaMovelSimplesCIL(10).size()) {
		        		
		        		seriesME.addOrUpdate(new Day(obterDiaMesAno("dia", 10, iterador), obterDiaMesAno("mes", 10, iterador), obterDiaMesAno("ano", 10, iterador)), obterValorME(10, iterador));
		        	
		        	}
		        	else {
		        		
		        		terminouME = true;
		        	}
		        	
		        	if(iterador < super.getMediaMovelSimplesCIL(15).size()) {
		        		
		        		//periodo deve ser 15 dias
		        		seriesMC.addOrUpdate(new Day(obterDiaMesAno("dia", 15, iterador), obterDiaMesAno("mes", 15, iterador), obterDiaMesAno("ano", 15, iterador)), obterValorMSCIL(15, iterador));
		        	
		        	}
		        	else {
		        		
		        		terminouMC = true;
		        	}
		        	
		        	if(iterador < super.getMediaMovelSimplesCIL(50).size()) {
		        
		        		//periodo deve ser 50 dias
		        		seriesMI.addOrUpdate(new Day(obterDiaMesAno("dia", 50, iterador), obterDiaMesAno("mes", 50, iterador), obterDiaMesAno("ano", 50, iterador)), obterValorMSCIL(50, iterador));
		        	
		        	}
		        	else {
		        		
		        		terminouMI = true;
		        	}
		        	
		        	if(iterador < super.getMediaMovelSimplesCIL(100).size()) {
		        		
		        		//periodo deve ser 100 dias
		        		seriesML.addOrUpdate(new Day(obterDiaMesAno("dia", 100, iterador), obterDiaMesAno("mes", 100, iterador), obterDiaMesAno("ano", 100, iterador)), obterValorMSCIL(100, iterador));
		        	
		        	}
		        	else {
		        		
		        		terminouML = true;
		        	}
		        	
		        	if(iterador < super.getDesvioPadrao(15).size()) {
		        		
		        		seriesDP.addOrUpdate(new Day(obterDiaMesAno("dia", 15, iterador), obterDiaMesAno("mes", 15, iterador), obterDiaMesAno("ano", 15, iterador)), obterValorDP(15, iterador));
		        	
		        	}
		        	else {
		        		
		        		terminouDP = true;
		        	}
		        	
		        	iterador++;
        	
            
        	}
            else {
            	
            		botaoPlotar.setText("TODOS OS DADOS JÁ FORAM PLOTADOS NOS GRÁFICOS!");
            		botaoPlotar.setEnabled(false);
            }
        	
        }
        
    }
    
}

