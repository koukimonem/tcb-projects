package mini.projet.entities;

import java.io.Serializable;

public class Paiment implements Serializable {
	
	private long numero;
	private String identifiant;
	private String codeMessage;
	private String montantTTC;
	private String dateEcheanceFacture;
	private String dateEcheanceApurement;
	private String marqueur4;
	private String marqueur5;

	public long getNumero() {
		return numero;
	}

	public void setNumero(long numero) {
		this.numero = numero;
	}

	public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

	public String getCodeMessage() {
		return codeMessage;
	}

	public void setCodeMessage(String codeMessage) {
		this.codeMessage = codeMessage;
	}

	public String getMontantTTC() {
		return montantTTC;
	}

	public void setMontantTTC(String montantTTC) {
		this.montantTTC = montantTTC;
	}

	public String getDateEcheanceFacture() {
		return dateEcheanceFacture;
	}

	public void setDateEcheanceFacture(String dateEcheanceFacture) {
		this.dateEcheanceFacture = dateEcheanceFacture;
	}

	public String getMarqueur5() {
		return marqueur5;
	}

	public void setMarqueur5(String marqueur5) {
		this.marqueur5 = marqueur5;
	}

	public String getDateEcheanceApurement() {
		return dateEcheanceApurement;
	}

	public void setDateEcheanceApurement(String dateEcheanceApurement) {
		this.dateEcheanceApurement = dateEcheanceApurement;
	}

	public String getMarqueur4() {
		return marqueur4;
	}

	public void setMarqueur4(String marqueur4) {
		this.marqueur4 = marqueur4;
	}



	public Paiment() {

	}

}
