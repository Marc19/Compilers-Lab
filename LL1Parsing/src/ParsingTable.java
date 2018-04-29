import java.util.ArrayList;

public class ParsingTable 
{
	ArrayList<ParsingTableEntry> entries;
	
	public ParsingTable()
	{
		this.entries = new ArrayList<>();
	}
	
	public void addEntry(ParsingTableEntry entry)
	{
		this.entries.add(entry);
	}

	public void printParsingTable()
	{
		for(int i=0; i<entries.size(); i++)
		{
			System.out.println(entries.get(i));
		}
	}
}
