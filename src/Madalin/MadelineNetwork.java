package Madalin;

import java.util.ArrayList;
import java.util.List;

import Neuron.Neuron;
import Pattern.Pattern;

public class MadelineNetwork {

	private List<Neuron> inputNeurons;

	public MadelineNetwork(List<Neuron> neurons) {
		this.setNeurons(neurons);
	}

	public List<Neuron> getNeurons() {
		return inputNeurons;
	}

	public void setNeurons(List<Neuron> neurons) {
		this.inputNeurons = neurons;
	}

	public List<Neuron> calculateResult(Pattern patterns) {
		return computeResult(this.getNeurons(), patterns);
	}

	public List<Neuron> normalizeInputLayer(Pattern patterns) {
		return computeInputLayer(this.getNeurons(), patterns);
	}

	public List<Neuron> computeInputLayer(List<Neuron> neurons, Pattern patterns) {

		for (Neuron neuron : neurons) {
			neuron.setWeights(normalize(neuron.getInputs(), patterns.sumOnes()));
		}
		return neurons;
	}

	public List<Neuron> computeResult(List<Neuron> neurons, Pattern patterns) {
		for (Neuron neuron : neurons) {
			neuron.setInputs(normalize(neuron.getInputs(), patterns.sumOnes()));
		}
		return neurons;
	}

	public double[] normalize(double[] inputs, int ones) {
		double[] output = new double [inputs.length];
		for (int i = 0; i < inputs.length; i++) {
			output[i] = inputs[i] / Math.sqrt(ones);
		}
		return output;

	}

}
