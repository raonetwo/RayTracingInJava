## What is this project
This is the java implementation of the books [Raytracing Series](https://raytracing.github.io/books/RayTracingInOneWeekend.html) . 

Updates will be slow as I only give 1 day (usually Sunday) a week to this project. 
The code was straight forward enough to port naively to Java. I ported the books in a day each, as the content is available in small enough chunks to digest.


We will start with a basic java implementation where we do not care about object creation and will try to adhere to java ways of returning the objects instead of output arguments, 
but as we will move towards optimising the code, we may move away from that style.

While the initial aim of the project was to optimize the implementation of the first book, 
we will for the time being be realigning ourselves to first implement all the books in the [series](https://raytracing.github.io/).

Project Current State:
1. [Book 1: Raytracing in one weekend](https://raytracing.github.io/books/RayTracingInOneWeekend.html) : Implemented Done.
2. [Book 2: Raytracing the next weekend](https://raytracing.github.io/books/RayTracingTheNextWeek.html) : Implementation Done.
3. [Book 3: Raytracing the rest of your life](https://raytracing.github.io/books/RayTracingTheRestOfYourLife.html) : Not Started

## Get Started

### Compile
```
mvn package 
```

### Run
```
java -jar RayTracingInJava-1.0-SNAPSHOT.jar > image.ppm 
```

Note it can take a few hours (may go upto a day depending on your system) to render.
Be patient or create a simpler scene with less samples per pixel, lower size image and lower max depth.
The reason Book 2 initial commit got pushed on Monday instead of Sunday when I actually ported the code over to Java
(Yes it took an entire day to finish the naive code port) was because it took almost 12 hours to render the final scene.
