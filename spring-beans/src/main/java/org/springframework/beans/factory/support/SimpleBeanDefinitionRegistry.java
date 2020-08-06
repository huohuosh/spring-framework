/*
 * Copyright 2002-2018 the original author or authors.
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

package org.springframework.beans.factory.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.SimpleAliasRegistry;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Simple implementation of the {@link BeanDefinitionRegistry} interface.
 * Provides registry capabilities only, with no factory capabilities built in.
 * Can for example be used for testing bean definition readers.
 * SimpleBeanDefinitionRegistry 为BeanDefinitionRegistry的默认实现，
 * 同时继承AliasRegistry的默认实现SimpleAliasRegistry，
 * 所以它总共维护了两个注册表aliasMap（别名注册表）与beanDefinitionMap（bean描述注册表）
 *
 * @author Juergen Hoeller
 * @since 2.5.2
 */
public class SimpleBeanDefinitionRegistry extends SimpleAliasRegistry implements BeanDefinitionRegistry {

	/** Map of bean definition objects, keyed by bean name. */
	// name-BeanDefinition 映射关系表，ConcurrentHashMap实现的内存注册表，用于存储BeanDefinition
	private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(64);

	/**
	 * 注册表中注册 name-BeanDefinition
	 * @param beanName the name of the bean instance to register
	 * @param beanDefinition definition of the bean instance to register
	 * @throws BeanDefinitionStoreException
	 */
	@Override
	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
			throws BeanDefinitionStoreException {

		Assert.hasText(beanName, "'beanName' must not be empty");
		Assert.notNull(beanDefinition, "BeanDefinition must not be null");
		this.beanDefinitionMap.put(beanName, beanDefinition);
	}

	/**
	 * 移除注册表中的name-BeanDefinition
	 * @param beanName the name of the bean instance to register
	 * @throws NoSuchBeanDefinitionException
	 */
	@Override
	public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
		if (this.beanDefinitionMap.remove(beanName) == null) {
			throw new NoSuchBeanDefinitionException(beanName);
		}
	}

	/**
	 * 获取注册表中的name-BeanDefinition
	 * @param beanName name of the bean to find a definition for
	 * @return
	 * @throws NoSuchBeanDefinitionException
	 */
	@Override
	public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
		BeanDefinition bd = this.beanDefinitionMap.get(beanName);
		if (bd == null) {
			throw new NoSuchBeanDefinitionException(beanName);
		}
		return bd;
	}

	/**
	 * 判断注册表中是否包含beanName的key
	 * @param beanName the name of the bean to look for
	 * @return
	 */
	@Override
	public boolean containsBeanDefinition(String beanName) {
		return this.beanDefinitionMap.containsKey(beanName);
	}

	/**
	 * 获取注册表beanName集合
	 * @return
	 */
	@Override
	public String[] getBeanDefinitionNames() {
		return StringUtils.toStringArray(this.beanDefinitionMap.keySet());
	}

	/**
	 * 获取注册表大小
	 * @return
	 */
	@Override
	public int getBeanDefinitionCount() {
		return this.beanDefinitionMap.size();
	}

	/**
	 * 判断beanName是否被使用，在BeanDefinition注册表中获取别名注册表中
	 * @param beanName the name to check
	 * @return
	 */
	@Override
	public boolean isBeanNameInUse(String beanName) {
		return isAlias(beanName) || containsBeanDefinition(beanName);
	}

}
