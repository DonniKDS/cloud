package com.geekbrains.donni.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMessage extends AbstractMessage {

    private String filename;
    private byte[] data;
    private boolean firstData;

    public String getFilename() {
        return filename;
    }

    public byte[] getData() {
        return data;
    }

    public boolean isFirstData() {
        return firstData;
    }

    public FileMessage(Path path) throws IOException {
        filename = path.getFileName().toString();
        data = Files.readAllBytes(path);
        firstData = true;
    }

    public FileMessage(Path path, byte[] data, boolean firstData) throws IOException {
        this.filename = path.getFileName().toString();
        this.data = data;
        this.firstData = firstData;
    }
}
