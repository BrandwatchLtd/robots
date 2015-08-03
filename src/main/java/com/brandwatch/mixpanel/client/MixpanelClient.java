package com.brandwatch.mixpanel.client;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mixpanel.mixpanelapi.ClientDelivery;
import com.mixpanel.mixpanelapi.MessageBuilder;
import com.mixpanel.mixpanelapi.MixpanelAPI;

import com.brandwatch.mixpanel.client.config.ClientConfig;

public class MixpanelClient implements AutoCloseable {

    private final Logger log = LoggerFactory.getLogger(MixpanelClient.class);

    private final MessageBuilder messageBuilder;
    private final MixpanelAPI mixpanelAPI;
    private final long maxQueueSize;
    private final long queueFlushInterval;

    private final BlockingQueue<JSONObject> eventQueue = new LinkedBlockingQueue<>();
    private final ScheduledExecutorService flushExecutorService;
    private ScheduledFuture<?> flushJobFuture;

    /**
     * Create a new Mixpanel client
     *
     * @param clientConfig Config to use for this client
     */
    public MixpanelClient(ClientConfig clientConfig) {
        this(clientConfig,
             new MessageBuilder(clientConfig.getProjectToken()),
             new MixpanelAPI(),
             Executors.newScheduledThreadPool(1));
    }

    MixpanelClient(ClientConfig clientConfig, MessageBuilder messageBuilder, MixpanelAPI mixpanelAPI, ScheduledExecutorService flushExecutorService) {

        this.messageBuilder = messageBuilder;
        this.mixpanelAPI = mixpanelAPI;
        this.flushExecutorService = flushExecutorService;

        this.queueFlushInterval = clientConfig.getMaxBatchTime();
        this.maxQueueSize = clientConfig.getMaxBatchSize();

        scheduleFlushTask(false);
    }

    public void event(String eventName) {
        event(null, eventName);
    }

    public void event(String eventName, Map<String, ? extends Object> properties) {
        event(null, eventName, properties);
    }

    public void event(String distinctId, String eventName) {
        JSONObject event = messageBuilder.event(distinctId, eventName, null);
        enqueueEvent(event);
    }

    public void event(String distinctId, String eventName, Map<String, ? extends Object> properties) {
        JSONObject props = new JSONObject(properties);
        JSONObject event = messageBuilder.event(distinctId, eventName, props);
        enqueueEvent(event);
    }

    public void set(String distinctId, Map<String, ? extends Object> properties) {
        JSONObject props = new JSONObject(properties);
        JSONObject event = messageBuilder.set(distinctId, props);
        enqueueEvent(event);
    }

    public void set(String distinctId, Map<String, ? extends Object> properties, Map<String, ? extends Object> modifiers) {
        JSONObject props = new JSONObject(properties);
        JSONObject mods = new JSONObject(modifiers);
        JSONObject event = messageBuilder.set(distinctId, props, mods);
        enqueueEvent(event);
    }

    public void setOnce(String distinctId, Map<String, ? extends Object> properties, Map<String, ? extends Object> modifiers) {
        JSONObject props = new JSONObject(properties);
        JSONObject mods = new JSONObject(modifiers);
        JSONObject event = messageBuilder.setOnce(distinctId, props, mods);
        enqueueEvent(event);
    }

    public void delete(String distinctId) {
        JSONObject event = messageBuilder.delete(distinctId);
        enqueueEvent(event);
    }

    public void delete(String distinctId, Map<String, ? extends Object> modifiers) {
        JSONObject mods = new JSONObject(modifiers);
        JSONObject event = messageBuilder.delete(distinctId, mods);
        enqueueEvent(event);
    }

    public void increment(String distinctId, Map<String, Long> properties) {
        JSONObject event = messageBuilder.increment(distinctId, properties);
        enqueueEvent(event);
    }

    public void increment(String distinctId, Map<String, Long> properties, Map<String, ? extends Object> modifiers) {
        JSONObject mods = new JSONObject(modifiers);
        JSONObject event = messageBuilder.increment(distinctId, properties, mods);
        enqueueEvent(event);
    }

    public void append(String distinctId, Map<String, ? extends Object> properties) {
        JSONObject props = new JSONObject(properties);
        JSONObject event = messageBuilder.append(distinctId, props);
        enqueueEvent(event);
    }

    public void append(String distinctId, Map<String, ? extends Object> properties, Map<String, ? extends Object> modifiers) {
        JSONObject props = new JSONObject(properties);
        JSONObject mods = new JSONObject(modifiers);
        JSONObject event = messageBuilder.append(distinctId, props, mods);
        enqueueEvent(event);
    }

    public void union(String distinctId, Map<String, ? extends Collection<? extends Object>> properties, Map<String, ? extends Object> modifiers) {
        Map<String, JSONArray> props = new HashMap<>();

        for (Map.Entry<String, ? extends Collection<? extends Object>> entrySet : properties.entrySet()) {
            String key = entrySet.getKey();
            Collection<? extends Object> value = entrySet.getValue();
            props.put(key, new JSONArray(value));
        }

        JSONObject mods = new JSONObject(modifiers);
        JSONObject event = messageBuilder.union(distinctId, props, mods);
        enqueueEvent(event);
    }

    public void unset(String distinctId, Collection<String> propertyNames, Map<String, ? extends Object> modifiers) {
        JSONObject mods = new JSONObject(modifiers);
        JSONObject event = messageBuilder.unset(distinctId, propertyNames, mods);
        enqueueEvent(event);
    }

    public void trackCharge(String distinctId, double amount, Map<String, ? extends Object> properties) {
        JSONObject props = new JSONObject(properties);
        JSONObject event = messageBuilder.trackCharge(distinctId, amount, props);
        enqueueEvent(event);
    }

    public void trackCharge(String distinctId, double amount, Map<String, ? extends Object> properties, Map<String, ? extends Object> modifiers) {
        JSONObject props = new JSONObject(properties);
        JSONObject mods = new JSONObject(modifiers);
        JSONObject event = messageBuilder.trackCharge(distinctId, amount, props, mods);
        enqueueEvent(event);
    }

    public void peopleMessage(String distinctId, String actionType, Map<String, ? extends Object> properties, Map<String, ? extends Object> modifiers) {
        JSONObject props = new JSONObject(properties);
        JSONObject mods = new JSONObject(modifiers);
        JSONObject event = messageBuilder.peopleMessage(distinctId, actionType, props, mods);
        enqueueEvent(event);
    }

    @Override
    public void close() throws Exception {
        flushExecutorService.shutdownNow();
        flushQueue();
    }

    void flushQueue() {
        if (eventQueue.isEmpty()) {
            return;
        }

        List<JSONObject> eventsToSend = new LinkedList<>();
        eventQueue.drainTo(eventsToSend);

        ClientDelivery delivery = new ClientDelivery();
        eventsToSend.stream().forEach(delivery::addMessage);

        try {
            mixpanelAPI.deliver(delivery);
        } catch (IOException ex) {
            log.error("Error sending events to Mixpanel", ex);
        }
    }

    private void scheduleFlushTask(boolean flushNow) {
        flushJobFuture = flushExecutorService.scheduleAtFixedRate(this::flushQueue,
                                                                  flushNow ? 0 : queueFlushInterval,
                                                                  queueFlushInterval,
                                                                  SECONDS);
    }

    private void enqueueEvent(JSONObject event) {
        try {
            eventQueue.add(event);
        } catch (IllegalStateException ex) {
            log.error("Unable to enqueu Mixpanel event", ex);
        }
        
        if (eventQueue.size() >= maxQueueSize) {
            rescheduleForImmediateFlush();
        }
    }

    private void rescheduleForImmediateFlush() {
        flushJobFuture.cancel(false);
        scheduleFlushTask(true);
    }

}
