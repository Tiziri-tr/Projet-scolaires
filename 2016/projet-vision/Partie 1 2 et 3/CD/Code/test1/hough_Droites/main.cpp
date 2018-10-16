#include <iostream>
#include <cv.h>
#include <highgui.h>
#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <vector>

//#define IMAGE_A_TRAITER "D:\\9raya\\M2 SII\\VISION\\tp\\hough_Droites\\test.jpg" // bon seuil 75
//#define IMAGE_A_TRAITER "D:\\9raya\\M2 SII\\VISION\\tp\\hough_Droites\\paint2.png" // bon seuil 80
//#define IMAGE_A_TRAITER "D:\\9raya\\M2 SII\\VISION\\tp\\hough_Droites\\straight_lines.jpeg"  // bon seuil 210
#define IMAGE_A_TRAITER "D:\\9raya\\M2 SII\\VISION\\tp\\hough_Droites\\image 1.jpg" // bon seuil 175
//#define IMAGE_A_TRAITER "D:\\9raya\\M2 SII\\VISION\\tp\\hough_Droites\\image 2.jpg"

#define PERIODICITE 180

struct ElementMatriceHough{
    CvPoint premierPoint;
    CvPoint dernierPoint;
    bool b = false;
};

using namespace std;

int DIAGONALE_IMAGE;
int SEUIL = 175;
const double Pi = 3.141592653589793238462643383279;
vector<int> v_ro;
vector<int> v_theta;
ElementMatriceHough **matriceDeHough;

IplImage *creerAccumulateurDeHough(IplImage *source);
IplImage *appliquerFiltreGaussien(IplImage * source);
IplImage *detectionDeContour(IplImage * source);
IplImage * afficherLignesRouges(IplImage * source);

void creerVecteurDroites(IplImage *accHough);
ElementMatriceHough ** creerMatriceDeHough();

int main(){
    IplImage * image = cvLoadImage(IMAGE_A_TRAITER);
    DIAGONALE_IMAGE = sqrt(pow(image->width,2) + pow(image->height,2));   // Pythagore AB² = AC² + BC²
    IplImage * image_detection_de_contour= cvCreateImage( cvGetSize(image), image->depth,1);

    int flip= 0;
    if(image->origin != IPL_ORIGIN_TL){
        flip = CV_CVTIMG_FLIP;
    }
    cvConvertImage(image,image_detection_de_contour,flip);

    image_detection_de_contour = appliquerFiltreGaussien(image_detection_de_contour);
    image_detection_de_contour = detectionDeContour(image_detection_de_contour);
    matriceDeHough = creerMatriceDeHough();
    IplImage * accumulateurDeHough = creerAccumulateurDeHough(image_detection_de_contour);
    creerVecteurDroites(accumulateurDeHough);
//    printf("taille v_ro: %d\n",v_ro.size());
//    printf("taille v_theta: %d\n",v_theta.size());
    IplImage *imageHough = afficherLignesRouges(image_detection_de_contour);


    cvNamedWindow("Image Originale", CV_WINDOW_AUTOSIZE);
    cvNamedWindow("Image Hough", CV_WINDOW_AUTOSIZE);
    cvNamedWindow("Image resultat", CV_WINDOW_AUTOSIZE);
    cvNamedWindow("Accumulateur De Hough", CV_WINDOW_AUTOSIZE);

    cvShowImage("Image Originale", image);
    cvShowImage("Image resultat", image_detection_de_contour);
    cvShowImage("Accumulateur De Hough", accumulateurDeHough);
    cvShowImage("Image Hough", imageHough);


    cvWaitKey(0);

    cvDestroyWindow("Image Originale");
    cvDestroyWindow("Image resultat");
    cvDestroyWindow("Accumulateur De Hough");
    cvDestroyWindow("Image Hough");

    cvReleaseImage(&image);
    cvReleaseImage(&image_detection_de_contour);
    cvReleaseImage(&accumulateurDeHough);
    cvReleaseImage(&imageHough);

    return 0;
}

IplImage *detectionDeContour(IplImage * source){
    IplImage * dest = cvCloneImage(source);
    cvCanny( source, dest, 50, 200, 3 );

    return dest;
}



IplImage *creerAccumulateurDeHough(IplImage * source){
    CvSize taille;
    taille.height = DIAGONALE_IMAGE * 2;
    taille.width =  PERIODICITE;
    IplImage * houghAcc = cvCreateImage(taille,8,1);
    cvZero(houghAcc);
    CvScalar scalaire;
    CvScalar scalaireAcc;

    double radiantDeTheta;
    double ro;

    for(int x=0;x<source->width;x++){
        for(int y =0; y<source->height;y++){
            scalaire = cvGet2D(source, y, x);
            if(scalaire.val[0]>0){
            /***************************************************/
            /*        REPRESENTATION SUR L4ACCUMULATEUR        */
            /***************************************************/
                for(int theta=0;theta<PERIODICITE;theta++){
                    radiantDeTheta = (Pi * theta / PERIODICITE)- (Pi/2);
                    ro = cos(radiantDeTheta) * x + sin (radiantDeTheta) * y + DIAGONALE_IMAGE;
                    scalaireAcc = cvGet2D(houghAcc, ro, theta);
                    scalaireAcc.val[0]++;
                    cvSet2D(houghAcc,ro,theta, scalaireAcc);
                    // mettre une structure avec 2 cvPoint pour sauvegarder Premier et Dernier Point
                    // avec un booleen/ou cpt pour savoir si c le début ou pas.

//                    printf("ro: %d\n",ro);
//                    printf("theta: %d\n",theta);

                    if( matriceDeHough[(int)ro][(int)theta].b == false){
                        matriceDeHough[(int)ro][(int)theta].premierPoint.x = x;
                        matriceDeHough[(int)ro][(int)theta].premierPoint.y = y;
                        matriceDeHough[(int)ro][(int)theta].b = true;
                    }
                    else{
                        matriceDeHough[(int)ro][(int)theta].dernierPoint.x = x;
                        matriceDeHough[(int)ro][(int)theta].dernierPoint.y = y;
                    }
                }
            }

        }
    }

    return houghAcc;
}

void creerVecteurDroites(IplImage *accHough){
    CvScalar scalaire;
    for(int x=0;x<accHough->height;x++){
        for(int y =0; y<accHough->width;y++){
            scalaire = cvGet2D(accHough, x, y);
            if(scalaire.val[0]>SEUIL){
                v_ro.push_back(x);
                v_theta.push_back(y);
            }
        }
    }

}

//IplImage *dessinerLignesBleues(){
//
//}

ElementMatriceHough ** creerMatriceDeHough(){
    ElementMatriceHough **matrice_hough = new ElementMatriceHough*[DIAGONALE_IMAGE*2];

    for(int i = 0; i< DIAGONALE_IMAGE*2 ; i++){
        matrice_hough[i] = new ElementMatriceHough[PERIODICITE];
        for(int j = 0; j< PERIODICITE; j++){
            matrice_hough[i][j].b = false;
//            printf("%d:%d\n",i,j);
        }
    }
    return matrice_hough;
}

IplImage *afficherLignesRouges(IplImage * source){
    IplImage * dest = cvCreateImage( cvGetSize(source), source->depth,3);
    cvCvtColor(source, dest, CV_GRAY2BGR);

    for(int i= 0; i <v_ro.size(); i++){
        int ro=v_ro[i];
        int theta=v_theta[i];
            cvLine(dest, matriceDeHough[ro][theta].premierPoint,
                           matriceDeHough[ro][theta].dernierPoint,
                           CV_RGB(255,0,0), 3, 8);
    }

    return dest;
}

IplImage *appliquerFiltreGaussien(IplImage * source){
    IplImage * dest = cvCloneImage(source);
    cvSmooth(source, dest, CV_GAUSSIAN, 3, 3, 0, 0);
    return dest;
}

// cv canny detection de contour
// acc matric 2d nb lignes = nb ligne images && nb col = periodicité
// periodicité de la sinuzoide 180 ou 360 ou 200...
/******************************************************************************************************************************/
/******************************************          ETAPES        ************************************************************/
/*****************************************************************************************************************************/

    // rendre en nvg    ------------------------------------------------------------------------------------------------> DONE
    // appliquer la gaussienne pour lisser et diminuer le bruit filtre de gausse ---------------------------------------> DONE
    // appliquer canny pour detecter les contours ----------------------------------------------------------------------> DONE
    // parcourir l'image si on trouver un pixel blanc on appliquer un traitement (a revoir) ----------------------------> DONE
    // boucler sur chaque pixel theta=0 to theta < periodicite theta ++ ------------------------------------------------> DONE
    // calculer a chaque fois le radiant de theta qui est = pi * thetha / periodicité ----------------------------------> DONE
    // lui soustraire pi/2 a radiantTheta   ----------------------------------------------------------------------------> DONE
    // ce radiantThetha l'ongle du point par rapport au repère (new) ---------------------------------------------------> OK
    // dans la meme boucle calculer ro qui est la distance du pixel par rapport au repaire a l'aide de pythagore -------> OK
    // ro = cos (radiantThetha) * x + sin (radiantTheta) * y + D_MAX ----- D_MAX diagonal de l'image -------------------> DONE
    // incrémenter le pixel ro, theta fin boucle theta -----------------------------------------------------------------> DONE
    // definir un seuil -> parcourir Matrice de hough --> si pixel > SEUIL donc il représente une droite ---------------> DONE
    // sauvegarder les coordonnées de ce point la dans un vecteur   ----------------------------------------------------> DONE
    // cree la matrice de hough ----------------------------------------------------------------------------------------> DONE
    // cvconvertcolor (resultatlaplacien, nouvelle image , GRAY2bgr)    ------------------------------------------------> DONE
    // ^puis dessiner les lignes a partir du vecteur conenant les coordonnées   ----------------------------------------> DONE
    //
