import java.io.File; 
import java.util.LinkedList;
import java.util.List;

public class Corpus {
	File folder;
	File fileList[];
	List<String> uniqueWords = new LinkedList<String>();
	
	/**
	 * 
	 * @param location folder location of input docs
	 */
	Corpus(String location) {
		folder = new File(location);
		fileList = folder.listFiles();
	}
	public File[] getFileList() {
		return fileList;
	}
	void addUniqueTerms(List<Document> docs) {
		for(int i=0; i<docs.size(); i++) {
			for(String term: docs.get(i).rawFrequency.keySet()) {
				if(!uniqueWords.contains(term)) {
					uniqueWords.add(term) ;
				}
			}
		}
	}
	public List<String> getUniqueWords() {
		return uniqueWords;
	}
	
	
}
