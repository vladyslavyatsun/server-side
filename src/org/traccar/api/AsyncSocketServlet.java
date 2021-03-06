/*
 * Copyright 2015 - 2016 Anton Tananaev (anton.tananaev@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar.api;

import org.eclipse.jetty.websocket.servlet.*;
import org.traccar.Context;
import org.traccar.api.resource.SessionResource;

import javax.servlet.http.HttpSession;

public class AsyncSocketServlet extends WebSocketServlet {

    private static final long ASYNC_TIMEOUT = 10 * 60 * 1000;

    @Override
    public void configure(WebSocketServletFactory factory) {

        factory.getPolicy().setIdleTimeout(ASYNC_TIMEOUT);//Context.getConfig().getLong("web.timeout", ASYNC_TIMEOUT));
        factory.setCreator(new WebSocketCreator() {
            @Override
            public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
                //Отримуємо сесію з Context, для роботи сокетів
                HttpSession session = Context.getHttpSession();
                if (session != null) {
                    //long userId = (Long) req.getSession().getAttribute(SessionResource.USER_ID_KEY);
                    long userId = (Long) session.getAttribute(SessionResource.USER_ID_KEY);
                    return new AsyncSocket(userId);
                } else {
                    return null;
                }

            }
        });
    }

}
