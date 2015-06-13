
public class QuadraticProbingHashTable<AnyType> {
	
	/**
	 * Default hash table constructor.
	 */
	public QuadraticProbingHashTable() {
		this(DEFAULT_TABLE_SIZE);
	}
	
	/**
	 * Construct the hash table.
	 * @param size approximate table size
	 */
	public QuadraticProbingHashTable(int size) {
		allocateArray(size);
		makeEmpty();
	}
	
	/**
	 * Make the hash table logically empty.
	 */
	public void makeEmpty() {
		currentSize = 0;
		for(int i = 0; i < array.length; i++)
			array[i] = null;
	}
	
	/**
	 * Find an item in the hash table.
	 * @param toCheck the item that will be searched for.
	 * @return if there is a matching item.
	 */
	public boolean contains(AnyType toCheck) {
		int currentPos = findPos(toCheck);
		return isActive(currentPos);
	}
	
	/**
	 * Insert into the hash table. If item is in the hash table do nothing.
	 * @param toInsert the item to be inserted.
	 */
	public void insert(AnyType toInsert) {
		int currentPos = findPos(toInsert);
		if(isActive(currentPos))
			return;
		
		array[currentPos] = new HashEntry<>(toInsert, true);
		
		//rehash
		if(++currentSize > array.length / 2)
			rehash();
	}
	
	/**
	 * Remove from the hash table.
	 * @param toRemove the item to be removed.
	 */
	public void remove(AnyType toRemove) {
		int currentPos = findPos(toRemove);
		if(isActive(currentPos))
			array[currentPos].isActive = false;
	}
	
	public static class HashEntry<AnyType> {
		public AnyType element;	//the element
		public boolean isActive; //if item in pos.
		
		public HashEntry(AnyType hashEntry) {
			this(hashEntry, true);
		}
		
		public HashEntry(AnyType hashEntry, boolean i) {
			element = hashEntry;
			isActive = i;
		}
	}
	
	private static final int DEFAULT_TABLE_SIZE = 11;	//Default hash table size.
	
	private HashEntry<AnyType> [] array;				//The array of elements.
	private int currentSize;							//The number of occupied cells.
	
	/**
	 * Method to allocate array.
	 * @param arraySize the current size of the array.
	 */
	private void allocateArray(int arraySize) {
		array = new HashEntry[nextPrime(arraySize)];
	}
	
	/**
	 * Return true if currentPos exists and is active.
	 * @param currentPos the result of a call to findPos
	 * @return true if currentPos is active.
	 */
	private boolean isActive(int currentPos) {
		return array[currentPos] != null && array[currentPos].isActive;
	}
	
	/**
	 * Use quadratic probing resolution in half-empty table.
	 * @param item the item to be searched.
	 * @return pos where the search terminates.
	 */
	private int findPos(AnyType item) {
		int offset = 1;
		int currentPos = myHash(item);
		
		while(array[currentPos] != null &&
				!array[currentPos].element.equals(item)) {
			currentPos += offset;	//Compute ith probe
			offset += 2;
			if(currentPos >= array.length)
				currentPos -= array.length;
		}
		
		return currentPos;
	}
	
	/**
	 * Rehashing for quadratic probing hash table.
	 */
	private void rehash() {
		HashEntry<AnyType> [] oldArray = array;
		
		//Create a new double-size, empty table.
		allocateArray(nextPrime(2 * oldArray.length));
		currentSize = 0;
		
		//Copy table over
		for(int i = 0; i < oldArray.length; i++) {
			if(oldArray[i] != null && oldArray[i].isActive)
				insert(oldArray[i].element);
		}
	}
	
	private int myHash(AnyType toHash) {
		int hashVal = toHash.hashCode();
		
		hashVal %= array.length;
		if(hashVal < 0)
			hashVal += array.length;
		
		return hashVal;
	}
	
	/**
	 * Find a prime number at least as large as n.
	 * @param n the starting number(must be positive).
	 * @return a prime number larger than or equal to n.
	 */
	private static int nextPrime(int n) {
		if(n % 2 == 0)
			n++;
		
		for(; !isPrime(n); n+= 2)
			;
		
		return n;
	}
	
	/**
	 * Test if a number is prime.
	 * @param n the number to test.
	 * @return the result of the test.
	 */
	private static boolean isPrime(int n) {
		if(n == 2 || n == 3)
			return true;
		
		if(n == 1 || n % 2 == 0)
			return false;
		
		for(int i = 3; i * i <= n; i += 2)
			if(n % i == 0)
				return false;
		
		return true;
	}
}
