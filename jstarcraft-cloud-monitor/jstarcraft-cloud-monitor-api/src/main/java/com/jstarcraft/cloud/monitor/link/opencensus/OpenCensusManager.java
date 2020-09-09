package com.jstarcraft.cloud.monitor.link.opencensus;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Callable;

import com.jstarcraft.cloud.monitor.link.LinkContext;
import com.jstarcraft.cloud.monitor.link.LinkManager;
import com.jstarcraft.cloud.monitor.link.LinkSpan;

public class OpenCensusManager implements LinkManager {

    private final ThreadLocal<LinkedList<OpenCensusSpan>> spans = new ThreadLocal<LinkedList<OpenCensusSpan>>() {

        protected LinkedList<OpenCensusSpan> initialValue() {
            return new LinkedList<>();
        }

    };

    @Override
    public <V> V doSpan(String name, LinkContext context, Map<String, String> properties, Callable<V> task) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void doSpan(String name, LinkContext context, Map<String, String> properties, Runnable task) {
        // TODO Auto-generated method stub

    }

    @Override
    public LinkSpan getSpan() {
        // TODO Auto-generated method stub
        return null;
    }

}
