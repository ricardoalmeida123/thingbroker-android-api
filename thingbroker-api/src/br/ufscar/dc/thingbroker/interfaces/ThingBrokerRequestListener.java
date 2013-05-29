package br.ufscar.dc.thingbroker.interfaces;

public interface ThingBrokerRequestListener 
{
    public void executeAfterRequest(final int serviceId, Object result, Exception ex);
}
