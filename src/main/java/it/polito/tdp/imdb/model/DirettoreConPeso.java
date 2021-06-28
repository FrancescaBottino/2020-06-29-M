package it.polito.tdp.imdb.model;

public class DirettoreConPeso implements Comparable<DirettoreConPeso>{

	
	private Director d;
	private Integer peso;
	
	public DirettoreConPeso(Director d, Integer peso) {
		super();
		this.d = d;
		this.peso = peso;
	}

	public Director getD() {
		return d;
	}

	public void setD(Director d) {
		this.d = d;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	@Override
	public int compareTo(DirettoreConPeso other) {
		
		
		
		return other.getPeso() - this.getPeso();
	}
	
	
	
}
