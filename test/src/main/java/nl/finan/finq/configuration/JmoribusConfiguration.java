package nl.finan.finq.configuration;

import nl.eernie.jmoribus.configuration.Configuration;
import nl.eernie.jmoribus.context.ContextProvider;
import nl.eernie.jmoribus.context.DefaultContextProvider;
import nl.eernie.jmoribus.reporter.ConcurrentReporter;
import nl.eernie.jmoribus.reporter.Reporter;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class JmoribusConfiguration implements Configuration {

    private ConcurrentReporter concurrentReporter = new ConcurrentReporter();
    private ContextProvider contextProvider = new DefaultContextProvider();
    private List<Object> steps = new ArrayList<>();

    public ConcurrentReporter getConcurrentReporter() {
        return concurrentReporter;
    }

    public void addReporter(Reporter reporter) {
        concurrentReporter.addReporter(reporter);
    }

    @Override
    public List<Object> getSteps() {
        return steps;
    }

    @Override
    public void addSteps(List<Object> steps) {
        this.steps.addAll(steps);
    }

    public ContextProvider getContextProvider() {
        return contextProvider;
    }

    @Override
    public WebDriver getWebDriver() {
        return null;
    }
}
