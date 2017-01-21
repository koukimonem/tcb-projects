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
import mini.projet.entities.TYPE01_01;
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
			ArrayList<TYPE01_01> listPaiments = new ArrayList<TYPE01_01>();
			Gson gson = new Gson();
			for (ConsumerRecord<String, String> record : records) {
				if (PatternMatcher.matchFacture(record.value().toString())) {
					String splits[] = record.value().toString().split(";");
					if (isInteger(splits[0], 10)) {
						TYPE01_01 type01_01 = new TYPE01_01();
						type01_01.setNumero(Long.parseLong(splits[0]));
						type01_01.setIdentifiant(splits[1]);
						type01_01.setCodeMessage(splits[2]);
						type01_01.setMontantTTC(splits[3]);
						type01_01.setDateEcheanceFacture(splits[4]);
						type01_01.setDateEcheanceApurement(splits[5]);
						if (splits.length > 6) {
							type01_01.setMarqueur4(splits[6]);
							if (splits.length == 8)
								type01_01.setMarqueur5(splits[7]);
						}

						try {
							hbaseServ.createPaimenet(type01_01);
							esObject.sendToElasticSearch(type01_01);
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