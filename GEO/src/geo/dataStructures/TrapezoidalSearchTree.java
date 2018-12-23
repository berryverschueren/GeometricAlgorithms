/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.dataStructures;

/**
 *
 * @author Berry-PC
 */
public class TrapezoidalSearchTree {
    private TrapezoidShape tree;
    
    public TrapezoidalSearchTree() {
    }
    
    public void initialize(TrapezoidShape ts) {
        if (ts == null) {
            return;
        }
        
        this.tree = ts;
        System.out.println("TrapezoidalSearchTree.initialize: " + ts.getLabel());
    }
    
    // int side:
    // 0 = left
    // 1 = right
    public void addNode(TrapezoidShape parentOfParent, TrapezoidShape parent, TrapezoidShape child, int side) {
        if (parentOfParent == null || parent == null || child == null) {
            return;
        }
        
        this.tree.findNode(parentOfParent.getLabel(), parent.getLabel());
    }
    
    public void addNode(String parentLabel, String label, int side) {
        
    }
    
    public void getPath(String label) {
        
    }
    
    public void removeNode(String parentLabel, String label) {
        
    }
    
    public void removeNode(String label) {
        
    }
    
    public void linkNodes(String parentOfParent1Label, String parent1Label, String label1, String parent2Label, String label2, int side) {
        
    }
    
    public boolean onlyHasRoot() {
        
        return false;
    }
    
    public void print() {
        
    }
}
