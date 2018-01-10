package fr.sttc.ttt.tttserver;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ThreadConfig {

    private final BeanFactory beanFactory;

    @Autowired
    public ThreadConfig(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Bean
    public ExecutorService getExecutorService() {

        ExecutorService delegate = Executors.newCachedThreadPool();
        return new TraceableExecutorService(beanFactory, delegate);
    }
}
