package assignment2;

public class Shelf {
	
	protected int height;
	protected int availableLength;
	protected int totalLength;
	protected Box firstBox;
	protected Box lastBox;

	public Shelf(int height, int totalLength){
		this.height = height;
		this.availableLength = totalLength;
		this.totalLength = totalLength;
		this.firstBox = null;
		this.lastBox = null;
	}
	
	protected void clear(){
		availableLength = totalLength;
		firstBox = null;
		lastBox = null;
	}
	
	public String print(){
		String result = "( " + height + " - " + availableLength + " ) : ";
		Box b = firstBox;
		while(b != null){
			result += b.id + ", ";
			b = b.next;
		}
		result += "\n";
		return result;
	}
	
	/**
	 * Adds a box on the shelf. Here we assume that the box fits in height and length on the shelf.
	 * @param b
	 */
	public void addBox(Box b){
		
		if (firstBox == null)
		{
			firstBox = b;
			lastBox = b;
			b.next = null;
			b.previous = null;
		}
		else
		{
			lastBox.next = b;
			b.previous = lastBox;
			b.next = null;
			lastBox = b;
		}
		availableLength -= lastBox.length;
		
	}
	
	/**
	 * If the box with the identifier is on the shelf, remove the box from the shelf and return that box.
	 * If not, do not do anything to the Shelf and return null.
	 * @param identifier
	 * @return
	 */
	public Box removeBox(String identifier){
		
		//empty
		if (firstBox == null)
		{
			return null;
		}
		
		Box searchBox = firstBox;
		while (searchBox.id != identifier) //if not true, means first box 
		{
			if (searchBox.next == null) //we've looked through everything
			{
				return null;
			}
			searchBox = searchBox.next; //iterate through 
			
			//it will stop when searchBox matches the id, now we just check cases
		}
		
		//checking each case 
		//1 box
		if(firstBox == lastBox && searchBox == firstBox)
		{
			firstBox = null;
			lastBox = null;
		}
		//its the first only
		else if (searchBox == firstBox)
		{
			searchBox.next.previous = null;
			firstBox = searchBox.next;
		}
		//its just the last box
		else if (lastBox == searchBox)
		{
			searchBox.previous.next = null;
			lastBox = searchBox.previous;
		}
		//if its any box in the middle
		else
		{
			searchBox.next.previous = searchBox.previous;
			searchBox.previous.next = searchBox.next;
		}
		availableLength += searchBox.length;
		searchBox.previous = null;
		searchBox.next = null;
		return searchBox;
	}

	
}

