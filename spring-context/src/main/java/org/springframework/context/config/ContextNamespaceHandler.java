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

package org.springframework.context.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.context.annotation.*;

/**
 * {@link org.springframework.beans.factory.xml.NamespaceHandler}
 * for the '{@code context}' namespace.
 *
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @since 2.5
 */
public class ContextNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("property-placeholder", new PropertyPlaceholderBeanDefinitionParser());
		registerBeanDefinitionParser("property-override", new PropertyOverrideBeanDefinitionParser());
		/**
		 * 注册注解相关的 PostProcessor
		 * @see AnnotationConfigUtils#registerAnnotationConfigProcessors
		 * @see org.springframework.context.annotation.ConfigurationClassPostProcessor
		 * @see org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
		 * @see org.springframework.context.annotation.CommonAnnotationBeanPostProcessor
		 * @see org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor
		 * @see org.springframework.context.event.EventListenerMethodProcessor
		 * @see org.springframework.context.event.DefaultEventListenerFactory
		 */
		registerBeanDefinitionParser("annotation-config", new AnnotationConfigBeanDefinitionParser());
		/**
		 * 扫描包注册相关的 BeanDefinition
		 * 如果 annotation-config 属性为 true (默认为 true), 功能等同于 <context:annotation-config/>
		 * @see ClassPathBeanDefinitionScanner#doScan
		 * @see ComponentScan
		 * @see org.springframework.context.annotation.ComponentScanAnnotationParser
		 */
		registerBeanDefinitionParser("component-scan", new ComponentScanBeanDefinitionParser());
		registerBeanDefinitionParser("load-time-weaver", new LoadTimeWeaverBeanDefinitionParser());
		/**
		 * 注册 {@link org.springframework.beans.factory.aspectj.AnnotationBeanConfigurerAspect}
		 */
		registerBeanDefinitionParser("spring-configured", new SpringConfiguredBeanDefinitionParser());
		registerBeanDefinitionParser("mbean-export", new MBeanExportBeanDefinitionParser());
		registerBeanDefinitionParser("mbean-server", new MBeanServerBeanDefinitionParser());
	}

}
