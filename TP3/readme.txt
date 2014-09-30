.raw().at(i)

FRANCOIS Remy

PARTIE III

1. Donnees stockees :
- mSSD : 
matrice qui fait la taille de l'image
contient les (valeur de difference) calcules de l'image de gauche pour le decalage actuel. C'est la fonction de cout
- mMinSSD :
contient les valeurs minimales de disparités (valeur de decalage)

2.
Le pointeur qui permet d'acceder a l'image est calcule en partant 
d'une des valeurs minimale trouvee puis en se mettant au centre de 
la fenetre de correlation.


-code c++
Le code pour calculer est  *vals += pow(mLeftGray.row(x + i).at<unsigned char>(y+j) - mRightGray.row(x + i).at<unsigned char>(y+j-iShift), 2.0);
voir formule dans le cours
verif : blabla

PARTIE II
Quoi dire... xD
