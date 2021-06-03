package com.bridgelabz.employeepayroll;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;

public class NIOFileApiTest
{
    private static String HOME=System.getProperty("user.home");
    private static String PLAY_WITH_NIO="TempPlayGround";

    @Test
    public void givenPathWhenCheckedThenConfirm() throws IOException {
        //check file exist
        Path homePath= Paths.get(HOME);
        Assertions.assertTrue(Files.exists(homePath));
        //Delete File and Check File Exist Or Not
        Path playPath=Paths.get(HOME+"/"+PLAY_WITH_NIO);
        if(Files.exists(playPath))
        {
            FileUtils.deleteFiles(playPath.toFile());
        }
        Assertions.assertTrue(Files.notExists(playPath));

        //Create directory
        Files.createDirectories(playPath);
        Assertions.assertTrue(Files.exists(playPath));

        //create file
        IntStream.range(1,10).forEach(cntr->{
            Path tempFile=Paths.get(playPath+"/temp"+cntr);
            Assertions.assertTrue(Files.notExists(tempFile));
            try
            {
                Files.createFile(tempFile);
            }
            catch (IOException e)
            {
                Assertions.assertTrue(Files.exists(tempFile));
            }
            Assertions.assertTrue(Files.exists(tempFile));
        });
    }
}
