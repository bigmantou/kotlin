/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.test;

import com.intellij.util.PathUtil;
import com.sun.istack.internal.NotNull;

import java.io.File;

public class AndroidSdkUtils {
    private AndroidSdkUtils() {}

    public static String getPlatformFolderInAndroidSdk() {
        return getAndroidSdkRoot() + "/platforms";
    }

    public static String getAndroidAvdRoot() {
        String androidEmulatorRoot = getAndroidSdkRoot() + "/compiler_box_test_avd";
        new File(androidEmulatorRoot).mkdirs();
        return androidEmulatorRoot;
    }

    @NotNull
    public static String getPlatformToolsFolderInAndroidSdk() {
        return getAndroidSdkRoot() + "/platform-tools";
    }

    @NotNull
    public static String getToolsFolderInAndroidSdk() {
        return getAndroidSdkRoot() + "/tools";
    }

    @NotNull
    public static String getEmulatorFolderInAndroidSdk() {
        return getAndroidSdkRoot() + "/emulator";
    }

    public static String getAndroidSdkRoot() {
        return AndroidSdkUtils.getAndroidSdkSystemIndependentPath();
    }

    @NotNull
    public static File findAndroidSdk() {
        String androidSdkProp = System.getProperty("android.sdk");
        File androidSdkDir = androidSdkProp == null ? null : new File(androidSdkProp);
        if (androidSdkDir == null || !androidSdkDir.isDirectory()) {
            throw new RuntimeException(
                    "Unable to get a valid path from 'android.sdk' property (" +
                    androidSdkProp +
                    "), please point it to the android SDK location");
        }
        return androidSdkDir;
    }

    public static String getAndroidSdkSystemIndependentPath() {
        return PathUtil.toSystemIndependentName(findAndroidSdk().getAbsolutePath());
    }
}