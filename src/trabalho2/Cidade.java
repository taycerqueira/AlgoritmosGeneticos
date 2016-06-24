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
		
		int d = (int) Math.sqrt((Math.pow((this.x - x), 2) + (Math.pow((this.y - y), 2))));
		/*if(this.x != x){
			System.out.println("x1 = " + this.x);
			System.out.println("x2 = " + x);
			System.out.println("y1 = " + this.y);
			System.out.println("y2 = " + y);
			System.out.println("d = " + d);
			System.exit(0);
		}*/
		
		return d;
		
	}

}
