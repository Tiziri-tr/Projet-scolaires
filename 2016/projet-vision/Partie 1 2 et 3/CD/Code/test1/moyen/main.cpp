#include <iostream>
#include <cv.h>
#include<highgui.h>
#include <algorithm>
#include <vector>
#include <stdlib.h>
#include <stdio.h>

#define TAILLE_TABLEAU 9

using namespace std;
void filtreMoyen(IplImage *img_nvg);

int main(){
    IplImage *image= cvLoadImage("D:\\9raya\\M2 SII\\VISION\\tp\\moyen\\lena.png");
    IplImage *image_nvg= cvCreateImage( cvGetSize(image), image->depth,1);
    int flip= 0;
    if(image->origin != IPL_ORIGIN_TL){
        flip = CV_CVTIMG_FLIP;
    }

    cvConvertImage(image,image_nvg,flip);
    cvNamedWindow("Avant filtre",CV_WINDOW_AUTOSIZE);
    cvShowImage("Avant filtre",image_nvg);
    filtreMoyen(image_nvg);
    cvNamedWindow("Apres filtre moyen",CV_WINDOW_AUTOSIZE);
    cvShowImage("Apres filtre moyen",image_nvg);

    cvConvertImage(image,image_nvg,flip);
    /*************/
    CvMat *masque = cvCreateMat(3,3,CV_32FC1);
    cvSet(masque,cvScalar(0));
    cvAddS(masque,cvScalar(1.0),masque);
    cvNormalize(masque,masque,1.0,0.0,CV_L1);
    cvFilter2D(image_nvg,image_nvg,masque);
    cvNamedWindow("Apres filtre moyen PREDEFINIE",CV_WINDOW_AUTOSIZE);
    cvShowImage("Apres filtre moyen PREDEFINIE",image_nvg);


    cvWaitKey(0);
    cvDestroyWindow("Apres filtre moyen");
    cvDestroyWindow("Apres filtre moyen PREDEFINIE");
    cvDestroyWindow("Avant filtre");
    cvReleaseImage(&image);
    cvReleaseImage(&image_nvg);
    return 0;
}


void filtreMoyen(IplImage *img_nvg){
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

    int somme=0;

    for(int x=1;x<img_nvg->width-1;x++){
            for(int y = 1; y<img_nvg->height-1;y++){

                 scalaire1 = cvGet2D(img_nvg, y, x);
                 scalaire2 = cvGet2D(img_nvg, y+1, x);
                 scalaire3 = cvGet2D(img_nvg, y+1, x+1);
                 scalaire4 = cvGet2D(img_nvg, y, x+1);
                 scalaire5 = cvGet2D(img_nvg, y, x-1);
                 scalaire6 = cvGet2D(img_nvg, y-1, x);
                 scalaire7 = cvGet2D(img_nvg, y-1, x+1);
                 scalaire8 = cvGet2D(img_nvg, y-1, x-1);
                 scalaire9 = cvGet2D(img_nvg, y+1, x-1);

                somme= scalaire1.val[0]+scalaire2.val[0]+scalaire3.val[0]+scalaire4.val[0]+scalaire5.val[0]+scalaire6.val[0]+scalaire7.val[0]+scalaire8.val[0]+scalaire9.val[0];

                scalaire.val[0]= somme/9;

        cvSet2D(img_nvg,y,x, scalaire);


            }
        }
}
