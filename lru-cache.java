public class LRUCache {
    
    Map<Integer, MyNode<Integer>> keyNodePairs;
    MyList<Integer> timeOrderedKeyNodes;
    
    Map<Integer, Integer> keyValuePairs;
    
    public static Integer UNDEFINED_VALUE = -1;
    
    public LRUCache(int capacity) {
        
        if (capacity <= 0) {
            
            throw new RuntimeException("invalid capacity: " + capacity);
        }
        
        timeOrderedKeyNodes = new MyList<Integer>(capacity);
        keyNodePairs = new HashMap<Integer, MyNode<Integer>>();
        
        keyValuePairs = new HashMap<Integer, Integer>();
    }
    
    public int get(int key) {
        
        if (keyValuePairs.containsKey(key)) {
            
            MyNode<Integer> keyNode = keyNodePairs.get(key);
    
            //since this node is recently accessed, move it to the top of the queue        
            timeOrderedKeyNodes.removeNode(keyNode);
            timeOrderedKeyNodes.addLast(keyNode);
            
            return keyValuePairs.get(key);
        }
        
        return UNDEFINED_VALUE;
    }
    
    public void set(int key, int value) {
        
        //need to evict one node since the capacity is full 
        if (!keyNodePairs.containsKey(key) && (timeOrderedKeyNodes.getSize() >= timeOrderedKeyNodes.getCapacity())) {
            
            MyNode<Integer> keyNode = timeOrderedKeyNodes.removeFirst();
            
            keyNodePairs.remove(keyNode.getValue());
            keyValuePairs.remove(keyNode.getValue());
        } 
        
        
        //remove this node if it has the key
        if (keyNodePairs.containsKey(key)) {
            
            MyNode<Integer> keyNode = keyNodePairs.remove(key);
            
            timeOrderedKeyNodes.removeNode(keyNode);
            keyValuePairs.remove(key);
        }
        
        //add the node to the tail of the queue
        MyNode<Integer> keyNode = new MyNode<Integer>(key);
        keyNodePairs.put(key, keyNode);
        timeOrderedKeyNodes.addLast(keyNode);
        keyValuePairs.put(key, value);
    }
    
    private class MyNode<T> {
        
        private T value;
        
        private MyNode<T> nextNode;
        private MyNode<T> prevNode;
        
        public MyNode(T value) {
            
            this.value = value;
        }
        
        T getValue() {
            
            return value;
        }
        
        void setValue(T value) {
            
            this.value = value;
        }
        
        public MyNode<T> getPrevNode() {
            
            return prevNode;
        }
        
        public void setPrevNode(MyNode<T> prevNode) {
            
            this.prevNode = prevNode;
        }
        
        public MyNode<T> getNextNode() {
            
            return nextNode;
        }
        
        public void setNextNode(MyNode<T> nextNode) {
            
            this.nextNode = nextNode;
        }
    }
    
    private class MyList<T> {
        
        private int capacity = -1;
        private int size = 0;
        
        private MyNode<T> head;
        private MyNode<T> tail;
        
        public MyList(int capacity) {
            
            if (capacity <= 0) {
                
                throw new RuntimeException("invalid capacity: " + capacity);
            }
            this.capacity = capacity;
            this.size = 0;
            
            head = new MyNode<T>(null);
            tail = new MyNode<T>(null);
            
            head.nextNode = tail;
            head.prevNode = null;
            
            tail.prevNode = head;
            tail.nextNode = null;
        }
        
        public int getSize() {
            
            return size;
        }
        
        public int getCapacity() {
            
            return capacity;
        }
        
        public MyNode<T> removeFirst() {
            
            if (size <= 0) {
                
                throw new RuntimeException("No data left in the list");
            }
            
            MyNode<T> firstNode = head.getNextNode();
            
            head.setNextNode(firstNode.getNextNode());
            firstNode.getNextNode().setPrevNode(head);
            
            size --;
            return firstNode;
        }
        
        public int addLast(MyNode<T> lastNode) {
            
            if (size >= capacity) {
                
                throw new RuntimeException("List out of capacity");
            }
            
            MyNode<T> prevLastNode = tail.getPrevNode();
            
            prevLastNode.setNextNode(lastNode);
            lastNode.setPrevNode(prevLastNode);
            
            lastNode.setNextNode(tail);
            tail.setPrevNode(lastNode);
            
            size++;
            return size;
        }
        
        public int removeNode(MyNode<T> node) {
            
            if ((node == head) || (node == tail)) {
                
                throw new RuntimeException("Cannot remove the anchor node");
            }
            
            MyNode<T> prevNode = node.getPrevNode();
            MyNode<T> nextNode = node.getNextNode();
            
            if ((prevNode == null) || (nextNode == null)) {
                
                throw new RuntimeException("Cannot find this node's previous node or last node");
            }
            
            prevNode.setNextNode(nextNode);
            nextNode.setPrevNode(prevNode);
            
            size--;
            return size;
        }
    }
}
