package com.phenix.plugin.platform

import com.intellij.openapi.project.Project
import java.io.FileNotFoundException

object PlatformCompat {

    /**
     * 是否是Android平台
     * @param it Project
     */
    fun isPlatformAndroid(it: Project): Boolean {
        return try {
            AndroidUtils.getManifestFile(it) != null
        } catch (e: FileNotFoundException) {
            false
        }
    }
}