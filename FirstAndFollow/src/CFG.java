import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CFG 
{
	ArrayList<String> nonTerminals; // the start variable should be the first element of the list
	ArrayList<String> terminals; // terminals start with a small letter while non-terminals start with a capital letter
	HashMap<String, String> rules; //key: nonterminals, value: sentential forms separated by ','. Epsilon is 'epsilon'
	
	HashMap<String, String> firsts = new HashMap<String, String>(); //key: nonterminals, value: terminals
	HashMap<String, String> follows = new HashMap<String, String>();//key: nonterminals, value: terminals
	
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
	
	public void computeFirst()
	{				
		for(int i=0; i<terminals.size(); i++)
			firsts.put(terminals.get(i), terminals.get(i));
		
		for(int i=0; i<nonTerminals.size(); i++)
			firsts.put(nonTerminals.get(i), "");
		
		boolean change = true;
		while(change)
		{
			change = false;
			
			Iterator<Map.Entry<String, String>> it = rules.entrySet().iterator();
			while (it.hasNext())
			{
				Map.Entry<String, String> entry = it.next();
				String key = entry.getKey();
				String[] value = entry.getValue().split(",");
				
				for(int i=0; i<value.length; i++)
				{
					String[] Bk = value[i].split(" ");
					
					boolean rhsIsEpsilon = true;
					for(int j=0; j<Bk.length; j++)
					{
						if(!(Bk[j].equals("epsilon")) && !(l1Containsl2(firsts.get(Bk[j]), "epsilon")))
						{
							rhsIsEpsilon = false;
							break;
						}
					}
					
					if(rhsIsEpsilon)
					{
						if(! (l1Containsl2(firsts.get(key), "epsilon")))
						{
							firsts.put(key, (firsts.get(key).length()>0)? 
									firsts.get(key) + ",epsilon" : "epsilon");
							change = true;
						}
						
						for(int j=0; j<Bk.length; j++)
						{
							if(!(Bk[j].equals("epsilon")) && !(l1Containsl2(firsts.get(key), firsts.get(Bk[j]))))
							{
								String newToAppend = toAppendOnFirst(key, firsts.get(Bk[j]));
								
								firsts.put(key, (firsts.get(key).length()>0)?
										(firsts.get(key) + "," + newToAppend) : newToAppend);
								
								change = true;
							}
						}
					}
					else
					{
						for(int j=0; j<Bk.length; j++)
						{				
							boolean allPrecedingHaveEpsilons = true;
							for(int k=0; k<j; k++)
							{								
								if(!(l1Containsl2(firsts.get(Bk[k]),"epsilon")))
								{
									allPrecedingHaveEpsilons = false;
									break;
								}
							}
							
							if(j == 0 || allPrecedingHaveEpsilons)
							{
								if(!(l1Containsl2(firsts.get(key), firsts.get(Bk[j].replace("epsilon", "")))))
								{
									String newToAppend = toAppendOnFirst(key, firsts.get(Bk[j].replace("epsilon", "")));
									firsts.put(key, (firsts.get(key).length()>0)?
											   (firsts.get(key) + "," + newToAppend) : newToAppend);
									change = true;
								}
							}
						}
					}
				}
			}
		}
		
		printFirsts();
	}
	
	public void computeFollow()
	{
		for(int i=0; i<nonTerminals.size(); i++)
			follows.put(nonTerminals.get(i), "");
		follows.put(nonTerminals.get(0), "$");
		
		boolean change = true;
		while(change)
		{
			change = false;
			
			Iterator<Map.Entry<String, String>> it = rules.entrySet().iterator();
			while (it.hasNext())
			{
				Map.Entry<String, String> entry = it.next();
				String a = entry.getKey();
				String[] value = entry.getValue().split(",");
				
				for(int i=0; i<value.length; i++)
				{
					String[] sententialForm = value[i].split(" ");
					
					for(int j=0; j<sententialForm.length; j++)
					{
						String b = sententialForm[j];
						if(Character.isUpperCase(b.charAt(0)))
						{
							int k=j+1;
							while(true)
							{
								if(k >= sententialForm.length)
									break;
								
								//String smallList = (k)<sententialForm.length?firsts.get(sententialForm[k]).replace("epsilon", ""):"";
								String smallList = firsts.get(sententialForm[k]).replace("epsilon", "");
								if(smallList.startsWith(","))
									smallList = smallList.substring(1);
								if(smallList.endsWith(","))
									smallList = smallList.substring(0, smallList.length()-1);
								
								//if( (j+1)<sententialForm.length && !(l1Containsl2(follows.get(b), smallList)) )
								if( !(l1Containsl2(follows.get(b), smallList)) )
								{
									String newToAppend = toAppendOnFollow(b,firsts.get(sententialForm[k]).replace("epsilon", ""));
									follows.put(b, (follows.get(b).length()>0)?
											(follows.get(b) + "," + newToAppend) : newToAppend );
									change = true;
								}
								
								if( !(l1Containsl2(firsts.get(sententialForm[k]), "epsilon")) )
									break;
								
								//if( j == sententialForm.length-1 || l1Containsl2(firsts.get(sententialForm[j+1]), "epsilon"))
								if( k == sententialForm.length-1 )
								{
									if(!(l1Containsl2(follows.get(b),follows.get(a))))
									{
										String newToAppend = toAppendOnFollow(b,follows.get(a));
										follows.put(b, (follows.get(b).length()>0)?
												    (follows.get(b) + "," + newToAppend) : newToAppend );
										change = true;
									}
								}
								
								k++;
							}
							
							if( j == sententialForm.length-1 )
							{
								if(!(l1Containsl2(follows.get(b),follows.get(a))))
								{
									String newToAppend = toAppendOnFollow(b,follows.get(a));
									follows.put(b, (follows.get(b).length()>0)?
											    (follows.get(b) + "," + newToAppend) : newToAppend );
									change = true;
								}
							}
						}
					}
				}
			}
		}
		
		printFollows();
	}
	
	private String toAppendOnFollow(String key, String toAppend) 
	{
		String toAppendFiltered = "";
		
		String[] value = follows.get(key).split(",");
		String[] toAppendArr = toAppend.split(",");
		
		for(int i=0; i<toAppendArr.length; i++)
		{
			String newVal = toAppendArr[i];
			boolean foundIt = false;

			for(int j=0; j<value.length; j++)
			{
				String val = value[j];
				
				if(val.equals(newVal))
				{
					foundIt = true;
					break;
				}
			}
			
			if(!foundIt)
				toAppendFiltered += "," + newVal;
		}
		
		return (toAppendFiltered.length()>0)?toAppendFiltered.substring(1):"";
	}

	private String toAppendOnFirst(String key, String toAppend) 
	{
		String toAppendFiltered = "";
		
		String[] value = firsts.get(key).split(",");
		String[] toAppendArr = toAppend.split(",");
		
		for(int i=0; i<toAppendArr.length; i++)
		{
			String newVal = toAppendArr[i];
			boolean foundIt = false;

			for(int j=0; j<value.length; j++)
			{
				String val = value[j];
				
				if(val.equals(newVal))
				{
					foundIt = true;
					break;
				}
			}
			
			if(!foundIt)
				toAppendFiltered += "," + newVal;
		}
		
		return (toAppendFiltered.length()>0)?toAppendFiltered.substring(1):"";
	}
	
	private void printFirsts() 
	{
		Iterator<Map.Entry<String, String>> it = firsts.entrySet().iterator();

		while (it.hasNext())
		{
			Map.Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			
			System.out.println("First(" + key + ")= {" + value + "}");
		}
	}
	
	private void printFollows() 
	{
		Iterator<Map.Entry<String, String>> it = follows.entrySet().iterator();

		while (it.hasNext())
		{
			Map.Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			
			System.out.println("Follow(" + key + ")= {" + value + "}");
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
			System.out.println(nonTerminals.get(i) + " --> " + this.rules.get(nonTerminals.get(i)).replace(",", "|"));
			
			if(rules.containsKey(nonTerminals.get(i) + "'"))
				System.out.println(nonTerminals.get(i) + "'" + " --> " + this.rules.get(nonTerminals.get(i) + "'").replace(",", "|"));
		}
	}
	
	public static boolean l1Containsl2(String bigList, String smallList)
	{
		if(smallList.length() == 0)
			return true;
		
		String[] bigListArr = bigList.split(",");
		String[] smallListArr = smallList.split(",");
		
		for(int i=0; i<smallListArr.length; i++)
		{
			String smallElement = smallListArr[i];
			boolean foundIt = false;
			for(int j=0; j<bigListArr.length; j++)
			{
				String bigElement = bigListArr[j];
				if(bigElement.equals(smallElement))
				{
					foundIt = true;
					break;
				}
			}
			
			if(!foundIt)
				return false;
		}
		
		return true;
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