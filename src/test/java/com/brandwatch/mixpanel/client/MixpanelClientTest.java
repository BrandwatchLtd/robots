package com.brandwatch.mixpanel.client;

import static java.util.Collections.singletonMap;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import com.mixpanel.mixpanelapi.ClientDelivery;
import com.mixpanel.mixpanelapi.MessageBuilder;
import com.mixpanel.mixpanelapi.MixpanelAPI;

import com.brandwatch.mixpanel.client.config.ClientConfig;
import com.brandwatch.mixpanel.client.config.ClientConfigBuilder;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class MixpanelClientTest {

    @Spy
    private MessageBuilder messageBuilder = new MessageBuilder(PROJECT_TOKEN);
    @Mock
    private MixpanelAPI mixpanelAPI;
    @Mock
    private ScheduledExecutorService scheduledExecutorService;
    @Mock
    private ScheduledFuture<Runnable> queueFlushFuture;

    private MixpanelClient mixpanelClient;

    private static final String PROJECT_TOKEN = "aaa-bbb-ccc-123";
    private static final String EVENT_NAME = "myEvent";
    private static final String DISTINCT_ID = "USER-987";

    private static final String STRING_KEY = "string";
    private static final String INTEGER_KEY = "integer";
    private static final String LONG_KEY = "long";
    private static final String DOUBLE_KEY = "double";
    private static final String BOOLEAN_KEY = "boolean";
    private static final String SET_KEY = "set";
    private static final String LIST_KEY = "list";

    private static final String STRING_VALUE = "stringValue";
    private static final int INTEGER_VALUE = 123;
    private static final long LONG_VALUE = 1244523429324242342L;
    private static final double DOUBLE_VALUE = 1.24352352d;
    private static final boolean BOOLEAN_VALUE = true;
    private static final String[] COLLECTION_VALUES = new String[]{"ele1", "ele2", "ele3"};

    private static final Map<String, ? extends Object> PROPERTIES = new HashMap<String, Object>() {
        {
            put(STRING_KEY, STRING_VALUE);
            put(INTEGER_KEY, INTEGER_VALUE);
            put(LONG_KEY, LONG_VALUE);
            put(DOUBLE_KEY, DOUBLE_VALUE);
            put(BOOLEAN_KEY, BOOLEAN_VALUE);
            put(SET_KEY, Sets.newSet(COLLECTION_VALUES));
            put(LIST_KEY, Arrays.asList(COLLECTION_VALUES));
        }
    };

    private final JsonObjectMatches MATCHES_PROPERTIES = new JsonObjectMatches(new JSONObject(PROPERTIES));

    private static final Map<String, String> MODIFIER = singletonMap("modifier1", "modified");

    private final JsonObjectMatches MATCHES_MODIFIER = new JsonObjectMatches(new JSONObject(MODIFIER));

    @Before
    public void createClientInstance() {
        ClientConfig clientConfig = new ClientConfigBuilder()
                .maxBatchSize(2)
                .maxBatchTimeInSeconds(10)
                .projectToken(PROJECT_TOKEN)
                .build();
        mixpanelClient = new MixpanelClient(clientConfig, messageBuilder, mixpanelAPI, scheduledExecutorService);
    }

    @Before
    public void setupExecutorService() {
        doReturn(queueFlushFuture).when(scheduledExecutorService)
                .scheduleAtFixedRate(any(Runnable.class), anyLong(), anyLong(), eq(SECONDS));
        when(queueFlushFuture.cancel(anyBoolean())).thenReturn(true);
    }

    @Test
    public void havingEventWithoutIdOrProperties_whenLoggingEvent_thenEventEnqueuedForLaterSending() throws JSONException {
        mixpanelClient.event(EVENT_NAME);

        Queue<JSONObject> clientQueue = getClientQueue();
        assertThat(clientQueue, hasSize(1));

        verify(messageBuilder).event(null, EVENT_NAME, null);
        verifyNoMoreInteractions(mixpanelAPI);
    }

    @Test
    public void havingEventWithoutId_whenLoggingEvent_thenEventEnquedForLaterSending() throws JSONException {
        mixpanelClient.event(EVENT_NAME, PROPERTIES);

        Queue<JSONObject> clientQueue = getClientQueue();
        assertThat(clientQueue, hasSize(1));

        verify(messageBuilder).event(isNull(String.class), eq(EVENT_NAME), argThat(MATCHES_PROPERTIES));
        verifyNoMoreInteractions(mixpanelAPI);
    }

    @Test
    public void havingEventWithoutProperties_whenLoggingEvent_thenEventEnquedForLaterSending() throws JSONException {
        mixpanelClient.event(DISTINCT_ID, EVENT_NAME);

        Queue<JSONObject> clientQueue = getClientQueue();

        assertThat(clientQueue, hasSize(1));

        verify(messageBuilder).event(eq(DISTINCT_ID), eq(EVENT_NAME), isNull(JSONObject.class));

        verifyNoMoreInteractions(mixpanelAPI);
    }

    @Test
    public void havingEvent_whenLoggingEvent_thenEventEnquedForLaterSending() throws JSONException {
        mixpanelClient.event(DISTINCT_ID, EVENT_NAME, PROPERTIES);

        Queue<JSONObject> clientQueue = getClientQueue();

        assertThat(clientQueue, hasSize(1));

        verify(messageBuilder).event(eq(DISTINCT_ID), eq(EVENT_NAME), argThat(MATCHES_PROPERTIES));

        verifyNoMoreInteractions(mixpanelAPI);
    }

    @Test
    public void havingSetEventWithoutModifiers_whenLoggingEvent_thenSetEventEnquedForLaterSending() throws JSONException {
        mixpanelClient.set(DISTINCT_ID, PROPERTIES);

        Queue<JSONObject> clientQueue = getClientQueue();

        assertThat(clientQueue, hasSize(1));

        verify(messageBuilder).set(eq(DISTINCT_ID), argThat(MATCHES_PROPERTIES));

        verifyNoMoreInteractions(mixpanelAPI);
    }

    @Test
    public void havingSetEvent_whenLoggingEvent_thenSetEventEnquedForLaterSending() throws JSONException {
        mixpanelClient.set(DISTINCT_ID, PROPERTIES, MODIFIER);

        Queue<JSONObject> clientQueue = getClientQueue();

        assertThat(clientQueue, hasSize(1));

        verify(messageBuilder).set(eq(DISTINCT_ID), argThat(MATCHES_PROPERTIES), argThat(MATCHES_MODIFIER));

        verifyNoMoreInteractions(mixpanelAPI);
    }

    @Test
    public void havingSetOnceEvent_whenLoggingEvent_thenSetEventEnquedForLaterSending() throws JSONException {
        mixpanelClient.setOnce(DISTINCT_ID, PROPERTIES, MODIFIER);

        Queue<JSONObject> clientQueue = getClientQueue();

        assertThat(clientQueue, hasSize(1));

        verify(messageBuilder).setOnce(eq(DISTINCT_ID), argThat(MATCHES_PROPERTIES), argThat(MATCHES_MODIFIER));

        verifyNoMoreInteractions(mixpanelAPI);
    }

    @Test
    public void havingDeleteWithoutModifiers_whenLoggingEvent_thenSetEventEnquedForLaterSending() throws JSONException {
        mixpanelClient.delete(DISTINCT_ID);

        Queue<JSONObject> clientQueue = getClientQueue();

        assertThat(clientQueue, hasSize(1));

        verify(messageBuilder).delete(eq(DISTINCT_ID));

        verifyNoMoreInteractions(mixpanelAPI);
    }

    @Test
    public void havingDelete_whenLoggingEvent_thenSetEventEnquedForLaterSending() throws JSONException {
        mixpanelClient.delete(DISTINCT_ID, MODIFIER);

        Queue<JSONObject> clientQueue = getClientQueue();

        assertThat(clientQueue, hasSize(1));

        verify(messageBuilder).delete(eq(DISTINCT_ID), argThat(MATCHES_MODIFIER));

        verifyNoMoreInteractions(mixpanelAPI);
    }

    @Test
    public void havingIncrementWithoutModifiers_whenLoggingEvent_thenSetEventEnquedForLaterSending() throws JSONException {
        Map<String, Long> incrementProps = singletonMap("value", 1L);
        mixpanelClient.increment(DISTINCT_ID, incrementProps);

        Queue<JSONObject> clientQueue = getClientQueue();

        assertThat(clientQueue, hasSize(1));

        verify(messageBuilder).increment(eq(DISTINCT_ID), eq(incrementProps));

        verifyNoMoreInteractions(mixpanelAPI);
    }

    @Test
    public void havingIncrementEvent_whenLoggingEvent_thenSetEventEnquedForLaterSending() throws JSONException {
        Map<String, Long> incrementProps = singletonMap("value", 1L);
        mixpanelClient.increment(DISTINCT_ID, incrementProps, MODIFIER);

        Queue<JSONObject> clientQueue = getClientQueue();

        assertThat(clientQueue, hasSize(1));

        verify(messageBuilder).increment(eq(DISTINCT_ID), eq(incrementProps), argThat(MATCHES_MODIFIER));

        verifyNoMoreInteractions(mixpanelAPI);
    }

    @Test
    public void havingAppendEventWithoutModifiers_whenLoggingEvent_thenSetEventEnquedForLaterSending() throws JSONException {
        mixpanelClient.append(DISTINCT_ID, PROPERTIES);

        Queue<JSONObject> clientQueue = getClientQueue();

        assertThat(clientQueue, hasSize(1));

        verify(messageBuilder).append(eq(DISTINCT_ID), argThat(MATCHES_PROPERTIES));

        verifyNoMoreInteractions(mixpanelAPI);
    }

    @Test
    public void havingAppendEvent_whenLoggingEvent_thenSetEventEnquedForLaterSending() throws JSONException {
        mixpanelClient.append(DISTINCT_ID, PROPERTIES, MODIFIER);

        Queue<JSONObject> clientQueue = getClientQueue();

        assertThat(clientQueue, hasSize(1));

        verify(messageBuilder).append(eq(DISTINCT_ID), argThat(MATCHES_PROPERTIES), argThat(MATCHES_MODIFIER));

        verifyNoMoreInteractions(mixpanelAPI);
    }

    @Test
    public void havingUnionEvent_whenLoggingEvent_thenSetEventEnquedForLaterSending() throws JSONException {
        Map<String, Collection<String>> unionProperties = new HashMap<String, Collection<String>>();

        unionProperties.put("key1", Arrays.asList(new String[]{"ele1", "ele2", "ele3"}));
        unionProperties.put("key2", Arrays.asList(new String[]{"ele4", "ele5", "ele6"}));

        mixpanelClient.union(DISTINCT_ID, unionProperties, MODIFIER);

        Queue<JSONObject> clientQueue = getClientQueue();

        assertThat(clientQueue, hasSize(1));

        verify(messageBuilder).union(eq(DISTINCT_ID), anyMapOf(String.class, JSONArray.class), argThat(MATCHES_MODIFIER));

        verifyNoMoreInteractions(mixpanelAPI);
    }

    @Test
    public void havingUnsetEvent_whenLoggingEvent_thenSetEventEnquedForLaterSending() throws JSONException {
        Collection<String> propertyNames = Arrays.asList(new String[]{"name1", "name2", "name3"});
        mixpanelClient.unset(DISTINCT_ID, propertyNames, MODIFIER);

        Queue<JSONObject> clientQueue = getClientQueue();

        assertThat(clientQueue, hasSize(1));

        verify(messageBuilder).unset(eq(DISTINCT_ID), eq(propertyNames), argThat(MATCHES_MODIFIER));

        verifyNoMoreInteractions(mixpanelAPI);
    }

    @Test
    public void havingTrackChargeEventWithoutModifiers_whenLoggingEvent_thenSetEventEnquedForLaterSending() throws JSONException {
        final double chargeAmount = 10.9d;
        mixpanelClient.trackCharge(DISTINCT_ID, chargeAmount, PROPERTIES);

        Queue<JSONObject> clientQueue = getClientQueue();

        assertThat(clientQueue, hasSize(1));

        verify(messageBuilder).trackCharge(eq(DISTINCT_ID), eq(chargeAmount), argThat(MATCHES_PROPERTIES));

        verifyNoMoreInteractions(mixpanelAPI);
    }

    @Test
    public void havingTrackChargeEvent_whenLoggingEvent_thenSetEventEnquedForLaterSending() throws JSONException {
        final double chargeAmount = 10.9d;
        mixpanelClient.trackCharge(DISTINCT_ID, chargeAmount, PROPERTIES, MODIFIER);

        Queue<JSONObject> clientQueue = getClientQueue();

        assertThat(clientQueue, hasSize(1));

        verify(messageBuilder).trackCharge(eq(DISTINCT_ID), eq(chargeAmount), argThat(MATCHES_PROPERTIES), argThat(MATCHES_MODIFIER));

        verifyNoMoreInteractions(mixpanelAPI);
    }

    @Test
    public void havingPeopleEvent_whenLoggingEvent_thenSetEventEnquedForLaterSending() throws JSONException {
        final String actionType = "myAction";
        mixpanelClient.peopleMessage(DISTINCT_ID, actionType, PROPERTIES, MODIFIER);

        Queue<JSONObject> clientQueue = getClientQueue();

        assertThat(clientQueue, hasSize(1));

        verify(messageBuilder).peopleMessage(eq(DISTINCT_ID), eq(actionType), argThat(MATCHES_PROPERTIES), argThat(MATCHES_MODIFIER));

        verifyNoMoreInteractions(mixpanelAPI);
    }

    @Test
    public void havingLargerThanAllowedQueue_whenAddingNextItem_thenImediateQueueFlushScheduled() {
        mixpanelClient.event(EVENT_NAME);
        mixpanelClient.event(EVENT_NAME);
        verify(queueFlushFuture).cancel(false);
        verify(scheduledExecutorService).scheduleAtFixedRate(any(Runnable.class), eq(0L), anyLong(), eq(SECONDS));
    }

    @Test
    public void havingEmptyQueue_whenFlushingQueue_thenNothingHappens() {
        mixpanelClient.flushQueue();
        verifyNoMoreInteractions(mixpanelAPI);
    }

    @Test
    public void havingQueueOfEvents_whenFlushingQueue_thenEventsSent() throws IOException {
        mixpanelClient.event(EVENT_NAME);
        mixpanelClient.event(EVENT_NAME);

        doNothing().when(mixpanelAPI).deliver(any(ClientDelivery.class));
        mixpanelClient.flushQueue();

        Queue<JSONObject> clientQueue = getClientQueue();

        assertThat(clientQueue, hasSize(0));

        verify(mixpanelAPI).deliver(any(ClientDelivery.class));
    }

    private Queue<JSONObject> getClientQueue() {
        Queue<JSONObject> eventQueue = (Queue<JSONObject>) Whitebox.getInternalState(mixpanelClient, "eventQueue");
        return eventQueue;
    }

    public static final class JsonObjectMatches extends ArgumentMatcher<JSONObject> {

        private final JSONObject target;

        public JsonObjectMatches(JSONObject target) {
            this.target = target;
        }

        @Override
        public boolean matches(Object argument) {
            if (argument instanceof JSONObject) {
                JSONObject jsonArgument = (JSONObject) argument;

                if (jsonArgument.length() != target.length()) {
                    return false;
                }

                for (Iterator<Object> iterator = jsonArgument.keys(); iterator.hasNext();) {
                    String key = (String) iterator.next();
                    try {
                        if (!jsonArgument.get(key).equals(target.get(key))) {
                            return false;
                        }
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } else {
                return false;
            }
            return true;
        }

    }

}
