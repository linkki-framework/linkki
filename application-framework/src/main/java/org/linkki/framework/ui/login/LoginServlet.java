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
import org.linkki.framework.ui.LinkkiStyles;

@WebServlet(urlPatterns = { "/login/*", "/logout/*" })
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = -7420962570636101969L;

    private static final String STYLESHEET_PARAM = "stylesheet"; //$NON-NLS-1$

    private static final String ICON_PARAM = "icon"; //$NON-NLS-1$

    private static final String LOCALE_PARAM = "locale"; //$NON-NLS-1$

    private static final String NAME_PARAM = "name"; //$NON-NLS-1$

    private static final String THEME_PARAM = "themename"; //$NON-NLS-1$

    @Inject
    private ApplicationConfig applicationConfiguration;


    @Override
    public void doGet(@Nullable HttpServletRequest req, @Nullable HttpServletResponse resp)
            throws ServletException, IOException {

        if (req != null) {
            req.setAttribute(THEME_PARAM, LinkkiStyles.THEME_NAME);
            req.setAttribute(ICON_PARAM, LinkkiStyles.ICON);
            req.setAttribute(STYLESHEET_PARAM, LinkkiStyles.STYLESHEET);
            req.setAttribute(NAME_PARAM, applicationConfiguration.getApplicationName());
            req.setAttribute(LOCALE_PARAM, req.getLocale().toString());
            req.getRequestDispatcher("/login.jsp").forward(req, resp); //$NON-NLS-1$
        }
    }


}
