import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.NewImage;
import ij.gui.Plot;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

import java.awt.Color;
import java.util.Random;

import javax.swing.JOptionPane;

public class FCM_Visa_FR implements PlugIn
{
    int[] init = new int[3];
    
    Random r = new Random();    
    public int rand(int min, int max) 
    {
        return min + (int)(r.nextDouble() * (max - min));
    }

    //calcul de la distance entre les centroides et data
    private double[][] distanceEntreCentreEtData(int nbpixels,int nbclasses, double[][] c,  double[] red, double[] green, double[] blue)
	{
       //?????
       double[][] distances = new double[nbclasses][nbpixels];
       for (int px = 0 ; px < nbpixels ; px++){
           for (int classe = 0 ; classe < nbclasses ; classe++){
                distances[classe][px] = Math.sqrt(Math.pow(red[px]-c[classe][0],2) + Math.pow(green[px]-c[classe][1],2) + Math.pow(blue[px]-c[classe][2],2));
           }
       }
       return distances;
    }

    // Initialisation des degres d'appartenance
    private double[][] calculDegreAppartenance(int nbpixels,int nbclasses, double m, double[][] Dprev)
	{
     //?????
        double[][] Umat = new double[nbclasses][nbpixels];
        for (int i = 0 ; i < nbclasses ; i++){
            for (int j = 0 ; j < nbpixels ; j++){
                
                double val = 0;
                for (int k = 0 ; k < nbclasses ; k++){
                    val+= Math.pow(Dprev[i][j] / Dprev[k][j], 2.0/(m-1));
                }
                Umat[i][j] = 1 / val;
            }
        }
        return Umat;
    }

    public void run(String arg) 
	{
        final ImageProcessor ip, ipseg;
        ImagePlus imp,impseg;
        IJ.showMessage("Algorithme FCM","If ready, Press OK");
        int height, width,nbclasses,nbpixels,iter,itermax;
        double stab,valeur_seuil,m;

        imp = WindowManager.getCurrentImage();
        ip = imp.getProcessor();

        width = ip.getWidth();
        height = ip.getHeight();

        impseg = NewImage.createImage("Image segmentée par FCM",width,height,1,24,0);
        ipseg = impseg.getProcessor();
		impseg.show();

        String demande = JOptionPane.showInputDialog("Nombre de classes : ");
        nbclasses = Integer.parseInt(demande);

        nbpixels = width * height;

        demande = JOptionPane.showInputDialog("Valeur de m (1 et +) : ");
        m = Double.parseDouble(demande);

        demande = JOptionPane.showInputDialog("Nombre iteration max : ");
        itermax = Integer.parseInt(demande);

        demande = JOptionPane.showInputDialog("Valeur du seuil de stabilite (entre 0.01 et 0.1) : ");
        valeur_seuil = Double.parseDouble(demande);
        valeur_seuil = 0.1;
        
        demande =JOptionPane.showInputDialog("centroide aleatoire ? (0 ou 1) : ");
		int valeur = Integer.parseInt(demande);

        double c[][] = new double[nbclasses][3];
        double Dmat[][] = new double[nbclasses][nbpixels];
        double Umat[][] = new double[nbclasses][nbpixels];
        double red[] = new double[nbpixels];
        double green[] = new double[nbpixels];
        double blue[] = new double[nbpixels];

		double figJ[] = new double[itermax];

        for(int i = 0; i < itermax; i++)
		{
            figJ[i] = 0;
        }

        // Recuperation des donnees images
        for(int j = 0; j < height; j++)
		{
            for(int i = 0; i < width; i++)
			{
                ip.getPixel(i,j,init);
                red[j*width+i] = (double)init[0];
                green[j*width+i] = (double) init[1];          
				blue[j*width+i] = (double)init[2];
            }
        }
        
		int xmin = 0;
		int ymin = 0;
		int rx, ry;	
		int epsilonx,epsilony;
		        
        //initialisation des centroides
		
		//?????
		for (int classe = 0 ; classe < nbclasses ; classe++){
		    int alea = rand(0,height*width);
	        c[classe][0] = red[alea];	
            c[classe][1] = green[alea];
	        c[classe][2] = blue[alea];
		}
       

        // Calcul de distance entre data et centroides
        Dmat = this.distanceEntreCentreEtData(nbpixels, nbclasses, c, red, green, blue);

        //calcul des appartenances
        Umat = this.calculDegreAppartenance(nbpixels, nbclasses, m, Dmat);

        iter = 0;
        stab = 2;
        while ((iter < itermax) && (stab > valeur_seuil)) 
		{
            //???
            for (int k = 0 ; k < nbclasses ; k++){
                double somme = 0;
                double sommeRouge = 0;
                double sommeVert = 0;
                double sommeBleu = 0;                
                for (int i = 0 ; i < nbpixels ; i++){
                    somme += Math.pow(Umat[k][i],m);
                    sommeRouge+=somme*red[i];                    
                    sommeVert+=somme*green[i];                    
                    sommeBleu+=somme*blue[i];
                                                                                                
                }
                c[k][0] = sommeRouge / somme;
                c[k][1] = sommeVert / somme;
                c[k][2] = sommeBleu / somme;                
            }
            double performIndex = 0;
            for (int i = 0 ; i < nbclasses; i++){
                for (int j = 0 ; j < nbpixels ; j++){
                    performIndex += Math.pow(Umat[i][j],m) * Dmat[i][j];
                }
            }
            figJ[iter] = performIndex;
            iter++;
        
        // Affichage de l'image segmentee 
           //????
           for (int x = 0 ; x < width ; x++){
                  for (int y = 0 ; y < height ; y++){
                    int classeApp = 0;
                    double maxApp = 0;
                    for (int classe = 0 ; classe < nbclasses ; classe++){
                        if (Umat[classe][y*width+x] >= maxApp){
                            classeApp = classe;
                            maxApp = Umat[classe][y*width+x];
                        }
                    }
                    int[] rgb = new int[3];
                    rgb[0] = (int) c[classeApp][0];
                    rgb[1] = (int) c[classeApp][1];
                    rgb[2] = (int) c[classeApp][2];
                    ipseg.putPixel(x, y, rgb);                                
               }
           }
       }
         // Affichage indice de performance    
//        Plot plot = new Plot("Performance Index (FCM)","iterations","J(P) value",xplot,yplot);
//        plot.setLineWidth(2);
//        plot.setColor(Color.blue);
//        plot.show();
    }

    public int  IndiceMaxOfArray(double[] array,int val) 
	{
        double max = 0;
        int indice = 0;
        for (int i = 0; i < val; i++)
		{
            if (array[i] > max)
			{ 
                max = array[i];
                indice = i;
            }
        }
        return indice;
    }

}

