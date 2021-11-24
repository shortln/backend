package org.shortln;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.Function;

@Configuration
public class CallFunByEnv<P, R> {
    @Value("${spring.profiles.active:#{null}}")
    private String active;

    public R run(Map<String, Function<P, R>> envsRun, P arg) {
        return envsRun.get(active == null ? "dev" : active).apply(arg);
    }
}
