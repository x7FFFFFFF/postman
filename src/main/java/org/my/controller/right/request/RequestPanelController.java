package org.my.controller.right.request;

import org.my.bus.MessageBusSingleton;
import org.my.bus.Subscribe;
import org.my.bus.TargetEvent;
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
public enum RequestPanelController implements PropertyChangeListener {
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


    public  static class  RequestCompletedEvent extends TargetEvent<HttpResponse> {
        public RequestCompletedEvent(HttpResponse target) {
            super(target);
        }
    }



    @SuppressWarnings("unchecked")
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println(evt);
       if  (evt.getPropertyName().equals("state")){
           SwingWorker.StateValue newValue=(SwingWorker.StateValue)evt.getNewValue();
           if (newValue==SwingWorker.StateValue.DONE){
               MessageBusSingleton.INSTANCE.get().post(RequestSourcePanel.Events.ACTIVATE_SEND_BUTTON);
               try {
                   HttpResponse response = ((SwingWorker<HttpResponse, Void>) evt.getSource()).get();
                   MessageBusSingleton.INSTANCE.get().post(new RequestCompletedEvent(response));
               } catch (Throwable e) {
                   //TODO: show error
                    StatusBarPanel.setStatusBarErr(e.getMessage());
               }

           }
       }


    }



    @Subscribe
    public  void onEvent(RequestSourcePanel.SendButtonPushedEvent event) {
            MessageBusSingleton.INSTANCE.get().post(RequestSourcePanel.Actions.DEACTIVATE_SEND_BUTTON);
            worker = workerFactory.createWorker(event.getTarget());
            worker.addPropertyChangeListener(this);
            worker.execute();
    }


}