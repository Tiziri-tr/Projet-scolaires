#include <iostream>
#include <cv.h>
#include <highgui.h>
#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <vector>

//#define IMAGE_A_TRAITER "D:\\9raya\\M2 SII\\VISION\\tp\\hough_Cercles\\test.jpg" // bon seuil 75
//#define IMAGE_A_TRAITER "D:\\9raya\\M2 SII\\VISION\\tp\\hough_Cercles\\paint2.png"
#define IMAGE_A_TRAITER "D:\\9raya\\M2 SII\\VISION\\tp\\hough_Cercles\\cercles.jpg"
//#define IMAGE_A_TRAITER "D:\\9raya\\M2 SII\\VISION\\tp\\hough_Cercles\\straight_lines.jpeg"  // bon seuil 210
//#define IMAGE_A_TRAITER "D:\\9raya\\M2 SII\\VISION\\tp\\hough_Cercles\\image 1.jpg"
//#define IMAGE_A_TRAITER "D:\\9raya\\M2 SII\\VISION\\tp\\hough_Cercles\\image 2.jpg"

#define PERIODICITE 360
#define RAYONS 200
#define TAILLE_MAX 600

struct ElementHough
{
    CvPoint centre;
    int r;
};

using namespace std;

int DIAGONALE_IMAGE;
int SEUIL = 9;
IplImage * Gradiant_X;
IplImage * Gradiant_Y;
IplImage * Gradiant_X_8u;
IplImage * Gradiant_Y_8u;
double gradient[TAILLE_MAX][TAILLE_MAX];
int accumulateurDeHough[TAILLE_MAX][TAILLE_MAX][TAILLE_MAX];
int nbL;
int nbC;
ElementHough temp;
vector <ElementHough> v_cercles;

IplImage *appliquerFiltreGaussien(IplImage * source);
IplImage *detectionDeContour(IplImage * source);
void creerMatriceGradiant(IplImage * source);
void creerAccumulateurDeHough();
IplImage *dessinerCercles(IplImage * source);
void appliquerHough(IplImage *source);


int main(){
    IplImage * image = cvLoadImage(IMAGE_A_TRAITER);
    DIAGONALE_IMAGE = sqrt(pow(image->width,2) + pow(image->height,2));   // Pythagore AB² = AC² + BC²
    nbL = image->height;
    nbC = image->width;
    printf("diagonale =%d\n",DIAGONALE_IMAGE);

    IplImage * image_detection_de_contour= cvCreateImage( cvGetSize(image), image->depth,1);

    int flip= 0;
    if(image->origin != IPL_ORIGIN_TL){
        flip = CV_CVTIMG_FLIP;
    }
    cvConvertImage(image,image_detection_de_contour,flip);
    creerMatriceGradiant(image_detection_de_contour);
    image_detection_de_contour = appliquerFiltreGaussien(image_detection_de_contour);
    image_detection_de_contour = detectionDeContour(image_detection_de_contour);

    creerAccumulateurDeHough();
//    printf("acc creer avec succes...\n");
    appliquerHough(image_detection_de_contour);




    cvNamedWindow("Image Originale", CV_WINDOW_AUTOSIZE);
    cvNamedWindow("Image Filtrée", CV_WINDOW_AUTOSIZE);

    cvShowImage("Image Originale", image);
    cvShowImage("Image Filtrée", image_detection_de_contour);

    cvWaitKey(0);

    cvDestroyWindow("Image Originale");
    cvDestroyWindow("Image Filtrée");

    cvReleaseImage(&image);
    cvReleaseImage(&image_detection_de_contour);


    return 0;
}

IplImage *detectionDeContour(IplImage * source){
    IplImage * dest = cvCloneImage(source);
    cvCanny( source, dest, 50, 200, 3 );

    return dest;
}

IplImage *appliquerFiltreGaussien(IplImage * source){
    IplImage * dest = cvCloneImage(source);
    cvSmooth(source, dest, CV_GAUSSIAN, 3, 3, 0, 0);
    return dest;
}

void creerAccumulateurDeHough(){
    for(int i = 0; i< TAILLE_MAX ; i++){
        for(int j = 0; j< TAILLE_MAX; j++){
            for(int k = 0; k< TAILLE_MAX; k++){
                accumulateurDeHough[i][j][k] = 0;
//                printf(" | %d",accumulateurDeHough[i][j][k]);
            }
        }
    }
//    return acc;
}

void creerMatriceGradiant(IplImage *source){
    Gradiant_X = cvCreateImage(cvGetSize(source),IPL_DEPTH_16S, 1);
    Gradiant_Y = cvCreateImage(cvGetSize(source),IPL_DEPTH_16S, 1);
    Gradiant_X_8u = cvCreateImage(cvGetSize(source),IPL_DEPTH_8U, 1);
    Gradiant_Y_8u = cvCreateImage(cvGetSize(source),IPL_DEPTH_8U, 1);

    cvSobel(source,Gradiant_X, 1, 0, 3);
    cvSobel(source,Gradiant_Y, 0, 1, 3);
    cvConvertScaleAbs(Gradiant_X, Gradiant_X_8u);
    cvConvertScaleAbs(Gradiant_Y, Gradiant_Y_8u);

    cvNamedWindow("derivee par rapport a x", CV_WINDOW_AUTOSIZE);
    cvShowImage("derivee par rapport a x", Gradiant_X_8u);

    cvNamedWindow("derivee par rapport a Y", CV_WINDOW_AUTOSIZE);
    cvShowImage("derivee par rapport a Y", Gradiant_Y_8u);

    CvScalar pixel_gradient_x;
    CvScalar pixel_gradient_y;


      for(int ligne =0; ligne < Gradiant_X->height ; ligne++){
        for(int colonne=0 ; colonne< Gradiant_X->width ; colonne++){
            pixel_gradient_x= cvGet2D(Gradiant_X, ligne, colonne);
            pixel_gradient_y= cvGet2D(Gradiant_Y, ligne, colonne);
            gradient[ligne][colonne] = atan2(pixel_gradient_y.val[0], pixel_gradient_x.val[0]);
        }
    }

}

void appliquerHough(IplImage *source){
    CvScalar scalaire;

    for(int x=0;x<source->width;x++){
        for(int y =0; y<source->height;y++){
            scalaire = cvGet2D(source, y, x);
            if(scalaire.val[0]>0){
                for(int r = 0; r < RAYONS; r++){
                    int a, b;
                    double gradient_y_x = gradient[y][x];
                    a = abs(x - r * cos(gradient_y_x));
                    b = abs(y + r * sin(gradient_y_x));
//                    printf("a:%d b:%d\n",a,b);
                    accumulateurDeHough[a][b][r]++;
                    /*** autre cas ***/
                        accumulateurDeHough[a][b][r]++;
                        a = abs(x + r * cos(gradient_y_x));
                        b = abs(y - r * sin(gradient_y_x));
                        accumulateurDeHough[a][b][r]++;
                        a = abs(x - r * cos(gradient_y_x));
                        b = abs(y - r * sin(gradient_y_x));
                        accumulateurDeHough[a][b][r]++;
                    /****************/
                }
            }
        }
    }
/**************************************************************************************************************************/
    dessinerCercles(source);
/**************************************************************************************************************************/
}

IplImage *dessinerCercles(IplImage * source){
    IplImage * dest = cvCreateImage( cvGetSize(source), source->depth,3);
    cvCvtColor(source, dest, CV_GRAY2BGR);
    int cpt =0;

    for(int i= 0; i <DIAGONALE_IMAGE; i++){
        for(int j= 0; j <DIAGONALE_IMAGE; j++){
            for(int k= 0; k <DIAGONALE_IMAGE; k++){
                if (accumulateurDeHough[i][j][k] > SEUIL){
                    temp.centre.x = i;
                    temp.centre.y = j;
                    temp.r = k;
                    v_cercles.push_back(temp);
                    cpt++;
                }
            }
        }
    }
    for(int i = 0; i < cpt ; i++){
        cvCircle( dest, v_cercles[i].centre, v_cercles[i].r, CV_RGB(255,0,0), 1, 8, 0);
    }
    cvNamedWindow("Hough", CV_WINDOW_AUTOSIZE);
    cvShowImage("Hough", dest);

    return dest;
}

/******************************************************************************************************************************/
/******************************************          FIN        ************************************************************/
/*****************************************************************************************************************************/

    //  calculer le gradiant par raport a x et a y avec cvSobel ---------------------------------------------->
    //  --------------------------------------->
    //  ---------------------------------------------------------------------->
    //  ---------------------------->
    //  ------------------------------------------------>
    //  ---------------------------------->
    //   ---------------------------------------------------------------------------->
    //  --------------------------------------------------->
    //  ------->
    //  ------------------->
    //  ----------------------------------------------------------------->
    //  --------------->
    //   ---------------------------------------------------->
    //  ---------------------------------------------------------------------------------------->
    //     ------------------------------------------------>
    //   ---------------------------------------->
    //  ---------------------------------------->
