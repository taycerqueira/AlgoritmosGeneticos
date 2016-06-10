package trabalho1;

import java.util.Arrays;
import java.util.Random;

public class Main {
	
	public static int[][] populacao;
	private static int tamPopulacaoIncial = 50;

	public static void main(String[] args) {
		
		int cromossomo[] = new int[22];
		gerarPopulacaoInicial(tamPopulacaoIncial);
		int[][] populacaoIntermediaria = selecaoTorneio(28, tamPopulacaoIncial);
		cruzamento(populacaoIntermediaria, 0.7);

	}
	
	private static double fitness(double x){
		
		return (x*Math.sin(10*Math.PI*x) + 1);
		
	}
	
	private static double getReal(int x){
		double real = 0;
		
		real = (-1.0) + x*(3/(Math.pow(2, 22) - 1));
		
		return real;
	}
	
	private static int getDecimal(int[] binario){
		
		int decimal = 0;
		
		int bill = 21;
		for(int i = 0; i < 22; i++){
			decimal += (binario[i]*Math.pow(2, bill));
			bill--;
		}
		
		return decimal;
		
	}
	
	private static int[][] gerarPopulacaoInicial(int quant){
		
		Random rand = new Random();
		populacao = new int[quant][22];
		//System.out.println("Gerando população inicial...");
		
		for(int i = 0; i < quant; i++){
			for(int j = 0; j < 22; j++){
				if(rand.nextBoolean()){
					populacao[i][j] = 1;
				}
				else{
					populacao[i][j] = 0;
				}
				//System.out.print(populacao[i][j] + " ");
			}
			//System.out.println("\r\n");
			
		}
		
		return populacao;
		
	}
	
	private static int[][] selecaoTorneio(int tamPopulacaoInter, int tamPopulacaoInicial){
		
		Random rand = new Random();
		int inter = 50;
		boolean escolhidos[] = new boolean[tamPopulacaoInicial];
		for(int i = 0; i < tamPopulacaoInicial; i++){
			escolhidos[i] = false;
		}
		
		int contPopInter = 0;
		int[][] populacaoInter = new int[tamPopulacaoInter][22];
		int contTrue = 0;
		for(int i = 0; i < tamPopulacaoInter; i++){
			
			int escolhido1 = rand.nextInt(inter);
			while(escolhidos[escolhido1]){
				escolhido1 = rand.nextInt(inter);
			}
			escolhidos[escolhido1] = true;
			contTrue++;
			
			int escolhido2 = rand.nextInt(inter);
			while(escolhido1 == escolhido2 || escolhidos[escolhido2]){
				escolhido2 = rand.nextInt(inter);
			}
			escolhidos[escolhido2] = true;
			contTrue++;
			
			int[] cromossomo1 = getCromossomo(populacao, escolhido1);
			int[] cromossomo2 = getCromossomo(populacao, escolhido2);
			
			double fitness1 = fitness(getReal(getDecimal(cromossomo1)));
			double fitness2 = fitness(getReal(getDecimal(cromossomo2)));
			
			if(fitness1 > fitness2){
				populacaoInter[contPopInter] = cromossomo1;
				escolhidos[escolhido2] = false;
			}
			else{
				populacaoInter[contPopInter] = cromossomo2;
				escolhidos[escolhido1] = false;
			}
			contPopInter++;
			//System.out.println("i = " + i);
			//System.out.println("contTrue = " + contTrue);
			
		}
		
		/*System.out.println("População Intermediária: ");
		for(int i = 0; i < populacaoInter.length; i++){
			for(int j = 0; j < 22; j++){
				System.out.print(populacaoInter[i][j] + " ");
			}
			System.out.println("\r\n");
		}
		
		System.out.println("tamanho populacao inter: " + populacaoInter.length);*/
		
		return populacaoInter;
		
	}
	
	private static int[] getCromossomo(int[][] populacao, int c){
		
		int[] cromossomo = new int[22];
		for(int i = 0; i < 22; i++){
			cromossomo[i] = populacao[c][i];
		}
		return cromossomo;
		
	}
	
	private static int[][] cruzamento(int[][] populacao, double taxa){
		
		int quant = (int) (populacao.length*taxa);
		
		int[][] populacaoCruzada = new int[quant][22];
		
		for(int i = 0; i < populacao.length - 1; i++){
			double r = Math.random();
			if(r > taxa){
				
				int[] pai1 = getCromossomo(populacao, i);
				int[] pai2 = getCromossomo(populacao, i+1);
				
				int[] filho1 = new int[22];

				for(int f = 0; f < 22; f++){
					if(f < 11){
						filho1[f] = pai1[f];
					}
					else{
						filho1[f] = pai2[f];
					}
					
				}
				
				System.out.println(Arrays.toString(filho1));
				System.out.println("Tamanho Filho 1: " + filho1.length);
				
				int[] filho2 = new int[22];

				for(int f = 0; f < 22; f++){
					if(f < 11){
						filho2[f] = pai2[f];
					}
					else{
						filho2[f] = pai1[f];
					}
					
				}
				
				System.out.println(Arrays.toString(filho2));
				System.out.println("Tamanho Filho 2: " + filho1.length);
				
			}

			
		
			
		}
		
		return populacaoCruzada;
		
	}
	

}
