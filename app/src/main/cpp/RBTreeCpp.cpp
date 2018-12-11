#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include <iostream>
#include <string>

using namespace std;

typedef
struct node *pt;
struct node{
    int key;
    pt l, r, p;
    char c;
};

pt temp, suc;
pt root=NULL;

void add(int data);
void addNode(pt *x,int data);
void printInOrder(pt x);
void printPreOrder(pt x);
void delete1(int data);
void deleteNode(pt *x, int data);
void deleteCases(pt *x);
void findSuccessor(pt x);
void checkDelete(pt *x);
void deleteBlack(pt *x);
int check(pt x, int data);

void leftRotate(pt *x);
void rightRotate(pt *x);
void insertFixup(pt *x);

int height(pt x);
void printLevelOrder(pt x);
void printGivenLevel(pt x, int level);

int random(int min,int max);
void deleteTree(pt *x);

string outputTree;

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_bill_androidredblacktree_MainActivity_RBTreeCpp(
        JNIEnv *env,
        jobject /* this */,
        jint jInput,
        jint jmin,
        jint jmax,
        jint jintegersNumber,
        jint jchoice){

    int insertValue = jInput;
    int max = jmax;
    int min = jmin;
    int integersNumber = jintegersNumber;
    int choice = jchoice;

    if (choice==0){ //insert one value
        add(insertValue);
    }
    else if (choice==1){ //insert random ints between min,max
        int i = 0;
        while (i < integersNumber) {
            int randomInt = random(min,max+1);
            if(check(root,randomInt)==0){
                add(randomInt);
                i++;
            }
        }
    }
    else if (choice==2){ //delete entire tree
        deleteTree(&root);
        root=NULL;
    }
    //default: print tree
    printLevelOrder(root);

    return env->NewStringUTF(outputTree.c_str());
}

int random(int min, int max) // [min, max)
{
    static bool first = true;
    if (first){
        srand( time(NULL) );
        first = false;
    }
    return min + rand() % (( max + 1 ) - min);
}

void deleteTree(pt *x){
    if ((*x) == NULL) {
        return;
    }
    deleteTree(&(*x)->l);
    deleteTree(&(*x)->r);
    (*x)->l=NULL;
    (*x)->r=NULL;
    (*x)->p=NULL;
    free((*x));
}

void add(int data){
    if (check(root,data)==0){
        addNode(&root,data);
    }
}

int check(pt x, int data){
    if (x==NULL)
        return 0;
    else {
        if ((x->l != NULL) && (x->key>data))
            return check(x->l,data);
        else if ((x->r != NULL) && (x->key<data))
            return check(x->r,data);
        if (x->key==data)
            return 1;
        return 0;
    }
}

void addNode(pt *x,int data){
    puts("Here");
    if (root==NULL){
        root=(pt)malloc(sizeof(struct node));
        root->key=data;
        root->p=NULL;
        root->l=NULL;
        root->r=NULL;
        root->c='B';
    }
    else{
        if((*x)->key > data){
            if((*x)->l!=NULL)
                addNode(&(*x)->l,data);
            else{
                temp=(pt)malloc(sizeof(struct node));
                temp->key=data;
                (*x)->l=temp;
                temp->p=(*x);
                temp->l=NULL;
                temp->r=NULL;
                temp->c='R';
                insertFixup(&temp);
            }
        }
        else if((*x)->key < data){
            if((*x)->r!=NULL){
                addNode(&(*x)->r,data);
            }
            else{
                temp=(pt)malloc(sizeof(struct node));
                temp->key=data;
                (*x)->r=temp;
                temp->p=(*x);
                temp->l=NULL;
                temp->r=NULL;
                temp->c='R';
                insertFixup(&temp);
            }
        }
    }
}

void printInOrder(pt x){

    if(x==NULL)
        printf("Empty tree");

    else{
        if(x->l!=NULL)
            printInOrder(x->l);
        printf("%d %c\n", x->key, x->c);
        if(x->r!=NULL)
            printInOrder(x->r);
    }
}

void printPreOrder(pt x){
    if(x==NULL)
        printf("Empty tree");
    else{
        printf("%d %c\n", x->key, x->c);
        if(x->l!=NULL)
            printPreOrder(x->l);
        if(x->r!=NULL)
            printPreOrder(x->r);
    }
}

void rightRotate(pt *x){

    pt y=(*x)->l, temp1=(*x)->p;

    (*x)->l=y->r;
    if (y->r!= NULL){
        y->r->p=(*x);
    }
    if ((*x)==root){
        root=y;
    }
    else if ((*x)==(*x)->p->l){
        (*x)->p->l=y;
    }
    else{
        (*x)->p->r=y;
    }
    y->r=(*x);
    (*x)->p=y;
    if ((*x)->p!=NULL){
        (*x)->p->p=temp1;
    }
    else{
        (*x)->p->p=NULL;
    }


}

void leftRotate(pt *x){

    pt y, temp1=(*x)->p;
    y=(*x)->r;

    (*x)->r=y->l;
    if (y->l!=NULL){
        y->l->p=(*x);
    }
    if (root==(*x)){
        root=y;
    }
    else if ((*x)==(*x)->p->l){
        (*x)->p->l = y;
    }
    else{
        (*x)->p->r=y;
    }
    y->l=(*x);
    (*x)->p=y;
    if ((*x)->p!=NULL){
        (*x)->p->p=temp1;
    }
    else{
        (*x)->p->p=NULL;
    }
}

void insertFixup(pt *x){

    while ((*x)->p!=NULL && (*x)->p->c=='R'){
        if ((*x)->p==(*x)->p->p->l){
            pt unc=(*x)->p->p->r;
            if ((unc!=NULL)&&(unc->c=='R')){
                unc->c='B';
                (*x)->p->c='B';
                (*x)->p->p->c='R';
                (*x)=(*x)->p->p;
            }
            else {
                if ((*x)==(*x)->p->r){
                    (*x)=(*x)->p;
                    leftRotate(&(*x));
                }
                (*x)->p->c='B';
                (*x)->p->p->c='R';
                rightRotate(&(*x)->p->p);
            }
        }
        else if ((*x)->p==(*x)->p->p->r) {
            pt unc=(*x)->p->p->l;
            if ((unc!=NULL)&&(unc->c=='R')){
                unc->c='B';
                (*x)->p->c='B';
                (*x)->p->p->c='R';
                (*x)=(*x)->p->p;
            }
            else{
                if ((*x)==(*x)->p->l){
                    (*x)=(*x)->p;
                    rightRotate(&(*x));
                }
                (*x)->p->c='B';
                (*x)->p->p->c='R';
                leftRotate(&(*x)->p->p);
            }
        }
    }
    root->c='B';
}

void printLevelOrder(pt x){
    int h = height(x);
    int i;
    outputTree="C++\n";
    for (i=1; i<=h; i++){
        printGivenLevel(x, i);
        outputTree=outputTree.append("\n");
        printf("\n");
    }
}

void printGivenLevel(pt x, int level){
    if (x == NULL)
        return;
    if (level == 1){
        outputTree=outputTree.append(" "+to_string(x->key)+" "+x->c);
        printf(" %d %c ", x->key,x->c);
        if (x->l!=NULL){
            outputTree=outputTree.append("l");
            printf("l");
        }
        if (x->r!=NULL){
            outputTree=outputTree.append("r");
            printf("r");
        }
    }
    else if (level > 1){
        printGivenLevel(x->l, level-1);
        printGivenLevel(x->r, level-1);
    }
}

int height(pt x){
    if (x==NULL)
        return 0;
    else{
        int lheight = height(x->l);
        int rheight = height(x->r);

        if (lheight > rheight)
            return(lheight+1);
        else return(rheight+1);
    }
}

void delete1(int data){
    if (check(root,data)==1){
        deleteNode(&root,data);
    }
}

void deleteNode(pt *x, int data){

    if(((*x)->l!=NULL)&&((*x)->key>data))
        deleteNode(&(*x)->l, data);
    if(((*x)->r!=NULL)&&((*x)->key<data))
        deleteNode(&(*x)->r, data);
    if((*x)->key==data){
        if (((*x)->l!=NULL)&&((*x)->r!=NULL)){
            findSuccessor((*x)->r);
            (*x)->key=suc->key;
            deleteCases(&suc);
        }
        else{
            deleteCases(&(*x));
        }
    }
}

void findSuccessor(pt x){
    if(x->l!=NULL)
        findSuccessor(x->l);
    else
        suc=x;
}

void deleteCases(pt *x){
    pt aux=(*x);
    if ((aux==root) && (aux->l==NULL) && (aux->r==NULL)){
        puts("Del0");
        free(*x);
        root=NULL;
    }
    else if (aux->c=='R'){

        if ((aux->l==NULL)&&(aux->r==NULL)){
            puts("Del1");

            if (aux==aux->p->l){
                aux->p->l=NULL;}
            else
                aux->p->r=NULL;
        }
        else if ((aux->l!=NULL)&&(aux->r==NULL)){
            puts("Del2");
            if (aux==aux->p->l)
                aux->p->l=aux->l;
            else
                aux->p->r=aux->l;
            aux->l->p=aux->p;
        }
        else if ((aux->l==NULL)&&(aux->r!=NULL)){
            puts("Del3");
            if (aux==aux->p->l)
                aux->p->l=aux->r;
            else
                aux->p->r=aux->r;
            aux->r->p=aux->p;
        }
        free(aux);
    }

    else if (aux->c=='B'){

        if ((aux->l!=NULL)&&(aux->r==NULL)&&(aux->l->c=='R')){
            puts("Del4");
            aux->key=aux->l->key;
            pt temp1=aux->l;
            aux->l=NULL;
            free(temp1);
        }
        else if ((aux->r!=NULL)&&(aux->l==NULL)&&(aux->r->c=='R')){
            puts("Del5");
            aux->key=aux->r->key;
            pt temp1=aux->r;
            aux->r=NULL;
            free(temp1);
        }
        else{
            puts("Del6");
            deleteBlack(&aux);
        }
    }
}

void checkDelete(pt *x){
    if (((*x)->l==NULL)&&((*x)->r==NULL)){
        if ((*x)==(*x)->p->l)
            (*x)->p->l=NULL;
        else
            (*x)->p->r=NULL;
        free((*x));
    }
}

void deleteBlack(pt *x){
    pt aux=(*x);
    if ((aux->p!=NULL)&&(aux->p->c=='R')){
        if (aux==aux->p->l){
            if (((aux->p->r->l==NULL)||(aux->p->r->l->c=='B'))&&((aux->p->r->r==NULL)||(aux->p->r->r->c=='B'))){
                aux->p->c='B';
                aux->p->r->c='R';
                aux->c='B';
                checkDelete(&aux);
            }
            else if ((aux->p->r->r!=NULL)&&(aux->p->r->r->c=='R')){
                leftRotate(&aux->p);
                aux->p->p->c=aux->p->c;
                aux->p->c='B';
                aux->p->p->r->c='B';
                aux->c='B';
                checkDelete(&aux);
            }
        }
        else if (aux==aux->p->r){
            if (((aux->p->l->l==NULL)||(aux->p->l->l->c=='B'))&&((aux->p->l->r==NULL)||(aux->p->l->r->c=='B'))){
                aux->p->c='B';
                aux->p->l->c='R';
                aux->c='B';
                checkDelete(&aux);
            }
            else if ((aux->p->l->l!=NULL)&&(aux->p->l->l->c=='R')){
                rightRotate(&aux->p);
                aux->p->p->c=aux->p->c;
                aux->p->c='B';
                aux->p->p->l->c='B';
                aux->c='B';
                checkDelete(&aux);
            }
        }
    }

    else {
        if ((aux->p!=NULL)&&(aux==aux->p->l)){
            //case6
            if ((aux->p->r!=NULL)&&(aux->p->r->c=='B')&&(aux->p->r->r!=NULL)&&(aux->p->r->r->c=='R')){
                leftRotate(&aux->p);
                aux->p->p->c=aux->p->c;
                aux->p->c='B';
                aux->p->p->r->c='B';
                aux->c='B';
                checkDelete(&aux);
            }
            else if ((aux->p->r!=NULL)&&(aux->p->r->c=='B')){
                //case3
                if (((aux->p->r->l==NULL)||(aux->p->r->l->c=='B'))&&((aux->p->r->r==NULL)||(aux->p->r->r->c=='B'))){
                    aux->p->r->c='R';
                    pt temp1=aux->p;
                    checkDelete(&aux);
                    deleteBlack(&temp1);
                }
                    //case5
                else if (((aux->p->r->l!=NULL)&&(aux->p->r->l->c=='R'))&&((aux->p->r->r==NULL)||(aux->p->r->r->c=='B'))){
                    rightRotate(&aux->p->r);
                    aux->p->r->c='B';
                    aux->p->r->r->c='R';
                    deleteBlack(&aux);
                }
            }
            else if ((aux->p->r!=NULL)&&(aux->p->r->c=='R')){
                leftRotate(&aux->p);
                aux->p->c='R';
                aux->p->p->c='B';
                deleteBlack(&aux);
            }
        }
        else if ((aux->p!=NULL)&&(aux==aux->p->r)){
            if ((aux->p->l!=NULL)&&(aux->p->l->c=='B')&&(aux->p->l->l!=NULL)&&(aux->p->l->l->c=='R')){
                rightRotate(&aux->p);
                aux->p->p->c=aux->p->c;
                aux->p->c='B';
                aux->p->p->l->c='B';
                aux->c='B';
                aux->p->r=NULL;
                checkDelete(&aux);
            }
            else if ((aux==aux->p->r)&&(aux->p->l!=NULL)&&(aux->p->l->c=='R')){
                rightRotate(&aux->p);
                aux->p->c='R';
                aux->p->p->c='B';
                deleteBlack(&aux);
            }
            else if ((aux==aux->p->r)&&(aux->p->l!=NULL)&&(aux->p->l->c=='B')){

                //case3
                if (((aux->p->l->l==NULL)||(aux->p->l->l->c=='B'))&&((aux->p->l->r==NULL)||(aux->p->l->r->c=='B'))){
                    aux->p->l->c='R';
                    pt temp1=aux->p;
                    checkDelete(&aux);
                    deleteBlack(&temp1);
                }
                    //case5
                else if (((aux->p->l->l==NULL)||(aux->p->l->l->c=='B'))&&((aux->p->l->r!=NULL)&&(aux->p->l->r->c=='R'))){
                    leftRotate(&aux->p->l);
                    aux->p->l->c='B';
                    aux->p->l->l->c='R';
                    deleteBlack(&aux);
                }
            }
        }
            //case1-ending
        else if (aux==root){

            aux->c='B';
        }
    }
}
