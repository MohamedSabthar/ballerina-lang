/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.siddhi.core.query.selector;

import org.apache.log4j.Logger;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.event.ComplexEvent;
import org.wso2.siddhi.core.event.ComplexEventChunk;
import org.wso2.siddhi.core.event.GroupedComplexEvent;
import org.wso2.siddhi.core.event.state.populater.StateEventPopulator;
import org.wso2.siddhi.core.event.stream.StreamEvent;
import org.wso2.siddhi.core.exception.SiddhiAppCreationException;
import org.wso2.siddhi.core.executor.condition.ConditionExpressionExecutor;
import org.wso2.siddhi.core.query.output.ratelimit.OutputRateLimiter;
import org.wso2.siddhi.core.query.processor.Processor;
import org.wso2.siddhi.core.query.selector.attribute.processor.AttributeProcessor;
import org.wso2.siddhi.core.query.selector.attribute.processor.executor.GroupByAggregationAttributeExecutor;
import org.wso2.siddhi.core.util.SiddhiConstants;
import org.wso2.siddhi.query.api.execution.query.selection.Selector;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Processor implementation representing selector portion of the Siddhi query.
 */
public class QuerySelector implements Processor {


    private static final Logger log = Logger.getLogger(QuerySelector.class);
    private Selector selector;
    private SiddhiAppContext siddhiAppContext;
    private boolean currentOn = false;
    private boolean expiredOn = false;
    private boolean containsAggregator = false;
    private OutputRateLimiter outputRateLimiter;
    private List<AttributeProcessor> attributeProcessorList;
    private ConditionExpressionExecutor havingConditionExecutor = null;
    private boolean isGroupBy = false;
    private GroupByKeyGenerator groupByKeyGenerator;
    private boolean isOrderBy = false;
    private OrderByEventComparator orderByEventComparator;
    private String id;
    private StateEventPopulator eventPopulator;
    private boolean batchingEnabled = true;
    private long limit = SiddhiConstants.UNKNOWN_STATE;

    public QuerySelector(String id, Selector selector, boolean currentOn, boolean expiredOn, SiddhiAppContext
            siddhiAppContext) {
        this.id = id;
        this.currentOn = currentOn;
        this.expiredOn = expiredOn;
        this.selector = selector;
        this.siddhiAppContext = siddhiAppContext;
    }

    @Override
    public void process(ComplexEventChunk complexEventChunk) {

        if (log.isTraceEnabled()) {
            log.trace("event is processed by selector " + id + this);
        }
        ComplexEventChunk outputComplexEventChunk = null;
        if (complexEventChunk.isBatch() && batchingEnabled) {
            if (isGroupBy) {
                outputComplexEventChunk = processInBatchGroupBy(complexEventChunk);
            } else if (containsAggregator) {
                outputComplexEventChunk = processInBatchNoGroupBy(complexEventChunk);
            } else {
                outputComplexEventChunk = processNoGroupBy(complexEventChunk);
            }
        } else {
            if (isGroupBy) {
                outputComplexEventChunk = processGroupBy(complexEventChunk);
            } else {
                outputComplexEventChunk = processNoGroupBy(complexEventChunk);
            }
        }
        if (outputComplexEventChunk != null) {
            outputRateLimiter.process(outputComplexEventChunk);
        }

    }

    public ComplexEventChunk execute(ComplexEventChunk complexEventChunk) {

        if (log.isTraceEnabled()) {
            log.trace("event is executed by selector " + id + this);
        }
        if (complexEventChunk.isBatch() && batchingEnabled) {
            if (isGroupBy) {
                return processInBatchGroupBy(complexEventChunk);
            } else if (containsAggregator) {
                return processInBatchNoGroupBy(complexEventChunk);
            } else {
                return processNoGroupBy(complexEventChunk);
            }
        } else {
            if (isGroupBy) {
                return processGroupBy(complexEventChunk);
            } else {
                return processNoGroupBy(complexEventChunk);
            }
        }
    }

    private ComplexEventChunk processNoGroupBy(ComplexEventChunk complexEventChunk) {
        if (isOrderBy) {
            orderEventChunk(complexEventChunk);
        }
        complexEventChunk.reset();
        synchronized (this) {
            long limitCount = 0;
            while (complexEventChunk.hasNext()) {
                ComplexEvent event = complexEventChunk.next();
                switch (event.getType()) {

                    case CURRENT:
                    case EXPIRED:
                        eventPopulator.populateStateEvent(event);
                        for (AttributeProcessor attributeProcessor : attributeProcessorList) {
                            attributeProcessor.process(event);
                        }
                        if (((event.getType() != StreamEvent.Type.CURRENT || !currentOn) && (event.getType() !=
                                StreamEvent.Type.EXPIRED || !expiredOn)) || ((havingConditionExecutor != null &&
                                !havingConditionExecutor.execute(event)))) {
                            complexEventChunk.remove();
                        } else {
                            if (limit != SiddhiConstants.UNKNOWN_STATE) {
                                if (limitCount >= limit) {
                                    complexEventChunk.remove();
                                } else {
                                    limitCount++;
                                }
                            }
                        }
                        break;
                    case RESET:
                        for (AttributeProcessor attributeProcessor : attributeProcessorList) {
                            attributeProcessor.process(event);
                        }
                        break;
                    case TIMER:
                        complexEventChunk.remove();
                        break;
                }
            }
        }
        complexEventChunk.reset();
        if (complexEventChunk.hasNext()) {
            return complexEventChunk;
        }
        return null;
    }

    private ComplexEventChunk<ComplexEvent> processGroupBy(ComplexEventChunk complexEventChunk) {
        if (isOrderBy) {
            orderEventChunk(complexEventChunk);
        }
        complexEventChunk.reset();
        ComplexEventChunk<ComplexEvent> currentComplexEventChunk = new ComplexEventChunk<ComplexEvent>
                (complexEventChunk.isBatch());

        synchronized (this) {
            int limitCount = 0;
            while (complexEventChunk.hasNext()) {
                ComplexEvent event = complexEventChunk.next();
                switch (event.getType()) {

                    case CURRENT:
                    case EXPIRED:
                        eventPopulator.populateStateEvent(event);
                        String groupedByKey = groupByKeyGenerator.constructEventKey(event);
                        GroupByAggregationAttributeExecutor.getKeyThreadLocal().set(groupedByKey);

                        for (AttributeProcessor attributeProcessor : attributeProcessorList) {
                            attributeProcessor.process(event);
                        }
                        if ((event.getType() == StreamEvent.Type.CURRENT && currentOn) || (event.getType() ==
                                StreamEvent.Type.EXPIRED && expiredOn)) {
                            if (!(havingConditionExecutor != null && !havingConditionExecutor.execute(event))) {
                                complexEventChunk.remove();
                                if (limit == SiddhiConstants.UNKNOWN_STATE) {
                                    currentComplexEventChunk.add(new GroupedComplexEvent(groupedByKey, event));
                                } else {
                                    if (limitCount < limit) {
                                        currentComplexEventChunk.add(new GroupedComplexEvent(groupedByKey, event));
                                        limitCount++;
                                    }
                                }
                            }
                        }
                        GroupByAggregationAttributeExecutor.getKeyThreadLocal().remove();
                        break;
                    case TIMER:
                        break;
                    case RESET:
                        for (AttributeProcessor attributeProcessor : attributeProcessorList) {
                            attributeProcessor.process(event);
                        }
                        break;
                }
            }
        }
        currentComplexEventChunk.reset();
        if (currentComplexEventChunk.hasNext()) {
            return currentComplexEventChunk;
        }
        return null;
    }

    private ComplexEventChunk processInBatchNoGroupBy(ComplexEventChunk complexEventChunk) {
        complexEventChunk.reset();
        ComplexEvent lastEvent = null;

        synchronized (this) {
            while (complexEventChunk.hasNext()) {
                ComplexEvent event = complexEventChunk.next();
                switch (event.getType()) {
                    case CURRENT:
                    case EXPIRED:
                        eventPopulator.populateStateEvent(event);
                        for (AttributeProcessor attributeProcessor : attributeProcessorList) {
                            attributeProcessor.process(event);
                        }
                        if (!(havingConditionExecutor != null && !havingConditionExecutor.execute(event))) {
                            if ((event.getType() == StreamEvent.Type.CURRENT && currentOn) || (event.getType() ==
                                    StreamEvent.Type.EXPIRED && expiredOn)) {
                                complexEventChunk.remove();
                                lastEvent = event;
                            }
                        }
                        break;
                    case TIMER:
                        break;
                    case RESET:
                        for (AttributeProcessor attributeProcessor : attributeProcessorList) {
                            attributeProcessor.process(event);
                        }
                        break;
                }
            }
        }

        if (lastEvent != null) {
            complexEventChunk.clear();
            if (limit == SiddhiConstants.UNKNOWN_STATE || limit > 0) {
                complexEventChunk.add(lastEvent);
            }
            return complexEventChunk;
        }
        return null;
    }

    private ComplexEventChunk processInBatchGroupBy(ComplexEventChunk complexEventChunk) {
        Map<String, ComplexEvent> groupedEvents = new LinkedHashMap<String, ComplexEvent>();
        complexEventChunk.reset();

        synchronized (this) {
            while (complexEventChunk.hasNext()) {
                ComplexEvent event = complexEventChunk.next();
                switch (event.getType()) {

                    case CURRENT:
                    case EXPIRED:
                        eventPopulator.populateStateEvent(event);
                        String groupByKey = groupByKeyGenerator.constructEventKey(event);
                        GroupByAggregationAttributeExecutor.getKeyThreadLocal().set(groupByKey);

                        for (AttributeProcessor attributeProcessor : attributeProcessorList) {
                            attributeProcessor.process(event);
                        }

                        if (!(havingConditionExecutor != null && !havingConditionExecutor.execute(event))) {
                            if ((event.getType() == StreamEvent.Type.CURRENT && currentOn) || (event.getType() ==
                                    StreamEvent.Type.EXPIRED && expiredOn)) {
                                complexEventChunk.remove();
                                groupedEvents.put(groupByKey, event);
                            }
                        }
                        GroupByAggregationAttributeExecutor.getKeyThreadLocal().remove();
                        break;
                    case TIMER:
                        break;
                    case RESET:
                        for (AttributeProcessor attributeProcessor : attributeProcessorList) {
                            attributeProcessor.process(event);
                        }
                        break;
                }
            }
        }

        if (groupedEvents.size() != 0) {
            complexEventChunk.clear();
            if (isOrderBy) {
                populateOrderedEventsInBatchGroupBy(complexEventChunk, groupedEvents);
            } else {
                populateEventsInBatchGroupBy(complexEventChunk, groupedEvents);
            }
            complexEventChunk.reset();
            return complexEventChunk;
        }
        return null;
    }

    private void populateEventsInBatchGroupBy(ComplexEventChunk complexEventChunk,
                                              Map<String, ComplexEvent> groupedEvents) {
        long limitCount = 0;
        for (Map.Entry<String, ComplexEvent> groupedEventEntry : groupedEvents.entrySet()) {
            if (limit == SiddhiConstants.UNKNOWN_STATE) {
                complexEventChunk.add(new GroupedComplexEvent(groupedEventEntry.getKey(),
                        groupedEventEntry.getValue()));
            } else {
                if (limitCount < limit) {
                    complexEventChunk.add(new GroupedComplexEvent(groupedEventEntry.getKey(),
                            groupedEventEntry.getValue()));
                    limitCount++;
                } else {
                    break;
                }
            }
        }
    }

    private void populateOrderedEventsInBatchGroupBy(ComplexEventChunk complexEventChunk,
                                                     Map<String, ComplexEvent> groupedEvents) {
        List<ComplexEvent> eventList = new ArrayList<>();
        for (Map.Entry<String, ComplexEvent> complexEventEntry : groupedEvents.entrySet()) {
            eventList.add(new GroupedComplexEvent(complexEventEntry.getKey(), complexEventEntry.getValue()));
        }
        eventList.sort(orderByEventComparator);
        long limitCount = 0;
        for (ComplexEvent orderedEvent : eventList) {
            if (limit == SiddhiConstants.UNKNOWN_STATE) {
                complexEventChunk.add(orderedEvent);
            } else {
                if (limitCount < limit) {
                    complexEventChunk.add(orderedEvent);
                    limitCount++;
                } else {
                    break;
                }
            }
        }
    }

    @Override
    public Processor getNextProcessor() {
        return null;    //since there is no processors after a query selector
    }

    @Override
    public void setNextProcessor(Processor processor) {
        //this method will not be used as there is no processors after a query selector
    }

    public void setNextProcessor(OutputRateLimiter outputRateLimiter) {
        if (this.outputRateLimiter == null) {
            this.outputRateLimiter = outputRateLimiter;
        } else {
            throw new SiddhiAppCreationException("outputRateLimiter is already assigned");
        }
    }

    @Override
    public void setToLast(Processor processor) {
        if (getNextProcessor() == null) {
            this.setNextProcessor(processor);
        } else {
            getNextProcessor().setToLast(processor);
        }
    }

    @Override
    public Processor cloneProcessor(String key) {
        return null;
    }

    public List<AttributeProcessor> getAttributeProcessorList() {
        return attributeProcessorList;
    }

    public void setAttributeProcessorList(List<AttributeProcessor> attributeProcessorList, boolean containsAggregator) {
        this.attributeProcessorList = attributeProcessorList;
        this.containsAggregator = this.containsAggregator || containsAggregator;
    }

    public void setGroupByKeyGenerator(GroupByKeyGenerator groupByKeyGenerator) {
        isGroupBy = true;
        this.groupByKeyGenerator = groupByKeyGenerator;
    }

    public void setOrderByEventComparator(OrderByEventComparator orderByEventComparator) {
        isOrderBy = true;
        this.orderByEventComparator = orderByEventComparator;
    }

    public void setHavingConditionExecutor(ConditionExpressionExecutor havingConditionExecutor, boolean
            containsAggregator) {
        this.havingConditionExecutor = havingConditionExecutor;
        this.containsAggregator = this.containsAggregator || containsAggregator;
    }

    public QuerySelector clone(String key) {
        QuerySelector clonedQuerySelector = new QuerySelector(id + key, selector, currentOn, expiredOn,
                siddhiAppContext);
        List<AttributeProcessor> clonedAttributeProcessorList = new ArrayList<AttributeProcessor>();
        for (AttributeProcessor attributeProcessor : attributeProcessorList) {
            clonedAttributeProcessorList.add(attributeProcessor.cloneProcessor(key));
        }
        clonedQuerySelector.attributeProcessorList = clonedAttributeProcessorList;
        clonedQuerySelector.isGroupBy = isGroupBy;
        clonedQuerySelector.containsAggregator = containsAggregator;
        clonedQuerySelector.groupByKeyGenerator = groupByKeyGenerator;
        clonedQuerySelector.havingConditionExecutor = havingConditionExecutor;
        clonedQuerySelector.eventPopulator = eventPopulator;
        clonedQuerySelector.batchingEnabled = batchingEnabled;
        clonedQuerySelector.isOrderBy = isOrderBy;
        clonedQuerySelector.orderByEventComparator = orderByEventComparator;
        clonedQuerySelector.limit = limit;
        return clonedQuerySelector;
    }

    public void setBatchingEnabled(boolean batchingEnabled) {
        this.batchingEnabled = batchingEnabled;
    }

    public void setEventPopulator(StateEventPopulator eventPopulator) {
        this.eventPopulator = eventPopulator;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    private void orderEventChunk(ComplexEventChunk complexEventChunk) {
        complexEventChunk.reset();
        List<ComplexEvent> eventList = new ArrayList<>();
        while (complexEventChunk.hasNext()) {
            ComplexEvent event = complexEventChunk.next();
            complexEventChunk.remove();
            eventList.add(event);
        }
        complexEventChunk.clear();
        eventList.sort(orderByEventComparator);
        for (ComplexEvent complexEvent : eventList) {
            complexEventChunk.add(complexEvent);
        }
    }

}
