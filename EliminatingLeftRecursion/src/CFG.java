import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CFG 
{
	ArrayList<String> nonTerminals; // the start variable should be the first element of the list
	ArrayList<String> terminals; // terminals start with a small letter while non-terminals start with a capital letter
	HashMap<String, String> rules; //key: nonterminals, value: sentential forms separated by ','. Epsilon is '$'
	
	public CFG()
	{
		nonTerminals = new ArrayList<>();
		terminals = new ArrayList<>();
		rules = new HashMap<>();
	}
	
	public void addNonTerminal(String nonTerminal)
	{
		this.nonTerminals.add(nonTerminal);
	}
	
	public void addTerminal(String terminal)
	{
		this.terminals.add(terminal);
	}
	
	public void addRule(String nonTerminal, String sententialForm)
	{
		if( rules.containsKey(nonTerminal))
			rules.put(nonTerminal, rules.get(nonTerminal) + "," + sententialForm);
		else
			rules.put(nonTerminal, sententialForm); 
	}
	
	public void removeRule(String key, String value)
	{
		if(this.rules.containsKey(key) && this.rules.get(key).contains(value))
		{
			this.rules.put(key, this.rules.get(key).replace(value, "").replace(",,", ","));
			if(this.rules.get(key).startsWith(","))
				this.rules.put(key, this.rules.get(key).substring(1));
			if(this.rules.get(key).endsWith(","))
				this.rules.put(key, this.rules.get(key).substring(0,this.rules.get(key).length()-1));
		}
	}
	
	public void justRemoveEpsilon(String key)
	{
		removeRule(key,"$");
	}
	
	public void printGrammar()
	{
		for(int i=0; i<this.nonTerminals.size(); i++)
		{
			System.out.println(nonTerminals.get(i) + " --> " + this.rules.get(nonTerminals.get(i)));
			
			if(rules.containsKey(nonTerminals.get(i) + "'"))
				System.out.println(nonTerminals.get(i) + "'" + " --> " + this.rules.get(nonTerminals.get(i) + "'"));
		}
	}
	
	public ArrayList<String> getNonTerminals() 
	{
		return nonTerminals;
	}
	
	public ArrayList<String> getTerminals() 
	{
		return terminals;
	}
	
	public HashMap<String, String> getRules()
	{
		return rules;
	}

	public String toString()
	{
		String result = "";
		for(int i=0; i<this.nonTerminals.size(); i++)
		{
			result += (nonTerminals.get(i) + " --> " + this.rules.get(nonTerminals.get(i)) + "\n");
			
			if(rules.containsKey(nonTerminals.get(i) + "'"))
				result += (nonTerminals.get(i) + "'" + " --> " + this.rules.get(nonTerminals.get(i) + "'" + "\n"));
		}
		
		return result;
	}
}
