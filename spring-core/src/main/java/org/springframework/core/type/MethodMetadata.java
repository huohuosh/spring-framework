/*
 * Copyright 2002-2015 the original author or authors.
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

package org.springframework.core.type;

/**
 * Interface that defines abstract access to the annotations of a specific
 * class, in a form that does not require that class to be loaded yet.
 *
 * @author Juergen Hoeller
 * @author Mark Pollack
 * @author Chris Beams
 * @author Phillip Webb
 * @since 3.0
 * @see StandardMethodMetadata
 * @see AnnotationMetadata#getAnnotatedMethods
 * @see AnnotatedTypeMetadata
 * 基本上是代理了Method introspectedMethod
 */
public interface MethodMetadata extends AnnotatedTypeMetadata {

	/**
	 * Return the name of the method.
	 * 方法名称
	 */
	String getMethodName();

	/**
	 * Return the fully-qualified name of the class that declares this method.
	 * 此方法所属类的全类名
	 */
	String getDeclaringClassName();

	/**
	 * Return the fully-qualified name of this method's declared return type.
	 * @since 4.2
	 * 方法返回值的全类名
	 */
	String getReturnTypeName();

	/**
	 * Return whether the underlying method is effectively abstract:
	 * i.e. marked as abstract on a class or declared as a regular,
	 * non-default method in an interface.
	 * @since 4.2
	 * 是否是抽象方法
	 */
	boolean isAbstract();

	/**
	 * Return whether the underlying method is declared as 'static'.
	 * 是否是静态方法
	 */
	boolean isStatic();

	/**
	 * Return whether the underlying method is marked as 'final'.
	 * 是否是final方法
	 */
	boolean isFinal();

	/**
	 * Return whether the underlying method is overridable,
	 * i.e. not marked as static, final or private.
	 * 是否可以被复写（不是静态、不是final、不是private的  就表示可以被复写）
	 */
	boolean isOverridable();

}
