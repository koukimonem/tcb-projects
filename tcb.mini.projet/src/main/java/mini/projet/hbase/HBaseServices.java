package mini.projet.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import mini.projet.entities.Paiment;

public final class HBaseServices {
	private static HBaseServices hbaseService;

	public void createPaimenet(Paiment paiment) throws IOException {
		Configuration conf = new Configuration();
		conf.set("hbase.zookeeper.quorum", "192.168.1.163");
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("zookeeper.znode.parent", "/hbase-unsecure");
		Connection connection = ConnectionFactory.createConnection(conf);
		Table table = connection.getTable(TableName.valueOf("kouki:paiment"));
		Put put = new Put(Bytes.toBytes(paiment.getIdentifiant()));
		put.addColumn("numero".getBytes(), "a".getBytes(), Bytes.toBytes(paiment.getNumero()));
		put.addColumn("identifiant".getBytes(), "a".getBytes(), Bytes.toBytes(paiment.getIdentifiant()));
		put.addColumn("codeMessage".getBytes(), "a".getBytes(), Bytes.toBytes(paiment.getCodeMessage()));
		put.addColumn("montantTTC".getBytes(), "a".getBytes(), Bytes.toBytes(paiment.getMontantTTC()));
		put.addColumn("dateEcheanceFacture".getBytes(), "a".getBytes(),
				Bytes.toBytes(paiment.getDateEcheanceFacture()));
		put.addColumn("dateEcheanceApurement".getBytes(), "a".getBytes(),
				Bytes.toBytes(paiment.getDateEcheanceApurement()));
		put.addColumn("marqueur4".getBytes(), "a".getBytes(),
				Bytes.toBytes(paiment.getMarqueur4() != null ? paiment.getMarqueur4() : ""));
		put.addColumn("marqueur5".getBytes(), "a".getBytes(), Bytes.toBytes(paiment.getMarqueur5() != null ? paiment.getMarqueur5() : ""));
		table.put(put);
		connection.close();
	}

	public static HBaseServices getHbaseService() {
		if (hbaseService == null)
			hbaseService = new HBaseServices();
		return hbaseService;
	}

	private HBaseServices() {

	}
}
