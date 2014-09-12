// -----------------------------------------------------------------------
/// \brief Calcule un terme de contrainte a partir d'une homographie.
///
/// \param H: matrice 3*3 définissant l'homographie.
/// \param i: premiere colonne.
/// \param j: deuxieme colonne.
/// \return vecteur definissant le terme de contrainte.
// -----------------------------------------------------------------------
function v = ZhangConstraintTerm(H, i, j)
  // A modifier!
  a = H(1,i)*H(1,j);
  b = H(1,i)*H(2,j)+H(2,i)*H(1,j);
  c = H(2,i)*H(2,j);
  d = H(3,i)*H(1,j)+H(1,i)*H(3,j);
  e = H(3,i)*H(2,j)+H(2,i)*H(3,j);
  f = H(3,i)*H(3,j);
  v = [a,b,c,d,e,f];
endfunction

// -----------------------------------------------------------------------
/// \brief Calcule deux equations de contrainte a partir d'une homographie
///
/// \param H: matrice 3*3 définissant l'homographie.
/// \return matrice 2*6 definissant les deux contraintes.
// -----------------------------------------------------------------------
function v = ZhangConstraints(H)
  v = [ZhangConstraintTerm(H, 1, 2); ...
    ZhangConstraintTerm(H, 1, 1) - ZhangConstraintTerm(H, 2, 2)];
endfunction

// -----------------------------------------------------------------------
/// \brief Calcule la matrice des parametres intrinseques.
///
/// \param b: vecteur resultant de l'optimisation de Zhang.
/// \return matrice 3*3 des parametres intrinseques.
// -----------------------------------------------------------------------
function A = IntrinsicMatrix(b)
  _v0 = (b(2)*b(4)-b(1)*b(5))/(b(1)*b(3)-b(2)*b(2));
  _lambda = b(6)-(b(4)*b(4)+_v0*(b(2)*b(4)-b(1)*b(5)))/b(1);
  _alpha = sqrt(_lambda/b(1));
  _beta = sqrt((_lambda*b(1))/(b(1)*b(3)-b(2)*b(2)));
  _gamma = -(b(2)*_alpha*_alpha*_beta/_lambda);
  _u0 = _gamma*_v0/_beta - b(4)*_alpha*_alpha/_lambda; 
  
  A =[_alpha,_gamma,_u0;
      0,_beta, _v0;
      0,0,1];
  
endfunction

// -----------------------------------------------------------------------
/// \brief Calcule la matrice des parametres extrinseques.
///
/// \param iA: inverse de la matrice intrinseque.
/// \param H: matrice 3*3 definissant l'homographie.
/// \return matrice 3*4 des parametres extrinseques.
// -----------------------------------------------------------------------
function E = ExtrinsicMatrix(iA, H)
  lambda = 1/abs(iA*H(:,1));
  lambda = lambda(1);
  r1 = lambda * iA * H(:,1);
  r2 = lambda * iA * H(:,2);
  r3 = CrossProduct(r1,r2);
  t = lambda * iA * H(:,3);

 E  = [r1,r2,r3,t];
endfunction

