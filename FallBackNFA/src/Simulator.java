import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Simulator 
{
	public static void readData(String fileName) throws IOException
	{
		FileReader file = new FileReader("src/"+fileName);
		BufferedReader br = new BufferedReader(file);
		
		while(true)
		{
			String statesLine = br.readLine();
			String goalStateLine = br.readLine();
			String actionsLine = br.readLine();
			String vocabularyLine = br.readLine();
			String startStateLine = br.readLine();
			String transitionsLine = br.readLine();
			String inputLine = br.readLine();
			String emptyLine = br.readLine();
		
			if(statesLine == null || goalStateLine == null || actionsLine == null || vocabularyLine == null || startStateLine == null  ||
			   transitionsLine == null || inputLine == null )
			{
				return;
			}
			
			ArrayList<String> states = new ArrayList<>(Arrays.asList(statesLine.split(",")));
			ArrayList<String> goalStates = new ArrayList<>(Arrays.asList(goalStateLine.split(",")));
			ArrayList<String> actions = new ArrayList<>(Arrays.asList(actionsLine.split(",")));
			ArrayList<String> vocabulary = new ArrayList<>(Arrays.asList(vocabularyLine.split(",")));
			String startState = startStateLine;
			String transitions = transitionsLine;
			String input = inputLine;
			
			//boolean isValid = validate(states, goalStates, vocabulary, startState, transitions);
			
			//if(!isValid)
			//	continue;
			
			//processDFA(states, goalStates, actions, vocabulary, startState, transitions, input);
			processNFA(states, goalStates, actions, vocabulary, startState, transitions, input);
			
		}
	}

	private static void processDFA(ArrayList<String> states, ArrayList<String> goalStates, ArrayList<String> actions, ArrayList<String> vocabulary,
								   String startState, String transitions, String input) 
	{
		//boolean isValid = validate(states, goalStates, vocabulary, startState, transitions);
		
		//if(!isValid)
		//	return;
		
		System.out.println("FBDFA constructed");
		DFA dfa = new DFA();
		for(int i=0; i<states.size(); i++)
		{
			dfa.addState(states.get(i));
		}
		
		for(int i=0; i<vocabulary.size(); i++)
		{
			dfa.addStringToVocab(vocabulary.get(i));
		}
		
		for(int i=0,j=0; i<states.size(); i++)
		{
			String actionName = "";
			
			String stateName = states.get(i);
			boolean isStart = false;
			boolean isGoal = false;
			
			if(goalStates.contains(stateName))
			{
				isGoal = true;
				actionName = actions.get(j);
				j++;
			}
			
			if(startState.equals(stateName))
				isStart = true;
			
			dfa.updateState(stateName, actionName, isStart, isGoal, transitions);
		}
		
		traceFallBackDFA(dfa, input);
		System.out.println("-----------------------------------------------------------");
		
	}

	private static void traceFallBackDFA(DFA dfa, String input) 
	{
		ArrayList<String> vocabulary = dfa.getVocabulary();
		ArrayList<String> inputArray = new ArrayList<>(Arrays.asList(input.split("#")));
		
		for(int i=0; i<inputArray.size(); i++)
		{
			String in = inputArray.get(i);
			ArrayList<String> inArray = new ArrayList<>(Arrays.asList(in.split(",")));
			
			int start = 0;
			int end = inArray.size();
			
			while(true)
			{
				if( start == inArray.size() )
				{
					System.out.println();
					break;
				}
				
				if(start == end)
				{
					System.out.print("<Error,'" + fromListToString(inArray.subList(start,inArray.size()))  +"'>");
					System.out.println();
					break;
				}
				
				String currentInput = fromListToString(inArray.subList(start, end));
				State resultState = traceDFAOneInput(dfa, currentInput , vocabulary);
				
				if(resultState.isGoal())
				{
					System.out.print("<" + resultState.getAction() + ", '" + currentInput + "'>");
					start = end;
					end = inArray.size();
					continue;
				}
				
				if(start > end)
				{
					System.out.println();
					break;
				}
				
				end--;
			}
		}
	}

	private static String fromListToString(List<String> list) 
	{
		String result = "";
		
		for(int i=0; i<list.size(); i++)
		{
			result += (i>0?",":"") + list.get(i);
		}
		
		return result;
	}

	private static State traceDFAOneInput(DFA dfa, String in, ArrayList<String> vocabulary) 
	{
		State currentState = dfa.getStartState();
		String[] ins = in.split(",");
		
		//boolean validInput = true;
		for(int j=0; j<ins.length; j++)
		{
			String str = ins[j];
			
			if(!vocabulary.contains(str))
			{   
				System.out.println("Invalid input string " + str);
			//	validInput = false;
				return null;
			}
			currentState = currentState.getTransitions().get(str);				
		}
		
//		if(!validInput)
//			continue;
		
//		if(currentState.isGoal())
//			System.out.println("Accepted");
//		else
//			System.out.println("Rejected");
		
		return currentState;
	}

	private static boolean validate(ArrayList<String> states, ArrayList<String> goalStates,
			ArrayList<String> vocabulary, String startState, String transitions) 
	{
		boolean result = true;
		ArrayList<String> transitionsArr = new ArrayList<>(Arrays.asList(transitions.split("#")));
		
		if(!states.contains(startState))
		{
			System.out.println("Invalid start state " + startState);
			result = false;
		}
		
		for(int i=0; i<goalStates.size(); i++)
		{
			if(goalStates.size() == 1 && goalStates.get(0).equals(""))
				break;
			
			if(!states.contains(goalStates.get(i)))
			{
				System.out.println("Invalid accept state " + goalStates.get(i)) ;
				result = false;
			}
		}
		
		for(int i=0; i<transitionsArr.size(); i++)
		{
			if(transitionsArr.get(i).split(",").length != 3)
			{
				System.out.println(transitionsArr.get(i).split(",").length < 3?
						"Incomplete transition " + transitionsArr.get(i) : 
						"Invalid transition '"+ transitionsArr.get(i) +"' Weird number of arguments");
				result = false;
				continue;
			}
			
			String from = transitionsArr.get(i).split(",")[0];
			String to = transitionsArr.get(i).split(",")[1];
			String str = transitionsArr.get(i).split(",")[2];
			
			if(!states.contains(from))
			{
				System.out.println("Invalid transition " + transitionsArr.get(i) +". State:" + from + " is an INVALID state");
				result = false;
			}
			if(!states.contains(to))
			{
				System.out.println("Invalid transition " + transitionsArr.get(i) +". State:" + to + " is an INVALID state");
				result = false;
			}
			if(!vocabulary.contains(str) && ! str.equals("$"))
			{
				System.out.println("Invalid transition " + transitionsArr.get(i) +". Input '" + str + "' " + "is not in the alphabet");
				result = false;
			}
		}
		
		//// UNCOMMENT THIS PART IN CASE OF VALIDATING A DFA ////
/////////////////////////////////////////////////////////////////////////////////////		
//		for(int i=0; i<states.size(); i++)
//		{
//			String stateName = states.get(i);
//			
//			for(int j=0; j<vocabulary.size(); j++)
//			{
//				String aVocab = vocabulary.get(j);
//				boolean foundTransition = false;
//				
//				for(int k=0; k<transitionsArr.size(); k++)
//				{
//					String from = transitionsArr.get(k).split(",")[0];
//					String str = transitionsArr.get(k).split(",")[2];
//					
//					if(from.equals(stateName) && aVocab.equals(str))
//					{
//						foundTransition = true;
//						break;
//					}
//				}
//				
//				if(!foundTransition)
//				{
//					System.out.println("Missing transition for state " + stateName);
//					System.out.println("-----------------------------------------------------------");
//					result = false;
//				}
//			}
//		}
		
		if(result == false)
		{
			System.out.println("DFA Construction skipped and inputs are ignored");
			System.out.println("-----------------------------------------------------------");
		}
		return result;
	}
	
	public static DFA fromNFAtoDFA(NFA nfa, String transitions, ArrayList<String> goalsPrioritized)
	{
		ArrayList<String> transitionsArr = new ArrayList<>(Arrays.asList(transitions.split("#")));
		
		DFA dfa = new DFA();
		dfa.setVocabulary(nfa.getVocabulary());
		
		State deadState = createDeadState(nfa.getVocabulary());
		dfa.addState(deadState);
		boolean deadStateReachable = false;
		
		State startState = nfa.getStartState();
		State newStartState = getEpsilonClosure(nfa, startState, transitionsArr);
		for(int j=0; j<goalsPrioritized.size(); j++)
		{
			if(iHaveThisState(newStartState.getStateName(), goalsPrioritized.get(j)))
			{
				newStartState.setAction(nfa.getState(goalsPrioritized.get(j)).getAction());
				break;
			}
			
		}
		newStartState.setStateName(orderStateName(newStartState.getStateName()));
		
		dfa.addState(newStartState); //loop over states of the dfa 
		
		ArrayList<State> unhandledStates = new ArrayList<>();
		unhandledStates.add(newStartState);
		
		while(true)
		{
			State s = unhandledStates.remove(0);
			
			String[] statesString = s.getStateName().split("-");
			ArrayList<String> alphabet = nfa.getVocabulary();
			
			for(int i=0; i<alphabet.size(); i++)
			{
				State newState = new State();
				for(int j=0; j<statesString.length; j++)
				{ 
					State aState = nfa.getState(statesString[j]); //get transitions from this
					
					if(aState.getTransitions().containsKey(alphabet.get(i)))
					{
						String [] newReachableStates = aState.getTransitions().get(alphabet.get(i)).getStateName().split("-");
						
						for(int k=0; k<newReachableStates.length; k++)
						{
							if(!(iHaveThisState(newState.getStateName(),newReachableStates[k])))
							{
								newState.setStateName(newState.getStateName() + "-" + newReachableStates[k]);
								
								if(nfa.getState(newReachableStates[k]).isStart())
									newState.setStart(true);
									
								if(nfa.getState(newReachableStates[k]).isGoal())
									newState.setGoal(true);
							}	
						}
					}
				}
				
				newState = getEpsilonClosure(nfa, newState, transitionsArr);
				newState.setStateName(newState.getStateName().length()>0? newState.getStateName().substring(1):"");
				newState.setStateName(orderStateName(newState.getStateName()));
				newState.setAction("");
				for(int j=0; j<goalsPrioritized.size(); j++)
				{
					if(iHaveThisState(newState.getStateName(), goalsPrioritized.get(j)))
					{
						newState.setAction(nfa.getState(goalsPrioritized.get(j)).getAction());
						break;
					}
					
				}
//				for(int j=0; j<newState.getStateName().split("-").length; j++)
//				{
//					if(newState.getStateName().isEmpty()) //dead
//						break;
//					if(!(nfa.getState(newState.getStateName().split("-")[j]).getAction().isEmpty()))
//					{
//						newState.setAction(nfa.getState(newState.getStateName().split("-")[j]).getAction());
//						break;
//					}
//				}				
				
				if(newState.getStateName().equals("")) //dead state
				{
					s.addTransition(alphabet.get(i), deadState);
					deadStateReachable = true;
					continue;
				}
				
				if( dfa.getState(newState.getStateName()) == null ) // DFA doesn't contain this state
				{
					dfa.addState(newState);
					s.addTransition(alphabet.get(i), newState);
					
					unhandledStates.add(newState);
				}
				else
				{
					s.addTransition(alphabet.get(i), dfa.getState(newState.getStateName()));
				}
			}
			
			if(unhandledStates.isEmpty())
				break;
		}
		
		if(!deadStateReachable)
			dfa.getStates().remove(deadState);
		
		return dfa;
	}
	
	private static boolean iHaveThisState(String bigState, String oneState)
	{
		String[] states = bigState.split("-");
		
		for(int i=0; i<states.length; i++)
		{
			if(states[i].equals(oneState))
				return true;
		}
		
		return false;
	}

	private static String orderStateName(String stateName) 
	{
		String result = "";
		ArrayList<String> statesArr = new ArrayList<>(Arrays.asList(stateName.split("-")));
		
		java.util.Collections.sort(statesArr);
		
		for(int i=0; i<statesArr.size(); i++)
			result += (i>0?"-":"") + statesArr.get(i);
		
		return result;
	}

	private static State createDeadState(ArrayList<String> vocabulary) 
	{
		State s = new State("dead");
		s.setAction("");
		
		for(int i=0; i<vocabulary.size(); i++)
			s.addTransition(vocabulary.get(i), s);
		
		return s;
	}

	private static State getEpsilonClosure(NFA nfa, State state, ArrayList<String> transitionsArr)
	{
		State newState = state.copy();
		
		String oldStateName = newState.getStateName();
		while(true)
		{
			for(int i=0; i<transitionsArr.size(); i++)
			{
				String from = transitionsArr.get(i).split(",")[0];
				String to = transitionsArr.get(i).split(",")[1];
				String str = transitionsArr.get(i).split(",")[2];
				
				if(! (iHaveThisState(newState.getStateName(), from)))
					continue;				
				
				if( str.equals("$") && !(iHaveThisState(newState.getStateName(), to)) )
				{
					newState.setStateName(newState.getStateName() + "-" + to);
					
					if(nfa.getState(to).isStart())
						newState.setStart(true);
					
					if(nfa.getState(to).isGoal())
						newState.setGoal(true);
				}
			}
			
			if(newState.getStateName().equals(oldStateName))
				break;
			
			oldStateName = newState.getStateName();
		}
		
		return newState;
	}

	private static void processNFA(ArrayList<String> states, ArrayList<String> goalStates, 
								   ArrayList<String> actions, ArrayList<String> vocabulary,
								   String startState, String transitions, String input) 
	{
		NFA nfa = new NFA();
		for(int i=0; i<states.size(); i++)
		{
			nfa.addState(states.get(i));
		}
		
		for(int i=0; i<vocabulary.size(); i++)
		{
			nfa.addStringToVocab(vocabulary.get(i));
		}
		
		for(int i=0; i<states.size(); i++)
		{
			String actionName = "";
			
			String stateName = states.get(i);
			boolean isStart = false;
			boolean isGoal = false;
			
			if(goalStates.contains(stateName))
				isGoal = true;
							
			if(startState.contains(stateName))
				isStart = true;
			
			nfa.updateState(stateName, actionName, isStart, isGoal, transitions);
		}
		
		for(int i=0; i<goalStates.size(); i++)
		{
			State goalState = nfa.getState(goalStates.get(i));
			goalState.setAction(actions.get(i));
		}
		
		DFA dfa = fromNFAtoDFA(nfa, transitions, goalStates);
		
		printFBDFA(dfa, input);
		traceFallBackDFA(dfa, input);
		System.out.println("---------------------------------------------------------");
		System.out.println();
	}
	
	private static void printFBDFA(DFA dfa, String inputLine)
	{
		System.out.println("FBNFA constructed");
		System.out.println("Equivalent FBDFA:");
		
		String statesLine = "";
		for(int i=0; i<dfa.getStates().size(); i++)
		{
			State s = dfa.getStates().get(i);
			String sName = new String(s.getStateName());
			sName = sName.replaceAll("-", "*");
			statesLine += "," + sName;
		}
		System.out.print(statesLine.length()>0?statesLine.substring(1):""); System.out.println();
		
		String goalLine = "";
		String actionsLine = "";
		for(int i=0; i<dfa.getStates().size(); i++)
		{
			State s = dfa.getStates().get(i);
			
			if(s.isGoal())
			{
				String sName = new String(s.getStateName());
				sName = sName.replaceAll("-", "*");
				goalLine += "," + sName;
				
				String actionName = new String(s.getAction());
				actionsLine += "," + actionName;
			}
			
		}
		System.out.print(goalLine.length()>0?goalLine.substring(1):""); System.out.println();
		System.out.print(actionsLine.length()>0?actionsLine.substring(1):""); System.out.println();
		
		for(int i=0; i<dfa.getVocabulary().size(); i++)
			System.out.print((i>0?",":"") + dfa.getVocabulary().get(i));
		System.out.println();
		
		System.out.println(dfa.getStartState().getStateName().replaceAll("-", "*"));
		
		String transitionsLine = "";
		for(int i=0; i<dfa.getStates().size(); i++)
		{
			State s = dfa.getStates().get(i);
			
			for(int j=0; j<dfa.getVocabulary().size(); j++)
				transitionsLine += (i>0||j>0?"#":"") + s.getStateName().replaceAll("-", "*") + "," +
						s.getTransitions().get(dfa.getVocabulary().get(j)).getStateName().replaceAll("-", "*") +
						"," + dfa.getVocabulary().get(j);						
			
		}
		System.out.println(transitionsLine);
		
		System.out.println(inputLine);
		
		System.out.println("FBDFA constructed");
	}
	
	public static void main(String[] args) 
	{
		try 
		{
			readData("in9.in");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
 
}
