#Task:
     Implement your implementation of a pool of objects that satisfy the following conditions.
     1) Supports thread safety
     2) Implements a pool for the Price Quotation data structure
     - Purchase price
     - Selling price
     - date and time
     - name of the trading instrument
    3) Object pool uses native memory (without heap)
    4) The object pool has a release () method for returning an object to the pool
    5) The object pool has a releaseAll () method for returning all objects with the expectation of completion of their use.      (maybe use timeut and separate traead after timeout reached user NOW )
    6) The object pool has the releaseAllNow () method for immediately returning all objects to the pool.
    7) The object pool has a size
    8) Make sure that your implementation does not crash the program :)
    9) prepare unit tests
    10) prepare a build script and a brief description of working with your pool
    Be mindful of the details.

#Description
    

#Usage
    1 Run main method in class Application from any IDE
    2 * go to the project folder
      * run "mvn clean package" command
      * java -jar ./target/object-pool-1-shaded.jar
      
    IN order to use this example, you can call several function in console, here are them:
     
    `list` - to view all LOCK elements 
    `obtain` - to obtain element from POOL;
    `release` - to release element to POOL;
            after this command you need to enter number of element in "list command" that you need to realese
    `ReleaseAll` - to release element all to POOL;
    `release_all` - to release element all to POOL;
    `release_all_now` - to release element all now to POOL;
    `quit` - to quit.    
    