package br.ufscar.dc.thingbroker.services;

import java.util.List;
import java.util.Map;

import br.ufscar.dc.thingbroker.interfaces.ThingBrokerRequestListener;
import br.ufscar.dc.thingbroker.model.Thing;

public interface ThingService {
    void createThing(final int requestId,ThingBrokerRequestListener listener, Thing thing);
    void updateThing(final int requestId,ThingBrokerRequestListener listener, Thing thing);
    void retrieveThing(final int requestId,ThingBrokerRequestListener listener, Map<String,String> searchParams);
    void deleteThing(final int requestId,ThingBrokerRequestListener listener, Thing thing);
    void addThingMetadata(final int requestId,ThingBrokerRequestListener listener, Thing thing);
    void retrieveThingMetadata(final int requestId,ThingBrokerRequestListener listener, Thing thing);
    void followThings(final int requestId,ThingBrokerRequestListener listener, Thing thing, List<String> thingsToFollow);
    void unFollowThings(final int requestId,ThingBrokerRequestListener listener, Thing thing, List<String> thingsToUnFollow);
}
