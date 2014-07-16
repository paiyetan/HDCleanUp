/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdcleanup;

import java.io.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.commons.codec.digest.DigestUtils;
import utilities.DirChooser;

/**
 *
 * @author paiyeta1
 * /*
         * DiscCleanUp
            DiscCleanUpFileRecord
            fileName
            filesize
            checksum
            parentFolder
            dateCreated
            lastModified
            deleted [true|false]

          DiscCleanUpSummary
            rootFolder
            totalFile(s)
            numberDeleted
            numberSkipped
            totalSpaceOccupied
            totalSpaceRecovered
 */
public class HDCleanUp {

    /**
     * 
     *    
     * @throws FileNotFoundException 
     * @throws IOException  
     */
   public void cleanUp() throws FileNotFoundException, IOException {
        //long startTime = new Date().getTime();
        String rootDir;
        //String currentDir;
       DirChooser chooser = new DirChooser("Select an root directory...");
        rootDir = chooser.getOutputDir();
        //currentDir = new File(rootDir).getParent();
        
        Calendar rightNow = Calendar.getInstance(); //returns a Calendar object whose calendar fields have been initialized with the current date and time: 
        PrintWriter logWriter = new PrintWriter("./logs" + File.separator + new File(rootDir).getName() + 
                                                "." + rightNow.getTimeInMillis() + ".log");
        
        //String outputDir;
        //long fileLastModifiedLimit = (long) 2592000000; //1 month == 2.62974e9 milliseconds...//1 mo = 2592000000 ms
        Calendar aMonthAgo = getAMonthAgoCalenderObject();
        long daysBetween = daysBetween(aMonthAgo, rightNow);
        System.out.println(daysBetween + " days set for file last modified time limit...");
        logWriter.println(daysBetween + " days set for file last modified time limit...");
        /*
        chooser = new DirChooser("Select an output directory...", currentDir);
        outputDir = chooser.getOutputDir();
        * 
        */
        String summaryFile = "./etc/out" + File.separator + new File(rootDir).getName() + 
                                                "." + rightNow.getTimeInMillis()  + ".out"; 
        PrintWriter summaryWriter = new PrintWriter(summaryFile);
        
        /*
            recursive grab/locate/identify files in directory/subdirectories
        */
        System.out.println("Cleaning up: " + rootDir);
        logWriter.println("Cleaning up: " + rootDir);
        System.out.println(" Retrieving root-folder/subfolder file(s)...");
        logWriter.println(" Retrieving root-folder/subfolder file(s)...");
        File file  = new File(rootDir);
        LinkedList<File> files = new LinkedList<File>();
        getFilesRecursively(file, files);
        System.out.println("   " + files.size() + " files found...");
        logWriter.println("   " + files.size() + " files found...");
        
            /* 
            - for each file/path{
                    getAbsoluteFileName
                    getFileSize; 
                            //increaseTotalSpaceOccupied (+fileSize)
                    computeChecksum
                    getParentFolder
                    getDateCreated
                    getDateLastModified
                    if((currentDate - dataLastModified) > 1 month [default]){
                            deleteFile
                            setDeleted true
                            increaseNumberOfFilesDeleted (+1)
                            increaseTotalSpaceRecovered (+fileSize)
                    }
            } 
            */
        LinkedList<HDCleanUpFileRecord> cleanUpRecords =  new LinkedList<HDCleanUpFileRecord>();
        int filesDeleted = 0;
        //rootFolder
        //totalFile(s)
        //numberDeleted
        //numberSkipped
        //totalSpaceOccupied
        //totalSpaceRecovered
        long diskSpaceOccuppied = 0;
        long diskSpaceRecovered = 0;
        
        int filesChecked = 0;
        System.out.println(" Checking/computing found file(s) attributes...");
        logWriter.println(" Checking/computing found file(s) attributes...");
        for(File fileFound : files){
            //Found file(s) record attributes...
            String fileName;
            long filesize;
            String checksum;
            String parentFolder;
            //String dateCreated;
            long lastModified;
            long daysSinceLastModified;
            boolean deleted = false; //[true|false];
            // derive attributes...
            fileName = fileFound.getName();
            filesize = fileFound.length();
            checksum = DigestUtils.md5Hex(new FileInputStream(fileFound));
            parentFolder = fileFound.getParent();
            
            lastModified = fileFound.lastModified();
            daysSinceLastModified = TimeUnit.MILLISECONDS.toDays(rightNow.getTimeInMillis()) - 
                    TimeUnit.MILLISECONDS.toDays(lastModified);
            
            diskSpaceOccuppied = diskSpaceOccuppied + filesize;
            
            boolean successful = false;
            if(daysSinceLastModified > daysBetween){
                //delete fileFound
                System.out.println("  deleting file: " + fileName );
                logWriter.println("  deleting file: " + fileName );
                
                //successful = fileToDelete.delete();
                
                /*Here is a workaround for JDK's failure to delete file(s) 
                  by file.delete() (known JDK Bugs - 
                  - JDK-4722539 : File.delete() doesn't work the same between Windows & Unix
                  - JDK-4045014 : In Win95 JDK 1.1.1, File.delete() fails to delete the file from disk).
                * 
                */
                String fileCanonicalPath = fileFound.getCanonicalPath();
                File fileToDelete = new File(fileCanonicalPath);
                fileToDelete.setWritable(true);
                System.gc(); // call the garbage collector
                
                RandomAccessFile tmp_file = null;
                try {
                    tmp_file = new RandomAccessFile(fileToDelete, "rw");
                    System.out.println("   created and opened the file's RandomAccessFile object (tmp_file)...");
                    logWriter.println("   created and opened the file's RandomAccessFile object (tmp_file)...");
                } catch (IOException e) {
                    System.out.println("   Error creating and opening tmp_file (RandomAccessFile object): " + e + ", exiting...");
                    logWriter.println("   Error creating and opening tmp_file (RandomAccessFile object): " + e + ", exiting...");
                    System.exit(0);
                }

                if (fileToDelete.exists()) {
                    System.out.println("   File exists");
                    logWriter.println("   File exists");
                    
                    
                    
                }else {
                    System.out.println("   File does not exist");
                    logWriter.println("   File does not exist");
                }
                /*
                 * 
                 * Right (concurring with description).  On win32 file
                    systems, the semantics are that, if a file is currently
                    open anywhere in the process (or, I think, by any process
                    on the machine), the file cannot be deleted.  There's
                    no efficient way for the runtime to workaround this
                    to provide the same semantics as a UNIX file system.

                    The workaround is to close files before deleting them.
                    WORK AROUND
                    Close the first instance of RandomAccessFile before opening and closing a second instance.
                 */
                tmp_file.close();
                
                if (!fileToDelete.delete()) {
                    System.out.println("   Error deleting file");
                    logWriter.println("   Error deleting file");
                }else {
                    successful = true;
                    filesDeleted++;
                    diskSpaceRecovered = diskSpaceRecovered + filesize;
                    System.out.println ("   Deleted the file");
                    logWriter.println("   Deleted the file");
                }
                                
                deleted = true;
                
            }
            cleanUpRecords.add(new HDCleanUpFileRecord(fileName, filesize, checksum, 
                                                parentFolder, lastModified, daysSinceLastModified, deleted, successful));
            filesChecked++;
            if((filesChecked % 100) == 0){
                System.out.println(" " + filesChecked + " files checked...");
                logWriter.println(" " + filesChecked + " files checked...");
            }
        }
        
        /*
         * rootFolder
            totalFile(s)
            numberDeleted
            numberSkipped
            totalSpaceOccupied
            totalSpaceRecovered
         */
        System.out.println(" Printing CleanUp summary...");
        logWriter.println(" Printing CleanUp summary...");
        printHDCleanUpSummary(rootDir, files.size(), filesDeleted, diskSpaceOccuppied, diskSpaceRecovered,
                                  logWriter, summaryWriter, cleanUpRecords);
        
        logWriter.close();
        summaryWriter.close();           
        
        JOptionPane.showMessageDialog(new JPanel(), "Complete!", "Complete!", JOptionPane.OK_OPTION);
        
        //new JDialog().EXIT_ON_CLOSE
    }
   
    private void getFilesRecursively(File dir, LinkedList<File> files) {
        //throw new UnsupportedOperationException("Not yet implemented");
        File[] dirFiles = dir.listFiles();
        for(File dirFile : dirFiles){
            if(dirFile.isDirectory()){
                getFilesRecursively(dirFile, files);
            } else {
                files.add(dirFile);
            }
        }
    }


    private Calendar getAMonthAgoCalenderObject() {
        Calendar aMonthAgo = Calendar.getInstance();
        aMonthAgo.add(Calendar.DAY_OF_MONTH, -31);
        return aMonthAgo;
    }
    
    private long daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
    }

    private void printHDCleanUpSummary(String rootDir, int numberOfFiles, int filesDeleted, 
            long diskSpaceOccuppied, long diskSpaceRecovered, PrintWriter logWriter, 
                PrintWriter summaryWriter, LinkedList<HDCleanUpFileRecord> cleanUpRecords) {
        //throw new UnsupportedOperationException("Not yet implemented");
        // print file header
        summaryWriter.println("       Root directory: " + rootDir);
        summaryWriter.println("        Total file(s): " + numberOfFiles);
        summaryWriter.println("      Deleted file(s): " + filesDeleted);
        summaryWriter.println(" Total space occupied: " + diskSpaceOccuppied);
        summaryWriter.println("Total space recovered: " + diskSpaceRecovered);
        summaryWriter.println();
        summaryWriter.println("File record(s): ");
        summaryWriter.println("==============");
        
        // print file header (in log file)
        logWriter.println();
        logWriter.println("=====================");
        logWriter.println("       Root directory: " + rootDir);
        logWriter.println("        Total file(s): " + numberOfFiles);
        logWriter.println("      Deleted file(s): " + filesDeleted);
        logWriter.println(" Total space occupied: " + diskSpaceOccuppied);
        logWriter.println("Total space recovered: " + diskSpaceRecovered);
        
        // printAttribute:
        // printAttriubute/table header
        /*
         private String fileName;
        private long filesize;
        private String checksum;
        private String parentFolder;
        private long lastModified;
        private long daysSinceLastModified;
        private boolean deleted;
        * 
        */
        summaryWriter.println("File_name\tFile_size\tChecksum\tParent_folder\tLast_modified\tDays_since_last_modified\tTo_Delete\tSuccessful_Delete");
        // print body....
        for(HDCleanUpFileRecord record : cleanUpRecords){
            summaryWriter.println(record.getFileName() + "\t" +
                                  record.getFilesize() + "\t" +
                                  record.getChecksum() + "\t" +
                                  record.getParentFolder() + "\t" +
                                  new Timestamp(record.getLastModified()).toString() + "\t" +
                                  record.getDaysSinceLastModified() + "\t" +
                                  record.isDeleted() + "\t" +
                                  record.isSuccessfullyDeleted());
        }
              
    }
   
    /*
    private void makeZip(String inFile, String outFile) throws FileNotFoundException {
        //Initialize the io streams
        FileInputStream inStream = new FileInputStream(inFile);
        FileOutputStream outStream = new FileOutputStream(outFile);

        ZipOutputStream zipOutStream = new ZipOutputStream(outStream);
        ZipEntry zipEntry = new ZipEntry(inFile);

        //Create a byte array for writing to the zip file
        File myFile = new File(inFile);
        long myLong = myFile.length();
        Long testLong = new Long(myLong);
        int intTest = testLong.intValue();
        byte[] outByte = new byte[intTest];

        try
        {
        inStream.read(outByte);
        }
        catch(IOException ioe)
        {
        dispFrame.addText("Error - " + ioe);
        }

        //Write to the zip file
        try
        {
        zipOutStream.putNextEntry(zipEntry);
        zipOutStream.write(outByte, 0, outByte.length);
        zipOutStream.close();
        dispFrame.addText("Zip File Created - " + outFile);
        }
        catch(IOException ioe)
        {
        dispFrame.addText("Error - " + ioe);
        }

        //Delete the original file
        //This is a call to your(Jerry's) method
        //When I call your method all tests are passed yet it doesnt work for some unknown reason
        //ie: the method runs till the final boolean check if(!success)
        //
        delete(myFile.getAbsolutePath());
    }
    * 
    */
    
    /**
    * The static method that does the deletion. Invoked by main(), and designed
    * for use by other programs as well. It first makes sure that the
    * specified file or directory is deletable before attempting to delete it.
    * If there is a problem, it throws an IllegalArgumentException.
    */
    /*private void delete(String filename) {
        // Create a File object to represent the filename
        File f = new File(filename);
        // Make sure the file or directory exists and isn't write protected
        if (!f.exists()) fail("Delete: no such file or directory: " + filename);
        if (!f.canWrite()) fail("Delete: write protected: " + filename);
        // If it is a directory, make sure it is empty
        if (f.isDirectory()) {
            String[] files = f.list();
            if (files.length > 0) fail("Delete: directory not empty: " + filename);
        }
        // If we passed all the tests, then attempt to delete it
        boolean success = f.delete();
        // And throw an exception if it didn't work for some (unknown) reason.
        // For example, because of a bug with Java 1.1.1 on Linux,
        // directory deletion always fails
        if (!success) fail("Delete: deletion failed");
    }
    * 
    */
    

    /** A convenience method to throw an exception 
     * @param msg
     * @throws IllegalArgumentException  
     */
    /*protected void fail(String msg) throws IllegalArgumentException {
        throw new IllegalArgumentException(msg);
    }
    * 
    */
      
     public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println();
        System.out.println("Starting...");
        // run disc clean-up
        HDCleanUp cleanUp = new HDCleanUp();
        cleanUp.cleanUp();        
        System.out.println("...Done!");
        
    }

    
}
