package it.polimi.tiw.utils;

public class MutablePair<A, B> {
	    private A left;
	    private B right; 

	    public MutablePair(A left, B right) {
	        super();
	        this.left = left;
	        this.right = right;
	    }
	    
	    public MutablePair() {}

	    public boolean equals(Object other) {
	        if (other instanceof MutablePair) {
	            MutablePair otherPair = (MutablePair) other;
	            return 
	            ((  this.left == otherPair.left ||
	                ( this.left != null && otherPair.left != null &&
	                  this.left.equals(otherPair.left))) &&
	             (  this.right == otherPair.right ||
	                ( this.right != null && otherPair.right != null &&
	                  this.right.equals(otherPair.right))) );
	        }

	        return false;
	    }

	    public String toString()
	    { 
	           return "(" + left + ", " + right + ")"; 
	    }

	    public A getLeft() {
	        return left;
	    }

	    public void setLeft(A left) {
	        this.left = left;
	    }

	    public B getRight() {
	        return right;
	    }

	    public void setRight(B right) {
	        this.right = right;
	    }
}

