#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <iostream>
/*#include <cv.h>
#include <highgui.h>*/
#include <math.h>
using namespace std;


MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::on_pushButton_clicked()
{
    system("C:\\Users\\Rachid\\Documents\\test1\\median\\median.exe");

}


/**********************************************************************/



void MainWindow::on_pushButton_2_clicked()
{
    system("C:\\Users\\Rachid\\Documents\\test1\\moyen\\moyen.exe");

}

void MainWindow::on_pushButton_4_clicked()
{
    system("C:\\Users\\Rachid\\Documents\\test1\\hough_Droites\\hough_Droites.exe");

}

void MainWindow::on_pushButton_5_clicked()
{
    system("C:\\Users\\Rachid\\Documents\\test1\\hough_Cercles\\hough_Cercles.exe");

}

void MainWindow::on_pushButton_3_clicked()
{
    system("C:\\Users\\Rachid\\Documents\\test1\\tp6\\tp6.exe");

}

void MainWindow::on_pushButton_7_clicked()
{
    system("C:\\Users\\Rachid\\Documents\\test1\\sup\\sup.exe");

}

void MainWindow::on_pushButton_6_clicked()
{
    system("C:\\Users\\Rachid\\Documents\\test1\\test\\test.exe");

}
