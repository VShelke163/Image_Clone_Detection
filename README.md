# Image_Clone_Detection
 Detect Clone Image Percentage

It appears that the provided code is a Java program that performs image comparison using the SIFT(Scale-Invariant Feature Transform) Algorithm And OpenCV library. The program allows you to compare two images and find similarities between them using keypoint detection and matching techniques. The comments in the code suggest that it detects keypoints in both images, computes descriptors for those keypoints, matches the descriptors between the two images, and then uses homography to transform the keypoints from the object image to the scene image to find the location of the object in the scene.

Here is a brief summary of the main components and functionalities of the code:

1. The `Logic` class extends `Frame` and seems to be responsible for the main logic of the program.

2. The `compare()` method is where the image comparison process takes place. It loads the two input images, detects keypoints using SURF (Speeded-Up Robust Features) feature detector, computes descriptors for the keypoints, matches keypoints between the object and scene images, and then performs a series of calculations to find a good match.

3. The program uses the OpenCV library for computer vision tasks, such as keypoint detection, descriptor extraction, and feature matching.

4. The `runAction()` method appears to handle user interaction with the program, specifically when a button (`b`) is pressed to initiate the image comparison process.

5. The program seems to display the results of the comparison, including images with keypoints and matched features, as well as the percentage of matching keypoints between the two images.

To fully understand and use the code, you will need to have the required libraries, particularly OpenCV, properly configured in your Java project. Additionally, you should ensure that the necessary image files (`first.jpg` and `second.jpg`) are available in the specified directories (`Image\\first.jpg` and `Image\\second.jpg`).

It's important to note that without access to the actual images and the corresponding OpenCV library, I cannot fully test the code or guarantee its functionality. If you intend to use this code, make sure you have the necessary prerequisites set up correctly.

If you have any specific questions or need further clarifications about any part of the code, please feel free to ask.
