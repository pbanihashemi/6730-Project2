import java.util.Random;

import weka.core.*;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.*;
import weka.classifiers.functions.*;

public class Test1 {

	public static void main(String[] args) {
		
		String filename = "bin/iris.csv";
		Instances instances = null;
		Instance inst;
		DataSource source = null;
		Classifier network = new MultilayerPerceptron();
		Evaluation eval;
		Attribute[] a;
		
		
		try {
			source = new DataSource(filename);
			instances = source.getDataSet();
			instances.setClassIndex(instances.numAttributes()-1);
			network.buildClassifier(instances);
			eval = new Evaluation(instances);
			
			eval.crossValidateModel(network, instances, 10, new Random(1));
			
			System.out.println(network);
			System.out.println(eval.toSummaryString(true));
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		a = new Attribute[instances.numAttributes()];
		for (int i=0; i<instances.numAttributes(); i++) a[i] = instances.attribute(i);
		
		//System.out.println(data);
		/*
		inst = new Instance(instances.numAttributes());
		
		inst.setValue(a[0], 6.3);
		inst.setValue(a[1], 2.7);
		inst.setValue(a[2], 5.5);
		inst.setValue(a[3], 2.0);
		
		
		try {
			System.out.println(network.classifyInstance(inst));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		*/
	}

}
