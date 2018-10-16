#include <iostream>
#include <cv.h>
#include <highgui.h>


using namespace std;

IplImage *img;
IplImage *img_nvg;
IplImage *img_bin;

// Nouvelles structures

IplImage *img_morph;
IplImage *img_temp;
IplConvKernel *noyau;

int seuil;
int inverser;

// Nouveaux parametres

int taille_noyau;
int forme_noyau;
int operateur;

// La fonction de trackbar

void seuillage(int valeur)
{

    int traitement;
    if ( inverser== 0) traitement= CV_THRESH_BINARY;
    else traitement = CV_THRESH_BINARY_INV;

    cvThreshold(img_nvg, img_bin, seuil, 255, traitement);
    cvShowImage("Binarisation", img_bin);

}

// Fonction de trackbar pour afficher le résultat d'une opération morphologique

void appliqueMorph(int valeur)
{


    switch(operateur){

    case 0: // Erosion
        cvErode(img_bin, img_morph, noyau);
        break;

    case 1: // dilatation

        cvDilate(img_bin, img_morph, noyau);
        break;

    case 2: // Ouverture
        cvMorphologyEx(img_bin, img_morph, img_temp, noyau, CV_MOP_OPEN );
        break;

    case 3: // fermeture
        cvMorphologyEx(img_bin, img_morph,img_temp, noyau, CV_MOP_CLOSE);
        break;

    case 4: // gradient
        cvMorphologyEx(img_bin, img_morph, img_temp, noyau, CV_MOP_GRADIENT);
        break;

    case 5: // top hat
        cvMorphologyEx(img_bin, img_morph, img_temp, noyau, CV_MOP_TOPHAT);
        break;

    case 6: // blackhat
        cvMorphologyEx(img_bin, img_morph, img_temp, noyau, CV_MOP_BLACKHAT);
        break;
        }

        cvShowImage("Morphologie", img_morph);
}

// Fonction trackbar pour mettre à jour le noyau

void changeNoyau(int valeur){
// On reprend la taille du noyau:
// si la taille_noyau=0, taille =3
// si taille_noyau =1, taille =5.

int taille=3+ taille_noyau*2;

// On relache le noyau precedent et on cree le nouveau selon la forme
cvReleaseStructuringElement(&noyau);

switch(forme_noyau){

case 0: // Noyau carre
    noyau= cvCreateStructuringElementEx(taille, taille, taille/2, taille/2, CV_SHAPE_RECT);
    break;

case 1: // Noyau cruciforme
noyau= cvCreateStructuringElementEx(taille, taille, taille/2, taille/2, CV_SHAPE_CROSS);
break;

case 2 : // Noyau circulaire
noyau= cvCreateStructuringElementEx(taille, taille, taille/2, taille/2, CV_SHAPE_ELLIPSE);
break;


}

appliqueMorph(valeur);



}

// Creation des trackbar dans la function main
// ajouter 3 trackbar par rapport au programme precedent ( binarisation)

int main()
{

    img= cvLoadImage("D:\\9raya\\M2 SII\\VISION\\tp\\moyen\\lena.png");
    img_nvg= cvCreateImage(cvGetSize(img), img->depth,1);

    img_bin = cvCloneImage(img_nvg);
    img_morph= cvCloneImage(img_bin);
    img_temp = cvCloneImage(img_bin);

    // Controle de l'origine

    inverser=0;

    int flip=0;
    if(img-> origin != IPL_ORIGIN_TL)
    {
        flip= CV_CVTIMG_FLIP;} // Corriger l'origine

        cvConvertImage(img, img_nvg, flip);

        cvNamedWindow("Originale", CV_WINDOW_AUTOSIZE);
        cvNamedWindow("Binarisation", CV_WINDOW_AUTOSIZE);
        cvNamedWindow("Morphologie", CV_WINDOW_AUTOSIZE);

        cvShowImage("Originale", img);

        // initialisation du noyau et des nouveaux parametres

        noyau= cvCreateStructuringElementEx(3,3,1,1, CV_SHAPE_RECT);

taille_noyau= 0;
forme_noyau=0;
operateur=0;

// 1ere binarisation

seuil=127;
cvThreshold(img_nvg, img_bin, seuil, 255, CV_THRESH_BINARY);
// Affichage du resultat

cvShowImage("Binarisation", img_bin);

// Creation des trackbars

cvCreateTrackbar("seuil de binarisation", "Binarisation", &seuil, 255, seuillage);
cvCreateTrackbar("inverser (0=non, 1=oui)", "Binarisation", &inverser, 1, seuillage);
// les nouvelles trackbars

cvCreateTrackbar("taille du noyau", "Morphologie", &taille_noyau, 1, changeNoyau) ;
cvCreateTrackbar("forme du noyau", "Morphologie", &forme_noyau, 2, changeNoyau) ;
cvCreateTrackbar("operateur", "Morphologie", &operateur, 6, appliqueMorph) ;

// 1ere morphologie pour afficher un premier résultat

appliqueMorph(0);
cvWaitKey(0);

// On relache tout

cvDestroyWindow("Binarisation");
cvDestroyWindow("Morphologie");

cvReleaseStructuringElement(&noyau);
cvReleaseImage(&img);
cvReleaseImage(&img_nvg);
cvReleaseImage(&img_bin);
cvReleaseImage(&img_temp);
cvReleaseImage(&img_morph);



    return 0;
}
