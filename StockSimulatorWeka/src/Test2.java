import weka.core.*;
import weka.classifiers.*;

public class Test2 {

	public static void main(String[] args) {

		
		String filename = "stock/iris.csv";
		Instances instances = Helper.getInstances(filename);
		Classifier network = Helper.buildANN(instances);
		
		Helper.evaluateClassifier(network, instances);
		
		
		
	}

}
