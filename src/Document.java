import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Document implements Comparable<Document>{
	HashMap<String,Integer> rawFrequency = new HashMap<String,Integer>(); 
	HashMap<String, Double> termWeight = new HashMap<String,Double>();
	String id="";
	double similarity;
	public HashMap<String, Integer> getrawFrequency() {
		return rawFrequency;
	}
	/**
	 * 
	 * @return document Id
	 */
	public String getId() {
		return id;
	}
	//construct terms of Document excluding stop words
	/**
	 * 
	 * @param file Input document from corpus
	 * @param id document id
	 * @return constructed document
	 * @throws IOException
	 */
	public Document constructDoc(File file,String id) throws IOException {
		this.id = id;
		String line = "";
		BufferedReader reader = new BufferedReader(new FileReader(file));
		while(  (line = reader.readLine()) != null) {
			for ( String word: line.split("[\\.,!\\s\\-/]\\s*") ) {
				word = word.replace("'", "").toLowerCase();				
				if( !Driver.stopWords.contains(word) ) {
					addTerm(word);
				}
				
			}
		}
		reader.close();
		return this;
	}
	public void setId(String id) {
		this.id = id;
	}
	//add term with frequency
	public void addTerm(String term) { 
		if(rawFrequency.get(term) != null) {
			rawFrequency.put(term, rawFrequency.get(term)+1);
		}
		else {
			rawFrequency.put(term, 1);
		}
	}
	public int getTermFrequency(String term) {
		
			return rawFrequency.get(term) !=null ? rawFrequency.get(term) : 0; 
		
	}
	public boolean containsTerm(String term) {
		if(rawFrequency.keySet().contains(term)) {
			return true;
		}
		else
			return false;
	}
	public int getMaxFreq() {
		int max = 0;
		for( String key:rawFrequency.keySet()) {
			if(rawFrequency.get(key) > max ) {
				max = rawFrequency.get(key);
			}
		}
		return max;
	}
	//term weight of term i in this document = (rawFrequency of term i / frequency of maximum occuring term in this document ) * idf of term i
	public void setTermWeight() {
		for(String term: rawFrequency.keySet()) {
			
			termWeight.put(term, ( (double)rawFrequency.get(term)/(double)getMaxFreq() ) * (double)Driver.idf.get(term) ) ;			
		}
	}
	public HashMap<String, Double> getTermWeight() {
		return termWeight;
	}
	public double getTermWeight(String term) {
		return termWeight.get(term) != null ? termWeight.get(term): 0;
	}
	public double getSimilarity() {
		return similarity;
	}
	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
	@Override
	public int compareTo(Document doc) {
		if(this.similarity > doc.similarity )
			return -1;
		else if(this.similarity < doc.similarity )
			return 1;
		return 0;
	}
	
	
}

