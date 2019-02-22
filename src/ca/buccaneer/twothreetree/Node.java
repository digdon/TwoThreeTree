package ca.buccaneer.twothreetree;

public class Node<T extends Comparable<T>>  {

    private T leftMax;
    private T middleMax;
    
    private Node<T> leftChild;
    private Node<T> middleChild;
    private Node<T> rightChild;
    
    public Node() {
    }
    
    public Node(T item) {
        this.leftMax = item;
    }

    public T getLeftMax() {
        return leftMax;
    }

    public void setLeftMax(T leftMax) {
        this.leftMax = leftMax;
    }

    public T getMiddleMax() {
        return middleMax;
    }

    public void setMiddleMax(T middleMax) {
        this.middleMax = middleMax;
    }

    public Node<T> getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node<T> leftChild) {
        this.leftChild = leftChild;
    }

    public Node<T> getMiddleChild() {
        return middleChild;
    }

    public void setMiddleChild(Node<T> middleChild) {
        this.middleChild = middleChild;
    }

    public Node<T> getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node<T> rightChild) {
        this.rightChild = rightChild;
    }

    public boolean isLeaf() {
        return (leftChild == null && middleChild == null && rightChild == null);
    }
    
    public boolean childrenAreLeaves() {
        return ((leftChild == null || leftChild.isLeaf())
                    && (middleChild == null || middleChild.isLeaf())
                    && (rightChild == null || rightChild.isLeaf()));
    }
    
    public boolean isFull() {
        return (rightChild != null);
    }

    public T maxValue() {
        if (rightChild != null) {
            return rightChild.getLeftMax();
        } else if (middleMax != null) {
            return middleMax;
        } else {
            return leftMax;
        }
    }
}
