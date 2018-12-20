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
            if (label == null || label.equals("")) {
                return null;
            }
            
            SearchNode sn = null;
            
            if (this.label.equals(label)) {
                sn = this;
                return sn;
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
            
            return sn;
        }
        
        public SearchNode findSpecificNode(String parentLabel, String label) {
            if (label == null || label.equals("") || parentLabel == null || parentLabel.equals("")) {
                return null;
            }
            
            SearchNode sn = null;
            
            if (this.label.equals(parentLabel)) {
                if (this.left != null && this.left.label.equals(label)) {
                    sn = this.left;
                    return sn;
                }
                else if (this.right != null && this.right.label.equals(label)) {
                    sn = this.right;
                    return sn;
                }
            }
            
            if (sn == null) {
                if (this.left != null) {
                    sn = this.left.findSpecificNode(parentLabel, label);
                }
            }
            
            if (sn == null) {
                if (this.right != null) {
                    sn = this.right.findSpecificNode(parentLabel, label);
                }
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
        
        public void removeSpecificNode(String parentLabel, String label) {
            if (label == null || label.equals("") || parentLabel == null || parentLabel.equals("")) {
                return;
            }
            
            if (this.label.equals(parentLabel)) {
                if (this.left.label.equals(label)) {
                    System.out.println("Node " + this.left.label + " removed left of " + this.label);
                    this.left = null;
                    return;
                }
                else if (this.right.label.equals(label)) {
                    System.out.println("Node " + this.right.label + " removed right of " + this.label);
                    this.right = null;
                    return;
                }
            }
            
            if (this.left != null) {
                this.left.removeSpecificNode(parentLabel, label);
            }
            
            if (this.right != null) {
                this.right.removeSpecificNode(parentLabel, label);
            }
        }
        
        public void print() {
            print("", true);
        }

        private void print(String prefix, boolean isTail) {
            System.out.println(prefix + (isTail ? "└── " : "├── ") + this.label);
            if (this.left != null) {
                this.left.print(prefix + (isTail ? "    " : "│   "), false);
            }
            if (this.right != null) {
                this.right.print(prefix + (isTail ? "    " : "│   "), false);
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
            tn.addNode(sn, side);
        }
    }
    
    public void AddNode(String parentLabel, String toLabel, String label, int side) {
        // side == 0 --> left, side == 1 --> right
        SearchNode sn = new SearchNode(label);
        //SearchNode tn = this.tree.findNode(toLabel);
        SearchNode tn = this.tree.findSpecificNode(parentLabel, toLabel);
        if (tn != null) {
            tn.addNode(sn, side);
        }
    }
    
    public void RemoveNode(String parentLabel, String label) {
        if (label == null || label.equals("")) {
            return;
        }
        
        if (this.tree.label.equals(label) && this.tree.left == null && this.tree.right == null) {
            this.tree = null;
        }
        else if (tree != null){
            this.tree.removeSpecificNode(parentLabel, label);
        }
    }
    
    public void LinkNodes(String parent1Label, String label1, String parent2Label, String label2, int side) {
        if (parent1Label == null || parent1Label.equals("") || parent2Label == null || parent2Label.equals("")
                || label1 == null || label1.equals("") || label2 == null || label2.equals("")) {
            return;
        }
        
        // link 1 as parent of 2
        if (this.tree != null) {
            SearchNode sn1 = this.tree.findSpecificNode(parent1Label, label1);
            SearchNode sn2 = this.tree.findSpecificNode(parent2Label, label2);
            
            if (sn1 != null) {
                sn1.addNode(sn2, side);
            }           
        }
    }
    
    public void Print() {
        if (this.tree != null) {
            System.out.println("Printing tree structure, first child in the tree is the left child node.");
            this.tree.print();
        }
    }
}
