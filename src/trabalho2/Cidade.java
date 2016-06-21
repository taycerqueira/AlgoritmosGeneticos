package trabalho2;

public class Cidade {
	
	private int numero;
	private double x;
	private double y;
	
	public Cidade(int numero, double x, double y) {
		this.numero = numero;
		this.x = x;
		this.y = y;
	}
	
	public double getNumero() {
		return numero;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public double getDistanciaEuclidiana(double x, double y){
		
		return Math.sqrt((Math.pow((x - this.x), 2) + (Math.pow((y - this.y), 2))));
		
	}

}
