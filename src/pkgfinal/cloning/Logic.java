package pkgfinal.cloning;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Scalar;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import java.awt.Frame;

/**
 *
 * @author Vaibhav
 */
public class Logic extends Frame {

    public String path = "Image\\";
    public int c;
    public int sensetivity = 3;
    public Label l1;
    public Label l2;
    public Panel panel;
    public Panel center;
    public Button btn1;
    public Button btn2;
    public BufferedImage img1;
    public BufferedImage img2;
    public JLabel pic1 = new JLabel();
    public JLabel pic2 = new JLabel();
    public JLabel result;
    public Panel footer;
    public Button b;
    public Graphics2D g2;
    public int h; // height of images
    public int w; // width of images
    public int width;
    public int height;

    Pattern pattern = Pattern.compile("(.*/)*.+\\.(?i)(png|jpg|gif|bmp|jpeg|tif)$");

    public Logic() {

        panel = new Panel();
        center = new Panel();
        panel.setBackground(Color.white);
        btn1 = new Button("Open Image 1");
        btn2 = new Button("Open Image 2");
        b = new Button("Run");
        footer = new Panel(new GridLayout(1, 3));
        btn1.setSize(150, 150);
        showFileDialogDemo(this);
        runAction();
        l1 = new Label();
        l1.setText(" Choose File 1 ");
        l2 = new Label();
        l2.setText(" Choose File 2 ");
        panel.setLayout(new GridLayout(2, 2));

        //Setting the layout for the Frame
        setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        b.setBounds(150, 150, 150, 150);
        center.add(b);
        panel.add(btn1);
        panel.add(l1);
        panel.add(btn2);
        panel.add(l2);
        width = (int) (0.99 * Toolkit.getDefaultToolkit().getScreenSize().width);
        height = (int) (0.95 * Toolkit.getDefaultToolkit().getScreenSize().height);
        setSize(width, height);
        setBackground(new Color(245, 255, 243));
        //centreWindow(this);
        add(panel, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
        setVisible(true);
    }

    public void showFileDialogDemo(Frame frame) {
        final FileDialog fileDialog = new FileDialog(frame, "Select file");
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // File chooser
                    fileDialog.setVisible(true);
                    if (fileDialog.getFile() != null && Filter(fileDialog.getFile())) {
                        img1 = ImageIO.read(new File(fileDialog.getDirectory() + fileDialog.getFile()));
                        ImageIO.write(img1, "JPG", new File(path, "first.jpg"));
                        l1.setText(fileDialog.getDirectory() + fileDialog.getFile());
                        Image image = img1;
                        int h = img1.getHeight();
                        int w = img1.getWidth();
                        if (h > w) {
                            image = image.getScaledInstance(500 * w / h, 500, java.awt.Image.SCALE_DEFAULT);
                        } else {
                            image = image.getScaledInstance(500, 500 * h / w, java.awt.Image.SCALE_DEFAULT);
                        }
                        pic1.setIcon(new ImageIcon(image));
                        footer.add(pic1, BorderLayout.SOUTH);
                        pic1.updateUI();
                    }
                } catch (IOException ex) {
                    System.out.println("Not correct File 1");
                }
            }
        });
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    fileDialog.setVisible(true);
                    if (fileDialog.getFile() != null && Filter(fileDialog.getFile())) {
                        img2 = ImageIO.read(new File(fileDialog.getDirectory() + fileDialog.getFile()));
                        ImageIO.write(img2, "JPG", new File(path, "second.jpg"));
                        l2.setText(fileDialog.getDirectory() + fileDialog.getFile());
                        Image image = img2;
                        h = img2.getHeight();
                        w = img2.getWidth();
                        if (h > w) {
                            image = image.getScaledInstance(500 * w / h, 500, java.awt.Image.SCALE_DEFAULT);
                        } else {
                            image = image.getScaledInstance(500, 500 * h / w, java.awt.Image.SCALE_DEFAULT);
                        }
                        pic2.setIcon(new ImageIcon(image));
                        footer.add(pic2, BorderLayout.SOUTH);
                        pic2.updateUI();
                    }
                } catch (IOException ex) {
                    System.out.println("Not correct File 2");
                }
            }
        });
    }

    //Filtration with regex pattern
    public boolean Filter(String name) {
        return pattern.matcher(name).matches();
    }

    // Start comaring process
    public void runAction() {
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (img1 == null || img2 == null) {
                    JOptionPane.showMessageDialog(null, "Choose  files!");
                } else if (img1.getHeight() != img2.getHeight() && img1.getWidth() != img2.getWidth()) {
                    JOptionPane.showMessageDialog(null, " The Images have a different size! Choose another Images!");
                } else if (img1 != null && img2 != null) {
                    //System.out.println(".actionPerformed()");
                    compare();
                }
            }
        });
    }

    public void compare() {

        File lib = null;
        String os = System.getProperty("os.name");
        String bitness = System.getProperty("sun.arch.data.model");

        if (os.toUpperCase().contains("WINDOWS")) {
            if (bitness.endsWith("64")) {
                lib = new File("opencv\\build\\java\\x64\\" + System.mapLibraryName("opencv_java249"));
            } else {
                lib = new File("opencv\\build\\java\\x64\\" + System.mapLibraryName("opencv_java249"));
            }
        }

        System.out.println(lib.getAbsolutePath());
        System.load(lib.getAbsolutePath());

        String bookObject = "Image\\first.jpg";
        String bookScene = "Image\\second.jpg";

        System.out.println("Started....");
        System.out.println("Loading images...");
        Mat objectImage = Highgui.imread(bookObject, Highgui.CV_LOAD_IMAGE_COLOR);
        Mat sceneImage = Highgui.imread(bookScene, Highgui.CV_LOAD_IMAGE_COLOR);
//       Detect the key point using surf
//      MatOfKeyPoint-to hold the read key point, through SURF/SIFT
        MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
        // create the object of FeatureDetector
        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);
        System.out.println("Detecting key points...");
//      detect the key point of given image To this method you need to pass a Mat object of source image & empty MatOfKeyPoint object to hold key point        
        featureDetector.detect(objectImage, objectKeyPoints);
        KeyPoint[] keypoints = objectKeyPoints.toArray();
        System.out.println(keypoints);

//      Extractors of keypoint descriptors in OpenCV have wrappers with a common interface that enables
//      you to easily switch between different algorithms solving the same problem
        MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
        DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
        System.out.println("Computing descriptors...");
        descriptorExtractor.compute(objectImage, objectKeyPoints, objectDescriptors);

        // Create the matrix for output image.
        Mat outputImage = new Mat(objectImage.rows(), objectImage.cols(), Highgui.CV_LOAD_IMAGE_COLOR);
        Scalar newKeypointColor = new Scalar(255, 0, 0);

        System.out.println("Drawing key points on object image...");
        Features2d.drawKeypoints(objectImage, objectKeyPoints, outputImage, newKeypointColor, 0);

        // Match object image with the scene image
        MatOfKeyPoint sceneKeyPoints = new MatOfKeyPoint();
        MatOfKeyPoint sceneDescriptors = new MatOfKeyPoint();
        System.out.println("Detecting key points in background image...");
        featureDetector.detect(sceneImage, sceneKeyPoints);
        System.out.println("Computing descriptors in background image...");
        descriptorExtractor.compute(sceneImage, sceneKeyPoints, sceneDescriptors);

        Mat matchoutput = new Mat(sceneImage.rows() * 2, sceneImage.cols() * 2, Highgui.CV_LOAD_IMAGE_COLOR);
        Scalar matchestColor = new Scalar(0, 255, 0);
//      match to image pixel 
        java.util.List<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();
        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
        System.out.println("Matching object and scene images...");
        descriptorMatcher.knnMatch(objectDescriptors, sceneDescriptors, matches, 2);
//      Class for matching keypoint descriptors.\/
        System.out.println("Calculating good match list...");
        LinkedList<DMatch> goodMatchesList = new LinkedList<DMatch>();

        float nndrRatio = 0.7f;///
//  
        for (int i = 0; i < matches.size(); i++) {
            MatOfDMatch matofDMatch = matches.get(i);
            DMatch[] dmatcharray = matofDMatch.toArray();
            DMatch m1 = dmatcharray[0];
            DMatch m2 = dmatcharray[1];

            if (m1.distance <= m2.distance * nndrRatio) {
                goodMatchesList.addLast(m1);

            }
        }

        if (goodMatchesList.size() >= 7) {
            System.out.println("Object Found!!!");

            java.util.List<KeyPoint> objKeypointlist = objectKeyPoints.toList();
            java.util.List<KeyPoint> scnKeypointlist = sceneKeyPoints.toList();

            LinkedList<org.opencv.core.Point> objectPoints = new LinkedList<>();
            LinkedList<org.opencv.core.Point> scenePoints = new LinkedList<>();
            //Get the keypoints from these matches
            for (int i = 0; i < goodMatchesList.size(); i++) {
                objectPoints.addLast(objKeypointlist.get(goodMatchesList.get(i).queryIdx).pt);
                scenePoints.addLast(scnKeypointlist.get(goodMatchesList.get(i).trainIdx).pt);
            }
//          create MatOfPoint2f from list of points
//          convert to type of MatOfPoint2f created from list of points
            MatOfPoint2f objMatOfPoint2f = new MatOfPoint2f();
            objMatOfPoint2f.fromList(objectPoints);
            MatOfPoint2f scnMatOfPoint2f = new MatOfPoint2f();
            scnMatOfPoint2f.fromList(scenePoints);
//Camera calibration and 3D reconstruction (calib3d module) Although we get most of our images in
//a 2D format they do come from a 3D world.
            Mat homography = Calib3d.findHomography(objMatOfPoint2f, scnMatOfPoint2f, Calib3d.RANSAC, 3);

            Mat obj_corners = new Mat(4, 1, CvType.CV_32FC2);// CvType class field
            Mat scene_corners = new Mat(4, 1, CvType.CV_32FC2);
//    This constructor accepts three parameters of integer type representing the number of rows and 
//    columns in a 2D array and the type of the array (that is to be used to store data).
            obj_corners.put(0, 0, new double[]{0, 0});
            obj_corners.put(1, 0, new double[]{objectImage.cols(), 0});
            obj_corners.put(2, 0, new double[]{objectImage.cols(), objectImage.rows()});
            obj_corners.put(3, 0, new double[]{0, objectImage.rows()});
//          Transform the object coordinates to the scene coordinates by the homography matrix
            System.out.println("Transforming object corners to scene corners...");
            Core.perspectiveTransform(obj_corners, scene_corners, homography);

            Mat img = Highgui.imread(bookScene, Highgui.CV_LOAD_IMAGE_COLOR);
//          points of object    and   point of object in scene     top left,right    and bottom right,left
            Core.line(img, new org.opencv.core.Point(scene_corners.get(0, 0)), new org.opencv.core.Point(scene_corners.get(1, 0)), new Scalar(0, 255, 0), 4);
            Core.line(img, new org.opencv.core.Point(scene_corners.get(1, 0)), new org.opencv.core.Point(scene_corners.get(2, 0)), new Scalar(0, 255, 0), 4);
            Core.line(img, new org.opencv.core.Point(scene_corners.get(2, 0)), new org.opencv.core.Point(scene_corners.get(3, 0)), new Scalar(0, 255, 0), 4);
            Core.line(img, new org.opencv.core.Point(scene_corners.get(3, 0)), new org.opencv.core.Point(scene_corners.get(0, 0)), new Scalar(0, 255, 0), 4);

            System.out.println("Drawing matches image...");
            MatOfDMatch goodMatches = new MatOfDMatch();
            goodMatches.fromList(goodMatchesList);

            Features2d.drawMatches(objectImage, objectKeyPoints, sceneImage, sceneKeyPoints, goodMatches, matchoutput, matchestColor, newKeypointColor, new MatOfByte(), 2);

            Highgui.imwrite("Image\\PixelImage9.jpg", outputImage);
            Highgui.imwrite("Image\\Cloningoutput9.jpg", matchoutput);
            Highgui.imwrite("Image\\Image9.jpg", img);
            /////
            BufferedImage imgg;
            try {
                imgg = ImageIO.read(new File(path, "Cloningoutput9.jpg"));

                JLabel picLabel = new JLabel(new ImageIcon(imgg));
                JOptionPane.showMessageDialog(null, picLabel, "Match Pixel", JOptionPane.PLAIN_MESSAGE, null);
            } catch (IOException ex) {
                Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
            }

            // public static void displayImage(Image img)
            BufferedImage imggg;
            try {
                imggg = ImageIO.read(new File(path, "PixelImage9.jpg"));
                JLabel picLabel1 = new JLabel(new ImageIcon(imggg));
                JOptionPane.showMessageDialog(null, picLabel1, "Pixel Point", JOptionPane.PLAIN_MESSAGE, null);

            } catch (IOException ex) {
                Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println("---------------\\//-------------");
            int totalKeypoints = keypoints.length;
            int matchedKeypoints = goodMatchesList.size();
            double percentage = (double) matchedKeypoints / totalKeypoints * 100;

            System.out.println("Total keypoints: " + totalKeypoints);
            System.out.println("Matched keypoints: " + matchedKeypoints);
            System.out.println("Matching percentage: " + percentage + " %");

            JLabel picLabell = new JLabel(percentage + "%");
            JOptionPane.showMessageDialog(null, picLabell, "Clone_Percentage", JOptionPane.PLAIN_MESSAGE, null);

            System.out.println("---------------//\\-------------");

        } else {
            System.out.println("Object Not Found");
        }

        System.out.println("Ended....");

    }

}
