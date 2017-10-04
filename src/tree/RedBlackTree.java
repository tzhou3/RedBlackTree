package tree;

import java.awt.Color;

/**
 * Red Black Tree implementation with only positive integers and colors represented by sign
 * Red is positive and black is negative
 * @author timothy
 *
 */
public class RedBlackTree {
	/*
	 * Enum class to denote color of nodes
	 */
	enum Color{
		Black, Red
	}
	/*
	 * Inner Node class
	 */
	private class Node{
		Color nodeColor;
		int data;
		Node left, right, parent;
		
		public Node(int dat) {
			data=dat;
		}
	}
	
	private Node root; // Root node in the red black tree
	
	/*
	 * Red Black Tree Constructor when no root node is given
	 */
	public RedBlackTree() {
		root =null;
	}
	/*
	 * Red Black Tree constructor when no root node is given
	 */
	public RedBlackTree(int data) {
		root.data=data;
	}
	
	/**
	 * Red Black Tree insert method
	 * @param data
	 */
	public void add(int data) {
		//Insert like a normal binary search tree then call fixTree to properly insert
		Node toInsert = new Node(data);
		if(root ==null) {
			root = toInsert;
		}else {
			Node prev = null;
			Node temp = root;	
			while(temp!=null) {
				prev = temp;
				
				if(data < temp.data) {
					temp = temp.left;
				}else if(temp.data > data){
					temp = temp.right;
				}else {
					return;
				}
			}
			
			toInsert.parent = prev;
			if(prev==null) {
				root = toInsert;
			}else if(toInsert.data < prev.data) {
				prev.left = toInsert;
			}else {
				prev.right = toInsert;
			}
			toInsert.left =null;
			toInsert.right = null;
		}
		toInsert.nodeColor = Color.Red;
		fixInsert(toInsert);
	}
	/**
	 * Method to fix any violations the tree may make upon insertion
	 * @param toFix
	 */
	public void fixInsert(Node toFix) {
		//Will only need to fix if parent is red since node being inserted is red
		if(toFix !=null && toFix !=root && toFix.parent.nodeColor == Color.Red) {
			//Checking case where parent of node inserted is red and sibling of parent is also red
			if(getSibling(toFix.parent).nodeColor == Color.Red) {
				toFix.parent.nodeColor = Color.Black;
				getSibling(toFix.parent).nodeColor = Color.Black;
				grandParent(toFix).nodeColor = Color.Red;
				fixInsert(grandParent(toFix));
			}else if (grandParent(toFix) != null && toFix.parent ==grandParent(toFix).left ) {
				//If the parent is the left child of the grandparent
				if(toFix == toFix.parent.right) { 
					//if the node inserted is the right child of the parent you want to do addres the triangle case
					rotateLeft(toFix = toFix.parent); //Setting toFix to parent value because after rotation for triangle case
					//Line case must also be addressed
				}
				toFix.parent.nodeColor = Color.Black;
				grandParent(toFix).nodeColor = Color.Red;
				rotateRight(grandParent(toFix));
			}else if(grandParent(toFix) !=null && toFix.parent == grandParent(toFix).right) {
				if(toFix==toFix.parent.left) {
					//once again addressing triangle case
					rotateRight(toFix = toFix.parent);
				}
				toFix.parent.nodeColor = Color.Black;
				grandParent(toFix).nodeColor = Color.Red;
				rotateLeft(grandParent(toFix));
			}
		}
		root.nodeColor = Color.Black; //set root to black in case it is red
	}
	
	/**
	 * Rotate original node left on the tree
	 * @param originalNode
	 */
	public void rotateLeft(Node originalNode) {
		Node replaceNode = originalNode.right; //Node to take the place of the replacement node in left rotate
		originalNode.right = replaceNode.left; //Set left subtree of replacement node to be right subtree of original node
		if(replaceNode.left !=null) {
			replaceNode.left.parent = originalNode; //fix the parent linkage
		}
		replaceNode.parent = originalNode.parent; //Link the replacement node to the parent of the original node
		if(originalNode.parent ==null) { //if original node was root set new node to root
			root = replaceNode;
		}else if(originalNode == originalNode.parent.left) {
			originalNode.parent.left = replaceNode; //If left child then link parent left child to replacement node
		}else {
			originalNode.parent.right = replaceNode; //Otherwise right child so same thinking
		}
		replaceNode.left = originalNode;
		originalNode.parent = replaceNode; //Officially change the links of the original node to the replacement node		
	}
	
	/**
	 * Rotate original node right on the tree
	 * @param originalNode
	 */
	public void rotateRight(Node originalNode) {
		Node replaceNode = originalNode.left; //Node to replace original node should be left child
		originalNode.left = replaceNode.right;
		if(replaceNode.right!=null) {
			replaceNode.right.parent = originalNode;
		}
		replaceNode.parent = originalNode.parent;
		if(originalNode.parent == null) {
			root = replaceNode;
		}else if(originalNode == originalNode.parent.left) {
			originalNode.parent.left = replaceNode;
		}else {
			originalNode.parent.right = replaceNode;
		}
		
		replaceNode.right = originalNode;
		originalNode.parent = replaceNode;
	}
	/*
	 * Color getter
	 */
	public Color getColor(Node node) {
		return node == null ? Color.Black:Color.Red;
	}
	
	/*
	 * Sibling getter
	 */
	public Node getSibling(Node node) {
		return (node == null || node.parent == null) ? null:
			(node.parent.left == node ? node.parent.right:node.parent.left);
	}
	/*
	 * Returns grandparent if exists otherwise null
	 */
	public Node grandParent(Node node) {
		return node.parent == null ? null: node.parent.parent;
	}
	
}
