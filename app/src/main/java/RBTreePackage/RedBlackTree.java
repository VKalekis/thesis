package RBTreePackage;

public class RedBlackTree<T extends Comparable<T>> extends GenericBST<T> {

    private StringBuilder outputTree = new StringBuilder();

    public RedBlackTree(){
    }

    private void leftRotate(Node<T> x){

        Node<T> y=x.right, temp1=x.parent;

        x.right=y.left;
        if (y.left!=null) {
            y.left.parent = x;
        }
        if (root==x){
            root=y;
        }
        else if (x==x.parent.left){
            x.parent.left= y;
        }
        else{
            x.parent.right=y;
        }
        y.left=x;
        x.parent=y;

        if (x.parent!=null){
            x.parent.parent=temp1;
        }
	    else{
	        x.parent.parent=null;
        }
    }

    private void rightRotate(Node<T> x){

        Node<T> y=x.left, temp1=x.parent;

        x.left=y.right;
        if (y.right!= null) {
            y.right.parent = x;
        }
        if (x==root){
            root=y;
        }
        else if (x==x.parent.left){
            x.parent.left=y;
        }
        else{
            x.parent.right=y;
        }
        y.right=x;
        x.parent=y;

        if (x.parent!=null){
            x.parent.parent=temp1;
        }
	    else{
            x.parent.parent=null;
        }
    }

    private void insertFixup(Node<T> x){

        while (x.parent!=null && x.parent.colour=='R'){
            System.out.println("here0");

            if (x.parent==x.parent.parent.left){
                System.out.println("here1");
                Node<T> uncle=x.parent.parent.right;
                if ( uncle!=null && uncle.colour=='R'){
                    System.out.println("here2");
                    uncle.colour='B';
                    x.parent.colour='B';
                    x.parent.parent.colour='R';
                    x=x.parent.parent;
                }
                else {
                    System.out.println("here3");
                    if (x==x.parent.right){
                        System.out.println("here4");
                        x=x.parent;
                        leftRotate(x);
                    }
                    x.parent.colour='B';
                    x.parent.parent.colour='R';
                    rightRotate(x.parent.parent);
                }
            }
		    else if (x.parent==x.parent.parent.right) {
                Node<T> uncle=x.parent.parent.left;
                if (uncle!=null && uncle.colour=='R'){
                    System.out.println("here5");
                    uncle.colour='B';
                    x.parent.colour='B';
                    x.parent.parent.colour='R';
                    x=x.parent.parent;
                }
                else{
                    if (x==x.parent.left){
                        System.out.println("here6\n");
                        x=x.parent;
                        rightRotate(x);
                    }
                    System.out.println("here7");
                    x.parent.colour='B';
                    x.parent.parent.colour='R';
                    leftRotate(x.parent.parent);
                }
            }
            System.out.println("here8");
        }
        root.colour='B';
    }

    @Override
    public Node<T> add(T data){
        Node<T> addedNode;
        addedNode=super.add(data);
        if (addedNode!=null) {
            System.out.println("New Node key" + addedNode.key + "\n");
            if (addedNode == root) {
                addedNode.colour = 'B';
            } else {
                addedNode.colour = 'R';
            }
            insertFixup(addedNode);
            return addedNode;
        }
        else {
            return null;
        }
    }

    public StringBuilder printLevelOrder(){
        outputTree.setLength(0);
        printLevelOrder(root);
        return outputTree;
    }

    private void printLevelOrder(Node<T> x){
        int h=height(x);
        int i;

        for (i=1; i<=h; i++) {
            printGivenLevel(x, i);
            System.out.println("");
            outputTree.append("\n");

        }
    }

    private void printGivenLevel(Node<T> x, int level){
        if (x==null)
            return;
        if (level==1){
            System.out.print(" "+x.key+" "+x.colour);
            outputTree.append(" "+x.key+" "+x.colour);
            if (x.left!=null) {
                System.out.print("l");
                outputTree.append("l");
            }
            if (x.right!=null) {
                System.out.print("r");
                outputTree.append("r");
            }
        }
        else if (level > 1){
            printGivenLevel(x.left, level-1);
            printGivenLevel(x.right, level-1);
        }
    }

    private int height(Node<T> x){
        if (x==null)
            return 0;
        else{
            int lheight = height(x.left);
            int rheight = height(x.right);

            if (lheight > rheight)
                return(lheight+1);
            else return(rheight+1);
        }
    }


    @Override
    public void delete(T data){
        if (super.check(data)) {
            this.deleteNode(root, data);
        }
    }

    private void deleteNode(Node<T> x, T data){

        int comp3=(x.key).compareTo(data);
        if (x.right!=null && comp3<0)
            deleteNode(x.right,data);
        if (x.left!=null && comp3>0)
            deleteNode(x.left,data);
        if (comp3==0){
            if ( x.left!=null && x.right!=null){
                Node<T> suc=super.findSuccessor(x.right);
                x.key=suc.key;
                deleteCases(suc);
            }
		    else{
                deleteCases(x);
            }
        }
    }

    private void deleteCases(Node<T> x) {
        System.out.println("To be deleted "+x.key);
        printLevelOrder(root);
        if (x==root && x.left==null && x.right==null){
            System.out.println("Del0");
            root=null;
        }
        else if (x.colour=='R'){
            if (x.left==null && x.right==null){
                System.out.println("Del1");
                if (x==x.parent.left){
                    x.parent.left=null;
                }
                else{
                    x.parent.right=null;
                }
            }
            else if (x.left!=null && x.right==null){
                System.out.println("Del2");
                if (x==x.parent.left) {
                    x.parent.left=x.left;
                }
                else {
                    x.parent.right=x.left;
                }
                x.left.parent=x.parent;
            }
            else if (x.left==null && x.right!=null){
                System.out.println("Del3");
                if (x==x.parent.left) {
                    x.parent.left=x.right;
                }
                else {
                    x.parent.right=x.right;
                }
                x.right.parent=x.parent;
            }
        }
        else if (x.colour=='B'){
            if (x.left!=null && x.right==null && x.left.colour=='R'){
                System.out.println("Del4");
                x.key=x.left.key;
                x.left=null;
            }
            else if (x.left==null && x.right!=null && x.right.colour=='R'){
                System.out.println("Del5");
                x.key=x.right.key;
                x.right=null;
            }
            else{
                System.out.println("Del6");
                deleteBlack(x);
            }
        }
    }

    private void deleteBlack(Node<T> x){

        System.out.println("b1");
        System.out.println("To be deleted111: %d\n"+ x.key);
        printLevelOrder(root);
        if (x.parent!=null && x.parent.colour=='R'){
            System.out.println("b2");
            if (x==x.parent.left){
                System.out.println("Case4a");
                if (((x.parent.right.left==null)||(x.parent.right.left.colour=='B'))&&((x.parent.right.right==null)||(x.parent.right.right.colour=='B'))){
                    System.out.println("Case4b");
                    x.parent.colour='B';
                    x.parent.right.colour='R';
                    x.colour='B';
                    checkDelete(x);
                }
                else if (x.parent.right.right!=null && x.parent.right.right.colour=='R'){
                    System.out.println("Case6");
                    leftRotate(x.parent);
                    x.parent.parent.colour=x.parent.colour;
                    x.parent.colour='B';
                    x.parent.parent.right.colour='B';
                    x.colour='B';
                    checkDelete(x);
                }
            }

            else if (x==x.parent.right){
                System.out.println("Case4c");
                if (((x.parent.left.left==null)||(x.parent.left.left.colour=='B'))&&((x.parent.left.right==null)||(x.parent.left.right.colour=='B'))){
                    System.out.println("Case4d");
                    x.parent.colour='B';
                    x.parent.left.colour='R';
                    x.colour='B';
                    checkDelete(x);
                }
                else if (x.parent.left.left!=null && x.parent.left.left.colour=='R'){
                    System.out.println("Case6");
                    rightRotate(x.parent);
                    x.parent.parent.colour=x.parent.colour;
                    x.parent.colour='B';
                    x.parent.parent.left.colour='B';
                    x.colour='B';
                    checkDelete(x);
                }
            }
        }

        else {
            System.out.println("b3");
            if (x.parent!=null && x==x.parent.left){
                //case6
                if (x.parent.right!=null && x.parent.right.colour=='B' && x.parent.right.right!=null && x.parent.right.right.colour=='R'){
                    System.out.println("Case6");
                    leftRotate(x.parent);
                    x.parent.parent.colour=x.parent.colour;
                    x.parent.colour='B';
                    x.parent.parent.right.colour='B';
                    x.colour='B';
                    checkDelete(x);
                }
                else if (x.parent.right!=null && x.parent.right.colour=='B'){
                    //case3
                    if (((x.parent.right.left!=null)||(x.parent.right.left.colour=='B'))&&
                            ((x.parent.right.right==null)||(x.parent.right.right.colour=='B'))){
                        System.out.println("Case3");
                        x.parent.right.colour='R';
                        System.out.println("Case3a");
                        checkDelete(x);
                        System.out.println("Case3b");
                        deleteBlack(x.parent);
                        System.out.println("Case3c");
                    }
                    //case5
                    else if (((x.parent.right.left!=null)&&(x.parent.right.left.colour=='R'))&&
                            ((x.parent.right.right==null)||(x.parent.right.right.colour=='B'))){
                        System.out.println("Case5");

                        rightRotate(x.parent.right);
                        x.parent.right.colour='B';
                        x.parent.right.right.colour='R';
                        deleteBlack(x);
                    }
                }
                else if (x.parent.right!=null && x.parent.right.colour=='R'){
                    System.out.println("Case2");
                    leftRotate(x.parent);
                    x.parent.colour='R';
                    x.parent.parent.colour='B';
                    deleteBlack(x);
                }
            }
            else if (x.parent!=null && x==x.parent.right){
                if (x.parent.left!=null && x.parent.left.colour=='B' && x.parent.left.left!=null && x.parent.left.left.colour=='R'){
                    System.out.println("Case6");
                    rightRotate(x.parent);
                    x.parent.parent.colour=x.parent.colour;
                    x.parent.colour='B';
                    x.parent.parent.left.colour='B';
                    x.colour='B';
                    x.parent.right=null;
                    checkDelete(x);
                }
                else if (x.parent.left!=null && x.parent.left.colour=='R'){
                    System.out.println("Case2");
                    rightRotate(x.parent);
                    x.parent.colour='R';
                    x.parent.parent.colour='B';
                    deleteBlack(x);
                }
                else if (x.parent.left!=null && x.parent.left.colour=='B'){

                    //case3
                    if (((x.parent.left.left==null)||(x.parent.left.left.colour=='B'))&&
                            ((x.parent.left.right==null)||(x.parent.left.right.colour=='B'))){
                        System.out.println("Case3");
                        x.parent.left.colour='R';
                        Node<T> temp=x.parent;
                        checkDelete(x);
                        deleteBlack(temp);
                    }
                    //case5
                    else if (((x.parent.left.left==null)||(x.parent.left.left.colour=='B'))&&
                            ((x.parent.left.right!=null)&&(x.parent.left.right.colour=='R'))){
                        System.out.println("Case5");
                        leftRotate(x.parent.left);
                        x.parent.left.colour='B';
                        x.parent.left.left.colour='R';
                        deleteBlack(x);
                    }
                }
            }
            //case1-ending
            else if (x==root){
                System.out.println("Case1");
                x.colour='B';
            }
        }
    }

    private void checkDelete(Node<T> x){
        if (x.left==null && x.right==null){
            if (x==x.parent.left){
                x.parent.left=null;
            }
            else{
                x.parent.right=null;
            }
        }
    }



}
