% Lecture des images 

img1 = imread('C:\\Users\\Tiziri\\workspace\\projetVisionFx\\images_test\\test1.tif');
img2 = imread('C:\\Users\\Tiziri\\workspace\\projetVisionFx\\images_test\\test2.tif');
img3 = imread('C:\\Users\\Tiziri\\workspace\\projetVisionFx\\images_test\\test3.tif');
img4 = imread('C:\\Users\\Tiziri\\workspace\\projetVisionFx\\images_test\\test4.tif');
img5 = imread('C:\\Users\\Tiziri\\workspace\\projetVisionFx\\images_test\\test5.tif');
%imgDif = imread('D:\\images_test\\differente.tif');
imgTest = imread('C:/Users/Tiziri/workspace/projetVisionFx/images_test/differente.tif');





%%%%%%%% FOURIER
% img1=im2double(img1);
%  fourier=fft2(img1);
%  retour=fft(fourier);
%  fourier(1 :200,1 :250) = 0;
%  retour=fft(fourier);
%  img1=abs(retour-img1);
%  
%  img2=im2double(img2);
%  fourier=fft2(img2);
%  retour=fft(fourier);
%  fourier(1 :200,1 :250) = 0;
%  retour=fft(fourier);
%  img2=abs(retour-img2);
%  
%  img3=im2double(img3);
%  fourier=fft2(img3);
%  retour=fft(fourier);
%  fourier(1 :200,1 :250) = 0;
%  retour=fft(fourier);
%  img3=abs(retour-img3);
%  
%  img4=im2double(img4);
%  fourier=fft2(img4);
%  retour=fft(fourier);
%  fourier(1 :200,1 :250) = 0;
%  retour=fft(fourier);
%  img4=abs(retour-img4);
%  
%  img5=im2double(img5);
%  fourier=fft2(img5);
%  retour=fft(fourier);
%  fourier(1 :200,1 :250) = 0;
%  retour=fft(fourier);
%  img5=abs(retour-img5);
 
 
tab= zeros(10); 
evaluate = 0;  
decision= '0'; 
maximum = 0;
minimum = 0;
%decisoon = 0 veut dire quoi veut dire faux !! n'appartient pas 

[lignes colonnes]= size(img1);
[l c]= size(imgTest); 

if( l  == lignes & colonnes == c )
    
    tab(1)=sqrt(sumabs((img1-img2).*(img1-img2))); 

    tab(2)=sqrt(sumabs((img1-img3).*(img1-img3)));
    tab(3)= sqrt(sumabs((img1-img4).*(img1-img4)));
    tab(4)= sqrt(sumabs((img1-img5).*(img1-img5)));
    tab(5)= sqrt(sumabs((img2-img3).*(img2-img3)));
    tab(6)= sqrt(sumabs((img2-img4).*(img2-img4)));
    tab(7)= sqrt(sumabs((img2-img5).*(img2-img5)));
    tab(8)= sqrt(sumabs((img3-img4).*(img3-img4)));
    %IMAGE 3 COMME IMAGE REFERENCE ELLE SE RAPPROCHE DE LA MOYENNE DES
    %DISTANCES
    tab(9)= sqrt(sumabs((img3-img5).*(img3-img5)));
    tab(10)= sqrt(sumabs((img4-img5).*(img4-img5)));

    maximum = max(tab);
    minimum = min(tab); 

    % display('différence ');
    % display(maximum- minimum);

    moyenne = 0; 
    for ( i=1 : 10) 

    %     display(tab(i));
        moyenne = moyenne+ tab(i);


    end; 

    moyenne = moyenne/10;
    % display( moyenne);

    evaluate = sqrt(sumabs((img3-imgTest).*(img3-imgTest)));

    if ( evaluate >= minimum(1) & evaluate <= maximum(1)) 

        decision = '1'; 

    end; 



end;
if( l ~= lignes & c ~= colonnes)
    decision = 'Taille Différente!';
end;

fichier = fopen('C:/Users/Tiziri/Documents/MATLAB/resultat.txt', 'w');
% display(decision)
% display(evaluate)
% display(minimum(1))
% display(maximum(1))

fprintf(fichier,'%s\n',decision);
fprintf(fichier,'%i\n',evaluate);
fprintf(fichier,'%i\n',minimum(1));
fprintf(fichier,'%i\n',maximum(1));
fclose(fichier);

% fwrite(fichier,decision)
% fwrite(fichier,evaluate)
% fwrite(fichier,minimum)
% fwrite(fichier,maximum)
