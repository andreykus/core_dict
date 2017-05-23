package com.bivgroup.common.orm.cash.kv;

import com.google.common.collect.ImmutableMap;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by bush on 19.08.2016.
 * not use
 */
public class AbstractCash<K, V> {
    public static abstract interface Listener<K, V> {
        public abstract void notify(Map<K, V> paramMap);
    }

    protected static abstract interface CallbackConsumer<V> {
        public abstract void consume(BigInteger paramBigInteger, CallBackResponse<V> paramResponseCallback);
    }

    static enum State {
        latent, starting, started, stopped;

        private State() {
        }
    }


    private final AtomicReference<BigInteger> latestIndex = new AtomicReference(null);
    private final AtomicReference<ImmutableMap<K, V>> lastResponse = new AtomicReference(ImmutableMap.of());
    private final AtomicReference<State> state = new AtomicReference(State.latent);

    private final CountDownLatch initLatch = new CountDownLatch(1);
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final CopyOnWriteArrayList<Listener<K, V>> listeners = new CopyOnWriteArrayList();

    private final CallbackConsumer<V> callBackConsumer;
    private final CallBackResponse<V> responseCallback;

    AbstractCash(CallbackConsumer<V> callbackConsumer) {
        this(callbackConsumer, 10L, TimeUnit.SECONDS);
    }

    AbstractCash(CallbackConsumer<V> callbackConsumer, final long backoffDelayQty, final TimeUnit backoffDelayUnit) {

        this.callBackConsumer = callbackConsumer;


        this.responseCallback = new CallBackResponse() {
            public void onComplete(Response paramResponse) {

                if (!AbstractCash.this.isRunning()) {
                    return;
                }
                // AbstractCash.this.updateIndex(response);

                ImmutableMap<K, V> full = null;//AbstractCash.this.convertToMap(response);

                boolean changed = !full.equals(AbstractCash.this.lastResponse.get());
                if (changed) {
                    AbstractCash.this.lastResponse.set(full);
                }
                if (changed) {
                    for (AbstractCash.Listener<K, V> l : AbstractCash.this.listeners) {
                        l.notify(full);
                    }
                }
                if (AbstractCash.this.state.compareAndSet(AbstractCash.State.starting, AbstractCash.State.started)) {
                    AbstractCash.this.initLatch.countDown();
                }

                AbstractCash.this.runCallback();
            }


            public void onFailure(Throwable throwable) {
                if (!AbstractCash.this.isRunning()) {
                    return;
                }

                AbstractCash.this.executorService.schedule(new Runnable() {
                    public void run() {
                        AbstractCash.this.runCallback();
                    }
                }, backoffDelayQty, backoffDelayUnit);
            }
        };
    }

    private static void checkState(boolean expression, String errorMessageTemplate) {
        if (!expression) {
            throw new IllegalStateException(errorMessageTemplate);
        }
    }

    public void start()
            throws Exception {
        checkState(this.state.compareAndSet(State.latent, State.starting), "Cannot transition from state %s to %s");
        runCallback();
    }

    public void stop()
            throws Exception {
        State previous = (State) this.state.getAndSet(State.stopped);
        if (previous != State.stopped) {
            this.executorService.shutdownNow();
        }
    }

    private void runCallback() {
        if (isRunning()) {
            this.callBackConsumer.consume((BigInteger) this.latestIndex.get(), this.responseCallback);
        }
    }

    private boolean isRunning() {
        return (this.state.get() == State.started) || (this.state.get() == State.starting);
    }

    public boolean awaitInitialized(long timeout, TimeUnit unit)
            throws InterruptedException {
        return this.initLatch.await(timeout, unit);
    }

    public ImmutableMap<K, V> getMap() {
        return (ImmutableMap) this.lastResponse.get();
    }

//
//        ImmutableMap<K, V> convertToMap(Response<List<V>> response)
//        {
//            if ((response == null)  || (((List)response.getResponse()).isEmpty())) {
//                return ImmutableMap.of();
//            }
//            ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
//            Set<K> keySet = new HashSet();
//            for (V v : (List)response)
//            {
//                K key = this.keyConversion.apply(v);
//                if (key != null) {
//                    if (!keySet.contains(key))
//                    {
//                        builder.put(key, v);
//                    }
//                    else
//                    {
//                        System.out.println(key.toString());
//                    }
//                }
//                keySet.add(key);
//            }
//            return builder.build();
//        }


    private void updateIndex(Response<List<V>> response) {
        if ((response != null)) {
            //   this.latestIndex.set(response.getIndex());
        }
    }

    public boolean addListener(Listener<K, V> listener) {
        boolean added = this.listeners.add(listener);
        if (this.state.get() == State.started) {
            listener.notify((Map) this.lastResponse.get());
        }
        return added;
    }

    public List<Listener<K, V>> getListeners() {
        return Collections.unmodifiableList(this.listeners);
    }

    public boolean removeListener(Listener<K, V> listener) {
        return this.listeners.remove(listener);
    }

    protected State getState() {
        return (State) this.state.get();
    }
}


