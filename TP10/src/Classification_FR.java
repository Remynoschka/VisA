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

public class Classification_FR implements PlugIn {
	int[] init = new int[3];

	Random r = new Random();

	public int rand(int min, int max) {
		return min + (int) (r.nextDouble() * (max - min));
	}

	// calcul de la distance entre les centroides et data
	private double[][] distanceEntreCentreEtData(int nbpixels, int nbclasses,
			double[][] c, double[] red, double[] green, double[] blue) {
		// ?????
		double[][] distances = new double[nbclasses][nbpixels];
		for (int px = 0; px < nbpixels; px++) {
			for (int classe = 0; classe < nbclasses; classe++) {
				double r = Math.pow(red[px] - c[classe][0], 2);
				double g = Math.pow(green[px] - c[classe][1], 2);
				double b = Math.pow(blue[px] - c[classe][2], 2);
				distances[classe][px] = r + g + b;
			}
		}
		return distances;
	}

	// Initialisation des degres d'appartenance
	private double[][] calculDegreAppartenanceFCM(int nbpixels, int nbclasses,
			double m, double[][] Dprev) {
		// ?????
		double[][] Umat = new double[nbclasses][nbpixels];
		for (int i = 0; i < nbclasses; i++) {
			for (int j = 0; j < nbpixels; j++) {

				double val = 0;
				for (int k = 0; k < nbclasses; k++) {
					if (Dprev[k][j] > 0) {
						val += Math.pow(Dprev[i][j] / Dprev[k][j],
								2.0 / (m - 1));
					} else {
						val += 1;
					}
				}
				Umat[i][j] = 1 / val;
			}
		}
		return Umat;
	}

	private double[][] calculDegreAppartenanceHCM(int nbpixels, int nbclasses,
			double m, double[][] Dprev) {
		// ?????
		double[][] Umat = new double[nbclasses][nbpixels];
		for (int i = 0; i < nbclasses; i++) {
			for (int j = 0; j < nbpixels; j++) {
				Umat[i][j] = 1;
			}
		}

		for (int i = 0; i < nbclasses; i++) {
			for (int j = 0; j < nbpixels; j++) {
				double val = 1;
				for (int k = 0; k < nbclasses; k++) {
					if (Dprev[k][j] <= Dprev[i][j] && k != i) {
						val = 0;
					}
				}
				Umat[i][j] = val;
			}
		}
		return Umat;
	}

	private double[][] calculDegreAppartenancePCM(int nbpixels, int nbclasses,
			double m, double[][] Dprev) {
		// ?????
		double[][] Umat = new double[nbclasses][nbpixels];
		for (int i = 0; i < nbclasses; i++) {
			for (int j = 0; j < nbpixels; j++) {

				double val = 0;
				for (int k = 0; k < nbclasses; k++) {
					if (Dprev[k][j] > Dprev[i][j] && k != i) {
						val = 1;
					} else {
						val = 0;
					}
				}
				Umat[i][j] = val;
			}
		}
		return Umat;
	}

	private double[][] calculDegreAppartenanceDave(int nbpixels, int nbclasses,
			double m, double[][] Dprev) {
		// ?????
		double[][] Umat = new double[nbclasses + 1][nbpixels];
		for (int i = 0; i < nbclasses; i++) {
			for (int j = 0; j < nbpixels; j++) {

				double val = 0;
				for (int k = 0; k < nbclasses; k++) {
					if (Dprev[k][j] > 0) {
						val += Math.pow(Dprev[i][j] / Dprev[k][j],
								2.0 / (m - 1));
					} else {
						val += 1;
					}
				}
				Umat[i][j] = 1 / val;
			}
		}

		for (int j = 0; j < nbpixels; j++) {
			double val = 0;
			for (int i = 0; i < nbclasses; i++) {
				val += Umat[i][j];
			}
			Umat[nbclasses][j] = 1 - val;
		}

		return Umat;
	}

	public void run(String arg) {
		ImageProcessor ip, ipseg = null;
		ImagePlus imp, impseg = null;
		int height, width, nbclasses, nbpixels, itermax = 0;
		double valeur_seuil, m = 0.0;
		double[] figJ = null;

		imp = WindowManager.getCurrentImage();
		ip = imp.getProcessor();

		width = ip.getWidth();
		height = ip.getHeight();

		impseg = NewImage.createImage("Image segmentée", width, height, 1, 24,
				0);
		ipseg = impseg.getProcessor();
		impseg.show();
		// ask nb classes
		String demande = JOptionPane.showInputDialog("Nombre de classes : ");
		nbclasses = Integer.parseInt(demande);

		nbpixels = width * height;

		// ask nb iterations
		demande = JOptionPane.showInputDialog("Nombre iteration max : ");
		itermax = Integer.parseInt(demande);

		// ask val stabilite
		demande = JOptionPane
				.showInputDialog("Valeur du seuil de stabilite (entre 0.01 et 0.1) : ");
		valeur_seuil = Double.parseDouble(demande);
		valeur_seuil = 0.1;

		// ask centroids
		demande = JOptionPane
				.showInputDialog("centroide aleatoire ? (0 ou 1) : ");
		int valeur = Integer.parseInt(demande);

		// choix de l'algo
		String choiceStr = JOptionPane
				.showInputDialog("Choisir Algorithme :\n1 - FCM\n2 - HCM\n3 - Davé");
		int choice = Integer.parseInt(choiceStr);
		switch (choice) {
		case 1:
			IJ.showMessage("Algorithme FCM", "If ready, Press OK");
			demande = JOptionPane.showInputDialog("Valeur de m (1 et +) : ");
			m = Double.parseDouble(demande);
			figJ = performFCM(ip, ipseg, height, width, nbclasses, nbpixels,
					itermax, valeur_seuil, m, valeur);
			calculatePerfIdx(itermax, figJ);
			break;
		case 2:
			IJ.showMessage("Algorithme HCM", "If ready, Press OK");
			m = 1;
			figJ = performHCM(ip, ipseg, height, width, nbclasses, nbpixels,
					itermax, valeur_seuil, m, valeur);
			calculatePerfIdx(itermax, figJ);
			break;
		case 3:
			IJ.showMessage("Algorithme Davé", "If ready, Press OK");
			demande = JOptionPane.showInputDialog("Valeur de m (1 et +) : ");
			m = Double.parseDouble(demande);
			figJ = performDave(ip, ipseg, height, width, nbclasses, nbpixels,
					itermax, valeur_seuil, m, valeur);
			calculatePerfIdxDave(itermax, figJ);
			break;
		default:
			IJ.showMessage("Mauvaise entrée");

			break;

		}

	}

	private void calculatePerfIdx(int itermax, double[] figJ) {
		// Affichage indice de performance
		double[] xplot = new double[itermax];
		double[] yplot = new double[itermax];
		for (int i = 0; i < itermax; i++) {
			xplot[i] = (double) i;
			yplot[i] = (double) figJ[i];
		}
		Plot plot = new Plot("Performance Index (FCM)", "iterations",
				"J(P) value", xplot, yplot);
		plot.setLineWidth(2);
		plot.setColor(Color.blue);
		plot.show();
	}

	private void calculatePerfIdxPCM(int itermax, double[] figJ) {
		// Affichage indice de performance
		double[] xplot = new double[itermax];
		double[] yplot = new double[itermax];
		for (int i = 0; i < itermax; i++) {
			xplot[i] = (double) i;
			yplot[i] = (double) figJ[i];
		}
		Plot plot = new Plot("Performance Index (FCM)", "iterations",
				"J(P) value", xplot, yplot);
		plot.setLineWidth(2);
		plot.setColor(Color.blue);
		plot.show();
	}

	private void calculatePerfIdxDave(int itermax, double[] figJ) {
		// Affichage indice de performance
		double[] xplot = new double[itermax];
		double[] yplot = new double[itermax];
		for (int i = 0; i < itermax; i++) {
			xplot[i] = (double) i;
			yplot[i] = (double) figJ[i];
		}

		Plot plot = new Plot("Performance Index (Davé)", "iterations",
				"J(P) value", xplot, yplot);
		plot.setLineWidth(2);
		plot.setColor(Color.blue);
		plot.show();
	}

	private double[] performFCM(ImageProcessor ip, ImageProcessor ipseg,
			int height, int width, int nbclasses, int nbpixels, int itermax,
			double valeur_seuil, double m, int valeur) {
		int iter;
		double stab;
		double c[][] = new double[nbclasses][3];
		double Dmat[][] = new double[nbclasses][nbpixels];
		double Umat[][] = new double[nbclasses][nbpixels];
		double red[] = new double[nbpixels];
		double green[] = new double[nbpixels];
		double blue[] = new double[nbpixels];

		double figJ[] = new double[itermax];

		for (int i = 0; i < itermax; i++) {
			figJ[i] = 0;
		}

		// Recuperation des donnees images
		getImageData(ip, height, width, red, green, blue);

		int xmin = 0;
		int ymin = 0;
		int rx, ry;
		int epsilonx, epsilony;

		// initialisation des centroides

		// ?????
		initCentroids(height, width, nbclasses, valeur, c, red, green, blue);

		// Calcul de distance entre data et centroides
		Dmat = this.distanceEntreCentreEtData(nbpixels, nbclasses, c, red,
				green, blue);

		// calcul des appartenances
		Umat = this.calculDegreAppartenanceFCM(nbpixels, nbclasses, m, Dmat);

		iter = 0;
		stab = 2;
		while ((iter < itermax) && (stab > valeur_seuil)) {
			// ???
			for (int k = 0; k < nbclasses; k++) {
				double somme = 0;
				double sommeRouge = 0;
				double sommeVert = 0;
				double sommeBleu = 0;
				for (int i = 0; i < nbpixels; i++) {
					somme += Math.pow(Umat[k][i], m);
					sommeRouge += Math.pow(Umat[k][i], m) * red[i];
					sommeVert += Math.pow(Umat[k][i], m) * green[i];
					sommeBleu += Math.pow(Umat[k][i], m) * blue[i];

				}
				if (somme > 0) {
					c[k][0] = sommeRouge / somme;
					c[k][1] = sommeVert / somme;
					c[k][2] = sommeBleu / somme;
				}
			}
			// Calcul de distance entre data et centroides
			Dmat = this.distanceEntreCentreEtData(nbpixels, nbclasses, c, red,
					green, blue);

			// calcul des appartenances
			Umat = this
					.calculDegreAppartenanceFCM(nbpixels, nbclasses, m, Dmat);

			double performIndex = 0;
			for (int i = 0; i < nbclasses; i++) {
				for (int j = 0; j < nbpixels; j++) {
					performIndex += Math.pow(Umat[i][j], m) * Dmat[i][j];
				}
			}
			figJ[iter] = performIndex;
			iter++;

			// Affichage de l'image segmentee
			// ????
			displayImage(ipseg, height, width, nbclasses, c, Umat);
		}
		return figJ;
	}

	private double[] performDave(ImageProcessor ip, ImageProcessor ipseg,
			int height, int width, int nbclasses, int nbpixels, int itermax,
			double valeur_seuil, double m, int valeur) {
		int iter;
		double stab;
		double c[][] = new double[nbclasses][3];
		double Dmat[][] = new double[nbclasses][nbpixels];
		double Umat[][] = new double[nbclasses][nbpixels];
		double red[] = new double[nbpixels];
		double green[] = new double[nbpixels];
		double blue[] = new double[nbpixels];

		double figJ[] = new double[itermax];

		for (int i = 0; i < itermax; i++) {
			figJ[i] = 0;
		}

		// Recuperation des donnees images
		getImageData(ip, height, width, red, green, blue);

		int xmin = 0;
		int ymin = 0;
		int rx, ry;
		int epsilonx, epsilony;

		// initialisation des centroides

		// ?????
		initCentroids(height, width, nbclasses, valeur, c, red, green, blue);

		// Calcul de distance entre data et centroides
		Dmat = this.distanceEntreCentreEtData(nbpixels, nbclasses, c, red,
				green, blue);

		// calcul des appartenances
		Umat = this.calculDegreAppartenanceDave(nbpixels, nbclasses, m, Dmat);

		iter = 0;
		stab = 2;
		while ((iter < itermax) && (stab > valeur_seuil)) {
			// ???
			for (int k = 0; k < nbclasses; k++) {
				double somme = 0;
				double sommeRouge = 0;
				double sommeVert = 0;
				double sommeBleu = 0;
				for (int i = 0; i < nbpixels; i++) {
					somme += Math.pow(Umat[k][i], m);
					sommeRouge += Math.pow(Umat[k][i], m) * red[i];
					sommeVert += Math.pow(Umat[k][i], m) * green[i];
					sommeBleu += Math.pow(Umat[k][i], m) * blue[i];

				}
				if (somme > 0) {
					c[k][0] = sommeRouge / somme;
					c[k][1] = sommeVert / somme;
					c[k][2] = sommeBleu / somme;
				}
			}
			// Calcul de distance entre data et centroides
			Dmat = this.distanceEntreCentreEtData(nbpixels, nbclasses, c, red,
					green, blue);

			// calcul des appartenances
			Umat = this.calculDegreAppartenanceDave(nbpixels, nbclasses, m,
					Dmat);

			double performIndex = 0;
			for (int i = 0; i < nbclasses; i++) {
				for (int j = 0; j < nbpixels; j++) {
//					System.out.println(nbpixels);
//					System.out.println(Umat[i][j]);
//					System.out.println(Dmat[i][j]);
//					System.out.println(Umat[nbclasses][j]);
//					System.out.println(i+"/"+j);
//					System.out.println("=====================================");
					performIndex += (Math.pow(Umat[i][j], m) * Dmat[i][j])
							+ Umat[nbclasses][j]
							* (Math.pow(Dmat[i][j], 2) / (nbclasses * nbpixels));
				}
			} 
			figJ[iter] = performIndex;
			iter++;

			// Affichage de l'image segmentee
			// ????
			displayImage(ipseg, height, width, nbclasses, c, Umat);
		}
		return figJ;
	}

	private double[] performHCM(ImageProcessor ip, ImageProcessor ipseg,
			int height, int width, int nbclasses, int nbpixels, int itermax,
			double valeur_seuil, double m, int valeur) {
		int iter;
		double stab;
		double c[][] = new double[nbclasses][3];
		double Dmat[][] = new double[nbclasses][nbpixels];
		double Umat[][] = new double[nbclasses][nbpixels];
		double red[] = new double[nbpixels];
		double green[] = new double[nbpixels];
		double blue[] = new double[nbpixels];

		double figJ[] = new double[itermax];

		for (int i = 0; i < itermax; i++) {
			figJ[i] = 0;
		}

		// Recuperation des donnees images
		getImageData(ip, height, width, red, green, blue);

		int xmin = 0;
		int ymin = 0;
		int rx, ry;
		int epsilonx, epsilony;

		// initialisation des centroides

		// ?????
		initCentroids(height, width, nbclasses, valeur, c, red, green, blue);

		// Calcul de distance entre data et centroides
		Dmat = this.distanceEntreCentreEtData(nbpixels, nbclasses, c, red,
				green, blue);

		// calcul des appartenances
		Umat = this.calculDegreAppartenanceHCM(nbpixels, nbclasses, m, Dmat);

		iter = 0;
		stab = 2;
		while ((iter < itermax) && (stab > valeur_seuil)) {
			// ???
			for (int k = 0; k < nbclasses; k++) {
				double somme = 0;
				double sommeRouge = 0;
				double sommeVert = 0;
				double sommeBleu = 0;
				for (int i = 0; i < nbpixels; i++) {
					somme += Math.pow(Umat[k][i], m);
					sommeRouge += Math.pow(Umat[k][i], m) * red[i];
					sommeVert += Math.pow(Umat[k][i], m) * green[i];
					sommeBleu += Math.pow(Umat[k][i], m) * blue[i];

				}
				if (somme > 0) {
					c[k][0] = sommeRouge / somme;
					c[k][1] = sommeVert / somme;
					c[k][2] = sommeBleu / somme;
				}
			}
			// Calcul de distance entre data et centroides
			Dmat = this.distanceEntreCentreEtData(nbpixels, nbclasses, c, red,
					green, blue);

			// calcul des appartenances
			Umat = this
					.calculDegreAppartenanceHCM(nbpixels, nbclasses, m, Dmat);

			double performIndex = 0;
			for (int i = 0; i < nbclasses; i++) {
				for (int j = 0; j < nbpixels; j++) {
					performIndex += Math.pow(Umat[i][j], m) * Dmat[i][j];
				}
			}
			figJ[iter] = performIndex;
			iter++;

			// Affichage de l'image segmentee
			// ????
			displayImage(ipseg, height, width, nbclasses, c, Umat);
		}
		return figJ;
	}

	/**
	 * Ecrit dans des tableaux les valeurs de chaque composantes
	 * 
	 * @param ip
	 *            : l'image dont on recupere les donnees
	 * @param height
	 *            : hauteur de l'image
	 * @param width
	 *            : largeur de l'image
	 * @param red
	 *            : tableau dans lequel sera stocke le rouge
	 * @param green
	 *            : tableau dans lequel sera stocke le vert
	 * @param blue
	 *            : tableau dans lequel sera stocke le bleu
	 */
	private void getImageData(ImageProcessor ip, int height, int width,
			double[] red, double[] green, double[] blue) {
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				ip.getPixel(i, j, init);
				red[j * width + i] = (double) init[0];
				green[j * width + i] = (double) init[1];
				blue[j * width + i] = (double) init[2];
			}
		}
	}

	/**
	 * Initialise les centroids
	 * 
	 * @param height
	 * @param width
	 * @param nbclasses
	 * @param valeur
	 *            : 1 si aleatoire
	 * @param c
	 *            : tableau des centroids
	 * @param red
	 *            : tableau de rouge
	 * @param green
	 *            : tableau de vert
	 * @param blue
	 *            : tableau de bleu
	 */
	private void initCentroids(int height, int width, int nbclasses,
			int valeur, double[][] c, double[] red, double[] green,
			double[] blue) {
		for (int classe = 0; classe < nbclasses; classe++) {
			if (valeur == 1) {
				int alea = rand(0, height * width);
				c[classe][0] = red[alea];
				c[classe][1] = green[alea];
				c[classe][2] = blue[alea];
			} else {
				c[classe][0] = init[0];
				c[classe][1] = init[1];
				c[classe][2] = init[2];
			}
		}
	}

	/**
	 * Affiche l'image segmentee
	 * 
	 * @param ipseg
	 *            : l'image segmentee
	 * @param height
	 * @param width
	 * @param nbclasses
	 * @param c
	 *            : tableau des centroids
	 * @param Umat
	 *            : tableau des degres d'appartenance
	 */
	private void displayImage(final ImageProcessor ipseg, int height,
			int width, int nbclasses, double[][] c, double[][] Umat) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int classeApp = 0;
				double maxApp = 0;
				for (int classe = 0; classe < nbclasses; classe++) {
					if (Umat[classe][y * width + x] >= maxApp) {
						classeApp = classe;
						maxApp = Umat[classe][y * width + x];
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

	public int IndiceMaxOfArray(double[] array, int val) {
		double max = 0;
		int indice = 0;
		for (int i = 0; i < val; i++) {
			if (array[i] > max) {
				max = array[i];
				indice = i;
			}
		}
		return indice;
	}

}
