package pl.edu.pw.ee.aisd2025zex5.cmdargs;

public class ProgramConfig {
    private String sourcePath;
    private String destPath;
    private Mode mode;
    private int maxLength = 1;
    
    public enum Mode{
        COMP,
        DECOMP
    }
    public String getSourcePath(){
        return sourcePath;
    }
    public void setSourcePath(String path){
        sourcePath = path;
    }
    public String getDestPath(){
        return destPath;
    }
    public void setDestPath(String path){
        destPath = path;
    }
    public Mode getMode(){
        return mode;
    }
    public void setMode(Mode mode){
       this.mode = mode;
    }
    public int getMaxLength(){
        return maxLength;
    }
    public void setMaxLength(int maxLength){
        this.maxLength = maxLength;
    }
}
