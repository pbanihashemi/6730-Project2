import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import au.com.bytecode.opencsv.CSVReader;

public class Stock {
	
	// used by generateStockDataset to create instances
	public static void generateInstances(BufferedWriter writer, ArrayList<Double> prices, ArrayList<Double> lows, ArrayList<Double> highs) throws Exception{
		String[] attributes; 
		String[] instance;
		int i, window;double avg;
		
		// define attributes
		i = 1;
		window = 5;
		attributes = new String[]{"PrevChange","Stochastic","Momentum","LarryWilliams","Disparity","Price","Low","High","NextChange"};
		//attributes = new String[]{"ClosePrice","Low","High","NextChange"};
		instance = new String[attributes.length];
		writer.write(CSVLine(attributes));
		
		// create each instance
		while (true){
			
			instance[0] = stockChangeStr(prices, i, i+1);
			instance[1] = stochasticStr(prices, lows, highs, i, window);
			instance[2] = momentumStr(prices, i, window);
			instance[3] = larryWilliamsStr(prices, lows, highs, i, window);
			instance[4] = disparityStr(prices, i, window);
			instance[5] = Double.toString(prices.get(i));
			instance[6] = Double.toString(lows.get(i));
			instance[7] = Double.toString(highs.get(i));
			
			/*
			instance[0] = Double.toString(prices.get(i));
			instance[1] = Double.toString(lows.get(i));
			instance[2] = Double.toString(highs.get(i));
			*/
			
			instance[attributes.length-1] = stockChangeStr(prices, i-1, i);
			writer.write(CSVLine(instance));
			i++;
		}
	}
	
	// Generates a dataset from a list of stock prices
	public static void generateStockDataset(String inFile, String outFile, int fileFormat){

		CSVReader reader;
		String[] nextLine;
		String[] instance;
		String[] attributes;
		ArrayList<Double> prices = new ArrayList<Double>();
		ArrayList<Double> highs = new ArrayList<Double>();
		ArrayList<Double> lows = new ArrayList<Double>();
		
		BufferedWriter writer = null;
		
		// If file came from Yahoo Historical Prices
		if (fileFormat == Values.YAHOO){
			
			// read file and create dataset
			try {
				reader = new CSVReader(new FileReader(inFile));
				reader.readNext();
				nextLine = reader.readNext();
				
				// read each line and extracts prices
				while (nextLine!=null){
					highs.add(Double.parseDouble(nextLine[2]));
					lows.add(Double.parseDouble(nextLine[3]));
					prices.add(Double.parseDouble(nextLine[4]));
					nextLine = reader.readNext();
				}
				reader.close();
				
				// Define attributes for instances
				writer = new BufferedWriter(new FileWriter(outFile));
				generateInstances(writer, prices, lows, highs);
				
			}
			catch (IndexOutOfBoundsException e){}
			catch (Exception e){
				e.printStackTrace();
			}
			try{
				writer.close();
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
		
		else{
			return;
		}
		
	}
	

	// change of stock price between 2 days
	public static double stockChange(ArrayList<Double> prices, int nextIdx, int prevIdx){
		double prev = prices.get(prevIdx);
		double next = prices.get(nextIdx);
		return (next-prev)/prev*100;	
	}
	public static String stockChangeStr(ArrayList<Double> prices, int nextIdx, int prevIdx){
		return Double.toString(stockChange(prices, nextIdx, prevIdx));
	}
	
	
	// average stock change over range of days
	public static double stockChangeAverage(ArrayList<Double> prices, int idx, int window){
		double avg = 0;
		for (int j=idx+1; j<=idx+window; j++) avg += stockChange(prices, j, j+1);
		avg /= window;
		
		return avg;
	}
	public static String stockChangeAverageStr(ArrayList<Double> prices, int idx, int window){
		return Double.toString(stockChangeAverage(prices, idx, window));
	}
	
	// converts string array to csv line
	public static String CSVLine(String[] arr){
		int len = arr.length;
		String temp = arr[0];
		for (int i=1; i<len; i++) temp+=", "+arr[i];
		return temp+"\n";
	}
	

	// gets the lowest price in a range of days
	public static double lowest(ArrayList<Double> prices, int idx, int window){
		double low = prices.get(idx);
		for (int i=idx; i<idx+window; i++) low = Math.min(low, prices.get(i));
		return low;
	}
			

	// gets the lowest price in a range of days
	public static double highest(ArrayList<Double> prices, int idx, int window){
		double high = prices.get(idx);
		for (int i=idx; i<idx+window; i++) high = Math.max(high, prices.get(i));
		return high;
	}	
	
	// returns the stochastic percent value 
	public static double stochastic(ArrayList<Double> prices, ArrayList<Double> lows, ArrayList<Double> highs, int idx, int window){
		double l = lowest(lows, idx, window);
		double h = highest(highs, idx, window);
		return (prices.get(idx)-l)/(h-l)*100;
	}
	public static String stochasticStr(ArrayList<Double> prices, ArrayList<Double> lows, ArrayList<Double> highs, int idx, int window){
		return Double.toString(stochastic(prices, lows, highs, idx, window));
	}
	
	
	
	public static double momentum(ArrayList<Double> prices, int idx, int window){
		return prices.get(idx)-prices.get(idx+window);
	}
	public static String momentumStr(ArrayList<Double> prices, int idx, int window){
		return Double.toString(momentum(prices, idx, window));
	}
	
	
	
	public static double larryWilliams(ArrayList<Double> prices, ArrayList<Double> lows, ArrayList<Double> highs, int idx, int window){
		double l = lowest(lows, idx, window);
		double h = highest(highs, idx, window);
		return (h-prices.get(idx))/(h-l)*100;
	}
	public static String larryWilliamsStr(ArrayList<Double> prices, ArrayList<Double> lows, ArrayList<Double> highs, int idx, int window){
		return Double.toString(larryWilliams(prices, lows, highs, idx, window));
	}
	
	
	
	public static double disparity(ArrayList<Double> prices, int idx, int window){
		return prices.get(idx)/stockChangeAverage(prices, idx, window)*100;
	}
	public static String disparityStr(ArrayList<Double> prices, int idx, int window){
		return Double.toString(disparity(prices, idx, window));
	}
}
