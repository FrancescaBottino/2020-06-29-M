package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Graph<Director, DefaultWeightedEdge> grafo;
	private Map<Integer, Director> idMap;
	private List<Director> soluzioneMigliore;
	private Integer lunghezzaMax;
	private Integer pesoMax;
	
	
	public Model() {
		
		dao = new ImdbDAO();
		idMap = new HashMap<>();
		dao.listAllDirectors(idMap);
		
		
	}
	
	public void creaGrafo(Integer anno) {
		
		
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		
		//vertici
		
		Graphs.addAllVertices(this.grafo, dao.getAllVertices(idMap, anno));
		
		
		//archi 
		
		for(Adiacenza a: dao.getAllAdiacenze(anno, idMap)) {
			
			Graphs.addEdgeWithVertices(this.grafo, a.getD1(), a.getD2(), a.getPeso());
			
			
		}
		
	}
	
	
	public Integer getNVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public Integer getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Director> getDirettori(){
		
		
		List<Director> direttori = new ArrayList<Director>(grafo.vertexSet());
		Collections.sort(direttori);
		
		return direttori;
	}
	
	
	public List<DirettoreConPeso> getRegistiAdiacenti(Director inizio){
		
		List<Director> vicini = Graphs.neighborListOf(this.grafo, inizio);
		List<DirettoreConPeso> direttoriConPeso = new ArrayList<DirettoreConPeso>();
		
		
		for(Director d: vicini) {
			
			DirettoreConPeso dp = new DirettoreConPeso(d, (int) grafo.getEdgeWeight(this.grafo.getEdge(inizio, d)));
			direttoriConPeso.add(dp);
			
		}
		
		
		Collections.sort(direttoriConPeso);
		
		return direttoriConPeso;
		
	}

	
	//Ricorsione
	/*
	 * un gruppo contenente il massimo numero di registi concatenati a partire da quello selezionato
	 * al punto 1.d per cui la somma degli attori condivisi sia minore o uguale a c.
	 * occorre trovare un cammino di lunghezza massima per cui la
		somma dei pesi degli archi percorsi sia minore o uguale a c.
	 */
	
	public List<Director> trovaPercorso(Integer c, Director inizio){
		
		this.soluzioneMigliore= null;
		
		this.lunghezzaMax=0;
		
		List<Director> parziale = new ArrayList<Director>();
		parziale.add(inizio);
		
		cerca(parziale, c);
		
		return soluzioneMigliore;
		
		
		
	}
	
	public void cerca(List<Director> parziale, Integer c) {
		
		
		int sommaPesi = calcolaPesoTot(parziale);
		
		if(sommaPesi <= c) {
			
			// calcolo la lunghezza e vedo se è massima
			int lunghezza = parziale.size();
			
			if(lunghezza > lunghezzaMax) {
				
				lunghezzaMax = lunghezza;
				soluzioneMigliore = new ArrayList<Director>(parziale);
				pesoMax = sommaPesi; 
				
			}		
			
		}
		else
			return;
		
		
		//sottoproblemi 
		
		
		for(Director vicino: Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			
			if(!parziale.contains(vicino)) {
				parziale.add(vicino);
				cerca(parziale, c);
				parziale.remove(parziale.size()-1);
			}
			
			
		}
			
	
		
		
		
	}

	private int calcolaPesoTot(List<Director> parziale) {
		
		int peso=0;
		
		//calcolo il peso della soluzione parziale
		
		for(int i = 0; i<parziale.size(); i++) {
			
			
			Director d1 = parziale.get(i);
			
			if (d1.equals(parziale.get(parziale.size()-1))) {
				return peso;
			}
			
			Director d2 = parziale.get(i+1);
			
			
			if(d1 != null && d2 != null) {
				peso += grafo.getEdgeWeight(grafo.getEdge(d1, d2));
				
			}
			
			
		}
		
		
		return peso;
	}
	
	
	public Integer getPesoMax() {
		return pesoMax;
	}
	
	
}
