import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ParsingTableEntry 
{
	String nonTerminal;
	String terminal;
	HashMap<String, String> rules;
	
	public ParsingTableEntry(String nonTerminal, String terminal, HashMap<String, String> rules)
	{
		this.nonTerminal = nonTerminal;
		this.terminal = terminal;
		this.rules = rules;
	}

	public String getNonTerminal() 
	{
		return nonTerminal;
	}

	public void setNonTerminal(String nonTerminal) 
	{
		this.nonTerminal = nonTerminal;
	}

	public String getTerminal()
	{
		return terminal;
	}

	public void setTerminal(String terminal) 
	{
		this.terminal = terminal;
	}

	public HashMap<String, String> getRules() 
	{
		return rules;
	}

	public void setRules(HashMap<String, String> rules)
	{
		this.rules = rules;
	}

	public String toString()
	{
		String rulesStr = "";
		Iterator<Map.Entry<String, String>> it = rules.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			
			rulesStr += ", " + key + "-->" + value;
		}
		
		rulesStr = rulesStr.substring(2);
		
		return "[" + nonTerminal + "," + terminal + "]: " + rulesStr;
	}
}
