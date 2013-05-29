package br.ufscar.dc.thingbroker.services.impl;

import java.util.Map;

import org.apache.http.auth.UsernamePasswordCredentials;

import br.ufscar.dc.thingbroker.config.Constants;
import br.ufscar.dc.thingbroker.interfaces.ThingBrokerRequestListener;
import br.ufscar.dc.thingbroker.model.Event;
import br.ufscar.dc.thingbroker.model.EventData;
import br.ufscar.dc.thingbroker.model.Thing;
import br.ufscar.dc.thingbroker.services.EventService;
import br.ufscar.dc.thingbroker.utils.Utils;

public class EventServiceImpl implements EventService {

	private StringBuilder serviceUrl;
	private UsernamePasswordCredentials credentials;
	private String baseUrl;
	
	public EventServiceImpl(String domain, String port,UsernamePasswordCredentials credentials, boolean useHttps) {
		this.credentials = credentials;
		this.baseUrl = (useHttps == false) ? Constants.THING_BROKER_BASE_URL : Constants.THING_BROKER_SECURE_BASE_URL; 
		this.baseUrl = this.baseUrl.replace("{address}", domain + ":" + port);
	} 
	
	@Override
	public void postEvent(final int requestId,ThingBrokerRequestListener listener, Event event, boolean keepStored) {
	  serviceUrl = new StringBuilder().append(baseUrl).append(Constants.SERVICE_POST_EVENT.replace("{thingId}", event.getThingId())).append("?").append("keep-stored=").append(keepStored);
	  new AsynchronousRequestTask(requestId, serviceUrl.toString(), "POST", Utils.generateJSON(event.getInfo()), credentials, listener).execute();
	}

	@Override
	public void postEventWithMultipartContent(final int requestId,ThingBrokerRequestListener listener, Event event, boolean keepStored, EventData [] data) {
	  serviceUrl = new StringBuilder().append(baseUrl).append(Constants.SERVICE_POST_EVENT.replace("{thingId}", event.getThingId())).append("?").append("keep-stored=").append(keepStored);
      new AsynchronousRequestTask(requestId, serviceUrl.toString(), "POST", data, credentials, listener).execute();
	}

	@Override
	public void getThingEvents(final int requestId,ThingBrokerRequestListener listener,Thing id, Map<String, String> searchParams) {
	  serviceUrl = new StringBuilder().append(baseUrl).append(Constants.SERVICE_RETRIEVE_EVENTS.replace("{thingId}", id.getThingId()));
	  if(searchParams != null && searchParams.size() > 0) {
			serviceUrl.append("?");
			for(String key : searchParams.keySet()) {
				serviceUrl.append(key).append("=").append(searchParams.get(key)).append("&");
			}
			serviceUrl = serviceUrl.deleteCharAt(serviceUrl.toString().length() -1);
	  }
	  new AsynchronousRequestTask(requestId, serviceUrl.toString(), "GET", null, credentials, listener).execute();
	}

	@Override
	public void getThingEvent(final int requestId,ThingBrokerRequestListener listener, Event id) {
		serviceUrl = new StringBuilder().append(baseUrl).append(Constants.SERVICE_RETRIEVE_EVENT.replace("{eventId}", id.getEventId()));
	    new AsynchronousRequestTask(requestId, serviceUrl.toString(), "GET", null, credentials, listener).execute();
	}

	@Override
	public void getEventContent(final int requestId,ThingBrokerRequestListener listener, String contentId) {
		serviceUrl = new StringBuilder().append(baseUrl).append(Constants.SERVICE_RETRIEVE_EVENT_CONTENT.replace("{contentId}",contentId));
	    new AsynchronousRequestTask(requestId, serviceUrl.toString(), "GET", null, credentials, listener).execute();
	}

	@Override
	public void updateEvent(final int requestId,ThingBrokerRequestListener listener, Event id, EventData[] data) {
		serviceUrl = new StringBuilder().append(baseUrl).append(Constants.SERVICE_UPDATE_EVENT.replace("{eventId}",id.getEventId())).append("?").append("serverTimestamp=").append(id.getServerTimestamp());
		if(data != null) {
			//TODO: implement multipart update
		}
		else {
		  new AsynchronousRequestTask(requestId, serviceUrl.toString(), "POST", Utils.generateJSON(id.getInfo()), credentials, listener).execute();	
		}
	}

	@Override
	public void getEventContentInfo(int requestId,ThingBrokerRequestListener listener, String contentId) {
		serviceUrl = new StringBuilder().append(baseUrl).append(Constants.SERVICE_RETRIEVE_EVENT_CONTENT_INFO.replace("{contentId}",contentId));
	    new AsynchronousRequestTask(requestId, serviceUrl.toString(), "GET", null, credentials, listener).execute();
	}

	@Override
	public void getEventContentsInfo(int requestId,ThingBrokerRequestListener listener, Event id) {
		serviceUrl = new StringBuilder().append(baseUrl).append(Constants.SERVICE_RETRIEVE_EVENT_CONTENTS_INFO.replace("{eventId}",id.getEventId()));
	    new AsynchronousRequestTask(requestId, serviceUrl.toString(), "GET", null, credentials, listener).execute();
	}

	@Override
	public void addDataToInfoField(int requestId,ThingBrokerRequestListener listener, Event event) {
		serviceUrl = new StringBuilder().append(baseUrl).append(Constants.SERVICE_ADD_DATA_TO_INFO_FIELD.replace("{eventId}",event.getEventId())).append("?").append("serverTimestamp=").append(event.getServerTimestamp());
	    new AsynchronousRequestTask(requestId, serviceUrl.toString(), "PUT", Utils.generateJSON(event.getInfo()), credentials, listener).execute();
	}
}
