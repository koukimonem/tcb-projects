package mini.projet.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex {

	private static Pattern pattern;
	private static Matcher matcher;

	public static void main(String[] args) {
		// String to be scanned to find the pattern.
		String line = "0621027926;SATHO_ACTCO|EE|OUE|20160126|TELEPHONE|PART_0018|DETECTION PROJETS RENO|ACCEPTEE|5000804735|RES_A78048|NANTES;SATHO_ACTCO_001;;20160126;;M.; AUDRU";
		String pattern = "(.*);(.*);(.*);(.*);(.*);(.*);(.*);(.*)";
		String REGEX_PATTERN_FACTUR_SEOND = "(.*)|(.*)|(.*)|(.*)|(.*)|(.*)|(.*)|(.*)|(.*)|(.*)|(.*)|(.*)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		if(m.find())
			{
			String ss =  m.group(2);
			r = Pattern.compile(REGEX_PATTERN_FACTUR_SEOND);
			m = r.matcher(ss);
			m.find();
			System.out.println(m.group(2));
			}

	}

}
