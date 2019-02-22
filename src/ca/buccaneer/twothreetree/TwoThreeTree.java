package ca.buccaneer.twothreetree;

import java.util.LinkedList;
import java.util.List;

public class TwoThreeTree<T extends Comparable<T>> {

    private Node<T> root = null;
    
    public TwoThreeTree() {
    }
    
    private static final String SPACER = "    ";
    
    public void display() {
        displayNode(root, "");
    }
    
    private void displayNode(Node<T> node, String spacer) {
        if (node == null) {
            return;
        }
        
        System.out.println(spacer + node);
        
        System.out.println(spacer + "LM: " + node.getLeftMax());
        System.out.println(spacer + "MM: " + node.getMiddleMax());

        System.out.println(spacer + "left: ");
        displayNode(node.getLeftChild(), spacer + SPACER);

        System.out.println(spacer + "middle: ");
        displayNode(node.getMiddleChild(), spacer + SPACER);

        System.out.println(spacer + "right: ");
        displayNode(node.getRightChild(), spacer + SPACER);
    }
    
    public void addItem(T item) {
        if (root == null) {
            root = new Node<>(item);
        } else {
            Node<T> newRoot = insertNode(root, item);
            
            if (newRoot != null) {
                root = newRoot;
            }
        }
    }
    
    private Node<T> insertNode(Node<T> current, T item) {
        Node<T> pushUpNode = null;
        
        if (current.isLeaf() || current.childrenAreLeaves() == true) {
            // Found the node/leaf to do the insertion
            
            // Is the item already in here?
            if (itemFoundInLeaf(current, item)) {
                System.out.println("Item " + item + " already in tree");
                return null;
            } else {
                if (current.isFull() == false) {
                    // Leaf has less than 3 children
                    placeInLeaf(current, item);
                } else {
                    pushUpNode = splitNode(current, item);
                }
            }
        } else {
            // Not deep enough - continue traversing
            if (item.compareTo(current.getLeftMax()) < 0) {
                pushUpNode = insertNode(current.getLeftChild(), item);
                
                if (pushUpNode != null) {
                    if (current.isFull() == false) {
                        current.setRightChild(current.getMiddleChild());
                        current.setMiddleChild(pushUpNode.getMiddleChild());
                        current.setLeftChild(pushUpNode.getLeftChild());
                        pushUpNode = null;
                    } else {
                        Node<T> parent = new Node<>();
                        Node<T> left = new Node<>();
                        Node<T> middle = new Node<>();

                        left.setLeftChild(pushUpNode.getLeftChild());
                        left.setMiddleChild(pushUpNode.getMiddleChild());
                        left.setLeftMax(left.getLeftChild().maxValue());
                        left.setMiddleMax(left.getMiddleChild().maxValue());
                        
                        middle.setLeftChild(current.getMiddleChild());
                        middle.setMiddleChild(current.getRightChild());
                        middle.setLeftMax(middle.getLeftChild().maxValue());
                        middle.setMiddleMax(middle.getMiddleChild().maxValue());
                        
                        parent.setLeftChild(left);
                        parent.setLeftMax(parent.getLeftChild().maxValue());
                        parent.setMiddleChild(middle);
                        parent.setMiddleMax(parent.getMiddleChild().maxValue());
                        pushUpNode = parent;
                    }
                }
            } else if (item.compareTo(current.getMiddleMax()) < 0 || current.getRightChild() == null) {
                pushUpNode = insertNode(current.getMiddleChild(), item);
                
                if (pushUpNode != null) {
                    if (current.isFull() == false) {
                        current.setMiddleChild(pushUpNode.getLeftChild());
                        current.setRightChild(pushUpNode.getMiddleChild());
                        pushUpNode = null;
                    } else {
                        Node<T> parent = new Node<>();
                        Node<T> left = new Node<>();
                        Node<T> middle = new Node<>();
                        
                        left.setLeftChild(current.getLeftChild());
                        left.setMiddleChild(pushUpNode.getLeftChild());
                        left.setLeftMax(left.getLeftChild().maxValue());
                        left.setMiddleMax(left.getMiddleChild().maxValue());
                        
                        middle.setLeftChild(pushUpNode.getMiddleChild());
                        middle.setMiddleChild(current.getRightChild());
                        middle.setLeftMax(middle.getLeftChild().maxValue());
                        middle.setMiddleMax(middle.getMiddleChild().maxValue());

                        parent.setLeftChild(left);
                        parent.setLeftMax(parent.getLeftChild().maxValue());
                        parent.setMiddleChild(middle);
                        parent.setMiddleMax(parent.getMiddleChild().maxValue());
                        pushUpNode = parent;
                    }
                }
            } else {
                pushUpNode = insertNode(current.getRightChild(), item);
                
                if (pushUpNode != null) {
                    Node<T> parent = new Node<>();
                    current.setRightChild(null);
                    parent.setLeftChild(current);
                    parent.setLeftMax(parent.getLeftChild().maxValue());
                    parent.setMiddleChild(pushUpNode);
                    parent.setMiddleMax(parent.getMiddleChild().maxValue());
                    pushUpNode = parent;
                }
            }
        }
        
        // Fix max values along the way
        current.setLeftMax(current.getLeftChild().maxValue());
        current.setMiddleMax(current.getMiddleChild().maxValue());
        
        return pushUpNode;
    }
    
    private boolean itemFoundInLeaf(Node<T> node, T item) {
        if (node.isLeaf()) {
            return (item.compareTo(node.getLeftMax()) == 0);
        } else { // if (node.childrenAreLeaves()) {
            return ((node.getLeftChild() != null && item.compareTo(node.getLeftChild().getLeftMax()) == 0)
                        || (node.getMiddleChild() != null && item.compareTo(node.getMiddleChild().getLeftMax()) == 0)
                        || (node.getRightChild() != null && item.compareTo(node.getRightChild().getLeftMax()) == 0));
        }
    }
    
    private void placeInLeaf(Node<T> leaf, T item) {
        // Figure out where this belongs - left, middle, or right?
        if (item.compareTo(leaf.getLeftMax()) < 0) {
            // Item is less that current leaf left - move everything to the right
            leaf.setRightChild(leaf.getMiddleChild());
            leaf.setMiddleMax(leaf.getLeftMax());
            leaf.setLeftMax(item);
        } else {
            if (leaf.getMiddleChild() == null) {
                // Item automatically becomes middle
                leaf.setMiddleMax(item);
            } else if (item.compareTo(leaf.getMiddleMax()) < 0) {
                // Item is less than current leaf middle - move middle to the right
                leaf.setRightChild(leaf.getMiddleChild());
                leaf.setMiddleMax(item);
            } else {
                // Item automatically goes to right
                leaf.setRightChild(new Node<>(item));
            }
        }
        
        // Fix the children
        leaf.setLeftChild(new Node<>(leaf.getLeftMax()));
        leaf.setMiddleChild(new Node<>(leaf.getMiddleMax()));
    }
    
    private Node<T> splitNode(Node<T> node, T item) {
        Node<T> parent = new Node<>();
        Node<T> left = new Node<>();
        Node<T> middle = new Node<>();
        Node<T> k = new Node<>(item);
        
        // Figure out which two children go to new left and which go to new middle
        if (item.compareTo(node.getMiddleMax()) < 0) {
            // New item less than middle, so item and left to go new left, middle and right go to new middle
            middle.setLeftChild(node.getMiddleChild());
            middle.setMiddleChild(node.getRightChild());
            
            // Left and new go to new left
            if (item.compareTo(node.getLeftMax()) < 0) {
                // New item is smallest
                left.setMiddleChild(node.getLeftChild());
                left.setLeftChild(k);
            } else {
                left.setLeftChild(node.getLeftChild());
                left.setMiddleChild(k);
            }
        } else  {
            // New item greater than middle, so left and middle go to new left, item and right move to new middle
            left.setLeftChild(node.getLeftChild());
            left.setMiddleChild(node.getMiddleChild());
            
            if (item.compareTo(node.getRightChild().maxValue()) > 0) {
                // New item is largest
                middle.setLeftChild(node.getRightChild());
                middle.setMiddleChild(k);
            } else {
                middle.setLeftChild(k);
                middle.setMiddleChild(node.getRightChild());
            }
        }

        left.setLeftMax(left.getLeftChild().maxValue());
        left.setMiddleMax(left.getMiddleChild().maxValue());
        
        middle.setLeftMax(middle.getLeftChild().maxValue());
        middle.setMiddleMax(middle.getMiddleChild().maxValue());
        
        parent.setLeftChild(left);
        parent.setLeftMax(left.maxValue());
        parent.setMiddleChild(middle);
        parent.setMiddleMax(middle.maxValue());
        
        return parent;
    }

    public List<T> flattenToList() {
        List<T> flattenedList = new LinkedList<>();
        inOrderAddToList(root, flattenedList);
        
        return flattenedList;
    }
    
    private void inOrderAddToList(Node<T> current, List<T> list) {
        if (current != null) {
            if (current.isLeaf()) {
                list.add(current.getLeftMax());
            } else {
                inOrderAddToList(current.getLeftChild(), list);
                inOrderAddToList(current.getMiddleChild(), list);
                inOrderAddToList(current.getRightChild(), list);
            }
        }
    }
}
