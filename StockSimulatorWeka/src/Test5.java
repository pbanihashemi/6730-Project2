import java.util.ArrayList;


public class Test5 {

	public static void main(String[] args){
		ArrayList<Double> d = new ArrayList<Double>();
		d.add(4.);
		d.add(3.);
		d.add(2.);
		d.add(1.);
		
		System.out.println(Stock.lowest(d, 0, 3));
		System.out.println(Stock.highest(d, 1, 3));
		
	}
	
}
