package finalproject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;




public class MyHashTable<K,V> implements Iterable<MyPair<K,V>>{
    // num of entries to the table
    private int size;
    // num of buckets
    private int capacity = 16;
    // load factor needed to check for rehashing
    private static final double MAX_LOAD_FACTOR = 0.75;
    // ArrayList of buckets. Each bucket is a LinkedList of HashPair
    private ArrayList<LinkedList<MyPair<K,V>>> buckets;


    // constructors
    public MyHashTable() {
        this.buckets = new ArrayList<>(this.capacity);
        for(int i = 0; i < this.capacity; i++){
            this.buckets.add(new LinkedList<MyPair<K, V>>());
        }
    }

    public MyHashTable(int initialCapacity) {
        this.capacity = initialCapacity;
        // initialize the buckets
        this.buckets = new ArrayList<>(this.capacity);
        for (int i = 0; i < this.capacity; i++) {
            this.buckets.add(new LinkedList<MyPair<K, V>>());
        }
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int numBuckets() {
        return this.capacity;
    }

    /**
     * Returns the buckets variable. Useful for testing  purposes.
     */
    public ArrayList<LinkedList< MyPair<K,V> > > getBuckets(){
        return this.buckets;
    }

    /**
     * Given a key, return the bucket position for the key.
     */
    public int hashFunction(K key) {
        int hashValue = Math.abs(key.hashCode())%this.capacity;
        return hashValue;
    }

    /**
     * Takes a key and a value as input and adds the corresponding HashPair
     * to this HashTable. Expected average run time  O(1)
     */
    public V put(K key, V value) {
        int index = this.hashFunction(key);
        LinkedList<MyPair<K,V>> bucket = this.buckets.get(index);

        // Check if the key already exists in the hash table
        for(MyPair<K,V> pair : bucket){
            if(pair.getKey().equals(key)){
                V oldValue = pair.getValue();
                pair.setValue(value);
                return oldValue;
            }
        }

        // Key not found, add a new MyPair
        MyPair<K,V> newPair = new MyPair<>(key, value);
        bucket.add(newPair);
        this.size++;

        // Check if rehashing is necessary
        if((double) this.size / this.capacity > MAX_LOAD_FACTOR){
            this.rehash();
        }

        return null;
    }


    /**
     * Get the value corresponding to key. Expected average runtime O(1)
     */

    public V get(K key) {

        int bucketIndex = Math.abs(key.hashCode()) % this.capacity;
        LinkedList<MyPair<K, V>> bucket = this.buckets.get(bucketIndex);
        for (MyPair<K, V> pair : bucket) {
            if (pair.getKey().equals(key)) {
                return pair.getValue();
            }
        }
        return null;
    }

    /**
     * Remove the HashPair corresponding to key . Expected average runtime O(1)
     */
    public V remove(K key) {
		/*
		int index = this.hashFunction(key);
		LinkedList<MyPair<K,V>> bucket = this.buckets.get(index);

		Iterator<MyPair<K,V>> iterator = bucket.iterator();
		while(iterator.hasNext()){
			MyPair<K,V> pair = iterator.next();
			if(pair.getKey().equals(key)){
				iterator.remove();
				this.size--;
				return pair.getValue();
			}
		}

		return null;

		 */
        int index = this.hashFunction(key);
        LinkedList<MyPair<K,V>> bucket = this.buckets.get(index);

        for(MyPair<K,V> pair : bucket) {
            if(pair.getKey().equals(key)) {
                bucket.remove(pair);
                this.size--;
                return pair.getValue();
            }
        }
        return null;
    }


    /**
     * Method to double the size of the hashtable if load factor increases
     * beyond MAX_LOAD_FACTOR.
     * Made public for ease of testing.
     * Expected average runtime is O(m), where m is the number of buckets
     */
    public void rehash() {
        int newCapacity = this.capacity * 2;
        ArrayList<LinkedList<MyPair<K, V>>> newBuckets = new ArrayList<>(newCapacity);
        for (int i = 0; i < newCapacity; i++) {
            newBuckets.add(new LinkedList<MyPair<K, V>>());
        }
        for (LinkedList<MyPair<K, V>> bucket : this.buckets) {
            for (MyPair<K, V> pair : bucket) {
                int index = Math.abs(pair.getKey().hashCode()) % newCapacity;
                LinkedList<MyPair<K, V>> newBucket = newBuckets.get(index);
                newBucket.add(pair);
            }
        }
        this.capacity = newCapacity;
        this.buckets = newBuckets;
    }


    /**
     * Return a list of all the keys present in this hashtable.
     * Expected average runtime is O(m), where m is the number of buckets
     */

    public ArrayList<K> getKeySet() {
        ArrayList<K> keys = new ArrayList<>();

        for (LinkedList<MyPair<K, V>> bucket : this.buckets) {
            for (MyPair<K, V> pair : bucket) {
                keys.add(pair.getKey());
            }
        }

        return keys;
    }

    /**
     * Returns an ArrayList of unique values present in this hashtable.
     * Expected average runtime is O(m) where m is the number of buckets
     */
    public ArrayList<V> getValueSet() {
		/*
		ArrayList<V> values = new ArrayList<>();
		for (LinkedList<MyPair<K,V>> bucket : this.buckets) {
			for (MyPair<K,V> pair : bucket) {
				if (!values.contains(pair.getValue())) {
					values.add(pair.getValue());
				}
			}
		}
		return values;

		 */
        Object[] uniqueValues = new Object[this.capacity];
        int uniqueValuesCount = 0;

        for (LinkedList<MyPair<K,V>> bucket : this.buckets) {
            for (MyPair<K,V> pair : bucket) {
                V value = pair.getValue();
                boolean isUnique = true;

                // check if the value is already in the uniqueValues array
                for (int i = 0; i < uniqueValuesCount; i++) {
                    if (uniqueValues[i].equals(value)) {
                        isUnique = false;
                        break;
                    }
                }

                // if the value is unique, add it to the uniqueValues array
                if (isUnique) {
                    uniqueValues[uniqueValuesCount] = value;
                    uniqueValuesCount++;
                }
            }
        }

        // convert the uniqueValues array to an ArrayList and return it
        ArrayList<V> values = new ArrayList<>();
        for (int i = 0; i < uniqueValuesCount; i++) {
            values.add((V)uniqueValues[i]);
        }
        return values;
    }


    /**
     * Returns an ArrayList of all the key-value pairs present in this hashtable.
     * Expected average runtime is O(m) where m is the number of buckets
     */
    public ArrayList<MyPair<K, V>> getEntries() {
        ArrayList<MyPair<K,V>> entries = new ArrayList<>();

        for(LinkedList<MyPair<K,V>> bucket : this.buckets){
            for(MyPair<K,V> pair : bucket){
                entries.add(pair);
            }
        }

        return entries;
    }



    @Override
    public MyHashIterator iterator() {
        return new MyHashIterator();
    }


    private class MyHashIterator implements Iterator<MyPair<K,V>> {
        private int bucketIndex;
        private Iterator<MyPair<K, V>> pairIterator;


        private MyHashIterator() {

            this.bucketIndex = 0;
            while (bucketIndex < MyHashTable.this.capacity && MyHashTable.this.buckets.get(bucketIndex).isEmpty()) {
                bucketIndex++;
            }
            if (bucketIndex < MyHashTable.this.capacity) {
                this.pairIterator = MyHashTable.this.buckets.get(bucketIndex).iterator();
            }
        }

        @Override
        public boolean hasNext() {
            return bucketIndex < MyHashTable.this.capacity && pairIterator.hasNext();
        }

        @Override
        public MyPair<K, V> next() {

            MyPair<K, V> nextPair = pairIterator.next();
            // check if there are more pairs in the current bucket
            if (!pairIterator.hasNext()) {
                // find the next non-empty bucket
                bucketIndex++;
                while (bucketIndex < MyHashTable.this.capacity && MyHashTable.this.buckets.get(bucketIndex).isEmpty()) {
                    bucketIndex++;
                }
                // update the pair iterator
                if (bucketIndex < MyHashTable.this.capacity) {
                    pairIterator = MyHashTable.this.buckets.get(bucketIndex).iterator();
                }
            }

            return nextPair;
        }

    }
}

