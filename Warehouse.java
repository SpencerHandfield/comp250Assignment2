package assignment2;

public class Warehouse{

	protected Shelf[] storage;
	protected int nbShelves;
	public Box toShip;
	public UrgentBox toShipUrgently;
	static String problem = "problem encountered while performing the operation";
	static String noProblem = "operation was successfully carried out";
	
	public Warehouse(int n, int[] heights, int[] lengths){
		this.nbShelves = n;
		this.storage = new Shelf[n];
		for (int i = 0; i < n; i++){
			this.storage[i]= new Shelf(heights[i], lengths[i]);
		}
		this.toShip = null;
		this.toShipUrgently = null;
	}
	
	public String printShipping(){
		Box b = toShip;
		String result = "not urgent : ";
		while(b != null){
			result += b.id + ", ";
			b = b.next;
		}
		result += "\n" + "should be already gone : ";
		b = toShipUrgently;
		while(b != null){
			result += b.id + ", ";
			b = b.next;
		}
		result += "\n";
		return result;
	}
	
 	public String print(){
 		String result = "";
		for (int i = 0; i < nbShelves; i++){
			result += i + "-th shelf " + storage[i].print();
		}
		return result;
	}
	
 	public void clear(){
 		toShip = null;
 		toShipUrgently = null;
 		for (int i = 0; i < nbShelves ; i++){
 			storage[i].clear();
 		}
 	}
 	
 	/**
 	 * initiate the merge sort algorithm
 	 */
	public void sort(){
		mergeSort(0, nbShelves -1);
	}
	
	/**
	 * performs the induction step of the merge sort algorithm
	 * @param start
	 * @param end
	 */
	protected void mergeSort(int start, int end){
		if (start < end)
		{
			int middle = (start + end)/2;
			
			mergeSort(start, middle);
			mergeSort(middle+1, end);
			merge(start, middle, end);
		}
	}
	
	/**
	 * performs the merge part of the merge sort algorithm
	 * @param start
	 * @param mid
	 * @param end
	 */
	protected void merge(int start, int mid, int end){
		
		//create temporary array of to sort (same size)
		//sorting the HEIGHT, storage is an array containing height and length
		//we only need one part of that
		
		int[] temporaryArray = new int[nbShelves];
		for (int i = start; i <= end; i++)
		{
			temporaryArray[i] = storage[i].height;
		}
		
		int i = start;
		int j = mid+1;
		int k = start;
		
		while (i <= mid && j <= end)
		{
			if (temporaryArray[i] <= temporaryArray[j])
			{
				storage[k].height = temporaryArray[i];
				i++;
			}else
			{
				storage[k].height = temporaryArray[j];
				j++;
			}
			k++;
		}
		while (i <=mid)
		{
			storage[k].height = temporaryArray[i];
			k++;
			i++;
		}
	}
	
	/**
	 * Adds a box is the smallest possible shelf where there is room available.
	 * Here we assume that there is at least one shelf (i.e. nbShelves >0)
	 * @param b
	 * @return problem or noProblem
	 */
	public String addBox (Box b){
		//iterate through all the shelves (which are sorted)
		//add to first one that respects height but also has available length
		//do the weird return they want	
		for (int i = 0; i < storage.length; i++)
		{
			if (storage[i].height >= b.height && storage[i].availableLength>=b.length)
			{
				storage[i].addBox(b);
				return noProblem;
			}
		}return problem;
	}
	
	/**
	 * Adds a box to its corresponding shipping list and updates all the fields
	 * @param b
	 * @return problem or noProblem
	 */
	public String addToShip (Box b){
		if(b instanceof UrgentBox)
		{
			if (toShipUrgently == null)
			{
				toShipUrgently = (UrgentBox) b;
			}else
			{
				b.next = toShipUrgently;
				toShipUrgently.previous = b;
				toShipUrgently = (UrgentBox) b;
			}
		}else if (b instanceof Box)
		{
			if (toShip == null)
			{
				toShip = b;
			}else
			{
				b.next = toShip;
				toShip.previous = b;
				toShip = b;
			}
		}else return problem;
		return noProblem;	
	}

	/**
	 * Find a box with the identifier (if it exists)
	 * Remove the box from its corresponding shelf
	 * Add it to its corresponding shipping list
	 * @param identifier
	 * @return problem or noProblem
	 */
	public String shipBox (String identifier){
		Box boxForShipping;
		int i = 0; 
		while ((boxForShipping = storage[i].removeBox(identifier)) == null)
		{
			 i++; //look at next shelf
			 if (i>=nbShelves) //>= because array starts at 0, wouldn't be able to attempt at = nb of shelves
			 {
				 return problem;
			 }
		}//automatically is while loop breaks means the box is the desired one so return it
		return addToShip(boxForShipping);
	}
	
	/**
	 * if there is a better shelf for the box, moves the box to the optimal shelf.
	 * If there are none, do not do anything
	 * @param b
	 * @param position
	 */
	public void moveOneBox (Box b, int position){
		//addbox always adds to the smallest height
		Box boxToMove = storage[position].removeBox(b.id);
		addBox(boxToMove);	
	}
		
	/**
	 * reorganize the entire warehouse : start with smaller shelves and first box on each shelf.
	 */
	public void reorganize (){
		int i = 0;
		Box orgBox;
		while (i<nbShelves)
		{
			orgBox = storage[i].firstBox;
			while(orgBox!=null)
			{
				Box next = orgBox.next; //must create temporary of the stationary
				moveOneBox(orgBox, i); //because if you move this
				orgBox = next; //then using a .next here would be completely different 
			}
			i++;
		}
	}
}
