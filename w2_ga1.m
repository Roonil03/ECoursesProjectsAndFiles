frame1 = imread("Rt9Frame1.png");
frame2 = imread("Rt9Frame2.png");

gray1 = im2gray(frame1);
gray2 = im2gray(frame2);

opticFlow = opticalFlowFarneback;
estimateFlow(opticFlow, gray1);
flow = estimateFlow(opticFlow, gray2);
mask = flow.Magnitude > 1;
mask = bwareaopen(mask, 500);
se = strel('disk', 20, 0);
mask = imclose(mask, se);

maskedFrame = frame2; maskedFrame(repmat(~mask, [1 1 3])) = 0;
imshow(frame2)
hold on
plot(flow,"DecimationFactor",[15 15],"ScaleFactor",7)
hold off
figure
imshow(maskedFrame)