#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include <time.h>

typedef
struct node *pt;
struct node {
    jint key;
    pt l, r, p;
    char c;
};

pt temp, suc;
pt root_c = NULL;

void addRandom(jint jmin, jint jmax, jint jintegersNumber);

void add(jint data);

void addNode(pt *x, jint data);

void prjintInOrder(pt x);

void prjintPreOrder(pt x);

void delete1(jint data);

void deleteNode(pt *x, jint data);

void deleteCases(pt *x);

void findSuccessor(pt x);

void checkDelete(pt *x);

void deleteBlack(pt *x);

jint check(pt x, jint data);

void leftRotate(pt *x);

void rightRotate(pt *x);

void insertFixup(pt *x);


jint random1(jint min, jint max);

void deleteTree(pt *x);

JNIEXPORT void JNICALL
Java_com_example_bill_androidredblacktree_RBTreeBenchmarks_RBTreeC(
        JNIEnv *env,
        jobject this,
        jint jmin,
        jint jmax,
        jint jintegersNumber,
        jint jchoice) {


    srand(time(NULL));

    if (jchoice == 1) { //insert random jints between min,max
        addRandom(jmin, jmax, jintegersNumber);
    } else if (jchoice == 2) { //delete entire tree
        deleteTree(&root_c);
        root_c = NULL;
    }
}

void addRandom(jint jmin, jint jmax, jint jintegersNumber) {
    int i=0;
    while (i < jintegersNumber) {
        jint randomInt = random1(jmin, jmax + 1);
        if (check(root_c, randomInt) == 0) {
            add(randomInt);
            i++;
        }
    }
}

jint random1(jint min, jint max) // [min, max)
{
    return min + rand() % ((max + 1) - min);

}

void deleteTree(pt *x) {
    if ((*x) == NULL) {
        return;
    }
    deleteTree(&(*x)->l);
    deleteTree(&(*x)->r);
    (*x)->l = NULL;
    (*x)->r = NULL;
    (*x)->p = NULL;
    free((*x));
}

void add(jint data) {
    if (check(root_c, data) == 0) {
        addNode(&root_c, data);
    }
}

jint check(pt x, jint data) {
    if (x == NULL)
        return 0;
    else {
        if ((x->l != NULL) && (x->key > data))
            return check(x->l, data);
        else if ((x->r != NULL) && (x->key < data))
            return check(x->r, data);
        if (x->key == data)
            return 1;
        return 0;
    }
}

void addNode(pt *x, jint data) {

    if (root_c == NULL) {
        root_c = (pt) malloc(sizeof(struct node));
        root_c->key = data;
        root_c->p = NULL;
        root_c->l = NULL;
        root_c->r = NULL;
        root_c->c = 'B';
    } else {
        if ((*x)->key > data) {
            if ((*x)->l != NULL)
                addNode(&(*x)->l, data);
            else {
                temp = (pt) malloc(sizeof(struct node));
                temp->key = data;
                (*x)->l = temp;
                temp->p = (*x);
                temp->l = NULL;
                temp->r = NULL;
                temp->c = 'R';
                insertFixup(&temp);
            }
        } else if ((*x)->key < data) {
            if ((*x)->r != NULL) {
                addNode(&(*x)->r, data);
            } else {
                temp = (pt) malloc(sizeof(struct node));
                temp->key = data;
                (*x)->r = temp;
                temp->p = (*x);
                temp->l = NULL;
                temp->r = NULL;
                temp->c = 'R';
                insertFixup(&temp);
            }
        }
    }
}

void prjintInOrder(pt x) {

    if (x == NULL)
        printf("Empty tree");

    else {
        if (x->l != NULL)
            prjintInOrder(x->l);
        printf("%d %c\n", x->key, x->c);
        if (x->r != NULL)
            prjintInOrder(x->r);
    }
}

void prjintPreOrder(pt x) {
    if (x == NULL)
        printf("Empty tree");
    else {
        printf("%d %c\n", x->key, x->c);
        if (x->l != NULL)
            prjintPreOrder(x->l);
        if (x->r != NULL)
            prjintPreOrder(x->r);
    }
}

void rightRotate(pt *x) {

    pt y = (*x)->l, temp1 = (*x)->p;

    (*x)->l = y->r;
    if (y->r != NULL) {
        y->r->p = (*x);
    }
    if ((*x) == root_c) {
        root_c = y;
    } else if ((*x) == (*x)->p->l) {
        (*x)->p->l = y;
    } else {
        (*x)->p->r = y;
    }
    y->r = (*x);
    (*x)->p = y;
    if ((*x)->p != NULL) {
        (*x)->p->p = temp1;
    } else {
        (*x)->p->p = NULL;
    }


}

void leftRotate(pt *x) {

    pt y, temp1 = (*x)->p;
    y = (*x)->r;

    (*x)->r = y->l;
    if (y->l != NULL) {
        y->l->p = (*x);
    }
    if (root_c == (*x)) {
        root_c = y;
    } else if ((*x) == (*x)->p->l) {
        (*x)->p->l = y;
    } else {
        (*x)->p->r = y;
    }
    y->l = (*x);
    (*x)->p = y;
    if ((*x)->p != NULL) {
        (*x)->p->p = temp1;
    } else {
        (*x)->p->p = NULL;
    }
}

void insertFixup(pt *x) {

    while ((*x)->p != NULL && (*x)->p->c == 'R') {
        if ((*x)->p == (*x)->p->p->l) {
            pt unc = (*x)->p->p->r;
            if ((unc != NULL) && (unc->c == 'R')) {
                unc->c = 'B';
                (*x)->p->c = 'B';
                (*x)->p->p->c = 'R';
                (*x) = (*x)->p->p;
            } else {
                if ((*x) == (*x)->p->r) {
                    (*x) = (*x)->p;
                    leftRotate(&(*x));
                }
                (*x)->p->c = 'B';
                (*x)->p->p->c = 'R';
                rightRotate(&(*x)->p->p);
            }
        } else if ((*x)->p == (*x)->p->p->r) {
            pt unc = (*x)->p->p->l;
            if ((unc != NULL) && (unc->c == 'R')) {
                unc->c = 'B';
                (*x)->p->c = 'B';
                (*x)->p->p->c = 'R';
                (*x) = (*x)->p->p;
            } else {
                if ((*x) == (*x)->p->l) {
                    (*x) = (*x)->p;
                    rightRotate(&(*x));
                }
                (*x)->p->c = 'B';
                (*x)->p->p->c = 'R';
                leftRotate(&(*x)->p->p);
            }
        }
    }
    root_c->c = 'B';
}

void delete1(jint data) {
    if (check(root_c, data) == 1) {
        deleteNode(&root_c, data);
    }
}

void deleteNode(pt *x, jint data) {

    if (((*x)->l != NULL) && ((*x)->key > data))
        deleteNode(&(*x)->l, data);
    if (((*x)->r != NULL) && ((*x)->key < data))
        deleteNode(&(*x)->r, data);
    if ((*x)->key == data) {
        if (((*x)->l != NULL) && ((*x)->r != NULL)) {
            findSuccessor((*x)->r);
            (*x)->key = suc->key;
            deleteCases(&suc);
        } else {
            deleteCases(&(*x));
        }
    }
}

void findSuccessor(pt x) {
    if (x->l != NULL)
        findSuccessor(x->l);
    else
        suc = x;
}

void deleteCases(pt *x) {
    pt aux = (*x);
    if ((aux == root_c) && (aux->l == NULL) && (aux->r == NULL)) {
        free(*x);
        root_c = NULL;
    } else if (aux->c == 'R') {

        if ((aux->l == NULL) && (aux->r == NULL)) {


            if (aux == aux->p->l) {
                aux->p->l = NULL;
            } else
                aux->p->r = NULL;
        } else if ((aux->l != NULL) && (aux->r == NULL)) {

            if (aux == aux->p->l)
                aux->p->l = aux->l;
            else
                aux->p->r = aux->l;
            aux->l->p = aux->p;
        } else if ((aux->l == NULL) && (aux->r != NULL)) {

            if (aux == aux->p->l)
                aux->p->l = aux->r;
            else
                aux->p->r = aux->r;
            aux->r->p = aux->p;
        }
        free(aux);
    } else if (aux->c == 'B') {

        if ((aux->l != NULL) && (aux->r == NULL) && (aux->l->c == 'R')) {

            aux->key = aux->l->key;
            pt temp1 = aux->l;
            aux->l = NULL;
            free(temp1);
        } else if ((aux->r != NULL) && (aux->l == NULL) && (aux->r->c == 'R')) {

            aux->key = aux->r->key;
            pt temp1 = aux->r;
            aux->r = NULL;
            free(temp1);
        } else {

            deleteBlack(&aux);
        }
    }
}

void checkDelete(pt *x) {
    if (((*x)->l == NULL) && ((*x)->r == NULL)) {
        if ((*x) == (*x)->p->l)
            (*x)->p->l = NULL;
        else
            (*x)->p->r = NULL;
        free((*x));
    }
}

void deleteBlack(pt *x) {
    pt aux = (*x);
    if ((aux->p != NULL) && (aux->p->c == 'R')) {
        if (aux == aux->p->l) {
            if (((aux->p->r->l == NULL) || (aux->p->r->l->c == 'B')) &&
                ((aux->p->r->r == NULL) || (aux->p->r->r->c == 'B'))) {
                aux->p->c = 'B';
                aux->p->r->c = 'R';
                aux->c = 'B';
                checkDelete(&aux);
            } else if ((aux->p->r->r != NULL) && (aux->p->r->r->c == 'R')) {
                leftRotate(&aux->p);
                aux->p->p->c = aux->p->c;
                aux->p->c = 'B';
                aux->p->p->r->c = 'B';
                aux->c = 'B';
                checkDelete(&aux);
            }
        } else if (aux == aux->p->r) {
            if (((aux->p->l->l == NULL) || (aux->p->l->l->c == 'B')) &&
                ((aux->p->l->r == NULL) || (aux->p->l->r->c == 'B'))) {
                aux->p->c = 'B';
                aux->p->l->c = 'R';
                aux->c = 'B';
                checkDelete(&aux);
            } else if ((aux->p->l->l != NULL) && (aux->p->l->l->c == 'R')) {
                rightRotate(&aux->p);
                aux->p->p->c = aux->p->c;
                aux->p->c = 'B';
                aux->p->p->l->c = 'B';
                aux->c = 'B';
                checkDelete(&aux);
            }
        }
    } else {
        if ((aux->p != NULL) && (aux == aux->p->l)) {
            //case6
            if ((aux->p->r != NULL) && (aux->p->r->c == 'B') && (aux->p->r->r != NULL) &&
                (aux->p->r->r->c == 'R')) {
                leftRotate(&aux->p);
                aux->p->p->c = aux->p->c;
                aux->p->c = 'B';
                aux->p->p->r->c = 'B';
                aux->c = 'B';
                checkDelete(&aux);
            } else if ((aux->p->r != NULL) && (aux->p->r->c == 'B')) {
                //case3
                if (((aux->p->r->l == NULL) || (aux->p->r->l->c == 'B')) &&
                    ((aux->p->r->r == NULL) || (aux->p->r->r->c == 'B'))) {
                    aux->p->r->c = 'R';
                    pt temp1 = aux->p;
                    checkDelete(&aux);
                    deleteBlack(&temp1);
                }
                    //case5
                else if (((aux->p->r->l != NULL) && (aux->p->r->l->c == 'R')) &&
                         ((aux->p->r->r == NULL) || (aux->p->r->r->c == 'B'))) {
                    rightRotate(&aux->p->r);
                    aux->p->r->c = 'B';
                    aux->p->r->r->c = 'R';
                    deleteBlack(&aux);
                }
            } else if ((aux->p->r != NULL) && (aux->p->r->c == 'R')) {
                leftRotate(&aux->p);
                aux->p->c = 'R';
                aux->p->p->c = 'B';
                deleteBlack(&aux);
            }
        } else if ((aux->p != NULL) && (aux == aux->p->r)) {
            if ((aux->p->l != NULL) && (aux->p->l->c == 'B') && (aux->p->l->l != NULL) &&
                (aux->p->l->l->c == 'R')) {
                rightRotate(&aux->p);
                aux->p->p->c = aux->p->c;
                aux->p->c = 'B';
                aux->p->p->l->c = 'B';
                aux->c = 'B';
                aux->p->r = NULL;
                checkDelete(&aux);
            } else if ((aux == aux->p->r) && (aux->p->l != NULL) && (aux->p->l->c == 'R')) {
                rightRotate(&aux->p);
                aux->p->c = 'R';
                aux->p->p->c = 'B';
                deleteBlack(&aux);
            } else if ((aux == aux->p->r) && (aux->p->l != NULL) && (aux->p->l->c == 'B')) {

                //case3
                if (((aux->p->l->l == NULL) || (aux->p->l->l->c == 'B')) &&
                    ((aux->p->l->r == NULL) || (aux->p->l->r->c == 'B'))) {
                    aux->p->l->c = 'R';
                    pt temp1 = aux->p;
                    checkDelete(&aux);
                    deleteBlack(&temp1);
                }
                    //case5
                else if (((aux->p->l->l == NULL) || (aux->p->l->l->c == 'B')) &&
                         ((aux->p->l->r != NULL) && (aux->p->l->r->c == 'R'))) {
                    leftRotate(&aux->p->l);
                    aux->p->l->c = 'B';
                    aux->p->l->l->c = 'R';
                    deleteBlack(&aux);
                }
            }
        }
            //case1-ending
        else if (aux == root_c) {

            aux->c = 'B';
        }
    }
}
