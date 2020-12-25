/*
 * Copyright 2002-2012 the original author or authors.
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

package org.springframework.scheduling.config;

import org.springframework.scheduling.annotation.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * {@code NamespaceHandler} for the 'task' namespace.
 *
 * @author Mark Fisher
 * @since 3.0
 */
public class TaskNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		/**
		 * 解析 <task:annotation-driven />
		 * - 注册 {@link AsyncAnnotationBeanPostProcessor} （主要和 {@link Async}相关）
		 * @see AsyncAnnotationAdvisor
		 * @see AnnotationAsyncExecutionInterceptor
		 * @see EnableAsync
		 * - 注册 {@link ScheduledAnnotationBeanPostProcessor} （主要和 {@link Scheduled}相关）
		 * @see ScheduledTaskRegistrar
		 * @see EnableScheduling
		 */
		this.registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
		/**
		 * 解析 <task:executor/>，创建线程池
		 * @see TaskExecutorFactoryBean
		 * @see ThreadPoolTaskExecutor
		 * @see ThreadPoolExecutor
		 */
		this.registerBeanDefinitionParser("executor", new ExecutorBeanDefinitionParser());
		/**
		 * 解析 <task:scheduled-tasks/> 和 {@link Schedules} 类似
		 * @see ContextLifecycleScheduledTaskRegistrar
		 * @see Task
		 */
		this.registerBeanDefinitionParser("scheduled-tasks", new ScheduledTasksBeanDefinitionParser());
		/**
		 * 解析 <task:scheduler/> 创建 ThreadPoolTaskScheduler
		 * @see ThreadPoolTaskScheduler
		 * @see org.springframework.scheduling.TaskScheduler
		 * @see ScheduledThreadPoolExecutor
		 */
		this.registerBeanDefinitionParser("scheduler", new SchedulerBeanDefinitionParser());
	}

}
