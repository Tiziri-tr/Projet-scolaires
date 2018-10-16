#include <iostream>
#include <cv.h>
#include<highgui.h>
#include <algorithm>
#include <vector>
#include <stdlib.h>
#include <stdio.h>

#define TAILLE_TABLEAU 9

using namespace std;
void filtreMedian(IplImage *img_nvg);
int *trieCroissant(int *tab,int taille);
void afficherTab(int tab[],int taille);

int main(){
    IplImage *image= cvLoadImage("C:\\Users\\Rachid\\Documents\\test1\\median\\lena.png");
    IplImage *image_nvg= cvCreateImage( cvGetSize(image), image->depth,1);
    int flip= 0;
    if(image->origin != IPL_ORIGIN_TL){
        flip = CV_CVTIMG_FLIP;
    }

    cvConvertImage(image,image_nvg,flip);
    cvNamedWindow("Avant filtre",CV_WINDOW_AUTOSIZE);
    cvShowImage("Avant filtre",image_nvg);
    filtreMedian(image_nvg);
    cvNamedWindow("Apres filtre median",CV_WINDOW_AUTOSIZE);
    cvShowImage("Apres filtre median",image_nvg);

    cvConvertImage(image,image_nvg,flip);
    cvSmooth(image_nvg, image_nvg, CV_MEDIAN, 3);
    cvNamedWindow("Apres filtre median PREDEFINIE",CV_WINDOW_AUTOSIZE);
    cvShowImage("Apres filtre median PREDEFINIE",image_nvg);


    cvWaitKey(0);
    cvDestroyWindow("Avant filtre");
    cvDestroyWindow("Apres filtre median");
    cvDestroyWindow("Apres filtre median PREDEFINIE");
    cvReleaseImage(&image);
    cvReleaseImage(&image_nvg);
    return 0;
}


void afficherTab(int tab[],int taille){
    cout<<"tab["<<tab[0];
    for(int i=1;i<taille;i++)
        cout<<" ; "<<tab[i];
    cout<<"]\n"<<endl;

}

int *trieCroissant(int *tab,int taille){
    int mini,imin,tmp;
    for(int i=0;i<taille-1;i++){
      imin=i;
      mini=tab[i];
      for(int j=i;j<taille;j++)
        if(tab[j]<mini){
            mini=tab[j];
            imin=j;
      }
      tmp=tab[imin];
      tab[imin]=tab[i];
      tab[i]=tmp;
    }

    return tab;
}

void filtreMedian(IplImage *img_nvg){

    CvScalar scalaire;
    CvScalar scalaire1;
    CvScalar scalaire2;
    CvScalar scalaire3;
    CvScalar scalaire4;
    CvScalar scalaire5;
    CvScalar scalaire6;
    CvScalar scalaire7;
    CvScalar scalaire8;
    CvScalar scalaire9;

    int *tab = new int[TAILLE_TABLEAU];

    for(int x=1;x<img_nvg->width-1;x++){
            for(int y = 1; y<img_nvg->height-1;y++){

                 scalaire1 = cvGet2D(img_nvg, y, x);
                 tab[0]= scalaire1.val[0];
                 scalaire2 = cvGet2D(img_nvg, y+1, x);
                 tab[1]= scalaire2.val[0];
                 scalaire3 = cvGet2D(img_nvg, y+1, x+1);
                 tab[2]= scalaire3.val[0];
                 scalaire4 = cvGet2D(img_nvg, y, x+1);
                 tab[3]= scalaire4.val[0];
                 scalaire5 = cvGet2D(img_nvg, y, x-1);
                 tab[4]= scalaire5.val[0];
                 scalaire6 = cvGet2D(img_nvg, y-1, x);
                 tab[5]= scalaire6.val[0];
                 scalaire7 = cvGet2D(img_nvg, y-1, x+1);
                 tab[6]= scalaire7.val[0];
                 scalaire8 = cvGet2D(img_nvg, y-1, x-1);
                 tab[7]= scalaire8.val[0];
                 scalaire9 = cvGet2D(img_nvg, y+1, x-1);
                 tab[8]= scalaire1.val[0];

        // TRIER TAB
        tab = trieCroissant(tab,TAILLE_TABLEAU);
        int moitieTableau = TAILLE_TABLEAU / 2;

        scalaire.val[0]= tab[TAILLE_TABLEAU/2];
        cvSet2D(img_nvg,y,x, scalaire);


            }


}
}
