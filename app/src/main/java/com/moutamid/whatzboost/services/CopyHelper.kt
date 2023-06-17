package com.moutamid.whatzboost.services

import timber.log.Timber
import java.io.*

object CopyHelper {

    @JvmStatic
    fun copyAll(inputFolder: File, outputFolder: File) {
        inputFolder.listFiles()?.forEach {
            copyFile(it, outputFolder)
        }
    }

    @JvmStatic
    fun copyFile(inputFile: File, outputFolder: File) {
        val inputStream: InputStream
        val outputStream: OutputStream
        try { //create output directory if it doesn't exist
            mkdirs(outputFolder)
            inputStream = FileInputStream(inputFile)
            outputStream = FileOutputStream(File(outputFolder, inputFile.name))
            val buffer = ByteArray(1024)
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }
            inputStream.close()
            // write the output file (You have now copied the file)
            outputStream.flush()
            outputStream.close()
        } catch (ex: FileNotFoundException) {
            Timber.i(ex)
        } catch (ex: Exception) {
            Timber.i(ex)
        }
    }

    private fun mkdirs(outputFolder: File) {
        if (!outputFolder.exists()) {
            outputFolder.mkdirs()
        }
    }
}