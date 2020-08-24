/*
 * Bundle Commons is part of the OrbisGIS platform
 *
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285
 * Equipe DECIDE
 * UNIVERSITÉ DE BRETAGNE-SUD
 * Institut Universitaire de Technologie de Vannes
 * 8, Rue Montaigne - BP 561 56017 Vannes Cedex
 *
 * Commons is distributed under LGPL 3 license.
 *
 * Copyright (C) 2018-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Commons is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Commons is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Commons. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.commons.utilities;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Utilities for file(s) and directory
 *
 * @author Erwan Bocher, CNRS, 2020
 */
public class FileUtils {

    /**
     * Use this method to delete all files in directory recursively without
     * deleting the root directory
     *
     * @param directory the directory
     * @return true if the directory already exists
     * @throws IOException
     */
    public static boolean deleteFiles(File directory) throws IOException {
        return deleteFiles(directory, false);
    }

    /**
     * Use this method to delete all files in directory recursively. The root
     * directory can be deleted
     *
     * @param directory the directory
     * @param delete true to delete the root directory
     * @return true if the directory already exists
     * @throws IOException
     */
    public static boolean deleteFiles(File directory, boolean delete) throws IOException {
        if (directory == null) {
            throw new IOException("The directory cannot be null");
        }
        if (!directory.isDirectory()) {
            throw new IOException("The input path must be a directory");
        }
        Path pathToBeDeleted = directory.toPath();
        try (Stream<Path> walk = Files.walk(pathToBeDeleted)) {
            if (delete) {
                walk.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } else {
                walk.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .filter(item -> !item.toPath().equals(pathToBeDeleted))
                        .forEach(File::delete);
            }
        }
        return Files.exists(pathToBeDeleted);
    }

    /**
     * List all files recursively
     *
     * @param directory the directory
     * @return a collection of java.io.File with the matching files
     * @throws IOException
     */
    public static Collection<File> listFiles(File directory) throws IOException {
        return org.apache.commons.io.FileUtils.listFiles(directory, null, true);
    }

    /**
     * List all files recursively which match an array of extensions.
     *
     * @param directory the directory
     * @param extensions - an array of extensions, ex. {"java","xml"}. If this parameter is null, all files are returned.
     * @return a collection of java.io.File with the matching files
     * @throws IOException
     */
    public static Collection<File> listFiles(File directory, String... extensions) throws IOException {
        return org.apache.commons.io.FileUtils.listFiles(directory, extensions, true);
    }

    /**
     * List all files in the directory and subdirectories
     *
     * @param directory the directory
     * @param extensions - an array of extensions, ex. {"java","xml"}. If this parameter is null, all files are returned.
     * recursive - if true all subdirectories are searched as well
     * @return a collection of java.io.File with the matching files
     * @throws IOException
     */
    public static Collection<File> listFiles(File directory, String[] extensions, boolean recursive) throws IOException {
        return org.apache.commons.io.FileUtils.listFiles(directory, extensions, false);
    }

    /**
     * Zips the specified files in a destination directory
     *
     * @param filesToZip a list of files
     * @param outZipFile destination file
     * @throws IOException
     */
    public static void zip(Collection<File> filesToZip, File outZipFile) throws IOException, ArchiveException {
        if (filesToZip == null) {
            throw new IOException("The file to zip cannot be null");
        }
        if (outZipFile == null) {
            throw new IOException("The destination file to zip cannot be null");
        } else if (outZipFile.exists()) {
            throw new IOException("The destination file to zip already exist");
        }
        if (!org.apache.commons.io.FilenameUtils.isExtension(outZipFile.getAbsolutePath(), "zip")) {
            throw new IOException("The extension of the file to zip must be .zip");
        }
        OutputStream archiveStream = new FileOutputStream(outZipFile);
        ArchiveOutputStream archive = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, archiveStream);

        for (File file : filesToZip) {
            ZipArchiveEntry entry = new ZipArchiveEntry(file.getName());
            archive.putArchiveEntry(entry);
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
            IOUtils.copy(input, archive);
            input.close();
            archive.closeArchiveEntry();
        }
        archive.finish();
        archiveStream.close();
    }

    /**
     * Zips a specified folder
     *
     * @param fileToZip a file or a directory
     * @param outZipFile destination file
     * @throws IOException
     */
    public static void zip(File fileToZip, File outZipFile) throws IOException, ArchiveException {
        if (fileToZip == null || !fileToZip.exists()) {
            throw new IOException("The file to zip cannot be null");
        }
        if(!fileToZip.isDirectory()){
            throw new IllegalArgumentException("The source to zip must be a directory");
        }
        if (outZipFile == null) {
            throw new IOException("The destination file to zip cannot be null");
        } else if (outZipFile.exists()) {
            throw new IOException("The destination file to zip already exist");
        }
        if(outZipFile.isDirectory()){
            throw new IllegalArgumentException("The destination to zip must be a file");
        }
        if (!org.apache.commons.io.FilenameUtils.isExtension(outZipFile.getAbsolutePath(), "zip")) {
            throw new IOException("The extension of the file to zip must be .zip");
        }
        if (fileToZip.equals(outZipFile)) {
            throw new IOException("The destination file must be different than the input file");
        }
        OutputStream archiveStream = new FileOutputStream(outZipFile);
        ArchiveOutputStream archive = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, archiveStream);

        Collection<File> fileList = FileUtils.listFiles(fileToZip, null, true);

        for (File file : fileList) {
            String entryName = getEntryName(fileToZip, file);
            ZipArchiveEntry entry = new ZipArchiveEntry(entryName);
            archive.putArchiveEntry(entry);

            BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));

            IOUtils.copy(input, archive);
            input.close();
            archive.closeArchiveEntry();
        }
        archive.finish();
        archiveStream.close();
    }

    /**
     * Remove the leading part of each entry that contains the source directory name
     *
     * @param source the directory where the file entry is found
     * @param file   the file that is about to be added
     * @return the name of an archive entry
     * @throws IOException if the io fails
     */
    private static String getEntryName(File source, File file) throws IOException {
        int index = source.getAbsolutePath().length() + 1;
        String path = file.getCanonicalPath();
        return path.substring(index);
    }

    /**
     * Unzip to a directory
     *
     * @param zipFile the zipped file
     * @param directory the directory to unzip the file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void unzip(File zipFile, File directory) throws IOException {
        if (directory == null) {
            throw new IOException("The directory cannot be null");
        }
        if (!directory.isDirectory()) {
            throw new IOException("The input path must be a directory");
        }

        if (zipFile == null) {
            throw new IOException("The file to zip cannot be null");
        } else if (!zipFile.exists()) {
            throw new IOException("The file to zip cannot doesn't exist");
        }

        if (!org.apache.commons.io.FilenameUtils.getExtension(zipFile.getAbsolutePath()).equalsIgnoreCase( "zip")) {
            throw new IOException("The extension of the file to zip must be .zip");
        }
        if (zipFile.equals(directory)) {
            throw new IOException("The destination file must be different than the zip file");
        }
        ZipInputStream zis = null;
        try {
            byte[] buffer = new byte[1024];
            zis = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = newFile(directory, zipEntry);
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        } finally {
            if (zis != null) {
                zis.close();
            }
        }
    }

    /**
     *
     * @param destinationDir
     * @param zipEntry
     * @return
     * @throws IOException
     */
    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target directory: " + zipEntry.getName());
        }
        return destFile;
    }

    /**
     * Compress a file to a gzip archive
     *
     * @param sourceFile the input file
     * @param outGZFile destination file
     * @throws IOException
     */
    public static void gzip(File sourceFile, File outGZFile) throws IOException {
        if (sourceFile == null|| !sourceFile.exists()) {
            throw new IOException("The file to zip cannot be null");
        }
        if(sourceFile.isDirectory()){
            throw new IOException("The source to compress must be a file");
        }
        if (outGZFile == null) {
            throw new IOException("The destination file to zip cannot be null");
        } else if (outGZFile.exists()) {
            throw new IOException("The destination file to zip already exist");
        }
        if (!org.apache.commons.io.FilenameUtils.isExtension(outGZFile.getAbsolutePath(), "gz")) {
            throw new IOException("The extension of the file to zip must be .zip");
        }
        OutputStream archiveStream =null;
        GZIPOutputStream gzipOutputStream = null;
        try {
            archiveStream = new FileOutputStream(outGZFile);
            gzipOutputStream = new GZIPOutputStream(archiveStream);
            IOUtils.copy(new FileInputStream(sourceFile), gzipOutputStream);
            gzipOutputStream.close();
        } finally {
            if (gzipOutputStream != null) {
                gzipOutputStream.close();
            }
            if (archiveStream != null) {
                archiveStream.close();
            }
        }
    }

    /**
     * Uncompress a gzip archive to a directory
     *
     * @param sourceFile the input gzip
     * @param targetFileName the outputfile
     * @throws IOException
     */
    public static void ungzip(File sourceFile,  File targetFileName) throws IOException, CompressorException {
        if (sourceFile == null|| !sourceFile.exists()) {
            throw new IOException("The file to zip cannot be null");
        }
        if(sourceFile.isDirectory()){
            throw new IOException("The source to compress must be a file");
        }
        if (targetFileName == null) {
            throw new IOException("The output file to uncompress cannot be null or must be a file");
        }
        if (targetFileName.exists()) {
            throw new IOException("The output file already exists");
        }

        if (!org.apache.commons.io.FilenameUtils.getExtension(sourceFile.getAbsolutePath()).equalsIgnoreCase( "gz")) {
            throw new IOException("The extension of the file to uncompress must be .gz");
        }
        GZIPInputStream gzip =null;
        FileOutputStream out=null;
        try {
            FileInputStream archiveStream = new FileInputStream(sourceFile);
            gzip = new GZIPInputStream(archiveStream);
            out = new FileOutputStream(targetFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = gzip.read(buffer, 0, 1024)) != -1) {
                out.write(buffer, 0, length);
            }
        }
        finally {
            if (gzip != null) {
                gzip.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
