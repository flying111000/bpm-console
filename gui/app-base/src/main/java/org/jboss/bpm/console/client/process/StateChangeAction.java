/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.bpm.console.client.process;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;
import com.mvc4g.client.Controller;
import com.mvc4g.client.Event;

import org.gwt.mosaic.ui.client.MessageBox;
import org.jboss.bpm.console.client.URLBuilder;
import org.jboss.bpm.console.client.common.AbstractRESTAction;
import org.jboss.bpm.console.client.model.ProcessDefinitionRef;
import org.jboss.bpm.console.client.model.ProcessInstanceRef;
import org.jboss.bpm.console.client.util.ConsoleLog;

/**
 * Starts a new process instance.
 * Triggered through {@link org.jboss.bpm.console.client.model.ProcessInstanceRef}
 *
 * @author Heiko.Braun <heiko.braun@jboss.com>
 */
public class StateChangeAction extends AbstractRESTAction
{
  public final static String ID = StateChangeAction.class.getName();

  public String getId()
  {
    return ID;
  }

  public String getUrl(Object event)
  {
    final ProcessInstanceRef inst = (ProcessInstanceRef)event;
    if(ProcessInstanceRef.STATE.ENDED == inst.getState())
      return URLBuilder.getInstance().getInstanceEndURL(inst.getId(), inst.getEndResult());
    else
      return URLBuilder.getInstance().getStateChangeURL(inst.getId(), inst.getState());
  }

  public RequestBuilder.Method getRequestMethod()
  {
    return RequestBuilder.POST;
  }

  public void handleSuccessfulResponse(final Controller controller, final Object event, Response response)
  {
    InstanceListView view = (InstanceListView) controller.getView(InstanceListView.ID);
    ProcessDefinitionRef def = view.getCurrentDefinition();

    // force reload instance list
    controller.handleEvent( new Event(UpdateInstancesAction.ID, def));
  }
  
  protected void handleError(String url, Throwable t) {
	  
	String message = t.getMessage();
	// remove the default prefix for errors added by AbstractRESTAction
	message = message.replaceFirst("HTTP \\d*: ","");

	ConsoleLog.warn("Server reported following warning: " + message + " for url " + url);
	MessageBox.alert("Status information", message);
  }
}

