#include <iostream>
#include<cv.h>
#include<highgui.h>
#include<math.h>

using namespace std;

int main()
{

    IplImage *img= cvLoadImage("D:\\9raya\\M2 SII\\VISION\\tp\\traitement supplémentaire\\face.png");
    //IplImage *img_nvg= cvCreateImage( cvGetSize(img), img->depth,1);
    IplImage *img_inv=cvCloneImage(img);

CvScalar scalaire;

cvNamedWindow("img", CV_WINDOW_AUTOSIZE);
//cvNamedWindow("nvg", CV_WINDOW_AUTOSIZE);
cvNamedWindow("inv", CV_WINDOW_AUTOSIZE);

scalaire = cvGet2D(img, 180, 100);

//cvConvertImage(img, img_nvg, flip);
cout << scalaire.val[0] << endl;
for (int x=65; x< 150; x++)
    //int x=65; x< 150; x++
{//212
    for (int y=160; y<200; y++)
    //int y=160; y<200; y++)
    {//237

        scalaire = cvGet2D(img, y, x);
        //cout << scalaire.val[0] << endl;
        if( scalaire.val[0] < 249  && scalaire.val[0] > 160){
        //scalaire.val[0] < 250
        //scalaire.val[0]= 255-scalaire.val[0];
        scalaire.val[0]= 153; //bleu
        scalaire.val[1]= 113; // vert
        scalaire.val[2]= 208; //Rouge

        cvSet2D(img_inv,y,x, scalaire);

        }

    }
}






cvShowImage("img", img);
//cvShowImage("nvg", img_nvg);
cvShowImage("inv", img_inv);

cvWaitKey(0);


cvDestroyWindow("img");
//cvDestroyWindow("nvg");
cvDestroyWindow("inv");


cvReleaseImage(&img);
//cvReleaseImage(&img_nvg);

cvReleaseImage(&img_inv);

    return 0;
}
