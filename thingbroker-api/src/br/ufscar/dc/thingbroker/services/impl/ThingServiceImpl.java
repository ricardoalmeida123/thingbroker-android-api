package br.ufscar.dc.thingbroker.services.impl;

import java.util.List;
import java.util.Map;

import org.apache.http.auth.UsernamePasswordCredentials;

import br.ufscar.dc.thingbroker.config.Constants;
import br.ufscar.dc.thingbroker.interfaces.ThingBrokerRequestListener;
import br.ufscar.dc.thingbroker.model.Thing;
import br.ufscar.dc.thingbroker.services.ThingService;
import br.ufscar.dc.thingbroker.utils.Utils;

public class ThingServiceImpl implements ThingService {

	private StringBuilder serviceUrl;
	private UsernamePasswordCredentials credentials;
	private String baseUrl;
	
	public ThingServiceImpl(String domain, String port,UsernamePasswordCredentials credentials, boolean useHttps) {
		this.credentials = credentials;
		this.baseUrl = (useHttps == false) ? Constants.THING_BROKER_BASE_URL : Constants.THING_BROKER_SECURE_BASE_URL; 
		this.baseUrl = this.baseUrl.replace("{address}", domain + ":" + port);
	}
	
	@Override
	public void createThing(final int requestId,ThingBrokerRequestListener listener, Thing thing) {
	   serviceUrl = new StringBuilder().append(baseUrl).append(Constants.SERVICE_CREATE_THING);
	   new AsynchronousRequestTask(requestId, serviceUrl.toString(), "POST", Utils.generateJSON(thing), credentials, listener).execute();
	}

	@Override
	public void updateThing(final int requestId,ThingBrokerRequestListener listener, Thing thing) {
	   serviceUrl = new StringBuilder().append(baseUrl).append(Constants.SERVICE_UPDATE_THING);
	   new AsynchronousRequestTask(requestId, serviceUrl.toString(), "PUT", Utils.generateJSON(thing), credentials, listener).execute();
	}

	@Override
	public void retrieveThing(final int requestId,ThingBrokerRequestListener listener, Map<String, String> searchParams) {
		serviceUrl = new StringBuilder().append(baseUrl).append(Constants.SERVICE_RETRIEVE_THING);
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
	public void deleteThing(final int requestId,ThingBrokerRequestListener listener, Thing thing) {
       serviceUrl = new StringBuilder().append(baseUrl).append(Constants.SERVICE_REMOVE_THING).append("/").append(thing.getThingId());
       new AsynchronousRequestTask(requestId, serviceUrl.toString(), "DELETE", null, credentials, listener).execute();
	}

	@Override
	public void addThingMetadata(final int requestId,ThingBrokerRequestListener listener, Thing thing) {
       serviceUrl = new StringBuilder().append(baseUrl).append(Constants.SERVICE_ADD_THING_METADATA.replace("{thingId}", thing.getThingId()));
	   new AsynchronousRequestTask(requestId, serviceUrl.toString(), "POST", Utils.generateJSON(thing.getMetadata()), credentials, listener).execute();
	}

	@Override
	public void retrieveThingMetadata(final int requestId,ThingBrokerRequestListener listener, Thing thing) {
	   serviceUrl = new StringBuilder().append(baseUrl).append(Constants.SERVICE_RETRIEVE_THING_METADATA.replace("{thingId}", thing.getThingId()));
	   new AsynchronousRequestTask(requestId, serviceUrl.toString(), "GET", Utils.generateJSON(thing), credentials, listener).execute();
	}

	@Override
	public void followThings(final int requestId,ThingBrokerRequestListener listener, Thing thing, List<String> thingsToFollow) {
	  serviceUrl = new StringBuilder().append(baseUrl).append(Constants.SERVICE_FOLLOW_THINGS.replace("{thingId}", thing.getThingId()));
	  new AsynchronousRequestTask(requestId, serviceUrl.toString(), "POST", Utils.generateJSON(thingsToFollow), credentials, listener).execute();
	}

	@Override
	public void unFollowThings(final int requestId,ThingBrokerRequestListener listener, Thing thing, List<String> thingsToUnFollow) {
	  serviceUrl = new StringBuilder().append(baseUrl).append(Constants.SERVICE_UNFOLLOW_THINGS.replace("{thingId}", thing.getThingId()));
	  new AsynchronousRequestTask(requestId, serviceUrl.toString(), "POST", Utils.generateJSON(thingsToUnFollow), credentials, listener).execute();
	}

}
