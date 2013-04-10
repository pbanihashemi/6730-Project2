import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import au.com.bytecode.opencsv.CSVReader;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;


public class Helper {

	public static Instances getInstances(String filename){
		Instances instances = null;
		DataSource source = null;
		PrintStream err = null;
		InputStream errorCheck;
		
		try {
			errorCheck = new FileInputStream(new File(filename));
			errorCheck.close();
			
			err = System.err;
			System.setErr(new PrintStream("bin/dump.txt"));
			source = new DataSource(filename);
			System.setErr(err);
			
			instances = source.getDataSet();
			instances.setClassIndex(instances.numAttributes()-1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return instances;
	}
	
	// uses 10-fold cross validation to build a neural network
	public static MultilayerPerceptron buildANN(Instances instances){
		
		MultilayerPerceptron network = new MultilayerPerceptron();
				
		try {
			network.buildClassifier(instances);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return network;
	}
	
	public static MultilayerPerceptron buildANN(String filename){
		return buildANN(Helper.getInstances(filename));
	}
	
	public static void evaluateClassifier(Classifier classifier, Instances instances, boolean simple){
		
		Evaluation eval = null;
		
		try {
			eval = new Evaluation(instances);
			eval.crossValidateModel(classifier, instances, 10, new Random(1));
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		System.out.println("Evaluation of Data Set: "+instances.relationName());
		if (!simple) System.out.println(classifier);
		System.out.println(eval.toSummaryString(true));
	}
	public static void evaluateClassifier(Classifier classifier, Instances instances){
		evaluateClassifier(classifier, instances, false);
	}
	
	public static void analyzeClassifications(Classifier classifier, Instances instances, boolean printClasses){
		Instance inst;
		double sum, sumSquared, est, act, num;
		int truePos, trueNeg, falsePos, falseNeg;
		truePos=0;
		falsePos=0;
		trueNeg=0;
		falseNeg=0;
		num = instances.numInstances();
		sum=0;
		sumSquared=0;
		
		try {
			for (int i=0; i<num; i++){
				inst = instances.instance(i);
				est = classifier.classifyInstance(inst);
				act = inst.classValue();
				
				if (printClasses) System.out.println(est+"\t"+act);
				
				sum += Math.abs(est-act);
				sumSquared += Math.pow(Math.abs(est-act),2);
				
				if (est>=0 && act>=0) truePos++;
				else if (est<0 && act<0) trueNeg++;
				else if (est>=0 && act<0) falsePos++;
				else if (est<0 && act>=0) falseNeg++;
				
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Mean Error: "+sum/num);
		System.out.println("Squared Mean Error: "+sumSquared/num);
		System.out.println("Pos/Neg Accuracy: "+(double)(truePos+trueNeg)/(double)num);
		System.out.println("True Positives: "+truePos);
		System.out.println("True Negatives: "+trueNeg);
		System.out.println("False Positives: "+falsePos);
		System.out.println("False Negatives: "+falseNeg);
		
	}
	
	
}
