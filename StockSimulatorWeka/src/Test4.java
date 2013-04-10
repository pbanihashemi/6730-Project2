import weka.classifiers.Classifier;
import weka.core.Instances;


public class Test4 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String inFile, outFile;
		//inFile = "stock/HLF.csv";
		//outFile = "stock/HLF_dataset.csv";
		inFile = "stock/GOOG_sparse.csv";
		outFile = "stock/GOOG_dataset.csv";
		Instances instances;
		Classifier network; 
		
		Stock.generateStockDataset(inFile, outFile, Values.YAHOO);

		instances = Helper.getInstances(outFile);
		network = Helper.buildANN(instances);
		
		Helper.evaluateClassifier(network, instances);
		Helper.analyzeClassifications(network, instances, false);
		
	}

}
