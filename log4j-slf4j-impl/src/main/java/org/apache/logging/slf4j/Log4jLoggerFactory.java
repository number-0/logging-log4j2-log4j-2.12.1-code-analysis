/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.logging.slf4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.spi.AbstractLoggerAdapter;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.util.StackLocatorUtil;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

/**
 * Log4j implementation of SLF4J ILoggerFactory interface.
 */
public class Log4jLoggerFactory extends AbstractLoggerAdapter<Logger> implements ILoggerFactory {

    private static final String FQCN = Log4jLoggerFactory.class.getName();
    private static final String PACKAGE = "org.slf4j";
    private static final String TO_SLF4J_CONTEXT = "org.apache.logging.slf4j.SLF4JLoggerContext";

    @Override
    protected Logger newLogger(final String name, final LoggerContext context) {
        final String key = Logger.ROOT_LOGGER_NAME.equals(name) ? LogManager.ROOT_LOGGER_NAME : name;

        //getLogger(key)：实例化Logger
        //使用Adaptor Log4jLogger包装Logger
        return new Log4jLogger(validateContext(context).getLogger(key), name);
    }

    @Override
    protected LoggerContext getContext() {
        //获取调用的类：org.slf4j.LoggerFactory
        final Class<?> anchor = StackLocatorUtil.getCallerClass(FQCN, PACKAGE);
        return anchor == null ? LogManager.getContext() : getContext(StackLocatorUtil.getCallerClass(anchor));
    }
    private LoggerContext validateContext(final LoggerContext context) {
        if (TO_SLF4J_CONTEXT.equals(context.getClass().getName())) {
            throw new LoggingException("log4j-slf4j-impl cannot be present with log4j-to-slf4j");
        }
        return context;
    }
}
