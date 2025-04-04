/******************************************************************
 *
 *   Sulaiman Mohamed / 272 001
 *
 *   Optimized Priority Queue Implementation
 *
 ********************************************************************/

import java.util.ArrayList;
import java.util.Comparator;

@SuppressWarnings("unchecked")
class PriorityQueue<E, P> {
    private static final int INITIAL_CAPACITY = 10;
    private final Comparator<P> priorityComparator;
    private final ArrayList<QueueNode> heap;

    public PriorityQueue() {
        this(INITIAL_CAPACITY, (a, b) -> ((Comparable<P>) a).compareTo(b));
    }

    public PriorityQueue(int initialSize, Comparator<P> comparator) {
        heap = new ArrayList<>(initialSize);
        this.priorityComparator = comparator;
    }

    public int size() { return heap.size(); }
    public boolean isEmpty() { return heap.isEmpty(); }
    public void clear() { heap.clear(); }
    public QueueNode peek() { return heap.isEmpty() ? null : heap.get(0); }

    public QueueNode add(E element, P priority) {
        QueueNode newNode = new QueueNode(element, priority, heap.size());
        heap.add(newNode);
        siftUp(heap.size() - 1);
        return newNode;
    }

    public QueueNode offer(E e, P p) { return add(e, p); }

    public boolean contains(E element) {
        for (QueueNode node : heap) {
            if (node.getElement().equals(element)) {
                return true;
            }
        }
        return false;
    }

    public QueueNode remove() {
        if (heap.isEmpty()) {
            throw new IllegalStateException("PriorityQueue is empty");
        }
        return poll();
    }

    public QueueNode poll() {
        if (heap.isEmpty()) return null;
        
        QueueNode root = heap.get(0);
        if (heap.size() == 1) {
            heap.remove(0);
            return root;
        }
        
        QueueNode last = heap.remove(heap.size() - 1);
        heap.set(0, last);
        last.setIndex(0);
        
        siftDown(0);
        return root;
    }

    private void siftUp(int nodeIndex) {
        while (nodeIndex > 0) {
            int parentIndex = getParentIndex(nodeIndex);
            if (priorityComparator.compare(heap.get(parentIndex).getPriority(), 
                                          heap.get(nodeIndex).getPriority()) <= 0) {
                break;
            }
            swapNodes(nodeIndex, parentIndex);
            nodeIndex = parentIndex;
        }
    }

    private void siftDown(int nodeIndex) {
        int smallest = nodeIndex;
        int leftChild = getLeftChildIndex(nodeIndex);
        int rightChild = getRightChildIndex(nodeIndex);

        if (leftChild < heap.size() && 
            priorityComparator.compare(heap.get(leftChild).getPriority(), 
                                     heap.get(smallest).getPriority()) < 0) {
            smallest = leftChild;
        }
        if (rightChild < heap.size() && 
            priorityComparator.compare(heap.get(rightChild).getPriority(), 
                                     heap.get(smallest).getPriority()) < 0) {
            smallest = rightChild;
        }

        if (smallest != nodeIndex) {
            swapNodes(nodeIndex, smallest);
            siftDown(smallest);
        }
    }

    private int getLeftChildIndex(int i) { return 2 * i + 1; }
    private int getRightChildIndex(int i) { return 2 * i + 2; }
    private int getParentIndex(int i) { return (i - 1) / 2; }

    private void swapNodes(int i, int j) {
        QueueNode nodeA = heap.get(i);
        QueueNode nodeB = heap.get(j);
        
        nodeA.setIndex(j);
        nodeB.setIndex(i);
        
        heap.set(i, nodeB);
        heap.set(j, nodeA);
    }

    public void remove(QueueNode node) {
        if (node.isRemoved() || node.getIndex() >= heap.size() || heap.get(node.getIndex()) != node) {
            throw new IllegalStateException("Node is not in the queue");
        }
        
        if (node.getIndex() == heap.size() - 1) {
            heap.remove(node.getIndex());
        } else {
            QueueNode lastNode = heap.get(heap.size() - 1);
            swapNodes(node.getIndex(), heap.size() - 1);
            heap.remove(heap.size() - 1);
            node.markAsRemoved();
            
            if (lastNode.getIndex() > 0 && 
                priorityComparator.compare(heap.get(getParentIndex(lastNode.getIndex())).getPriority(), 
                                          lastNode.getPriority()) > 0) {
                siftUp(lastNode.getIndex());
            } else {
                siftDown(lastNode.getIndex());
            }
        }
    }

    public final class QueueNode {
        private final E element;
        private P priority;
        private int index;
        private boolean removed = false;

        public QueueNode(E element, P priority, int index) {
            this.element = element;
            this.priority = priority;
            this.index = index;
        }

        public E getElement() { return element; }
        public P getPriority() { return priority; }
        public boolean isValid() { return !removed; }
        public int getIndex() { return index; }
        void setIndex(int newIndex) { index = newIndex; }
        void markAsRemoved() { removed = true; }

        public void updatePriority(P newPriority) {
            if (removed) throw new IllegalStateException("Node is removed");
            int comparison = priorityComparator.compare(newPriority, priority);
            priority = newPriority;
            if (comparison < 0) siftUp(index);
            else if (comparison > 0) siftDown(index);
        }

        public void removeFromQueue() {
            PriorityQueue.this.remove(this);
        }
    }
}
