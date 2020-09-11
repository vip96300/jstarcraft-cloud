package com.jstarcraft.cloud.monitor.link;

import org.junit.Test;

import com.jstarcraft.core.utility.RandomUtility;

import io.opencensus.trace.Span;
import io.opencensus.trace.SpanContext;
import io.opencensus.trace.SpanId;
import io.opencensus.trace.TraceId;
import io.opencensus.trace.TraceOptions;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracestate;
import io.opencensus.trace.Tracestate.Entry;
import io.opencensus.trace.Tracing;

public class OpenCensusTestCase {

    @Test
    public void test() {
        TraceId traceId = TraceId.generateRandomId(RandomUtility.getRandom());
//        traceId = TraceId.INVALID;
        SpanId spanId = SpanId.generateRandomId(RandomUtility.getRandom());
//        spanId = SpanId.INVALID;
        TraceOptions traceOption = TraceOptions.builder().build();
        Tracer tracer = Tracing.getTracer();
        Tracestate tracestate = Tracestate.builder().set("key", "value").build();
        SpanContext spanContext = SpanContext.create(traceId, spanId, traceOption, tracestate);
        System.out.println(traceId);
        System.out.println(spanId);

        Span span = tracer.spanBuilderWithRemoteParent("root", spanContext).startSpan();
        System.out.println(span.getContext().getTraceId());
        System.out.println(span.getContext().getSpanId());
        for(Entry term : span.getContext().getTracestate().getEntries()) {
            System.out.println(term.getKey() + ":" + term.getValue());
        }
//      
//      if (context == null) {
//          spanContext = SpanContext.;
//          SpanContext.create(traceId, spanId, traceOptions, tracestate)
//      } else {
//          TraceId traceId = TraceId.fromLowerBase16(context.getRoot());
//          SpanId spanId = SpanId.fromLowerBase16(context.getId());
//          TraceOptions traceOption = TraceOptions.builder().build();
//          Tracestate.builder().set(key, value)
//          spanContext = SpanContext.create(traceId, spanId, traceOption, tracestate)
//      }
//      tracer.spanBuilderWithExplicitParent(spanName, parent)
//      tracer.spanBuilderWithRemoteParent(name, spanContext);
    }

}
