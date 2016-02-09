package Activation;

public interface ActivationFunction {
	
	public double calculateOutput(double netInput);
	public double calculateDerivative(double netInput);
	
}