package com.bridgelabz.employeepayroll;

import java.io.File;

public class FileUtils
{

        public static boolean deleteFiles(File containsTodelete)
        {
            File[] allCotents=containsTodelete.listFiles();
            if(allCotents!=null)
            {
                for(File file :allCotents)
                {
                    deleteFiles(file);
                }
            }
            return containsTodelete.delete();

        }
}

