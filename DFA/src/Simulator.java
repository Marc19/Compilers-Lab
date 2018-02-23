import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
			String vocabularyLine = br.readLine();
			String startStateLine = br.readLine();
			String transitionsLine = br.readLine();
			String inputLine = br.readLine();
			String emptyLine = br.readLine();
		
			if(statesLine == null || goalStateLine == null || vocabularyLine == null || startStateLine == null  ||
			   transitionsLine == null || inputLine == null )
			{
				return;
			}
			
			ArrayList<String> states = new ArrayList<>(Arrays.asList(statesLine.split(",")));
			ArrayList<String> goalStates = new ArrayList<>(Arrays.asList(goalStateLine.split(",")));
			ArrayList<String> vocabulary = new ArrayList<>(Arrays.asList(vocabularyLine.split(",")));
			String startState = startStateLine;
			String transitions = transitionsLine;
			String input = inputLine;
			
			processDFA(states, goalStates, vocabulary, startState, transitions, input);
		}
	}

	private static void processDFA(ArrayList<String> states, ArrayList<String> goalStates, ArrayList<String> vocabulary,
			String startState, String transitions, String input) 
	{
		boolean isValid = validate(states, goalStates, vocabulary, startState, transitions);
		
		if(!isValid)
			return;
		
		System.out.println("DFA constructed");
		DFA dfa = new DFA();
		for(int i=0; i<states.size(); i++)
		{
			dfa.addState(states.get(i));
		}
		
		for(int i=0; i<states.size(); i++)
		{
			String stateName = states.get(i);
			boolean isStart = false;
			boolean isGoal = false;
			
			if(goalStates.contains(stateName))
				isGoal = true;
			
			if(startState.equals(stateName))
				isStart = true;
			
			dfa.updateState(stateName, isStart, isGoal, transitions);
		}
		
		traceDFA(dfa, input, vocabulary);
		System.out.println("-----------------------------------------------------------");
		
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

		for(int i=0; i<states.size(); i++)
		{
			String stateName = states.get(i);
			
			for(int j=0; j<vocabulary.size(); j++)
			{
				String aVocab = vocabulary.get(j);
				boolean foundTransition = false;
				
				for(int k=0; k<transitionsArr.size(); k++)
				{
					String from = transitionsArr.get(k).split(",")[0];
					String str = (transitionsArr.get(k).split(",").length>2)?transitionsArr.get(k).split(",")[2]:null;
					
					if(str == null)
						continue;
					
					if(from.equals(stateName) && aVocab.equals(str))
					{
						foundTransition = true;
						break;
					}
				}
				
				if(!foundTransition)
				{
					System.out.println("Missing transition for state " + stateName);
					result = false;
				}
			}
		}
		
		if(result == false)
		{
			System.out.println("DFA Construction skipped and inputs are ignored");
			System.out.println("-----------------------------------------------------------");
		}
		return result;
	}

	private static void traceDFA(DFA dfa, String input, ArrayList<String> vocabulary)
	{
		ArrayList<String> inputArray = new ArrayList<>(Arrays.asList(input.split("#")));
				
		for(int i=0; i<inputArray.size(); i++)
		{
			State currentState = dfa.getStartState();
			
			String in = inputArray.get(i);
			String[] ins = in.split(",");
			
			boolean validInput = true;
			for(int j=0; j<ins.length; j++)
			{
				String str = ins[j];
				
				if(!vocabulary.contains(str))
				{
					System.out.println("Invalid input string " + str);
					validInput = false;
					break;
				}
				
				currentState = currentState.getTransitions().get(str);				
			}
			
			if(!validInput)
				continue;
			
			if(currentState.isGoal())
				System.out.println("Accepted");
			else
				System.out.println("Rejected");
		}
	}

	public static void main(String[] args) 
	{
		try 
		{
			readData("in1.in");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

}
