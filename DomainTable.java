import java.util.ArrayList;
import java.util.List;

//Maximum 32 attributes

public final class DomainTable {
	private List<Character> attNames;
	private List<Integer> attIndices;
	private int index;

	public DomainTable() {
		attNames = new ArrayList<>();
		attIndices = new ArrayList<>();
		index = 0;
	}

	public DomainTable(char[] initAtts) {
		attNames = new ArrayList<>(initAtts.length);
		attIndices = new ArrayList<>(initAtts.length);
		int i = 0;
		int val = 0;
		for (; i < initAtts.length; i++) {
			val = 1 << i;
			attNames.add(initAtts[i]);
			attIndices.add(val);
		}
		index = i;
	}

	/*public void setNewNames(List<String> names) {
		if (names == null) {
			return;
		}
		attNames.clear();
		attIndices.clear();
		int i = 0;
		int val = 0;
		for (; i < names.size(); i++) {
			val = 1 << i;
			attNames.add(names.get(i));
			attIndices.add(val);
		}
		index = i;
		notifyListeners();
	}

	public void addListener(DomainTableListener l) {
		if(l != null)
			listeners.add(l);
	}

	public void removeListener(DomainTableListener l) {
		if(l != null)
			listeners.remove(l);
	}*/

	public boolean addAtt(char attName) {
		if(index >= 32) return false;
		int val = 1 << index;
		//if(containsNameCanseInsensitive(attName)) return false;
		attNames.add(attName);
		attIndices.add(val);
		index++;
		//notifyListeners();
		return true;
	}

	public boolean removeAtt(char attName) {
		if(index < 1) return false;
		int i = indexNameCaseInsensitive(attName);
		if(i >= 0) {
			attNames.remove(i);
			attIndices.remove(i);
			index--;
			//notifyListeners();
			return true;
		}
		return false;
	}

	/*public void loadDomainTable(DomainTable that) {
		attNames.clear();
		attIndices.clear();
		index = 0;
		this.attNames = that.attNames;
		this.attIndices = that.attIndices;
		this.index = that.index;
		notifyListeners();
	}*/

	/*public void clearData() {
		attNames.clear();
		attIndices.clear();
		index = 0;
		notifyListeners();
	}*/

	/*public boolean renameAtt(String oldAttName, String newAttName) {
		if(index < 1) return false;
		int i = indexNameCaseInsensitive(oldAttName);
		if(i >= 0) {
			attNames.remove(i);
			attNames.add(i, newAttName);
			notifyListeners();
			return true;
		}
		return false;
	}*/

	public int getAttIndex(char name) {
		int i = indexNameCaseInsensitive(name);
		if(i >= 0) {
			return attIndices.get(i);
		} else {
			return 0;
		}
	}

	public char getAttName(int attIndex) {
		int i = attIndices.indexOf(attIndex);
		if(i >= 0) {
			return attNames.get(i);
		} else {
			return 0;
		}
	}

	public boolean containsAttIndex(int attIndex) {
		int i = attIndices.indexOf(attIndex);
		return i >= 0;
	}

	public int size() {
		return index;
	}


	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < attNames.size(); i++) {
			sb.append(attNames.get(i));
			sb.append('\t');
			sb.append(attIndices.get(i));
			sb.append('\n');
		}
		return sb.toString();
	}

	public char[] getAttNames() {
		char[] result = new char[attNames.size()];
		for(int i=0;i<attNames.size();i++)
		{
			result[i]=attNames.get(i);
		}
		return result;
	}

	public Integer[] getAttMasks() {
		Integer[] result = new Integer[attIndices.size()];
		return attIndices.toArray(result);
	}

	public int getAttIndicesAsInteger() {
		int result = 0;
		for (Integer l : attIndices) {
			result |= l;
		}
		return result;
	}

	public AttributeSet createAttributeSet() {
		AttributeSet result = new AttributeSet(this);
		result.setMask(getAttIndicesAsInteger());
		return result;
	}

	private int indexNameCaseInsensitive(char name) {
		if (name == 0) return -1;
		int i = 0;
		for (char attName : attNames) {
			if(attName == name) {
				return i;
			}
			i++;
		}
		return -1;
	}

	/*private boolean containsNameCanseInsensitive(String name) {
		return indexNameCaseInsensitive(name) != -1;
	}


	private void notifyListeners() {
		for (DomainTableListener l : listeners) {
			l.onDomainChange();
		}
	}*/
}