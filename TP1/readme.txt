FRANCOIS Remy

PARTIE I

1. Dans sont rapport, Zhang commence par trouver la matrice d'homographie, puis determine la matrice intrinseque et enfin la matrice extrinseque.
Dans le code fourni, ces indications sont verifiees par le code suivant :

----------------Homographie
H = zeros(3, 3, ni);
m = zeros(3, np, ni);
for i = 1:ni
  // Lire les points de l'image
  m(1:2,:,i) = read('points-'+string(i)+'.txt', -1, 2)';
  m(3,:,i) = ones(1, np);
  // Estimer l'homographie entre la mire et l' image
  H(:,:,i) = ZhangHomography(M(sansZ,:), m(:,:,i));
  // Ajouter deux lignes de contraintes dans V
  V = [V; ZhangConstraints(H(:,:,i))];
end

----------------Intrinseque
A = IntrinsicMatrix(b);

----------------Extrinseque
E = zeros(3, 4, ni);
for i = 1:ni
  E(:,:,i) = ExtrinsicMatrix(iA, H(:,:,i));
end


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

Les valeurs obtenues par povray et celles calculées sont sensiblements égales. On peut donc dire que les valeurs calculées sont bonnes.


PARTIE II

1. Nous avons calculée la matrice extrinsèque avec le code suivant :
  lambda = 1/abs(iA*H(:,1));
  lambda = lambda(1);
  r1 = lambda * iA * H(:,1);
  r2 = lambda * iA * H(:,2);
  r3 = CrossProduct(r1,r2);
  t = lambda * iA * H(:,3);

 E  = [r1,r2,r3,t];

2. Les données fournies sont :
Translation = (0, 0, 10000), Rotation = (0, 0, 0)

Et les données que nous avons calculées sont : 

1.           0.0009052  - 0.0006686  - 48.811577  
0.0000377    0.9982951    0.0015769    54.73332   
0.0006696  - 0.0015763    0.9982950    9854.3628  

Les valeurs sont proches de celles fournies, en effet, les colonnes de cette matrice sont : rotation 1, rotation 2 et rotation 3 ainsi que translation.

4.

