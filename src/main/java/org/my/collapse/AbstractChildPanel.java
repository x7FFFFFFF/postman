package org.my.collapse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by paramonov on 14.09.17.
 */
public abstract class AbstractChildPanel extends JPanel implements ICollapsible{
    protected int id;
    protected int moveSpeed;
    protected boolean collapsed;
    protected JPanel contentPane;
    protected JPanel headerPane;
    protected volatile Lock lock =new ReentrantLock();
    protected volatile  boolean movingComponents;
    protected ExecutorService pool =  Executors.newFixedThreadPool(1, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, String.format("Thread move ChildPanel [id=%d]", id));
        }
    });

    @Override
    public void draw() {
        setLayout(new BorderLayout());
        check();
        add(headerPane, BorderLayout.BEFORE_FIRST_LINE);
        add(contentPane);
        contentPane.setVisible(collapsed);

    }

    protected void check() {
        if (contentPane==null||headerPane==null){
            throw new IllegalAccessError();
        }
    }

    @Override
    public void collapse(boolean collapse) {
        this.collapsed=collapse;
        contentPane.setVisible(collapse);
    }

    @Override
    public boolean isCollapsed() {
        return collapsed;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id=id;
    }

    @Override
    public void setMouseListener(MouseListener ml) {
        this.headerPane.addMouseListener(ml);
    }

    @Override
    public void setHeaderPane(JPanel pane) {
        this.headerPane=pane;
    }

    @Override
    public void setContentPane(JPanel pane) {
        this.contentPane=pane;
    }

    @Override
    public void moveRelativeY(int dy) {
        movingComponents =true;
        pool.submit(() -> {
            try {
                if (lock.tryLock(2L, TimeUnit.SECONDS)) {
                    try {
                        final Point pHeadCurrent = headerPane.getLocation();
                        final Point pContentCurrent = contentPane.getLocation();
                        int count = Math.abs(dy)/moveSpeed;
                        while (count>=0){
                            try {
                                Thread.sleep(25);
                            } catch (InterruptedException e) {

                            }
                            pHeadCurrent.y+=moveSpeed;
                            pContentCurrent.y+=moveSpeed;
                            headerPane.setLocation(pHeadCurrent);
                            contentPane.setLocation(pContentCurrent);
                            repaint();
                            count--;
                        }
                        movingComponents =false;

                    } finally {
                        lock.unlock();
                    }
                } else {
                    // // TODO:
                }
            } catch (InterruptedException e) {
                // TODO:
            }


        });


    }

    @Override
    public void setMoveSpeed(int speed) {
        this.moveSpeed = speed;
    }


}