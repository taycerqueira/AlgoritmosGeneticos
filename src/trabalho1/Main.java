package trabalho1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Main {
	
	private static final int tamCromossomo = 22;
	private static final int tamPopulacaoIncial = 50;
	private static final double criterioParada = 2.85027;

	public static void main(String[] args) {
		
		System.out.println(" ======= Algoritmo Genético =======\n");
		
		boolean continua = true;
		
		ArrayList<Individuo> populacaoIncial = gerarPopulacaoInicial(tamPopulacaoIncial);
		ArrayList<Individuo> populacaoIntermediaria1 = selecaoTorneio(populacaoIncial, 28);
		ArrayList<Individuo> populacaoIntermediaria2 = cruzamento(populacaoIntermediaria1, 0.7);
		ArrayList<Individuo> populacaoIntermediaria3 = mutacao(populacaoIntermediaria2, 0.05);

	}
	
	private static ArrayList<Individuo> gerarPopulacaoInicial(int quant){
		
		System.out.println("=> Gerando população Inicial...\n");
		
		ArrayList<Individuo> populacaoInicial = new ArrayList<Individuo>();
		Random rand = new Random();
		for(int i = 0; i < quant; i++){
			int cromossomo[] = new int[tamCromossomo];
			for(int j = 0; j < tamCromossomo; j++){
				if(rand.nextBoolean()){
					cromossomo[j] = 1;
				}
				else{
					cromossomo[j] = 0;
				}
			}
			System.out.println(Arrays.toString(cromossomo));
			populacaoInicial.add(new Individuo(cromossomo));
		}
		
		System.out.println("\n* Tamanho da população inicial: " + populacaoInicial.size());
		return populacaoInicial;
		
	}
	
	private static ArrayList<Individuo> selecaoTorneio(ArrayList<Individuo> populacao, int tamPopulacaoInter){
		
		System.out.println("\n=> Realizando Torneio...\n");
		
		ArrayList<Individuo> populacaoIntermediaria = new ArrayList<Individuo>();
		boolean escolhidos[] = new boolean[populacao.size()];
		for(int i = 0; i < populacao.size(); i++){
			escolhidos[i] = false;
		}
		
		Random rand = new Random();
		int intervalo = 50;
		for(int i = 0; i < tamPopulacaoInter; i++){
			
			int escolhido1 = rand.nextInt(intervalo);
			while(escolhidos[escolhido1]){
				escolhido1 = rand.nextInt(intervalo);
			}
			escolhidos[escolhido1] = true;
			
			int escolhido2 = rand.nextInt(intervalo);
			while(escolhido1 == escolhido2 || escolhidos[escolhido2]){
				escolhido2 = rand.nextInt(intervalo);
			}
			escolhidos[escolhido2] = true;
			
			Individuo individuo1 = populacao.get(escolhido1);
			Individuo individuo2 = populacao.get(escolhido2);
			
			double fitness1 = individuo1.getFitness();
			double fitness2 = individuo2.getFitness();
			
			if(fitness1 > fitness2){
				populacaoIntermediaria.add(individuo1);
				escolhidos[escolhido2] = false;
				System.out.println(Arrays.toString(individuo1.cromossomo));
			}
			else{
				populacaoIntermediaria.add(individuo2);
				escolhidos[escolhido1] = false;
				System.out.println(Arrays.toString(individuo2.cromossomo));
			}
			
		}
		
		System.out.println("\n* Tamanho população intermediária: " + populacaoIntermediaria.size());
		
		return populacaoIntermediaria;
		
	}
	
	private static ArrayList<Individuo> cruzamento(ArrayList<Individuo> populacao, double taxa){
		
		System.out.println("\n=> Realizando cruzamento...\n");
		
		ArrayList<Individuo> filhos = new ArrayList<Individuo>();
		
		for(int i = 0; i < populacao.size() - 1; i++){
			double r = Math.random();
			if(r < taxa){
				
				int[] pai1 = populacao.get(i).getVetorCromossomo();
				int[] pai2 = populacao.get(i+1).getVetorCromossomo();
				
				int[] filho1 = new int[tamCromossomo];

				for(int f = 0; f < tamCromossomo; f++){
					if(f < (tamCromossomo/2)){
						filho1[f] = pai1[f];
					}
					else{
						filho1[f] = pai2[f];
					}
					
				}
				
				filhos.add(new Individuo(filho1));
				System.out.println(Arrays.toString(filho1));
				//System.out.println("Tamanho Filho 1: " + filho1.length);
				
				int[] filho2 = new int[22];

				for(int f = 0; f < tamCromossomo; f++){
					if(f < 11){
						filho2[f] = pai2[f];
					}
					else{
						filho2[f] = pai1[f];
					}
					
				}
				
				filhos.add(new Individuo(filho2));
				System.out.println(Arrays.toString(filho2));
				//System.out.println("Tamanho Filho 2: " + filho1.length);
				
			}
		}
		
		System.out.println("\n* Quantidade de filhos gerados: " + filhos.size());
		
		return filhos;
		
	}
	
	private static ArrayList<Individuo> mutacao(ArrayList<Individuo> populacao, double taxa){
		
		System.out.println("\n=> Realizando mutação...\n");
		
		ArrayList<Individuo> filhos = new ArrayList<Individuo>();
		int contMutacao = 0;
		
		for(int i = 0; i < populacao.size() - 1; i++){
			double r = Math.random();
			if(r < taxa){
				Random rand = new Random();
				int indice = rand.nextInt(tamCromossomo);
				int gene = populacao.get(i).cromossomo[indice];
				if(gene == 1){
					populacao.get(i).alteraGene(indice, 0);
				}
				else{
					populacao.get(i).alteraGene(indice, 1);
				}
				System.out.println("Cromossomo alterado: " + i + " | Gente alterado: " + indice);
				System.out.println(Arrays.toString(populacao.get(i).cromossomo) + "\r\n");
				contMutacao++;
			}
		}
		
		System.out.println("* Quantidade de individuos mutados: " + contMutacao);
		
		return populacao;
		
	}
	

}
