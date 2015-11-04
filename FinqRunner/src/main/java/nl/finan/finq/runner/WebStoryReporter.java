package nl.finan.finq.runner;

import nl.eernie.jmoribus.reporter.Reporter;

import javax.ejb.Local;

@Local
public interface WebStoryReporter extends Reporter
{
	void afterSuccessRun(long runningProcessId);

	void afterErrorRun(long runningProcessId);
}
