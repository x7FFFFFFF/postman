package org.my.controller.right.request;

import org.my.bus.IBusEventListener;
import org.my.bus.Message;
import org.my.bus.MessageBus;
import org.my.http.client.HttpRequest;
import org.my.http.client.HttpResponse;
import org.my.http.client.SimpleHttpClient;
import org.my.view.StatusBarPanel;
import org.my.view.right.IWorkerFactory;
import org.my.view.right.request.RequestSourcePanel;

import javax.swing.SwingWorker;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 * Created by paramonov on 18.08.17.
 */
public enum RequestPanelController implements PropertyChangeListener, IBusEventListener {
    INSTANCE( param -> new  SendRequestWorker((HttpRequest) param));
    private final IWorkerFactory<HttpResponse,Void> workerFactory;



    RequestPanelController(IWorkerFactory<HttpResponse,Void> workerFactory) {
        this.workerFactory = workerFactory;

    }

    public static class SendRequestWorker extends SwingWorker<HttpResponse, Void> {
        private final HttpRequest request;

        public SendRequestWorker(HttpRequest request)  {
            this.request = request;
        }

        @Override
        protected HttpResponse doInBackground() throws Exception {
            SimpleHttpClient client = new SimpleHttpClient();
            return client.doRequest(request);
        }


    }

    @SuppressWarnings("unused")
    public SwingWorker<HttpResponse, Void> getWorker() {
        return worker;
    }

    private  SwingWorker<HttpResponse, Void> worker;

    public  enum  Actions {
        REQUEST_COMPLETED
    }



    @SuppressWarnings("unchecked")
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println(evt);
       if  (evt.getPropertyName().equals("state")){
           SwingWorker.StateValue newValue=(SwingWorker.StateValue)evt.getNewValue();
           if (newValue==SwingWorker.StateValue.DONE){
               MessageBus.INSTANCE.sentMessage(RequestSourcePanel.Actions.ACTIVATE_SEND_BUTTON);
               try {
                   HttpResponse response = ((SwingWorker<HttpResponse, Void>) evt.getSource()).get();
                   MessageBus.INSTANCE.sentMessage(Actions.REQUEST_COMPLETED, response);
               } catch (Throwable e) {
                   //TODO: show error
                    StatusBarPanel.setStatusBarErr(e.getMessage());
               }

           }
       }


    }



    @Override
    public  void onEvent(Message message) {
        if (message.getId()== RequestSourcePanel.Actions.SEND_BUTTON_PUSHED){
            MessageBus.INSTANCE.sentMessage(RequestSourcePanel.Actions.DEACTIVATE_SEND_BUTTON);
            HttpRequest httpRequest =  message.getSource();
            worker = workerFactory.createWorker(httpRequest);
            worker.addPropertyChangeListener(this);
            worker.execute();
        }
    }


}