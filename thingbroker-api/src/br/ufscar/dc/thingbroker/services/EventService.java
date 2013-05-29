package br.ufscar.dc.thingbroker.services;

import java.util.Map;

import br.ufscar.dc.thingbroker.interfaces.ThingBrokerRequestListener;
import br.ufscar.dc.thingbroker.model.Event;
import br.ufscar.dc.thingbroker.model.EventData;
import br.ufscar.dc.thingbroker.model.Thing;

public interface EventService { 
	void postEvent(final int requestId, ThingBrokerRequestListener listener, Event event, boolean keepStored);
    void postEventWithMultipartContent(final int requestId,ThingBrokerRequestListener listener, Event event, boolean keepStored, EventData [] data);
    void getThingEvents(final int requestId,ThingBrokerRequestListener listener,Thing id, Map<String,String> searchParams);
    void getThingEvent(final int requestId,ThingBrokerRequestListener listener, Event id);
    void getEventContent(final int requestId,ThingBrokerRequestListener listener, String contentId);
    void getEventContentInfo(final int requestId,ThingBrokerRequestListener listener, String contentId);
    void getEventContentsInfo(final int requestId,ThingBrokerRequestListener listener, Event id);
    void updateEvent(final int requestId,ThingBrokerRequestListener listener, Event id, EventData [] data);
    void addDataToInfoField(final int requestId, ThingBrokerRequestListener listener, Event event);
}
