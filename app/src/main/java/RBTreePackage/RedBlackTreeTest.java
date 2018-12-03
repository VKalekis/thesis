package RBTreePackage;

import RBTreePackage.RedBlackTree;

public class RedBlackTreeTest{

    public static void main(String[] args) {

//        GenericBST newBST = new GenericBST();
//        newBST.add(1);
//        newBST.add(2);
//        newBST.printInOrder();

        RedBlackTree RBTree = new RedBlackTree();
        RBTree.add(10);
        RBTree.add(2);
        RBTree.add(5);
        RBTree.add(7);
        RBTree.add(1);
        RBTree.add(3);
        RBTree.add(15);
        RBTree.add(-5);
        RBTree.delete(2);
        RBTree.add(22);
        RBTree.delete(10);


        RBTree.printLevelOrder();


    }
}
