package br.ufscar.dc.thingbroker.services.impl;

import org.apache.http.auth.UsernamePasswordCredentials;

import android.os.AsyncTask;
import br.ufscar.dc.thingbroker.config.Constants;
import br.ufscar.dc.thingbroker.interfaces.ThingBrokerRequestListener;
import br.ufscar.dc.thingbroker.model.EventData;

class AsynchronousRequestTask extends AsyncTask<Object, Object, Object> {
	private ThingBrokerRequestListener listener;
	private Exception exception;
	private int serviceId;
	private String url;
	private String httpOperation;
	private Object requestBody;
	private UsernamePasswordCredentials credentials;

	public AsynchronousRequestTask(final int serviceId, String url,String httpOperation, Object requestBody,UsernamePasswordCredentials credentials,ThingBrokerRequestListener listener) {
		this.serviceId = serviceId;
		this.url = url;
		this.httpOperation = httpOperation;
		this.requestBody = requestBody;
		this.credentials = credentials;
		this.listener = listener;
	}

	@Override
	protected Object doInBackground(Object... params) {
		if(Constants.DEBUG_MODE) {
			System.out.println("Calling URL: " + url);
			if(requestBody != null) {
				System.out.println("Request body: " + requestBody.toString());
			}
		}
		try {
			if (httpOperation.equals("GET")) {
				return HTTPUtil.doGet(url, credentials);
			} else {
				if (httpOperation.equals("POST")) {
					if(requestBody instanceof String) {
					    return HTTPUtil.doPost(url, (String)requestBody, credentials);
					}
					else {
						if(requestBody instanceof EventData[]) {
							EventData [] dataToUpload = (EventData []) requestBody;
							return HTTPUtil.doPost(url, dataToUpload, credentials);
						}
					}
				} else {
					if (httpOperation.equals("PUT")) {
                       return HTTPUtil.doPut(url, (String)requestBody, credentials);
					} 
					else {
					   if(httpOperation.equals("DELETE")) {
                          return HTTPUtil.doDelete(url, credentials);
					   }
					}
				}
			}
		} catch (Exception ex) {
			this.exception = ex;
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object obj) {
		listener.executeAfterRequest(serviceId, obj, exception);
	}

}
