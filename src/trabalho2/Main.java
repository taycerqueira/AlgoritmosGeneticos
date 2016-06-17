package trabalho2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Main {
	
	private static int tamCromossomo;
	private static final int tamPopulacaoIncial = 50;
	private static final double criterioParada = 20.000;
	
	private static double[][] matrizDistancia;
	

	public static void main(String[] args) {
		
		System.out.println(" ======= Algoritmo Genético: Problema do Caixeiro Viajante =======\n");
		
		boolean continua = true;
		int contGeracao = 0;
		Individuo melhorIndividuo = null;
		
		matrizDistancia = getMatriz("kroA100.tsp");
		tamCromossomo = matrizDistancia.length;
		
		ArrayList<Individuo> populacaoInicial = gerarPopulacaoInicial(tamPopulacaoIncial);
		ArrayList<Individuo> populacaoIntermediaria = selecaoTorneio(populacaoInicial, 28);
		ArrayList<Individuo> filhosPopulacaoIntermediaria = cruzamentoOX(populacaoIntermediaria, 0.7);
		
		/*int[] vetor1 = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
		int[] vetor2 = new int[]{4, 5, 2, 1, 8, 7, 6, 9, 3};
		Individuo ind1 = new Individuo(vetor1);
		Individuo ind2 = new Individuo(vetor2);
		ArrayList<Individuo> popTeste = new ArrayList<Individuo>();
		popTeste.add(ind1);
		popTeste.add(ind2);
		cruzamentoOX(popTeste, 1);*/
		
		/*while(continua){
			contGeracao++;
			ArrayList<Individuo> populacaoInicial = gerarPopulacaoInicial(tamPopulacaoIncial);
			ArrayList<Individuo> populacaoIntermediaria = selecaoTorneio(populacaoInicial, 28);
			ArrayList<Individuo> filhosPopulacaoIntermediaria = cruzamento(populacaoIntermediaria, 0.7);
			populacaoIntermediaria.addAll(filhosPopulacaoIntermediaria);
			
			ArrayList<Individuo> populacaoIntermediariaMutada = mutacao(populacaoIntermediaria, 0.05);
			
			populacaoInicial.addAll(populacaoIntermediariaMutada);
			
			ArrayList<Individuo> populacaoFinal = selecionaMelhores(populacaoInicial, tamPopulacaoIncial);
			if(populacaoFinal.get(0).getFitness(matrizDistancia) >= criterioParada){
				melhorIndividuo = populacaoFinal.get(0);
				continua = false;
			}
			
			Collections.shuffle(populacaoFinal);
			
		}
		System.out.println(" => Melhor individuo encontrado!");
		System.out.println("* Geração: " + contGeracao);
		System.out.println("* Fitness: " + melhorIndividuo.getFitness(matrizDistancia));
		System.out.println("* Cromossomo: " + Arrays.toString(melhorIndividuo.cromossomo));*/

	}
	
	private static ArrayList<Individuo> gerarPopulacaoInicial(int quant){
		
		System.out.println("\n=> Gerando população Inicial...\n");
		
		ArrayList<Individuo> populacaoInicial = new ArrayList<Individuo>();

		for(int i = 0; i < quant; i++){
			int[] cromossomo = getCromossomoEmbaralhado(tamCromossomo);
			System.out.println(Arrays.toString(cromossomo));
			populacaoInicial.add(new Individuo(cromossomo));
		}
		
		System.out.println("\n* Tamanho da população inicial: " + populacaoInicial.size());
		return populacaoInicial;
		
	}
	
	private static int[] getCromossomoEmbaralhado(int tamanhoCromossomo){
		
		int[] cromossomo = new int[tamanhoCromossomo];
		
		ArrayList<Integer> numeros = new ArrayList<Integer>();
		for (int i = 0; i < tamanhoCromossomo; i++) { 
		    numeros.add(i);
		}
		//Embaralhamos os números:
		Collections.shuffle(numeros);
		//Adicionamos os números aleatórios no vetor
		for (int i = 0; i < tamanhoCromossomo; i++) {
			cromossomo[i] = numeros.get(i);
		}
		
		return cromossomo;
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
			
			double fitness1 = individuo1.getFitness(matrizDistancia);
			double fitness2 = individuo2.getFitness(matrizDistancia);
			
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
	
	private static ArrayList<Individuo> cruzamentoOX(ArrayList<Individuo> populacao, double taxa){
		
		System.out.println("\n=> Realizando cruzamento...\n");
		
		ArrayList<Individuo> filhos = new ArrayList<Individuo>();
		
		for(int i = 0; i < populacao.size() - 1; i++){
			double r = Math.random();
			if(r < taxa){
				
				int[] pai1 = populacao.get(i).getVetorCromossomo();
				int[] pai2 = populacao.get(i+1).getVetorCromossomo();
				
				int[] filho1 = new int[tamCromossomo];
				int[] filho2 = new int[tamCromossomo];
				for(int j = 0; j < tamCromossomo; j++){
					filho1[j] = -1;
					filho2[j] = -1;
				}
				
				int a;
				/*if(tamCromossomo%3 == 0){
					a = tamCromossomo/3;
				}
				else{
					a = (tamCromossomo/3) + 1;
				}*/
				a = tamCromossomo/3;
				int corte1 = a;
				int corte2 = 2*a;

				for(int f = corte1; f < corte2; f++){
					
					filho1[f] = pai1[f];
					filho2[f] = pai2[f];
				}
				
				int cont = (tamCromossomo - a);
				
				int f = corte2;
				int p = corte2;
				
				//Gera filho 1
				while(cont > 0){
					if(f == tamCromossomo){
						f = 0;
					}
					if(p == tamCromossomo){
						p = 0;
					}
					if(filho1[f] == -1){
						
						while(cidadeJaExiste(filho1, pai2[p])){
							if(p == (tamCromossomo)){
								p = 0;
							}
							p++;
							
						}
						filho1[f] = pai2[p];
						cont--;
						f++;
						p++;
						
					}
					System.out.println("cont: " + cont);
				}
				
				//System.out.println(Arrays.toString(pai1));
				//System.out.println(Arrays.toString(pai2));
				System.out.println(Arrays.toString(filho1));
				
				filhos.add(new Individuo(filho1));
				//System.out.println("Tamanho Filho 1: " + filho1.length);
				
				cont = (tamCromossomo - a);
				f = corte2;
				p = corte2;
				
				//Gera filho 2
				while(cont > 0){
					if(f == tamCromossomo){
						f = 0;
					}
					if(p == tamCromossomo){
						p = 0;
					}
					if(filho2[f] == -1){
						
						while(cidadeJaExiste(filho2, pai1[p])){
							if(p == (tamCromossomo - 1)){
								p = 0;
							}
							p++;
							
						}
						filho2[f] = pai1[p];
						cont--;
						f++;
						p++;
						
					}
					System.out.println("cont: " + cont);
				}                                             

				filhos.add(new Individuo(filho2));
				//System.out.println(Arrays.toString(filho2));
				//System.out.println("Tamanho Filho 2: " + filho1.length);
				
			}
		}
		
		System.out.println("\n* Quantidade de filhos gerados: " + filhos.size());
		
		return filhos;
		
	}
	
	private static boolean cidadeJaExiste(int[] caminho, int cidade){
		
		for(int i = 0; i < caminho.length; i++){
			if(caminho[i] == cidade){
				return true;
			}
		}
			
		return false;
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
	
	private static ArrayList<Individuo> mutacaoBinaria(ArrayList<Individuo> populacao, double taxa){
		
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
				//System.out.println(Arrays.toString(populacao.get(i).cromossomo) + "\r\n");
				contMutacao++;
			}
		}
		
		System.out.println("* Quantidade de individuos mutados: " + contMutacao);
		
		return populacao;
		
	}
	
	private static ArrayList<Individuo> selecionaMelhores(ArrayList<Individuo> populacao, int quant){
		
		System.out.println("\n=> Selecionando os melhores...\n");
		
		ArrayList<Individuo> populacaoFinal = new ArrayList<Individuo>();
		boolean escolhidos[] = new boolean[populacao.size()];
		for(int i= 0; i < populacao.size(); i++){
			escolhidos[i] = false;
		}
		
		while(quant > 0){
			double maiorFitness = Double.MIN_VALUE;
			int index = -1;
			for(int i = 0; i < populacao.size(); i++){
				if(populacao.get(i).getFitness(matrizDistancia) > maiorFitness && !escolhidos[i]){
					maiorFitness = populacao.get(i).getFitness(matrizDistancia);
					index = i;
				}
			}
			//System.out.println(" - Fitness: " + populacao.get(index).getFitness());
			populacaoFinal.add(populacao.get(index));
			escolhidos[index] = true;
			quant--;
			
		}
		
		//System.out.println("* Melhores: " + populacaoFinal.size());
		
		return populacaoFinal;
		
	}
	
	private static double[][] getMatriz(String nomeArquivo){
		
		System.out.println("=> Lendo matriz de distâncias...");
		
		double[][] matriz = null;
		File arquivo = new File(nomeArquivo);
		try(InputStream in = new FileInputStream(arquivo) ){
		  Scanner scan = new Scanner(in);
		  int numeroCidades = 0;
		  while(!scan.next().equals("EOF")){
			  
			  String s = scan.next();
			  //System.out.println("s: " + s);
			  if(s.equals("DIMENSION:")){
				  numeroCidades = scan.nextInt();
				  matriz = new double[numeroCidades][numeroCidades];
				  //System.out.println("Número de cidades: " + numeroCidades);
			  }
			  
			  int cAnterior = 0;
			  double xAnterior = 0;
			  double yAnterior = 0;
			  
			  if(s.equals("NODE_COORD_SECTION")){
				  for(int i = 0; i < numeroCidades - 1; i++){
					  if(i == 0){
						  int numCidade1 = scan.nextInt();
						  //System.out.println("numCidade: " + numCidade1);
						  double x1 = Double.parseDouble(scan.next());
						  //System.out.println("x1: " + x1);
						  double y1 = Double.parseDouble(scan.next());
						  //System.out.println("y1: " + y1);
						  
						  int numCidade2 = scan.nextInt();
						  cAnterior = numCidade2;
						  //System.out.println("numCidade: " + numCidade2);
						  double x2 = Double.parseDouble(scan.next());
						  xAnterior = x2;
						  //System.out.println("x2: " + x2);
						  double y2 = Double.parseDouble(scan.next());
						  yAnterior = y2;
						  //System.out.println("y1: " + y2);
						  
						  double distancia =  Math.sqrt((Math.pow((x2 - x1), 2) + (Math.pow((y2 - y1), 2))));
						  matriz[i][i+1] = distancia;
					  }
					  else{
						  
						  //System.out.println("numCidade: " + cAnterior);
						  //System.out.println("xAnterior: " + xAnterior);
						  //System.out.println("yAnterior: " + yAnterior);
						  
						  int numCidade = scan.nextInt();
						  //System.out.println("numCidade: " + numCidade);
						  cAnterior = numCidade;
						  double x = Double.parseDouble(scan.next());
						  //System.out.println("x: " + x);
						  xAnterior = x;
						  double y = Double.parseDouble(scan.next());
						  //System.out.println("y: " + y);
						  yAnterior = y;
						  
						  double distancia =  Math.sqrt((Math.pow((x - xAnterior), 2) + (Math.pow((y - yAnterior), 2))));
						  matriz[i][i+1] = distancia;
						  
					  }	  
						   
				  }
			
			  }

		  }
		}catch(IOException ex){
		  ex.printStackTrace();
		}
		
		System.out.println("\n* Arquivo lido com sucesso");
		System.out.println("* Tamanho da matriz: " + matriz.length);
		return matriz;
	}
	

}
