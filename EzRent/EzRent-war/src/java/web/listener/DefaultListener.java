/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Web application lifecycle listener.
 *
 * @author Li Xin
 */
@WebListener

public class DefaultListener implements ServletContextListener, HttpSessionListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("********** DefaultListener.contextInitialized()");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("********** DefaultListener.contextDestroyed()");
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("********** DefaultListener.sessionCreated()");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("********** DefaultListener.sessionDestroyed()");
    }
}
