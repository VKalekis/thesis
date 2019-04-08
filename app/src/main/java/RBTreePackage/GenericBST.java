package RBTreePackage;

public class GenericBST<T extends Comparable<T>> {
    public Node<T> root;

    public GenericBST() {
        root=null;
    }

    public Node<T> getRoot(){

        return root;
    }

    public Node<T> add(T data) {

        if (!check(data)) {
            Node<T> temp;
            temp = this.addNode(root, data);
            return temp;
        }
        return null;
    }

    public void delete(T datadel){
        if (this.check(datadel))
            this.deleteNode(root, datadel);
    }


    private Node<T> addNode(Node<T> x, T data) {
        if (root==null) {
            Node<T> newNode = new Node<T>(data);
            root=newNode;
            System.out.print("Root inserted"+newNode.key+root.key);
            return newNode;
        }
        else {
            int comp1=data.compareTo(x.key);
            if (comp1>0) {
                if (x.right != null) {
                    return addNode(x.right, data);
                } else {
                    Node<T> newNode = new Node<T>(data);
                    x.right = newNode;
                    newNode.parent = x;
                    return newNode;
                }
            }
            if (comp1<0) {
                if (x.left != null) {
                    return addNode(x.left, data);
                } else {
                    Node<T> newNode = new Node<T>(data);
                    x.left = newNode;
                    newNode.parent = x;
                    return newNode;
                }
            }
        }
        return null;
    }

    public boolean check(T data){
        return check(root,data);
    }

    private boolean check(Node<T> x, T data){

        if (x==null)
            return false;
        else {
            int comp2=(x.key).compareTo(data);
            if (x.left != null && comp2>0)
                return check(x.left,data);
            else if (x.right != null && comp2<0)
                return check(x.right,data);
            if (comp2==0)
                return true;
            return false;
        }
    }

    public void printInOrder(){
        System.out.println("Tree:");
        printInOrder(root);
    }

    private void printInOrder(Node<T> x) {
        if (x==null)
            System.out.println("Empty BST");
        else{
            if (x.left!=null)
                printInOrder(x.left);
            System.out.print(x.key+"\n");
            if (x.right!=null)
                printInOrder(x.right);
        }
    }

    private void deleteNode(Node<T> x, T data){

        int comp3=(x.key).compareTo(data);
        if (x.right!=null && comp3<0)
            deleteNode(x.right,data);
        if (x.left!=null && comp3>0)
            deleteNode(x.left,data);
        if (comp3==0){
            int comp4=(x.parent.key).compareTo(x.key);
            if (x.left==null && x.right==null){
                if (x==root)
                    root=null;
                else if (comp4<0)
                    x.parent.right=null;
                else
                    x.parent.left=null;
            }
            else if (x.left!=null && x.right==null){
                if (x==root) {
                    root=x.left;
                    x.left.parent=x.left;
                }
                else if (comp4<0)
                    x.parent.right=x.left;
                else
                    x.parent.left=x.left;
                x.left.parent=x.parent;
            }
            else if (x.right!=null && x.left==null){
                if (x==root){
                    root=x.right;
                    x.right.parent=x.right;
                }
                else if (comp4<0) {
                    x.parent.right=x.right;
                }
                else
                    x.parent.left=x.right;
                x.right.parent=x.parent;
            }
            else{
                Node<T> suc = findSuccessor(x.right);
                x.key=suc.key;
                deleteNode(suc, suc.key);
            }
        }
    }

    public Node<T> findSuccessor(Node<T> x){
        while (x.left!=null)
            x=x.left;
        return x;

    }

    class Node<T> {
        T key;
        char colour;
        Node<T> left, right, parent;

        private Node(T data){
            key=data;
            left=null;
            right=null;
            parent=null;
            colour=' ';
        }

    }
}

