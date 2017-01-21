package mini.projet.main;

public final class Conf {
	public static final String TOPICNAME = "koukiProj";
	public static final String CONSUMERURI = "kouki:8081";
	public static final String PRODUCERURI = "kouki:8081";
	public static final String GROUP = "kouki-miniprojet-group-1";
	public static final String ADRESS_ES = "127.0.0.1";
	public static final int PORT_ES = 9300;
	public static final String REGEX_PATTERN_FACTUR = "(.*);(.*);(.*);(.*);(.*);(.*);(.*);(.*)";
	public static final String REGEX_PATTERN_FACTUR_SEOND = "(.*)|(.*)|(.*)|(.*)|(.*)|(.*)|(.*)|(.*)|(.*)|(.*)|(.*)";
	public static final String INDEX_ES = "mini_projet";
	public static final String TYPE_ES = "paiment";
	
	private Conf() {

	}
}
