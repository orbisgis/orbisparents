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
 * Copyright (C) 2019-2020 CNRS (Lab-STICC UMR CNRS 6285)
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

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  FileUtils tests
 *  @author Erwan Bocher, CNRS, 2020
 */
public class FileUtilsTest {

    @Test
    public void deleteFiles() throws Exception {
        assertThrows(IOException.class, () -> {
            FileUtils.deleteFiles(null);
        });
        File directory = new File("./target/directory");
        directory.mkdir();
        File tmpFile = File.createTempFile("test", ".txt", directory);
        assertTrue(tmpFile.exists());
        assertTrue(FileUtils.deleteFiles(directory));
        tmpFile = File.createTempFile("test", ".txt", directory);
        assertTrue(tmpFile.exists());
        assertFalse(FileUtils.deleteFiles(directory, true));
        directory = new File("./target/directory");
        directory.mkdir();
        File subdirectory = new File(directory.getAbsolutePath() + File.separator + "subdirectory");
        subdirectory.mkdir();
        tmpFile = File.createTempFile("test", ".txt", subdirectory);
        assertTrue(tmpFile.exists());
        assertTrue(FileUtils.deleteFiles(directory));
        assertFalse(subdirectory.exists());
        assertThrows(IOException.class, () -> {
            File dir = new File("./target/directory");
            dir.mkdir();
            File tmpFileTest = File.createTempFile("test", ".txt", dir);
            assertTrue(tmpFileTest.exists());
            FileUtils.deleteFiles(tmpFileTest);
        });
    }

    @Test
    public void listFiles() throws Exception {
        assertThrows(NullPointerException.class, () -> {
            FileUtils.listFiles(null);
        });
        File directory = new File("./target/directory");
        if (directory.exists()) {
            FileUtils.deleteFiles(directory);
        } else {
            directory.mkdir();
        }
        File tmpFile = File.createTempFile("test", ".txt", directory);
        assertTrue(tmpFile.exists());
        Collection<File> files = FileUtils.listFiles(directory);
        assertEquals(1, files.size());
        assertEquals(tmpFile, files.stream().findFirst().get());
        directory = new File("./target/directory");
        if (directory.exists()) {
            FileUtils.deleteFiles(directory);
        } else {
            directory.mkdir();
        }
        File subdirectory = new File(directory.getAbsolutePath() + File.separator + "subdirectory");
        subdirectory.mkdir();
        tmpFile = File.createTempFile("test", ".txt", subdirectory);
        assertTrue(tmpFile.exists());
        assertTrue(subdirectory.exists());
        files = FileUtils.listFiles(directory);
        assertEquals(1, files.size());
        assertEquals(tmpFile.getName(), files.stream().findFirst().get().getName());
    }

    @Test
    public void listFilesExtension() throws Exception {
        assertThrows(NullPointerException.class, () -> {
            FileUtils.listFiles(null);
        });
        File directory = new File("./target/directory");
        if (directory.exists()) {
            FileUtils.deleteFiles(directory);
        } else {
            directory.mkdir();
        }
        File tmpFile = File.createTempFile("test", ".txt", directory);
        assertTrue(tmpFile.exists());
        Collection<File> files = FileUtils.listFiles(directory, "txt");
        assertEquals(1, files.size());
        assertEquals(tmpFile,files.stream().findFirst().get());
        directory = new File("./target/directory");
        if (directory.exists()) {
            FileUtils.deleteFiles(directory);
        } else {
            directory.mkdir();
        }
        File subdirectory = new File(directory.getAbsolutePath() + File.separator + "subdirectory");
        subdirectory.mkdir();
        tmpFile = File.createTempFile("test", ".txt", subdirectory);
        assertTrue(tmpFile.exists());
        assertTrue(subdirectory.exists());
        files = FileUtils.listFiles(directory, "txt");
        assertEquals(1, files.size());
        assertEquals(tmpFile.getName(), files.stream().findFirst().get().getName());
        files = FileUtils.listFiles(directory, "shp");
        assertTrue(files.isEmpty());

        directory = new File("./target/directory");
        if (directory.exists()) {
            FileUtils.deleteFiles(directory);
        } else {
            directory.mkdir();
        }
        tmpFile = File.createTempFile("test", ".TXT", directory);
        assertTrue(tmpFile.exists());
        files = FileUtils.listFiles(directory, "TXT");
        assertEquals(1, files.size());
        assertEquals(tmpFile.getName(), files.stream().findFirst().get().getName());
    }

    @Test
    public void zipFolder() throws Exception {
        File directory = new File("./target/directory");
        if (directory.exists()) {
            FileUtils.deleteFiles(directory);
        } else {
            directory.mkdir();
        }
        File tmpFile = File.createTempFile("test", ".txt", directory);
        assertTrue(tmpFile.exists());
        AtomicReference<File> outPutZip = new AtomicReference<>(new File("./target/output.zip"));
        outPutZip.get().delete();
        FileUtils.zip(directory, outPutZip.get());
        assertTrue(outPutZip.get().exists());

        assertThrows(IllegalArgumentException.class, () -> {
            outPutZip.set(new File("./target/output.zip"));
            outPutZip.get().delete();
            FileUtils.zip(tmpFile, outPutZip.get());
        });

    }

    @Test
    public void zipUnZipFiles() throws Exception {
        File directory = new File("./target/directory");
        if (directory.exists()) {
            FileUtils.deleteFiles(directory);
        } else {
            directory.mkdir();
        }
        File tmpFile = File.createTempFile("test", ".txt", directory);
        assertTrue(tmpFile.exists());
        File outPutZip = new File("./target/output.zip");
        outPutZip.delete();
        FileUtils.zip(Arrays.asList(new File[]{tmpFile}), outPutZip);
        tmpFile.delete();
        assertTrue(outPutZip.exists());
        FileUtils.unzip(outPutZip, directory);
        assertTrue(tmpFile.exists());
    }


    @Test
    public void gzipUnGZipFile() throws Exception {
        File directory = new File("./target/directory");
        if (directory.exists()) {
            FileUtils.deleteFiles(directory);
        } else {
            directory.mkdir();
        }
        File tmpFile = File.createTempFile("test", ".txt", directory);
        assertTrue(tmpFile.exists());
        File outPutZip = new File("./target/output.gz");
        outPutZip.delete();
        FileUtils.gzip(tmpFile, outPutZip);
        tmpFile.delete();
        assertTrue(outPutZip.exists());
        File outputFile = new File("./target/outputFile_deflate.txt");
        outputFile.delete();
        FileUtils.ungzip(outPutZip, outputFile);
        assertTrue(outputFile.exists());
    }

    @Test
    public void gzipUnGZipFileSubExtension() throws Exception {
        File directory = new File("./target/directory");
        if (directory.exists()) {
            FileUtils.deleteFiles(directory);
        } else {
            directory.mkdir();
        }
        File tmpFile = File.createTempFile("test", ".txt", directory);
        assertTrue(tmpFile.exists());
        File outPutZip = new File("./target/output.txt.gz");
        outPutZip.delete();
        FileUtils.gzip(tmpFile, outPutZip);
        tmpFile.delete();
        assertTrue(outPutZip.exists());
        File outputFile = new File("./target/outputFile_deflate.txt");
        outputFile.delete();
        FileUtils.ungzip(outPutZip, outputFile);
        assertTrue(outputFile.exists());
    }
}
