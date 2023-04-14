package com.imageuploader.RestApiImageUpload.Config;

import java.util.Arrays;
import java.util.HashSet;

public class AppConstants {
    public static final HashSet<String> ALLOW_EXTENSIONS =
            new HashSet<>(
                    Arrays.asList(
                            ".jpg"
                            , ".jpeg"
                            , ".jfif"
                            , ".pjpeg"
                            , ".pjp"
                            , ".gif"
                            , ".png"
                            , ".svg"));
}
