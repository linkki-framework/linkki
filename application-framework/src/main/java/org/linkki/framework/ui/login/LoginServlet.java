/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.login;

import java.io.IOException;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.linkki.framework.state.ApplicationConfig;

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
    public void doGet(@Nullable HttpServletRequest req, @Nullable HttpServletResponse resp)
            throws ServletException, IOException {
        if (req != null) {
            req.setAttribute(NAME, applicationConfiguration.getApplicationName());
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

}
