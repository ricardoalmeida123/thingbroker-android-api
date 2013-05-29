package br.ufscar.dc.thingbroker.config;

public class Constants {
	
	 public static String THING_BROKER_BASE_URL = "http://{address}/thingbroker/";
	 public static String THING_BROKER_SECURE_BASE_URL = "https://{address}/thingbroker/";
	 public static boolean USE_SECURE_ACCESS = false;
	 public static boolean DEBUG_MODE = false;
	 
	 //------------------------------ Thing Broker Service Paths -----------------------------
	 public static final String SERVICE_CREATE_THING = "things";
	 public static final String SERVICE_UPDATE_THING = "things";
	 public static final String SERVICE_RETRIEVE_THING = "things";
	 public static final String SERVICE_REMOVE_THING = "things";
	 public static final String SERVICE_ADD_THING_METADATA = "things/{thingId}/metadata";
	 public static final String SERVICE_RETRIEVE_THING_METADATA = "things/{thingId}/metadata";
	 public static final String SERVICE_FOLLOW_THINGS = "things/{thingId}/follow";
	 public static final String SERVICE_UNFOLLOW_THINGS = "things/{thingId}/unfollow";
	 public static final String SERVICE_POST_EVENT = "things/{thingId}/events";
	 public static final String SERVICE_RETRIEVE_EVENTS = "things/{thingId}/events";
	 public static final String SERVICE_RETRIEVE_EVENT = "events/event/{eventId}";
	 public static final String SERVICE_RETRIEVE_EVENT_CONTENT = "events/event/content/{contentId}";
	 public static final String SERVICE_RETRIEVE_EVENT_CONTENT_INFO = "events/event/content-info/{contentId}";
	 public static final String SERVICE_RETRIEVE_EVENT_CONTENTS_INFO = "events/event/{eventId}/contents-description";
	 public static final String SERVICE_UPDATE_EVENT = "events/event/{eventId}";
	 public static final String SERVICE_ADD_DATA_TO_INFO_FIELD = "events/event/{eventId}";
	 
	 
	 //------------------------------ Thing Broker Services ----------------------------------
     public static final int SERVICE_ID_CREATE_THING = 0;
     public static final int SERVICE_ID_UPDATE_THING = 1;
     public static final int SERVICE_ID_RETRIEVE_THING = 2;
     public static final int SERVICE_ID_REMOVE_THING = 3;
     public static final int SERVICE_ID_ADD_THING_METADATA = 4;
     public static final int SERVICE_ID_RETRIEVE_THING_METADATA = 5;
     public static final int SERVICE_ID_FOLLOW_THINGS = 6;
     public static final int SERVICE_ID_UNFOLLOW_THINGS = 7;
     public static final int SERVICE_ID_POST_EVENT = 8;
     public static final int SERVICE_ID_RETRIEVE_EVENT = 9;
     public static final int SERVICE_ID_RETRIEVE_EVENTS = 10;
     public static final int SERVICE_ID_RETRIEVE_EVENT_CONTENT = 11;
     public static final int SERVICE_ID_RETRIEVE_EVENT_CONTENT_INFO = 12;
     public static final int SERVICE_ID_RETRIEVE_EVENT_CONTENTS_INFO = 13;
     public static final int SERVICE_ID_UPDATE_EVENT = 14;
     
}
