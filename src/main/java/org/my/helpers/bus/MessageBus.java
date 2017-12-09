package org.my.helpers.bus;

import javax.swing.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: BOF VIP-Service
 *
 * $Id: MessageBus.java 2017-11-22 11:22 paramonov $
 *****************************************************************/

public enum MessageBus {
    INSTANCE(SwingUtilities::invokeLater, 1);

    private final BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
    private final  CopyOnWriteArrayList<IBusEventListener> listeners = new CopyOnWriteArrayList<>();
    private final Consumer<Runnable> runnableConsumer;
    private final ExecutorService service;
    private final AtomicInteger threadCount = new AtomicInteger(0);



    MessageBus( Consumer<Runnable> runnableConsumer, int numberOfCostumerThreads) {

        this.runnableConsumer = runnableConsumer;
        this.service = Executors.newFixedThreadPool(numberOfCostumerThreads, (r)->{
            final Thread thread = new Thread(r);
            thread.setName("IBusWorker"+threadCount.getAndIncrement());
            return thread;
        });

        IntStream.rangeClosed(1,numberOfCostumerThreads).forEach(i->
            this.service.submit(new ConsumerThread())
        );

    }
    class ConsumerThread implements  Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    consume(queue.take());
                }
            } catch (InterruptedException ex) {
                //TODO: handle
                throw new RuntimeException(ex);
            }
        }
        private void consume(Message msg) {
                runnableConsumer.accept(()->{
                    for (IBusEventListener listener : listeners) {
                        listener.onEvent(msg);
                    }
                });
        }
    }





    public void sentMessage(Message msg) throws InterruptedException {
        queue.put(msg);
    }
    public void sentMessage(Enum id, Object src) {
        try {
            queue.put(new Message(id,src));
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
            //TODO: handle
        }
    }
    public void sentMessage(Enum id) {
        try {
            queue.put(new Message(id,new Object()));
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
            //TODO: handle
        }
    }


    public void publishListener(IBusEventListener listener){
        listeners.add( listener);
    }

    public void unpublishListener(IBusEventListener listener){
        listeners.remove(listener);
    }

}