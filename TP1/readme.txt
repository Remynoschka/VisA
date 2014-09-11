FRANCOIS Remy

PARTIE I

1. * A faire tranquillement avec les autres *

2.Le rapport de Zhang indique que les contraines sont faite de la manière suivante :
vij=    [hi1*hj1;
        hi1*hj2 + hi2*hj1;
        hi2*hj2;
        hi3*hj1+hi1*hj3;
        hi3*hj2+hi2*hj3;
        hi3*hj3]

Du coup, l'implémentation se fait ainsi : 
  a = H(1,i)*H(1,j);
  b = H(1,i)*H(2,j)+H(2,i)*H(1,j);
  c = H(2,i)*H(2,j);
  d = H(3,i)*H(1,j)+H(1,i)*H(3,j);
  e = H(3,i)*H(2,j)+H(2,i)*H(3,j);
  f = H(3,i)*H(3,j);
  v = [a,b,c,d,e,f];


3. En suivant les indications dans le rapport de Zhang, nous calculons la matrice intrinsèque avec le code suivant :
  _v0 = (b(2)*b(4)-b(1)*b(5))/(b(1)*b(3)-b(2)*b(2));
  _lambda = b(6)-(b(4)*b(4)+_v0*(b(2)*b(4)-b(1)*b(5)))/b(1);
  _alpha = sqrt(_lambda/b(1));
  _beta = sqrt((_lambda*b(1))/(b(1)*b(3)-b(2)*b(2)));
  _gamma = -(b(2)*_alpha*_alpha*_beta/_lambda);
  _u0 = _gamma*_v0/_beta - b(4)*_alpha*_alpha/_lambda; 
  
  A =[_alpha,_gamma,_u0;
      0,_beta, _v0;
      0,0,1];



4. On obtient 
 A  =
 
    3498.2767  - 3.13105      336.76583  
    0.           3503.8946    220.1142   
    0.           0.           1.       

Les valeurs obtenues par povray sont : 

 3546.099291   0.000000   320.000000
 0.000000   3546.099291   240.000000
 0.000000   0.000000   1.000000

Les valeurs obtenues par povray et celles calculées sont sensiblements égales (différence dûe à *a trouver*). On peut donc dire que les valeurs calculées sont bonnes.


PARTIE II

1. 

