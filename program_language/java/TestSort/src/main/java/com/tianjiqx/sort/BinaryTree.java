package com.tianjiqx.sort;

import java.util.*;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

public class BinaryTree {

  public TreeNode  init( int [] nums) {
    if (nums.length ==0) return null;
    TreeNode [] nodes = new TreeNode[nums.length];
    int i = 1;
    TreeNode root = new TreeNode(nums[0]);
    nodes[0] = root;
    while (i <  nums.length) {
      if (nums[i] != 0) {
        nodes[i] = new TreeNode(nums[i]);
        if (i %2 == 1) {
          nodes[(i-1)/2].left = nodes[i];
        } else {
          nodes[(i-1)/2].right = nodes[i];
        }
      }
      i++;
    }
    return root;
  }

  public void printBinaryTree(TreeNode root) {
    if (root == null) {
      return;
    }
    Queue<TreeNode> queue = new LinkedList<>();
    queue.add(root);
    while (!queue.isEmpty()) {
      TreeNode node = queue.poll();
      System.out.println(node.val);
      if (node.left != null) {
        queue.add(node.left);
      }
      if (node.right != null) {
        queue.add(node.right);
      }
    }
  }

  public static void main(String[] args) {
    // 0 represent is null TreeNode
    int [] nums = {3,9,20,0,0,15,7};

    BinaryTree tree = new BinaryTree();
    TreeNode root = tree.init(nums);

    tree.printBinaryTree(root);
  }

}
