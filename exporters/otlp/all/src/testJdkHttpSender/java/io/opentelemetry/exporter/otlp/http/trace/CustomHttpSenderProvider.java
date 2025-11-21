package io.opentelemetry.exporter.otlp.http.trace;

import io.opentelemetry.exporter.internal.http.HttpSender;
import io.opentelemetry.exporter.internal.http.HttpSenderConfig;
import io.opentelemetry.exporter.internal.http.HttpSenderProvider;

public class CustomHttpSenderProvider implements HttpSenderProvider {

  @Override
  public HttpSender createSender(HttpSenderConfig httpSenderConfig) {
    return new CustomJdkHttpSender(
        httpSenderConfig.getEndpoint(),
        httpSenderConfig.getCompressor(),
        httpSenderConfig.getExportAsJson(),
        httpSenderConfig.getContentType(),
        httpSenderConfig.getTimeoutNanos(),
        httpSenderConfig.getConnectTimeoutNanos(),
        httpSenderConfig.getHeadersSupplier(),
        httpSenderConfig.getRetryPolicy(),
        httpSenderConfig.getProxyOptions(),
        httpSenderConfig.getSslContext(),
        httpSenderConfig.getExecutorService());
  }
}
