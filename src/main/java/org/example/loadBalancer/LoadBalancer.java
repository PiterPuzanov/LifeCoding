package org.example.loadBalancer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadBalancer {

    private static final int MAX_INSTANCES = 10;

    private final List<String> instances = new CopyOnWriteArrayList<>();
    private final AtomicInteger index = new AtomicInteger();

    public void register(String instanceId) {

        if (instances.size() >= MAX_INSTANCES) {
            throw new IllegalStateException("Max instances reached");
        }

        if (instances.contains(instanceId)) {
            throw new IllegalArgumentException("Duplicate instance");
        }

        instances.add(instanceId);
    }

    public String get() {

        if (instances.isEmpty()) {
            throw new IllegalStateException("No instances registered");
        }

        int i = Math.abs(index.getAndIncrement() % instances.size());

        return instances.get(i);
    }
}

