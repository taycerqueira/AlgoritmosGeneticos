package trabalho2;

import java.util.Arrays;

public class Individuo {
	
	int[] cromossomo;
	
	public Individuo(int[] vetor){
		
		this.cromossomo = vetor;
		
	}
	
	public int[] getVetorCromossomo(){
		
		return this.cromossomo;
		
	}
	
	public void alteraGene(int i, int gene){
		
		this.cromossomo[i] = gene;
		
	}
	
	public double getFitness(double[][] matrizDistancias){
		
		double distancia = -1;

		for(int i = 0; i < cromossomo.length - 1; i++){
			
			distancia += matrizDistancias[cromossomo[i]][cromossomo[i+1]];
			//System.out.println("DISTANCIA: " + distancia);
			
		}
		
		//System.out.println("distancia: " + distancia);
		
		return distancia;
		
	}

}
