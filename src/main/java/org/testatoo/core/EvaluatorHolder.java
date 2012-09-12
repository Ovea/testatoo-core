/**
 * Copyright (C) 2008 Ovea <dev@testatoo.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testatoo.core;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author dev@testatoo.org
 */
public final class EvaluatorHolder {

    private static ThreadLocal<ExecutorService> EXECUTOR = new ThreadLocal<ExecutorService>();

    private static Map<String, Evaluator<?>> EVALUATORS = new ConcurrentHashMap<String, Evaluator<?>>();

    private static ThreadLocal<Deque<Evaluator<?>>> currentEvaluators = new ThreadLocal<Deque<Evaluator<?>>>() {
        @Override
        protected Deque<Evaluator<?>> initialValue() {
            return new LinkedList<Evaluator<?>>();
        }
    };

    private EvaluatorHolder() {
    }

    public static void setConcurrentExecutor(ExecutorService executor) {
        EvaluatorHolder.EXECUTOR.set(executor);
    }

    public static void clearConcurrentExecutor() {
        EvaluatorHolder.EXECUTOR.remove();
    }

    public static void register(Evaluator evaluator) {
        if (evaluator == null) {
            throw new IllegalArgumentException("Evaluator cannot be null");
        }
        if (evaluator.name() == null) {
            throw new IllegalArgumentException("Evaluator name cannot be null");
        }
        EVALUATORS.put(evaluator.name(), evaluator);
    }

    @SuppressWarnings({"unchecked"})
    public static <T extends Evaluator<?>> T get() {
        Evaluator<?> evaluator = currentEvaluators.get().peek();
        // if for this thread we ave no evaluator set yet, we get the default one
        if (evaluator == null) {
            evaluator = EVALUATORS.get(Evaluator.DEFAULT_NAME);
            currentEvaluators.get().offer(evaluator);
        }
        // if still null, not evaluator has been chosen by user
        if (evaluator == null) {
            throw new IllegalStateException(String.format("No evaluator is bound to local thread."));
        }
        return (T) evaluator;
    }

    public static void unregister(String name) {
        Evaluator<?> evaluator = EVALUATORS.remove(name);
        if (evaluator != null)
            currentEvaluators.get().remove(evaluator);
    }

    public static RunnerBuilder withEvaluator(String evaluatorName) {
        return new RunnerBuilder(evaluatorName);
    }

    private static ExecutorService getConcurrentExecutor() {
        if (EXECUTOR.get() == null)
            throw new IllegalStateException("No Executor found");
        return EXECUTOR.get();
    }

    public static final class RunnerBuilder {

        private final String evaluatorName;

        private RunnerBuilder(String evaluatorName) {
            this.evaluatorName = evaluatorName;
        }

        public <V> V run(Callable<V> callable) throws Exception {
            Evaluator<?> evaluator = EVALUATORS.get(evaluatorName);
            if (evaluator == null) {
                throw new IllegalArgumentException("Unknown evaluator: " + evaluatorName);
            }
            currentEvaluators.get().offerFirst(evaluator);
            try {
                return callable.call();
            } finally {
                currentEvaluators.get().pop();
            }
        }

        public void run(Runnable runnable) {
            Evaluator<?> evaluator = EVALUATORS.get(evaluatorName);
            if (evaluator == null) {
                throw new IllegalArgumentException("Unknown evaluator: " + evaluatorName);
            }
            currentEvaluators.get().offerFirst(evaluator);
            try {
                runnable.run();
            } finally {
                currentEvaluators.get().pop();
            }
        }

        public <V> Future<V> runInParallel(final Callable<V> callable) {
            return getConcurrentExecutor().submit(new Callable<V>() {
                @Override
                public V call() throws Exception {
                    return run(callable);
                }
            });
        }

        public Future<?> runInParallel(final Runnable runnable) {
            return getConcurrentExecutor().submit(new Runnable() {
                @Override
                public void run() {
                    RunnerBuilder.this.run(runnable);
                }
            });
        }
    }

}
