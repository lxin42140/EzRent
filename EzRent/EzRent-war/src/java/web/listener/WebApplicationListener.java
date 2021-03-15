package web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;



@WebListener

public class WebApplicationListener implements ServletContextListener, HttpSessionListener
{
    @Override
    public void contextInitialized(ServletContextEvent sce) 
    {
    }

    
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) 
    {
    }

    
    
    @Override
    public void sessionCreated(HttpSessionEvent se) 
    {
        se.getSession().setAttribute("isLogin", false);
    }

    
    
    @Override
    public void sessionDestroyed(HttpSessionEvent se)
    {
    }
}
