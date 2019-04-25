package com.phenix.plugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtilCore
import com.phenix.plugin.platform.AndroidUtils
import com.phenix.plugin.platform.PlatformCompat
import java.io.File
import java.io.FileNotFoundException

/**
 * 打包提示Action
 */
class PackingTipsAction : AnAction() {

    override fun update(e: AnActionEvent) {
        super.update(e)
        // 设置菜单使能
        e.getData(PlatformDataKeys.PROJECT)?.let {
            e.presentation.isEnabled = PlatformCompat.isPlatformAndroid(it) || Constants.IS_FOR_PLATFORM_ANY
        }

    }


    override fun actionPerformed(e: AnActionEvent) {
        val project = e.getData(PlatformDataKeys.PROJECT)
        val sb = StringBuilder("UnKnown Exception...")
        project?.run {
            sb.delete(0, sb.length)
            sb.append("项目:\t").append(name).append('\n')
            sb.append("路径:\t").append(basePath).append('\n')

            try {
                AndroidUtils.getAppPackageName(this)?.let {
                    sb.append("包名:\t").append(it).append('\n')
                }
            } catch (e: FileNotFoundException) {
                sb.append("E::\t").append(e.message).append('\n')
            } catch (e: NullPointerException) {
                sb.append("E::\t").append(e.message).append('\n')
            }

            //配置文件路径，读取后缀.md的所有文件
            val configFileDir = basePath + File.separator + "config" + File.separator /*+ "ReadMe.md"*/
            val configFile = LocalFileSystem.getInstance().findFileByPath(configFileDir)
            configFile?.let {
                sb.append("配置:\t").append(configFileDir).append('\n')
                sb.append("++++++++++++++++++++++++++++++++++++++++++++++++++").append('\n')
                if (it.isDirectory) {
                    val fs = it.children.filter { cf ->
                        cf.extension.equals("md", true)
                    }
                    print("size ${fs.size}")
                    fs.forEach { f ->
                        sb.append(VfsUtilCore.loadText(f)).append('\n')
                        sb.append("**************************************************").append('\n')
                    }
                } else {
                    println("The file of $configFileDir is a folder.")
                }

            }
        }
        Messages.showWarningDialog(project, sb.toString(), "【注意】打包流程")
    }


}

