package listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Web application lifecycle listener.
 *
 * @author rbastide
 */
public class CountingUsers implements HttpSessionListener, ServletContextListener {

	// Appelée automatiquement quand une session est créée
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		se.getSession().getServletContext().log("Creating session");
		// On incrémente le nombre d'utilisateurs
		int connected = (Integer) se.getSession().getServletContext().getAttribute("numberConnected");
		connected++;
		// On stocke ce nombre dans le contexte d'application
		se.getSession().getServletContext().setAttribute("numberConnected", connected);

	}

	// Appelée automatiquement quand une session est détruite
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		se.getSession().getServletContext().log("Destroying session");
		// On décrémente le nombre d'utilisateurs
		int connected = (Integer) se.getSession().getServletContext().getAttribute("numberConnected");
		connected--;
		// On stocke ce nombre dans le contexte d'application
		se.getSession().getServletContext().setAttribute("numberConnected", connected);
	}

	// Appelée automatiquement quand l'application est démarrée
	@Override
	public void contextInitialized(ServletContextEvent se) {
		// On initialise le nombre d'utilisateurs connectés dans le contexte d'application
		se.getServletContext().setAttribute("numberConnected", 0);
	}

	// Appelée automatiquement quand l'application est arrétée
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
}
