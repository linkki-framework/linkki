/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.login;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.faktorzehn.ipm.web.security.AuthenticationProvider;
import de.faktorzehn.ipm.web.state.ApplicationConfig;

@WebServlet(urlPatterns = { "/login/*", "/logout/*" })
public class LoginServlet extends HttpServlet {

    private static final String NAME = "name";

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -7420962570636101969L;

    @Inject
    private ApplicationConfig applicationConfiguration;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(NAME, applicationConfiguration.getApplicationName());
        if (AuthenticationProvider.getCurrent() == AuthenticationProvider.IN_MEMORY) {
            req.setAttribute("username", "ortmann");
            req.setAttribute("password", "password");
        }
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

}
