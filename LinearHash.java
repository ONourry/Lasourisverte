import java.util.*;

public class LinearHash<Key,Value>{
	
	private int nbrElements;
	private int sizeHash;
	
	private final int DEFAULT_SIZE = 11;
	private final double LOAD_FACTOR_TOO_BIG = 0.75;
	private final double LOAD_FACTOR_TOO_SMALL = 0.20;
	
	private Key[] keys;
	private Value[] values;
	
	//Default constructor
	public LinearHash(){
		this.sizeHash = this.DEFAULT_SIZE;
		this.nbrElements = 0;
		
		this.keys = (Key[]) new Object [this.DEFAULT_SIZE];
		this.values =  (Value[]) new Object[this.DEFAULT_SIZE];
	}
	
	//Constructor taking a size as argument
	public LinearHash(int size){
		this.sizeHash = size;
		this.nbrElements = 0;
		
		this.keys =  (Key[]) new Object[this.sizeHash];
		this.values = (Value[]) new Object[this.sizeHash];
	}
	
	public int size(){
		return this.nbrElements;
	}
	
	//Put a value and a key in the hash table
	public void put(Key key, Value value){
		//Invalid key
		try{
			if(key == null)
				throw new Exception("Invalid Key");
		} catch(Exception e){
			e.printStackTrace();;
		}
		
		int index = hashKey(key);
		
		//Key valid but empty value
		try{
			if(value == null){
				throw new Exception("Invalid Value");
			}
		} 
		catch(Exception e){
			e.printStackTrace();
		}
		
		//If we exceed the 0.75 load, double the size
		if (( ((double)this.nbrElements) / ((double)this.sizeHash) ) > LOAD_FACTOR_TOO_BIG)
			resize(2*this.sizeHash);
		
		
		//Hash index is already occupied
		if(keys[index] != null){
			
			int newIndex;
			
			for(newIndex = index; keys[newIndex] != null; newIndex++){
				
				//Key already in hashtable, update value
				if(keys[newIndex] == key){
					values[newIndex] = value;
					return;
				}
				
				//Return to beginning of array and keep searching if last index has been reached
				if(newIndex == keys.length - 1)
					newIndex = -1;
			}
			
			//Adjust attributes
			keys[newIndex] = key;
			values[newIndex] = value;
			this.nbrElements++;

		}
		
		
	}
	
	public boolean contains (Key key){
		try{
			if(key == null)
				throw new Exception("Invalid Key");
			
		} catch (Exception e){
			e.printStackTrace();
		}
		return get(key) != null;
	}
	
	//Get the corresponding value of the key given in parameter
	public Value get(Key key){
		try{
			if(key == null)
				throw new Exception("Invalid Key");
			
		} catch (Exception e){
			e.printStackTrace();
		}	
		for(int i = hashKey(key); keys[i] != null; i++){
			
			
			//There has been something there before but it was deleted, to avoid issues with the .equals method
			if(keys[i].toString() == ("-1"))
				continue;
			
			if(keys[i] == key){
				return values[i];
			}
			
			//Return to beginning of array and keep searching if last index has been reached
			if(i == keys.length - 1)
				i = -1;
		}
		
		//Couldnt find anything at the specified key
		return null;
	}
	
	//Remove a key (Set to -1) from the hashtable and set the value to null
	public void remove(Key key){
		try{
			if(key == null)
				throw new Exception("Invalid Key");
			
		} catch (Exception e){
			e.printStackTrace();
		}
		if (!contains(key))
			return;
		
		for(int i = hashKey(key); keys[i] != null; i++){
			//-----------------------------------------------------------------------replace by empty object??
			if(keys[i].equals(key)){
				keys[i] = null;
				values[i] = null;
			}
			
			//Return to beginning of array and keep searching if last index has been reached
			if(i == keys.length - 1)
				i = -1;
		}
		
		this.sizeHash--;
		
		//Hash table has too much empty space, resize
		if (( ((double)this.nbrElements) / ((double)this.sizeHash) ) < LOAD_FACTOR_TOO_SMALL)
			resize(this.sizeHash/2);
	}
	
	//Get hash code for the key
	private int hashKey(Key key){
		return (key.hashCode() % this.sizeHash);
	}
	
		
	public Set<Key> keySet(){
		Set<Key> keySet = new HashSet<Key>(Arrays.asList(keys));
		
		return keySet;
	}
	
	//Resize the hashtable
	private void resize(int sizeHash){
		LinearHash newHash = new LinearHash<Key,Value>(sizeHash);
		
		for(Key key : this.keySet()){
			try {
				newHash.put(key, this.get(key));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		this.keys = (Key[]) newHash.keys;
		this.values = (Value[]) newHash.values;
		this.sizeHash = newHash.sizeHash;
	}
}