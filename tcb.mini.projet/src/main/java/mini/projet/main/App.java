package mini.projet.main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;

import mini.projet.kafka.consumer.KafkaConsumerServices;
import mini.projet.kafka.producer.KafkaProducerServices;

public class App {
	public static void main(String[] args) throws Exception {

		KafkaConsumerServices consumer = KafkaConsumerServices.getConsumerServices();
		consumer.start();
		KafkaProducerServices producer = KafkaProducerServices.getProducer();
		String uri = "hdfs://hdpmaster1:8020/user/kouki/type1/FLUX_TYPE01_00001.tar.gz";
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		Path inputPath = new Path(uri);
		CompressionCodecFactory factory = new CompressionCodecFactory(conf);
		CompressionCodec codec = factory.getCodec(inputPath);
		InputStream in = null;
		try {
			in = codec.createInputStream(fs.open(inputPath));
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = br.readLine()) != null) {
				producer.send(line);
			}
		} finally {

		}
	}
}