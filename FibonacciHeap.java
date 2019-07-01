/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over non-negative integers.
 * constructor could receive 0 arguments or a HeapNode object as a root alongside its size.
 */
public class FibonacciHeap
{
	private HeapNode root;
	private int size;
	private int marks;
	private static int totalLinks;
	private static int totalCuts;
	
	public FibonacciHeap(){
		this.root = null;
		this.size = 0;
		this.marks = 0;
	}
	
	private FibonacciHeap(HeapNode root, int size)
	{
		this.root = root;
		this.size = size;
	}
	
   /**
    * public boolean empty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    *   
    */
    public boolean empty()
    {
    	return root == null;
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap. 
    */
    public HeapNode insert(int key, int value)
    {    
    	HeapNode new_node = new HeapNode(key, value);
    	size++;
    	if (root != null)
    	{
    		root.prev.next = new_node;
    		new_node.next = root;
    		new_node.prev = root.prev;
    		root.prev = new_node;
    		if (new_node.getKey() < root.getKey())
    			root = new_node;
    		return new_node;
    	}
    	root = new_node;
    	root.next = new_node;
    	root.prev = new_node;
    	return root;
    }

   /**
    * public void deleteMin()
    *
    * Delete the node containing the minimum key.
    *
    */
    public void deleteMin()
    {
    	if (empty())
    		return;
     	HeapNode child = root.child;
     	size = size - 1;
     	if (child == null && root.next.equals(root)) // If no kids & no siblings - delete root
     	{
     		root = null;
     		return;
     	}
     	if (child == null) // If no kids but siblings exist
     	{
     		root.prev.next = root.next;
     		root.next.prev = root.prev;
     		root = root.next;
     	}
     	else if (child != null && root.next.equals(root)) // If kids exist but siblings don't
     	{
     		root = root.child;
     		deleteParents();
     	}
     	else
     	{
     		FibonacciHeap heap2 = new FibonacciHeap(root.child, 0);
     		heap2.deleteParents();  
     		root.prev.next = root.next;
     		root.next.prev = root.prev;
     		root = root.next;
     		meld(heap2);
     	}
     	
     	
     	Link();
     	return;
     	
    }
    
    private void Link()
    {
    	HeapNode [] arr = new HeapNode[(int) Math.round(Math.log(size) / Math.log(2)) + 1];
    	HeapNode n = root;
    	HeapNode min = root;
    	HeapNode next = null;
    	do {
    		int rank = n.rank;
			if (min.key > n.key)
				min = n;
			
			next = n.next;
			if (n.equals(next))
				next = null;
			removeFromSiblings(n);
			n.next = n;
			n.prev = n;
			
    		if (arr[rank] == null)
    		{
    			
    			arr[rank] = n;
    		}
    		else
    		{
    			HeapNode merged_tree = merge(n, arr[rank]);
    			arr[rank] = null;
    			while (arr[merged_tree.rank] != null)
    			{
    				merged_tree = merge(merged_tree, arr[merged_tree.rank]);
    				arr[merged_tree.rank - 1] = null;
    			}
    			arr[merged_tree.rank] = merged_tree;
    		}
    		n = next;
    	} while (next != null);
    	if (arr.length == 1)
    	{
    		root = min;
    		return;
    	}
    	HeapNode first = arr[0];
    	HeapNode temp = first;
    	for (int i = 1; i < arr.length; i++)
    	{
    		if (first == null)
    		{
    			first = arr[i];
    			temp = arr[i];
    			continue;
    		}
    		if (arr[i] != null)
    		{
    			first.next = arr[i];
    			arr[i].prev = first;
    			first = arr[i];
    		}
    		
    	}
    	
    	first.next = temp;
    	temp.prev = first;
    	min = temp;
    	HeapNode temp2 = temp;
    	temp = temp.next;
    	while (!temp.equals(temp2))
    	{
    		if (temp.key <= min.key)
    			min = temp;
    		temp = temp.next;
    	}
    	root = min;
    	return;
    	// NOW ITERATE THE ARRAY AND CREATE FIB HEAP FROM HEAPNODES
    	// USE MIN AS ROOT
    }
    
    private void deleteParents()
    {
    	HeapNode n = root;
 		do {
 			n.parent = null;
 			n = n.next;
 		} while (!n.equals(root));
    	
    }
    
    private HeapNode merge(HeapNode tree1, HeapNode tree2)
    {
    	HeapNode parent = tree2;
    	HeapNode child = tree1;
    	if (tree1.key <= tree2.key)
    	{
    		parent = tree1;
    		child = tree2;
    	}
    	child.parent = parent;
    	if (parent.child == null)
    		parent.child = child;
    	else
    	{
    		parent.child.next.prev = child;
    		child.next = parent.child.next;
    		child.prev = parent.child;
    		parent.child.next = child;
    	}
    	parent.rank = parent.rank + 1;
    	totalLinks++;
    	return parent;
    		
    }

   /**
    * public HeapNode findMin()
    *
    * Return the node of the heap whose key is minimal. 
    *
    */
    public HeapNode findMin()
    {
    	return root;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Meld the heap with heap2
    *
    */
    public void meld (FibonacciHeap heap2)
    {
    	size = size + heap2.size;
    	HeapNode temp = heap2.root;
    	if (root.key >= heap2.root.key)
    	{
    		temp = root;
    		root = heap2.root;
    	}
    	root.prev.next = temp;
    	temp.prev.next = root;
    	root.prev = temp.prev;
    	temp.prev = root.prev;
    }

   /**
    * public int size()
    *
    * Return the number of elements in the heap
    *   
    */
    public int size()
    {
    	return size; // should be replaced by student code
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap. 
    * 
    */
    public int[] countersRep()
    {
    	HeapNode n = root;
    	int i = root.rank;
    	while (!n.next.equals(root))
    	{
    		n = n.next;
    		i = Math.max(i, n.rank);
    	}
    	int [] arr = new int[i + 1];
    	n = root;
    	do {
    		arr[n.rank] = arr[n.rank] + 1;
    		n = n.next;
    	} while (!n.equals(root));
        return arr;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap. 
    *
    */
    public void delete(HeapNode x) 
    {    
    	decreaseKey(x, x.key - root.key + 2019);
    	deleteMin();
    }

    /**
     * public void decreaseKey(HeapNode x, int delta)
     *
     * The function decreases the key of the node x by delta. The structure of the heap should be updated
     * to reflect this chage (for example, the cascading cuts procedure should be applied if needed).
     */
     public void decreaseKey(HeapNode x, int delta)
     {    
     	x.key -= delta;
     	if (x.parent == null)
     	{
     		if (x.key <= root.key)
     			root = x;// case node is a root
     		return;
     	}
     	else if (x.key >= x.parent.key)
     		return;
     	else if (x.key < x.parent.key) {  // we have to cut the node 
     		if (!x.parent.mark) { // node's parent is unmarked - cut
     			cut(x);
     		}
 			else {  //  node's parent is marked - cascading cut
 				cascadingCut(x);
 			}
     	}
     	return;
     }

     private void cut (HeapNode node) {
    	if (!node.parent.mark && node.parent.parent != null)
    	{
    		node.parent.mark = true;
    		marks++;
    	}
     	if (node.mark)
     		marks--;
     	node.mark = false;	
     	node.parent.rank -=1;
     	HeapNode parent = node.parent;
     	node.parent = null;
     	if (node.next.equals(node))
     		parent.child = null;
     	else
     		parent.child = node.next;
 		removeFromSiblings(node);
 		addAsSibling(node, root);
 		if (root.key >= node.key)
 			root = node;
 		totalCuts++;
     }
     
     private void cascadingCut (HeapNode node) {
     	HeapNode parent = node.parent;
     	boolean mark = parent.mark;
     	cut(node);
     	if (parent.parent != null) {
     		if (mark) 
     			cascadingCut(parent);
     	}
     }
     
     private void addAsSibling(HeapNode node, HeapNode sibling) {
     	sibling.next.prev = node;
     	node.next = sibling.next;
     	node.prev = sibling;
     	sibling.next = node;
     }
     
     private void removeFromSiblings(HeapNode node) {
     	node.prev.next = node.next;
     	node.next.prev = node.prev;
     }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap. 
    */
    public int potential() 
    {   
    	if (root == null)
    		return 0;
    	HeapNode n = root;
    	int trees = 1;
    	while (!n.next.equals(root))
    	{
    		n = n.next;
    		trees++;
    	}
    	return trees + 2 * marks;
    	
    	
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the run-time of the program.
    * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of 
    * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value 
    * in its root.
    */
    public static int totalLinks()
    {    
    	return totalLinks;
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the run-time of the program.
    * A cut operation is the operation which diconnects a subtree from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return totalCuts;
    }
    
   /**
    * public class HeapNode
    * 
    * The HeapNode class encapsulates all the necessary data to implement a fibheap,
    * including mark and rank.
    *  
    */
    public class HeapNode{

	private int key;
	private int value;
	private int rank;
	private boolean mark;
	private HeapNode child;
	private HeapNode next;
	private HeapNode prev;
	private HeapNode parent;

  	public HeapNode(int key, int value) {
	    this.key = key;
	    this.value = value;
		rank = 0;
		mark = false;
		parent = null;
		child = null;
      }

  	public int getKey() {
	    return this.key;
      }
  	public int getValue() {
	    return this.value;
      }

    }
}
