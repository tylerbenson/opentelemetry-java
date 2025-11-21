package io.opentelemetry.exporter.otlp.http.trace;

import io.opentelemetry.api.trace.TracerProvider;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DemoHttpSender {

  @BeforeAll
  static void beforeAll() {
    System.setProperty(
        "io.opentelemetry.exporter.internal.http.HttpSenderProvider",
        "io.opentelemetry.exporter.otlp.http.trace.CustomHttpSenderProvider");
  }

  @AfterAll
  static void afterAll() {
    System.clearProperty("io.opentelemetry.exporter.internal.http.HttpSenderProvider");
  }

  @Test
  public void testServiceLoader() {
    OtlpHttpSpanExporter spanExporter = OtlpHttpSpanExporter.builder().build();
    TracerProvider tracerProvider =
        SdkTracerProvider.builder()
            .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
            .build();
    Logger.getAnonymousLogger().info(tracerProvider.toString());
  }
}
