package org.processmining.localprocessmodeldiscoverybycombiningplaces.connections;

import org.processmining.framework.connections.impl.AbstractConnection;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.models.YourFirstInput;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.models.YourOutput;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.models.YourSecondInput;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.parameters.YourParameters;

public class YourConnection extends AbstractConnection {

	/**
	 * Label for first input.
	 */
	public final static String FIRSTINPUT = "First Input";
	
	/**
	 * Label for second input.
	 */
	public final static String SECONDINPUT = "Second Input";
	
	/**
	 * Label for output.
	 */
	public final static String OUTPUT = "Output";

	/**
	 * Private copy of parameters.
	 */
	private YourParameters parameters;

	/**
	 * Create a connection.
	 * @param input1 First input.
	 * @param input2 Second input.
	 * @param output Output.
	 * @param parameters Parameters.
	 */
	public YourConnection(YourFirstInput input1, YourSecondInput input2, YourOutput output, YourParameters parameters) {
		super("Your Connection");
		put(FIRSTINPUT, input1);
		put(SECONDINPUT, input2);
		put(OUTPUT, output);
		this.parameters = new YourParameters(parameters);
	}

	/**
	 * 
	 * @return The parameters stored in the connection.
	 */
	public YourParameters getParameters() {
		return parameters;
	}
}
