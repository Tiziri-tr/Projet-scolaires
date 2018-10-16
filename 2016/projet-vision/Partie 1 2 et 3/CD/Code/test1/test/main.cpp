/**********************************************************************************************************/
/********************************* HOUGH STANDARD POUR LES DROITES ****************************************/
/**********************************************************************************************************/

#include <cv.h>
#include <highgui.h>
#include <math.h>

int main(int argc, char** argv)
{
    IplImage* src;
    argv[1] = "D:\\9raya\\M2 SII\\VISION\\tp\\test\\image 1.jpg";
    if( argc == 1 && (src=cvLoadImage(argv[1], 0))!= 0)
    {
        IplImage* dst = cvCreateImage( cvGetSize(src), 8, 1 );
        IplImage* color_dst = cvCreateImage( cvGetSize(src), 8, 3 );
        CvMemStorage* storage = cvCreateMemStorage(0);
        CvSeq* lines = 0;
        int i;
        cvCanny( src, dst, 50, 200, 3 );
        cvCvtColor( dst, color_dst, CV_GRAY2BGR );
#if 1
        lines = cvHoughLines2( dst, storage, CV_HOUGH_STANDARD, 1, CV_PI/180, 100, 0, 0 );

        for( i = 0; i < MIN(lines->total,100); i++ )
        {
            float* line = (float*)cvGetSeqElem(lines,i);
            float rho = line[0];
            float theta = line[1];
            CvPoint pt1, pt2;
            double a = cos(theta), b = sin(theta);
            double x0 = a*rho, y0 = b*rho;
            pt1.x = cvRound(x0 + 1000*(-b));
            pt1.y = cvRound(y0 + 1000*(a));
            pt2.x = cvRound(x0 - 1000*(-b));
            pt2.y = cvRound(y0 - 1000*(a));
            cvLine( color_dst, pt1, pt2, CV_RGB(255,0,0), 3, 8 );
        }
#else
        lines = cvHoughLines2( dst, storage, CV_HOUGH_PROBABILISTIC, 1, CV_PI/180, 50, 50, 10 );
        for( i = 0; i < lines->total; i++ )
        {
            CvPoint* line = (CvPoint*)cvGetSeqElem(lines,i);
            cvLine( color_dst, line[0], line[1], CV_RGB(255,0,0), 3, 8 );
        }
#endif
        cvNamedWindow( "Source", 1 );
        cvShowImage( "Source", src );

        cvNamedWindow( "Hough", 1 );
        cvShowImage( "Hough", color_dst );

        cvWaitKey(0);
    }
}



/*******************************************************************************************************************/
/*********************************** HOUGH STANDARD POUR LES CERCLES **********************************************/
/*******************************************************************************************************************/


//#include <cv.h>
//#include <highgui.h>
//#include <math.h>
//int main(int argc, char** argv)
//{
//Mat img, gray;
//if( argc != 2 && !(img=imread(argv[1], 1)).data)
//return -1;
//cvtColor(img, gray, CV_BGR2GRAY);
// smooth it, otherwise a lot of false circles may be detected
//GaussianBlur( gray, gray, Size(9, 9), 2, 2 );
//vector<Vec3f> circles;
//HoughCircles(gray, circles, CV_HOUGH_GRADIENT,
//2, gray->rows/4, 200, 100 );
//for( size_t i = 0; i < circles.size(); i++ )
//{
//Point center(cvRound(circles[i][0]), cvRound(circles[i][1]));
//int radius = cvRound(circles[i][2]);
// draw the circle center
//circle( img, center, 3, Scalar(0,255,0), -1, 8, 0 );
// draw the circle outline
//circle( img, center, radius, Scalar(0,0,255), 3, 8, 0 );
//}
//namedWindow( "circles", 1 );
//imshow( "circles", img );
//return 0;
//}
