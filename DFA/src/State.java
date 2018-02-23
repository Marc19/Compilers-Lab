import java.util.HashMap;

public class State 
{
	String stateName;
	boolean isStart;
	boolean isGoal;
	HashMap<String, State> transitions;
	
	public State()
	{
		stateName = "";
		isStart = false;
		isGoal = false;
		transitions = new HashMap<>();
	}
	
	public State(String stateName)
	{
		this.stateName = stateName;
		isStart = false;
		isGoal = false;
		transitions = new HashMap<>();
	}
	
	public void updateStateName(String name)
	{
		this.stateName = name;
	}
	
	public void addTransition(String str, State destination)
	{
		this.transitions.put(str, destination);
	}
	
	public State copy()
	{
		State result = new State(this.stateName);
		result.isGoal = this.isGoal;
		result.isStart = this.isStart;
		result.transitions = (HashMap<String, State>) this.transitions.clone();
		
		return result;
	}
	
	public String getStateName() 
	{
		return stateName;
	}
	
	public void setStateName(String stateName)
	{
		this.stateName = stateName;
	}
	
	public boolean isStart() 
	{
		return isStart;
	}

	public void setStart(boolean isStart) 
	{
		this.isStart = isStart;
	}

	public boolean isGoal() 
	{
		return isGoal;
	}

	public void setGoal(boolean isGoal) 
	{
		this.isGoal = isGoal;
	}

	public HashMap<String, State> getTransitions() 
	{
		return transitions;
	}
	
	public String toString()
	{
		return this.stateName + " " + (isStart?"Start":"") + (isGoal?" Goal\n":"\n");// +
		//transitions.toString();
	}
}
