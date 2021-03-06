/*
 * Copyright (c)  2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.siddhi.core.util.transport;

import org.apache.log4j.Logger;
import org.wso2.siddhi.core.exception.ConnectionUnavailableException;
import org.wso2.siddhi.core.exception.ExecutionPlanCreationException;
import org.wso2.siddhi.core.exception.OutputTransportException;
import org.wso2.siddhi.core.exception.TestConnectionNotSupportedException;
import org.wso2.siddhi.core.publisher.MessageType;
import org.wso2.siddhi.core.publisher.OutputTransport;
import org.wso2.siddhi.query.api.execution.io.Transport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * InMemoryOutputTransport will be used to publish events internally.
 * To use with testing, manually register this extension to {@link org.wso2.siddhi.core.SiddhiManager}
 * siddhiManager.setExtension("outputtransport:inMemory", InMemoryOutputTransport.class);
 */
public class InMemoryOutputTransport extends OutputTransport {
    private static final Logger log = Logger.getLogger(InMemoryOutputTransport.class);
    private static final String TOPIC_KEY = "topic";

    @Override
    public void init(Transport transportOptions, Map<String, String> unmappedDynamicOptions)
            throws OutputTransportException {
        List<String> availableConfigs = new ArrayList<>();
        availableConfigs.addAll(transportOptions.getOptions().keySet());
        availableConfigs.addAll(unmappedDynamicOptions.keySet());
        if (!availableConfigs.contains(TOPIC_KEY)) {
            throw new ExecutionPlanCreationException(String.format("{{%s}} configuration " +
                    "could not be found in provided configs.", TOPIC_KEY));
        }
    }

    @Override
    public void testConnect() throws TestConnectionNotSupportedException, ConnectionUnavailableException {
        // do nothing
    }

    @Override
    public void connect() throws ConnectionUnavailableException {
        // do nothing
    }

    @Override
    public void publish(Object event, Map<String, String> dynamicTransportOptions)
            throws ConnectionUnavailableException {
        InMemoryBroker.publish(dynamicTransportOptions.get(TOPIC_KEY), event);
    }

    @Override
    public void disconnect() {
        // do nothing
    }

    @Override
    public void destroy() {
        // do nothing
    }

    @Override
    public boolean isPolled() {
        return false;
    }

    @Override
    public List<String> getSupportedMessageFormats() {
        return new ArrayList<String>() {{
            add(MessageType.TEXT);
            add(MessageType.JSON);
        }};
    }
}