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
import java.util.Spliterator.OfInt;

public class Main {
	
	private static int tamCromossomo;
	private static final int tamPopulacaoIncial = 200;
	private static final double criterioParada = 30000;
	private static final double taxaPopulacaoIntermediaria = 0.6;
	private static final double taxaCruzamento = 0.8;
	private static final double taxaMutacao = 0.07;
	
	//private static final double criterioParada = 21282; //Menor dist�ncia
	
	private static double[][] matrizDistancia;
	

	public static void main(String[] args) {
		
		System.out.println(" ======= Algoritmo Gen�tico: Problema do Caixeiro Viajante =======\n");
		
		boolean continua = true;
		int contGeracao = 0;
		Individuo melhorIndividuo = null;
		
		matrizDistancia = getMatriz("kroA100.tsp");
		tamCromossomo = matrizDistancia.length;
		
		System.out.println("Executando algoritmo. Aguarde...");
		ArrayList<Individuo> populacaoInicial = gerarPopulacaoInicial(tamPopulacaoIncial);
		ArrayList<Individuo> populacao = new ArrayList<>(populacaoInicial);
		
		while(continua){
			
			contGeracao++;
			System.out.println("Gera��o: " + contGeracao);
			ArrayList<Individuo> pais = selecaoTorneio(populacao, (int)(tamPopulacaoIncial*taxaPopulacaoIntermediaria));
			ArrayList<Individuo> filhos = cruzamentoOX(pais, taxaCruzamento);
			populacao.addAll(filhos);
			//ArrayList<Individuo> filhosMutados = mutacaoSwapCompleto(filhos, taxaMutacao);
			//populacao.addAll(filhosMutados);
			
			//ArrayList<Individuo> populacaoMutada = mutacaoSwap(populacao, taxaMutacao);
			ArrayList<Individuo> populacaoMutada = mutacaoSwapCompleto(populacao, taxaMutacao);
			
			ArrayList<Individuo> melhoresIndividuos = selecionaMelhores(populacaoMutada, tamPopulacaoIncial);
			double menorDistancia = melhoresIndividuos.get(0).getFitness(matrizDistancia);
			System.out.println("Melhor fitness (menor dist�ncia): " + (int)menorDistancia);
			
			if(menorDistancia <= criterioParada){
				melhorIndividuo = melhoresIndividuos.get(0);
				continua = false;
			}
			
			Collections.shuffle(melhoresIndividuos);
			populacao = new ArrayList<>(melhoresIndividuos);
			
		}
		System.out.println(" => Melhor individuo encontrado!");
		System.out.println("* Gera��o: " + contGeracao);
		System.out.println("* Fitness: " + melhorIndividuo.getFitness(matrizDistancia));
		System.out.println("* Cromossomo: " + Arrays.toString(melhorIndividuo.cromossomo));

	}
	
	private static ArrayList<Individuo> gerarPopulacaoInicial(int quant){
		
		//System.out.println("\n=> Gerando popula��o Inicial...\n");
		
		ArrayList<Individuo> populacaoInicial = new ArrayList<Individuo>();

		for(int i = 0; i < quant; i++){
			int[] cromossomo = getCromossomoEmbaralhado(tamCromossomo);
			//System.out.println(Arrays.toString(cromossomo));
			populacaoInicial.add(new Individuo(cromossomo));
		}
		
		//System.out.println("\n* Tamanho da popula��o inicial: " + populacaoInicial.size());
		return populacaoInicial;
		
	}
	
	private static int[] getCromossomoEmbaralhado(int tamanhoCromossomo){
		
		int[] cromossomo = new int[tamanhoCromossomo];
		
		ArrayList<Integer> numeros = new ArrayList<Integer>();
		for (int i = 0; i < tamanhoCromossomo; i++) { 
		    numeros.add(i);
		}
		//Embaralhamos os n�meros:
		Collections.shuffle(numeros);
		//Adicionamos os n�meros aleat�rios no vetor
		for (int i = 0; i < tamanhoCromossomo; i++) {
			cromossomo[i] = numeros.get(i);
		}
		
		return cromossomo;
	}
	
	private static ArrayList<Individuo> selecaoTorneio(ArrayList<Individuo> populacao, int tamPopulacaoInter){
		
		//System.out.println("\n=> Realizando Torneio...\n");
		
		ArrayList<Individuo> populacaoIntermediaria = new ArrayList<Individuo>();
		boolean escolhidos[] = new boolean[populacao.size()];
		for(int i = 0; i < populacao.size(); i++){
			escolhidos[i] = false;
		}
		
		Random rand = new Random();
		int intervalo = populacao.size();
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
			
			if(fitness1 < fitness2){ //se a distancia 1 for menor que a distancia 2 (quanto menor a distancia melhor o fitness)
				populacaoIntermediaria.add(individuo1);
				escolhidos[escolhido2] = false;
				//System.out.println(Arrays.toString(individuo1.cromossomo));
			}
			else{
				populacaoIntermediaria.add(individuo2);
				escolhidos[escolhido1] = false;
				//System.out.println(Arrays.toString(individuo2.cromossomo));
			}
			
		}
		
		//System.out.println("\n* Tamanho popula��o intermedi�ria: " + populacaoIntermediaria.size());
		
		return populacaoIntermediaria;
		
	}
	
	private static ArrayList<Individuo> cruzamentoOX(ArrayList<Individuo> populacao, double taxa){
		
		//System.out.println("\n=> Realizando cruzamento...\n");
		
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
				
				int a = tamCromossomo/3;
				//System.out.println("a = " + a);
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
						//System.out.println("p1 = " + p);
						
						while(cidadeJaExiste(filho1, pai2[p])){
							p++;
							if(p == (tamCromossomo)){
								p = 0;
							}
							
							
						}
						filho1[f] = pai2[p];
						cont--;
						f++;
						p++;
						
					}
					//System.out.println("cont: " + cont);
				}
				
				//System.out.println(Arrays.toString(pai1));
				//System.out.println(Arrays.toString(pai2));
				//System.out.println(Arrays.toString(filho1));
				
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
						//System.out.println("p2 = " + p);
						while(cidadeJaExiste(filho2, pai1[p])){
							p++;
							if(p == (tamCromossomo)){
								p = 0;
							}
							
							
						}
						filho2[f] = pai1[p];
						cont--;
						f++;
						p++;
						
					}
					//System.out.println("cont: " + cont);
				}                                             

				filhos.add(new Individuo(filho2));
				//System.out.println(Arrays.toString(filho2));
				//System.out.println("Tamanho Filho 2: " + filho1.length);
				
			}
		}
		
		//System.out.println("\n* Quantidade de filhos gerados: " + filhos.size());
		
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
	
	//Divide o cromossomo no meio e troca as partes
	/*private static ArrayList<Individuo> mutacaoSwapCompleto(ArrayList<Individuo> populacao, double taxa){
		
		//System.out.println("\n=> Realizando muta��o...\n");
		
		int contMutacao = 0;
		
		for(int i = 0; i < populacao.size() - 1; i++){
			double r = Math.random();
			if(r < taxa){
				
				Individuo individuo = populacao.get(i);
				
				//System.out.println(Arrays.toString(individuo.cromossomo));
				
				int a = (tamCromossomo/2);
				
				int[] cromossomo = new int[tamCromossomo];
				int[] p1 = Arrays.copyOfRange(individuo.cromossomo, 0, a);
				//System.out.println("p1: " + Arrays.toString(p1) + " | tam: " + p1.length);
				int[] p2 = Arrays.copyOfRange(individuo.cromossomo, a, tamCromossomo);
				//System.out.println("p2: " + Arrays.toString(p2) + " | tam: " + p2.length);
				
				for(int j = 0; j < a; j++){
					cromossomo[j] = p2[j];
				}
				int aux = 0;
				for(int j = a; j < tamCromossomo; j++){
					cromossomo[j] = p1[aux];
					aux++;
				}
				populacao.get(i).setCromossomo(cromossomo);
				//System.out.println(Arrays.toString(cromossomo));
				//System.exit(0);
				
				contMutacao++;
				//System.out.println(Arrays.toString(populacao.get(i).cromossomo) + "\r\n");
				
			}
		}
		
		//System.out.println("* Quantidade de indiv�duos mutados: " + contMutacao);
		
		return populacao;
		
	}*/
	
	private static ArrayList<Individuo> mutacaoSwapCompleto(ArrayList<Individuo> populacao, double taxa){
		
		int contMutacao = 0;
		Random rand = new Random();
		for(int i = 0; i < populacao.size() - 1; i++){
			double r = Math.random();
			if(r < taxa){
				
				Individuo individuo = populacao.get(i);
				
				//System.out.println(Arrays.toString(individuo.cromossomo));
				
				//int a = (tamCromossomo/4);
				
				int a = rand.nextInt(tamCromossomo);
				
				int[] cromossomo = new int[tamCromossomo];
				int[] p1 = Arrays.copyOfRange(individuo.cromossomo, 0, a);
				//System.out.println("p1: " + Arrays.toString(p1) + " | tam: " + p1.length);
				int[] p2 = Arrays.copyOfRange(individuo.cromossomo, a, tamCromossomo);
				//System.out.println("p2: " + Arrays.toString(p2) + " | tam: " + p2.length);
				
				for(int j = 0; j < (tamCromossomo - a); j++){
					cromossomo[j] = p2[j];
				}
				int aux = 0;
				for(int j = (tamCromossomo - a); j < tamCromossomo; j++){
					cromossomo[j] = p1[aux];
					aux++;
				}
				populacao.get(i).setCromossomo(cromossomo);
				//System.out.println(Arrays.toString(cromossomo));
				//System.exit(0);
				
				contMutacao++;
				//System.out.println(Arrays.toString(populacao.get(i).cromossomo) + "\r\n");
				
			}
		}
		
		//System.out.println("* Quantidade de indiv�duos mutados: " + contMutacao);
		
		return populacao;
		
	}
	
	private static ArrayList<Individuo> mutacaoSwap(ArrayList<Individuo> populacao, double taxa){
		
		//System.out.println("\n=> Realizando muta��o...\n");
		
		int contMutacao = 0;
		
		for(int i = 0; i < populacao.size() - 1; i++){
			double r = Math.random();
			if(r < taxa){
				Random rand = new Random();
				int gene1 = rand.nextInt(tamCromossomo);
				int gene2 = rand.nextInt(tamCromossomo);
				while(gene1 == gene2){
					gene2 = rand.nextInt(tamCromossomo);
				}
				
				int cidade1 = populacao.get(i).cromossomo[gene1];
				int cidade2 = populacao.get(i).cromossomo[gene2];
				
				populacao.get(i).alteraGene(gene1, cidade2);
				populacao.get(i).alteraGene(gene2, cidade1);

				
				//System.out.println(Arrays.toString(populacao.get(i).cromossomo) + "\r\n");
				contMutacao++;
			}
		}
		
		//System.out.println("* Quantidade de indiv�duos mutados: " + contMutacao);
		
		return populacao;
		
	}
	
	private static ArrayList<Individuo> mutacaoBinaria(ArrayList<Individuo> populacao, double taxa){
		
		//System.out.println("\n=> Realizando muta��o...\n");
		
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
		
		//System.out.println("* Quantidade de individuos mutados: " + contMutacao);
		
		return populacao;
		
	}
	
	private static ArrayList<Individuo> selecionaMelhores(ArrayList<Individuo> populacao, int quant){
		
		//System.out.println("\n=> Selecionando os melhores...\n");
		
		ArrayList<Individuo> populacaoFinal = new ArrayList<Individuo>();
		boolean escolhidos[] = new boolean[populacao.size()];
		for(int i= 0; i < populacao.size(); i++){
			escolhidos[i] = false;
		}
		
		while(quant > 0){
			double menorDistancia = Double.MAX_VALUE;
			int index = 0;
			for(int i = 0; i < populacao.size(); i++){
				if(populacao.get(i).getFitness(matrizDistancia) < menorDistancia && !escolhidos[i]){
					menorDistancia = populacao.get(i).getFitness(matrizDistancia);
					index = i;
				}
			}
			//System.out.println(" - Fitness: " + populacao.get(index).getFitness(matrizDistancia));
			populacaoFinal.add(populacao.get(index));
			escolhidos[index] = true;
			quant--;
			
		}
		
		//System.out.println("* Melhores: " + populacaoFinal.size());
		
		return populacaoFinal;
		
	}
	
	private static double[][] getMatriz(String nomeArquivo){
		
		//System.out.println("=> Lendo matriz de dist�ncias...");
		
		ArrayList<Cidade> cidades = new ArrayList<Cidade>();
		double[][] matriz = null;
		File arquivo = new File(nomeArquivo);
		
		try(InputStream in = new FileInputStream(arquivo) ){
			
		  Scanner scan = new Scanner(in);
		  int numeroCidades = 0;
		  
		  //while(!scan.next().equals("EOF")){
		  while(scan.hasNext()){
			  
			  String s = scan.next();
			  //System.out.println("s: " + s);
			  
			  if(s.equals("DIMENSION:")){
				  numeroCidades = scan.nextInt();
				  matriz = new double[numeroCidades][numeroCidades];
				  //System.out.println("N�mero de cidades: " + numeroCidades);
			  }
			  
			  
			  if(s.equals("NODE_COORD_SECTION")){
				  for(int i = 0; i < numeroCidades; i++){
					  int numCidade = scan.nextInt();
					  //System.out.println("numCidade: " + numCidade);
					  double x = Double.parseDouble(scan.next());
					  //System.out.println("x: " + x);
					  double y = Double.parseDouble(scan.next());
					 //System.out.println("x: " + x);
					  cidades.add(new Cidade(numCidade, x, y));	   
				  }
			
			  }

		  }
		}catch(IOException ex){
		  ex.printStackTrace();
		}
		
		System.out.println("\n* Arquivo lido com sucesso");
		System.out.println("\n* Criando matriz...");
		
		for(int i = 0; i < cidades.size(); i++){
			for(int j = 0; j < cidades.size(); j++){
				double distancia = cidades.get(i).getDistanciaEuclidiana(cidades.get(j).getX(), cidades.get(j).getY());		
				matriz[i][j] = distancia;
				matriz[j][i] = distancia;
			}
		}
		
		System.out.println("* Tamanho da matriz: " + matriz.length);
		
		/*for(int i = 0; i < matriz.length; i++){
			System.out.println(Arrays.toString(matriz[i]));
		}
		System.exit(0);*/
		return matriz;
	}
	

}
