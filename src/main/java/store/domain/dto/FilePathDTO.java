package store.domain.dto;

public class FilePathDTO {
    private final String fileName;

    public FilePathDTO(String fileName) {
        this.fileName = fileName;
    }
    public String getFileName() {
        return fileName;
    }

}
