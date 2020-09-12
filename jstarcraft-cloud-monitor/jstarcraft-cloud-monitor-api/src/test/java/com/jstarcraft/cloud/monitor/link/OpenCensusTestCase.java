package com.jstarcraft.cloud.monitor.link;

import java.util.Collection;

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
import io.opencensus.trace.config.TraceConfig;
import io.opencensus.trace.config.TraceParams;
import io.opencensus.trace.export.SpanData;
import io.opencensus.trace.export.SpanExporter.Handler;
import io.opencensus.trace.samplers.Samplers;

public class OpenCensusTestCase {

    @Test
    public void test() throws Exception {
        // 获取追踪配置
        TraceConfig traceConfig = Tracing.getTraceConfig();
        // 获取追踪参数
        TraceParams traceParams = traceConfig.getActiveTraceParams();
        // 设置采样率为100%
        traceParams = traceParams.toBuilder().setSampler(Samplers.alwaysSample()).build();
        // 设置最总参数
        traceConfig.updateActiveTraceParams(traceParams);
        // 注册追踪单元处理器
        Tracing.getExportComponent().getSpanExporter().registerHandler("mock", new Handler() {

            @Override
            public void export(Collection<SpanData> datas) {
                System.out.println("export");
                System.out.println(datas);
            }

        });

        Tracer tracer = Tracing.getTracer();
        TraceId traceId = TraceId.generateRandomId(RandomUtility.getRandom());
        SpanId spanId = SpanId.generateRandomId(RandomUtility.getRandom());
        TraceOptions traceOption = TraceOptions.builder().build();
        Tracestate tracestate = Tracestate.builder().set("key", "value").build();
        SpanContext spanContext = SpanContext.create(traceId, spanId, traceOption, tracestate);
        System.out.println(traceId);
        System.out.println(spanId);

        Span span = tracer.spanBuilderWithRemoteParent("root", spanContext).startSpan();
        System.out.println(span.getContext().getTraceId());
        System.out.println(span.getContext().getSpanId());
        for (Entry term : span.getContext().getTracestate().getEntries()) {
            System.out.println(term.getKey() + ":" + term.getValue());
        }

        span.end();
        Thread.sleep(10000L);

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
