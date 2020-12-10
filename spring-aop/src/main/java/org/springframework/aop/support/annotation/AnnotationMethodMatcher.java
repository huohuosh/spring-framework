/*
 * Copyright 2002-2019 the original author or authors.
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

package org.springframework.aop.support.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.StaticMethodMatcher;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;

/**
 * Simple MethodMatcher that looks for a specific Java 5 annotation
 * being present on a method (checking both the method on the invoked
 * interface, if any, and the corresponding method on the target class).
 * 通过注解判断的方法匹配器
 *
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 2.0
 * @see AnnotationMatchingPointcut
 */
public class AnnotationMethodMatcher extends StaticMethodMatcher {

	/**
	 * 注解类型
	 */
	private final Class<? extends Annotation> annotationType;
	/**
	 * 是否检查继承
	 */
	private final boolean checkInherited;


	/**
	 * Create a new AnnotationClassFilter for the given annotation type.
	 * @param annotationType the annotation type to look for
	 */
	public AnnotationMethodMatcher(Class<? extends Annotation> annotationType) {
		this(annotationType, false);
	}

	/**
	 * Create a new AnnotationClassFilter for the given annotation type.
	 * @param annotationType the annotation type to look for
	 * @param checkInherited whether to also check the superclasses and
	 * interfaces as well as meta-annotations for the annotation type
	 * (i.e. whether to use {@link AnnotatedElementUtils#hasAnnotation}
	 * semantics instead of standard Java {@link Method#isAnnotationPresent})
	 * @since 5.0
	 */
	public AnnotationMethodMatcher(Class<? extends Annotation> annotationType, boolean checkInherited) {
		Assert.notNull(annotationType, "Annotation type must not be null");
		this.annotationType = annotationType;
		this.checkInherited = checkInherited;
	}



	@Override
	public boolean matches(Method method, Class<?> targetClass) {
		// 如果方法被当前 annotationType 注解所注解，返回 true
		if (matchesMethod(method)) {
			return true;
		}
		// Proxy classes never have annotations on their redeclared methods.
		// 代理类返回 false
		if (Proxy.isProxyClass(targetClass)) {
			return false;
		}
		// The method may be on an interface, so let's check on the target class as well.
		// 如果方法是一个接口，查找实现类，判断是否匹配
		Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
		return (specificMethod != method && matchesMethod(specificMethod));
	}

	private boolean matchesMethod(Method method) {
		return (this.checkInherited ? AnnotatedElementUtils.hasAnnotation(method, this.annotationType) :
				method.isAnnotationPresent(this.annotationType));
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AnnotationMethodMatcher)) {
			return false;
		}
		AnnotationMethodMatcher otherMm = (AnnotationMethodMatcher) other;
		return (this.annotationType.equals(otherMm.annotationType) && this.checkInherited == otherMm.checkInherited);
	}

	@Override
	public int hashCode() {
		return this.annotationType.hashCode();
	}

	@Override
	public String toString() {
		return getClass().getName() + ": " + this.annotationType;
	}

}
