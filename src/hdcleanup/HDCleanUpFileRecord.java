/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdcleanup;

/**
 *
 * @author paiyeta1
 */
public class HDCleanUpFileRecord {
    
    private String fileName;
    private long filesize;
    private String checksum;
    private String parentFolder;
    private long lastModified;
    private long daysSinceLastModified;
    private boolean deleted;

    public HDCleanUpFileRecord(String fileName, long filesize, String checksum, 
                                String parentFolder, long lastModified, long daysSinceLastModified, boolean deleted) {
        this.fileName = fileName;
        this.filesize = filesize;
        this.checksum = checksum;
        this.parentFolder = parentFolder;
        this.lastModified = lastModified;
        this.daysSinceLastModified = daysSinceLastModified;
        this.deleted = deleted;
    }

    //public void setDeleted(boolean deleted) {
    //    this.deleted = deleted;
    //}
    
    public String getChecksum() {
        return checksum;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFilesize() {
        return filesize;
    }

    public long getLastModified() {
        return lastModified;
    }

    public String getParentFolder() {
        return parentFolder;
    }

    public long getDaysSinceLastModified() {
        return daysSinceLastModified;
    }
    
    
    
}
