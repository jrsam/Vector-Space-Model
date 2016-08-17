import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
/**
 * 
 * @author Samyu
 *
 */

public class Driver {
	static HashMap<String, Double> idf = new HashMap<String,Double>();
	static List<Double> similarities = new LinkedList<Double>();
	static List<String> stopWords = new LinkedList<String>();
	//calculate cosine similarity
	public static double cosineSimilarity(Document query, Document doc) { 
		double numerator = 0, denominator =0,temp = 0 ;
		for( String key:query.getTermWeight().keySet() ) {
			numerator += (query.getTermWeight(key) * doc.getTermWeight(key));
		}
		for(String key: query.getTermWeight().keySet()) {
			temp += Math.pow(query.getTermWeight(key), 2 ) ; 
		}
		
		denominator += Math.sqrt(temp) ;
		temp = 0;
		for(String key: doc.getTermWeight().keySet() ) {
			temp += Math.pow(doc.getTermWeight(key), 2 ) ; 
		}	
		denominator *= Math.sqrt(temp) ;
		return numerator/denominator;
	}
	public static void main(String[] args) throws IOException {
		//construct stop words
		BufferedReader reader = new BufferedReader(new FileReader(".\\stopwords.txt"));
		String word = "";
		while( ( word = reader.readLine() ) != null){
			stopWords.add( word.replace("'", "").toLowerCase() );
		}
		reader.close();
		Corpus corpus = new Corpus(".\\corpus");
		List<Document> Docs = new LinkedList<Document>();
		for(int i=0; i< corpus.getFileList().length; i++ ) {
			Docs.add( new Document().constructDoc(corpus.getFileList()[i], "Doc "+String.valueOf(i+1)) ) ;
		}
		//construct unique terms from all documents in corpus
		corpus.addUniqueTerms(Docs); 
		int docsWithTerm = 0;
		
		//idf of term i = No.of docs / no. of docs containing the term i
		for(int i=0; i<corpus.getUniqueWords().size(); i++ ) {
			for(int j=0; j<Docs.size(); j++) {
				if(Docs.get(j).containsTerm(corpus.getUniqueWords().get(i))) {
					docsWithTerm += 1;
				}
			}
			idf.put(corpus.getUniqueWords().get(i), Math.log10( (double)Docs.size() /(double)docsWithTerm) ) ;
			docsWithTerm = 0;
		}

		//set term weights for all docs
		for(Document doc: Docs) {			
			doc.setTermWeight();
		}
		
		//processing query
		File file = new File(".\\query.txt");
		Document query = new Document();
		query.constructDoc(file, "query") ;
		query.setTermWeight();
		//ranking
		for(Document doc: Docs) {
			doc.setSimilarity( cosineSimilarity(query, doc) ) ;
			  System.out.println("Similarity of "+doc.getId()+ " is "+ String.format("%.4f", doc.getSimilarity()) ) ;
		}
		//Rank docs with respect to query
		Collections.sort(Docs);
		for(int i=0; i<Docs.size(); i++) {
			System.out.println( "Rank "+(i+1)+ " - "+ Docs.get(i).getId() );
		}
	}
	

}
