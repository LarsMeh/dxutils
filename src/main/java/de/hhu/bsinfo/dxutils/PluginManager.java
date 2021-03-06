/*
 * Copyright (C) 2018 Heinrich-Heine-Universitaet Duesseldorf, Institute of Computer Science,
 * Department Operating Systems
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package de.hhu.bsinfo.dxutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

/**
 * Load classes from jar files during runtime to implement plugins.
 *
 * @author Stefan Nothaas, stefan.nothaas@hhu.de, 18.02.2019
 */
public class PluginManager {
    private final File m_pluginDirectory;
    private URLClassLoader m_classLoader;

    /**
     * Constructor
     *
     * @param p_path
     *         Path to folder with jar files to load.
     * @throws FileNotFoundException
     *         If plugin folder does not exist or is not a directory.
     */
    public PluginManager(final String p_path) throws FileNotFoundException {
        m_pluginDirectory = new File(p_path);

        if (m_pluginDirectory.exists() && m_pluginDirectory.isDirectory()) {
            load();
        } else {
            throw new FileNotFoundException("Path '" + m_pluginDirectory + "' does not exist or is no directory.");
        }
    }

    /**
     * Get a list of loaded plugin jar files
     *
     * @return List with paths to jar files loaded
     */
    public List<String> getListPlugins() {
        return Arrays.stream(m_classLoader.getURLs()).map(URL::getFile).collect(Collectors.toList());
    }

    /**
     * Get a class by its fully qualified name from the class loader that loaded the plugin jar files.
     *
     * @param p_fullyQualifiedName
     *         Fully qualified name of class.
     * @return Class denoted by the fully qualified name.
     * @throws ClassNotFoundException
     *         If no class with the specified name was found.
     */
    public Class getClassByName(final String p_fullyQualifiedName) throws ClassNotFoundException {
        return Class.forName(p_fullyQualifiedName, true, m_classLoader);
    }

    /**
     * Get a list of all sub-classes of the specified class.
     *
     * @param p_class
     *         Class to get sub-classes of.
     * @param <T>
     *         Type of the class specified as parameter.
     * @return List of sub-classes.
     */
    public <T> List<Class<? extends T>> getAllSubClasses(final Class p_class) {
        List<Class<? extends T>> classes = new ArrayList<>();

        try {
            for (URL url : m_classLoader.getURLs()) {
                File jarFile = new File(url.getFile());

                JarInputStream jarIs = new JarInputStream(new FileInputStream(jarFile));

                while (true) {
                    String classname = getNextClass(jarIs, jarFile);

                    if (classname == null) {
                        break;
                    }

                    if (classname.isEmpty()) {
                        continue;
                    }

                    try {
                        Class<?> clazz = Class.forName(classname, true, m_classLoader);

                        if (p_class.equals(clazz.getSuperclass())) {
                            classes.add((Class<? extends T>) clazz);
                        }
                    } catch (final ClassNotFoundException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }

        return classes;
    }

    /**
     * Load the plugins
     */
    private void load() {
        File[] files = m_pluginDirectory.listFiles();

        assert files != null;

        ArrayList<URL> list = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().endsWith(".jar")) {
                try {
                    list.add(files[i].toURI().toURL());
                } catch (final MalformedURLException e) {
                    throw new IllegalStateException(e);
                }
            }
        }

        m_classLoader = new URLClassLoader(list.toArray(new URL[0]), ClassLoader.getSystemClassLoader());
    }

    /**
     * Get the next class from the jar file.
     *
     * @param p_jarFile
     *         Opened jar file input stream.
     * @param p_jar
     *         Path to the jar file.
     * @return Name of the next class.
     * @throws IOException
     *         If reading jar file failed.
     */
    private static String getNextClass(final JarInputStream p_jarFile, final File p_jar) throws IOException {
        JarEntry jarEntry = p_jarFile.getNextJarEntry();

        if (jarEntry == null) {
            return null;
        }

        if (jarEntry.getName().endsWith(".class")) {
            String classname = jarEntry.getName().replaceAll("/", "\\.");
            classname = classname.substring(0, classname.length() - 6);
            return classname;
        }

        return "";
    }
}
