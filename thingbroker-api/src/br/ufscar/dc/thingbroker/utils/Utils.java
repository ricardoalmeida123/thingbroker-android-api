package br.ufscar.dc.thingbroker.utils;

import java.io.IOException;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.fasterxml.jackson.core.JsonParseException;

public class Utils<T> {
	
	/**
	 * Generate JSON string from a java object
	 * 
	 * @param javaObject
	 *            Java Object to be converted to JSON
	 * @return JSON string
	 */
	public static String generateJSON(Object javaObject) {
		String jsonString = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			jsonString = mapper.writeValueAsString(javaObject);
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
			jsonString = null;
		}
		return jsonString;
	}

	public static <T> T parseToObject(String json, Class<T> classe)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, classe);
	}
}
