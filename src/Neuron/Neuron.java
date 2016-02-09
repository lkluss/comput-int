package Neuron;

import java.util.List;

import Activation.ActivationFunction;

public class Neuron {



	private double biasValue;
	private double biasWeight;
	private double previousBiasWeight;

	private double inputs[];
	private double weights[];
	private double previousWeights[];

	private double output;
	private double error;

	private ActivationFunction activationFunction;

	public Neuron(ActivationFunction activationFunction, double[] weights, double[] input) {
		this.weights = weights;
		this.activationFunction = activationFunction;
		this.inputs = input;
	}

	public Neuron(){}

	public double[] getWeights() {
		return weights;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}

	public void setWeights(double bias, double[] weights) {
		this.setPreviousWeights(this.weights);

		this.weights = weights;

		this.setPreviousBiasWeight(this.biasWeight);
		this.setBiasWeight(biasWeight);
	}

	public double[] getInputs() {
		return inputs;
	}

	public void setInputs(double[] inputs) {
		this.inputs = inputs;
	}

	public ActivationFunction getActivationFunction() {
		return activationFunction;
	}

	public void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}

	public double getBiasValue() {
		return biasValue;
	}

	public void setBiasValue(double biasValue) {
		this.biasValue = biasValue;
	}

	public double getBiasWeight() {
		return biasWeight;
	}

	public void setBiasWeight(double biasWeight) {
		this.biasWeight = biasWeight;
	}

	public double getPreviousBiasWeight() {
		return previousBiasWeight;
	}

	public void setPreviousBiasWeight(double previousBiasWeight) {
		this.previousBiasWeight = previousBiasWeight;
	}

	public double[] getPreviousWeights() {
		return previousWeights;
	}

	public void setPreviousWeights(double[] previousWeights) {
		this.previousWeights = previousWeights;
	}

	public void calculateOutput() {
		double result = 0;
		result += biasValue * biasWeight;

		for (int i = 0; i < inputs.length; i++) {
			double d = inputs[i];
			d *= weights[i];
			result += d;
		}
		this.output = result;

	}

	public double getAnswer() {
		return activationFunction.calculateOutput(output);
	}

	public double calculateWeightedSum(double[] inputs) {
		double netInput = 0.0;
		for (int i = 0; i < weights.length; i++) {
			netInput += weights[i] * inputs[i];
		}
		return netInput;
	}

	public void presentWeights() {

		String printable = "WEIGHTS: ";

		for (Double weight : weights) {
			printable += weight + ", ";
		}

		String substr = printable.substring(0, printable.length() - 1);

		System.out.println(substr);

	}

	public void presentInputs() {

		String printable = "INPUTS: ";

		for (Double input : inputs) printable += input + ", ";

		String substr = printable.substring(0, printable.length() - 1);

		System.out.println(substr);

	}

	public void updateWeight(double value, int i) {

		this.weights[i] = value;
	}

}
