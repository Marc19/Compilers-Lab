import java.util.ArrayList;
import java.util.HashMap;

public class DFA 
{
	ArrayList<State> states;
	ArrayList<String> vocabulary;
	
	public DFA()
	{
		this.states = new ArrayList<>();
		this.vocabulary = new ArrayList<>();
	}
	
	public void addState(String name)
	{
		State s = new State(name);
		this.states.add(s);
	}
	
	public void addState(State state)
	{
		this.states.add(state);
	}
	
	public void updateState(String stateName, boolean isStart, boolean isGoal, String transitions)
	{
		State theState = null;
		for(int i=0; i<this.states.size(); i++)
		{
			if(this.states.get(i).getStateName().equals(stateName))
			{
				theState = states.get(i);
				break;
			}
		}
		
		if( theState == null)
			return;
		
		theState.setStart(isStart);
		theState.setGoal(isGoal);
		
		String[] transitionsArr = transitions.split("#");
		
		for(int i=0; i<transitionsArr.length; i++)
		{
			String[] transition = transitionsArr[i].split(",");
			String from = transition[0];
			String to = transition [1];
			String input = transition[2];
			
			State destintationState = getState(to);
			
			if(!from.equals(stateName))
				continue;
			
			theState.addTransition(input, destintationState);
		}
		
		this.states.add(theState);
	}
	
	public State getState(String stateName) 
	{
		for(int i=0 ; i<this.states.size(); i++)
		{
			if(states.get(i).getStateName().equals(stateName))
				return states.get(i);
		}
		
		return null;
	}

	public void addStringToVocab(String str)
	{
		this.vocabulary.add(str);
	}
	
	public ArrayList<State> getStates() 
	{
		return states;
	}

	public void setStates(ArrayList<State> states)
	{
		this.states = states;
	}

	public ArrayList<String> getVocabulary()
	{
		return vocabulary;
	}

	public void setVocabulary(ArrayList<String> vocabulary) 
	{
		this.vocabulary = vocabulary;
	}
	
	public String toString()
	{
		String vocab = this.vocabulary.toString();
		String statesStr = "";
		for(int i=0; i<states.size(); i++)
		{
			statesStr += states.get(i) + "\n";
		}
		
		return "States: [" + statesStr + "]\n" + "Vocabulary: " + vocab;
	}

	public State getStartState()
	{
		for(int i=0; i<this.states.size(); i++)
		{
			if(states.get(i).isStart())
				return states.get(i);
		}
		return null;
	}
}
