package monitoring.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Utils {
	

	public static String[] generateKeywordExp(String keywordExpression) {
		String[] blocks = keywordExpression.split(" AND ");
		for (int i = 0; i < blocks.length; ++i) {
			blocks[i] = blocks[i].replace("(", "");
			blocks[i] = blocks[i].replace(")", "");
		}
		List<List<String>> cnfCombinations = new ArrayList<>();
		for (int i = 0; i < blocks.length; ++i) 
			cnfCombinations.add(getKeyElements(blocks[i]));
		List<List<String>> dnfCombinations = new ArrayList<>();
		for (int i = 0; i < cnfCombinations.get(0).size(); ++i) 
			dnfCombinations.addAll(getDnfCombination(cnfCombinations, 0, i));
		
		String[] keywordDNFExpression = new String[dnfCombinations.size()];
		for (int i = 0; i < dnfCombinations.size(); ++i) {
			String keyword = "";
			for (String s : dnfCombinations.get(i)) 
				keyword += s + " ";
			keywordDNFExpression[i] = keyword;
		}
		
		return keywordDNFExpression;
	}
	
	/**
	 * Backtracking method for getting all combinations in DNF
	 */
	private static List<List<String>> getDnfCombination(List<List<String>> cnfCombination, int k, int j) {
		List<List<String>> dnf = new ArrayList<>();
		if (k == cnfCombination.size() -1 ) {
			List<String> l = new ArrayList<>();
			l.add(cnfCombination.get(k).get(j));
			dnf.add(l);
		}
		else {
			for (int i = 0; i < cnfCombination.get(k+1).size(); ++i) {
				List<List<String>> lists = getDnfCombination(cnfCombination, k + 1, i);
				for (List<String> l : lists) l.add(cnfCombination.get(k).get(j));
				dnf.addAll(lists);
			}
		}
		return dnf;
	}
	
	private static List<String> getKeyElements(String block) {
		String[] elements = block.split(" OR ");
		List<String> keyElements = new ArrayList<>();
		for (int i = 0; i < elements.length; ++i) {
			keyElements.add(elements[i]);
		}
		return keyElements;
	}
	
}
