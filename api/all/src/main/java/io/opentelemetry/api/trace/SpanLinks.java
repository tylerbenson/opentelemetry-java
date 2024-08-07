/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.api.trace;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import io.opentelemetry.context.Context;
import io.opentelemetry.context.ImplicitContextKeyed;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public interface SpanLinks extends ImplicitContextKeyed {

  static Context updateParentAndAddSpan(Context context, SpanContext spanContext) {
    SpanContext existingSpanContext = Span.fromContext(context).getSpanContext();
    if(existingSpanContext.equals(spanContext)) {
      return context;
    }
    SpanLinks links = fromContextOrNull(context);
    if(links == null) {
      links = ParentExcludedSpanLinks.create(spanContext, existingSpanContext);
    } else {
      links.updateParent(spanContext);
    }
    return context.with(Span.wrap(spanContext)).with(links);
  }

  /**
   * Returns the {@link SpanLinks} from the specified {@link Context}, falling back to a default, no-op
   * {@link SpanLinks} if there is no span in the context.
   */
  static SpanLinks fromContext(Context context) {
    SpanLinks links = fromContextOrNull(context);
    return links == null ? ParentExcludedSpanLinks.create(Span.fromContext(context).getSpanContext()) : links;
  }

  /**
   * Returns the {@link SpanLinks} from the specified {@link Context}, or {@code null} if there is no
   * span in the context.
   */
  @Nullable
  static SpanLinks fromContextOrNull(Context context) {
    return context.get(SpanLinksKey.KEY);
  }

  ImplicitContextKeyed updateParent(SpanContext spanContext);

  void consume(Consumer<SpanContext> consumer);

  @Override
  default Context storeInContext(Context context) {
    return context.with(SpanLinksKey.KEY, this);
  }
}
