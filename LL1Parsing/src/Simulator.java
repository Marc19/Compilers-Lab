import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Simulator 
{
	public static ParsingTable buildParsingTable(CFG cfg)
	{
		ParsingTable parsingTable = new ParsingTable();
		
		ArrayList<String> nonTerminals = cfg.getNonTerminals();
		ArrayList<String> terminals = cfg.getTerminals();
		terminals.add("$");
		HashMap<String, String> rules = cfg.getRules();
		HashMap<String, String> allFirsts = cfg.getFirsts();
		HashMap<String, String> allFollows = cfg.getFollows();
		
		boolean isStartVariable = true;
		for(int i=0; i<nonTerminals.size(); i++)
		{
			String nonTerminal = nonTerminals.get(i);
			
			ArrayList<String> firsts = new ArrayList<>(Arrays.asList(allFirsts.get(nonTerminal).split(",")));
			ArrayList<String> follows = new ArrayList<>(Arrays.asList(allFollows.get(nonTerminal).split(",")));
			
			HashMap<String, String> dollarAddedRules = new HashMap<String, String>();
			for(int j=0; j<terminals.size(); j++)
			{
				HashMap<String, String> addedRules = new HashMap<String, String>();
				String terminal = terminals.get(j);
								
				if(firsts.contains(terminal))
				{
					Iterator<Map.Entry<String, String>> it = rules.entrySet().iterator();
					while (it.hasNext())
					{
						Map.Entry<String, String> entry = it.next();
						String key = entry.getKey();
						String rulesatStr = entry.getValue();
						String[] rulesatArr = rulesatStr.split(",");
						
						if(nonTerminal.equals(key))
						{
							for(int k=0; k<rulesatArr.length; k++)
							{
								String rulayaStr = rulesatArr[k];
								String[] rulayaArr = rulayaStr.split(" ");
								
								boolean alreadyInsertedRule = false;
								for(int l=0; l<rulayaArr.length; l++)
								{
									if(rulayaArr[l].equals("epsilon"))
										continue;
									
									ArrayList<String> firstat = new ArrayList<>(Arrays.asList(allFirsts.get(rulayaArr[l]).split(",")));
									if(firstat.contains(terminal) && !alreadyInsertedRule)
									{
										if(!(addedRules.containsKey(nonTerminal)))
											addedRules.put(nonTerminal, rulayaStr);
										else
											addedRules.put(nonTerminal, addedRules.get(nonTerminal) + "," + rulayaStr);
										alreadyInsertedRule = true;
									}
									else if(!(firstat.contains("epsilon")))
										break;
									
									if((l+1) == rulayaArr.length && isStartVariable && firstat.contains("epsilon"))
									{
										if(!(dollarAddedRules.containsKey(nonTerminal)))
											dollarAddedRules.put(nonTerminal, rulayaStr);
										else
											dollarAddedRules.put(nonTerminal, dollarAddedRules.get(nonTerminal) + "," + rulayaStr);
									}
								}
							}
							
						}
					}
				}
				
				if(firsts.contains("epsilon") && follows.contains(terminal))
				{
					if(terminal.equals("$"))
					{
						if(!(dollarAddedRules.containsKey(nonTerminal)))
							dollarAddedRules.put(nonTerminal, "epsilon");
						else
							dollarAddedRules.put(nonTerminal, dollarAddedRules.get(nonTerminal) + "," + "epsilon");
					}
					else
					{
						if(!(addedRules.containsKey(nonTerminal)))
							addedRules.put(nonTerminal, "epsilon");
						else
							addedRules.put(nonTerminal, addedRules.get(nonTerminal) + "," + "epsilon");
					}
				}
				
				if(!(addedRules.isEmpty()))
				{
					ParsingTableEntry pte = new ParsingTableEntry(nonTerminal, terminal, addedRules);
					parsingTable.addEntry(pte);
				}
				
			}
			
			if(!(dollarAddedRules.isEmpty()))
			{
				List<String> duplicateRules =  new ArrayList<>(Arrays.asList(dollarAddedRules.get(nonTerminal).split(",")));
				
				Set<String> hs = new HashSet<>();
				hs.addAll(duplicateRules);
				duplicateRules.clear();
				duplicateRules.addAll(hs);
				
				dollarAddedRules.put(nonTerminal, hs.toString());
				
				ParsingTableEntry pte = new ParsingTableEntry(nonTerminal, "$", dollarAddedRules);
				parsingTable.addEntry(pte);
			}
			isStartVariable = false;
		}
		
		return parsingTable;
	}

	public static void main(String[] args) 
	{
		CFG cfg1 = new CFG();
		
		cfg1.addNonTerminal("S");
		cfg1.addNonTerminal("A");
		cfg1.addNonTerminal("B");
		cfg1.addNonTerminal("C");
		
		cfg1.addTerminal("a");
		cfg1.addTerminal("b");
		cfg1.addTerminal("c");
		
		cfg1.addRule("S", "S A B");
		cfg1.addRule("S", "S B C");
		cfg1.addRule("S", "epsilon");
		cfg1.addRule("A", "a A a");
		cfg1.addRule("A", "epsilon");
		cfg1.addRule("B", "b B");
		cfg1.addRule("B", "epsilon");
		cfg1.addRule("C", "c C");
		cfg1.addRule("C", "epsilon");
		
		cfg1.printGrammar();
		System.out.println("---------------------");
		cfg1.computeFirst();
		System.out.println();
		cfg1.computeFollow();
		ParsingTable pt1 = buildParsingTable(cfg1);
		pt1.printParsingTable();
		
		System.out.println("-------------------------------------------------------------------------");	
		
		CFG cfg2 = new CFG();
		
		cfg2.addNonTerminal("S");
		cfg2.addNonTerminal("A");
		cfg2.addNonTerminal("B");
		cfg2.addNonTerminal("C");
		
		cfg2.addTerminal("id");
		cfg2.addTerminal("num");
		cfg2.addTerminal("0");
		cfg2.addTerminal("1");
		
		cfg2.addRule("S", "A B");
		cfg2.addRule("A", "id A");
		cfg2.addRule("A", "num");
		cfg2.addRule("B", "C A");
		cfg2.addRule("C", "0 C");
		cfg2.addRule("C", "1");
		
		cfg2.printGrammar();
		System.out.println("---------------------");
		cfg2.computeFirst();
		System.out.println();
		cfg2.computeFollow();
		ParsingTable pt2 = buildParsingTable(cfg2);
		pt2.printParsingTable();
		
		System.out.println("-------------------------------------------------------------------------");
		
		CFG cfg3 = new CFG();
		
		cfg3.addNonTerminal("S");
		cfg3.addNonTerminal("L");
		cfg3.addNonTerminal("LPrime");
		
		cfg3.addTerminal("(");
		cfg3.addTerminal(")");
		cfg3.addTerminal("a");
		cfg3.addTerminal(";");
		
		cfg3.addRule("S", "( L )");
		cfg3.addRule("S", "a");
		cfg3.addRule("L", "( L ) LPrime");
		cfg3.addRule("L", "a LPrime");
		cfg3.addRule("LPrime", "; S LPrime");
		cfg3.addRule("LPrime", "epsilon");
		
		cfg3.printGrammar();
		System.out.println("---------------------");
		cfg3.computeFirst();
		System.out.println();
		cfg3.computeFollow();
		ParsingTable pt3 = buildParsingTable(cfg3);
		pt3.printParsingTable();
		
		System.out.println("-------------------------------------------------------------------------");
	
		
	}
}
