FRANCOIS Remy

INTRO
Dans ce TP, on va faire la recherche de points similaires dans un systeme stereoscopique en se servant de tout les points de l'image et en faisant une recherche avec le voisinage de chaque pixel.

PARTIE I
1. Donnees stockees :
- mSSD : 
matrice qui fait la taille de l'image
contient les (valeur de difference) calcules de l'image de gauche pour le decalage actuel. C'est la fonction de cout
- mMinSSD :
contient les valeurs minimales de disparités (valeur de decalage, difference entre les 2 images)

2.
Le pointeur qui permet d'acceder a l'image est calcule en partant 
d'une des valeurs minimale trouvee puis en se mettant au centre de 
la fenetre de correlation.

la fonction minMaxLoc permet d'avoir les valeurs minimum et maximum de l'image ca sert pour la fonction normalize.
Normalize sert a etaler les valeurs en partant du min et du max de maniere a ce que le min devienne 0 et le max 255.


-code c++
Le code pour calculer est  *vals += pow(mLeftGray.row(x + i).at<unsigned char>(y+j) - mRightGray.row(x + i).at<unsigned char>(y+j-iShift), 2.0);
voir formule dans le cours
verif : blabla

PARTIE II

Le code avec celui de gauche est pratiquement le meme, cependant, on applique le decalage sur l'image de gauche, cependant on inverse la facon de se decaler dans l'image pour aller vers la gauche


disparity : disparite gauche et droite fusionnee, ceux qui sont a droite et a gauche
validity : affiche les pixels occupes

Pour voir si la disparite est correctement calculee :
if (dispariteLeft != dispariteLeftCorrespondant || dispariteRight != dispariteRightCorrespondant)
ca vient d'une formule dans le cours

