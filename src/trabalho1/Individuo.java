package trabalho1;

public class Individuo {
	
	int[] cromossomo;
	
	public Individuo(int[] vetor){
		
		this.cromossomo = vetor;
		
	}
	
	public int[] getVetorCromossomo(){
		
		return this.cromossomo;
		
	}
	
	public int getValorDecimal(){
		
		int decimal = 0;
		
		int cont = 21;
		for(int i = 0; i < 22; i++){
			decimal += (cromossomo[i]*Math.pow(2, cont));
			cont--;
		}
		
		return decimal;
		
	}
	
	public double getValorReal(){
		
		int x = getValorDecimal();
		
		double real = 0;
		
		real = (-1.0) + x*(3/(Math.pow(2, 22) - 1));
		
		return real;
	}
	
	public double getFitness(){
		
		double x = getValorReal();
		
		return (x*Math.sin(10*Math.PI*x) + 1);
		
	}

}
