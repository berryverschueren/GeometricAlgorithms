/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.dataStructures;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Berry-PC
 */
public class TrapezoidShape {
    private String label;
    private TrapezoidShape left, right;
    private List<TrapezoidShape> parents;
    
    public TrapezoidShape () {
        this.parents = new ArrayList<>();
    }
    
    public TrapezoidShape (String label) {
        this.parents = new ArrayList<>();
        this.label = label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return this.label;
    }

    public TrapezoidShape getLeft() {
        return left;
    }

    public TrapezoidShape getRight() {
        return right;
    }
    
    public List<TrapezoidShape> getParents() {
        return parents;
    }
    
    public TrapezoidShape findNode(String label) {
        if (label == null || label.equals(""))
            return null;
        
        if (this.label.equals(label)) {
            return this;
        }
        
        TrapezoidShape ts = null;
        
        if (ts == null) { 
            if (this.left != null) {
                ts = this.left.findNode(label);
            }
        }
        
        if (ts == null) {
            if (this.right != null) {
                ts = this.right.findNode(label);
            }
        }
        
        return ts;
    }
    
    public TrapezoidShape findNode(String parentLabel, String label) {
        if (parentLabel == null || parentLabel.equals("") || 
                label == null || label.equals(""))
            return null;
        
        if (this.label.equals(parentLabel)) {
            if (this.left != null && this.left.label.equals(label)) {
                return this.left;
            } else if (this.right != null && this.right.label.equals(label)) {
                return this.right;
            }
        }
        
        TrapezoidShape ts = null;
        
        if (ts == null) { 
            if (this.left != null) {
                ts = this.left.findNode(parentLabel, label);
            }
        }
        
        if (ts == null) {
            if (this.right != null) {
                ts = this.right.findNode(parentLabel, label);
            }
        }
        
        return ts;
    }
    
    public TrapezoidShape findNode(String parentOfParentLabel, String parentLabel, String label) {
        if (parentOfParentLabel == null || parentOfParentLabel.equals("") || 
                parentLabel == null || parentLabel.equals("") || 
                label == null || label.equals(""))
            return null;
        
        if (this.label.equals(parentOfParentLabel)) {
            if (this.left != null && this.left.label.equals(parentLabel)) {
                if (this.left.left != null && this.left.left.label.equals(label)) {
                    return this.left.left;
                }
                if (this.left.right != null && this.left.right.label.equals(label)) {
                    return this.left.right;
                }
            } else if (this.right != null && this.right.label.equals(parentLabel)) {
                if (this.right.left != null && this.right.left.label.equals(label)) {
                    return this.right.left;
                }
                if (this.right.right != null && this.right.right.label.equals(label)) {
                    return this.right.right;
                }
            }
        }
        
        TrapezoidShape ts = null;
        
        if (ts == null) { 
            if (this.left != null) {
                ts = this.left.findNode(parentOfParentLabel, parentLabel, label);
            }
        }
        
        if (ts == null) {
            if (this.right != null) {
                ts = this.right.findNode(parentOfParentLabel, parentLabel, label);
            }
        }
        
        return ts;
    }

    public void addNode(TrapezoidShape ts, int side) {
        if (side == 0) {
            this.left = ts;
            this.left.parents.add(this);
        }
        if (side == 1) {
            this.right = ts;
            this.right.parents.add(this);
        }
    }

    public void removeNode(String label) {
        if (label == null || label.equals("")) {
            return;
        }
        
        if (this.left != null && this.left.label.equals(label)) {
            this.left = null;
            return;
        }
        else if (this.right != null && this.right.label.equals(label)) {
            this.right = null;
            return;
        }
        
        if (this.left != null) {
            this.left.removeNode(label);
        }
        
        if (this.right != null) {
            this.right.removeNode(label);
        }
    }
    
    public void removeNode(String parentLabel, String label) {
        if (parentLabel == null || parentLabel.equals("") 
                || label == null || label.equals("")) {
            return;
        }
        
        if (this.label.equals(parentLabel)) {
            if (this.left != null && this.left.label.equals(label)) {
                this.left = null;
                return;
            }
            else if (this.right != null && this.right.label.equals(label)) {
                this.right = null;
                return;
            }
        }
        
        if (this.left != null) {
            this.left.removeNode(parentLabel, label);
        }
        
        if (this.right != null) {
            this.right.removeNode(parentLabel, label);
        }
    }
    
    public void removeNode(String parentOfParentLabel, String parentLabel, String label) {
        if (parentOfParentLabel == null || parentOfParentLabel.equals("") 
                || parentLabel == null || parentLabel.equals("") 
                || label == null || label.equals("")) {
            return;
        }
        
        if (this.label.equals(parentOfParentLabel)) {
            if (this.left != null && this.left.label.equals(parentLabel)) {
                if (this.left.left != null && this.left.left.label.equals(label)) {
                    this.left.left = null;
                    return;
                }
                if (this.left.right != null && this.left.right.label.equals(label)) {
                    this.left.right = null;
                    return;
                }
            }
            else if (this.right != null && this.right.label.equals(parentLabel)) {
                if (this.right.left != null && this.right.left.label.equals(label)) {
                    this.right.left = null;
                    return;
                }
                if (this.right.right != null && this.right.right.label.equals(label)) {
                    this.right.right = null;
                    return;
                }
            }
        }
        
        if (this.left != null) {
            this.left.removeNode(parentOfParentLabel, parentLabel, label);
        }
        
        if (this.right != null) {
            this.right.removeNode(parentOfParentLabel, parentLabel, label);
        }
    }

    public void print() {
        print("", true);
    }
    
    private void print(String prefix, boolean isTail) {
        String parentsLabel = "";
        if (this.parents.size() > 0) {
            parentsLabel = "(";
            for (int i = 0; i < parents.size(); i++) {
                parentsLabel += parents.get(i).label + " ";
            }
            parentsLabel = parentsLabel.substring(0, parentsLabel.length() - 1);
            parentsLabel += ")";
        }
        System.out.println(prefix + (isTail ? "└── " : "├── ") + this.label + " " + parentsLabel);
        if (this.left != null) {
            this.left.print(prefix + (isTail ? "    " : "│   "), false);
        }
        if (this.right != null) {
            this.right.print(prefix + (isTail ? "    " : "│   "), false);
        }
    }
}
