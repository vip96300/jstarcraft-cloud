package com.jstarcraft.cloud.monitor.link;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.core.utility.RandomUtility;

import io.opentelemetry.OpenTelemetry;
import io.opentelemetry.trace.Span;
import io.opentelemetry.trace.SpanContext;
import io.opentelemetry.trace.SpanId;
import io.opentelemetry.trace.TraceFlags;
import io.opentelemetry.trace.TraceId;
import io.opentelemetry.trace.TraceState;
import io.opentelemetry.trace.TraceState.Entry;
import io.opentelemetry.trace.Tracer;
import io.opentelemetry.trace.TracerProvider;

public class OpenTelemetryTestCase {

    @Test
    public void test() throws Exception {
        Random random = RandomUtility.getRandom();
        TracerProvider provider = OpenTelemetry.getTracerProvider();
        Tracer tracer = provider.get("sdk");
        TraceId traceId = new TraceId(random.nextLong(), random.nextLong());
        SpanId spanId = new SpanId(random.nextLong());
        TraceFlags traceFlag = TraceFlags.getDefault();
        TraceState traceState = TraceState.builder().set("key", "value").build();
        SpanContext context = SpanContext.create(traceId, spanId, traceFlag, traceState);

        // TODO 注意:按照目前OpenTelemetry逻辑,只要想携带上下文信息,无论如何都无法构建根Span
        Span span = tracer.spanBuilder("root").setParent(context).startSpan();
        Assert.assertEquals(traceId, span.getContext().getTraceId());
        Assert.assertNotEquals(spanId, span.getContext().getSpanId());
        for (Entry term : span.getContext().getTraceState().getEntries()) {
            Assert.assertEquals("key", term.getKey());
            Assert.assertEquals("value", term.getValue());
        }
        span.end();
    }

}
