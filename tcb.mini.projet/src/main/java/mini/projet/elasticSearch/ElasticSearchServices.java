package mini.projet.elasticSearch;

import java.net.InetSocketAddress;

import org.apache.htrace.fasterxml.jackson.core.JsonProcessingException;
import org.apache.htrace.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import mini.projet.main.Conf;

public final class ElasticSearchServices {

	private static ObjectMapper mapper = new ObjectMapper();
	private static ElasticSearchServices ESObject;

	public static void sendToElasticSearch(Object obj) {
		TransportClient client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(
				new InetSocketTransportAddress(new InetSocketAddress(Conf.ADRESS_ES, Conf.PORT_ES)));
		byte[] json;
		try {
			json = mapper.writeValueAsBytes(obj);
			client.prepareIndex(Conf.INDEX_ES, Conf.TYPE_ES).setSource(json).get();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}

	private ElasticSearchServices() {
	}

	public static ElasticSearchServices getESObject() {
		if (ESObject == null)
			ESObject = new ElasticSearchServices();
		return ESObject;
	}

}
