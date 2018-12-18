/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.dataStructures;

/**
 *
 * @author Berry
 */
public class TrapezoidalSearchStructure {
    
    class SearchNode {
        private String label;
        private SearchNode left, right;

        public SearchNode(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public SearchNode getLeft() {
            return left;
        }

        public SearchNode getRight() {
            return right;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public void setLeft(SearchNode left) {
            this.left = left;
        }

        public void setRight(SearchNode right) {
            this.right = right;
        }
        
        public SearchNode findNode(String label) {
            System.out.println("Searching in " + this.label + " for label " + label);
            if (label == null || label.equals("")) {
                return null;
            }
            
            SearchNode sn = null;
            
            if (this.label.equals(label)) {
                sn = this;
                return sn;
            } 
            
            if (this.left == null && this.right == null) {
                sn = null;
            }
            
            if (sn == null) {
                if (this.left != null) {
                    sn = this.left.findNode(label);
                }
            }
            
            if (sn == null) {
                if (this.right != null) {
                    sn = this.right.findNode(label);
                }
            }
            if (sn != null) {
                System.out.println("Found " + sn.label + " in " + this.label);
            }
            
            return sn;
        }
        
        public void addNode(SearchNode sn, int side) {
            if (side == 0) {
                this.left = sn;
                System.out.println("Node " + this.left.label + " added left of " + this.label);
            } 
            else if (side == 1) {
                this.right = sn;
                System.out.println("Node " + this.right.label + " added right of " + this.label);
            }
        }
        
        public void removeNode(String label) {
            if (label == null || label.equals("")) {
                return;
            }            
            
            if (this.left != null && this.left.label.equals(label) && this.left.left == null && this.left.right == null) {
                this.left = null;
                return;
            }         
            
            if (this.right != null && this.right.label.equals(label) && this.right.left == null && this.right.right == null) {
                this.right = null;
            }
        }
    }
    
    private SearchNode tree;

    public TrapezoidalSearchStructure() {
    }
    
    public void Initialize(String label) {
        if (label == null || label.equals("")) {
            label = "root";
        }
        
        this.tree = new SearchNode(label);
        System.out.println("Initialized tree: " + this.tree.label);
    }
    
    public void AddNode(String toLabel, String label, int side) {
        // side == 0 --> left, side == 1 --> right
        SearchNode sn = new SearchNode(label);
        SearchNode tn = this.tree.findNode(toLabel);
        if (tn != null) {
            System.out.println("Found node for " + toLabel + " -- " + tn.label);
            tn.addNode(sn, side);
        }
    }
    
    public void RemoveNode(String label) {
        if (label == null || label.equals("")) {
            return;
        }
        
        if (this.tree.label.equals(label) && this.tree.left == null && this.tree.right == null) {
            this.tree = null;
        }
        else if (tree != null){
            this.tree.removeNode(label);
        }
    }
}
