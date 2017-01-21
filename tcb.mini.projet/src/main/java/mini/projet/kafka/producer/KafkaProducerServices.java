package mini.projet.kafka.producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import mini.projet.main.Conf;

public final class KafkaProducerServices {

	private static KafkaProducerServices INSTANCE = null;

	public void send(String message) {

		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Conf.PRODUCERURI);

		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.RETRIES_CONFIG, 0);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

		Producer<String, String> producer = new KafkaProducer<String, String>(props);
		ProducerRecord<String, String> data = new ProducerRecord<String, String>(Conf.TOPICNAME, message);
		producer.send(data);

		producer.close();
	}

	public static KafkaProducerServices getProducer() {
		if (INSTANCE == null)
			INSTANCE = new KafkaProducerServices();
		return INSTANCE;
	}

	private KafkaProducerServices() {

	}
}