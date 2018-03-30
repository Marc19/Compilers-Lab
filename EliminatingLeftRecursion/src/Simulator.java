import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Simulator 
{	
	public static CFG eliminateLeftRecursion(CFG cfg)
	{
		CFG resultingGrammar = new CFG();
		eliminateEpsilon(cfg);;
		resultingGrammar.copyNonTerminals(cfg);
		resultingGrammar.copyTerminals(cfg);
		
		for(int i=0; i<cfg.getNonTerminals().size(); i++)
		{
			for(int j=0; j<i; j++)
			{
				String[] rules = cfg.getRules().get(cfg.getNonTerminals().get(i)).split(",");
				
				for(int k=0; k<rules.length; k++)
				{
					String rule = rules[k];
					if(rule.startsWith(cfg.getNonTerminals().get(j)))
					{
						cfg.removeRule(cfg.getNonTerminals().get(i), rule);
						
						String[] rulesToBeSubstituted = cfg.getRules().get(cfg.getNonTerminals().get(j)).split(",");
						
						for(int l=0; l<rulesToBeSubstituted.length; l++)
						{
							String newRuleToBeSubstituted = rulesToBeSubstituted[l];
							String newRule = rule.replace(cfg.getNonTerminals().get(j), newRuleToBeSubstituted);
							
							cfg.addRule(cfg.getNonTerminals().get(i), newRule);
						}
					}
				}
			}
			
			eliminateImmediateLeftRecursion(cfg, cfg.getNonTerminals().get(i));
		}
		
		return resultingGrammar;
	}
	
	private static void eliminateImmediateLeftRecursion(CFG cfg, String nonTerminal) 
	{		
		if(!(cfg.getRules().get(nonTerminal).startsWith(nonTerminal)) && 
		   !(cfg.getRules().get(nonTerminal).contains(","+nonTerminal)))
			return;
			
		ArrayList<String> alphas = new ArrayList<>();
		ArrayList<String> betas =  new ArrayList<>();
		
		String[] rules = cfg.getRules().get(nonTerminal).split(",");
		
		
		for(int i=0; i<rules.length; i++)
		{
			String rule = rules[i];
			if(rule.startsWith(nonTerminal))
				alphas.add(rule.substring(nonTerminal.length()));
			else
				betas.add(rule);
		}
		
		cfg.getRules().remove(nonTerminal);
		
		for(int i=0; i<betas.size(); i++)
			cfg.addRule(nonTerminal, betas.get(i) + nonTerminal + "'");
		
		for(int i=0; i<alphas.size(); i++)
			cfg.addRule(nonTerminal + "'", alphas.get(i) + nonTerminal + "'");
		cfg.addRule(nonTerminal + "'", "$");
	}

	private static void eliminateEpsilon(CFG cfg) 
	{
		// this is not a full implementation of eliminating epsilon rules
		// this implementation assumes that a RHS of a rule contains only one variable that can go to epsilon
		
		Iterator<Map.Entry<String, String>> it = cfg.getRules().entrySet().iterator();

		while (it.hasNext())
		{
			Map.Entry<String, String> entry = it.next();
		  
			if(entry.getValue().contains("$,") || entry.getValue().contains(",$")) // found epsilon rule
			{   
				
				String nonTerminal = entry.getKey();
				cfg.justRemoveEpsilon(nonTerminal);
				  
				Iterator<Map.Entry<String, String>> it2 = cfg.getRules().entrySet().iterator();
				while (it2.hasNext())
				{ 
					Map.Entry<String, String> entry2 = it2.next();
					String[] distinctRules = entry2.getValue().split(",");
					 
					for(int i=0; i<distinctRules.length; i++)
					{
						String aRule = distinctRules[i];
						if(aRule.contains(nonTerminal))
						{
							String newRule = aRule.replace(nonTerminal,"");
							if(newRule.startsWith("."))
								newRule = newRule.substring(1);
							if(newRule.endsWith("."))
								newRule = newRule.substring(0, newRule.length()-1);
							  
							cfg.addRule(entry2.getKey(), newRule);
						}
					}
				}       
			}
		}
	}

	public static void main(String[] args) 
	{
		CFG cfg1 = new CFG();
		
		cfg1.addNonTerminal("S");
		cfg1.addNonTerminal("A");
		
		cfg1.addTerminal("a");
		cfg1.addTerminal("b");
		cfg1.addTerminal("c");
		cfg1.addTerminal("d");
		
		cfg1.addRule("S", "Aa");
		cfg1.addRule("S", "b");
		cfg1.addRule("A", "Ac");
		cfg1.addRule("A", "Sd");
		cfg1.addRule("A", "$");
		
		System.out.println("--------- Before eliminating left recursion: ---------");
		cfg1.printGrammar();
		
		eliminateLeftRecursion(cfg1);
		
		System.out.println("--------- After eliminating left recursion: ---------");
		cfg1.printGrammar();
		
		System.out.println("-----------------------------------------------------------------------------------------");
		
		CFG cfg2 = new CFG();
		
		cfg2.addNonTerminal("S");
		
		cfg2.addTerminal("a");
		cfg2.addTerminal("b");
		
		cfg2.addRule("S", "Sa");
		cfg2.addRule("S", "b");
		
		System.out.println("--------- Before eliminating left recursion: ---------");
		cfg2.printGrammar();
		
		eliminateLeftRecursion(cfg2);
		
		System.out.println("--------- After eliminating left recursion: ---------");
		cfg2.printGrammar();
		
		System.out.println("-----------------------------------------------------------------------------------------");
		
		CFG cfg3 = new CFG();
		
		cfg3.addNonTerminal("S");
		
		cfg3.addTerminal("a");
		cfg3.addTerminal("b");
		cfg3.addTerminal("c");
		cfg3.addTerminal("d");
		
		cfg3.addRule("S", "Sab");
		cfg3.addRule("S", "cd");
		
		System.out.println("--------- Before eliminating left recursion: ---------");
		cfg3.printGrammar();
		
		eliminateLeftRecursion(cfg3);
		
		System.out.println("--------- After eliminating left recursion: ---------");
		cfg3.printGrammar();
		
		System.out.println("-----------------------------------------------------------------------------------------");
		
		CFG cfg4 = new CFG();
		
		cfg4.addNonTerminal("S");
		
		cfg4.addTerminal("u");
		cfg4.addTerminal("*");
		cfg4.addTerminal("(");
		cfg4.addTerminal(")");
		cfg4.addTerminal("a");
		
		cfg4.addRule("S", "SuS");
		cfg4.addRule("S", "SS");
		cfg4.addRule("S", "S*");
		cfg4.addRule("S", "(S)");
		cfg4.addRule("S", "a");
		
		System.out.println("--------- Before eliminating left recursion: ---------");
		cfg4.printGrammar();
		
		eliminateLeftRecursion(cfg4);
		
		System.out.println("--------- After eliminating left recursion: ---------");
		cfg4.printGrammar();
		
		System.out.println("-----------------------------------------------------------------------------------------");
		
		CFG cfg5 = new CFG();
		
		cfg5.addNonTerminal("REXPR");
		cfg5.addNonTerminal("RTERM");
		cfg5.addNonTerminal("RFACTOR");
		cfg5.addNonTerminal("RPRIMARY");
		
		cfg5.addTerminal("u");
		cfg5.addTerminal("*");
		cfg5.addTerminal("a");
		cfg5.addTerminal("b");
		
		cfg5.addRule("REXPR", "REXPRuRTERM");
		cfg5.addRule("REXPR", "RTERM");
		cfg5.addRule("RTERM", "RTERMRFACTOR");
		cfg5.addRule("RTERM", "RFACTOR");
		cfg5.addRule("RFACTOR", "RFACTOR*");
		cfg5.addRule("RFACTOR", "RPRIMARY");
		cfg5.addRule("RPRIMARY", "a");
		cfg5.addRule("RPRIMARY", "b");
		
		System.out.println("--------- Before eliminating left recursion: ---------");
		cfg5.printGrammar();
		
		eliminateLeftRecursion(cfg5);
		
		System.out.println("--------- After eliminating left recursion: ---------");
		cfg5.printGrammar();
		
		System.out.println("-----------------------------------------------------------------------------------------");
		
		CFG cfg6 = new CFG();
		
		cfg6.addNonTerminal("A");
		cfg6.addNonTerminal("T");
		
		cfg6.addTerminal("0");
		cfg6.addTerminal("1");
		
		cfg6.addRule("A", "0");
		cfg6.addRule("A", "T1");
		cfg6.addRule("T", "1");
		cfg6.addRule("T", "A0");
		
		System.out.println("--------- Before eliminating left recursion: ---------");
		cfg6.printGrammar();
		
		eliminateLeftRecursion(cfg6);
		
		System.out.println("--------- After eliminating left recursion: ---------");
		cfg6.printGrammar();
		
		System.out.println("-----------------------------------------------------------------------------------------");
		
		CFG cfg7 = new CFG();
		
		cfg7.addNonTerminal("A");
		cfg7.addNonTerminal("B");
		cfg7.addNonTerminal("C");
		
		cfg7.addTerminal("a");
		cfg7.addTerminal("b");
		
		cfg7.addRule("A", "BC");
		cfg7.addRule("B", "Bb");
		cfg7.addRule("B", "$");
		cfg7.addRule("C", "AC");
		cfg7.addRule("C", "a");
		
		System.out.println("--------- Before eliminating left recursion: ---------");
		cfg7.printGrammar();
		
		eliminateLeftRecursion(cfg7);
		
		System.out.println("--------- After eliminating left recursion: ---------");
		cfg7.printGrammar();
		
		System.out.println("-----------------------------------------------------------------------------------------");
	}
}
