package cs1501_p2;
import java.util.*;
import java.io.*;

public class AutoCompleter implements AutoComplete_Inter
{
	private DLB dlbTrie;
	private UserHistory rWayTrie;

	public AutoCompleter(String dict)
	{
		dlbTrie = new DLB();
		rWayTrie = new UserHistory();

		try
		{
			File dictFile = new File(dict);
			Scanner reader = new Scanner(dictFile);

			while (reader.hasNextLine())
			{
				String word = reader.nextLine();
				dlbTrie.add(word);
			}
			reader.close();
		}
		catch(FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}

	public AutoCompleter(String dict, String history)
	{
		dlbTrie = new DLB();
		rWayTrie = new UserHistory();

		try
		{
			File dictFile = new File(dict);
			Scanner reader = new Scanner(dictFile);

			while (reader.hasNextLine())
			{
				String word = reader.nextLine();
				dlbTrie.add(word);
			}
			reader.close();

			File userFile = new File(history);
			Scanner userReader = new Scanner(userFile);

			while (userReader.hasNextLine())
			{
				String userWord = userReader.nextLine();
				rWayTrie.add(userWord);
			}
			userReader.close();

		}
		catch(FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}

	public ArrayList<String> nextChar(char next)
	{
		ArrayList<String> returnList = new ArrayList<String>();
		//user history then dictionary of english words
		dlbTrie.searchByChar(next);
		if (rWayTrie.count() == 0)
		{
			return dlbTrie.suggest();
		}

		rWayTrie.searchByChar(next);
		ArrayList<String> tempList = rWayTrie.suggest();
		for (String user : tempList)
		{
			if (returnList.size() == 5) return returnList;
			returnList.add(user);
		}
		if (returnList.size() == 5) return returnList;
		ArrayList<String> tempDict = dlbTrie.suggest();

		for (String word : tempDict)
		{
			if (returnList.size() == 5) return returnList;
			if (returnList.contains(word)) continue;
			returnList.add(word);
		}

		return returnList;
	}

	public void finishWord(String cur)
	{
		rWayTrie.resetByChar();
		dlbTrie.resetByChar();
		rWayTrie.add(cur);
	}

	public void saveUserHistory(String fname)
	{
		ArrayList<String> traversal = rWayTrie.traverse();
		try 
		{
			FileWriter fileWriter = new FileWriter(fname);
			for (String word : traversal)
			{
				fileWriter.write(word + System.lineSeparator());
			}

			fileWriter.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		rWayTrie.resetByChar();
		dlbTrie.resetByChar();

	}
}