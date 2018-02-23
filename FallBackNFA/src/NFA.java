import java.util.ArrayList;

public class NFA 
{
	private ArrayList<State> states;
	private ArrayList<String> vocabulary;
	
	public NFA()
	{
		this.states = new ArrayList<>();
		this.vocabulary = new ArrayList<>();
	}
	
	public void addState(String name)
	{
		State s = new State(name);
		this.states.add(s);
	}
	
	public void updateState(String stateName, String actionName, boolean isStart, boolean isGoal, String transitions)
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
		theState.setAction(actionName);
		
		String[] transitionsArr = transitions.split("#");
		
		for(int i=0; i<transitionsArr.length; i++)
		{
			String[] transition = transitionsArr[i].split(",");
			String from = transition[0];
			String to = transition [1];
			String input = transition[2];
			
			State destinationState = getState(to);
			
			if(!from.equals(stateName))
				continue;
			
			if( theState.getTransitions().containsKey(input) )
			{
				State oldStateCopy = theState.getTransitions().get(input).copy();
				
				oldStateCopy.setStateName(oldStateCopy.getStateName() + "-" + destinationState.getStateName());
				
				if(destinationState.isStart())
					oldStateCopy.setStart(true);
				
				if(destinationState.isGoal())
					oldStateCopy.setGoal(true);
				
//				for(int j=0; j<oldStateCopy.getStateName().split("-").length; j++)
//				{
//					State oneState = this.getState(oldStateCopy.getStateName().split("-")[j]);
//					
//					if(oneState.isGoal())
//					{
//						
//					}
//				}
				
				this.states.add(oldStateCopy);
				theState.getTransitions().remove(input);
				theState.getTransitions().put(input, oldStateCopy);
			}
			else
				theState.addTransition(input, destinationState);
		}
		
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
