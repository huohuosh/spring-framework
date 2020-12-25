/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.scheduling;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.springframework.lang.Nullable;

/**
 * Task scheduler interface that abstracts the scheduling of
 * {@link Runnable Runnables} based on different kinds of triggers.
 *
 * <p>This interface is separate from {@link SchedulingTaskExecutor} since it
 * usually represents for a different kind of backend, i.e. a thread pool with
 * different characteristics and capabilities. Implementations may implement
 * both interfaces if they can handle both kinds of execution characteristics.
 *
 * <p>The 'default' implementation is
 * {@link org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler},
 * wrapping a native {@link java.util.concurrent.ScheduledExecutorService}
 * and adding extended trigger capabilities.
 *
 * <p>This interface is roughly equivalent to a JSR-236
 * {@code ManagedScheduledExecutorService} as supported in Java EE 7
 * environments but aligned with Spring's {@code TaskExecutor} model.
 *
 * @author Juergen Hoeller
 * @since 3.0
 * @see org.springframework.core.task.TaskExecutor
 * @see java.util.concurrent.ScheduledExecutorService
 * @see org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
 */
public interface TaskScheduler {

	/**
	 * Schedule the given {@link Runnable}, invoking it whenever the trigger
	 * indicates a next execution time.
	 * <p>Execution will end once the scheduler shuts down or the returned
	 * {@link ScheduledFuture} gets cancelled.
	 * @param task the Runnable to execute whenever the trigger fires
	 * @param trigger an implementation of the {@link Trigger} interface,
	 * e.g. a {@link org.springframework.scheduling.support.CronTrigger} object
	 * wrapping a cron expression
	 * @return a {@link ScheduledFuture} representing pending completion of the task,
	 * or {@code null} if the given Trigger object never fires (i.e. returns
	 * {@code null} from {@link Trigger#nextExecutionTime})
	 * @throws org.springframework.core.task.TaskRejectedException if the given task was not accepted
	 * for internal reasons (e.g. a pool overload handling policy or a pool shutdown in progress)
	 * @see org.springframework.scheduling.support.CronTrigger
	 * 提交任务调度请求
	 * Runnable task：待执行得任务
	 * Trigger trigger：使用 Trigger 指定任务调度规则
	 */
	@Nullable
	ScheduledFuture<?> schedule(Runnable task, Trigger trigger);

	/**
	 * Schedule the given {@link Runnable}, invoking it at the specified execution time.
	 * <p>Execution will end once the scheduler shuts down or the returned
	 * {@link ScheduledFuture} gets cancelled.
	 * @param task the Runnable to execute whenever the trigger fires
	 * @param startTime the desired execution time for the task
	 * (if this is in the past, the task will be executed immediately, i.e. as soon as possible)
	 * @return a {@link ScheduledFuture} representing pending completion of the task
	 * @throws org.springframework.core.task.TaskRejectedException if the given task was not accepted
	 * for internal reasons (e.g. a pool overload handling policy or a pool shutdown in progress)
	 * @since 5.0
	 * @see #schedule(Runnable, Date)
	 */
	default ScheduledFuture<?> schedule(Runnable task, Instant startTime) {
		return schedule(task, Date.from(startTime));
	}

	/**
	 * Schedule the given {@link Runnable}, invoking it at the specified execution time.
	 * <p>Execution will end once the scheduler shuts down or the returned
	 * {@link ScheduledFuture} gets cancelled.
	 * @param task the Runnable to execute whenever the trigger fires
	 * @param startTime the desired execution time for the task
	 * (if this is in the past, the task will be executed immediately, i.e. as soon as possible)
	 * @return a {@link ScheduledFuture} representing pending completion of the task
	 * @throws org.springframework.core.task.TaskRejectedException if the given task was not accepted
	 * for internal reasons (e.g. a pool overload handling policy or a pool shutdown in progress)
	 * 提交任务调度请求
	 * startTime 表示它的执行时间
	 * 注意任务只执行一次，使用 startTime 指定其启动时间
	 */
	ScheduledFuture<?> schedule(Runnable task, Date startTime);

	/**
	 * Schedule the given {@link Runnable}, invoking it at the specified execution time
	 * and subsequently with the given period.
	 * <p>Execution will end once the scheduler shuts down or the returned
	 * {@link ScheduledFuture} gets cancelled.
	 * @param task the Runnable to execute whenever the trigger fires
	 * @param startTime the desired first execution time for the task
	 * (if this is in the past, the task will be executed immediately, i.e. as soon as possible)
	 * @param period the interval between successive executions of the task
	 * @return a {@link ScheduledFuture} representing pending completion of the task
	 * @throws org.springframework.core.task.TaskRejectedException if  the given task was not accepted
	 * for internal reasons (e.g. a pool overload handling policy or a pool shutdown in progress)
	 * @since 5.0
	 * @see #scheduleAtFixedRate(Runnable, Date, long)
	 */
	default ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Instant startTime, Duration period) {
		return scheduleAtFixedRate(task, Date.from(startTime), period.toMillis());
	}

	/**
	 * Schedule the given {@link Runnable}, invoking it at the specified execution time
	 * and subsequently with the given period.
	 * <p>Execution will end once the scheduler shuts down or the returned
	 * {@link ScheduledFuture} gets cancelled.
	 * @param task the Runnable to execute whenever the trigger fires
	 * @param startTime the desired first execution time for the task
	 * (if this is in the past, the task will be executed immediately, i.e. as soon as possible)
	 * @param period the interval between successive executions of the task (in milliseconds)
	 * @return a {@link ScheduledFuture} representing pending completion of the task
	 * @throws org.springframework.core.task.TaskRejectedException if  the given task was not accepted
	 * for internal reasons (e.g. a pool overload handling policy or a pool shutdown in progress)
	 * 使用 fixedRate 的方式提交任务调度请求
	 * 任务首次启动时间由传入参数指定
	 * task　待执行的任务
	 * startTime　任务启动时间
	 * period　两次任务启动时间之间的间隔时间，默认单位是毫秒
	 */
	ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period);

	/**
	 * Schedule the given {@link Runnable}, starting as soon as possible and
	 * invoking it with the given period.
	 * <p>Execution will end once the scheduler shuts down or the returned
	 * {@link ScheduledFuture} gets cancelled.
	 * @param task the Runnable to execute whenever the trigger fires
	 * @param period the interval between successive executions of the task
	 * @return a {@link ScheduledFuture} representing pending completion of the task
	 * @throws org.springframework.core.task.TaskRejectedException if the given task was not accepted
	 * for internal reasons (e.g. a pool overload handling policy or a pool shutdown in progress)
	 * @since 5.0
	 * @see #scheduleAtFixedRate(Runnable, long)
	 */
	default ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Duration period) {
		return scheduleAtFixedRate(task, period.toMillis());
	}

	/**
	 * Schedule the given {@link Runnable}, starting as soon as possible and
	 * invoking it with the given period.
	 * <p>Execution will end once the scheduler shuts down or the returned
	 * {@link ScheduledFuture} gets cancelled.
	 * @param task the Runnable to execute whenever the trigger fires
	 * @param period the interval between successive executions of the task (in milliseconds)
	 * @return a {@link ScheduledFuture} representing pending completion of the task
	 * @throws org.springframework.core.task.TaskRejectedException if the given task was not accepted
	 * for internal reasons (e.g. a pool overload handling policy or a pool shutdown in progress)
	 * 使用 fixedRate 的方式提交任务调度请求
	 * 任务首次启动时间未设置，任务池将会尽可能早的启动任务
	 * task 待执行任务
	 * period 两次任务启动时间之间的间隔时间，默认单位是毫秒
	 */
	ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period);

	/**
	 * Schedule the given {@link Runnable}, invoking it at the specified execution time
	 * and subsequently with the given delay between the completion of one execution
	 * and the start of the next.
	 * <p>Execution will end once the scheduler shuts down or the returned
	 * {@link ScheduledFuture} gets cancelled.
	 * @param task the Runnable to execute whenever the trigger fires
	 * @param startTime the desired first execution time for the task
	 * (if this is in the past, the task will be executed immediately, i.e. as soon as possible)
	 * @param delay the delay between the completion of one execution and the start of the next
	 * @return a {@link ScheduledFuture} representing pending completion of the task
	 * @throws org.springframework.core.task.TaskRejectedException if the given task was not accepted
	 * for internal reasons (e.g. a pool overload handling policy or a pool shutdown in progress)
	 * @since 5.0
	 * @see #scheduleWithFixedDelay(Runnable, Date, long)
	 */
	default ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Instant startTime, Duration delay) {
		return scheduleWithFixedDelay(task, Date.from(startTime), delay.toMillis());
	}

	/**
	 * Schedule the given {@link Runnable}, invoking it at the specified execution time
	 * and subsequently with the given delay between the completion of one execution
	 * and the start of the next.
	 * <p>Execution will end once the scheduler shuts down or the returned
	 * {@link ScheduledFuture} gets cancelled.
	 * @param task the Runnable to execute whenever the trigger fires
	 * @param startTime the desired first execution time for the task
	 * (if this is in the past, the task will be executed immediately, i.e. as soon as possible)
	 * @param delay the delay between the completion of one execution and the start of the next
	 * (in milliseconds)
	 * @return a {@link ScheduledFuture} representing pending completion of the task
	 * @throws org.springframework.core.task.TaskRejectedException if the given task was not accepted
	 * for internal reasons (e.g. a pool overload handling policy or a pool shutdown in progress)
	 * 使用 fixedDelay 的方式提交任务调度请求
	 * 任务首次启动时间由传入参数指定
	 * task 待执行任务
	 * startTime　任务启动时间
	 * delay 上一次任务结束时间与下一次任务开始时间的间隔时间，单位默认是毫秒
	 */
	ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Date startTime, long delay);

	/**
	 * Schedule the given {@link Runnable}, starting as soon as possible and invoking it with
	 * the given delay between the completion of one execution and the start of the next.
	 * <p>Execution will end once the scheduler shuts down or the returned
	 * {@link ScheduledFuture} gets cancelled.
	 * @param task the Runnable to execute whenever the trigger fires
	 * @param delay the delay between the completion of one execution and the start of the next
	 * @return a {@link ScheduledFuture} representing pending completion of the task
	 * @throws org.springframework.core.task.TaskRejectedException if the given task was not accepted
	 * for internal reasons (e.g. a pool overload handling policy or a pool shutdown in progress)
	 * @since 5.0
	 * @see #scheduleWithFixedDelay(Runnable, long)
	 */
	default ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Duration delay) {
		return scheduleWithFixedDelay(task, delay.toMillis());
	}

	/**
	 * Schedule the given {@link Runnable}, starting as soon as possible and invoking it with
	 * the given delay between the completion of one execution and the start of the next.
	 * <p>Execution will end once the scheduler shuts down or the returned
	 * {@link ScheduledFuture} gets cancelled.
	 * @param task the Runnable to execute whenever the trigger fires
	 * @param delay the delay between the completion of one execution and the start of the next
	 * (in milliseconds)
	 * @return a {@link ScheduledFuture} representing pending completion of the task
	 * @throws org.springframework.core.task.TaskRejectedException if the given task was not accepted
	 * for internal reasons (e.g. a pool overload handling policy or a pool shutdown in progress)
	 * 使用 fixedDelay 的方式提交任务调度请求
	 * 任务首次启动时间未设置，任务池将会尽可能早的启动任务
	 * task 待执行任务
	 * startTime　任务启动时间
	 * delay 上一次任务结束时间与下一次任务开始时间的间隔时间，单位默认是毫秒
	 */
	ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long delay);

}
