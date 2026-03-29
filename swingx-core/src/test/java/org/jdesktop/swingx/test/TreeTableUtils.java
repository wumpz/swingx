/**
 * 
 */
package org.jdesktop.swingx.test;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

/**
 *
 */
public class TreeTableUtils {
    private TreeTableUtils() {
        //does nothing
    }
    
    public static DefaultTreeTableModel convertDefaultTreeModel(DefaultTreeModel model) {
    	Vector<String> v = new Vector<>();
    	v.add("A");
        DefaultTreeTableModel ttModel = new DefaultTreeTableModel(null, v);
        
        ttModel.setRoot(convertDefaultMutableTreeNode((DefaultMutableTreeNode) model.getRoot()));
        
        return ttModel;
    }
    
    @SuppressWarnings("unchecked")
    private static DefaultMutableTreeTableNode convertDefaultMutableTreeNode(DefaultMutableTreeNode node) {
        DefaultMutableTreeTableNode ttNode = new DefaultMutableTreeTableNode(node.getUserObject());
        
        Enumeration<TreeNode> children = node.children();
        
        while (children.hasMoreElements()) {
            ttNode.add(convertDefaultMutableTreeNode((DefaultMutableTreeNode)children.nextElement()));
        }
        
        return ttNode;
    }
}
