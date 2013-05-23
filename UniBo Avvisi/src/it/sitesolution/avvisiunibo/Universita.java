package it.sitesolution.avvisiunibo;

public class Universita {
	private String nome;
	private int n;
	private String xmlUrl; 
	private String color;
	
	public Universita(String nome,int n,String xmlUrl,String color) {
		this.nome=nome;
		this.n=n;
		this.xmlUrl = xmlUrl;
		this.color = color;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}
	public String getXmlUrl() {
		return xmlUrl;
	}
	public void setXmlUrl(String xmlUrl) {
		this.xmlUrl = xmlUrl;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	
}
