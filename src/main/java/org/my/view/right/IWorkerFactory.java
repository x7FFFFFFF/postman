package org.my.view.right;

import javax.swing.*;

/**
 * Created by paramonov on 23.08.17.
 */
public interface IWorkerFactory<T,V> {

    SwingWorker<T, V> createWorker(Object param);
}