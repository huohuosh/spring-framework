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

package org.springframework.web.servlet.config;

import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

/**
 * {@link NamespaceHandler} for Spring MVC configuration namespace.
 *
 * @author Keith Donald
 * @author Jeremy Grelle
 * @author Sebastien Deleuze
 * @since 3.0
 */
public class MvcNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		/**
		 * 解析 <mvc:annotation-driven> 标签
		 * 注册以下 BeanDefinition
		 * HandlerMapping 请求映射
		 * - 1.{@link RequestMappingHandlerMapping}
		 * @see WebMvcConfigurationSupport#requestMappingHandlerMapping()
		 *   支持 {@link RequestMapping} 注解
		 *   是 {@link HandlerMapping} 的实现类，它会在容器启动的时候，扫描容器内的 bean,解析带有 @RequestMapping
		 *   注解的方法，并将其解析为 url 和 handlerMethod 键值对方式注册到请求映射表中
		 * - 2.{@link BeanNameUrlHandlerMapping}
		 * @see WebMvcConfigurationSupport#beanNameHandlerMapping()
		 *   将 controller 类的名字映射为请求 url
		 * HandlerAdapter 请求处理
		 * - 1.{@link RequestMappingHandlerAdapter}
		 * @see WebMvcConfigurationSupport#requestMappingHandlerAdapter()
		 * 	 处理 {@link Controller} 和 {@link RequestMapping} 注解的处理器
		 * 	 是 {@link HandlerAdapter} 的实现类，它是处理请求的适配器，就是确定调用哪个类的哪个方法，并且构造方法参数，返回值
		 * - 2.{@link HttpRequestHandlerAdapter}
		 * @see WebMvcConfigurationSupport#httpRequestHandlerAdapter()
		 *   处理继承了 HttpRequestHandler 创建的处理器
		 * - 3.{@link SimpleControllerHandlerAdapter}
		 * @see WebMvcConfigurationSupport#simpleControllerHandlerAdapter()
		 * 	 处理继承自 Controller 接口的处理器
		 * ExceptionResolver 处理异常的解析器
		 * - {@link ExceptionHandlerExceptionResolver}
		 * - {@link ResponseStatusExceptionResolver}
		 * - {@link DefaultHandlerExceptionResolver}
		 * @see WebMvcConfigurationSupport#handlerExceptionResolver()
		 */
		registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
		/**
		 * 解析 <mvc:default-servlet-handler> 标签
		 * @see DefaultServletHttpRequestHandler
		 * @see DefaultServletHandlerConfigurer
		 */
		registerBeanDefinitionParser("default-servlet-handler", new DefaultServletHandlerBeanDefinitionParser());
		/**
		 * 解析 <mvc:interceptors> 标签
		 * @see MappedInterceptor
		 */
		registerBeanDefinitionParser("interceptors", new InterceptorsBeanDefinitionParser());
		/**
		 * 解析 <mvc:resources> 标签
		 * @see SimpleUrlHandlerMapping
		 * @see ResourceHttpRequestHandler
		 * @see WebMvcConfigurationSupport#resourceHandlerMapping
		 */
		registerBeanDefinitionParser("resources", new ResourcesBeanDefinitionParser());
		/**
		 * 解析 <mvc:view-controller> 标签
		 * @see SimpleUrlHandlerMapping
		 * @see ParameterizableViewController
		 * @see ViewControllerRegistration
		 * @see WebMvcConfigurationSupport#viewControllerHandlerMapping()
		 * @see MvcNamespaceUtils#registerDefaultComponents
		 */
		registerBeanDefinitionParser("view-controller", new ViewControllerBeanDefinitionParser());
		registerBeanDefinitionParser("redirect-view-controller", new ViewControllerBeanDefinitionParser());
		registerBeanDefinitionParser("status-controller", new ViewControllerBeanDefinitionParser());
		registerBeanDefinitionParser("view-resolvers", new ViewResolversBeanDefinitionParser());
		registerBeanDefinitionParser("tiles-configurer", new TilesConfigurerBeanDefinitionParser());
		registerBeanDefinitionParser("freemarker-configurer", new FreeMarkerConfigurerBeanDefinitionParser());
		registerBeanDefinitionParser("groovy-configurer", new GroovyMarkupConfigurerBeanDefinitionParser());
		registerBeanDefinitionParser("script-template-configurer", new ScriptTemplateConfigurerBeanDefinitionParser());
		registerBeanDefinitionParser("cors", new CorsBeanDefinitionParser());
	}

}
