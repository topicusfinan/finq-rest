package nl.finan.jbehave.rest;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RunningContextLocal {

    private ThreadLocal<RunningContext> threadLocal;

    public RunningContext get() {
        if(threadLocal ==  null){
            threadLocal = new ThreadLocal<RunningContext>();
            threadLocal.set(new RunningContext());
        }
        return threadLocal.get();
    }

}
