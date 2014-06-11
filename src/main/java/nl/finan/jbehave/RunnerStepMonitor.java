package nl.finan.jbehave;

import java.io.PrintStream;

import org.jbehave.core.steps.PrintStreamStepMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunnerStepMonitor extends PrintStreamStepMonitor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RunnerStepMonitor.class);
	
	@Override
	protected void print(PrintStream output, String message) {
		LOGGER.info("Message = {}", message);
		super.print(output, message);
	}

	
	
}
