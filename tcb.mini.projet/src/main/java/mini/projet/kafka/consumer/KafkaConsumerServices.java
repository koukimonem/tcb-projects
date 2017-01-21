package mini.projet.kafka.consumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import com.google.gson.Gson;

import mini.projet.elasticSearch.ElasticSearchServices;
import mini.projet.entities.Paiment;
import mini.projet.hbase.HBaseServices;
import mini.projet.main.Conf;

public final class KafkaConsumerServices implements Runnable {
	private ElasticSearchServices esObject = ElasticSearchServices.getESObject();
	private HBaseServices hbaseServ = HBaseServices.getHbaseService();
	private static KafkaConsumerServices consumerServices;

	public void run() {

		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Conf.CONSUMERURI);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, Conf.GROUP);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");

		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		TestConsumerRebalanceListener rebalanceListener = new TestConsumerRebalanceListener();
		consumer.subscribe(Collections.singletonList(Conf.TOPICNAME), rebalanceListener);

		while (true) {

			ConsumerRecords<String, String> records = consumer.poll(1000);
			ArrayList<Paiment> listPaiments = new ArrayList<Paiment>();
			Gson gson = new Gson();
			for (ConsumerRecord<String, String> record : records) {
				if (PatternMatcher.matchFacture(record.value().toString())) {
					String splits[] = record.value().toString().split(";");
					if (isInteger(splits[0], 10)) {
						Paiment paimment = new Paiment();
						paimment.setNumero(Long.parseLong(splits[0]));
						paimment.setIdentifiant(splits[1]);
						paimment.setCodeMessage(splits[2]);
						paimment.setMontantTTC(splits[3]);
						paimment.setDateEcheanceFacture(splits[4]);
						paimment.setDateEcheanceApurement(splits[5]);
						if (splits.length > 6) {
							paimment.setMarqueur4(splits[6]);
							if (splits.length == 8)
								paimment.setMarqueur5(splits[7]);
						}

						try {
							hbaseServ.createPaimenet(paimment);
							esObject.sendToElasticSearch(paimment);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			}
			consumer.commitSync();
		}

	}

	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}

	public static boolean isInteger(String s, int radix) {
		if (s.isEmpty())
			return false;
		for (int i = 0; i < s.length(); i++) {
			if (i == 0 && s.charAt(i) == '-') {
				if (s.length() == 1)
					return false;
				else
					continue;
			}
			if (Character.digit(s.charAt(i), radix) < 0)
				return false;
		}
		return true;
	}

	private static class TestConsumerRebalanceListener implements ConsumerRebalanceListener {

		public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
			System.out.println("Called onPartitionsRevoked with partitions:" + partitions);
		}

		public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
			System.out.println("Called onPartitionsAssigned with partitions:" + partitions);
		}
	}

	public static KafkaConsumerServices getConsumerServices() {
		if (consumerServices == null)
			consumerServices = new KafkaConsumerServices();
		return consumerServices;
	}

	private KafkaConsumerServices() {
	}
}